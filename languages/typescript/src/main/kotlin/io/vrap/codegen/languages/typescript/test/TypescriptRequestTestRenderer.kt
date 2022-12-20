package io.vrap.codegen.languages.typescript.test

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.typescript.model.*
import io.vrap.codegen.languages.typescript.toRequestBuilderName
import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import java.util.regex.Pattern
import kotlin.random.Random

class TypescriptRequestTestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider): ResourceRenderer, TsObjectTypeExtensions{

    override fun render(type: Resource): TemplateFile {
        val `package` = (type.toVrapType() as VrapObjectType).`package`
        return TemplateFile(
                relativePath = type.tsRequestModuleName(`package`) + ".test.ts",
                content = """|
                |$tsGeneratedComment
                |<${getImports(type)}>
                |
                |const apiRoot: ApiRoot = new ApiRoot({ executeRequest: null })
                |
                |<${type.getRequestsWithMethodParameters(type)}>
                |<${type.getTests(type)}>
                |<${type.dummyTests(type)}>
                |
            """.trimMargin().keepIndentation()
        )
    }

    protected fun Resource.getTests(type:Resource): String {
        if(type.methods == null || type.methods.filterNot { it.deprecated() }.isEmpty())
            return  ""
        val groupRequests = type.toRequestBuilderName()
        val methodAndUrl = "\${rm.method} and url: \${rm.uri}"
        return """|
                    |  describe('Testing $groupRequests Requests', () =\> {
                    |  const requestsToTest = getRequestsWithMethodParameters()
                    |  requestsToTest.forEach(rm =\> {
                    |  test(`Testing =\> request method: $methodAndUrl`, async () =\> {
                    |    expect(rm.method.toLowerCase()).toBe(rm.request.clientRequest().method.toLowerCase())
                    |    expect(rm.uri.toLowerCase()).toBe(rm.request.clientRequest().uri.toLowerCase())
                    |    })
                    |  })
                    |})      
                    """.trimMargin()
    }

    protected fun Resource.getRequestsWithMethodParameters(type:Resource): String {
        if(type.methods == null || type.methods.filterNot { it.deprecated() }.isEmpty())
            return  ""
        return """|
                    |  export function getRequestsWithMethodParameters(): RequestWithMethod[]  {
                    |  return [
                    |           <<${type.methods.filterNot { it.deprecated() }.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }
                                                    .plus(parameterTestProvider(type, method)) }.filterNotNull().joinToString(",\n")}>>  
                    |  ]
                    |}      
                    """.trimMargin()
    }

    protected fun Resource.dummyTests(type:Resource): String {
        if(type.methods == null || type.methods.filterNot { it.deprecated() }.isEmpty())
        {
            return """|
                    |  test('test', () =\> {
                    |   expect(apiRoot).toBeInstanceOf(ApiRoot)
                    |   })
                    """.trimMargin()
        }
        return ""
    }


    private fun parameterTestProvider(resource: Resource, method: Method, parameter: QueryParameter): String {

        val anno = parameter.getAnnotation("placeholderParam", true)

        var paramName: String = parameter.name
        var methodValue = parameter.template()
        var requiredParams = method.queryParameters.toMutableList().filter { p -> p.name != parameter.name && p.required  }
        var requiredParamsStr: String = requiredParams.map { r -> "${r.name}:${queryParamValueString(r.name, r.type, Random(r.name.hashCode()))}" }.joinToString(", ")
        var requiredParamsStrUrl: String = requiredParams.map { r -> "${r.name}:${queryParamValueString(r.name, r.type, Random(r.name.hashCode())).toString().replace("\"","")}" }.joinToString("&")

