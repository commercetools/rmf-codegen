package io.vrap.codegen.languages.typescript.client.files_producers

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.codegen.languages.typescript.toRequestBuilderName
import io.vrap.codegen.languages.typescript.toTsComment
import io.vrap.codegen.languages.typescript.tsRequestVrapType
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.ResourceContainer

class ApiRootFileProducer constructor(
        @ClientPackageName val client_package: String,
        val clientConstants: ClientConstants,
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : FileProducer, TsObjectTypeExtensions {


    override fun produceFiles(): List<TemplateFile> {

        return listOf(produceApiRoot(api))
    }

    fun produceApiRoot(type: Api): TemplateFile {
        val moduleName = clientConstants.apiRoot
        return TemplateFile(
                relativePath = "$moduleName.ts",
                content = """|
                |$tsGeneratedComment
                |${type.imports(moduleName)}
                |
                |export class ApiRoot {
                |
                |  private executeRequest: executeRequest;
                |  private baseUri: string;
                |
                |  constructor(args: {
                |    executeRequest: executeRequest;
                |    baseUri?: string;
                |  }) {
                |    this.executeRequest = args.executeRequest
                |    this.baseUri = args.baseUri${if(api.baseUri?.template.isNullOrEmpty()) "" else " ?? '${api.baseUri?.template}'"}
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
                    |<${it.toTsComment().escapeAll()}>
                    |public ${it.getMethodName()}($args): ${it.toRequestBuilderName()} {
                    |   return new ${it.toRequestBuilderName()}(
                    |         {
                    |            pathArgs: {
                    |               <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |            },
                    |            executeRequest: this.executeRequest,
                    |            baseUri: this.baseUri
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
                .plus(
                        listOf(
                                VrapObjectType(clientConstants.commonTypesPackage,"QueryParam"),
                                VrapObjectType(clientConstants.commonTypesPackage,"executeRequest"),
                                VrapObjectType(clientConstants.requestUtilsPackage,"ApiRequest")
                        )
                )
                .getImportsForModuleVrapTypes(moduleName)
    }

}
