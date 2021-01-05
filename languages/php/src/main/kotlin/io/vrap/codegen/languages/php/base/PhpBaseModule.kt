
package io.vrap.codegen.languages.php.base

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.*

class PhpBaseModule: AbstractModule() {

    override fun configure() {
        val generators = Multibinder.newSetBinder(binder(), CodeGenerator::class.java)
        generators.addBinding().to(FileGenerator::class.java)

        val fileBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileBinder.addBinding().to(PhpBaseFileProducer::class.java)
        fileBinder.addBinding().to(PhpBaseTestFileProducer::class.java)
    }
}
