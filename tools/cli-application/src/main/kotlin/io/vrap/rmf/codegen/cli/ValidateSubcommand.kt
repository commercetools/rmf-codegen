package io.vrap.rmf.codegen.cli

import com.commercetools.rmf.validators.ValidatorSetup
import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.OasGeneratorComponent
import io.vrap.rmf.codegen.di.OasGeneratorModule
import io.vrap.rmf.codegen.di.OasProvider
import io.vrap.rmf.raml.model.RamlDiagnostic
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.RamlModelResult
import io.vrap.rmf.raml.model.modules.Api
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

enum class DiagnosticSeverity(val severity: String, val value: Int) {
    INFO("info", Diagnostic.INFO),
    WARN("warn", Diagnostic.WARNING),
    ERROR("error", Diagnostic.ERROR);

    companion object {
        const val VALID_VALUES = "info, warn, error"
    }
}

@CommandLine.Command(name = "validate", description = ["Allows to verify if a raml spec is valid according to CT guideline"])
class ValidateSubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Parameters(index = "0", description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    @CommandLine.Option(names = ["-r", "--ruleset"], description = ["Ruleset configuration"], required = false)
    var rulesetFile: Path? = null

    @CommandLine.Option(names = ["-t", "--temp"], description = ["Temporary folder"], required = false)
    var tempFile: Path? = null

    @CommandLine.Option(names = ["-w", "--watch"], description = ["Watches the files for changes"], required = false)
    var watch: Boolean = false

    @CommandLine.Option(names = ["-f", "--format"], required = false)
    var formatter: OutputFormat = OutputFormat.CLI

    @CommandLine.Option(names = ["-o", "--outputTarget"], required = false)
    var outputTarget: Path? = null

    @CommandLine.Option(names = ["-s", "--severity"], description = ["Diagnostic severity", "Valid values: ${DiagnosticSeverity.VALID_VALUES}"])
    var checkSeverity: DiagnosticSeverity = DiagnosticSeverity.ERROR

    lateinit var modelBuilder: RamlModelBuilder

    lateinit var diagnosticFormatter: FormatPrinter

    override fun call(): Int {
        val tmpDir = tempFile ?: Paths.get(".tmp")
        diagnosticFormatter = diagnosticFormatter(formatter)
        modelBuilder = setupValidators()
        val res = safeRun { validate(tmpDir)}
        if (watch) {
            val watchDir = ramlFileLocation.toRealPath().toAbsolutePath().parent

            val source = Observable.create<DirectoryChangeEvent> { emitter ->
                run {
                    val watcher = DirectoryWatcher.builder()
                            .path(watchDir)
                            .listener { event ->
                                when (event.eventType()) {
                                    DirectoryChangeEvent.EventType.CREATE,
                                    DirectoryChangeEvent.EventType.MODIFY,
                                    DirectoryChangeEvent.EventType.DELETE -> {
                                        if (event.path().startsWith(tmpDir)) {
                                            return@listener
                                        }
                                        val json = event.path().toString().endsWith("json")
                                        val raml = event.path().toString().endsWith("raml")
                                        val yml = event.path().toString().endsWith("yml")
                                        val yaml = event.path().toString().endsWith("yaml")
                                        if (json || raml || yaml || yml) {
                                            emitter.onNext(event)
                                        }
                                    }
                                    else -> {
                                    }
                                }
                            }
                            .build()
                    watcher.watchAsync()
                }
            }

            source.subscribeOn(Schedulers.io())
                    .throttleLast(1, TimeUnit.SECONDS)
                    .blockingSubscribe(
                            {
                                InternalLogger.debug("Consume ${it.eventType().name.lowercase(Locale.getDefault())}: ${it.path()}")
                                safeRun { validate(tmpDir) }
                            },
                            {
                                InternalLogger.error(it)
                            }
                    )
        }
        return res
    }

    fun validate(tmpDir: Path): Int {
        var fileLocation = ramlFileLocation
        if (ramlFileLocation.toString().endsWith(".raml").not()) {
            val apiProvider = OasProvider(ramlFileLocation)

            val ramlConfig = CodeGeneratorConfig(
                outputFolder = tmpDir,
            )
            val generatorModule = OasGeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes)
            val generatorComponent = OasGeneratorComponent(generatorModule, RamldocModelModule)
            generatorComponent.generateFiles()

            fileLocation = tmpDir.resolve("api.raml")
        }
        val fileURI = URI.createURI(fileLocation.toUri().toString())
        val modelResult = modelBuilder.buildApi(fileURI)
//        if (tmpDir.exists()) {
//            tmpDir.toFile().deleteRecursively()
//        }
        val output = diagnosticFormatter.print(fileURI, modelResult)

        outputTarget?.let {
            InternalLogger.info("Writing to ${it.toAbsolutePath().normalize()}")
            Files.write(it.toAbsolutePath().normalize(), output.toByteArray(StandardCharsets.UTF_8))
        } ?: run {
            println(output)
        }

        return modelResult.validationResults.any { result -> result.severity >= checkSeverity.value }.let { b -> if(b) 1 else 0 }
    }

    private fun setupValidators(): RamlModelBuilder {
        val ruleset = rulesetFile?.toFile()?.inputStream() ?: ValidateSubcommand::class.java.getResourceAsStream("/ruleset.xml")
        return RamlModelBuilder(ValidatorSetup.setup(ruleset))
    }

    companion object {
        fun diagnosticFormatter(printer: OutputFormat): FormatPrinter {
            return when (printer) {
                OutputFormat.CLI -> CliFormatPrinter()
                OutputFormat.MARKDOWN -> MarkdownFormatPrinter()
                OutputFormat.JSON -> TODO()
            }
        }
    }

    interface FormatPrinter {
        fun print(fileURI: URI, result: RamlModelResult<Api>): String
    }

    class CliFormatPrinter: FormatPrinter {
        override fun print(fileURI: URI, result: RamlModelResult<Api>): String {
            val validationResults = result.validationResults
            var output = ""
            if (validationResults.isNotEmpty()) {
                val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
                val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
                val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

                if (errors.isNotEmpty()) output += "🛑 ${errors.size} Error(s) found validating ${fileURI.toFileString()}:\n${errors.joinToString("\n") { it.message }}"
                if (warnings.isNotEmpty()) output += "⚠️ ${warnings.size} Warnings(s) found validating ${fileURI.toFileString()}:\n${warnings.joinToString("\n") { it.message }}"
                if (infos.isNotEmpty()) output += "✅ ${infos.size} Info(s) found validating ${fileURI.toFileString()}:\n${infos.joinToString("\n") { it.message }}"

                return output
            }
            return "✅ Specification at ${fileURI.toFileString()} is valid."
        }
    }

    class MarkdownFormatPrinter: FormatPrinter {
        override fun print(fileURI: URI, result: RamlModelResult<Api>): String {
            val validationResults = result.validationResults
            var output = ""
            if (validationResults.isNotEmpty()) {
                val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
                val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
                val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

                if (errors.isNotEmpty()) output += """
                        |<details>
                        |<summary>🛑  ${errors.size} Error(s) found validating ${fileURI.toFileString()}</summary>
                        |
                        |${errors.joinToString("\n") { "${it.message} (${it.location}:${it.line}:${it.column})" }}
                        |</details>
                    """.trimMargin()
                if (warnings.isNotEmpty()) output += """
                        |<details>
                        |<summary>⚠️  ${warnings.size} Warnings(s) found validating ${fileURI.toFileString()}</summary>
                        |
                        |${warnings.joinToString("\n") { "${it.message} (${it.location}:${it.line}:${it.column})" }}
                        |</details>
                    """.trimMargin()
                if (infos.isNotEmpty()) output += """
                        |<details>
                        |<summary>✅  ${infos.size} Info(s) found validating ${fileURI.toFileString()}</summary>
                        |
                        |${infos.joinToString("\n") { "${it.message} (${it.location}:${it.line}:${it.column})" }}
                        |</details>
                    """.trimMargin()

                return output
            }
            return "Specification at ${fileURI.toFileString()} is valid."
        }
    }
}


