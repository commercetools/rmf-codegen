@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

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
import picocli.CommandLine
import picocli.CommandLine.*
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable

fun main(args: Array<String>) {
    CommandLine(GeneratorTask())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(*args)
}

//fun createCliParser(): Cli<Runnable> {
//    val builder = Cli.builder<Runnable>("rmf-codegen")
//            .withDescription("code generator for raml")
//            .withDefaultCommand(Help::class.java)
//            .withCommands(Help::class.java, VersionTask::class.java, GeneratorTask::class.java)
//
//    return builder.build()
//}

/** Targets section */
enum class GenerationTarget {
    JAVA_CLIENT,
    TYPESCRIPT_CLIENT,
    PHP_CLIENT,
    POSTMAN,
    RAML_DOC
}
const val ValidTargets = "JAVA_CLIENT, TYPESCRIPT_CLIENT, PHP_CLIENT, POSTMAN, RAML_DOC"


@Command(version = [BuildInfo.VERSION],description = ["Generate source code from a RAML specification."], helpCommand = true)
class GeneratorTask : Callable<Int> {

    @Option(names = ["-v", "--version"], versionHelp = true, description = ["print version information and exit"])
    var versionRequested = false

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


    @Parameters(index = "0",description = ["Api file location"])
    lateinit var ramlFileLocation: Path


    override fun toString(): String {
        return "GeneratorTask{" +
                ", sharedPackageName='" + sharedPackage +
                ", basePackageName='" + basePackageName +
                ", modelPackageName=" + modelPackageName +
                ", clientPackageName=" + clientPackageName +
                ", outputFolder=" + outputFolder +
                ", ramlFileLocation=" + ramlFileLocation +
                '}'.toString()
    }

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
        val apiProvider = ApiProvider(ramlFileLocation)

        val generatorComponent: GeneratorComponent = when (target) {

            GenerationTarget.JAVA_CLIENT -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, JavaModelModule)
            }
            GenerationTarget.TYPESCRIPT_CLIENT -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                    GeneratorComponent(generatorModule, TypescriptModelModule,TypescriptClientModule)
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
                        sharedPackage = sharedPackage,
                        basePackageName = generatorConfig.basePackageName,
                        modelPackage = modelPackageName,
                        clientPackage = clientPackageName,
                        outputFolder = outputFolder
                )
                val generatorModule = GeneratorModule(apiProvider, ramlConfig, RamldocBaseTypes)
                GeneratorComponent(generatorModule, RamldocModelModule())
            }
        }

        generatorComponent.generateFiles()
        return 0
    }
}
