package io.vrap.codegen.languages.csharp.modules

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.csharp.model.CsharpModelInterfaceRenderer
import io.vrap.codegen.languages.csharp.model.CsharpObjectTypeRenderer
import io.vrap.codegen.languages.csharp.model.CsharpStringTypeRenderer
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeRenderer

object CsharpModule: AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(CsharpModelInterfaceRenderer::class.java)
        objectTypeBinder.addBinding().to(CsharpObjectTypeRenderer::class.java)

        val stringTypeBinder = Multibinder.newSetBinder(binder(), StringTypeRenderer::class.java)
        stringTypeBinder.addBinding().to(CsharpStringTypeRenderer::class.java)
    }
}
