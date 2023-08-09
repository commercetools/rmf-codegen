package io.vrap.codegen.languages.typescript


import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.typescript.joi.simpleJoiName
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.nio.file.Path
import java.nio.file.Paths


fun Method.tsRequestName(): String = "${this.toRequestName()}Request"
fun Method.tsRequestModuleName(clientPackageName: String): String = "${if (clientPackageName.isEmpty()) "" else "$clientPackageName."}${this.resource().resourcePathName}.${this.toRequestName()}Request"

fun VrapObjectType.tsModuleName(): String = "${this.`package`.replace(".", "/")}.ts"

fun Method.tsMediaType(): String {

    val headers = mutableMapOf<String, List<String>>()
    if (!this.bodies.isNullOrEmpty() && !this.bodies[0].contentMediaType.type().isNullOrEmpty()) {
        headers["Content-Type"] = listOf(this.bodies[0].contentMediaType.toString())
    }
    this.headers
            .map {
                it.name to (it.type as StringType).enum.map { it.value as String }
            }
            .toMap(headers)


    return headers
            .filter { it.value.size == 1 }
            .map {
                "'${it.key}': '${it.value[0]}'"
            }.joinToString(separator = "\n")

}

fun String.tsRemoveRegexp(): String {
    if (this.startsWith("/")) {
        val index = this.indexOf("\\")
        return this.substring(1, index)
    }
    if (this.contains(".")) {
        val index = this.indexOf(".")
        return this.substring(index + 1, this.length)
    }
    return this
}

fun DescriptionFacet.toTsComment(): String {
    val description = this.description
    return if (description?.value.isNullOrBlank()) {
        ""
    } else description.value
            .lines()
            .joinToString(prefix = "/**\n*\t", postfix = "\n*/", separator = "\n*\t")
}

fun DescriptionFacet.toTsCommentList(): List<String> {
    val description = this.description
    return if (description?.value.isNullOrBlank()) {
        return Lists.newArrayList()
    } else description.value
        .lines()
}

fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"

fun Resource.tsRequestModuleName(clientPackageName: String): String = "$clientPackageName${if(this.resourcePathName.isNullOrEmpty()) "" else "/${this.resourcePathName}"}/${StringCaseFormat.LOWER_HYPHEN_CASE.apply(this.toRequestBuilderName())}"

fun Resource.tsRequestVrapType(clientPackageName: String): VrapObjectType = VrapObjectType(`package` = this.tsRequestModuleName(clientPackageName), simpleClassName = this.toRequestBuilderName())

fun VrapType.toImportStatement(): String {
    return when (this) {
        is VrapLibraryType -> "import { ${this.simpleClassName} } from '${this.`package`}'"
        is VrapObjectType -> {
            "import { ${this.simpleTSName()} } from '${this.`package`}'"
        }
        is VrapEnumType -> {
            "import { ${this.simpleTSName()} } from '${this.`package`}'"
        }
        is VrapArrayType -> {
            val objType = this.itemType as VrapObjectType
            return "import { ${objType.simpleTSName()} } from '$${objType.`package`}'"
        }
        else -> throw Error("not supposed to arrive here")
    }
}

fun ObjectType.markDeprecated(): String {
    val anno = this.getAnnotation("markDeprecated", true)
    if (anno != null && (anno.value as BooleanInstance).value == true) {
        return "@deprecated";
    }
    return "";
}

fun Resource.markDeprecated() : Boolean {
    val anno = this.getAnnotation("markDeprecated")
    return (anno != null && (anno.value as BooleanInstance).value)
}

fun Method.markDeprecated() : Boolean {
    val anno = this.getAnnotation("markDeprecated")
    return (anno != null && (anno.value as BooleanInstance).value)
}

fun AnyType.deprecated() : Boolean {
    val anno = this.getAnnotation("deprecated")
    return (anno != null && (anno.value as BooleanInstance).value)
}

fun Property.deprecated() : Boolean {
    val anno = this.getAnnotation("deprecated")
    if (anno != null) {
        return (anno.value as BooleanInstance).value
    }
    val typeAnno = this.type.getAnnotation("deprecated")
    return (typeAnno != null && (typeAnno.value as BooleanInstance).value)
}

fun ResourceContainer.allMethods(): List<Method> = this
        .allContainedResources
        .flatMap {
            it.methods
        }

fun Method.toParamName() = "${this.toRequestName()}Parameter"
fun Method.toResponseName() = "${this.toRequestName()}Response"
fun Method.toHandlerName() = "${this.toRequestName()}Handler"

fun String.toJoiPackageName() = "${this}-types"
fun VrapObjectType.toJoiVrapType(): VrapObjectType = VrapObjectType(`package` = this.`package`.toJoiPackageName(), simpleClassName = this.simpleJoiName())
