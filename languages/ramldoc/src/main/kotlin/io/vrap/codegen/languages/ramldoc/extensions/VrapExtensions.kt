package io.vrap.codegen.languages.ramldoc.extensions

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import io.vrap.rmf.raml.model.values.RegExp
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
    return this.renderEAttributes().plus("type: ${this.name ?: BuiltinType.of(this.eClass()).get().getName()}").joinToString("\n")
}

fun NumberType.renderNumberType(): String {
    val name = this.name ?: BuiltinType.of(this.eClass()).get().getName()
    val typeName = if (name == "number" && this.format.literal.findAnyOf(listOf("int", "long")) != null) "integer" else name
    if (!this.isInlineType) {
        return "type: $typeName"
    }
    return this.renderEAttributes().plus("type: $typeName").joinToString("\n")
}

fun ObjectType.renderObjectType(): String {
    if (!this.isInlineType) {
        return "type: ${this.name}"
    }
    return this.renderEAttributes().plus("type: ${this.name ?: "object"}").joinToString("\n")
}

fun AnyType.renderEAttributes(): List<String> {
    val eAttributes = this.eClass().eAllAttributes
    return eAttributes.filter { eAttribute -> eAttribute.name != "name" && this.eGet(eAttribute) != null}
            .map { eAttribute -> when(val eValue = this.eGet(eAttribute)) {
                is RegExp -> "${eAttribute.name}: \"${eValue}\""
                is String -> "${eAttribute.name}: \"${eValue}\""
                else -> "${eAttribute.name}: ${this.eGet(eAttribute)}"
            } }
}

fun ArrayType.renderArrayType(): String {
    var t = this.renderEAttributes().plus("type: array").joinToString("\n")
    if (this.items != null) {
        t += """
                |items:
                |  <<${this.items.renderTypeFacet()}>>
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

fun AnyType.renderTypeFacet(): String {
    return when (this) {
        is ArrayType -> this.renderArrayType()
        is UnionType -> this.renderUnionType()
        is ObjectType -> this.renderObjectType()
        is NumberType -> this.renderNumberType()
        else -> this.renderScalarType()}
}

fun AnyType.renderType(withDescription: Boolean = true): String {
    val builtinTypeName = BuiltinType.of(this.eClass()).map { it.getName() }.orElse("any")
    val typeName = if (this is NumberType && builtinTypeName == "number" && this.format.literal.findAnyOf(listOf("int", "long")) != null) "integer" else name
    val builtinType = "(builtinType): $typeName"
    val description = if (withDescription && this.description?.value.isNullOrBlank().not()) {
        """
        |description: |-
        |  <<${this.description.value.trim()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    } else {
        ""
    }
    return """
        |${this.renderTypeFacet()}
        |$builtinType
        |$description
        """.trimMargin().trimEnd()
}

fun AnyAnnotationType.renderScalarType(): String {
    if (this.type != null && this.type.isInlineType) {
        return this.renderEAttributes().plus("type: ${this.type?.name ?: BuiltinType.of(this.eClass()).get().getName()}").joinToString("\n")
    }
    return "type: ${this.type?.name ?: BuiltinType.of(this.eClass()).get().getName()}"
}

fun ObjectAnnotationType.renderObjectType(): String {
    if (this.type != null && this.type.isInlineType) {
        return this.renderEAttributes().plus("type: ${this.type?.name ?: "object"}").joinToString("\n")
    }
    return "type: ${this.type?.name ?: "object"}"
}

fun AnyAnnotationType.renderEAttributes(): List<String> {
    val eAttributes = this.eClass().eAllAttributes
    return eAttributes.filter { eAttribute -> eAttribute.name != "name" && this.eGet(eAttribute) != null}
            .map { eAttribute -> when(val eValue = this.eGet(eAttribute)) {
                is RegExp -> "${eAttribute.name}: \"${eValue}\""
                is String -> "${eAttribute.name}: \"${eValue}\""
                else -> "${eAttribute.name}: ${this.eGet(eAttribute)}"
            } }
}

fun ArrayAnnotationType.renderArrayType(): String {
    var t = this.renderEAttributes().plus("type: array").joinToString("\n")
    if (this.items != null) {
        t += """
                |items:
                |  <<${this.items.renderTypeFacet()}>>
            """
    }
    return t.trimMargin().keepIndentation("<<", ">>")
}

fun UnionAnnotationType.renderUnionType(): String {
    val typeString = this.oneOf.joinToString(" | ") { when(it) { is ArrayType -> "${it.items.name}[]" else -> it.name } }

    val unionString = """
        |(oneOf):
        |${this.oneOf.joinToString("\n") { when(it) { is ArrayType -> "- ${it.items.name}[]" else -> "- ${it.name}" } }}
    """.trimMargin()

    val t = listOf<String>().plus("type: ${typeString}").plus(unionString).plus(this.renderEAttributes()).joinToString("\n")
    return t.trimMargin().keepIndentation("<<", ">>")
}

fun AnyAnnotationType.renderTypeFacet(): String {
    return when (this) {
        is ArrayAnnotationType -> this.renderArrayType()
        is UnionAnnotationType -> this.renderUnionType()
        is ObjectAnnotationType -> this.renderObjectType()
        else -> this.renderScalarType()}
}

fun AnyAnnotationType.renderType(withDescription: Boolean = true): String {
    val builtinType = "(builtinType): ${BuiltinType.of(this.eClass()).map { it.getName() }.orElse("any")}"
    val description = if (withDescription && this.description?.value.isNullOrBlank().not()) {
        """
        |description: |-
        |  <<${this.description.value.trim()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    } else {
        ""
    }
    return """
        |${this.renderTypeFacet()}
        |$builtinType
        |$description
        """.trimMargin().trimEnd()
}

fun VrapEnumType.packageDir(prefix: String): String {
    val dir = this.`package`.replace(prefix, "").trim('/')
    return dir + if (dir.isNotEmpty()) "/" else ""
}

fun VrapObjectType.packageDir(prefix: String): String {
    val dir = this.`package`.replace(prefix, "").trim('/')
    return dir + if (dir.isNotEmpty()) "/" else ""
}

fun Annotation.renderAnnotation(): String {
    return when (this.type) {
        is ObjectAnnotationType -> """
            |(${this.type.name}):
            |  <<${this.value.toYaml()}>>""".trimMargin().keepIndentation("<<", ">>")
        is StringAnnotationType ->
            when(this.value) {
                is ObjectInstance -> """
                    |(${this.type.name}): |-
                    |  <<${this.value.toJson()}>>""".trimMargin().keepIndentation("<<", ">>")
                else -> "(${this.type.name}): ${this.value.toYaml()}"
            }
        else -> "(${this.type.name}): ${this.value.toYaml()}"
    }
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

fun Instance.toYaml(): String {
    var example = ""
    val mapper = YAMLMapper()
    mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(StringInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
    mapper.registerModule(module)

    if (this is ObjectInstance) {
        try {
            example = mapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
        }
    } else {
        example = mapper.writeValueAsString(this.value)
    }

    return example.trim()
}

fun Instance.toJson(): String {
    var example = ""
    val mapper = ObjectMapper()

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(StringInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
    mapper.registerModule(module)

    if (this is ObjectInstance) {
        try {
            example = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
        } catch (e: JsonProcessingException) {
        }
    } else {
        example = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.value)
    }

    return example.trim()
}
