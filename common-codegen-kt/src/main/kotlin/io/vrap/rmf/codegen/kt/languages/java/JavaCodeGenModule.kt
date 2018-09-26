package io.vrap.rmf.codegen.kt.languages.java

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.kt.core.ObjectTypeRenderer

class JavaCodeGenModule:AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(JavaObjectTypeRenderer::class.java)
    }
}