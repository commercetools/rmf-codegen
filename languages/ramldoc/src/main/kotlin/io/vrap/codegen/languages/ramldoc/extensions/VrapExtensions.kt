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
import io.vrap.codegen.languages.extensions.toParamName
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import io.vrap.rmf.raml.model.values.RegExp
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import java.io.IOException
import java.util.stream.Collectors

fun Resource.toResourceName(): String {
    return this.fullUri.normalize().toParamName("By")
}

fun UriTemplate.toParamName(delimiter: String): String {
    return this.toParamName(delimiter, "")
}

fun UriTemplate.toParamName(delimiter: String, suffix: String): String {
    return this.components.stream().map { uriTemplatePart ->
        if (uriTemplatePart is Expression) {
            return@map uriTemplatePart.varSpecs.stream()
                    .map { s -> delimiter + s.variableName.firstUpperCase() + suffix }.collect(Collectors.joining())
        }
        StringCaseFormat.UPPER_CAMEL_CASE.apply(uriTemplatePart.toString().replace("/", "-"))
    }.collect(Collectors.joining()).replace("[^\\p{L}\\p{Nd}]+".toRegex(), "").firstUpperCase()
}

fun UriTemplate.normalize(): UriTemplate {
    return UriTemplate.fromTemplate(this.template.replace("{ID}", "{id}", ignoreCase = true))
}

