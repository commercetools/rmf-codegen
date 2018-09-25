package io.vrap.rmf.codegen.kt.io

import io.vrap.rmf.codegen.kt.rendring.fixIndentation

object DataSink {


    fun save(data: TemplateFile) {
        val result = """
            |____________________________________________________________________________________________________________________________
            |file : ${data.relativePath}
            |content :
            |____________________________________________________________________________________________________________________________
            |
            |${data.content}
            |
        """.trimMargin()

        println(result)
    }

}