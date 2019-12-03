package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.*

class ApiRamlRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {
    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api)
        )
    }

    private fun apiRaml(api: Api): TemplateFile {
        val content = """
            |#%RAML 1.0
            |---
            |title: ${api.title}
            |annotationTypes:
            |  resourcePathUri:
            |    type: string
            |    allowedTargets: Method
            |  originalType:
            |    type: string
            |    allowedTargets: TypeDeclaration
            |types:
            |  <<${api.types.filterNot { it is UnionType }.sortedWith(compareBy { it.name }).joinToString("\n") { "${it.name}: !include ${ramlFileName(it)}" }}>>
            |  
            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { "${it.fullUri.template}: !include resources/${it.toResourceName()}.raml" }}
        """.trimMargin().keepIndentation("<<", ">>")

        return TemplateFile(relativePath = "api.raml",
                content = content
        )
    }

    private fun ramlFileName(type: AnyType): String {
        when (val vrapType = type.toVrapType()) {
            is VrapObjectType ->
                return "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml"
            is VrapEnumType ->
                return "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml"
            is VrapScalarType ->
                return "types/" + type.name + ".raml"
            else -> return ""
        }
    }
}
