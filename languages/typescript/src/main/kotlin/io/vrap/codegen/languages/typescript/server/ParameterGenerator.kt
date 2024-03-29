package io.vrap.codegen.languages.typescript.server


import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.typescript.*
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.QueryParameter
import java.lang.Error
import java.nio.file.Paths

class ParameterGenerator constructor(
        val api: Api,
        val constantsProvider: ConstantsProvider,
        @ClientPackageName val clientPackageName: String,
        override val vrapTypeProvider: VrapTypeProvider
) : FileProducer, EObjectExtensions {

    override fun produceFiles(): List<TemplateFile> = listOf(parametersDef())

    val modelsLocation = "../model"

    val moduleFileName = "${constantsProvider.parametersModule}.ts"
    val moduleFilePath = Paths.get("${constantsProvider.parametersModule}.ts")

    fun parametersDef(): TemplateFile {



        val content = """
            |$tsGeneratedComment
            |
            |${imports()}
            |
            |${api.parameters()}
            |
            |${api.responses()}
            |
            |${api.handlers()}
            |
            """
                .trimMargin()
                .keepIndentation()


        return TemplateFile(
                relativePath = moduleFileName,
                content = content
        )
    }


    private fun ResourceContainer.parameters(): String {
        return this
                .allMethods()
                .map {
                    """
                    |export type ${it.toParamName()} = {
                    |   headers: {
                    |       <${it.headers.map { "${it.name}: ${it.type.toVrapType().simpleTSName()}" }.joinToString("\n")}>
                    |       [key:string]:string
                    |   },
                    |   <${if(it.hasQueryParams()) it.queryParams() else "" }>
                    |   <${if(it.hasPathParams()) it.pathParams() else "" }>
                    |  
                    |   <${if(it.bodies.isNotEmpty()) "body: ${it.bodyDefinition()}" else ""}>
                    |}
                    """.trimMargin()
                }
                .joinToString(separator = "\n\n")
    }

    private fun Method.queryParams() = """|
        |queryParams: {
        |       <${this.queryParameters.map { "${it.name}: ${it.type.toVrapType().simpleTSName()}" }.joinToString("\n")}>
        |}
    """.trimMargin()


    private fun Method.pathParams() = """|
        |pathParams: {
        |       <${this.resource().fullUri.variables.map{ "$it: string" }.joinToString("\n")}>
        |}
    """.trimMargin()


    private fun Method.bodyDefinition(): String = this.bodies
            .map { it.type.toVrapType().simpleTSName() }
            .joinToString(separator = " | ")

    private fun ResourceContainer.responses(): String {
        return this.allMethods()
                .map {
                    """ |export type ${it.toResponseName()} = 
                        |${it.returnRenderer()}
                        |""".trimMargin()
                }
                .joinToString(separator = "\n")
    }

    private fun Method.returnRenderer():String{

        return this.responses
                .map {
                   """
                    |{
                    |   headers?: {[key:string]:string}
                    |   statusCode: ${it.statusCode?:"number"}
                    |   body: ${it.bodies.map {it.type.toVrapType().simpleTSName()}.joinToString(separator = " | ").ifEmpty { "void" }}
                    |}
                   """.trimMargin()
                }.joinToString(separator = "\n\\|")
                .ifBlank {
                    """
                    |{
                    |   headers?: {[key:string]:string}
                    |   statusCode: number
                    |}
                    """.trimMargin()
                }
    }

    private fun ResourceContainer.handlers(): String {
        return this.allMethods()
                .map {
                    """ |
                        |export type ${it.toHandlerName()} = (input: ${it.toParamName()}) =\> Promise\<${it.toResponseName()}\>
                        |""".trimMargin()
                }
                .joinToString(separator = "\n")
    }



    private fun imports():String {
        return api.allMethods()
                .flatMap { method ->

                    method.bodies
                            .map {
                                it.type.toVrapType()
                            }
                            .plus(
                                    method.responses
                                            .flatMap {
                                                it.bodies.map {
                                                    body -> body.type.toVrapType()
                                                }
                                            }
                            ).plus(

                                    method.queryParameters.map { it.parameterType() }
                            )

                }
                .filter { it is VrapObjectType || it is VrapEnumType}
                .distinct()
                .map {
                    it.importModelsStatements()
                }
                .joinToString(separator = "\n")

    }

        private fun QueryParameter.parameterType(): VrapType {
        val vrapType = this.type.toVrapType()
            if(vrapType is VrapArrayType)
                return vrapType.itemType
            return vrapType
    }

    private fun VrapType.importModelsStatements():String {
        return when (this) {
            is VrapObjectType -> {
                "import { ${this.simpleTSName()} } from '${this.`package`}'"
            }
            is VrapEnumType -> {
                "import { ${this.simpleTSName()} } from '${this.`package`}'"
            }
            is VrapArrayType -> {
                val objType = this.itemType as VrapObjectType
                return "import { ${objType.simpleTSName()} } from '${objType.`package`}'"
            }
            else -> throw Error("not supposed to arrive here")
        }
    }
}
