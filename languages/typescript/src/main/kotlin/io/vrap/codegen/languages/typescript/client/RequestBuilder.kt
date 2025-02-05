package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.typescript.*
import io.vrap.codegen.languages.typescript.client.files_producers.ClientConstants
import io.vrap.codegen.languages.typescript.markDeprecated
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.BooleanInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringType
import java.util.*


class RequestBuilder constructor(
        @ClientPackageName val client_package: String,
        private val clientConstants: ClientConstants,
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : ResourceRenderer, TsObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {

        val `package` = (type.toVrapType() as VrapObjectType).`package`

        return TemplateFile(
                relativePath = type.tsRequestModuleName(`package`) + ".ts",
                content = """|
                |$tsGeneratedComment
                |${type.imports(type.tsRequestModuleName(`package`))}
                |/**${if (type.markDeprecated() ) """
                |@deprecated""" else ""}
                |**/
                |export class ${type.toRequestBuilderName()} {
                |
                |    <${type.constructor()}>
                |    <${type.subResources()}>
                |    <${type.methods()}>
                |
                |}
                |
            """.trimMargin().keepIndentation()

        )
    }


    protected fun Resource.constructor(): String {

        val pathArgs = if (this.fullUri.variables.isEmpty())
            ""
        else
            this
                    .fullUri
                    .variables
                    .map { "      $it: string" }
                    .joinToString(
                            separator = ",\n",
                            prefix = "pathArgs: {\n",
                            postfix = "\n }")

        return """|
                    |  constructor(
                    |    protected readonly args: {
                    |      <$pathArgs>,
                    |      executeRequest: executeRequest,
                    |      baseUri?: string
                    |    }
                    |  ) {}
                    """.trimMargin()
    }

    protected fun ResourceContainer.subResources(): String {
        return this.resources
                .filterNot { it.deprecated() }
                .map {

                    val args = if (it.relativeUri.variables.isNullOrEmpty()) "" else """|
                        |   childPathArgs: {
                        |       <${it.relativeUri.variables.map { "$it: string" }.joinToString(separator = "\n")}>
                        |   }
                        |
                    """.trimMargin()

                    """
                    |<${it.toTsComment().plus(if (it.markDeprecated() ) """
                    |/** 
                    |* @deprecated
                    |**/""" else "").escapeAll()}>
                    |public ${it.getMethodName()}($args): ${it.toRequestBuilderName()} {
                    |   return new ${it.toRequestBuilderName()}(
                    |         {
                    |            pathArgs: {
                    |               ...this.args.pathArgs,
                    |               <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |            },
                    |            executeRequest: this.args.executeRequest,
                    |            baseUri: this.args.baseUri
                    |         }
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }


    protected fun Resource.methods(): String {

        return this.methods
                .filterNot { it.deprecated() }
                .map {

                    var queryParamsArg = ""
                    var bodies = ""
                    if (!it.queryParameters.isEmpty()) {
                        val allQueryParamsOptional = it.queryParameters.map { !it.required }.reduce(Boolean::and)
                        queryParamsArg =
                                """|queryArgs${if (allQueryParamsOptional) "?" else ""}: {
                            |   <${it.queryParameters.filter { !it.isPatternProperty() }.map { "'${it.name}'${if (it.required) "" else "?"}: ${it.parameterType()}" }.joinToString(separator = "\n")}>
                            |   [key: string]: QueryParam
                            |},""".trimMargin()
                    }
                    if (!it.bodies.isEmpty()) {
                        bodies = "body: ${it.bodies.map {
                            it.type.toVrapType().simpleTSName()
                        }.joinToString(separator = " | ")},"
                    }

                    val argsAreOptional =
                            it.bodies.isNullOrEmpty()
                                    && (
                                    it.queryParameters.isNullOrEmpty() ||
                                            it.queryParameters.map { !it.required }.reduce(Boolean::and)
                                    )

                    val methodArgs = """ |
                            |methodArgs${if (argsAreOptional) "?" else ""}:{
                            |   
                            |   <$queryParamsArg>
                            |   <$bodies>
                            |   <${it.headersPartInMethodSigniture()}>
                            |}
                        """.trimMargin()

                    val methodReturn = "ApiRequest\\<${it.returnType().toVrapType().simpleTSName()}\\>"

                    val bodyLiteral = """|{
                        |   baseUri: this.args.baseUri,
                        |   method: '${it.methodName.uppercase(Locale.getDefault())}',
                        |   uriTemplate: '${it.resource().fullUri.template}',
                        |   pathVariables: this.args.pathArgs,
                        |   headers: {
                        |       <${if (it.tsMediaType().isNotEmpty()) "${it.tsMediaType()}," else ""}>
                        |       ...methodArgs?.headers
                        |   },
                        |   <${if (it.queryParameters.isNullOrEmpty()) "" else "queryParams: methodArgs?.queryArgs,"}>
                        |   <${if (it.bodies.isNullOrEmpty()) "" else "body: methodArgs?.body,"}>
                        |}
                    """.trimMargin()


                    """
                    |<${it.toTsComment().plus(if (it.markDeprecated() ) """
                    |/** 
                    |* @deprecated
                    |**/""" else "").escapeAll()}>
                    |public ${it.methodName}(<$methodArgs>): $methodReturn {
                    |   return new $methodReturn(
                    |       <$bodyLiteral>,
                    |       this.args.executeRequest
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }


    private fun QueryParameter.parameterType(): String {
        val vrapType = this.type.toVrapType()
        return when(vrapType) {
            is VrapArrayType -> "${vrapType.itemType.simpleTSName()} | ${vrapType.simpleTSName()}"
            else -> vrapType.simpleTSName()
        }
    }

    fun Method.headersPartInMethodSigniture(): String {
        return """
            |headers${if (this.headerIsRequired()) "" else "?"}: {
            |   <${this
                .headers
                .map { "'${it.name}': ${
                    if ((it.type as StringType).enum.isNotEmpty()) (it.type as StringType).enum.map { "'${it.value}'" }.joinToString(separator = " | ") else "string | string[]" }"
                }
                .joinToString(separator = "\n")
        }>
            |   [key:string]: string | string[]
            |},
        """.trimMargin()
    }

    fun Method.headerIsRequired(): Boolean {
        if (this.headers.isEmpty()) {
            return false
        }
        return this.headers.map { (it.type as StringType).enum.size > 1 }.reduce { acc, b -> acc || b }
    }


    fun Resource.imports(moduleName: String): String {
        return this.resources
            .asSequence()
            .filterNot { it.deprecated() }
            .map {
                it.tsRequestVrapType(client_package)
            }.plus(
                this.methods
                    .filterNot { it.deprecated() }
                    .flatMap { method ->
                        method.bodies
                            .plus(
                                method.queryParameters
                            )
                    }
                    .filter { it.type != null }
                    .map { it.type.toVrapType() }
                    .filter { it is VrapEnumType || it is VrapObjectType || (it is VrapArrayType && (it.itemType is VrapObjectType || it.itemType is VrapEnumType ) ) }
            )
            .plus(
                this.methods
                    .filterNot { it.deprecated() }
                    .map { it.returnType().toVrapType() }
                    .filter { it is VrapObjectType || (it is VrapArrayType && it.itemType is VrapObjectType) }

            )
            .plus(
                listOf(
                    VrapObjectType(clientConstants.commonTypesPackage, "QueryParam"),
                    VrapObjectType(clientConstants.commonTypesPackage, "executeRequest"),
                    VrapObjectType(clientConstants.requestUtilsPackage, "ApiRequest")
                )
            )
            .toList()
                .getImportsForModuleVrapTypes(moduleName, emptyList(), emptyList())
    }


}
