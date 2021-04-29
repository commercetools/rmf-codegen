package io.vrap.codegen.languages.oas.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.oas.extensions.*
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
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
        val discriminatorTypes = type.subTypes.filterNot { it.isInlineType }.filterIsInstance<ObjectType>().sortedWith(compareBy { it.name })

        val typeContent = """
            |type: "object"${if (type.discriminator != null) """
            |discriminator:
            |  propertyName: ${type.discriminator}
            |  mapping:
            |    <<${discriminatorTypes.joinToString("\n") { "${it.discriminatorValue}: '#/components/schemas/${it.name}'" }}>>""" else ""}${if (properties.isEmpty().not()) """
            |properties:
            |  <<${properties.joinToString("\n") { renderProperty(type, it) }}>>""" else ""}${if (patternProperties.size > 1) """
            |additionalProperties:
            |  oneOf:
            |  <<${patternProperties.joinToString("\n") { "- <<${renderPatternProperties(type, it)}>>".keepAngleIndent() }}>>""" else ""}${if (patternProperties.size == 1) """
            |additionalProperties:
            |  <<${renderPatternProperties(type, patternProperties[0])}>>""" else ""}
            """.trimMargin().keepAngleIndent()

        val content = if (type.type != null) {
            """
                |allOf:
                |- ${"$"}ref: '#/components/schemas/${type.type.name}'
                |- <<$typeContent>>
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
            |  type: "string"
        """.trimMargin().keepAngleIndent()
    }
}
