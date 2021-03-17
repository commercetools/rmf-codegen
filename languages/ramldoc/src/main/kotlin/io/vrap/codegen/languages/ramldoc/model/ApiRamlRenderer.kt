package io.vrap.codegen.languages.ramldoc.model

import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
import io.vrap.codegen.languages.ramldoc.extensions.renderAnnotation
import io.vrap.codegen.languages.ramldoc.extensions.renderType
import io.vrap.codegen.languages.ramldoc.extensions.renderUriParameter
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.security.OAuth20Settings
import io.vrap.rmf.raml.model.security.SecurityScheme
import io.vrap.rmf.raml.model.types.*

class ApiRamlRenderer constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider, @AllAnyTypes val anyTypeList: List<AnyType>, @ModelPackageName val modelPackageName: String) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api)
        ).plus(api.securitySchemes.map { it.renderScheme() })
    }

    private fun apiRaml(api: Api): TemplateFile {
        val content = """
            |#%RAML 1.0
            |---
            |title: ${api.title}
            |annotationTypes:
            |  resourceName:
            |    type: string
            |    allowedTargets: [Resource, Method]
            |  resourcePathUri:
            |    type: string
            |    allowedTargets: [Resource, Method]
            |  builtinType:
            |    type: string
            |    allowedTargets: TypeDeclaration
            |  oneOf:
            |    type: array
            |    items: string
            |    allowedTargets: TypeDeclaration
            |  inherited:
            |    type: boolean
            |    allowedTargets: TypeDeclaration
            |  codeExamples:
            |    type: object
            |    allowedTargets: Method
            |  <<${api.annotationTypes.plus(api.uses.flatMap { libraryUse -> libraryUse.library.annotationTypes }).joinToString("\n") { renderAnnotationType(it) }}>>
            |baseUri: ${api.baseUri.template}${if (api.baseUriParameters.size > 0) """
            |baseUriParameters:
            |  <<${api.baseUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}
            |${api.annotations.joinToString("\n") { it.renderAnnotation() }}
            |securitySchemes:
            |  <<${api.securitySchemes.joinToString("\n") { "${it.name}: !include ${it.name}.raml" }}>>
            |securedBy:
            |- oauth_2_0
            |types:
            |  <<${anyTypeList.filterNot { it is UnionType }.sortedWith(compareBy { it.name }).joinToString("\n") { "${it.name}: !include ${ramlFileName(it)}" }}>>
            |  
            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { "${it.fullUri.template}: !include resources/${it.toResourceName()}.raml" }}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(relativePath = "api.raml",
                content = content
        )
    }

    private fun SecurityScheme.renderScheme(): TemplateFile {
        val settings = this.settings as OAuth20Settings
        return TemplateFile(
            relativePath = "${this.name}.raml",
            content = """
            |#%RAML 1.0 SecurityScheme
            |description: |
            |  <<${this.description?.value}>>
            |type: ${this.type}
            |describedBy:
            |  headers:
            |    Authorization:
            |      description: |
            |        On successful completion of an authorization flow,
            |        a client will be given an `access_token`, which they need to include in requests
            |        to authorized service endpoints via the HTTP `Authorization` header like this:
            |
            |        Authorization: Bearer {access_token}
            |      type: string
            |  responses:
            |    401:
            |      description: Unauthorized
            |settings:
            |  authorizationUri: ${settings.authorizationUri}
            |  accessTokenUri: ${settings.accessTokenUri}
            |  <<${settings.annotations.joinToString("\n") { it.renderAnnotation() }}>>
            |  authorizationGrants: ${settings.authorizationGrants.joinToString(", ", "[ ", " ]")}
            |  scopes:
            |    <<${settings.scopes.joinToString("\n") { "- '${it}'" }}>>
            """.trimMargin().keepAngleIndent())
    }

    private fun renderAnnotationType(annotation: AnyAnnotationType): String {
        return """
            |${annotation.name}:
            |   <<${annotation.renderType()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun ramlFileName(type: AnyType): String {
        when (val vrapType = type.toVrapType()) {
            is VrapObjectType ->
                return "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml"
            is VrapEnumType ->
                return "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml"
            is VrapScalarType ->
                return "types/" + type.name + ".raml"
            else -> return ""
        }
    }
}
