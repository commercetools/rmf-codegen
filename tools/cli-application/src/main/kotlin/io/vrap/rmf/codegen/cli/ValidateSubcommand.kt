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
import io.vrap.rmf.nodes.antlr.NodeTokenProvider
import io.vrap.rmf.raml.model.RamlDiagnostic
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.RamlModelResult
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.validation.Source
import io.vrap.rmf.raml.validation.Violation
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import picocli.CommandLine
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.relativeTo
import kotlin.io.path.relativeToOrSelf
import kotlin.io.path.toPath

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

    @CommandLine.Option(names = ["-r", "--ruleset"], description = ["Ruleset configuration"])
    var rulesetFile: Path? = null

    @CommandLine.Option(names = ["-t", "--temp"], description = ["Temporary folder"])
    var tempFile: Path? = null

    @CommandLine.Option(names = ["-w", "--watch"], description = ["Watches the files for changes"])
    var watch: Boolean = false

    @CommandLine.Option(names = ["-f", "--format"], description = ["Specifies the output format","Valid values: ${OutputFormat.VALID_VALUES}"] )
    var outputFormat: OutputFormat = OutputFormat.CLI

    @CommandLine.Option(names = ["-l", "--link-base"])
    var linkBase: java.net.URI? = null

    @CommandLine.Option(names = ["-lf", "--link-format"], description = ["Specifies the link format","Valid values: ${LinkFormat.VALID_VALUES}"] )
    var linkFormat: LinkFormat = LinkFormat.CLI

    @CommandLine.Option(names = ["-o", "--outputTarget"])
    var outputTarget: Path? = null

    @CommandLine.Option(names = ["-s", "--severity"], description = ["Diagnostic severity", "Valid values: ${DiagnosticSeverity.VALID_VALUES}"])
    var checkSeverity: DiagnosticSeverity = DiagnosticSeverity.ERROR

    @CommandLine.Option(names = ["--list-rules"], description = ["Show all rules"])
    var listRules: Boolean = false

    lateinit var modelBuilder: RamlModelBuilder

    private fun linkURI(): java.net.URI {
        var uri = linkBase ?: java.net.URI.create(".")
        if (uri.toString().endsWith("/").not()) {
            uri = java.net.URI.create("$uri/")
        }
        return uri
    }

    override fun call(): Int {
        if (listRules) {
            println("Available validators:")
            println(ValidatorSetup.allValidatorRules().joinToString("\n"));
            return 0
        }
        val tmpDir = tempFile?.toAbsolutePath()?.normalize() ?: Paths.get(".tmp")
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
        var fileLocation = ramlFileLocation.toAbsolutePath().normalize()
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
        val filePath =  Path(fileURI.toFileString()).parent
        val diagnosticFormatter = diagnosticFormatter(outputFormat, filePath, linkURI())
        val output = diagnosticFormatter.print(fileURI, modelResult)

        outputTarget?.let {
            if (modelResult.validationResults.any()) {
                InternalLogger.info("Writing to ${it.toAbsolutePath().normalize()}")
                Files.write(it.toAbsolutePath().normalize(), output.toByteArray(StandardCharsets.UTF_8))
            }
        } ?: run {
            println(output)
        }

        return modelResult.validationResults.any { result -> result.severity >= checkSeverity.value }.let { b -> if(b) 1 else 0 }
    }

    private fun setupValidators(): RamlModelBuilder {
        val ruleset = rulesetFile?.toFile()?.inputStream() ?: ValidateSubcommand::class.java.getResourceAsStream("/ruleset.xml")
        return RamlModelBuilder(ValidatorSetup.setup(ruleset))
    }

    private fun diagnosticFormatter(printer: OutputFormat, filePath: Path, linkUri: java.net.URI): FormatPrinter {
        val linkFormatter = linkFormatter(linkFormat, filePath, linkUri)
        return when (printer) {
            OutputFormat.CLI -> CliFormatPrinter(linkFormatter)
            OutputFormat.MARKDOWN -> MarkdownFormatPrinter(linkFormatter)
            OutputFormat.JSON -> TODO()
        }
    }

    private fun linkFormatter(formatter: LinkFormat, filePath: Path, linkBase: java.net.URI): LinkFormatter {
        return when (formatter) {
            LinkFormat.CLI -> CliLinkFormatter(filePath, linkBase)
            LinkFormat.GITHUB -> GithubLinkFormatter(filePath, linkBase)
        }
    }

    interface FormatPrinter {
        val linkFormatter: LinkFormatter
        fun print(fileURI: URI, result: RamlModelResult<Api>): String
    }

    interface LinkFormatter {
        val filePath: Path
        val linkBase: java.net.URI

        fun format(path: Path): String {
            return path.relativeTo(filePath).toLink(linkBase).toString()
        }

        fun format(diagnostic: RamlDiagnostic): String

        fun RamlDiagnostic.toLocation(filePath: Path): Path {
            return java.net.URI.create(this.location).toPath().relativeToOrSelf(filePath)
        }

        fun Path.toLink(baseUri: java.net.URI): java.net.URI {
            return baseUri.resolve(this.toString())
        }
    }

    class CliLinkFormatter(override val filePath: Path, override val linkBase: java.net.URI): LinkFormatter {
        override fun format(diagnostic: RamlDiagnostic): String {
            return "${diagnostic.toLocation(filePath).toLink(linkBase)}:${diagnostic.line}:${diagnostic.column}"
        }
    }

    class GithubLinkFormatter(override val filePath: Path, override val linkBase: java.net.URI): LinkFormatter {
        override fun format(diagnostic: RamlDiagnostic): String {
            return "${diagnostic.toLocation(filePath).toLink(linkBase)}#L${diagnostic.line}${suffix(diagnostic)}"
        }

        private fun suffix(diagnostic: RamlDiagnostic): String {
            val location = diagnostic.toLocation(filePath)
            val violation = diagnostic.data.filterIsInstance<Violation>().find { true }
            if (violation?.`object` != null) {
                val nodeTokenProvider = EcoreUtil.getExistingAdapter(violation.`object`, NodeTokenProvider::class.java) as NodeTokenProvider
                val nodeToken = nodeTokenProvider.stop
                val t = java.net.URI.create(nodeToken.location).toPath().relativeToOrSelf(filePath)
                if (t == location && nodeToken.line > diagnostic.line) {
                    return "-L${nodeToken.line}"
                }
            }
            return ""
        }
    }

    class CliFormatPrinter(override val linkFormatter: LinkFormatter): FormatPrinter {

        override fun print(fileURI: URI, result: RamlModelResult<Api>): String {
            val validationResults = result.validationResults
            var output = ""
            if (validationResults.isNotEmpty()) {
                val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
                val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
                val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

                if (errors.isNotEmpty()) output += "🛑 ${errors.size} Error(s) found validating ${fileURI.toFileString()}:\n${errors.joinToString("\n") { it.detailMessage() }}"
                if (warnings.isNotEmpty()) output += "⚠️ ${warnings.size} Warnings(s) found validating ${fileURI.toFileString()}:\n${warnings.joinToString("\n") { it.detailMessage() }}"
                if (infos.isNotEmpty()) output += "✅ ${infos.size} Info(s) found validating ${fileURI.toFileString()}:\n${infos.joinToString("\n") { it.detailMessage() }}"

                return output
            }
            return "✅ Specification at ${fileURI.toFileString()} is valid."
        }
    }

    class MarkdownFormatPrinter(override val linkFormatter: LinkFormatter): FormatPrinter {
        override fun print(fileURI: URI, result: RamlModelResult<Api>): String {
            val relativeFileLink = Path(fileURI.toFileString()).relativeTo(linkFormatter.filePath)
            val validationResults = result.validationResults
            var output = ""
            if (validationResults.isNotEmpty()) {
                val errors = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.ERROR }
                val warnings = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.WARNING }
                val infos = validationResults.filter { diagnostic -> diagnostic.severity == Diagnostic.INFO }

                if (errors.isNotEmpty()) output += """
                        |<details>
                        |<summary>🛑  ${errors.size} Error(s) found validating ${relativeFileLink}</summary>
                        |
                        |${errors.joinToString("\n") { "- ${it.detailMessage()} ${linkFormatter.format(it)}" }}
                        |</details>
                        |
                    """.trimMargin()
                if (warnings.isNotEmpty()) output += """
                        |<details>
                        |<summary>⚠️  ${warnings.size} Warnings(s) found validating ${relativeFileLink}</summary>
                        |
                        |${warnings.joinToString("\n") { "- ${it.detailMessage()} ${linkFormatter.format(it)}" }}
                        |</details>
                        |
                    """.trimMargin()
                if (infos.isNotEmpty()) output += """
                        |<details>
                        |<summary>✅  ${infos.size} Info(s) found validating ${relativeFileLink}</summary>
                        |
                        |${infos.joinToString("\n") { "- ${it.detailMessage()} ${linkFormatter.format(it)}" }}
                        |</details>
                        |
                    """.trimMargin()

                return output
            }
            return "Specification at ${fileURI.toFileString()} is valid."
        }
    }
}


