package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.raml.model.modules.Api

class EntryPointFileProducer @Inject constructor(val api: Api): FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceEntryPoint())
    }

    fun produceEntryPoint(): TemplateFile {
        var content = """|
            |
        """.trimMargin()



        return TemplateFile(content = content,relativePath = "base/entry-point.ts")

    }
}