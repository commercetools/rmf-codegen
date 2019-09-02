package io.vrap.codegen.languages


import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.codegen.languages.typescript.server.TypescriptServerModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generateTsModels() {
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, TypescriptModelModule,TypescriptClientModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateTsServer() {

        // Generate joi validators
        val joiGeneratorConfig = CodeGeneratorConfig(modelPackage = "")
        val joigeneratorModule = GeneratorModule(apiProvider, joiGeneratorConfig, TypeScriptBaseTypes)
        val joigeneratorComponent = GeneratorComponent(joigeneratorModule, JoiModule)
        joigeneratorComponent.generateFiles()

        // Generate ts models
        val modelGeneratorConfig = CodeGeneratorConfig(modelPackage = "")
        val modelGeneratorModule = GeneratorModule(apiProvider, modelGeneratorConfig, TypeScriptBaseTypes)
        val modelGeneratorComponent = GeneratorComponent(modelGeneratorModule, TypescriptModelModule)
        modelGeneratorComponent.generateFiles()

        // Generate the server code 
        val serverGeneratorConfig = CodeGeneratorConfig(modelPackage = "",clientPackage = "")
        val serverGeneratorModule = GeneratorModule(apiProvider, serverGeneratorConfig, TypeScriptBaseTypes)
        val serverGeneratorComponent = GeneratorComponent(serverGeneratorModule, TypescriptServerModule)
        serverGeneratorComponent.generateFiles()
        

    }


    @Test
    fun generateJoiValidators() {
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JoiBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JoiModule)
        generatorComponent.generateFiles()
    }


    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}