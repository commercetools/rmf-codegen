package io.vrap.rmf.codegen.rendring

import io.vrap.rmf.codegen.io.TemplateFile


/**
 * Static file producer, usually files that don't map any data from the raml specification
 */
interface FileProducer {

    fun produceFiles():List<TemplateFile>

}