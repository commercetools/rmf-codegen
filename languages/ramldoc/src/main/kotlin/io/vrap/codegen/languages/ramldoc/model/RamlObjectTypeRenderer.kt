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
            |  <<${type.allProperties.joinToString("\n") { renderProperty(it) }}>>
        """.trimMargin().keepIndentation("<<", ">>")

        return TemplateFile(
                relativePath = "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun renderProperty(property: Property): String {
        return """
            |${property.name}:
            |  <<${property.type.renderType()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }


}

private fun AnyType.renderScalarType(): String {
    if (!this.isInlineType) {
        return "type: ${this.name}"
    }
    return this.renderEAttributes().plus("type: ${this.name}").joinToString("\n")
}

private fun AnyType.renderEAttributes(): List<String> {
    val eAttributes = this.eClass().eAllAttributes
    return eAttributes.filter { eAttribute -> eAttribute.name != "name" && this.eGet(eAttribute) != null}
            .map { eAttribute -> "${eAttribute.name}: ${this.eGet(eAttribute)}" }

}

private fun ArrayType.renderArrayType(): String {
    var t = this.renderEAttributes().plus("type: array").joinToString("\n")
    if (this.items != null) {
        t += """
                |items:
                |  <<${this.items.renderScalarType()}>>
            """
    }
    return t.trimMargin().keepIndentation("<<", ">>")
}

private fun AnyType.renderType(): String {
    var t = ""
    if (this.description?.value.isNullOrBlank().not()) {
        t += """
                |description: |-
                |  <<${this.description.value}>>
                |
            """.trimMargin()
    }
    when (this) {
        is ArrayType -> return t + this.renderArrayType()
        else ->
            return t + this.renderScalarType();
    }
}
