package io.vrap.codegen.languages.oas.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.oas.extensions.*
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*

class OasObjectTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName val modelPackageName: String) : ExtensionsBase, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val postmanExampleAnno = type.getAnnotation("postman-example")
        val postmanExample = if (postmanExampleAnno != null) {
            val example = TypesFactory.eINSTANCE.createExample()
            val boolInstance = TypesFactory.eINSTANCE.createBooleanInstance()
            boolInstance.value = true
            example.name = "postman"
            example.strict = boolInstance
            example
        } else {
            null
        }
        val examples = type.examples.plus(postmanExample).filterNotNull().sortedWith(compareBy { it.name })
        val properties = type.allProperties.filterNot { it.isPatternProperty() }
        val patternProperties = type.allProperties.filter { it.isPatternProperty() }
        val discriminatorTypes = type.subTypes.filterNot { it.isInlineType }.filterIsInstance<ObjectType>().filter { it.discriminatorValue != null }.sortedWith(compareBy { it.name })

        val typeContent = """
            |type: "object"${if (type.discriminator != null) """
            |discriminator:
            |  propertyName: ${type.discriminator}
            |  mapping:
            |    <<${discriminatorTypes.joinToString("\n") { "${it.discriminatorValue}: '#/components/schemas/${it.name}'" }}>>""" else ""}${if (type.properties.any { it.required } || type.discriminator != null) """
            |required:
            |  <<${type.properties.filter { it.required }.map { it.name }.plus(if(type.discriminator != null) listOf(type.discriminator) else emptyList()).filterNotNull().distinct().sorted().joinToString("\n") { "- $it" }}>>""" else ""}${if (properties.isEmpty().not()) """
            |properties:
            |  <<${properties.joinToString("\n") { renderProperty(type, it) }}>>""" else ""}${if (patternProperties.size > 1) """
            |additionalProperties:
            |  oneOf:
            |    <<${patternProperties.joinToString("\n") { "- <<${renderPatternProperties(type, it)}>>".keepAngleIndent() }}>>""" else ""}${if (patternProperties.size == 1) """
            |additionalProperties:
            |  <<${renderPatternProperties(type, patternProperties[0])}>>""" else ""}
            """.trimMargin().keepAngleIndent()

        val content = if (type.type != null) {
            """
                |allOf:
                |  - ${"$"}ref: '#/components/schemas/${type.type.name}'
                |  - <<$typeContent>>
            """.trimMargin().keepAngleIndent()
        } else {
            typeContent
        }

        return TemplateFile(
                relativePath = "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun renderExample(type: VrapObjectType, example: Example): String {
        val t = if (type.packageDir(modelPackageName).isNotEmpty()) "../.." else ".."
        val exampleName = "${t}/examples/" + type.packageDir(modelPackageName) + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        return """
            |${if (example.name.isNotEmpty()) example.name else "default"}:${if (example.displayName != null) """
            |  displayName: ${example.displayName.value.trim()}""" else ""}${if (example.description != null) """
            |  description: |-
            |    <<${example.description.value.trim()}>>""" else ""}${if (example.annotations.isNotEmpty()) """
            |  <<${example.annotations.joinToString("\n") { it.renderAnnotation() }}>>""" else ""}
            |  strict: ${example.strict.value}
            |  value: !include $exampleName
        """.trimMargin()
    }

    private fun renderExample(example: Example): String {
        return """
            |${if (example.name.isNotEmpty()) example.name else "default"}: 
            |  value: ${if (example.value is ObjectInstance) """|-
            |    <<${example.value.toJson()}>>""" else example.value.toJson() }
        """.trimMargin().keepAngleIndent()
    }

    private fun renderPatternProperties(type: ObjectType, property: Property): String {
        var inherited = false;
        if (type.properties.none { p -> p.name == property.name }) {
            inherited = true;
        }
        val discriminatorProp = type.discriminatorProperty()?.name
        val discriminatorValue = type.discriminatorValue
        val examples = if (property.type.isInlineType) property.type.examples.filterNotNull().sortedWith(compareBy { it.name }) else listOf()
        return """
            |type: "string"
        """.trimMargin().keepAngleIndent()
    }

    private fun renderProperty(type: ObjectType, property: Property): String {
        var inherited = false;
        if (type.properties.none { p -> p.name == property.name }) {
            inherited = true;
        }
        val discriminatorProp = type.discriminatorProperty()?.name
        val discriminatorValue = type.discriminatorValue
        val examples = if (property.type.isInlineType) property.type.examples.filterNotNull().sortedWith(compareBy { it.name }) else listOf()
        return """
            |${if (property.isPatternProperty()) "\"${property.name}\"" else property.name}:
            |  <<${property.type.renderAnyType()}>>
        """.trimMargin().keepAngleIndent()
    }


}

