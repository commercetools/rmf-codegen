package io.vrap.rmf.codegen.kt.io

object ConsoleDataSink : DataSink{


    override fun save(templateFile: TemplateFile) {
        val result = """
            |____________________________________________________________________________________________________________________________
            |file : ${templateFile.relativePath}
            |content :
            |____________________________________________________________________________________________________________________________
            |
            |${templateFile.content}
            |
        """.trimMargin()

        println(result)
    }

}