package io.vrap.codegen.languages.csharp.client.builder.test

import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import kotlin.random.Random

class CsharpRequestTestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, private val basePackagePrefix: String): ResourceRenderer, CsharpEObjectTypeExtensions, CsharpObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toCsharpVType() as VrapObjectType
        var entityFolder = type.GetNameAsPlural()
        val cPackage = vrapType.requestBuildersPackage(entityFolder)
        val modelsUsing = basePackagePrefix.replace("Tests", "Models")

        val content = """
            |using System;
            |using System.Collections.Generic;
            |using System.Net.Http;
            |using System.Text.Json;
            |using ${basePackagePrefix};
            |using ${modelsUsing};
            |using ${"$modelsUsing.Common"};
            |using Xunit;
            |
            |namespace $cPackage
            |{
            |   public class ${type.toResourceName()}Test:RequestBuilderParentTests 
            |   { 
            |       ${if (type.methods.size > 0) 
                 """[Theory]
            |       [MemberData(nameof(GetData))]
            |       public void WithMethods(HttpRequestMessage request, string httpMethod, string uri) {
            |           Assert.Equal(httpMethod.ToLower(), request.Method.Method.ToLower());
            |           Assert.Equal(uri.ToLower(), request.RequestUri.ToString().ToLower());
            |       }""".trimMargin() else ""}
            |       
            |       public static IEnumerable<object[]> GetData() {
            |       return new List<object[]> {
            |               <<${type.methods.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |       };
            |    }
            |   }
            |}
        """.trimMargin().keepAngleIndent()

        val relativePath = cPackage
                .replace(basePackagePrefix, "").replace("Tests","").replace(".", "/")
                .trimStart('/').trimEnd('/')
        return TemplateFile(
                relativePath = "${relativePath}/${type.toResourceName()}Test.cs",
                content = content
        )
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName().firstUpperCase()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method.toString().firstUpperCase()}(${if (method.firstBody() != null) "null" else ""})")
                .plus("Build()")

        return """
            |new Object[] {           
            |    ApiRoot
            |    <<${builderChain.joinToString("\n.", ".")}>>,
            |    "${method.method.toString().firstUpperCase()}",
            |    "/${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}",
            |}
        """.trimMargin().keepAngleIndent()
    }

    private fun parameterTestProvider(resource: Resource, method: Method, parameter: QueryParameter): String {
        val anno = parameter.getAnnotation("placeholderParam", true)

        var paramName: String = parameter.name
        var methodValue = parameter.template()

        if (anno != null) {
            val o = anno.value as ObjectInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            val placeholderTemplate = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            paramName = placeholderTemplate.value.replace("<${placeholder.value}>", placeholder.value)
            methodValue = "\"${placeholder.value}\", \"${paramName}\""
        }

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName().firstUpperCase()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method.toString().firstUpperCase()}(${if (method.firstBody() != null) "null" else ""})")
                .plus("${parameter.methodName().firstUpperCase()}(${methodValue})")
                .plus("Build()")
        return """
                |new Object[] {           
                |    ApiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,
                |    "${method.method.toString().firstUpperCase()}",
                |    "/${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}?${paramName}=${queryParamValueString(paramName, parameter.type, Random(paramName.hashCode()))}",
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
            else -> name
        }
    }

    private fun queryParamValue(name: String, type: AnyType, r: Random) : Any {
        val vrapType = type.toVrapType();
        return when (type) {
            is ArrayType -> queryParamValue(name, type.items, r)
            is BooleanType -> true
            is IntegerType -> r.nextInt(1, 10)
            is NumberType -> when (type.format) {
                NumberFormat.DOUBLE -> r.nextDouble()
                NumberFormat.FLOAT -> r.nextFloat()
                else -> r.nextInt(1, 10)
            }
            is StringType -> when (vrapType) {
                is VrapEnumType -> "${vrapType.`package`.replace(".Tests", "")}.${vrapType.simpleName()}.FindEnum(\"${name}\")"
                else -> "\"${name}\""
            }
            else -> "\"${name}\""
        }
    }
}
