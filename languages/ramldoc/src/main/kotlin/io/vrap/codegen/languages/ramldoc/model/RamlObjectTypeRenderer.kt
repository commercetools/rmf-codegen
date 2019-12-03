package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
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
            |type: object
            |(originalType): ${type.type?.name?: "object"}
            |displayName: ${type.displayName?.value ?: vrapType.simpleClassName}
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
