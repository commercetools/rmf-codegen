package io.vrap.rmf.codegen.kt.rendring

import io.vrap.rmf.codegen.kt.io.TemplateFile


/**
 * Static file producer, usually files that don't map any data from the raml specification
 */
interface FileProducer {

    fun produceFiles():List<TemplateFile>

}