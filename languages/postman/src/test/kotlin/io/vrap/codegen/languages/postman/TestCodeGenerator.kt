package io.vrap.codegen.languages.postman

import io.vrap.codegen.languages.postman.model.PostmanBaseTypes
import io.vrap.codegen.languages.postman.model.PostmanModelModule
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
    fun generatePostmanModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com/commercetools/importer",
                outputFolder = Paths.get("/Users/jensschulze/workspace/commercetools-api-reference/postman")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PostmanBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PostmanModelModule())
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("/Users/jensschulze/workspace/commercetools-api-reference/postman")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
