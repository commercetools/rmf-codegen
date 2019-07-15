package io.vrap.codegen.languages

import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
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
        private val apiPath : Path = Paths.get( "/Users/abeniasaad/IdeaProjects/commercetools-api-reference/update-actions.raml")
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "",outputFolder = Paths.get("/Users/abeniasaad/IdeaProjects/rmf-codegen/typescript_client/src/gen"))
    }

    @Test
    fun generatePHPModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com.commercetools.importer",
                outputFolder = Paths.get("build/gensrc/commercetools-raml-sdk")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}