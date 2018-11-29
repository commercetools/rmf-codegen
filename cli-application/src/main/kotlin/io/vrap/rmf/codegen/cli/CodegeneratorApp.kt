//package io.vrap.rmf.codegen.cli
//
//import io.airlift.airline.*
//import io.vrap.codegen.kt.languages.java.JavaBaseTypes
//import io.vrap.codegen.kt.languages.java.model.JavaModelModule
//import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
//import io.vrap.rmf.codegen.kt.di.GeneratorComponent
//import io.vrap.rmf.codegen.kt.di.GeneratorModule
//import org.eclipse.emf.common.util.URI
//import java.io.File
//
//
//fun main(args: Array<String>) {
//    val commandParser = createCliParser()
//    commandParser.parse(*args).run()
//}
//
//fun createCliParser(): Cli<Runnable> {
//    val builder = Cli.builder<Runnable>("rmf-codegen")
//            .withDescription("code generator for raml")
//            .withDefaultCommand(Help::class.java)
//            .withCommands(Help::class.java, GeneratorTask::class.java)
//
//    return builder.build()
//}
//
//
//@Command(name = "generate", description = "Generate source code from a RAML specification.")
//class GeneratorTask : Runnable {
//
//    @Option(name = ["-b", "--base-package"], description = "The base package to be used for the generated code.")
//    var basePackageName: String? = null
//
//    @Option(name = ["-m", "--model-package"], description = "The base package to be used for the generated model code.")
//    var modelPackageName: String? = null
//
//    @Option(name = ["-c", "--client-package"], description = "The base package to be used for the generated client code.")
//    var clientPackageName: String? = null
//
//    @Option(name = ["-o", "--output-folder"], description = "Output folder for generated files.", required = true)
//    lateinit var outputFolder: File
//
//    @Arguments(description = "Api file location", required = true)
//    lateinit var ramlFileLocation: File
//
//
//    override fun toString(): String {
//        return "GeneratorTask{" +
//                ", basePackageName='" + basePackageName +
//                ", modelPackageName=" + modelPackageName +
//                ", clientPackageName=" + clientPackageName +
//                ", outputFolder=" + outputFolder +
//                ", ramlFileLocation=" + ramlFileLocation +
//                '}'.toString()
//    }
//
//    override fun run() {
//        val generatorConfig = CodeGeneratorConfig(
//                basePackageName = basePackageName,
//                modelPackage = modelPackageName,
//                clientPackage = clientPackageName,
//                outputFolder = outputFolder.toPath(),
//
//                ramlFileLocation = URI.createURI(ramlFileLocation.toURI().toString())
//        )
//        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
//        val generatorComponent = GeneratorComponent(generatorModule, JavaModelModule())
//        generatorComponent.generateFiles()
//    }
//}
