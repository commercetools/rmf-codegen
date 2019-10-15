package io.vrap.codegen.languages.typescript.client

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.returnType
import io.vrap.codegen.languages.typescript.*
import io.vrap.codegen.languages.typescript.client.files_producers.apiRequestExecutor
import io.vrap.codegen.languages.typescript.client.files_producers.apiRequest
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.StringType


class RequestBuilder @Inject constructor(
        @ClientPackageName val client_package: String,
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : ResourceRenderer, EObjectExtensions {

    override fun render(type: Resource): TemplateFile {

        val pakage = (type.toVrapType() as VrapObjectType).`package`

        return TemplateFile(
                relativePath = type.tsRequestModuleName(pakage).replace(".", "/") + ".ts",
                content = """|
                |${type.imports(type.tsRequestModuleName(pakage))}
                |import { ${apiRequestExecutor.simpleClassName}, ${apiRequest.simpleClassName} } from '${relativizePaths(type.tsRequestModuleName(pakage), apiRequestExecutor.`package`)}'
                |
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
                    |      apiRequestExecutor: ApiRequestExecutor;
                    |    }
                    |  ) {}
                    """.trimMargin()
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
                    |${it.getMethodName()}($args): ${it.toRequestBuilderName()} {
                    |   return new ${it.toRequestBuilderName()}(
                    |         {
                    |            pathArgs: {
                    |               ...this.args.pathArgs,
                    |               <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |            },
                    |            apiRequestExecutor: this.args.apiRequestExecutor
                    |         }
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }


    protected fun Resource.methods(): String {

        return this.methods
                .map {

                    var queryParamsArg = ""
                    var bodies = ""
                    if (!it.queryParameters.isEmpty()) {
                        val allQueryParamsOptional = it.queryParameters.map { !it.required }.reduce(Boolean::and)
                        queryParamsArg =
                                """|queryArgs${if (allQueryParamsOptional) "?" else ""}: {
                            |   <${it.queryParameters.map { "'${it.name.tsRemoveRegexp()}'${if (it.required) "" else "?"}: ${it.type.toVrapType().simpleTSName()} | ${it.type.toVrapType().simpleTSName()}[]" }.joinToString(separator = "\n")}>
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
                        |   baseURL: '${api.baseUri.template}',
                        |   method: '${it.methodName.toUpperCase()}',
                        |   uriTemplate: '${it.resource().fullUri.template}',
                        |   pathVariables: this.args.pathArgs,
                        |   headers: {
                        |       <${if (it.tsMediaType().isNotEmpty()) "${it.tsMediaType()}," else ""}>
                        |       ...(methodArgs || {} as any).headers
                        |   },
                        |   <${if (it.queryParameters.isNullOrEmpty()) "" else "queryParams: (methodArgs || {} as any).queryArgs,"}>
                        |   <${if (it.bodies.isNullOrEmpty()) "" else "body: (methodArgs || {} as any).body,"}>
                        |}
                    """.trimMargin()


                    """|
                    |${it.methodName}(<$methodArgs>): $methodReturn {
                    |   return new $methodReturn(
                    |       <$bodyLiteral>,
                    |       this.args.apiRequestExecutor
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }

    fun Method.headersPartInMethodSigniture(): String {
        return """
            |headers${if (this.headerIsRequired()) "" else "?"}: {
            |   <${this
                .headers
                .map { "'${it.name}': ${(it.type as StringType).enum.map { "'${it.value}'" }.joinToString(separator = " | ")}" }
                .joinToString(separator = "\n")
        }>
            |   [key:string]:string
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
                .map {
                    it.tsRequestVrapType(client_package)
                }.plus(
                        this.methods
                                .flatMap { method ->
                                    method.bodies
                                            .plus(
                                                    method.queryParameters
                                            )
                                }
                                .filter { it.type != null }
                                .map { it.type.toVrapType() }
                                .filter { it is VrapEnumType || it is VrapObjectType || (it is VrapArrayType && it.itemType is VrapObjectType) }
                )
                .plus(
                        this.methods
                                .map { it.returnType().toVrapType() }
                                .filter { it is VrapObjectType || (it is VrapArrayType && it.itemType is VrapObjectType) }

                )
                .map {
                    it.toImportStatement(moduleName)
                }
                .distinct()
                .joinToString(separator = "\n")
    }


}



