package io.vrap.rmf.codegen.io

import com.google.inject.Inject
import java.io.File
import java.nio.file.Path

class FileDataSink @Inject constructor(val outputFolder:Path): DataSink {


    override fun write(templateFile: TemplateFile) {
        val outputFile = File("$outputFolder/${templateFile.relativePath}")
        outputFile.parentFile.mkdirs()
        outputFile.createNewFile()
        outputFile.bufferedWriter().use { it.write(templateFile.content) }
    }

    override fun clean() = outputFolder.toFile().deleteRecursively()


}
