package io.vrap.rmf.codegen.io

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File
import java.nio.file.Path
import java.security.MessageDigest
import java.io.InputStream
import java.lang.Exception
import kotlin.io.path.exists

class FileDataSink constructor(val outputFolder:Path, private val dryRun: Boolean = false): DataSink {

    private val generatedFiles = mutableListOf<File>()

    override fun write(templateFile: TemplateFile) {
        val outputFile = File("$outputFolder/${templateFile.relativePath}")
        val content = templateFile.content.trimEnd().plus("\n")
        if (outputFile.exists()) {
            val fileHash = checksum(outputFile.inputStream())
            val templateHash = checksum(content.byteInputStream())
            if (fileHash != null && templateHash != null && fileHash.contentEquals(templateHash)) {
                generatedFiles.add(outputFile)
                return
            }
        }
        outputFile.parentFile.mkdirs()
        outputFile.createNewFile()
        outputFile.bufferedWriter().use { it.write(content) }
        generatedFiles.add(outputFile)
    }

    fun checksum(input: InputStream): ByteArray? {
        try {
            input.use { `in` ->
                val digest = MessageDigest.getInstance("SHA1")
                val block = ByteArray(4096)
                var length: Int
                while (`in`.read(block).also { length = it } > 0) {
                    digest.update(block, 0, length)
                }
                return digest.digest()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun postClean() {
        if (!outputFolder.exists()) {
            return;
        }
        val files = FileUtils.listFiles(
                outputFolder.toFile(),
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE
        ).filterNot { generatedFiles.contains(it) }

        files.map { it.delete() }
    }

    override fun dryRun(): Boolean {
        return dryRun
    }
}
