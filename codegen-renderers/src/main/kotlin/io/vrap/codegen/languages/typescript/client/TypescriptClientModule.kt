package io.vrap.codegen.languages.typescript.client

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.java.commands.TypescriptMethodRenderer
import io.vrap.codegen.languages.typescript.client.files_producers.EntryPointFileProducer
import io.vrap.codegen.languages.typescript.client.files_producers.TsClientFileProducer
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.ResourceRenderer

class TypescriptClientModule : AbstractModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), MethodRenderer::class.java).addBinding().to(TypescriptMethodRenderer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(TsClientFileProducer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(EntryPointFileProducer::class.java)
        Multibinder.newSetBinder(binder(), ResourceRenderer::class.java).addBinding().to(RequestBuilder::class.java)
    }
}