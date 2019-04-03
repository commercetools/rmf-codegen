package io.vrap.codegen.languages

import io.vrap.codegen.languages.java.JavaBaseTypes
import io.vrap.codegen.languages.java.client.SpringClientModule
import io.vrap.codegen.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.languages.java.model.JavaModelModule
import io.vrap.codegen.languages.java.model.second.JavaModelWithInterfacesModule
import io.vrap.codegen.languages.java.plantuml.PlantUmlModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypeScriptModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.tools.StandardLocation
import javax.tools.ToolProvider


class TestCodeGenerator {

    companion object {
        val apiProvider: ApiProvider = ApiProvider(Paths.get("../api-spec/api.raml"))
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer")
    }

    @Test
    fun generateTsModels() {
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, TypeScriptModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateJavaModels() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JavaModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateJavaModelsWithInterfacesModule() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer", outputFolder = Paths.get("build/gensrc/java"))
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JavaModelWithInterfacesModule())
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

            Assert.assertTrue(result)
        }
    }

    /**
     * This test method uses code generator to generate interface and a class for simple-type.raml which is a part of the test-api.raml located in the resources
     * folder. After the classes are generated, it checks if they are the same as the ones specified in SimpleType.txt and SimpleTypeImpl.txt.
     */
    @Test
    fun generateFromCustomRamlAndCompareToAlreadyGeneratedFiles() {
        val testApiProvider = ApiProvider(Paths.get("src/test/resources/java/ramlTestFiles/test-api.raml"))
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.test", outputFolder = Paths.get("build/gensrc/java"))
        val generatorModule = GeneratorModule(testApiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JavaModelWithInterfacesModule())
        generatorComponent.generateFiles()

        val generatedSimpleTypeInterface = String(Files.readAllBytes(Paths.get("build/gensrc/java/com/commercetools/test/models/simpleTypes/SimpleType.java")))
        val generatedSimleTypeClass = String(Files.readAllBytes(Paths.get("build/gensrc/java/com/commercetools/test/models/simpleTypes/SimpleTypeImpl.java")))

        val correctSimpleTypeInterface = String(Files.readAllBytes(Paths.get("src/test/resources/java/ramlTestFiles/generated-files/SimpleType.txt")))
        val correctSimpleTypeClass = String(Files.readAllBytes(Paths.get("src/test/resources/java/ramlTestFiles/generated-files/SimpleTypeImpl.txt")))

        Assert.assertEquals(correctSimpleTypeClass, generatedSimleTypeClass)
        Assert.assertEquals(correctSimpleTypeInterface, generatedSimpleTypeInterface)
    }

    @Test
    fun generateGroovyDsl() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, GroovyDslModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateSpringClient() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, SpringClientModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePlantUmlDiagram() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com.commercetools.importer")
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PlantUmlModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateJoiValidators() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "joi" )
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, JoiBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JoiModule())
        generatorComponent.generateFiles()
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
