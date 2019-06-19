package io.vrap.codegen.languages.java.commands

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.java.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.typescript.client.files_producers.commonRequest
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.tsMediaType
import io.vrap.codegen.languages.typescript.tsRemoveRegexp
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.codegen.languages.typescript.tsRequestName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import java.nio.file.Path
import java.nio.file.Paths

class TypescriptMethodRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, EObjectExtensions, TsObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val moduleName = type.tsRequestModuleName(vrapType.`package`)

        val content = """
            |/* tslint:disable */
            |//Generated file, please do not change
            |
            |${type.imports(moduleName)}
            |import { ${commonRequest.simpleClassName} } from '${relativizePaths(moduleName, commonRequest.`package`)}'
            |
            |export interface ${type.tsRequestName()} extends CommonRequest\<${type.bodyType()}\> {
            |
            |   method: '${type.methodName.toUpperCase()}',
            |   headers: {
            |       <${type.tsMediaType()}>
            |     },
            |   uriTemplate: '${type.resource().fullUri.template}'
            |   <${type.pathVariables()}>
            |   <${type.queryParams()}>
            |
            |   <${type.body()}>
            |
            |}
            |
            """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${moduleName.replace(".", "/")}.ts",
                content = content
        )
    }

    fun Method.imports(moduleName: String): String {

        val importTypes = mutableListOf(this.returnType())
        importTypes.addAll(this.bodies.map { it.type })
        return importTypes
                .filter { it != null }
                .map { it.toVrapType() }
                .filter { it is VrapObjectType }
                .map { it as VrapObjectType }
                .map { "import { ${it.simpleTSName()} } from '${relativizePaths(moduleName, it.`package`)}'" }
                .joinToString(separator = "\n")

    }

    fun Method.body(): String {
        return if (this.hasNonEmptyBody()) {
            "payload: ${this.bodyType()}"
        } else {
            ""
        }
    }

    fun Method.bodyType(): String {
        return if (this.hasNonEmptyBody()) {
            return this.bodies
                    .map { it.type }
                    .filter { it != null }
                    .map { it.toVrapType().simpleTSName() }
                    .joinToString(separator = " | ")

        } else {
            "void"
        }
    }

    fun Method.hasNonEmptyBody() = this.bodies != null
            && this.bodies.isNotEmpty()
            && this.bodies.map { it.type != null }.reduce { acc, b -> acc && b }

    fun Method.pathVariables(): String {
        val pathArguments = this.pathArguments()
        if (pathArguments.isEmpty())
            return ""
        return """|
            |pathVariables: {
            |   <${pathArguments.map { "$it: string" }.joinToString(separator = ",\n")}>
            |}
        """.trimMargin()
    }

    fun Method.queryParams(): String {
        val queryParams = this.queryParametersList()
        if (queryParams.isEmpty())
            return ""
        return """|
            |queryParams: {
            |   <${this.queryParameters.map { "${it.name.tsRemoveRegexp()}${if (!it.required) "?" else ""}: ${it.type.toVrapType().simpleTSName()}" }.joinToString(separator = ",\n")}>
            |}
        """.trimMargin()
    }


    private fun relativizePaths(currentModule: String, targetModule: String): String {
        val currentRelative: Path = Paths.get(currentModule.replace(".", "/"))
        val targetRelative: Path = Paths.get(targetModule.replace(".", "/"))
        return currentRelative.relativize(targetRelative).toString().replaceFirst("../", "")
    }

    private fun Method.pathArguments(): List<String> = this.resource().fullUri.variables.toList()

    private fun Method.queryParametersList(): List<String> = this.queryParameters.map { it.name }

}