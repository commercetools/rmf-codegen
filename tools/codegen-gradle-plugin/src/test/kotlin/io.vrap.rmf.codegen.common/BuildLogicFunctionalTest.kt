package io.vrap.rmf.codegen.common

import org.assertj.core.api.Assertions
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Path
import java.nio.file.Paths

class BuildLogicFunctionalTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File
    private val logger = LoggerFactory.getLogger(javaClass)

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle")
    }

    @Test
    fun testGenerateEntitiesTask() {

        val userProvidedPath = System.getenv("TEST_RAML_FILE")
        val apiFile : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath).toAbsolutePath()

        Assertions.assertThat(apiFile).exists()

        val buildFileContent = """
        |plugins {
        |    id 'com.commercetools.rmf.codegen-plugin'
        |}
        |
        |RamlGenerator {
        |    apiSpec {
        |      uri = file('$apiFile')
        |      targets {
        |          importApi {
        |               path = file('import-api/build/generated-classes')
        |               models_package = 'com.commercetools.models'
        |               client_package = 'com.commercetools.client'
        |               target = 'typescriptModel'
        |          }
        |          serviceName2 {
        |               path = file('import-api/build/generated-classes')
        |               models_package = 'com.commercetools.models'
        |               client_package = 'com.commercetools.client'
        |               target = 'typescriptClient'
        |          }
        |          importApi_groovy_dsl {
        |               path = file('import-api/build/generated-classes')
        |               models_package = 'com.commercetools.models'
        |               client_package = 'com.commercetools.client'
        |               target = 'groovyDsl'
        |          }
        |          importStorageService {
        |               path = file('import-api2/build/generated-classes')
        |               models_package = 'com.commercetools.models2'
        |               client_package = 'com.commercetools.client2'
        |               target = 'javaModel'
        |          }
        |          importResolveableModels {
        |               path = file('import-api-resolveable-model/build/generated-classes')
        |               base_package = 'com.commercetools.importapi.resolveable'
        |               target = 'javaModel'
        |               customTypeMapping = [
        |                       'CategoryKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'CartDiscountKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ResolvableReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ChannelKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'CustomerKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'CustomerGroupKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'PriceKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ProductKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ProductTypeKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ProductVariantKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'ShippingMethodKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'StateKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference',
        |                       'TaxCategoryKeyReference': 'com.commercetools.importstorage.resolvable.models.common.ResolvableReference'
        |               ]
        |          }
        |          importStorageService {
        |               path = file('import-api2/build/generated-classes')
        |               models_package = 'com.commercetools.models2'
        |               client_package = 'com.commercetools.client2'
        |               target = 'joiValidator'
        |          }
        |       }
        |    }
        |}
        """.trimMargin()

        writeFile(buildFile, buildFileContent)
        val result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("generateRamlStub", "--stacktrace")
                .withPluginClasspath()
                .build()

        logger.warn(result.output)

        Assertions.assertThat(result.task(":generateRamlStub")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)

        Assertions.assertThat(testProjectDir.list { _, name -> name == "import-api" }).isNotEmpty
    }

    private fun writeFile(destination: File?, content: String) {
        var output: BufferedWriter? = null
        try {
            output = BufferedWriter(FileWriter(destination!!))
            output.write(content)
        } finally {
            output?.close()
        }
    }
}
