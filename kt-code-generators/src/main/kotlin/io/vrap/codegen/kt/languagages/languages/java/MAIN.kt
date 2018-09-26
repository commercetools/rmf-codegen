package io.vrap.codegen.kt.languagages.languages.java

import io.vrap.codegen.kt.languagages.languages.java.groovy.dsl.GroovyDslModule
import io.vrap.codegen.kt.languagages.languages.java.model.JavaModelModule
import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
import io.vrap.rmf.codegen.kt.di.GeneratorComponent
import io.vrap.rmf.codegen.kt.di.GeneratorModule
import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet
import org.eclipse.emf.common.util.URI

fun main(args: Array<String>) {

    val generatorConfig = CodeGeneratorConfig(docTransformer = DescriptionFacet::toHtml,
            ramlFileLocation = URI.createFileURI("/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/test/resources/api-spec/api.raml")
    )

    val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
    val generatorComponent = GeneratorComponent(generatorModule, GroovyDslModule(),JavaModelModule())

    generatorComponent.generateFiles()

//    val javaCodeGenerator = generatorComponent.injector.getInstance(JavaCodeGenerator::class.java)
//    val objectTypeRenderer = generatorComponent.injector.getInstance(JavaObjectTypeRenderer::class.java)
//
//    javaCodeGenerator.allTypes
//            .filter { it is ObjectType }
//            .map { it as ObjectType }
//            .map(objectTypeRenderer::render)
//            .forEach(DataSink::save)

}


