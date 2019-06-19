package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.resource
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.codegen.languages.typescript.client.files_producers.middleware
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.tsMediaType
import io.vrap.codegen.languages.typescript.tsRemoveRegexp
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.codegen.languages.typescript.tsRequestName
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractRequestBuilder constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : EObjectTypeExtensions {


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
                    |      middlewares: Middleware[];
                    |    }
                    |  ) {}
                    """.trimMargin()
    }

    protected fun Resource.methods(): String {

        return this.methods
                .map {
                    val methodArgs = if (it.queryParameters.isNullOrEmpty() && it.bodies.isNullOrEmpty())
                        ""
                    else {
                        var queryParamsArg = ""
                        var bodies = ""
                        if (!it.queryParameters.isNullOrEmpty()) {
                            val allQueryParamsOptional = it.queryParameters.map { !it.required }.reduce(Boolean::and)
                            queryParamsArg =
                                    """|queryArgs${if (allQueryParamsOptional) "?" else ""}: {
                            |   <${it.queryParameters.map { "${it.name.tsRemoveRegexp()}${if (it.required) "" else "?"}: ${it.type.toVrapType().simpleTSName()}" }.joinToString(separator = "\n")}>
                            |},""".trimMargin()
                        }
                        if (!it.bodies.isNullOrEmpty()) {
                            bodies = "payload: ${it.bodies.map {
                                it.type.toVrapType().simpleTSName()
                            }.joinToString(separator = " | ")}"
                        }

                        val argsAreOptional =
                                it.bodies.isNullOrEmpty()
                                        && (
                                        it.queryParameters.isNullOrEmpty() ||
                                                it.queryParameters.map { !it.required }.reduce(Boolean::and)
                                        )

                        """ |
                            |methodArgs${if (argsAreOptional) "?" else ""}:{
                            |   <$queryParamsArg>
                            |   <$bodies>
                            |}
                        """.trimMargin()
                    }
                    val methodReturn = "ApiRequest\\<${it.bodies.map { it.type.toVrapType().simpleTSName() }.joinToString(separator = " | ").ifEmpty { "void" }}, ${it.returnType().toVrapType().simpleTSName()}, ${it.tsRequestName()}\\>"

                    val bodyLiteral = """|{
                        |   method: '${it.methodName.toUpperCase()}',
                        |   uriTemplate: '${it.resource().fullUri.template}',
                        |   pathVariables: this.args.pathArgs,
                        |   <${if(it.tsMediaType().isNotEmpty()) "${it.tsMediaType()}," else ""}>
                        |   <${if(it.queryParameters.isNullOrEmpty()) "" else "queryParams: (methodArgs || {} as any).queryArgs,"}>
                        |   <${if(it.bodies.isNullOrEmpty()) "" else "payload: (methodArgs || {} as any).payload,"}>
                        |}
                    """.trimMargin()


                    """|
                    |${it.methodName}(<$methodArgs>): $methodReturn {
                    |   return new $methodReturn(
                    |       <$bodyLiteral>,
                    |       this.args.middlewares
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }

    protected fun Resource.subResources(): String {
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
                    |            middlewares: this.args.middlewares
                    |         }
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }

    fun Resource.imports(moduleName: String): String {
        return this.methods
                .map {
                    val requestModuleName = it.tsRequestModuleName((it.toVrapType() as VrapObjectType).`package`)
                    val relativePath = relativizePaths(moduleName, requestModuleName)
                    "import { ${it.tsRequestName()} } from '$relativePath'"
                }.plus(
                        this.resources
                                .map {
                                    val relativePath = relativizePaths(moduleName, it.tsRequestModuleName((this.toVrapType() as VrapObjectType).`package`))
                                    "import { ${it.toRequestBuilderName()} } from '$relativePath'"
                                }
                ).plus(
                        this.methods
                                .flatMap { it.bodies }
                                .filter { it.type != null }
                                .map { it.type.toVrapType() }
                                .filter { it is VrapObjectType }
                                .map { it as VrapObjectType }
                                .map {
                                    val relativePath = relativizePaths(moduleName, it.`package`)
                                    "import { ${it.simpleTSName()} } from '$relativePath'"
                                }
                )
                .plus(
                        this.methods
                                .map { it.returnType().toVrapType() }
                                .filter { it is VrapObjectType }
                                .map { it as VrapObjectType }
                                .map {
                                    val relativePath = relativizePaths(moduleName, it.`package`)
                                    "import { ${it.simpleTSName()} } from '$relativePath'"
                                }
                )
                .plus(
                        "import { ${middleware.simpleClassName} } from '${relativizePaths(moduleName, middleware.`package`)}'"
                )
                .distinct()
                .joinToString(separator = "\n")
    }

    protected fun relativizePaths(currentModule: String, targetModule: String): String {
        val currentRelative: Path = Paths.get(currentModule.replace(".", "/"))
        val targetRelative: Path = Paths.get(targetModule.replace(".", "/"))
        return "./" + currentRelative.relativize(targetRelative).toString().replaceFirst("../", "")
    }

    protected fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"
}



