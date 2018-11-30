package io.vrap.rmf.codegen.io

interface DataSink {

    fun write(templateFile: TemplateFile)

    fun clean():Boolean = true

}