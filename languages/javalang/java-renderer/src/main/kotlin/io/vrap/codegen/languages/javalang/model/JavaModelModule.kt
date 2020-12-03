package io.vrap.codegen.languages.javalang.model

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.*

object JavaModelModule: AbstractModule() {
    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(ObjectTypeGenerator::class.java)
        generators.addBinding().to(StringTypeGenerator::class.java)

        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(JavaObjectTypeRenderer::class.java)

        val stringTypeBinder = Multibinder.newSetBinder(binder(), StringTypeRenderer::class.java)
        stringTypeBinder.addBinding().to(JavaStringTypeRenderer::class.java)
    }
}
