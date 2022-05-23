package io.vrap.codegen.languages.javalang.dsl

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.ObjectTypeGenerator

object GroovyDslModule: Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
            ObjectTypeGenerator(
                    setOf(
                            GroovyDslRenderer(generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allObjectTypes()
            )
    )
}
