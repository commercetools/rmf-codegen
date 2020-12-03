
package io.vrap.codegen.languages.php.model

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.*

class PhpModelModule: AbstractModule() {

    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(ObjectTypeGenerator::class.java)
        generators.addBinding().to(UnionTypeGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)
        generators.addBinding().to(MethodGenerator::class.java)
        generators.addBinding().to(ResourceGenerator::class.java)

        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(PhpInterfaceObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(PhpObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(PhpBuilderObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(PhpCollectionRenderer::class.java)

        val unionTypeBinder = Multibinder.newSetBinder(binder(), UnionTypeRenderer::class.java)
        unionTypeBinder.addBinding().to(PhpUnionTypeRenderer::class.java)
        unionTypeBinder.addBinding().to(PhpInterfaceUnionTypeRenderer::class.java)
        unionTypeBinder.addBinding().to(PhpUnionCollectionRenderer::class.java)

        val fileBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileBinder.addBinding().to(PhpFileProducer::class.java)
        fileBinder.addBinding().to(ApiRootFileProducer::class.java)
        fileBinder.addBinding().to(DocsProducer::class.java)

        val methodBinder = Multibinder.newSetBinder(binder(), MethodRenderer::class.java)
        methodBinder.addBinding().to(PhpMethodRenderer::class.java)

        val resourceBinder = Multibinder.newSetBinder(binder(), ResourceRenderer::class.java)
        resourceBinder.addBinding().to(PhpMethodBuilderRenderer::class.java)
    }
}
