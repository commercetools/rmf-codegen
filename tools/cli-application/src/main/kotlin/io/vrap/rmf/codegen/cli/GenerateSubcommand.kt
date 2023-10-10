package io.vrap.rmf.codegen.cli

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.vrap.codegen.languages.csharp.CsharpBaseTypes
import io.vrap.codegen.languages.csharp.client.builder.test.CsharpTestModule
import io.vrap.codegen.languages.csharp.modules.CsharpClientBuilderModule
import io.vrap.codegen.languages.csharp.modules.CsharpModule
import io.vrap.codegen.languages.csharp.predicates.CsharpQueryPredicateModule
import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.client.builder.module.JavaCompleteModule
import io.vrap.codegen.languages.javalang.client.builder.test.JavaTestModule
import io.vrap.codegen.languages.oas.model.OasBaseTypes
import io.vrap.codegen.languages.oas.model.OasModelModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.base.PhpBaseModule
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.php.test.PhpTestModule
import io.vrap.codegen.languages.postman.model.PostmanBaseTypes
import io.vrap.codegen.languages.postman.model.PostmanModelModule
import io.vrap.codegen.languages.python.client.PythonClientModule
import io.vrap.codegen.languages.python.model.PythonBaseTypes
import io.vrap.codegen.languages.python.model.PythonModelModule
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.codegen.languages.typescript.test.TypescriptTestModule
import io.vrap.codegen.languages.go.GoBaseTypes
import io.vrap.codegen.languages.go.model.GoModelModule
import io.vrap.codegen.languages.go.client.GoClientModule
import io.vrap.codegen.languages.java.base.PlantUmlBaseTypes
import io.vrap.codegen.languages.javalang.client.builder.predicates.JavaQueryPredicateModule
import io.vrap.codegen.languages.javalang.plantuml.PlantUmlModule
import io.vrap.codegen.languages.ramldoc.model.MarkdownModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.*
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.toSeconds
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import picocli.CommandLine
import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.io.path.exists
import kotlin.system.measureTimeMillis


/** Targets section */
enum class GenerationTarget {
    JAVA_CLIENT,
    JAVA_TEST,
    JAVA_QUERY_PREDICATES,
    TYPESCRIPT_CLIENT,
    TYPESCRIPT_TEST,
    PHP_CLIENT,
    PHP_BASE,
    PHP_TEST,
    GO_CLIENT,
    POSTMAN,
    RAML_DOC,
    CSHARP_CLIENT,
    CSHARP_TEST,
    CSHARP_QUERY_PREDICATES,
    OAS,
    PYTHON_CLIENT,
    PLANTUML,
    DOC_MARKDOWN
}
const val ValidTargets = "JAVA_CLIENT, JAVA_TEST, JAVA_QUERY_PREDICATES, TYPESCRIPT_CLIENT, TYPESCRIPT_TEST, CSHARP_CLIENT, CSHARP_TEST, CSHARP_QUERY_PREDICATES, PHP_CLIENT, PHP_BASE, PHP_TEST, POSTMAN, RAML_DOC, OAS, PYTHON_CLIENT, PLANTUML, DOC_MARKDOWN"

