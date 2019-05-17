package io.vrap.codegen.languages.typescript.client

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.resource
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.tsMediaType
import io.vrap.codegen.languages.typescript.tsRemoveRegexp
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.codegen.languages.typescript.tsRequestName
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import java.nio.file.Path
import java.nio.file.Paths


class RequestBuilder @Inject constructor(
        @Named(VrapConstants.CLIENT_PACKAGE_NAME) val client_package: String,
        override val vrapTypeProvider: VrapTypeProvider
) : ResourceRenderer, EObjectTypeExtensions {

    @Inject
    lateinit var api: Api

    override fun render(type: Resource): TemplateFile {


        return TemplateFile(
                relativePath = type.toRequestBuilderModuleName().replace(".", "/") + ".ts",
                content = """|
                |${type.imports(type.toRequestBuilderModuleName())}
                |import { ApiRequest } from '${relativizePaths(type.toRequestBuilderModuleName(),"base.requests-utils")}'
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


    private fun Resource.constructor(): String {

        val constructorArgs = if (this.fullUri.variables.isEmpty())
            ""
        else
            this
                    .fullUri
                    .variables
                    .map { "      $it: string" }
                    .joinToString(separator = ",\n", prefix = "\n  private readonly pathArgs: {\n", postfix = "\n  }\n")

        return """|
        |constructor($constructorArgs){}
        """.trimMargin()
    }

    private fun Resource.methods(): String {

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
                        |   pathVariables: this.pathArgs,
                        |   <${if(it.tsMediaType().isNotEmpty()) "${it.tsMediaType()}," else ""}>
                        |   <${if(it.queryParameters.isNullOrEmpty()) "" else "queryParams: methodArgs.queryArgs,"}>
                        |   <${if(it.bodies.isNullOrEmpty()) "" else "payload: methodArgs.payload,"}>
                        |}
                    """.trimMargin()


                    """|
                    |${it.methodName}(<$methodArgs>): $methodReturn {
                    |   return new $methodReturn(
                    |       <$bodyLiteral>
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }

    private fun Resource.subResources(): String {
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
                    |       {
                    |          ...this.pathArgs,
                    |          <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |       }
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
                                    val relativePath = relativizePaths(moduleName, it.toRequestBuilderModuleName())
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
                .distinct()
                .joinToString(separator = "\n")
    }

    private fun relativizePaths(currentModule: String, targetModule: String): String {
        val currentRelative: Path = Paths.get(currentModule.replace(".", "/"))
        val targetRelative: Path = Paths.get(targetModule.replace(".", "/"))
        return "./" + currentRelative.relativize(targetRelative).toString().replaceFirst("../", "")
    }

    private fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"
    private fun Resource.toRequestBuilderModuleName(): String = "$client_package.request-builder.${this.toRequestBuilderName()}"
}



