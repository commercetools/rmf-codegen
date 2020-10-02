package io.vrap.codegen.languages.csharp



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
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
    }


    @Test
    fun generateCSharpModels() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "commercetools.Api")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, CsharpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, CsharpModule)
        generatorComponent.generateFiles()
    }
}