
package io.vrap.codegen.languages.php.test

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.codegen.languages.php.model.PhpFileProducer
import io.vrap.codegen.languages.php.test.PhpRequestTestRenderer
import io.vrap.rmf.codegen.rendring.*

class PhpTestModule: AbstractModule() {

    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(ResourceGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        val resourceBinder = Multibinder.newSetBinder(binder(), ResourceRenderer::class.java)
        resourceBinder.addBinding().to(PhpRequestTestRenderer::class.java)

        val fileBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileBinder.addBinding().to(PhpTestRenderer::class.java)
    }
}
