package io.vrap.codegen.languages.java.commands

import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.java.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import java.nio.file.Path
import java.nio.file.Paths

class TypescriptClientRenderer @javax.inject.Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, EObjectExtensions, TsObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val moduleName = "${vrapType.`package`}.${type.resource().resourcePathName}.${type.toRequestName()}".replace(".", "/")

        val content = """
            |/* tslint:disable */
            |//Generated file, please do not change
            |
            |${type.imports(moduleName)}
            |import { CommonRequest } from '${relativizePaths(moduleName,"base/requests-utils")}'
            |
            |export interface ${type.toRequestName()} extends CommonRequest\<${type.bodyType()}, ${type.returnType().toVrapType().simpleTSName()}\> {
            |
            |   method: '${type.methodName.toUpperCase()}',
            |
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
                relativePath = "$moduleName.ts",
                content = content
        )
    }


    fun Method.imports(moduleName: String): String {

        val importTypes = mutableListOf(this.returnType())
        importTypes.addAll(this.bodies.map { it.type })
        return importTypes
                .filter { it != null }
                .map { it.toVrapType() }
                .filter { it is VrapObjectType  }
                .map { it as VrapObjectType }
                .map { "import { ${it.simpleTSName()} } from '${relativizePaths(moduleName, it.`package`.replace(".","/"))}'" }
                .joinToString(separator = "\n")

    }

    fun Method.body():String{
        return if (this.bodies != null && this.bodies.isNotEmpty()) {

            "payload: ${this.bodyType()}"
        } else {
            ""
        }
    }

    fun Method.bodyType():String {
        return if (this.bodies != null && this.bodies.isNotEmpty()) {
            return this.bodies
                    .map { it.type }
                    .filter { it != null }
                    .map { it.toVrapType().simpleTSName() }
                    .joinToString(separator = " | ")

        } else {
            "void"
        }
    }

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
            |   <${this.queryParameters.map { "${it.name}${if (!it.required) "?" else ""}: ${it.type.toVrapType().simpleTSName()}" }.joinToString(separator = ",\n")}>
            |}
        """.trimMargin()
    }

    private fun relativizePaths(currentModule:String, targetModule : String) : String {
        val currentRelative : Path = Paths.get(currentModule)
        val targetRelative : Path = Paths.get(targetModule)
        return currentRelative.relativize(targetRelative).toString().replaceFirst("../","")
    }

    private fun Method.pathArguments(): List<String> {
        val urlPathParts = this.resource().fullUri.template.split("/").filter { it.isNotEmpty() }
        return urlPathParts.filter { it.startsWith("{") && it.endsWith("}") }.map { it.replace("{", "").replace("}", "") }
    }

    private fun Method.queryParametersList(): List<String> = this.queryParameters.map { it.name }

}