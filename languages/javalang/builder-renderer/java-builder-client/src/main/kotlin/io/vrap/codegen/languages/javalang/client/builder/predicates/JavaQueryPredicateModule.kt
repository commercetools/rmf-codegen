
package io.vrap.codegen.languages.javalang.client.builder.predicates

import io.vrap.codegen.languages.java.base.extensions.isExpandable
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object JavaQueryPredicateModule: Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
        ObjectTypeGenerator(
                setOf(
                        JavaQueryPredicateRenderer(generatorModule.providePackageName(), generatorModule.vrapTypeProvider())
                ), generatorModule.allObjectTypes()
        ),
        ObjectTypeGenerator(
                setOf(
                        JavaExpansionPredicateRenderer(generatorModule.providePackageName(), generatorModule.vrapTypeProvider())
                ), generatorModule.allObjectTypes().filter { it.isExpandable() }
        ),
        FileGenerator(
                setOf(JavaExpansionProducer(generatorModule.providePackageName()))
        )
    )
}
