package io.vrap.rmf.codegen.kt.io

interface DataSink {

    fun write(templateFile: TemplateFile)

    fun clean():Boolean = true

}