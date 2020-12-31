package io.vrap.codegen.languages.python.server
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.FileProducer

object TypescriptServerModule : AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        objectTypeBinder.addBinding().to(ParameterGenerator::class.java)
        objectTypeBinder.addBinding().to(ServerHelpers::class.java)
        objectTypeBinder.addBinding().to(ServerRenderer::class.java)
    }
}
