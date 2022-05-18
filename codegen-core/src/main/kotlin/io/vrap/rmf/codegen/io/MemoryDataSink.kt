package io.vrap.rmf.codegen.io;

import com.google.common.collect.Maps

class MemoryDataSink: DataSink {
    val files: MutableMap<String, String> = mutableMapOf()

    override fun write(templateFile: TemplateFile) {
        files.put(templateFile.relativePath, templateFile.content)
    }

    override fun postClean() {
//        files.clear()
    }


}
