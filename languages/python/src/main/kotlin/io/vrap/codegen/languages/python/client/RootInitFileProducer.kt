/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.codegen.languages.python.pyGeneratedComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.codegen.di.BasePackageName

class RootInitFileProducer constructor(
    val vrapTypeProvider: VrapTypeProvider,
    @BasePackageName
    val basePackageName: String
) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRootInitFile())
    }

    fun produceRootInitFile(): TemplateFile {
        return TemplateFile(
            relativePath = "$basePackageName/__init__.py",
            content = """|
                |$pyGeneratedComment
                |from .client import Client
            """.trimMargin()
        )
    }
}
