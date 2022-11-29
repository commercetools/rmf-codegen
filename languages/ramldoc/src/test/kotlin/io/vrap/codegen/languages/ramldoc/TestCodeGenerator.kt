package io.vrap.codegen.languages.ramldoc

import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.extensions.toJson
import io.vrap.codegen.languages.ramldoc.model.RamldocBaseTypes
import io.vrap.codegen.languages.ramldoc.model.RamldocModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.*
import io.vrap.rmf.codegen.io.MemoryDataSink
import io.vrap.rmf.raml.model.types.ObjectInstance
import org.assertj.core.api.Assertions
import org.assertj.core.util.diff.DiffUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.nio.file.Files
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

        Assertions.assertThat(dataSink.files).hasSize(6)
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
              headers:
                FOO:
                  type: string
                  (builtinType): string
                  required: true
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
                  --header 'FOO: ${'$'}{FOO}' \
                  --data-binary @- << DATA 
                  {
                    "foo" : "bar"
                  }
                  DATA
            put:
              body:
                application/json:
                  type: Test
                  (builtinType): object
                  examples:
                    default:
                      strict: true
                      value: !include ../examples/TestPut-default.json
            
              responses:
                200:
                  body:
                    application/json:
                      type: Test
                      (builtinType): object
            
              (codeExamples):
                curl: |-
                  curl -X PUT http://com.foo.bar/api/test -i \
                  --header 'Content-Type: application/json' \
                  --data-binary @- << DATA 
                  {
                    "foo" : "bar"
                  }
                  DATA
        """.trimIndent().trim())
        Assertions.assertThat(dataSink.files.get("resources/Foo.raml")?.trim()).isEqualTo("""
            # Resource
            (resourceName): Foo
            (resourcePathUri): /foo
            
            get:
              headers:
                FOO:
                  type: string
                  (builtinType): string
                  required: true
              responses:
                200:
                  body:
                    application/json:
                      type: Test
                      (builtinType): object
            
              (codeExamples):
                curl: |-
                  curl -X GET http://com.foo.bar/api/foo -i \
                  --header 'FOO: ${'$'}{FOO}'
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
    fun ramlRenderToRamlDoc() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = RamlApiProvider(Paths.get("src/test/resources/oauth.raml"))

        val dataSink = MemoryDataSink()
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes, dataSink = dataSink)
        val generatorComponent = RamlGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()

        Assertions.assertThat(dataSink.files).hasSize(15)

        Assertions.assertThat(dataSink.files.get("api.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/api.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/Oauth.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/Oauth.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/OauthIntrospect.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/OauthIntrospect.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/OauthToken.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/OauthToken.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/OauthByProjectKeyCustomersToken.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/OauthByProjectKeyCustomersToken.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/OauthByProjectKeyAnonymousToken.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/OauthByProjectKeyAnonymousToken.raml".readFile())
        Assertions.assertThat(dataSink.files.get("resources/OauthByProjectKey.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/resources/OauthByProjectKey.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/ClientCredentialsType.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/ClientCredentialsType.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/GrantType.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/GrantType.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/IntrospectResponse.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/IntrospectResponse.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/PasswordType.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/PasswordType.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/RefreshTokenResponse.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/RefreshTokenResponse.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/RefreshTokenType.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/RefreshTokenType.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/TokenType.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/TokenType.raml".readFile())
        Assertions.assertThat(dataSink.files.get("types/TokenResponse.raml")?.trim())
            .isEqualTo("src/test/resources/fixtures/types/TokenResponse.raml".readFile())

        val t = RamlApiProvider(Paths.get("src/test/resources/fixtures/api.raml"))
        t.api
    }

//    @Test
    fun oasRenderToRamlDocOutput() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = OasProvider(Paths.get(System.getenv("TEST_OAS_FILE")))
        val generatorModule = OasGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes)
        val generatorComponent = OasGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun oasRenderToRamlDoc() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com/commercetools/importer",
            outputFolder = Paths.get("build/gensrc")
        )

        val apiProvider = OasProvider(Paths.get("src/test/resources/openapi.yaml"))

        val dataSink = MemoryDataSink()
        val generatorModule = OasGeneratorModule(apiProvider, generatorConfig, RamldocBaseTypes, dataSink = dataSink)
        val generatorComponent = OasGeneratorComponent(generatorModule, RamldocModelModule)
        generatorComponent.generateFiles()

        assertAll(
            {
                Assertions.assertThat(dataSink.files).hasSize(13)
            },
            {
                Assertions.assertThat(dataSink.files.keys.sorted()).isEqualTo(listOf("api.raml",
                    "resources/OauthByProjectKeyAnonymousToken.raml",
                    "resources/OauthByProjectKeyCustomersToken.raml",
                    "resources/OauthIntrospect.raml",
                    "resources/OauthToken.raml",
                    "types/ClientCredentialsType.raml",
                    "types/GrantType.raml",
                    "types/IntrospectResponse.raml",
                    "types/PasswordType.raml",
                    "types/RefreshTokenResponse.raml",
                    "types/RefreshTokenType.raml",
                    "types/TokenResponse.raml",
                    "types/TokenType.raml"
                ))
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/api.raml".readFileLines(),
                    dataSink.files.get("api.raml")?.trim()?.lines(),
                ).deltas).`as`("api.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/resources/OauthIntrospect.raml".readFileLines(),
                    dataSink.files.get("resources/OauthIntrospect.raml")?.trim()?.lines()
                ).deltas).`as`("resources/OauthIntrospect.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/resources/OauthToken.raml".readFileLines(),
                    dataSink.files.get("resources/OauthToken.raml")?.trim()?.lines()
                ).deltas).`as`("resources/OauthToken.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/resources/OauthByProjectKeyCustomersToken.raml".readFileLines(),
                    dataSink.files.get("resources/OauthByProjectKeyCustomersToken.raml")?.trim()?.lines()
                ).deltas).`as`("resources/OauthByProjectKeyCustomersToken.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/resources/OauthByProjectKeyAnonymousToken.raml".readFileLines(),
                    dataSink.files.get("resources/OauthByProjectKeyAnonymousToken.raml")?.trim()?.lines()
                ).deltas).`as`("resources/OauthByProjectKeyAnonymousToken.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/ClientCredentialsType.raml".readFileLines(),
                    dataSink.files.get("types/ClientCredentialsType.raml")?.trim()?.lines()
                ).deltas).`as`("types/ClientCredentialsType.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/GrantType.raml".readFileLines(),
                    dataSink.files.get("types/GrantType.raml")?.trim()?.lines()
                ).deltas).`as`("types/GrantType.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/IntrospectResponse.raml".readFileLines(),
                    dataSink.files.get("types/IntrospectResponse.raml")?.trim()?.lines()
                ).deltas).`as`("types/IntrospectResponse.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/PasswordType.raml".readFileLines(),
                    dataSink.files.get("types/PasswordType.raml")?.trim()?.lines()
                ).deltas).`as`("types/PasswordType.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/RefreshTokenResponse.raml".readFileLines(),
                    dataSink.files.get("types/RefreshTokenResponse.raml")?.trim()?.lines()
                ).deltas).`as`("types/RefreshTokenResponse.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/RefreshTokenType.raml".readFileLines(),
                    dataSink.files.get("types/RefreshTokenType.raml")?.trim()?.lines()
                ).deltas).`as`("types/RefreshTokenType.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/TokenType.raml".readFileLines(),
                    dataSink.files.get("types/TokenType.raml")?.trim()?.lines()
                ).deltas).`as`("types/TokenType.raml").isEmpty()
            },
            {
                Assertions.assertThat(DiffUtils.diff(
                    "src/test/resources/oasfixtures/types/TokenResponse.raml".readFileLines(),
                    dataSink.files.get("types/TokenResponse.raml")?.trim()?.lines()
                ).deltas).`as`("types/TokenResponse.raml").isEmpty()
            }
        )

//        val t = RamlApiProvider(Paths.get("src/test/resources/fixtures/api.raml"))
//        t.api
    }

    private fun String.readFile(): String {
        return Paths.get(this).toFile().readText().trim()
    }

    private fun String.readFileLines(): List<String> {
        return Paths.get(this).toFile().readLines()
    }
}
