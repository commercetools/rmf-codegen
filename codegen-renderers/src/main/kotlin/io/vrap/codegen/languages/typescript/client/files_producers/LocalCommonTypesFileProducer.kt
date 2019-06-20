package io.vrap.codegen.languages.typescript.client.files_producers

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class LocalCommonTypesFileProducer : FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile {
        val content = """
import { PathLike } from "fs";

export type VFile = {
    filePath: PathLike
}

"""


        return TemplateFile(content = content,relativePath = "base/local-common-types.ts")

    }
}