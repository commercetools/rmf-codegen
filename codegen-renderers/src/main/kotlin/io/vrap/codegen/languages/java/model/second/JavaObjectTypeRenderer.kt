package io.vrap.codegen.languages.java.model.second

import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.ObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject

abstract class JavaObjectTypeRenderer : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    fun ObjectType.subTypesAnnotations(): String {
        return if (this.hasSubtypes())
            """
            |@JsonSubTypes({
            |   <${this.subTypes.map { "@JsonSubTypes.Type(value = ${it.toVrapType().simpleName()}.class, name = \"${(it as ObjectType).discriminatorValue}\")" }.joinToString(separator = ",\n")}>
            |})
            |@JsonTypeInfo(
            |   use = JsonTypeInfo.Id.NAME,
            |   include = JsonTypeInfo.As.PROPERTY,
            |   property = "${this.discriminator}"
            |)
            """.trimMargin()
        else
            ""
    }

    fun ObjectType.imports() = this.getImports().map { "import $it;" }.joinToString(separator = "\n")

    fun Property.validationAnnotations(): String {
        val validationAnnotations = ArrayList<String>()
        if (this.required != null && this.required!!) {
            validationAnnotations.add("@NotNull")
        }
        if (JavaObjectTypeRenderer.CascadeValidationCheck.doSwitch(this.type)) {
            validationAnnotations.add("@Valid")
        }
        return validationAnnotations.joinToString(separator = "\n")
    }

    private object CascadeValidationCheck : TypesSwitch<Boolean>() {
        override fun defaultCase(`object`: EObject?): Boolean? {
            return false
        }

        override fun caseObjectType(objectType: ObjectType?): Boolean? {
            return true
        }

        override fun caseArrayType(arrayType: ArrayType): Boolean? {
            return if (arrayType.items != null) {
                JavaObjectTypeRenderer.CascadeValidationCheck.doSwitch(arrayType.items)
            } else {
                false
            }
        }
    }
}
