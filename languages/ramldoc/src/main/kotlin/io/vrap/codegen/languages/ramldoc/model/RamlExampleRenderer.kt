package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.toJson
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.util.EcoreUtil

class RamlExampleRenderer constructor(val allMethods: List<Method>, @AllAnyTypes val allAnyTypes: List<AnyType>, override val vrapTypeProvider: VrapTypeProvider, @ModelPackageName val modelPackageName: String) : ExtensionsBase, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf<TemplateFile>()
                .plus(allAnyTypes.flatMap { render(it) })
                .plus(allMethods.flatMap { render(it) })
    }

     private fun render(type: AnyType): List<TemplateFile> {
        val files = listOf<TemplateFile>()

        val vrapType = vrapTypeProvider.doSwitch(type)

        val postmanExampleAnno = type.getAnnotation("postman-example")
        val postmanExample = if (postmanExampleAnno != null) {
            val example = TypesFactory.eINSTANCE.createExample()
            val boolInstance = TypesFactory.eINSTANCE.createBooleanInstance()
            boolInstance.value = true
            example.name = if (type.examples.firstOrNull { e -> e.name.isNullOrEmpty() } != null) "postman" else ""
            example.strict = boolInstance
            example.value = EcoreUtil.copy(postmanExampleAnno.value)
            example
        } else {
            null
        }
        val examples = type.examples.plus(postmanExample).filterNotNull()


        return files.plus(examples.map {
            when(vrapType) {
                is VrapObjectType -> renderExample(vrapType, it)
                is VrapEnumType -> renderExample(vrapType, it)
                else -> renderExample(type, it)
            }
        })
    }

    private fun render(method: Method): List<TemplateFile> {
        val files = listOf<TemplateFile>()

        val examples = method.responses.filter { response -> response.isSuccessfull() }.flatMap { it.bodies.flatMap { body -> body.inlineTypes.flatMap { inlineType -> inlineType.examples.map { example -> "${method.toRequestName()}-${it.statusCode}-${if (example.name.isNotEmpty()) example.name else "default"}" to example } } } }.plus(method.bodies.flatMap {
            method.bodies.flatMap {
                body -> body.inlineTypes.flatMap {
                    inlineType -> inlineType.examples.map {
                        example -> "${method.toRequestName()}-${if (example.name.isNotEmpty()) example.name else "default"}" to example
                    }
                }
            }
        }).toMap()

        return files.plus(examples.map {
            renderExample(it.key, it.value)
        })
    }

    private fun renderExample(type: VrapObjectType, example: Example): TemplateFile {
        val exampleName = "examples/" + type.packageDir(modelPackageName) + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        val content = example.value.toJson()

        return TemplateFile(
                relativePath = exampleName,
                content = content
        )
    }

    private fun renderExample(type: VrapEnumType, example: Example): TemplateFile {
        val exampleName = "examples/" + type.packageDir(modelPackageName) + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        val content = example.value.toJson()

        return TemplateFile(
                relativePath = exampleName,
                content = content
        )
    }

    private fun renderExample(type: AnyType, example: Example): TemplateFile {
        val exampleName = "examples/" + type.name + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        val content = example.value.toJson()

        return TemplateFile(
                relativePath = exampleName,
                content = content
        )
    }

    private fun renderExample(exampleName: String, example: Example): TemplateFile {
        val content = example.value.toJson()

        return TemplateFile(
                relativePath = "examples/$exampleName.json",
                content = content
        )
    }
}
