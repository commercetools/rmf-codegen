package io.vrap.codegen.languages.ramldoc

import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.extensions.toJson
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.io.MemoryDataSink
import io.vrap.rmf.raml.model.types.ObjectInstance
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
    fun testNullExample() {
        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/nullinstance.raml"))

        val api = apiProvider.api
        val t = api.getType("foo").examples[0].value
        Assertions.assertThat(t.toJson()).isEqualTo("""
            {
              "attributes" : {
                "attribute-to-update" : {
                  "type" : "boolean",
                  "value" : true
                },
                "attribute-to-delete" : null
              }
            }""".trimIndent())
    }

    @Test
    fun testCurlExample() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/curlexample.raml"))

        val dataSink = MemoryDataSink()
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes, dataSink = dataSink)
        val generatorComponent = RamlGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()

        Assertions.assertThat(dataSink.files).hasSize(4)
        Assertions.assertThat(dataSink.files.get("resources/Test.raml")?.trim()).isEqualTo("""
            # Resource
            (resourceName): Test
            (resourcePathUri): /test
            
            get:
              responses:
                200:
                  body:
                    application/json:
                      type: Test
                      (builtinType): object
            
              (codeExamples):
                curl: |-
                  curl -X GET http://com.foo.bar/api/test -i 
            post:
              body:
                application/json:
                  type: Test
                  (builtinType): object
                  examples:
                    default:
                      strict: true
                      value: !include ../examples/TestPost-default.json
            
              responses:
                200:
                  body:
                    application/json:
                      type: Test
                      (builtinType): object
            
              (codeExamples):
                curl: |-
                  curl -X POST http://com.foo.bar/api/test -i \
                  --header 'Content-Type: application/json' \
                  --data-binary @- << DATA 
                  {
                    "foo" : "bar"
                  }
                  DATA
        """.trimIndent().trim())
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
