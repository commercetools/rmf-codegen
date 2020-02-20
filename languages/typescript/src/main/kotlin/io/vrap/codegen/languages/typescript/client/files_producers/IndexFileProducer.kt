package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType

class IndexFileProducer @Inject constructor(
        private val clientConstants: ClientConstants,
        override val vrapTypeProvider: VrapTypeProvider,
        @AllAnyTypes val allAnyTypes: MutableList<AnyType>
) : FileProducer, TsObjectTypeExtensions {
    override fun produceFiles(): List<TemplateFile> = listOf(TemplateFile(
            relativePath = "${clientConstants.indexFile}.ts",
            content = """|
                |$tsGeneratedComment
                |
                |//models
                |${allAnyTypes.exportModels()}
                |
                |//Root client that is used to access all the endpoints in the API
                |export * from '${clientConstants.apiRoot}'
                |
                |//Common package
                |export * from '${clientConstants.commonTypesPackage}'
                |export * from '${clientConstants.middlewarePackage}'

            """.trimMargin()
    ))

    fun MutableList<AnyType>.exportModels() =
        this.filter { it is ObjectType || (it is StringType && it.pattern == null) }
                .map {
                    "export * from '${it.moduleName()}'"
                }
                .distinct()
                .joinToString(separator = "\n")


}