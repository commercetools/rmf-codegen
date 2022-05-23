package io.vrap.codegen.languages.javalang.model

import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*

object JavaModelModule: Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
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