@CommandLine.Command(name = "generate",description = ["Generate source code from a RAML specification."])
class GenerateSubcommand : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    @CommandLine.Option(names = ["-s", "--shared-package"], description = ["The shared package to be used for the generated code."])
    var sharedPackage: String? = null

    @CommandLine.Option(names = ["-b", "--base-package"], description = ["The base package, this package in case the model or client models aren't provided"],defaultValue = "")
    var basePackageName: String? = null

    @CommandLine.Option(names = ["-m", "--model-package"], description =[ "The models package, this will be used as the model package in the generated code."])
    var modelPackageName: String? = null

    @CommandLine.Option(names = ["-c", "--client-package"], description = ["The client package, This will be used as the package for the client stub."])
    var clientPackageName: String? = null

    @CommandLine.Option(names = ["-o", "--output-folder"], description = ["Output folder for generated files."], required = true)
    lateinit var outputFolder: Path

    @CommandLine.Option(names = ["-t", "--target"], description = ["Specifies the code generation target","Valid values: $ValidTargets"], required = true)
    lateinit var target: GenerationTarget

    @CommandLine.Option(names = ["-w", "--watch"], description = ["Watches the files for changes"], required = false)
    var watch: Boolean = false

    @CommandLine.Option(names = ["--inline-examples"], description = ["Inline the Examples to the generated RAML files"], required = false)
    var inlineExamples: Boolean = false

    @CommandLine.Option(names = ["--predicates"], description = ["Generate predicate builders"], required = false)
    var predicateBuilders: Boolean = false

    @CommandLine.Option(names = ["-v", "--verbose"], description = ["If set, this would move the verbosity level to debug."], required = false)
    var verbose: Boolean = false

    @CommandLine.Option(names = ["--hash"], description = ["If set a gen.properties file with the git hash of the RAML specification will be written"], required = false)
    var writeGitHash: Boolean = false

    @CommandLine.Option(names = ["--mappingFile"], required = false )
    var typeMappingFile: Path? = null

    @CommandLine.Option(names = ["--dry-run"], description = ["Don't write to files"], required = false )
    var dryRun: Boolean? = false

    @CommandLine.Parameters(index = "0",description = ["Api file location"])
    lateinit var ramlFileLocation: Path

    private fun mapStringClass(className: String): VrapType {
        val classParts = className.split(".")
        return VrapObjectType(simpleClassName = classParts.last(), `package` = classParts.dropLast(1).joinToString("."))
    }

    private fun readMapFile(file: Path?): Map<String, VrapType> {
        if (file == null || !file.exists()) {
            return mapOf()
        }
        val prop = Properties()

        FileInputStream(file.toFile()).use {
            prop.load(it)
        }

        return prop.map { it.key.toString() to mapStringClass(it.value.toString()) }.toMap()
    }

    override fun call(): Int {
        RxJavaPlugins.setErrorHandler { e: Throwable -> InternalLogger.warn(e)}
        if(verbose){
            InternalLogger.logLevel = LogLevel.DEBUG
        }
        if(!(Files.exists(ramlFileLocation) && Files.isRegularFile(ramlFileLocation))){
            InternalLogger.error("File '$ramlFileLocation' does not exist, please provide an existing spec path.")
            return 1
        }
        outputFolder = Paths.get("").toAbsolutePath().resolve(outputFolder).normalize()
        val customTypeMapping = readMapFile(typeMappingFile)
        val generatorConfig = CodeGeneratorConfig(
                sharedPackage = sharedPackage,
                basePackageName = basePackageName,
                modelPackage = modelPackageName,
                clientPackage = clientPackageName,
                outputFolder = outputFolder,
                writeGitHash = writeGitHash,
                customTypeMapping = customTypeMapping,
                inlineExamples = inlineExamples

        )

        val res = safeRun { generate(ramlFileLocation, target, generatorConfig) }
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
                                safeRun { generate(ramlFileLocation, target, generatorConfig) }
                            },
                            {
                                InternalLogger.error(it)
                            }
                    )
        }
        return res
    }

    private fun generate(fileLocation: Path, target: GenerationTarget, generatorConfig: CodeGeneratorConfig): Int {
        val generateDuration = measureTimeMillis {
            val generatorComponent: GeneratorComponent
            val sink = FileDataSink(generatorConfig.outputFolder, dryRun ?: false)
            if (fileLocation.toString().endsWith(".raml")) {
                val apiProvider = RamlApiProvider(fileLocation)
                generatorComponent = when (target) {
                    GenerationTarget.JAVA_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes, dataSink = sink)
                        if (predicateBuilders) {
                            RamlGeneratorComponent(generatorModule, JavaCompleteModule, JavaQueryPredicateModule)
                        } else {
                            RamlGeneratorComponent(generatorModule, JavaCompleteModule)
                        }
                    }
                    GenerationTarget.JAVA_QUERY_PREDICATES -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, JavaQueryPredicateModule)
                    }
                    GenerationTarget.JAVA_TEST -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, JavaTestModule)
                    }
                    GenerationTarget.TYPESCRIPT_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, TypescriptModelModule, TypescriptClientModule)
                    }
                    GenerationTarget.TYPESCRIPT_TEST -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, TypescriptTestModule)
                    }
                    GenerationTarget.PHP_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PhpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PhpModelModule)
                    }
                    GenerationTarget.PHP_BASE -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PhpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PhpBaseModule)
                    }
                    GenerationTarget.PHP_TEST -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PhpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PhpTestModule)
                    }
                    GenerationTarget.CSHARP_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
                    }
                    GenerationTarget.CSHARP_TEST -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, CsharpTestModule)
                    }
                    GenerationTarget.CSHARP_QUERY_PREDICATES -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, CsharpQueryPredicateModule)
                    }
                    GenerationTarget.POSTMAN -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PostmanBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PostmanModelModule)
                    }
                    GenerationTarget.RAML_DOC -> {
                        val ramlConfig = CodeGeneratorConfig(
                            sharedPackage = generatorConfig.sharedPackage,
                            basePackageName = generatorConfig.basePackageName,
                            modelPackage = generatorConfig.modelPackage,
                            clientPackage = generatorConfig.clientPackage,
                            outputFolder = generatorConfig.outputFolder,
                            writeGitHash = generatorConfig.writeGitHash,
                            inlineExamples = generatorConfig.inlineExamples
                        )
                        val generatorModule = RamlGeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, RamldocModelModule)
                    }
                    GenerationTarget.DOC_MARKDOWN -> {
                        val ramlConfig = CodeGeneratorConfig(
                                sharedPackage = generatorConfig.sharedPackage,
                                basePackageName = generatorConfig.basePackageName,
                                modelPackage = generatorConfig.modelPackage,
                                clientPackage = generatorConfig.clientPackage,
                                outputFolder = generatorConfig.outputFolder,
                                writeGitHash = generatorConfig.writeGitHash,
                                inlineExamples = generatorConfig.inlineExamples
                        )
                        val generatorModule = RamlGeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, MarkdownModelModule)
                    }
                    GenerationTarget.PLANTUML -> {
                        val ramlConfig = CodeGeneratorConfig(
                            sharedPackage = generatorConfig.sharedPackage,
                            basePackageName = generatorConfig.basePackageName,
                            modelPackage = generatorConfig.modelPackage,
                            clientPackage = generatorConfig.clientPackage,
                            outputFolder = generatorConfig.outputFolder,
                            writeGitHash = generatorConfig.writeGitHash
                        )
                        val generatorModule = RamlGeneratorModule(apiProvider, ramlConfig, PlantUmlBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PlantUmlModule)
                    }
                    GenerationTarget.OAS -> {
                        val ramlConfig = CodeGeneratorConfig(
                            sharedPackage = generatorConfig.sharedPackage,
                            basePackageName = generatorConfig.basePackageName,
                            modelPackage = generatorConfig.modelPackage,
                            clientPackage = generatorConfig.clientPackage,
                            outputFolder = generatorConfig.outputFolder,
                            writeGitHash = generatorConfig.writeGitHash
                        )
                        val generatorModule = RamlGeneratorModule(apiProvider, ramlConfig, OasBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, OasModelModule)
                    }
                    GenerationTarget.PYTHON_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PythonBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, PythonModelModule, PythonClientModule)
                    }
                    GenerationTarget.GO_CLIENT -> {
                        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, GoBaseTypes, dataSink = sink)
                        RamlGeneratorComponent(generatorModule, GoModelModule, GoClientModule)
                    }
                }
            } else {
                val apiProvider = OasProvider(fileLocation)
                generatorComponent = when (target) {
                    GenerationTarget.RAML_DOC -> {
                        val ramlConfig = CodeGeneratorConfig(
                            sharedPackage = generatorConfig.sharedPackage,
                            basePackageName = generatorConfig.basePackageName,
                            modelPackage = generatorConfig.modelPackage,
                            clientPackage = generatorConfig.clientPackage,
                            outputFolder = generatorConfig.outputFolder,
                            writeGitHash = generatorConfig.writeGitHash
                        )
                        val generatorModule = OasGeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes, dataSink = sink)
                        OasGeneratorComponent(generatorModule, RamldocModelModule)
                    }
                    else -> {
                        throw IllegalArgumentException("Target not supported for OAS files")
                    }
                }
            }
            generatorComponent.generateFiles()
        }
        InternalLogger.info("Generation took: ${generateDuration.toSeconds(3)}s")
        return 0
    }
}
