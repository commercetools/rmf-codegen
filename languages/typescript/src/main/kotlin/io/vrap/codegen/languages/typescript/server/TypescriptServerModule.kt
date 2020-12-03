package io.vrap.codegen.languages.typescript.server
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.CodeGenerator
import io.vrap.rmf.codegen.rendring.FileGenerator
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.ResourceGenerator

object TypescriptServerModule : AbstractModule() {
    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        val objectTypeBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        objectTypeBinder.addBinding().to(ParameterGenerator::class.java)
        objectTypeBinder.addBinding().to(ServerHelpers::class.java)
        objectTypeBinder.addBinding().to(ServerRenderer::class.java)
    }
}
