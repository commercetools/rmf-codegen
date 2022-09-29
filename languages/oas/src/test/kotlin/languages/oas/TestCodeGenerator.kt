package io.vrap.codegen.languages.oas

import io.vrap.codegen.languages.oas.model.OasBaseTypes
import io.vrap.codegen.languages.oas.model.OasModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.io.MemoryDataSink
import org.assertj.core.api.Assertions
import org.assertj.core.util.diff.DiffUtils
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
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generateOasFile() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com/commercetools/importer",
                outputFolder = Paths.get("${outputFolder}")
        )

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, OasBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, OasModelModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun oasRender() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/oauth.raml"))

        val dataSink = MemoryDataSink()
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, OasBaseTypes, dataSink = dataSink)
        val generatorComponent = RamlGeneratorComponent(generatorModule, OasModelModule)
        generatorComponent.generateFiles()

        Assertions.assertThat(dataSink.files).hasSize(1)

        Assertions.assertThat(
            DiffUtils.diff(
            "src/test/resources/fixtures/openapi.yaml".readFileLines(),
            dataSink.files.get("openapi.yaml")?.trim()?.lines(),
        ).deltas).`as`("openapi.yaml").isEmpty()

        Assertions.assertThat(dataSink.files.get("openapi.yaml")?.trim())
            .isEqualTo("src/test/resources/fixtures/openapi.yaml".readFile())
    }

    private fun String.readFile(): String {
        return Paths.get(this).toFile().readText().trim()
    }

    private fun String.readFileLines(): List<String> {
        return Paths.get(this).toFile().readLines()
    }
    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
