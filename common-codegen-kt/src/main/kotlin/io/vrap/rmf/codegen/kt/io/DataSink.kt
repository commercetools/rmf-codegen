package io.vrap.rmf.codegen.kt.io

interface DataSink {

    fun save(templateFile: TemplateFile)

    fun clean():Boolean = true

}