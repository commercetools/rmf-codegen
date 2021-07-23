package io.vrap.codegen.languages.csharp



import io.vrap.codegen.languages.csharp.modules.CsharpClientBuilderModule
import io.vrap.codegen.languages.csharp.modules.CsharpModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val generatedCodePath = System.getenv("GENERATED_CODE_PATH")
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val outputFolder : Path = Paths.get(if (generatedCodePath == null) "build/gensrc/dotnet-generated" else generatedCodePath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
    }


    @Test
    fun generateCSharpModels() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Api", outputFolder = outputFolder)
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpModelsForImportApi() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.ImportApi")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpModelsForHistoryApi() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.HistoryApi")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }
}
