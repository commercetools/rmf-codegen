package io.vrap.codegen.languages.ramldoc

import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.io.MemoryDataSink
import org.assertj.core.api.Assertions
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
    fun generateRamldocModels() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "com/commercetools/importer",
                outputFolder = Paths.get("${outputFolder}")
        )

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }

    @Test
    fun testAnnotationToYaml() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/multiline.raml"))

        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()

        val api = apiProvider.api
        val t = api.getAnnotation("test").renderAnnotation()


        Assertions.assertThat(t).isEqualTo("""
            (test):
              enumValue1: "Description 1"
              enumValue2: |-
                Description 2 with a line break (first line)
                Second line.
            
                New paragraph.
              enumWithMarkdownDescription: "`inline-code` should be formatted as an inline code. [ObjectTestType](/types/general#objecttesttype) should link to the header of the definition of `ObjectTestType` on this website - `api-docs-smoke-test`. [Links](/../docs-smoke-test/views/markdown#links) should link to the header for the definition of  `Links` on `docs-smoke-test` microsite."
        """.trimIndent().trimStart())
    }

    @Test
    fun oasRenderToRamlDoc() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/oauth.raml"))

        val dataSink = MemoryDataSink()
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes, dataSink = dataSink)
        val generatorComponent = RamlGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()

    }
}
