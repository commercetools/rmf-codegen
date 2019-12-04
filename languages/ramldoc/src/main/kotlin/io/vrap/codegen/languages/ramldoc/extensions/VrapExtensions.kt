package io.vrap.codegen.languages.ramldoc.extensions

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.io.IOException
import java.util.stream.Collectors

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

fun AnyType.renderScalarType(): String {
    if (!this.isInlineType) {
        return "type: ${this.name}"
    }
    return this.renderEAttributes().plus("type: ${this.name}").joinToString("\n")
}

fun AnyType.renderEAttributes(): List<String> {
    val eAttributes = this.eClass().eAllAttributes
    return eAttributes.filter { eAttribute -> eAttribute.name != "name" && this.eGet(eAttribute) != null}
            .map { eAttribute -> "${eAttribute.name}: ${this.eGet(eAttribute)}" }

}

fun ArrayType.renderArrayType(): String {
    var t = this.renderEAttributes().plus("type: array").joinToString("\n")
    if (this.items != null) {
        t += """
                |items:
                |  <<${this.items.renderScalarType()}>>
            """
    }
    return t.trimMargin().keepIndentation("<<", ">>")
}

fun UnionType.renderUnionType(): String {
    val typeString = this.oneOf.joinToString(" | ") { when(it) { is ArrayType -> "${it.items.name}[]" else -> it.name } }

    val unionString = """
        |(oneOf):
        |${this.oneOf.joinToString("\n") { when(it) { is ArrayType -> "- ${it.items.name}[]" else -> "- ${it.name}" } }}
    """.trimMargin()

    val t = listOf<String>().plus("type: ${typeString}").plus(unionString).plus(this.renderEAttributes()).joinToString("\n")
    return t.trimMargin().keepIndentation("<<", ">>")
}

fun AnyType.renderType(withDescription: Boolean = true): String {
    val builtinType = "(builtinType): ${BuiltinType.of(this.eClass()).map { it.getName() }.orElse("any")}"
    val description = if (withDescription && this.description?.value.isNullOrBlank().not()) {
        """
        |description: |-
        |  <<${this.description.value.trim()}>>
        """.trimEnd().keepIndentation("<<", ">>")
    } else {
        ""
    }
    return """
        |${when (this) {
            is ArrayType -> this.renderArrayType()
            is UnionType -> this.renderUnionType()
            else -> this.renderScalarType()}}
        |$builtinType$description
        """.trimMargin()
}

class InstanceSerializer : JsonSerializer<Instance>() {

    @Throws(IOException::class)
    override fun serialize(value: Instance, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeObject(value.value)
    }
}

class ObjectInstanceSerializer : JsonSerializer<ObjectInstance>() {

    @Throws(IOException::class)
    override fun serialize(value: ObjectInstance, gen: JsonGenerator, provider: SerializerProvider) {
        val properties = value.value
        gen.writeStartObject()
        for (v in properties) {
            gen.writeObjectField(v.name, v.value)
        }
        gen.writeEndObject()
    }
}