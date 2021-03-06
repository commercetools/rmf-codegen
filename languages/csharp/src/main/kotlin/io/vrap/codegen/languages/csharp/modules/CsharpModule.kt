package io.vrap.codegen.languages.csharp.modules

import io.vrap.codegen.languages.csharp.model.CsharpModelInterfaceRenderer
import io.vrap.codegen.languages.csharp.model.CsharpObjectTypeRenderer
import io.vrap.codegen.languages.csharp.model.CsharpStringTypeRenderer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object CsharpModule: Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
                ObjectTypeGenerator(
                        setOf(
                            CsharpModelInterfaceRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName()),
                            CsharpObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName())
                        ),
                        generatorModule.allObjectTypes()
                ),
                StringTypeGenerator(
                        setOf(
                            CsharpStringTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName())
                        ),
                        generatorModule.allEnumStringTypes()
                )
        )
}
