package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.ramldoc.extensions.renderType
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
            |(builtinType): object
            |type: ${type.type?.name?: "object"}
            |displayName: ${type.displayName?.value ?: vrapType.simpleClassName}${if (type.discriminatorProperty() != null) """
            |discriminator: ${type.discriminatorProperty()?.name}""" else ""}${if (type.discriminatorValue.isNullOrBlank().not()) """
            |discriminatorValue: ${type.discriminatorValue}""" else ""}${if (type.subTypes.filterNot { it.isInlineType }.isNotEmpty()) """
            |(subTypes):
            |${type.subTypes.filterNot { it.isInlineType }.joinToString("\n") { "- ${it.name}" }}""" else ""}
            |properties:
            |  <<${type.allProperties.joinToString("\n") { renderProperty(type, it) }}>>
        """.trimMargin().keepIndentation("<<", ">>")

        return TemplateFile(
                relativePath = "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun renderProperty(type: ObjectType, property: Property): String {
        val discriminatorProp = type.discriminatorProperty()?.name
        val discriminatorValue = type.discriminatorValue
        return """
            |${property.name}:
            |  <<${property.type.renderType()}>>${if (discriminatorProp == property.name && discriminatorValue.isNullOrBlank().not()) """
            |  enum:
            |  - $discriminatorValue""" else ""}${if (property.type.default != null) """
            |  default: ${property.type.default.value}""" else ""}
            |  required: ${property.required}
        """.trimMargin().keepIndentation("<<", ">>")
    }


}
