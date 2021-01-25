package io.vrap.rmf.codegen.io

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File
import java.nio.file.Path
import java.util.concurrent.TimeUnit

class FileDataSink constructor(val outputFolder:Path): DataSink {

    private val generatedFiles = mutableListOf<File>()

    override fun write(templateFile: TemplateFile) {
        val outputFile = File("$outputFolder/${templateFile.relativePath}")
        outputFile.parentFile.mkdirs()
        outputFile.createNewFile()
        outputFile.bufferedWriter().use { it.write(templateFile.content.trimEnd().plus("\n")) }
        generatedFiles.add(outputFile)
    }

    override fun postClean() {
        val files = FileUtils.listFiles(
                outputFolder.toFile(),
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE
        ).filterNot { generatedFiles.contains(it) }

        files.map { it.delete() }
    }
}
