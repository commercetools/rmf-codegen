package io.vrap.rmf.codegen.cli

import com.commercetools.rmf.validators.ValidatorSetup
import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.rmf.raml.model.RamlModelBuilder
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.nio.file.Path
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


@CommandLine.Command(name = "validate", description = ["Allows to verify if a raml spec is valid according to CT guideline"])
class ValidateSubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Parameters(index = "0", description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    @CommandLine.Option(names = ["-r", "--ruleset"], description = ["Ruleset configuration"], required = false)
    var rulesetFile: Path? = null

    @CommandLine.Option(names = ["-w", "--watch"], description = ["Watches the files for changes"], required = false)
    var watch: Boolean = false

    @CommandLine.Option(names = ["-p", "--printer"], required = false)
    var printerType: Printer = Printer.LOGGER

    lateinit var modelBuilder: RamlModelBuilder

    lateinit var ramlDiagnosticPrinter: RamlDiagnosticPrinter

    override fun call(): Int {
        ramlDiagnosticPrinter = Printer.diagnosticPrinter(printerType)
        modelBuilder = setupValidators()
        val res = safeRun { validate()}
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
                                        val json = event.path().toString().endsWith("json")
                                        val raml = event.path().toString().endsWith("raml")
                                        if (json || raml) {
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
                                safeRun { validate() }
                            },
                            {
                                InternalLogger.error(it)
                            }
                    )
        }
        return res
    }

    fun validate(): Int {
        val fileURI = URI.createURI(ramlFileLocation.toUri().toString())
        val modelResult = modelBuilder.buildApi(fileURI)

        return ramlDiagnosticPrinter.print(fileURI, modelResult);
    }

    private fun setupValidators(): RamlModelBuilder {
        val ruleset = rulesetFile?.toFile()?.inputStream() ?: ValidateSubcommand::class.java.getResourceAsStream("/ruleset.xml")
        return RamlModelBuilder(ValidatorSetup.setup(ruleset))
    }
}


