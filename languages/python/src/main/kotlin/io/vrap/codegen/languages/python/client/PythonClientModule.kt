package io.vrap.codegen.languages.python.client

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.python.client.files_producers.ApiRootFileProducer
import io.vrap.codegen.languages.python.client.files_producers.ClientFileProducer
import io.vrap.codegen.languages.python.client.files_producers.IndexFileProducer
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.ResourceRenderer

object PythonClientModule : AbstractModule() {
    override fun configure() {

        Multibinder.newSetBinder(binder(), ResourceRenderer::class.java).addBinding().to(RequestBuilder::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(ApiRootFileProducer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(ClientFileProducer::class.java)
        Multibinder.newSetBinder(binder(), FileProducer::class.java).addBinding().to(IndexFileProducer::class.java)

    }
}
