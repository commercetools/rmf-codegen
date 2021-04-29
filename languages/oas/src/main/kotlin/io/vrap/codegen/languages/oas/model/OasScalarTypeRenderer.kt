package io.vrap.codegen.languages.oas.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.oas.extensions.packageDir
import io.vrap.codegen.languages.oas.extensions.renderAnnotation
import io.vrap.codegen.languages.oas.extensions.renderEAttributes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.NamedScalarTypeRenderer
import io.vrap.rmf.codegen.rendring.PatternStringTypeRenderer
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import java.lang.Exception

class OasScalarTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName val modelPackageName: String) : ExtensionsBase, StringTypeRenderer, PatternStringTypeRenderer, NamedScalarTypeRenderer {

    override fun render(type: StringType): TemplateFile {
        return when (val vrapType = vrapTypeProvider.doSwitch(type)) {
            is VrapEnumType -> render(vrapType, type)
            is VrapScalarType -> render(vrapType, type)
            else -> throw Exception()
        }
    }

    private fun render(vrapType: VrapEnumType, type: StringType): TemplateFile {
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

        val content = """
            |type: "string"
            |enum:
            |${type.enum.joinToString("\n") { "- ${it.value}" }}
            |<<${type.annotations.joinToString("\n") { it.renderAnnotation() }}>>
        """.trimMargin().keepAngleIndent()
        return TemplateFile(
                relativePath = "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun render(vrapType: VrapScalarType, type: StringType): TemplateFile {
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

        val content = """
            |type: "string"
        """.trimMargin().keepAngleIndent()
        return TemplateFile(
                relativePath = "types/" + type.name + ".raml",
                content = content
        )
    }

    private fun renderExample(type: VrapEnumType, example: Example): String {
        val t = if (type.packageDir(modelPackageName).isNotEmpty()) "../.." else ".."
        val exampleName = "${t}/examples/" + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
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

    private fun renderExample(type: AnyType, example: Example): String {
        val exampleName = "../examples/" + type.name + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
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

}
