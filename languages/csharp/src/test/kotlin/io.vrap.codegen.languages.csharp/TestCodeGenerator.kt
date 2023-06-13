package io.vrap.codegen.languages.csharp



import io.vrap.codegen.languages.csharp.client.builder.test.CsharpTestModule
import io.vrap.codegen.languages.csharp.modules.CsharpClientBuilderModule
import io.vrap.codegen.languages.csharp.modules.CsharpModule
import io.vrap.codegen.languages.csharp.predicates.CsharpQueryPredicateModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val generatedCodePath = System.getenv("GENERATED_CODE_PATH")
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(userProvidedPath ?: "../../api-spec/api.raml")
        private val outputFolder : Path = Paths.get(generatedCodePath ?: "build/gensrc/dotnet-generated")
        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
    }


    @Test
    fun generateCSharpModels() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.Api", outputFolder = outputFolder)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpPredicates() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.Api", outputFolder = outputFolder)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpQueryPredicateModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpTestModule() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.Api.Tests", outputFolder = outputFolder)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpTestModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpModelsForImportApi() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.ImportApi")
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpModelsForHistoryApi() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.HistoryApi", outputFolder = outputFolder)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpModule, CsharpClientBuilderModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generateCSharpModelsForHistoryApiTests() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Sdk.HistoryApi", outputFolder = outputFolder)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, CsharpTestModule)
        generatorComponent.generateFiles()
    }
}
