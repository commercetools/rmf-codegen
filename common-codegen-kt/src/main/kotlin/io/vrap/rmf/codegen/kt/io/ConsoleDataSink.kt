package io.vrap.rmf.codegen.kt.io

import org.slf4j.LoggerFactory
import java.util.logging.Logger

object ConsoleDataSink : DataSink{

    private val LOGGER = LoggerFactory.getLogger(ConsoleDataSink::class.java)

    override fun write(templateFile: TemplateFile) {
        val result = """
            |____________________________________________________________________________________________________________________________
            |file : ${templateFile.relativePath}
            |content :
            |____________________________________________________________________________________________________________________________
            |
            |${templateFile.content}
            |
        """.trimMargin()

        LOGGER.info(result)
    }

}