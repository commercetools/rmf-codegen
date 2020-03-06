package io.vrap.rmf.codegen.cli

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.rmf.raml.model.RamlModelBuilder
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


@CommandLine.Command(name = "verify", description = ["Allows to verify if a raml spec is valid."])
class VerifySubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Parameters(index = "0", description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    @CommandLine.Option(names = ["-w", "--watch"], description = ["Watches the files for changes"], required = false)
    var watch: Boolean = false

    override fun call(): Int {
        val res = safeRun { verify()}
        if (watch) {
            val watchDir = ramlFileLocation.toAbsolutePath().parent

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
                                InternalLogger.debug("Consume ${it.eventType().name.toLowerCase()}: ${it.path()}")
                                safeRun { verify() }
                            },
                            {
                                InternalLogger.error(it)
                            }
                    )
        }
        return res
    }

    fun verify(): Int {
        val fileURI = URI.createURI(ramlFileLocation.toUri().toString())
        val modelResult = RamlModelBuilder().buildApi(fileURI)
        val validationResults = modelResult.validationResults
        if (validationResults.isNotEmpty()) {
            validationResults.stream().forEach { validationResult -> InternalLogger.error("Error encountered while checking Raml API $validationResult") }
            return 1
        }
        InternalLogger.info("specification at $ramlFileLocation is valid!")
        return 0
    }

}


