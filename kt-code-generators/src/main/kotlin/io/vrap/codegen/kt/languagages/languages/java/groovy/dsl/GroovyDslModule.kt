package io.vrap.codegen.kt.languagages.languages.java.groovy.dsl

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.kt.core.ObjectTypeRenderer

class GroovyDslModule: AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(GroovyDslGenerator::class.java)
    }
}