        if (anno != null) {
            val o = anno.value as ObjectInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            val placeholderTemplate = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            paramName = "\"${placeholderTemplate.value.replace("<${placeholder.value}>", placeholder.value)}\""
            methodValue = "$paramName"
        }
        else if(paramName.contains("."))
        {
            paramName = "\"${paramName}\""
        }

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "{${r.relativeUri.paramValues().joinToString(", ") { p -> " $p: \"test_$p\""} }}" else ""})" }
                .plus("${method.method}({${if (method.firstBody() != null) "body: null,\nheaders:null,\n" else ""}queryArgs:{ ${paramName}:${methodValue}${if(requiredParamsStr!="") ", $requiredParamsStr" else ""} }})")

        return """
                |{   
                |    method: '${method.method}',
                |    uri: '/${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}?${paramName.replace("\"", "")}=${queryParamValueString(paramName, parameter.type, Random(paramName.hashCode())).toString().replace("\"","")}${ if(requiredParamsStrUrl!="") "&${requiredParamsStrUrl.replace(":","=")}" else ""}',            
                |    request: apiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,    
                |}
            """.trimMargin().keepAngleIndent()
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String? {
        var shouldPassBody = resource.resourcePath.contains("product-projections/search")
                && method.methodName == "post"

        if(method.queryParameters.any{p ->p.required})
            return null
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "{${r.relativeUri.paramValues().joinToString(", ") { p -> " ${if(p.contains('.')) "\"${p}\"" else "$p"}: \"test_$p\""} }}" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null || shouldPassBody)  "{body: null,\nheaders:null}" else ""})")
                //.plus("${method.method}(${if (method.methodName=="post") "{body: null,\nheaders:null}" else ""})")

        return """
            |{           
            |    method: '${method.method}',
            |    uri: '/${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}',    
            |    request: apiRoot
            |    <<${builderChain.joinToString("\n.", ".")}>>
            |}
        """.trimMargin().keepAngleIndent()
    }

    private fun QueryParameter.template(): Any {
        val anno = this.getAnnotation("placeholderParam", true)

        if (anno != null) {
            val o = anno.value as ObjectInstance
            val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            return "sprintf('" + template.value.replace("<" + placeholder.value + ">", "%s") + "', $" + placeholder.value + ")"
        }

        val r = Random(this.name.hashCode())
        return queryParamValue(this.name, this.type, r)
    }
    private fun queryParamValue(name: String, type: AnyType, r: Random) : Any {
        return when (type) {
            is ArrayType -> queryParamValue(name, type.items, r)
            is BooleanType -> true
            is IntegerType -> r.nextInt(1, 10)
            is NumberType -> when (type.format) {
                NumberFormat.DOUBLE -> r.nextDouble()
                NumberFormat.FLOAT -> r.nextFloat()
                else -> r.nextInt(1, 10)
            }
            is StringType -> {
                if(type.enum.isNotEmpty())
                    return "\'${type.enum.first().value}\'"
                else if(type.type?.enum?.isNotEmpty() == true)
                    return "\'${type.type.enum.first().value}\'"
                else
                    return "\"${name}\""
            }
            else -> "\"${name}\""
        }
    }
    private fun queryParamValueString(name: String, type: AnyType, r: Random) : Any {
        return when (type) {
            is ArrayType -> queryParamValueString(name, type.items, r)
            is BooleanType -> true
            is IntegerType -> r.nextInt(1, 10)
            is NumberType -> when (type.format) {
                NumberFormat.DOUBLE -> r.nextDouble()
                NumberFormat.FLOAT -> r.nextFloat()
                else -> r.nextInt(1, 10)
            }
            is StringType -> {
                if(type.enum.isNotEmpty())
                    return "\"${type.enum.first().value}\""
                else if(type.type?.enum?.isNotEmpty() == true)
                    return "\"${type.type.enum.first().value}\""
                else
                    return "\"${name}\""
            }
            else -> "\"${name}\""
        }
    }

    private fun getImports(type:Resource):String
    {
        val `package` = (type.toVrapType() as VrapObjectType).`package`
        val r = type.tsRequestModuleName(`package`)
        val pathContainsOneSlash = countMatches(r, "/") == 1
        var isRoot = type.parent == null || pathContainsOneSlash
        var imports = """|
                |import { RequestWithMethod } from '${if(isRoot)"../../" else "../../../"}request-with-method'
                |import { ApiRoot } from '${if(isRoot)"../../../" else "../../../../"}src'
            """.trimMargin().keepIndentation()
        return imports
    }
    fun countMatches(string: String, pattern: String): Int {
        val matcher = Pattern.compile(pattern).matcher(string)

        var count = 0
        while (matcher.find()) {
            count++
        }
        return count
    }

}
