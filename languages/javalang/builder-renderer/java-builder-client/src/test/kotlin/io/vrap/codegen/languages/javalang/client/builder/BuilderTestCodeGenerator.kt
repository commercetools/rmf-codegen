package io.vrap.codegen.languages.javalang.client.builder

import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.client.builder.module.JavaCompleteModule
import io.vrap.codegen.languages.javalang.client.builder.test.JavaTestModule

import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.tools.StandardLocation
import javax.tools.ToolProvider

class BuilderTestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val importApiProvidedPath = System.getenv("IMPORT_API_RAML_FILE")
        private val basePackageEnv = System.getenv("BASE_PACKAGE")
        private val baseBackage = if (basePackageEnv == null) "com.commercetools.api" else basePackageEnv

        /**
         * Specifies a path where the code generator should place generated code
         * */
        private val generatedCodePath = System.getenv("GENERATED_CODE_PATH")
        private val generatedTestCodePath = System.getenv("GENERATED_TEST_CODE_PATH")
        private val generatedImporterCodePath = System.getenv("GENERATED_IMPORT_API_CODE_PATH")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../../../api-spec/api.raml" else userProvidedPath)
        private val importApiPath : Path = Paths.get(if (importApiProvidedPath == null) "../../../../api-spec/api.raml" else importApiProvidedPath)
        private val outputFolder : Path = Paths.get(if (generatedCodePath == null) "build/gensrc/main/java-generated" else generatedCodePath)
        private val testOutputFolder : Path = Paths.get(if (generatedCodePath == null) "build/gensrc/test/java-generated" else generatedTestCodePath)

        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
        val importApiProvider: RamlApiProvider = RamlApiProvider(importApiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com/commercetools/test")
    }

    @Disabled
    @Test
    fun generateJavaModelsWithInterfacesModule() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com/commercetools/importer", outputFolder = Paths.get("build/gensrc/java"))
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JavaCompleteModule)
        generatorComponent.generateFiles()

        File("build/compiled").deleteRecursively()
        File("build/compiled").mkdirs()

        val filesList =  File("build/gensrc")
                .walkTopDown()
                .filter( File::isFile )
                .filter { it.name.endsWith(".java") }
                .toList()

        val compiler = ToolProvider.getSystemJavaCompiler()
        val fileManager = compiler.getStandardFileManager(null, null, null)

        fileManager.use {
            it.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(File("build/compiled")))
            val result = compiler.getTask(null,
                    fileManager, null, null, null,
                    fileManager.getJavaFileObjectsFromFiles(filesList)).call()

            Assertions.assertTrue(result)
        }
    }

    private fun mapStringClass(className: String): VrapType {
        val classParts = className.split(".")
        return VrapObjectType(simpleClassName = classParts.last(), `package` = classParts.dropLast(1).joinToString("."))
    }

    @Test
    fun generateJavaCompleteModule() {
        val typeMapping = mapOf(
            Pair("LocalizedString", "com.commercetools.api.models.common.LocalizedString"),
            Pair("Money", "com.commercetools.api.models.common.Money")
        )
        val customTypeMapping = typeMapping.map { it.key to mapStringClass(it.value)}.toMap()
        val generatorConfig = CodeGeneratorConfig(basePackageName = baseBackage, outputFolder = outputFolder, customTypeMapping = customTypeMapping)
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JavaCompleteModule)
        generatorComponent.generateFiles()

        val generatorTestConfig = CodeGeneratorConfig(basePackageName = baseBackage, outputFolder = testOutputFolder)
        val generatorTestModule = RamlGeneratorModule(apiProvider, generatorTestConfig, JavaBaseTypes)
        val generatorTestComponent = RamlGeneratorComponent(generatorTestModule, JavaTestModule)
        generatorTestComponent.generateFiles()
    }

    @Disabled
    @Test
    fun generateJavaImportApi() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer", outputFolder = Paths.get(generatedImporterCodePath))
        val generatorModule = RamlGeneratorModule(importApiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JavaCompleteModule)
        generatorComponent.generateFiles()
    }

    /**
     * This test method uses code generator to generate interface and a class for simple-type.raml which is a part of the test-api.raml located in the resources
     * folder. After the classes are generated, it checks if they are the same as the ones specified in SimpleType.txt and SimpleTypeImpl.txt.
     */
    @Disabled
    @Test
    fun generateFromCustomRamlAndCompareToAlreadyGeneratedFiles() {
        val testApiProvider = RamlApiProvider(Paths.get("src/test/resources/java/ramlTestFiles/test-api.raml"))
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.test", outputFolder = Paths.get("build/gensrc/java"))
        val generatorModule = RamlGeneratorModule(testApiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JavaCompleteModule)
        generatorComponent.generateFiles()

        val generatedSimpleTypeInterface = String(Files.readAllBytes(Paths.get("build/gensrc/java/com/commercetools/test/models/simpleTypes/SimpleType.java")))
        val generatedSimleTypeClass = String(Files.readAllBytes(Paths.get("build/gensrc/java/com/commercetools/test/models/simpleTypes/SimpleTypeImpl.java")))

        val correctSimpleTypeInterface = String(Files.readAllBytes(Paths.get("src/test/resources/java/ramlTestFiles/generated-files/SimpleType.txt")))
        val correctSimpleTypeClass = String(Files.readAllBytes(Paths.get("src/test/resources/java/ramlTestFiles/generated-files/SimpleTypeImpl.txt")))

        Assertions.assertEquals(correctSimpleTypeClass, generatedSimleTypeClass)
        Assertions.assertEquals(correctSimpleTypeInterface, generatedSimpleTypeInterface)
    }
}
