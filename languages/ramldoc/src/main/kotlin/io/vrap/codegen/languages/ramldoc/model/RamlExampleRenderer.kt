package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.toJson
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.util.EcoreUtil

class RamlExampleRenderer @Inject constructor(val allObjectTypes: MutableList<ObjectType>, override val vrapTypeProvider: VrapTypeProvider) : ExtensionsBase, FileProducer {

    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun produceFiles(): List<TemplateFile> {
        return listOf<TemplateFile>()
                .plus(allObjectTypes.flatMap { render(it) })
    }

    private fun render(type: ObjectType): List<TemplateFile> {
        val files = listOf<TemplateFile>()

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val postmanExampleAnno = type.getAnnotation("postman-example")
        val postmanExample = if (postmanExampleAnno != null) {
            val example = TypesFactory.eINSTANCE.createExample()
            val boolInstance = TypesFactory.eINSTANCE.createBooleanInstance()
            boolInstance.value = true
            example.name = "postman"
            example.strict = boolInstance
            example.value = EcoreUtil.copy(postmanExampleAnno.value)
            example
        } else {
            null
        }
        val examples = type.examples.plus(postmanExample).filterNotNull()


        return files.plus(examples.map { renderExample(vrapType, it) })
    }

    private fun renderExample(type: VrapObjectType, example: Example): TemplateFile {
        val exampleName = "examples/" + type.packageDir(modelPackageName) + type.simpleClassName + "-${if (example.name.isNotEmpty()) example.name else "default"}.json"
        val content = example.value.toJson()

        return TemplateFile(
                relativePath = exampleName,
                content = content
        )
    }
}
