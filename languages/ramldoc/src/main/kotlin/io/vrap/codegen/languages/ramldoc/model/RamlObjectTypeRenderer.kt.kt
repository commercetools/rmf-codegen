package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*

class RamlObjectTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ExtensionsBase, ObjectTypeRenderer {

    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val content = """
            |#%RAML 1.0 DataType
            |type: ${type.type?.name?: "object"}
            |displayName: ${vrapType.simpleClassName}
            |${if (type.discriminator.isNullOrBlank().not()) "discriminator: ${type.discriminator}" else ""}
            |${if (type.discriminatorValue.isNullOrBlank().not()) "discriminatorValue: ${type.discriminatorValue}" else ""}
            |properties:
            |   <<${type.properties.joinToString("\n") { renderProperty(it) }}>>
        """.trimMargin().keepIndentation("<<", ">>")

        return TemplateFile(
                relativePath = "types/" + vrapType.`package`.replace(modelPackageName, "") + "/" + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun renderProperty(property: Property): String {
        return """
            |${property.name}:
            |   <<${renderType(property.type)}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderScalarType(type: AnyType): String {
        if (!type.isInlineType) {
            return "type: ${type.name}"
        }
        return renderEAttributes(type).plus("type: ${type.name}").joinToString("\n")
    }

    private fun renderEAttributes(type: AnyType): List<String> {
        val eAttributes = type.eClass().eAllAttributes
        return eAttributes.filter { eAttribute -> eAttribute.name != "name" && type.eGet(eAttribute) != null}
                .map { eAttribute -> "${eAttribute.name}: ${type.eGet(eAttribute)}" }

    }

    private fun renderArrayType(type: ArrayType): String {
        var t = renderEAttributes(type).plus("type: array").joinToString("\n")
        if (type.items != null) {
            t += """
                |items:
                |  <<${renderScalarType(type.items)}>>
            """
        }
        return t.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderType(type: AnyType): String {
        var t = ""
        if (type.description?.value.isNullOrBlank().not()) {
            t += """
                |description: |-
                |  <<${type.description.value}>>
                |
            """.trimMargin()
        }
        when (type) {
            is ArrayType -> return t + renderArrayType(type)
            else ->
                return t + renderScalarType(type);
        }
    }
}
