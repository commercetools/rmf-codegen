package io.vrap.rmf.codegen.cli

import com.commercetools.rmf.validators.ValidatorSetup
import com.google.common.collect.Lists
import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.validation.RamlValidationSetup
import io.vrap.rmf.raml.validation.RamlValidator
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import java.io.File
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


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

    private lateinit var validators: List<RamlValidator>

    override fun call(): Int {
        setupValidators()
        val res = safeRun { validate()}
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
        val modelResult = RamlModelBuilder().buildApi(fileURI, validators)
        val validationResults = modelResult.validationResults
        if (validationResults.isNotEmpty()) {
            val res = validationResults.stream().map { "$it" }.collect( Collectors.joining( "\n" ) );
            InternalLogger.error("${validationResults.size} Error(s) found validating ${fileURI.toFileString()}:\n$res")
            return 1
        }
        InternalLogger.info("Specification at ${fileURI.toFileString()} is valid.")
        return 0
    }

    private fun setupValidators() {
        val ruleset = rulesetFile?.toFile() ?: File(ValidateSubcommand::class.java.getResource("/ruleset.xml").toURI())
        validators = ValidatorSetup.setup(ruleset)
    }

}


