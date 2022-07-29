package io.vrap.codegen.languages.jsonschema.model
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.BooleanType
import io.vrap.rmf.raml.model.types.DateOnlyType
import io.vrap.rmf.raml.model.types.DateTimeType
import io.vrap.rmf.raml.model.types.IntegerType
import io.vrap.rmf.raml.model.types.NumberType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.TimeOnlyType
import org.eclipse.emf.ecore.EObject
import org.json.JSONObject


class JsonSchemaRenderer constructor(
    val vrapTypeProvider: VrapTypeProvider,
    @AllAnyTypes var allAnyTypes: List<AnyType>
) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        var produced = allAnyTypes
            .map {
                when (it) {
                    is ObjectType -> createObjectSchema(it)
                    is StringType -> createStringSchema(it)
                    else -> null
                }
            }
            .filterNotNull()
            .toList()

        return produced
    }

    private fun createObjectSchema(type: ObjectType): TemplateFile {
        val schema = linkedMapOf<String, Any>(
            "${"$"}schema" to "https://json-schema.org/draft/2020-12/schema#",
            "${"$"}id" to create_schema_id(type.filename()),
            "title" to type.name,
            "type" to "object",
        )

        if (type.description != null) {
            schema.put("description", type.description.value.trim())
        }

        val obj = LinkedHashMap<String, Any>()
        schema.put("properties", type.getObjectProperties())

        val patterns = type.getPatternProperties()
        if (!patterns.isNullOrEmpty()) {
            schema.put("patternProperties", patterns)
        }

        val required = type.getRequiredPropertyNames()
        if (!required.isNullOrEmpty()) {
            schema.put("required", required)
        }

        schema.put("additionalProperties", false)

        val discriminatorProperty = type.discriminatorProperty()
        if (discriminatorProperty != null && type.discriminatorValue.isNullOrEmpty()) {

            schema.put(
                "discriminator",
                mapOf(
                    "propertyName" to discriminatorProperty.name
                )
            )
            schema.put(
                "oneOf",
                allAnyTypes.getTypeInheritance(type)
                    .filterIsInstance<ObjectType>()
                    .map {
                        mapOf("${"$"}ref" to create_schema_id(it.filename()))
                    }
            )
        }

        // obj.forEach { (key, value) -> schema.put(key, value) }

        return TemplateFile(JSONObject(schema).toString(2), type.filename())
    }

    private fun createStringSchema(type: StringType): TemplateFile? {
        val vrap = type.toVrapType()
        if (vrap !is VrapEnumType) {
            return null
        }

        val schema = mutableMapOf(
            "${"$"}schema" to "https://json-schema.org/draft/2020-12/schema#",
            "${"$"}id" to create_schema_id(type.filename()),
            "title" to type.name,
            "type" to "string",
            "enum" to type.enumValues()
        )

        if (type.description != null) {
            schema.put("description", type.description.value.trim())
        }
        return TemplateFile(JSONObject(schema).toString(2), type.filename())
    }

    private fun ObjectType.getObjectProperties(): LinkedHashMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        this.allProperties
            .filter {
                !it.isPatternProperty()
            }
            .forEach {
                result.put(it.name, it.type.toSchemaProperty(this, it))
            }
        return result
    }

    private fun ObjectType.getPatternProperties(): LinkedHashMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        this.allProperties
            .filter {
                it.isPatternProperty()
            }
            .forEach {
                result.put(it.name.trim('/'), it.type.toSchemaProperty(this, it))
            }
        return result
    }

    private fun ObjectType.getRequiredPropertyNames(): List<String> {
        return this.allProperties
            .filter {
                !it.isPatternProperty() && (it.required || this.discriminatorProperty() == it)
            }
            .map {
                it.name
            }
    }

    private fun AnyType.toSchemaProperty(parent: ObjectType, property: Property): Map<String, Any> {

        if ( parent.discriminatorProperty() == property) {
            var subTypes = if (property.type != null) {
                allAnyTypes.getTypeInheritance(parent)
                    .filterIsInstance<ObjectType>()
                    .map {
                        it.discriminatorValue
                    }
                } else {
                    listOf<String>()
                }

            if (parent.discriminatorValue != null) {
                subTypes = subTypes.plus(parent.discriminatorValue)
            }

            return mapOf(
                "enum" to subTypes
            )
        }

        val vrap = this.toVrapType()

        if (vrap is VrapEnumType && this is StringType) {
            return mapOf(
                "${"$"}ref" to vrap.filename()
            )
        }

        return when (this) {
            is ObjectType -> this.toSchemaProperty(parent, property)
            is ArrayType -> this.toSchemaProperty(parent, property)
            is StringType -> this.toSchemaProperty(parent, property)
            is NumberType -> this.toSchemaProperty(parent, property)
            is BooleanType -> this.toSchemaProperty(parent, property)
            is DateTimeType -> this.toSchemaProperty(parent, property)
            is IntegerType -> this.toSchemaProperty(parent, property)
            is AnyType -> mapOf(
                "type" to listOf("number", "string", "boolean", "object", "array", "null")
            )
            else -> {
                println("Missing case for " + this + property)

                return mapOf(
                    "type" to listOf("number", "string", "boolean", "object", "array", "null")
                )
            }
        }
    }

    private fun ObjectType.toSchemaProperty(parent: ObjectType, property: Property): Map<String, Any> {

        val dProperty = this.discriminatorProperty()
        if (dProperty != null && !this.discriminatorValue.isNullOrEmpty()) {
            return mapOf(
                "${"$"}ref" to this.filename()
            )
        }

        if (this.toVrapType() !is VrapObjectType) {
            return mapOf(
                "type" to "object"
            )
        } else {
            return mapOf(
                "${"$"}ref" to this.filename()
            )
        }
    }

    private fun StringType.toSchemaProperty(parent: ObjectType, property: Property): Map<String, Any> {
        val vrapType = this.toVrapType()
        if (vrapType !is VrapScalarType) {
            throw Exception("Expected scalar")
        }
        val result = when (vrapType.scalarType) {
            "any" -> mapOf(
                "type" to listOf("number", "string", "boolean", "object", "array", "null")
            )
            else -> mapOf(
                "type" to vrapType.scalarType
            )
        }
        return result
    }

    private fun IntegerType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "number",
        // "format" to "integer"
    )

    private fun BooleanType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "boolean"
    )

    private fun DateTimeType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "string",
        "format" to "date-time"
    )

    private fun DateOnlyType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "string",
        "format" to "date"
    )

    private fun TimeOnlyType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "string",
        "format" to "time"
    )

    private fun NumberType.toSchemaProperty(parent: ObjectType, property: Property) = mapOf(
        "type" to "number"
    )

    private fun ArrayType.toSchemaProperty(parent: ObjectType, property: Property): Map<String, Any> {
        return mapOf(
            "type" to "array",
            "items" to this.items.toSchemaProperty(parent, property)
        )
    }

    private fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

    fun EObject?.toVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType
    }

    fun ObjectType.filename(): String {
        val type = this.toVrapType()
        if (type !is VrapObjectType) {
            return "unknown.json"
        }
        return type.filename()
    }

    fun StringType.filename(): String {
        val type = this.toVrapType()
        if (type !is VrapEnumType) {
            return "unknown.json"
        }
        return type.filename()
    }

    fun VrapObjectType.filename(): String {
        return this.simpleClassName + ".schema.json"
    }

    fun VrapEnumType.filename(): String {
        return this.simpleClassName + "Enum.schema.json"
    }

    fun List<AnyType>.getTypeInheritance(type: AnyType): List<AnyType> {
        return this
            .filter { it.type != null && it.type.name == type.name }
        // TODO: Shouldn't this be necessary?
        // .plus(
        //     this
        //     .filter { it.type != null && it.type.name == type.name }
        //     .flatMap { this.getTypeInheritance(it.type) }
        // )
    }

    fun StringType.enumValues(): List<String> = enum.filterIsInstance<StringInstance>()
        .map { it.value }
        .filterNotNull()
}
