package io.vrap.codegen.languages.ramldoc.model

import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.*
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent

class OasStringTypeRenderer constructor(override val modelPackageName: String) : OasScalarTypeRenderer<StringSchema, String>(modelPackageName)
//class OasAnyTypeRenderer constructor(override val modelPackageName: String) : OasScalarTypeRenderer<AnyType>(modelPackageName)

sealed class OasScalarTypeRenderer<T: Schema<V>, V: Any> constructor(open val modelPackageName: String) : Renderer<Map.Entry<String, T>> {

    override fun render(type: Map.Entry<String, T>): TemplateFile {
        val typeName = type.key
        val typeVal = type.value
        val packageName = typeVal.extensions?.get("x-annotation-package") ?: "Common"

//        return when (val vrapType = vrapTypeProvider.doSwitch(type)) {
//            is VrapEnumType -> when (type) {
//                is StringType -> render(vrapType, type)
//                else -> throw Exception()
//            }
//            is VrapScalarType -> render(vrapType, type)
//            else -> throw Exception()
//        }
        val content = """
            |#%RAML 1.0 DataType
            |(package): $packageName
            |displayName: $typeName
            |type: string
            |(builtinType): string
            |${if (typeVal.enum != null && typeVal.enum.size > 0) """
            |enum:
            |  <<${typeVal.enum.joinToString("\n") { "- '$it'" }}>>
            """.trimMargin().keepAngleIndent() else ""}
            """.trimMargin().keepAngleIndent()

        return TemplateFile(
            relativePath = "types/$typeName.raml",
            content = content
        )
    }
}
