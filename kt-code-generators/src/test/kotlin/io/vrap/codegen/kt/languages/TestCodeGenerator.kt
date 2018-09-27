package io.vrap.codegen.kt.languages

import io.vrap.codegen.kt.languages.java.JavaBaseTypes
import io.vrap.codegen.kt.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.kt.languages.java.model.JavaModelModule
import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
import io.vrap.rmf.codegen.kt.di.GeneratorComponent
import io.vrap.rmf.codegen.kt.di.GeneratorModule
import org.eclipse.emf.common.util.URI
import org.junit.Test


class TestCodeGenerator {


    val generatorConfig = CodeGeneratorConfig(
            ramlFileLocation = URI.createFileURI("/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/test/resources/api-spec/api.raml")
    )

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
}