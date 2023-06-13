
package io.vrap.codegen.languages.csharp.predicates

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object CsharpQueryPredicateModule: Module {
    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator> (
        ObjectTypeGenerator(
                setOf(
                    CsharpQueryPredicateRenderer(generatorModule.providePackageName(), generatorModule.vrapTypeProvider())
                ), generatorModule.allObjectTypes()
        )
    )
}
