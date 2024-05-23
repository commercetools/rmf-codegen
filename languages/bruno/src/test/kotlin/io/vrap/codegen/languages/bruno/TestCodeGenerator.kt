package io.vrap.codegen.languages.bruno

import io.vrap.codegen.languages.bruno.model.BrunoBaseTypes
import io.vrap.codegen.languages.bruno.model.BrunoModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

class TestCodeGenerator {
    companion object {
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc/bruno" else userProvidedOutputPath)
        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generatePostmanModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com/commercetools/importer",
                outputFolder = Paths.get("$outputFolder")
        )

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, BrunoBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, BrunoModelModule)
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
