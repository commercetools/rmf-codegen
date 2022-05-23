package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.extensions.renderEAttributes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.NamedScalarTypeRenderer
import io.vrap.rmf.codegen.rendering.NamedStringTypeRenderer
import io.vrap.rmf.codegen.rendering.PatternStringTypeRenderer
import io.vrap.rmf.codegen.rendering.StringTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import java.lang.Exception

class RamlStringTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName override val modelPackageName: String) : RamlScalarTypeRenderer<StringType>(vrapTypeProvider, modelPackageName), StringTypeRenderer, PatternStringTypeRenderer, NamedStringTypeRenderer
class RamlAnyTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName override val modelPackageName: String) : RamlScalarTypeRenderer<AnyType>(vrapTypeProvider, modelPackageName)

sealed class RamlScalarTypeRenderer<T: AnyType> constructor(override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName open val modelPackageName: String) : ExtensionsBase, NamedScalarTypeRenderer<T> {


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
            |${type.enum.joinToString("\n") { "- ${it.value}" }}
            |<<${type.annotations.joinToString("\n") { it.renderAnnotation() }}>>${if (examples.isNotEmpty()) """
            |examples:
            |  <<${examples.joinToString("\n") { renderExample(vrapType, it) }}>>""" else ""}
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
            |type: ${type.type?.name?: "string"}
            |${if (type.description != null) """description: |-
            |  <<${type.description.value.trim()}>>""" else ""}
            |${type.renderEAttributes().joinToString("\n")}${if (examples.isNotEmpty()) """
            |examples:
            |  <<${examples.joinToString("\n") { renderExample(type, it) }}>>""" else ""}
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
