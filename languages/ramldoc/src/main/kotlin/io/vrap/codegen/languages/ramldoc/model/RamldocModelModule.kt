package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.ramldoc.extensions.isScalar
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.di.OasGeneratorModule
import io.vrap.rmf.codegen.rendering.*
import io.vrap.rmf.raml.model.types.*

object RamldocModelModule : Module {
    override fun configure(generatorModule: OasGeneratorModule) = setOf<CodeGenerator>(
        FileGenerator(
            setOf(
                OasApiRamlRenderer(generatorModule.apiProvider.api)
            )
        )
    )

    override fun configure(generatorModule: RamlGeneratorModule) = setOf(
        ObjectTypeGenerator(
                setOf(
                        RamlObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ),
                generatorModule.allObjectTypes()
        ),
        StringTypeGenerator(
                setOf(
                        RamlStringTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ),
                generatorModule.allEnumStringTypes()
        ),
        PatternStringTypeGenerator(
                setOf(
                        RamlStringTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ), generatorModule.allPatternStringTypes()),
        NamedStringTypeGenerator(
                setOf(
                        RamlStringTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ), generatorModule.allNamedScalarTypes()),
        NamedScalarTypeGenerator(
                setOf(
                        RamlAnyTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.provideModelPackageName())
                ), generatorModule.allAnyTypes().namedAnyTypes()),
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

    fun List<AnyType>.namedAnyTypes(): List<AnyType> {
        return this.filter { !it.isScalar() && !(it is ObjectType) && !(it is ArrayType) && !(it is UnionType) && !(it is IntersectionType) && !(it is NilType)}
    }
}
