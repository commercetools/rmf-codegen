package io.vrap.codegen.languages.ramldoc.model

import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object RamldocModelModule : Module {
    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
        ObjectTypeGenerator(
                setOf(
                        RamlObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ),
                generatorModule.allObjectTypes()
        ),
        StringTypeGenerator(
                setOf(
                        RamlScalarTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ),
                generatorModule.allEnumStringTypes()
        ),
        PatternStringTypeGenerator(
                setOf(
                        RamlScalarTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ), generatorModule.allPatternStringTypes()),
        NamedScalarTypeGenerator(
                setOf(
                        RamlScalarTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ), generatorModule.allNamedScalarTypes()),
        ResourceGenerator(
                setOf(
                        RamlResourceRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider())
                ), generatorModule.allResources()),
        FileGenerator(
                setOf(
                        ApiRamlRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.allAnyTypes(), generatorModule.provideModelPackageName()),
                        RamlExampleRenderer(generatorModule.allResourceMethods(), generatorModule.allAnyTypes(), generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ))
    )
}
