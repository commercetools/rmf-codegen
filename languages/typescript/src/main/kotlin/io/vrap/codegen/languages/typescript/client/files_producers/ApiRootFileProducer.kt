package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.toImportStatement
import io.vrap.codegen.languages.typescript.toRequestBuilderName
import io.vrap.codegen.languages.typescript.tsRequestVrapType
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.ResourceContainer

class ApiRootFileProducer @Inject constructor(
        @ClientPackageName val client_package: String,
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : FileProducer, TsObjectTypeExtensions {


    override fun produceFiles(): List<TemplateFile> {

        return listOf(produceApiRoot(api))
    }

    fun produceApiRoot(type: Api): TemplateFile {
        val moduleName = "$client_package/api-root"
        return TemplateFile(
                relativePath = "$moduleName.ts",
                content = """|
                |${type.imports(moduleName)}
                |import { Middleware } from '../base/common-types'
                |import { ApiRequestExecutor } from '../base/requests-utils'
                |
                |export class ApiRoot {
                |  private apiRequestExecutor: ApiRequestExecutor
                |  constructor(args: { middlewares: Middleware[] }) {
                |    this.apiRequestExecutor = new ApiRequestExecutor(args.middlewares)
                |  }
                |
                |  <${type.subResources()}>
                |
                |}
                |
            """.trimMargin().keepIndentation()

        )
    }

    protected fun ResourceContainer.subResources(): String {
        return this.resources
                .map {

                    val args = if (it.relativeUri.variables.isNullOrEmpty()) "" else """|
                        |   childPathArgs: {
                        |       <${it.relativeUri.variables.map { "$it: string" }.joinToString(separator = "\n")}>
                        |   }
                        |
                    """.trimMargin()

                    """|
                    |public ${it.getMethodName()}($args): ${it.toRequestBuilderName()} {
                    |   return new ${it.toRequestBuilderName()}(
                    |         {
                    |            pathArgs: {
                    |               <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |            },
                    |            apiRequestExecutor: this.apiRequestExecutor
                    |         }
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }


    fun Api.imports(moduleName: String): String {
        return this.resources
                .map {
                    it.tsRequestVrapType(client_package)
                }
                .getImportsForModuleVrapTypes(moduleName)
    }

}