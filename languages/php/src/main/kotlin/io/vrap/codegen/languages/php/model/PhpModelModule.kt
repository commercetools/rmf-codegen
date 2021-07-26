
package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*

object PhpModelModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator> (
        ObjectTypeGenerator(
                setOf(
                        PhpInterfaceObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        PhpObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        PhpBuilderObjectTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        PhpCollectionRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allObjectTypes()
        ),
        UnionTypeGenerator(
                setOf(
                        PhpUnionTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        PhpInterfaceUnionTypeRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        PhpUnionCollectionRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allUnionTypes()
        ),
        FileGenerator(
                setOf(
                        PhpFileProducer(generatorModule.provideRamlModel(), generatorModule.clientConstants()),
                        ApiRootFileProducer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants()),
                        DocsProducer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                )
        ),
        MethodGenerator(
                setOf(
                        PhpMethodRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allResourceMethods()
        ),
        ResourceGenerator(
                setOf(
                        PhpMethodBuilderRenderer(generatorModule.provideRamlModel(), generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
                ),
                generatorModule.allResources()
        ),
        TraitGenerator(setOf(
                PhpTraitRenderer(generatorModule.vrapTypeProvider(), generatorModule.clientConstants())
        ), generatorModule.allTraits())
    )

    private fun GeneratorModule.clientConstants() =
            ClientConstants(this.provideSharedPackageName(), this.provideClientPackageName(), this.providePackageName())
}
