package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.extensions.renderEAttributes
import io.vrap.codegen.languages.ramldoc.extensions.toJson
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.NamedScalarTypeRenderer
import io.vrap.rmf.codegen.rendering.NamedStringTypeRenderer
import io.vrap.rmf.codegen.rendering.PatternStringTypeRenderer
import io.vrap.rmf.codegen.rendering.StringTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import java.lang.Exception

class RamlStringTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName override val modelPackageName: String, override val inlineExamples: Boolean = false) : RamlScalarTypeRenderer<StringType>(vrapTypeProvider, modelPackageName, inlineExamples), StringTypeRenderer, PatternStringTypeRenderer, NamedStringTypeRenderer
class RamlAnyTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName override val modelPackageName: String, override val inlineExamples: Boolean = false) : RamlScalarTypeRenderer<AnyType>(vrapTypeProvider, modelPackageName, inlineExamples)

sealed class RamlScalarTypeRenderer<T: AnyType> constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName open val modelPackageName: String, open val inlineExamples: Boolean = false) : ExtensionsBase, NamedScalarTypeRenderer<T> {


    override fun render(type: T): TemplateFile {
        return when (val vrapType = vrapTypeProvider.doSwitch(type)) {
            is VrapEnumType -> when (type) {
                is StringType -> render(vrapType, type)
                else -> throw Exception()
            }
            is VrapScalarType -> render(vrapType, type)
            else -> throw Exception()
        }
    }

    private fun render(vrapType: VrapEnumType, type: AnyType): TemplateFile {
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
            |#%RAML 1.0 DataType
            |displayName: ${type.displayName?.value ?: vrapType.simpleClassName}
            |${if (type.description != null) """description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |type: ${type.type?.name?: "string"}
            |enum:
            |${type.enum.joinToString("\n") { "- '${it.value}'" }}
            |<<${type.annotations.joinToString("\n") { it.renderAnnotation() }}>>${if (examples.isNotEmpty()) """
            |examples:
            |  <<${examples.joinToString("\n") { renderExample(vrapType, it, inlineExamples) }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
        return TemplateFile(
                relativePath = "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml",
                content = content
        )
    }

    private fun render(vrapType: VrapScalarType, type: AnyType): TemplateFile {
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
            |#%RAML 1.0 DataType
            |displayName: ${type.displayName?.value ?: type.name}
            |(builtinType): string
            |<<${type.annotations.joinToString("\n") { it.renderAnnotation() }}>>
            |type: ${type.type?.name?: vrapType.scalarType}
            |${if (type.description != null) """description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |${type.renderEAttributes().joinToString("\n")}${if (examples.isNotEmpty()) """
            |examples:
            |  <<${examples.joinToString("\n") { renderExample(type, it, inlineExamples) }}>>""" else ""}
        """.trimMargin().keepAngleIndent()
        val packageDir = type.getAnnotation("package")?.value?.value.toString().packageDir(modelPackageName)
        return TemplateFile(
                relativePath = "types/" + type.name + ".raml",
                content = content
        )
    }

    private fun renderExample(type: VrapEnumType, example: Example, inlineExample: Boolean = false): String {
        val t = if (type.packageDir(modelPackageName).isNotEmpty()) "../.." else ".."
        val exampleName = "${t}/examples/" + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        return """
            |${if (example.name.isNotEmpty()) example.name else "default"}:${if (example.displayName != null) """
            |  displayName: ${example.displayName.value.trim()}""" else ""}${if (example.description != null) """
            |  description: |-
            |    <<${example.description.value.trim()}>>""" else ""}${if (example.annotations.isNotEmpty()) """
            |  <<${example.annotations.joinToString("\n") { it.renderAnnotation() }}>>""" else ""}
            |  strict: ${example.strict.value}
            |  value: ${if (!inlineExample) " !include $exampleName" else if (example.value is ObjectInstance || example.value is ArrayInstance) """
            |    <<${example.value.toJson().escapeAll()}>>""" else " " + example.value.toJson().escapeAll() }
        """.trimMargin().keepAngleIndent()
    }

    private fun renderExample(type: AnyType, example: Example, inlineExample: Boolean = false): String {
        val exampleName = "../examples/" + type.name + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        return """
            |${if (example.name.isNotEmpty()) example.name else "default"}:${if (example.displayName != null) """
            |  displayName: ${example.displayName.value.trim()}""" else ""}${if (example.description != null) """
            |  description: |-
            |    <<${example.description.value.trim()}>>""" else ""}${if (example.annotations.isNotEmpty()) """
            |  <<${example.annotations.joinToString("\n") { it.renderAnnotation() }}>>""" else ""}
            |  strict: ${example.strict.value}
            |  value: ${if (!inlineExample) " !include $exampleName" else if (example.value is ObjectInstance || example.value is ArrayInstance) """
            |    <<${example.value.toJson().escapeAll()}>>""" else " " + example.value.toJson().escapeAll() }
        """.trimMargin().keepAngleIndent()
    }
}
