package io.vrap.codegen.languages.php.extensions

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.security.OAuth20Settings
import io.vrap.rmf.raml.model.security.SecurityScheme
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.stream.Collectors

fun String.toNamespaceName():String{
    val `package` = this.split("/")
    return `package`.takeLast(maxOf(`package`.size, 1)).joinToString("\\") { s -> s.capitalize() }
}

fun String.toNamespaceDir():String{
    val `package` = this.split("/")
    return `package`.takeLast(maxOf(`package`.size, 1)).joinToString("/") { s -> s.capitalize() }
}

fun VrapType.namespaceName():String{
    return when(this){
        is VrapObjectType -> this.`package`.toNamespaceName()
        is VrapArrayType -> this.itemType.namespaceName()
        else -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.simpleName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "string"
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> if (this.itemType.isScalar()) "array" else "${this.itemType.simpleName()}Collection"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.simpleBuilderName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "string"
        is VrapObjectType -> this.simpleClassName + "Builder"
        is VrapArrayType -> if (this.itemType.isScalar()) "array" else "${this.itemType.simpleName()}CollectionBuilder"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "string"
        is VrapObjectType -> "${this.namespaceName()}\\${this.simpleClassName}"
        is VrapArrayType -> "${this.itemType.fullClassName()}${if (!this.itemType.isScalar()) "Collection" else "[]"}"
        is VrapNilType -> throw IllegalStateException("$this has no full class name.")
    }
}

fun AnyType.isScalar(): Boolean {
    return when(this) {
        is StringType -> true
        is IntegerType -> true
        is NumberType -> true
        is BooleanType -> true
        is DateTimeType -> true
        is DateOnlyType -> true
        is DateTimeOnlyType -> true
        is TimeOnlyType -> true
        is ArrayType -> this.items == null || this.items.isScalar()
        else -> false
    }
}

fun VrapType.isScalar(): Boolean {
    return when(this){
        is VrapObjectType -> when(this.simpleClassName) {
            in scalarTypes() -> true
            else -> false
        }
        is VrapArrayType -> when(this.itemType.simpleName()) {
            in scalarTypes() -> true
            else -> false
        }
        is VrapScalarType -> true
        is VrapEnumType -> true
        else -> false
    }
}


fun Api.accessTokenUri(): UriTemplate {
    return UriTemplate.fromTemplate(this.securitySchemes.map { s -> s.settings }.filterIsInstance<OAuth20Settings>()
            .map { securityScheme -> securityScheme.accessTokenUri() }
            .firstOrNull() ?: "")
}

fun Api.accessTokenUriParams(): ObjectInstance? {
    return this.securitySchemes.map { s -> s.settings }.filterIsInstance<OAuth20Settings>()
            .map { securityScheme -> securityScheme.accessTokenUriParams() }
            .first()
}

private fun OAuth20Settings.accessTokenUri(): String {
    val authInfo = this.getAnnotation("authInfo")
    if (authInfo != null) {
        return ((authInfo.value as ObjectInstance).getValue("authUri") as StringInstance).value ?: ""
    }
    return this.accessTokenUri
}

private fun OAuth20Settings.accessTokenUriParams(): ObjectInstance? {
    val authInfo = this.getAnnotation("authInfo")
    return (authInfo.value as ObjectInstance).getValue("authUriParameters") as? ObjectInstance
}

fun Method.apiResource(): Resource = this.eContainer() as Resource

fun Method.toRequestName(): String {
    if (this.apiResource().fullUri.template == "/") {
        return "ApiRoot" + this.method.toString().capitalize()
    }
    return this.apiResource().fullUri.toParamName("By") + this.method.toString().capitalize()
}

fun Resource.toResourceName(): String {
    return this.fullUri.toParamName("By")
}

fun UriTemplate.toParamName(delimiter: String): String {
    return this.toParamName(delimiter, "")
}

fun UriTemplate.toParamName(delimiter: String, suffix: String): String {
    return this.components.stream().map { uriTemplatePart ->
        if (uriTemplatePart is Expression) {
            return@map uriTemplatePart.varSpecs.stream()
                    .map { s -> delimiter + s.variableName.capitalize() + suffix }.collect(Collectors.joining())
        }
        StringCaseFormat.UPPER_CAMEL_CASE.apply(uriTemplatePart.toString().replace("/", "-"))
    }.collect(Collectors.joining()).replace("[^\\p{L}\\p{Nd}]+".toRegex(), "").capitalize()
}

fun Method.allParams(): List<String>? {
    return this.apiResource().fullUri.components.stream()
            .filter { uriTemplatePart -> uriTemplatePart is Expression }
            .flatMap { uriTemplatePart -> (uriTemplatePart as Expression).varSpecs.stream().map { it.variableName } }
            .collect(Collectors.toList())
}

fun Method.firstBody(): Body? = this.bodies.stream().findFirst().orElse(null)

fun scalarTypes():Array<String> { return arrayOf("string", "int", "float", "bool", "array", "stdClass") }

fun QueryParameter.methodName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
    }
    return "with" + StringCaseFormat.UPPER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}

fun QueryParameter.methodParam(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "string $" + StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value) + ", $" + paramName.value
    }
    return "$" + StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}

fun QueryParameter.paramName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return "$" + paramName.value
    }
    return "$" + StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}

fun QueryParameter.simpleParamName(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
        return paramName.value
    }
    return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
}

fun QueryParameter.template(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
        val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
        return "sprintf('" + template.value.replace("<" + placeholder.value + ">", "%s") + "', $" + placeholder.value + ")"
    }
    return "'" + this.name + "'"
}

fun QueryParameter.placeholderDocBlock(): String {
    val anno = this.getAnnotation("placeholderParam", true)

    if (anno != null) {
        val o = anno.value as ObjectInstance
        val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
        return "@psalm-param string $" + placeholder.value
    }
    return ""
}

fun QueryParameter.withParam(type: Method): String {
    return """
            |/**
            | * ${this.placeholderDocBlock()}
            | * @psalm-param scalar|scalar[] ${this.paramName()}
            | */
            |public function ${this.methodName()}(${this.methodParam()}): ${type.toRequestName()}
            |{
            |    return $!this->withQueryParam(${this.template()}, ${this.paramName()});
            |}
        """.trimMargin()
}

fun UriTemplate.paramValues(): List<String> {
    return this.components.filterIsInstance<Expression>().flatMap { expression -> expression.varSpecs.map { varSpec -> varSpec.variableName  } }
}
