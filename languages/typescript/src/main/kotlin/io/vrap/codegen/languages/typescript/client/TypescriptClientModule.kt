package io.vrap.codegen.languages.typescript.client

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.typescript.client.files_producers.ApiRootFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.ClientFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.IndexFileProducer
import io.vrap.rmf.codegen.rendring.*

object TypescriptClientModule : AbstractModule() {
    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(ResourceGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        Multibinder.newSetBinder(binder(), ResourceRenderer::class.java).addBinding().to(RequestBuilder::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(ApiRootFileProducer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(ClientFileProducer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(IndexFileProducer::class.java)

    }
}
