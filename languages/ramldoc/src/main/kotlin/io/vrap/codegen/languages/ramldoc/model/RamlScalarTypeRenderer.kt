package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.renderEAttributes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.NamedScalarTypeRenderer
import io.vrap.rmf.codegen.rendring.PatternStringTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import java.lang.Exception

class RamlScalarTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ExtensionsBase, StringTypeRenderer, PatternStringTypeRenderer, NamedScalarTypeRenderer {

    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun render(type: StringType): TemplateFile {
        return when (val vrapType = vrapTypeProvider.doSwitch(type)) {
            is VrapEnumType -> render(vrapType, type)
            is VrapScalarType -> render(vrapType, type)
            else -> throw Exception()
        }
    }

    private fun render(vrapType: VrapEnumType, type: StringType): TemplateFile {
        val content = """
            |#%RAML 1.0 DataType
            |displayName: ${type.displayName?.value ?: vrapType.simpleClassName}
            |${if (type.description != null) """description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |type: ${type.type?.name?: "string"}
            |enum:
            |${type.enum.joinToString("\n") { "- ${it.value}" }}
        """.trimMargin().keepIndentation("<<", ">>")
        return TemplateFile(
                relativePath = "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun render(vrapType: VrapScalarType, type: StringType): TemplateFile {
        val content = """
            |#%RAML 1.0 DataType
            |displayName: ${type.displayName?.value ?: type.name}
            |(builtinType): string
            |type: ${type.type?.name?: "string"}
            |${if (type.description != null) """description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |${type.renderEAttributes().joinToString("\n")}
        """.trimMargin().keepIndentation("<<", ">>")
        return TemplateFile(
                relativePath = "types/" + type.name + ".raml",
                content = content
        )
    }
}
