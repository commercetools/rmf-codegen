package io.vrap.codegen.languages.typescript.server

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.typescript.*
import io.vrap.codegen.languages.typescript.joi.simpleJoiName
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import java.lang.Error
import java.nio.file.Paths

class ServerRenderer @Inject constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider,
        val constantsProvider: ConstantsProvider,
        @ClientPackageName val client_package: String
) : FileProducer, EObjectExtensions {

    override fun produceFiles(): List<TemplateFile> = listOf(apiServer())

    val joiValidatorLocation = "../joi"

    val moduleFileName = "${constantsProvider.serverModule}.ts"
    val moduleFilePath = Paths.get("$moduleFileName.ts")


    fun apiServer(): TemplateFile {
        val moduleName = "$client_package/joi-server"

        val content = """
            |import * as Joi from 'joi'
            |<${handlerImports(moduleName)}>
            |<${joiValidatorImports(moduleName)}>
            |
            |const requiredString = Joi.string().required()
            |
            |export type ApiServer = {
            |    <${serverDef(api)}>
            |}
            |
            |export function toHapiServerRoutes(
            |   arg: {
            |       apiServer: ApiServer,
            |       failAction: Lifecycle.FailAction,
            |       handleError: ErrorHandler
            |   }
            |) : ServerRoute[] {
            |    const { apiServer, failAction, handleError } = arg
            |    return [
            |               <${resourceArray()}>
            |           ]
            |}
            """
                .trimMargin()
                .keepIndentation()


        return TemplateFile(
                relativePath = moduleFileName,
                content = content)


    }

    private fun serverDef(resourceContainer: ResourceContainer): String {
        val listOfString = listOf<String>()
                .plus(
                        if (resourceContainer is Resource) {
                            resourceContainer
                                    .methods
                                    .map {
                                        "${it.methodName.toLowerCase()}: ${it.toHandlerName()}"
                                    }
                        } else {
                            emptyList()
                        }
                )
                .plus(
                        resourceContainer
                                .resources
                                .map {
                                    """
                                    |"${it.relativePath()}": {
                                    |    <${serverDef(it)}>
                                    |}
                                    """.trimMargin()
                                }
                )

        return listOfString.joinToString(separator = ",\n")

    }

    fun resourceArray(): String {

        // the old code for handeler navigator ${it.handlerNavigator()
        return api.allMethods()
                .map {
                    """ |{
                        |   path: '${it.resource().fullUri.template}',
                        |   method: '${it.methodName.toUpperCase()}',
                        |   handler: async (request: Request, responseToolkit: ResponseToolkit, err?: Error) =\> {
                        |        const method =
                        |          ${it.handlerNavigator()}
                        |        try {
                        |          const result = await method({
                        |            headers: request.headers,
                        |            ${if(it.hasPathParams()) "pathParams: request.params as any," else ""}
                        |            ${if(it.hasQueryParams()) "queryParams: request.query as any," else ""}
                        |            <${if(it.hasBody()) "body: request.payload as any" else ""}>
                        |          });
                        |          const response = responseToolkit
                        |            .response(${if(it.hasReturnPayload()) "result.body" else ""})
                        |            .code(result.statusCode);
                        |          // Add headers to response
                        |          for (const header in result.headers) {
                        |            response.header(header, result.headers[header]);
                        |          }
                        |          return response;
                        |         } catch (error) {
                        |          return handleError({
                        |            request,
                        |            responseToolkit,
                        |            error
                        |          })
                        |         }
                        |   },
                        |   options: {
                        |      validate: {
                        |        <${if(it.hasBody()) "payload: ${it.bodies[0].type.toVrapType().simpleJoiName()}()," else ""}>
                        |        params: {
                        |          <${it.resource().fullUri.variables.map { "$it: requiredString" }.joinToString(separator = ",\n")}>
                        |        },
                        |        failAction,
                        |      },
                        |    }
                        |}
                    """.trimMargin()
                }.joinToString(separator = ",\n")
    }

    private fun Method.handlerNavigator(): String = this.resource().fullUri
            .components
            .map {
                it.value
            }.map {
                it.replace("/", "")
            }
            .filter {
                it.isNotEmpty()
            }
            .map {
                "'$it'"
            }
            .joinToString(
                    separator = "][",
                    prefix = "apiServer[",
                    postfix = "].${this.methodName}"
            )


    private fun Resource.relativePath(): String = this.relativeUri.template.replaceFirst("/", "")

    private fun handlerImports(moduleName: String): String {
        return api.allMethods()
                .map {
                    VrapObjectType(constantsProvider.parametersModule, it.toHandlerName())
                }
                .plus(
                        listOf(
                                constantsProvider.Resource,
                                constantsProvider.ServerRoute,
                                constantsProvider.Lifecycle,
                                constantsProvider.Request,
                                constantsProvider.ResponseToolkit,
                                constantsProvider.ErrorHandler
                        )
                )
                .map {
                    it.toImportStatement(moduleName)
                }
                .joinToString(separator = "\n")

    }

    private fun joiValidatorImports(moduleName: String): String {
        return api.allMethods()
                .filter { it.bodies.isNotEmpty() }
                .map { it.bodies[0] }
                .filter { it.type != null }
                .map { it.type.toVrapType() }
                .distinct()
                .map {
                    it.joiImportStatement(moduleName)
                }
                .joinToString(separator = "\n")

    }

    private fun VrapType.joiImportStatement(moduleName:String):String {
        return when (this) {
            is VrapObjectType -> {
                val joiType = this.toJoiVrapType()
                val relativePath = relativizePaths("./${moduleFilePath.parent}", "$joiValidatorLocation${joiType.`package`}")
                "import { ${joiType.simpleClassName} } from '$relativePath'"
            }
            is VrapArrayType -> {
                return this.itemType.joiImportStatement(moduleName)
            }
            is VrapScalarType -> {
                return ""
            }
            else -> throw Error("not supposed to arrive here")
        }
    }

}