fun AnyType.renderFileType(): String {
    if (!this.isInlineType) {
        return "type: ${this.name}"
    }
    return this.renderEAttributes().plus("type: ${this.name ?: "file"}").joinToString("\n")
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
    return eAttributes.filter { eAttribute -> eAttribute.name !in listOf("name", "discriminator", "discriminatorValue") && this.eGet(eAttribute) != null}
            .map { eAttribute -> when(val eValue = this.eGet(eAttribute)) {
                is RegExp -> "${eAttribute.name}: \"${eValue}\""
                is String -> "${eAttribute.name}: \"${eValue}\""
                is Collection<*> -> "${eAttribute.name}:\n" + (this.eGet(eAttribute) as Collection<*>).joinToString("\n") { "  - ${it?.toYaml()}" }
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
    return t.trimMargin().keepAngleIndent()
}

fun UnionType.renderUnionType(): String {
    val typeString = this.oneOf.joinToString(" | ") { when(it) { is ArrayType -> "${it.items.name}[]" else -> it.name } }

    val unionString = """
        |(oneOf):
        |${this.oneOf.joinToString("\n") { when(it) { is ArrayType -> "- ${it.items.name}[]" else -> "- ${it.name}" } }}
    """.trimMargin()

    val t = listOf<String>().plus("type: ${typeString}").plus(unionString).plus(this.renderEAttributes()).joinToString("\n")
    return t.trimMargin().keepAngleIndent()
}

fun AnyType.renderTypeFacet(): String {
    return when (this) {
        is ArrayType -> this.renderArrayType()
        is UnionType -> this.renderUnionType()
        is ObjectType -> this.renderObjectType()
        is NumberType -> this.renderNumberType()
        is FileType -> this.renderFileType()
        else -> this.renderScalarType()}
}

fun UriParameter.renderUriParameter(): String {
    val parameterExamples = this.inlineTypes.flatMap { inlineType -> inlineType.examples }
    return """
            |${this.name.replace("^ID$".toRegex(RegexOption.IGNORE_CASE), "id")}:${if (this.type.enum.size > 0) """
            |  enum:
            |  <<${this.type.enum.joinToString("\n") { "- ${it.value}"}}>>""" else ""}
            |  <<${this.type.renderType()}>>
            |  required: ${this.required}${if (parameterExamples.isNotEmpty()) """
            |  examples:
            |    <<${parameterExamples.joinToString("\n") { it.renderSimpleExample() }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
}

fun Example.renderSimpleExample(): String {
    val t = """
            |${if (this.name.isNotEmpty()) this.name else "default"}:${if (this.displayName != null) """
            |  displayName: ${this.displayName.value.trim()}""" else ""}${if (this.description != null) """
            |  description: |-
            |    <<${this.description.value.trim()}>>""" else ""}${if (this.annotations.isNotEmpty()) """
            |  <<${this.annotations.joinToString("\n") { it.renderAnnotation() }}>>""" else ""}
            |  strict: ${this.strict.value}
            |  value: ${this.value.toJson().escapeAll().escapeAll()}
        """.trimMargin()

    return t
}

fun Example.renderExample(exampleName: String, inlineExample: Boolean = false): String {
    return """
            |${if (this.name.isNotEmpty()) this.name else "default"}:${if (this.displayName != null) """
            |  displayName: ${this.displayName.value.trim()}""" else ""}${if (this.description != null) """
            |  description: |-
            |    <<${this.description.value.trim()}>>""" else ""}${if (this.annotations.isNotEmpty()) """
            |  <<${this.annotations.joinToString("\n") { it.renderAnnotation() }}>>""" else ""}
            |  strict: ${this.strict.value}
            |  value:${if (!inlineExample) " !include ../examples/$exampleName.json" else if (this.value is ObjectInstance) """|
            |    <<${this.value.toJson().escapeAll()}>>""".trimMargin().keepAngleIndent().escapeAll() else " " + this.value.toJson().escapeAll() }
        """.trimMargin().keepAngleIndent().escapeAll()
}

fun AnyType.renderType(withDescription: Boolean = true): String {
    val builtinTypeName = BuiltinType.of(this.eClass()).map { it.getName() }.orElse("any")
    val typeName = if (this is NumberType && builtinTypeName == "number" && this.format.literal.findAnyOf(listOf("int", "long")) != null) "integer" else builtinTypeName
    val builtinType = "(builtinType): $typeName"
    val description = if (withDescription && (this.isInlineType || this.isScalar())  && this.description?.value.isNullOrBlank().not()) {
        """
        |description: |-
        |  <<${this.description.value.trim()}>>
        """.trimMargin().keepAngleIndent()
    } else {
        ""
    }
    val displayName = if ((this.isInlineType || this.isScalar())  && this.displayName?.value.isNullOrBlank().not()) {
        """
        |
        |displayName: ${this.displayName.value.trim()}
        """.trimMargin()
    } else {
        ""
    }
    return """
        |${this.renderTypeFacet()}
        |$builtinType$displayName
        |$description
        """.trimMargin().trimEnd()
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
    return t.trimMargin().keepAngleIndent()
}

fun UnionAnnotationType.renderUnionType(): String {
    val typeString = this.oneOf.joinToString(" | ") { when(it) { is ArrayType -> "${it.items.name}[]" else -> it.name } }

    val unionString = """
        |(oneOf):
        |${this.oneOf.joinToString("\n") { when(it) { is ArrayType -> "- ${it.items.name}[]" else -> "- ${it.name}" } }}
    """.trimMargin()

    val t = listOf<String>().plus("type: ${typeString}").plus(unionString).plus(this.renderEAttributes()).joinToString("\n")
    return t.trimMargin().keepAngleIndent()
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
        """.trimMargin().keepAngleIndent()
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
    return this.`package`.packageDir(prefix)
}

fun String.packageDir(prefix: String): String {
    val dir = this.replace(prefix, "").trim('/')
    return dir + if (dir.isNotEmpty()) "/" else ""
}

fun VrapObjectType.packageDir(prefix: String): String {
    return this.`package`.packageDir(prefix)
}

fun Annotation.renderAnnotation(): String {
    return when (this.type) {
        is ObjectAnnotationType -> """
            |(${this.type.name}):
            |  <<${this.value.toYaml()}>>""".trimMargin().keepAngleIndent()
        is ArrayAnnotationType -> """
            |(${this.type.name}):
            |  <<${this.value.toYaml()}>>""".trimMargin().keepAngleIndent()
        is StringAnnotationType ->
            when(this.value) {
                is ObjectInstance -> """
                    |(${this.type.name}): |-
                    |  <<${this.value.toJson()}>>""".trimMargin().keepAngleIndent()
                is ArrayInstance -> """
                    |(${this.type.name}): |-
                    |  <<${this.value.toJson()}>>""".trimMargin().keepAngleIndent()
                else -> "(${this.type.name}): ${this.value.toYaml()}"
            }
        else -> "(${this.type.name}): ${this.value.toYaml()}"
    }
}


class NullInstanceSerializer : JsonSerializer<NullInstance>() {

    @Throws(IOException::class)
    override fun serialize(value: NullInstance, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeNull()
    }
}

class StringInstanceSerializer : JsonSerializer<StringInstance>() {

    @Throws(IOException::class)
    override fun serialize(value: StringInstance, gen: JsonGenerator, provider: SerializerProvider) {
        if (value.value == null) {
            gen.writeNull()
        } else {
            gen.writeObject(value.value.trim())
        }
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
    mapper.enable(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE)
    mapper.disable(YAMLGenerator.Feature.SPLIT_LINES)

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer(StringInstance::class.java, StringInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
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

fun Instance.toJson(pretty: Boolean = true): String {
    var example = ""
    val mapper = ObjectMapper()

    val module = SimpleModule()
    module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
    module.addSerializer(StringInstance::class.java, StringInstanceSerializer())
    module.addSerializer(NullInstance::class.java, NullInstanceSerializer())
    module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
    module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
    mapper.registerModule(module)

    val writer = if (pretty) mapper.writerWithDefaultPrettyPrinter() else mapper.writer()
    if (this is ObjectInstance) {
        try {
            example = writer.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
        }
    } else {
        example = writer.writeValueAsString(this.value)
    }

    return example.trim()
}

@Suppress("UNCHECKED_CAST")
fun <T> EObject.getParent(parentClass: Class<T>): T? {
    if (this.eContainer() == null) {
        return null
    }
    return if (parentClass.isInstance(this.eContainer())) {
        this.eContainer() as T
    } else this.eContainer().getParent(parentClass)
}
