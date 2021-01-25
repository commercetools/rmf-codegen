package io.vrap.codegen.languages.javalang.dsl

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.ObjectTypeGenerator
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeGenerator

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
