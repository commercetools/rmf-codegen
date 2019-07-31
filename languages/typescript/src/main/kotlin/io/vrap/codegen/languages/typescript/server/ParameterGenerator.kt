package io.vrap.codegen.languages.typescript.server


import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.returnType
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.typescript.allMethods
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.toImportStatement
import io.vrap.codegen.languages.typescript.toParamName
import io.vrap.codegen.languages.typescript.toResponseName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.NilType
import io.vrap.rmf.raml.model.types.ObjectType

class ParameterGenerator @Inject constructor(
        val api: Api,
        val constantsProvider: ConstantsProvider,
        override val vrapTypeProvider: VrapTypeProvider,
        @ClientPackageName val client_package: String
) : FileProducer, EObjectExtensions {

    override fun produceFiles(): List<TemplateFile> = listOf(parametersDef())


    fun parametersDef(): TemplateFile {
        val moduleName = constantsProvider.parametersModule

        val content = """
                    |${imports(moduleName)}
                    |
                    |${api.parameters()}
                    |
                    |${api.responses()}
                    |
                    """
                .trimMargin()
                .keepIndentation()


        return TemplateFile(
                relativePath = "$moduleName.ts",
                content = content
        )
    }


    private fun ResourceContainer.parameters(): String {
        return this
                .allMethods()
                .map {
                    """
                    |export interface ${it.toParamName()} extends HttpInput {
                    |   headers: {
                    |       <${it.headers.map { "${it.name}: ${it.type.toVrapType().simpleTSName()}" }.joinToString("\n")}>
                    |       (key:string):string
                    |   },
                    |   queryParams: {
                    |       <${it.queryParameters.map { "${it.name}: ${it.type.toVrapType().simpleTSName()}" }.joinToString("\n")}>
                    |       (key:string):VariableMap
                    |   }
                    |   pathParams: {
                    |       <${it.resource().fullUri.variables.map{ "$it: string" }.joinToString("\n")}>
                    |       (key:string):string
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
                    """|
                        |export interface ${it.toResponseName()} extends HttpResponse {
                        |   headers?: {(key:string):string}
                        |   statusCode: number
                        |   <${if(it !is NilType) "payload: ${it.returnType().toVrapType().simpleTSName()}" else ""}>
                        |}
                        |""".trimMargin()
                }
                .joinToString(separator = "\n\n")
    }




    private fun imports(moduleName:String):String {
        return api.allMethods()
                .flatMap {
                    it.bodies
                            .map {
                                it.type
                            }
                            .plus(it.returnType())
                }
                .map{
                    it.toVrapType()
                }
                .plus(
                        listOf(
                                constantsProvider.HttpInput,
                                constantsProvider.HttpResponse,
                                constantsProvider.VariableMap
                        )
                )
                .filter { it is VrapObjectType }
                .distinct()
                .map {
                    it.toImportStatement(moduleName)
                }
                .joinToString(separator = "\n")

    }
}
