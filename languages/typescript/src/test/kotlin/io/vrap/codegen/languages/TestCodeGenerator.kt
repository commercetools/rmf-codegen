package io.vrap.codegen.languages


import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.codegen.languages.typescript.server.TypescriptServerModule
import io.vrap.codegen.languages.typescript.test.TypescriptTestModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")

        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc" else userProvidedOutputPath)

        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "",
                outputFolder = Paths.get("${outputFolder}")
        )
    }


    @Test
    fun generateTsModels() {
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, TypescriptModelModule,TypescriptClientModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateTsTests() {
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, TypescriptTestModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateTsServer() {

        // Generate joi validators
        val joiGeneratorConfig = CodeGeneratorConfig(modelPackage = "")
        val joigeneratorModule = RamlGeneratorModule(apiProvider, joiGeneratorConfig, TypeScriptBaseTypes)
        val joigeneratorComponent = RamlGeneratorComponent(joigeneratorModule, JoiModule)
        joigeneratorComponent.generateFiles()

        // Generate ts models
        val modelGeneratorConfig = CodeGeneratorConfig(modelPackage = "")
        val modelGeneratorModule = RamlGeneratorModule(apiProvider, modelGeneratorConfig, TypeScriptBaseTypes)
        val modelGeneratorComponent = RamlGeneratorComponent(modelGeneratorModule, TypescriptModelModule)
        modelGeneratorComponent.generateFiles()

        // Generate the server code
        val serverGeneratorConfig = CodeGeneratorConfig(modelPackage = "",clientPackage = "")
        val serverGeneratorModule = RamlGeneratorModule(apiProvider, serverGeneratorConfig, TypeScriptBaseTypes)
        val serverGeneratorComponent = RamlGeneratorComponent(serverGeneratorModule, TypescriptServerModule)
        serverGeneratorComponent.generateFiles()


    }


    @Test
    fun generateJoiValidators() {
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JoiBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JoiModule)
        generatorComponent.generateFiles()
    }


    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
