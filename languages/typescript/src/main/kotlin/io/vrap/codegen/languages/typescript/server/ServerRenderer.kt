package io.vrap.codegen.languages.typescript.server

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.typescript.allMethods
import io.vrap.codegen.languages.typescript.toImportStatement
import io.vrap.codegen.languages.typescript.toParamName
import io.vrap.codegen.languages.typescript.toResponseName
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

class ServerRenderer @Inject constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider,
        val constantsProvider: ConstantsProvider,
        @ClientPackageName val client_package: String
) : FileProducer, EObjectExtensions {

    override fun produceFiles(): List<TemplateFile> = listOf(apiServer())


    fun apiServer(): TemplateFile {
        val moduleName = "$client_package/api-server"

        val content = """
            |${imports(moduleName)}
            |
            |
            |export type ApiServer = {
            |    <${serverDef(api)}>
            |}
            |
            |export function toResources(apiServer:ApiServer) : Resource[] {
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
                                        "${it.methodName.toLowerCase()}: (input: ${it.toParamName()}) =\\> ${it.toResponseName()}"
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

        return api.allMethods()
                .map {
                    """ |{
                        |   uri: '${it.resource().fullUri.template}',
                        |   method: '${it.methodName.toUpperCase()}',
                        |   handler: ${it.handlerNavigator()}
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
                .flatMap {
                    listOf(
                            VrapObjectType(constantsProvider.parametersModule, it.toParamName()),
                            VrapObjectType(constantsProvider.parametersModule, it.toResponseName())
                    )
                }
                .plus(
                        constantsProvider.Resource
                )
                .map {
                    it.toImportStatement(moduleName)
                }
                .joinToString(separator = "\n")

    }
}




