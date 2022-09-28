package io.vrap.codegen.languages.ramldoc.model

import com.damnhandy.uri.template.UriTemplate
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityScheme
import io.vrap.codegen.languages.ramldoc.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.raml.model.types.*
import java.lang.IllegalArgumentException
import java.text.MessageFormat

class OasApiRamlRenderer constructor(val api: OpenAPI): FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api)
        ).plus(api.components?.securitySchemes?.map { it.renderScheme() } ?: emptyList())
    }

    private fun apiRaml(api: OpenAPI): TemplateFile {
        val baseUri = api.servers.get(0).url
        val content = """
            |#%RAML 1.0
            |---
            |title: ${api.info.title}
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
            |  package:
            |    type: string
            |    allowedTargets: TypeDeclaration
            |baseUri: ${baseUri}${if (api.servers[0].variables != null && api.servers[0].variables.size > 0) """
            |baseUriParameters:
            |  <<${api.servers[0].variables.entries.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}${if (api.components?.securitySchemes != null) """
            |securitySchemes:
            |  <<${api.components.securitySchemes.entries.joinToString("\n") { "${it.key}: !include ${it.key}.raml" }}>>""" else ""}${if (api.components?.securitySchemes != null) """
            |securedBy:
            |  <<${api.components.securitySchemes.keys.joinToString("\n") { "- ${it}" }}>>""" else ""}
            |
            |types:
            |  <<${api.components.schemas.entries.sortedWith(compareBy { it.key }).joinToString("\n") { "${it.key}: !include types/${it.key}.raml" }}>>
            |
            |${api.paths.entries.sortedWith(compareBy { it.key }).joinToString("\n") { "${it.key}: !include resources/${UriTemplate.fromTemplate(it.key).toResourceName()}.raml" }}
        """.trimMargin().keepAngleIndent()
//            |  <<${api.annotationTypes.plus(api.uses.flatMap { libraryUse -> libraryUse.library.annotationTypes }).joinToString("\n") { renderAnnotationType(it) }}>>
//            |baseUri: ${baseUri}${if (docsBaseUriParameters == null && api.baseUriParameters.size > 0) """
//            |baseUriParameters:
//            |  <<${api.baseUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}${if (docsBaseUriParameters != null) """
//            |baseUriParameters:
//            |  <<${(docsBaseUriParameters.value).toYaml()}>>""" else ""}
//            |${api.annotations.joinToString("\n") { it.renderAnnotation() }}
//            |securitySchemes:
//            |  <<${api.securitySchemes.joinToString("\n") { "${it.name}: !include ${it.name}.raml" }}>>
//            |securedBy:
//            |- oauth_2_0
//            |types:
//            |  <<${anyTypeList.filterNot { it is UnionType }.sortedWith(compareBy { it.name }).joinToString("\n") { "${it.name}: !include ${ramlFileName(it)}" }}>>
//            |
//            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { "${it.fullUri.normalize().template }: !include resources/${it.toResourceName()}.raml" }}

        return TemplateFile(relativePath = "api.raml",
                content = content
        )
    }

    private fun Map.Entry<String, SecurityScheme>.renderScheme(): TemplateFile {
        val settings = this.value
        if (settings.type != SecurityScheme.Type.OAUTH2) {
            throw IllegalArgumentException(MessageFormat.format("Security scheme {0} not supported", settings.type))
        }
        return TemplateFile(
            relativePath = "${this.key}.raml",
            content = """
            |#%RAML 1.0 SecurityScheme
            |description: |
            |  <<${settings.description ?: ""}>>
            |type: ${settings.type}
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
            |  authorizationUri: ${settings.flows.clientCredentials.tokenUrl}
            |  accessTokenUri: ${settings.flows.clientCredentials.tokenUrl}
            |  authorizationGrants: ${settings.flows.authorisationGrants().joinToString(", ", "[ ", " ]")}
            |  scopes:
            |    <<${settings.flows.clientCredentials.scopes.keys.joinToString("\n") { "- '${it}'" }}>>
            """.trimMargin().keepAngleIndent())
//            |  <<${settings.extensions.entries.joinToString("\n") { it.renderAnnotation() }}>>
    }

    private fun renderAnnotationType(annotation: AnyAnnotationType): String {
        return """
            |${annotation.name}:
            |   <<${annotation.renderType()}>>
        """.trimMargin().keepAngleIndent()
    }

    private fun OAuthFlows.authorisationGrants(): List<String> {
        val grants = mutableListOf<String>()
        if (this.clientCredentials != null) grants.add("client_credentials")
        if (this.password != null) grants.add("password")
        if (this.implicit != null) grants.add("implicit")
        if (this.authorizationCode != null) grants.add("authorization_code")
        return grants
    }

//    private fun ramlFileName(type: AnyType): String {
//        when (val vrapType = type.toVrapType()) {
//            is VrapObjectType ->
//                return "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml"
//            is VrapEnumType ->
//                return "types/" + vrapType.packageDir(modelPackageName) + vrapType.simpleClassName + ".raml"
//            is VrapScalarType ->
//                return "types/" + type.name + ".raml"
//            else -> return ""
//        }
//    }
}