public fun AnyType.renderAnyType(): String {
    if (this.isInlineType && this.type != null && this.name != null) {
        return this.type.renderAnyType()
    }
    return when(this) {
        is BooleanType -> renderBoolean(this)
        is NumberType -> renderNumber(this)
        is IntegerType -> renderInteger(this)
        is StringType -> renderString(this)
        is DateTimeType -> renderDateTime(this)
        is DateTimeOnlyType -> renderDateTimeOnly(this)
        is DateOnlyType -> renderDateOnly(this)
        is TimeOnlyType -> renderTimeOnly(this)
        is ObjectType -> renderObject(this)
        is ArrayType -> renderArray(this)
        is UnionType -> renderUnionType(this)
        else -> renderAny(this)
    }
}

private fun renderString(type: StringType): String {
    if (type.name != "string") {
        return "${"$"}ref: '#/components/schemas/${type.name}'"
    }
    return "type: \"string\""
}

private fun renderArray(type: ArrayType): String {
    return """
            |type: "array"
            |items:
            |  <<${type.items.renderAnyType()}>>""".trimMargin().keepAngleIndent()
}

private fun renderBoolean(type: BooleanType): String {
    return "type: \"boolean\""
}

private fun renderAny(type: AnyType): String {
    return """
            |{}
        """.trimMargin()
}

private fun renderObject(type: ObjectType): String {
    if (type.name != "object") {
        return "${"$"}ref: '#/components/schemas/${type.name}'"
    }
    return "type: \"object\""
}

private fun NumberType.numberType(): String {
    return when (this.format) {
        NumberFormat.INT,
        NumberFormat.INT8,
        NumberFormat.INT16,
        NumberFormat.INT32,
        NumberFormat.LONG,
        NumberFormat.INT64 ->  "integer"
        else -> "number"
    }
}

private fun NumberType.format(): String {
    return when (this.format) {
        NumberFormat.INT,
        NumberFormat.INT8,
        NumberFormat.INT16,
        NumberFormat.INT32 -> "int32"
        NumberFormat.LONG,
        NumberFormat.INT64 ->  "int64"
        else -> "double"
    }
}

private fun IntegerType.format(): String {
    return when (this.format) {
        NumberFormat.INT,
        NumberFormat.INT8,
        NumberFormat.INT16,
        NumberFormat.INT32 -> "int32"
        else -> "int64"
    }
}

private fun renderNumber(type: NumberType): String {
    return """
            |type: "${type.numberType()}"
            |format: "${type.format()}"
        """.trimMargin()
}

private fun renderInteger(type: IntegerType): String {
    return """
            |type: "integer"
            |format: "${type.format()}"
        """.trimMargin()
}

private fun renderDateTime(type: DateTimeType): String {
    return """
            |type: "string"
            |format: "datetime"
        """.trimMargin()
}

private fun renderDateTimeOnly(type: DateTimeOnlyType): String {
    return """
            |type: "string"
            |format: "datetime-only"
        """.trimMargin()
}

private fun renderDateOnly(type: DateOnlyType): String {
    return """
            |type: "string"
            |format: "date-only"
        """.trimMargin()
}

private fun renderTimeOnly(type: TimeOnlyType): String {
    return """
            |type: "string"
            |format: "time-only"
        """.trimMargin()
}

private fun renderUnionType(type: UnionType): String {
    return """
        |oneOf:
        |  <<${type.oneOf.sortedBy { it.name }.joinToString("\n") { "- \$ref: '#/components/schemas/${it.name}'" }}>>
    """.trimMargin().keepAngleIndent()
}
