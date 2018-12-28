package io.vrap.codegen.languages

import io.vrap.codegen.languages.java.JavaBaseTypes
import io.vrap.codegen.languages.java.client.SpringClientModule
import io.vrap.codegen.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.languages.java.model.JavaModelModule
import io.vrap.codegen.languages.java.plantuml.PlantUmlModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.typescript.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.TypeScriptModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.io.File
import java.nio.file.Paths


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
    fun generatePHPModels() {
        val generatorConfig = CodeGeneratorConfig(
            basePackageName = "com.commercetools.importer",
            outputFolder = Paths.get("build/gensrc/commercetools-raml-sdk")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }
}
