package io.vrap.codegen.languages.javalang.model

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object JavaModelModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf(
            ObjectTypeGenerator(
                    setOf(
                            JavaObjectTypeRenderer(generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allObjectTypes()
            ),
            StringTypeGenerator(
                    setOf(
                            JavaStringTypeRenderer(generatorModule.vrapTypeProvider())
                    ),
                    generatorModule.allEnumStringTypes()
            )
    )
}
