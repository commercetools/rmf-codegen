package io.vrap.codegen.kt.languages

import io.vrap.codegen.kt.languages.java.JavaBaseTypes
import io.vrap.codegen.kt.languages.java.client.SpringClientModule
import io.vrap.codegen.kt.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.kt.languages.java.model.JavaModelModule
import io.vrap.codegen.kt.languages.java.plantuml.PlantUmlModule
import io.vrap.codegen.kt.languages.php.PhpBaseTypes
import io.vrap.codegen.kt.languages.php.model.PhpModelModule
import io.vrap.codegen.kt.languages.typescript.TypeScriptBaseTypes
import io.vrap.codegen.kt.languages.typescript.TypeScriptModule
import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
import io.vrap.rmf.codegen.kt.di.GeneratorComponent
import io.vrap.rmf.codegen.kt.di.GeneratorModule
import org.eclipse.emf.common.util.URI
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths


class TestCodeGenerator {


    val generatorConfig = CodeGeneratorConfig(
            packagePrefix = "com.commercetools.importer",
            ramlFileLocation = URI.createFileURI(Paths.get("").resolve("../api-spec/api.raml").toAbsolutePath().toString())
    )

    @Test
    fun generateTsModels(){
        val generatorModule = GeneratorModule(generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, TypeScriptModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateJavaModels(){

        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, JavaModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateGroovyDsl(){
        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, GroovyDslModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateSpringClient(){
        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, SpringClientModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePlantUmlDiagram(){
        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PlantUmlModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePHPModels() {
        val generatorConfig = CodeGeneratorConfig(
                packagePrefix = "com.commercetools.importer",
                ramlFileLocation = URI.createFileURI("../api-spec/api.raml"),
                outputFolder = Paths.get("build/gensrc/commercetools-raml-sdk")
        )
        val generatorModule = GeneratorModule(generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }
}
