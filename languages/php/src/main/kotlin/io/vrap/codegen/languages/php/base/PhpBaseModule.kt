
package io.vrap.codegen.languages.php.base

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.ResourceRenderer

class PhpBaseModule: AbstractModule() {

    override fun configure() {
        val fileBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileBinder.addBinding().to(PhpBaseFileProducer::class.java)
        fileBinder.addBinding().to(PhpBaseTestFileProducer::class.java)
    }
}
