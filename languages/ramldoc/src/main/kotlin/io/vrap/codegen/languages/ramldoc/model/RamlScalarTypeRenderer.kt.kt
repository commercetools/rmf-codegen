package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*

class RamlScalarTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ExtensionsBase, StringTypeRenderer {

    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapEnumType
        val content = """
            |#%RAML 1.0 DataType
            |type: ${type.type?.name?: "string"}
            |displayName: ${vrapType.simpleClassName}
            |enum:
            |${type.enum.joinToString("\n") { "- ${it.value}" }}
        """.trimMargin()
        return TemplateFile(
                relativePath = "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml",
                content = content
        )
    }
}
