package io.vrap.codegen.languages

import io.vrap.codegen.languages.java.JavaBaseTypes
import io.vrap.codegen.languages.java.client.SpringClientModule
import io.vrap.codegen.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.languages.java.model.JavaModelModule
import io.vrap.codegen.languages.java.plantuml.PlantUmlModule
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.typescript.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.TypeScriptModule
import org.eclipse.emf.common.util.URI
import org.junit.Test
import java.nio.file.Paths


class TestCodeGenerator {


    val generatorConfig = io.vrap.rmf.codegen.CodeGeneratorConfig(
            basePackageName = "com.commercetools.importer",
            ramlFileLocation = URI.createFileURI("../api-spec/api.raml")
    )

    @Test
    fun generateTsModels(){
        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, TypeScriptBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, TypeScriptModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateJavaModels(){

        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, JavaModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateGroovyDsl(){
        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, GroovyDslModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateSpringClient(){
        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, SpringClientModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePlantUmlDiagram(){
        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, JavaBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, PlantUmlModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePHPModels() {
        val generatorConfig = io.vrap.rmf.codegen.CodeGeneratorConfig(
                basePackageName = "com.commercetools.importer",
                ramlFileLocation = URI.createFileURI("../api-spec/api.raml"),
                outputFolder = Paths.get("build/gensrc/commercetools-raml-sdk")
        )
        val generatorModule = io.vrap.rmf.codegen.di.GeneratorModule(generatorConfig, PhpBaseTypes)
        val generatorComponent = io.vrap.rmf.codegen.di.GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }
}
