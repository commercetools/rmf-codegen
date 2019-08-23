package io.vrap.codegen.languages.typescript.server


import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.typescript.*
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.ResourceContainer
import java.lang.Error
import java.nio.file.Paths

class ParameterGenerator @Inject constructor(
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
                    |${imports(moduleFileName)}
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
                    |   queryParams: {
                    |       <${it.queryParameters.map { "${it.name}: ${it.type.toVrapType().simpleTSName()}" }.joinToString("\n")}>
                    |       [key:string]: ScalarValue | ScalarValue[]
                    |   }
                    |   pathParams: {
                    |       <${it.resource().fullUri.variables.map{ "$it: string" }.joinToString("\n")}>
                    |       [key:string]:string
                    |   }
                    |   <${if(it.bodies.isNotEmpty()) "body: ${it.bodyDefinition()}" else ""}>
                    |}
                    """.trimMargin()
                }
                .joinToString(separator = "\n\n")
    }

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



    private fun imports(moduleName:String):String {
        return api.allMethods()
                .flatMap {
                    it.bodies
                            .map {
                                it.type
                            }
                            .plus(
                                    it.responses
                                            .flatMap {
                                                it.bodies.map {
                                                    body -> body.type
                                                }
                                            }
                            )
                }
                .map{
                    it.toVrapType()
                }
                .filter { it is VrapObjectType }
                .distinct()
                .map {
                    it.importModelsStatements(moduleName)
                }
                .plus(
                        constantsProvider.ScalarValue.toImportStatement(moduleName)
                )
                .joinToString(separator = "\n")

    }

    private fun VrapType.importModelsStatements(moduleName:String):String {
        return when (this) {
            is VrapObjectType -> {
                val relativePath = relativizePaths("./${moduleFilePath.parent}", "$modelsLocation${this.`package`}")
                "import { ${this.simpleTSName()} } from '$relativePath'"
            }
            is VrapArrayType -> {
                val objType = this.itemType as VrapObjectType
                val relativePath = relativizePaths(moduleName, objType.`package`)
                return "import { ${objType.simpleTSName()} } from '$relativePath'"
            }
            else -> throw Error("not supposed to arrive here")
        }
    }
}
