/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.codegen.languages.python.pyGeneratedComment
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType

class InitFileProducer constructor(
    override val vrapTypeProvider: VrapTypeProvider,
    @AllAnyTypes val allAnyTypes: List<AnyType>
) : FileProducer, PyObjectTypeExtensions {
    override fun produceFiles(): List<TemplateFile> = listOf(
        TemplateFile(
            relativePath = "models/__init__.py",
            content = """|
                |$pyGeneratedComment
                |
                |${allAnyTypes.exportModels()}
            """.trimMargin()
        )
    )

    fun List<AnyType>.exportModels(): String {
        return allAnyTypes
            .filter {
                it is ObjectType || (it is StringType && it.pattern == null)
            }
            .groupBy {
                it.moduleName()
            }
            .map {
                var moduleName = it.key.split(".").last()
                "from .$moduleName import *  # noqa"
            }
            .joinToString(separator = "\n")
    }
}
