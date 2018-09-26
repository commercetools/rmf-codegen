package io.vrap.rmf.codegen.kt

import io.vrap.codegen.kt.languagages.languages.java.JavaBaseTypes
import io.vrap.codegen.kt.languagages.languages.java.JavaCodeGenerator
import io.vrap.rmf.codegen.kt.types.TypeNameSwitch


class TestCodeGenerator {

//
//    @Test
//    fun firstSteps(){
//        val generatorConfig = CodeGeneratorConfig(docTransformer = ::html,
//                ramlFileLocation = URI.createFileURI("/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/test/resources/api-spec/api.raml")
//        )
//
//        val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
//        val generatorComponent = GeneratorComponent(generatorModule)
//
//        val typeNameSwitch = generatorComponent.injector.getInstance(TypeNameSwitch::class.java)
//        val javaCodeGenerator = generatorComponent.injector.getInstance(JavaCodeGenerator::class.java)
//
//        javaCodeGenerator.allTypes
//                .map { typeNameSwitch.doSwitch(it) }
//                .filter { it is VrapObjectType }
//                .map { it as VrapObjectType }
//                .map { "${it.`package`}.${it.simpleClassName}" }
//                .sortedBy { it }
//                .map { println(it) }
//    }
}