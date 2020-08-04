package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.ramldoc.extensions.packageDir
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
import io.vrap.rmf.raml.model.types.*

class ApiRamlRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider, @AllAnyTypes val anyTypeList: MutableList<AnyType>) : EObjectExtensions, FileProducer {
    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api),
                oauth2()
        )
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
            |  codeExamples:
            |    type: object
            |    allowedTargets: Method
            |  <<${api.annotationTypes.plus(api.uses.flatMap { libraryUse -> libraryUse.library.annotationTypes }).joinToString("\n") { renderAnnotationType(it) }}>>
            |securitySchemes:
            |  oauth_2_0: !include oauth2.raml
            |securedBy:
            |- oauth_2_0
            |baseUri: ${api.baseUri.template}${if (api.baseUriParameters.size > 0) """
            |baseUriParameters:
            |  <<${api.baseUriParameters.joinToString("\n") { it.renderUriParameter() }}>>""" else ""}
            |types:
            |  <<${anyTypeList.filterNot { it is UnionType }.sortedWith(compareBy { it.name }).joinToString("\n") { "${it.name}: !include ${ramlFileName(it)}" }}>>
            |  
            |${api.allContainedResources.sortedWith(compareBy { it.resourcePath }).joinToString("\n") { "${it.fullUri.template}: !include resources/${it.toResourceName()}.raml" }}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(relativePath = "api.raml",
                content = content
        )
    }

    private fun oauth2(): TemplateFile {
        val content = """
            #%RAML 1.0 SecurityScheme

            description: |
              HTTP API authorization uses [OAuth2](http://tools.ietf.org/html/rfc6750).

              Clients must obtain an access token from the auth service using one of
              the authorization flows described below, before they are able to make authorized requests
              to other commercetools services. On successful completion of an authorization flow,
              a client will be given an `access_token`, which they need to include in requests
              to authorized service endpoints via the HTTP `Authorization` header like this:
            type: OAuth 2.0
            describedBy:
              headers:
                Authorization:
                  description: |
                    On successful completion of an authorization flow,
                    a client will be given an `access_token`, which they need to include in requests
                    to authorized service endpoints via the HTTP `Authorization` header like this:

                    Authorization: Bearer {access_token}
                  type: string
              responses:
                401:
                  description: Unauthorized
            settings:
              authorizationUri: https://auth.sphere.io/oauth/token
              accessTokenUri: https://auth.sphere.io/oauth/token
              authorizationGrants: [ client_credentials ]
              scopes:
                - "manage_project:{projectKey}"
                - "manage_products:{projectKey}"
                - "view_products:{projectKey}"
                - "manage_orders:{projectKey}"
                - "manage_orders:{projectKey}:{storeKey}"
                - "view_orders:{projectKey}"
                - "view_orders:{projectKey}:{storeKey}"
                - "manage_customers:{projectKey}"
                - "view_customers:{projectKey}"
                - "manage_payments:{projectKey}"
                - "view_payments:{projectKey}"
                - "manage_subscriptions:{projectKey}"
                - "manage_extensions:{projectKey}"
                - "manage_customers:{projectKey}"
                - "view_customers:{projectKey}"
                - "manage_types:{projectKey}"
                - "view_types:{projectKey}"
                - "view_shopping_lists:{projectKey}"
                - "manage_shopping_lists:{projectKey}"
                - "manage_my_orders:{projectKey}"
                - "manage_my_orders:{projectKey}:{storeKey}"
                - "manage_my_profile:{projectKey}"
                - "view_project_settings:{projectKey}"
                - "view_published_products:{projectKey}"
        """.trimIndent()
        return TemplateFile(relativePath = "oauth2.raml",
                content = content
        )
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
