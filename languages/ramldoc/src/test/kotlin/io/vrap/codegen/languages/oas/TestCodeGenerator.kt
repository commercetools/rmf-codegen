package io.vrap.codegen.languages.oas

import io.vrap.codegen.languages.oas.model.RamldocBaseTypes
import io.vrap.codegen.languages.oas.model.RamldocModelModule
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
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc" else userProvidedOutputPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generateRamldocModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com/commercetools/importer",
                outputFolder = Paths.get("${outputFolder}")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, RamldocModelModule())
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
