package io.vrap.codegen.languages.typescript.server

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.hasBody
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.typescript.allMethods
import io.vrap.codegen.languages.typescript.joi.simpleJoiName
import io.vrap.codegen.languages.typescript.toHandlerName
import io.vrap.codegen.languages.typescript.toImportStatement
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer

class JoiServerRenderer @Inject constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider,
        val constantsProvider: ConstantsProvider,
        @ClientPackageName val client_package: String
) : FileProducer, EObjectExtensions {

    override fun produceFiles(): List<TemplateFile> = listOf(apiServer())


    fun apiServer(): TemplateFile {
        val moduleName = "$client_package/joi-server"

        val content = """
            |import * as Joi from 'joi'
            |${imports(moduleName)}
            |
            |const requiredString = Joi.string().required()
            |
            |export type ApiServer = {
            |    <${serverDef(api)}>
            |}
            |
            |export function toJoiServerRoutes(apiServer:ApiServer) : ServerRoute[] {
            |    return [
            |               <${resourceArray()}>
            |           ]
            |}
            """
                .trimMargin()
                .keepIndentation()


        return TemplateFile(
                relativePath = "$moduleName.ts",
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
                        |   handler: (request: Request, h: ResponseToolkit, err?: Error) =\> {},
                        |   options: {
                        |      validate: {
                        |        <${if(it.hasBody()) "payload: ${it.bodies[0].type.toVrapType().simpleJoiName()}()," else ""}>
                        |        params: {
                        |          <${it.resource().fullUri.variables.map { "$it: requiredString" }.joinToString(separator = ",\n")}>
                        |        },
                        |        failAction: validateFailAction,
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

    private fun imports(moduleName: String): String {
        return api.allMethods()
                .map {
                    VrapObjectType(constantsProvider.parametersModule, it.toHandlerName())
                }
                .plus(
                        listOf(
                                constantsProvider.Resource,
                                constantsProvider.ServerRoute,
                                constantsProvider.ResponseToolkit
                        )
                )
                .map {
                    it.toImportStatement(moduleName)
                }
                .joinToString(separator = "\n")

    }
}