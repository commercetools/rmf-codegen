@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.model.JavaModelModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.postman.model.PostmanBaseTypes
import io.vrap.codegen.languages.postman.model.PostmanModelModule
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.cli.info.BuildInfo
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.toSeconds
import io.vrap.rmf.raml.model.RamlModelBuilder
import org.eclipse.emf.common.util.URI
import picocli.CommandLine
import picocli.CommandLine.*
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val exitCode = CommandLine(RMFCommand())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(*args)
    exitProcess(exitCode)
}

@Command(
        version = [BuildInfo.VERSION],
        description = ["Allows to validate Raml files and generate code from them"],
        subcommands = [GeneratorTask::class,VerifyCommand::class]
        )
class RMFCommand : Runnable {

    @Option(names = ["-v", "--version"], versionHelp = true, description = ["print version information and exit"])
    var versionRequested = false

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    override fun run() {}
}



/** Targets section */
enum class GenerationTarget {
    JAVA_CLIENT,
    TYPESCRIPT_CLIENT,
    PHP_CLIENT,
    POSTMAN,
    RAML_DOC
}
const val ValidTargets = "JAVA_CLIENT, TYPESCRIPT_CLIENT, PHP_CLIENT, POSTMAN, RAML_DOC"

@Command(name = "generate",description = ["Generate source code from a RAML specification."])
class GeneratorTask : Callable<Int> {

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @Option(names = ["-s", "--shared-package"], description = ["The shared package to be used for the generated code."])
    var sharedPackage: String? = null

    @Option(names = ["-b", "--base-package"], description = ["The base package, this package in case the model or client models aren't provided"],defaultValue = "")
    var basePackageName: String? = null

    @Option(names = ["-m", "--model-package"], description =[ "The models package, this will be used as the model package in the generated code."])
    var modelPackageName: String? = null

    @Option(names = ["-c", "--client-package"], description = ["The client package, This will be used as the package for the client stub."])
    var clientPackageName: String? = null

    @Option(names = ["-o", "--output-folder"], description = ["Output folder for generated files."], required = true)
    lateinit var outputFolder: Path

    @Option(names = ["-t", "--target"], description = ["Specifies the code generation target","Valid values: $ValidTargets"], required = true)
    lateinit var target: GenerationTarget

    @Option(names = ["-w", "--watch"], description = ["Watches the files for changes"], required = false)
    var watch: Boolean = false

    @Parameters(index = "0",description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    override fun call(): Int {
        if(!(Files.exists(ramlFileLocation) && Files.isRegularFile(ramlFileLocation))){
            println("File '$ramlFileLocation' does not exist, please provide an existing spec path.")
            return 1
        }
        val generatorConfig = CodeGeneratorConfig(
                sharedPackage = sharedPackage,
                basePackageName = basePackageName,
                modelPackage = modelPackageName,
                clientPackage = clientPackageName,
                outputFolder = outputFolder
        )

        generate(ramlFileLocation, target, generatorConfig)
        if (watch) {
            val watchDir = ramlFileLocation.parent

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
                                            println("Captured ${event.eventType().name.toLowerCase()}: ${event.path()}")
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
                    .blockingSubscribe {
                        event -> println("Consume ${event.eventType().name.toLowerCase()}: ${event.path()}")
                        generate(ramlFileLocation, target, generatorConfig)
                    }
        }
        return 0
    }

    private fun generate(ramlFileLocation: Path, target: GenerationTarget, generatorConfig: CodeGeneratorConfig) {
        val generateDuration = measureTimeMillis {
            val apiProvider = ApiProvider(ramlFileLocation)
            val generatorComponent: GeneratorComponent = when (target) {
                GenerationTarget.JAVA_CLIENT -> {
                    val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                    GeneratorComponent(generatorModule, JavaModelModule)
                }
                GenerationTarget.TYPESCRIPT_CLIENT -> {
                    val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                    GeneratorComponent(generatorModule, TypescriptModelModule, TypescriptClientModule)
                }
                GenerationTarget.PHP_CLIENT -> {
                    val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
                    GeneratorComponent(generatorModule, PhpModelModule())
                }
                GenerationTarget.POSTMAN -> {
                    val generatorModule = GeneratorModule(apiProvider, generatorConfig, PostmanBaseTypes)
                    GeneratorComponent(generatorModule, PostmanModelModule())
                }
                GenerationTarget.RAML_DOC -> {
                    val ramlConfig = CodeGeneratorConfig(
                            sharedPackage = generatorConfig.sharedPackage,
                            basePackageName = generatorConfig.basePackageName,
                            modelPackage = generatorConfig.modelPackage,
                            clientPackage = generatorConfig.clientPackage,
                            outputFolder = generatorConfig.outputFolder
                    )
                    val generatorModule = GeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes)
                    GeneratorComponent(generatorModule, RamldocModelModule())
                }
            }
            generatorComponent.generateFiles()
        }
        println("Generation took: ${generateDuration.toSeconds(3)}s")
    }
}

@Command(name = "verify",description = ["Allows to verify if a raml spec is valid."])
class VerifyCommand : Callable<Int> {

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @Parameters(index = "0",description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    override fun call(): Int {
        val fileURI = URI.createURI(ramlFileLocation.toUri().toString())
        val modelResult = RamlModelBuilder().buildApi(fileURI)
        val validationResults = modelResult.validationResults
        if (validationResults.isNotEmpty()) {
            validationResults.stream().forEach { validationResult -> println("Error encountered while checking Raml API $validationResult") }
            return 1
        }
        println("specification at $ramlFileLocation is valid!")
        return 0
    }
}
