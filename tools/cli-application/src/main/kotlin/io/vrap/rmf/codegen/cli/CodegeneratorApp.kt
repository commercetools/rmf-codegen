@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

import io.airlift.airline.*
import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.client.SpringClientModule
import io.vrap.codegen.languages.javalang.model.JavaModelModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypeScriptModelModule
import io.vrap.codegen.languages.postman.model.PostmanBaseTypes
import io.vrap.codegen.languages.postman.model.PostmanModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import java.io.File


fun main(args: Array<String>) {
    val commandParser = createCliParser()
    commandParser.parse(*args).run()
}

fun createCliParser(): Cli<Runnable> {
    val builder = Cli.builder<Runnable>("rmf-codegen")
            .withDescription("code generator for raml")
            .withDefaultCommand(Help::class.java)
            .withCommands(Help::class.java, GeneratorTask::class.java)

    return builder.build()
}

/** Targets section */
const val javaModel = "java-model"
const val springClient = "spring-client"
const val typescriptModel = "typescript-model"
const val php = "php"
const val postman = "postman"


@Command(name = "generate", description = "Generate source code from a RAML specification.")
class GeneratorTask : Runnable {

    @Option(name = ["-b", "--base-package"], description = "The base package to be used for the generated code.")
    var basePackageName: String? = null

    @Option(name = ["-m", "--model-package"], description = "The base package to be used for the generated model code.")
    var modelPackageName: String? = null

    @Option(name = ["-c", "--client-package"], description = "The base package to be used for the generated client code.")
    var clientPackageName: String? = null

    @Option(name = ["-o", "--output-folder"], description = "Output folder for generated files.", required = true)
    lateinit var outputFolder: File

    @Option(name = ["-t", "--target"], allowedValues = [javaModel, springClient, typescriptModel, php, postman], description = "the generation target can be one of the following: $javaModel, $springClient, $typescriptModel, $php, $postman", required = true)
    lateinit var target: String


    @Arguments(description = "Api file location", required = true)
    lateinit var ramlFileLocation: File


    override fun toString(): String {
        return "GeneratorTask{" +
                ", basePackageName='" + basePackageName +
                ", modelPackageName=" + modelPackageName +
                ", clientPackageName=" + clientPackageName +
                ", outputFolder=" + outputFolder +
                ", ramlFileLocation=" + ramlFileLocation +
                '}'.toString()
    }

    override fun run() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = basePackageName,
                modelPackage = modelPackageName,
                clientPackage = clientPackageName,
                outputFolder = outputFolder.toPath()
        )
        val apiProvider = ApiProvider(ramlFileLocation.toPath())

        val generatorComponent: GeneratorComponent = when (target) {

            javaModel -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, JavaModelModule)
            }
            springClient -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, SpringClientModule)
            }
            typescriptModel -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                    GeneratorComponent(generatorModule, TypescriptModelModule)
            }
            php -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
                GeneratorComponent(generatorModule, PhpModelModule())
            }
            postman -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, PostmanBaseTypes)
                GeneratorComponent(generatorModule, PostmanModelModule())
            }
            else -> throw IllegalArgumentException("unsupported target '$target', allowed values are $javaModel, $springClient, $typescriptModel and $php")
        }

        generatorComponent.generateFiles()
    }
}
