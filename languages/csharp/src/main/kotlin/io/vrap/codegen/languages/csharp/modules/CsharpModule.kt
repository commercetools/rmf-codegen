package io.vrap.codegen.languages.csharp.modules

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.csharp.model.CsharpModelClassRenderer
import io.vrap.codegen.languages.csharp.model.CsharpStringTypeRenderer
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeRenderer

class CsharpModule : AbstractModule() {

    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(CsharpModelClassRenderer::class.java)


        val stringTypeBinder = Multibinder.newSetBinder(binder(), StringTypeRenderer::class.java)
        stringTypeBinder.addBinding().to(CsharpStringTypeRenderer::class.java)
    }

}