package io.vrap.codegen.languages


import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypeScriptModelModule
import io.vrap.codegen.languages.typescript.server.TsServerModule
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
        val generatorComponent = GeneratorComponent(generatorModule, TypeScriptModelModule,TypescriptClientModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateTsServer() {

        // Generate joi validators
        val joiGeneratorConfig = CodeGeneratorConfig(modelPackage = "",outputFolder = Paths.get("/Users/abeniasaad/IdeaProjects/newTsSdk/src/joi"))
        val joigeneratorModule = GeneratorModule(apiProvider, joiGeneratorConfig, TypeScriptBaseTypes)
        val joigeneratorComponent = GeneratorComponent(joigeneratorModule, JoiModule)
        joigeneratorComponent.generateFiles()

        // Generate ts models
        val modelGeneratorConfig = CodeGeneratorConfig(modelPackage = "",outputFolder = Paths.get("/Users/abeniasaad/IdeaProjects/newTsSdk/src/models"))
        val modelGeneratorModule = GeneratorModule(apiProvider, modelGeneratorConfig, TypeScriptBaseTypes)
        val modelGeneratorComponent = GeneratorComponent(modelGeneratorModule, TypeScriptModelModule)
        modelGeneratorComponent.generateFiles()

        // Generate the server code 
        val serverGeneratorConfig = CodeGeneratorConfig(modelPackage = "",clientPackage = "",outputFolder = Paths.get("/Users/abeniasaad/IdeaProjects/newTsSdk/src/server"))
        val serverGeneratorModule = GeneratorModule(apiProvider, serverGeneratorConfig, TypeScriptBaseTypes)
        val serverGeneratorComponent = GeneratorComponent(serverGeneratorModule, TsServerModule)
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