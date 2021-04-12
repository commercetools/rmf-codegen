package io.vrap.codegen.languages.java.base.extensions

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toParamName
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat
import kotlin.random.Random

fun VrapType.simpleName(): String {
    return when (this) {
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapAnyType -> this.baseType
        is VrapArrayType -> "List<${this.itemType.simpleName()}>"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "${this.`package`}.${this.simpleClassName}"
        is VrapObjectType -> "${this.`package`}.${this.simpleClassName}"
        is VrapArrayType -> "java.util.List<${this.itemType.fullClassName()}>"
        is VrapNilType -> "void"
    }
}

fun VrapType.toJavaVType(): VrapType {
    return when (this) {
        is VrapObjectType -> {
            VrapObjectType(`package` = this.`package`.toJavaPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapEnumType -> {
            VrapEnumType(`package` = this.`package`.toJavaPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapArrayType -> {
            VrapArrayType(this.itemType.toJavaVType())
        }
        else -> {
            this
        }

    }
}

fun String.toJavaPackage(): String {
    return this.split('.', '/')
            .map(
                    StringCaseFormat.LOWER_UNDERSCORE_CASE::apply
            )
            .joinToString(separator = ".")
}

fun QueryParameter.template(): Any {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
        val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
        return "sprintf('" + template.value.replace("<" + placeholder.value + ">", "%s") + "', $" + placeholder.value + ")"
    }

    if (this.type.name == "boolean") {
        return true
    } else if (this.type.name == "number") {
        return Random.nextInt(1, 10)
    } else {
        return "\"" + this.name + "\""
    }
}

fun Method.toRequestName(): String {
    return this.resource().fullUri.toParamName("By") + this.method.toString().capitalize()
}

fun Resource.resourcePathList(): List<Resource> {
    val path = Lists.newArrayList<Resource>()
    if (this.fullUri.template == "/") {
        return path
    }
    path.add(this)
    var t = this.eContainer()
    while (t is Resource) {
        val template = t.fullUri.template
        if (template != "/") {
            path.add(t)
        }
        t = t.eContainer()
    }
    return Lists.reverse(path)
}

fun UriTemplate.paramValues(): List<String> {
    return this.components.filterIsInstance<Expression>().flatMap { expression -> expression.varSpecs.map { varSpec -> varSpec.variableName  } }
}

fun Method.firstBody(): Body? = this.bodies.stream().findFirst().orElse(null)

fun QueryParameter.methodName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
    }
    return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}
