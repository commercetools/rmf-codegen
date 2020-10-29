package io.vrap.codegen.languages.csharp.modules

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.csharp.requests.CsharpHttpRequestRenderer
import io.vrap.codegen.languages.csharp.requests.CsharpRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.rendring.*


object CsharpClientBuilderModule: AbstractModule() {
    override fun configure() {
       val resourceTypeBinder = Multibinder.newSetBinder(binder(), ResourceRenderer::class.java)
       resourceTypeBinder.addBinding().to(CsharpRequestBuilderResourceRenderer::class.java)
       val methodTypeBinder = Multibinder.newSetBinder(binder(), MethodRenderer::class.java)
        methodTypeBinder.addBinding().to(CsharpHttpRequestRenderer::class.java)
    }
}
