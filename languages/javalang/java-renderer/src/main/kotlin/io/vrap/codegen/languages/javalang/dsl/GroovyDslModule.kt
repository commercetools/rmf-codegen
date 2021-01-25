package io.vrap.codegen.languages.javalang.dsl

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.ObjectTypeGenerator

object GroovyDslModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf(
            ObjectTypeGenerator(
                    setOf(
                            GroovyDslRenderer(generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allObjectTypes()
            )
    )
}
