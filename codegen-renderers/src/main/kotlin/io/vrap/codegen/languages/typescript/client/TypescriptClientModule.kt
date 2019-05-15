package io.vrap.codegen.languages.typescript.client

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.java.commands.TypescriptClientRenderer
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.MethodRenderer

class TypescriptClientModule : AbstractModule() {
    override fun configure() {
        val resourceCollectionBinder = Multibinder.newSetBinder(binder(), MethodRenderer::class.java)
        resourceCollectionBinder.addBinding().to(TypescriptClientRenderer::class.java)

        val fileProducerBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileProducerBinder.addBinding().to(TsClientFileProducer::class.java)
    }
}