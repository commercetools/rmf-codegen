package io.vrap.codegen.languages.javalang.client.builder.test

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.*
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import kotlin.random.Random

class JavaRequestTestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider): ResourceRenderer, JavaEObjectTypeExtensions, JavaObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content = """
            |package ${vrapType.`package`}.resource;
            |
            |import io.vrap.rmf.base.client.*;
            |import io.vrap.rmf.base.client.error.ApiServerException;
            |import io.vrap.rmf.base.client.error.ApiClientException;
            |import io.vrap.rmf.base.client.VrapHttpClient;
            |import ${vrapType.`package`}.ApiRoot;
            |import org.junit.jupiter.api.Test;
            |import org.junit.jupiter.params.ParameterizedTest;
            |import org.junit.jupiter.params.provider.MethodSource;
            |import org.mockito.Mockito;
            |import io.vrap.rmf.base.client.utils.Generated;
            |import io.vrap.rmf.base.client.ApiHttpClient;
            |import io.vrap.rmf.base.client.ApiHttpRequest;
            |import org.assertj.core.api.Assertions;
            |
            |import java.nio.charset.StandardCharsets;
            |import java.util.concurrent.CompletableFuture;
            |
            |${JavaSubTemplates.generatedAnnotation}
            |public class ${type.toResourceName()}Test {
            |    private final VrapHttpClient httpClientMock = Mockito.mock(VrapHttpClient.class);
            |    private final String projectKey = "test_projectKey";
            |    private final static ApiRoot apiRoot = ApiRoot.of();
            |    private final ApiHttpClient client = ClientBuilder.of(httpClientMock).defaultClient("").build();
            |
            |    ${if (type.methods.size > 0) """@ParameterizedTest
            |    @MethodSource("requestWithMethodParameters")
            |    public void withMethods(ApiHttpRequest request, String httpMethod, String uri) {
            |        Assertions.assertThat(httpMethod).isEqualTo(request.getMethod().name().toLowerCase());
            |        Assertions.assertThat(uri).isEqualTo(request.getUri().toString());
            |    }""".trimMargin() else ""}
            |    
            |    ${if (type.methods.size > 0) """@ParameterizedTest
            |    @MethodSource("executeMethodParameters")
            |    public void executeServerException(HttpRequestCommand<?> httpRequest) throws Exception{
            |        Mockito.when(httpClientMock.execute(Mockito.any())).thenReturn(CompletableFuture.completedFuture(
            |                       new ApiHttpResponse<>(500, null, "".getBytes(StandardCharsets.UTF_8), "Oops!")));
            |                   
            |        Assertions.assertThatThrownBy(
            |               () -> client.execute(httpRequest).toCompletableFuture().get()).hasCauseInstanceOf(ApiServerException.class); 
            |    }""".trimMargin() else ""}
            |    
            |    ${if (type.methods.size > 0) """@ParameterizedTest
            |    @MethodSource("executeMethodParameters")
            |    public void executeClientException(HttpRequestCommand<?> httpRequest) throws Exception{
            |        Mockito.when(httpClientMock.execute(Mockito.any())).thenReturn(CompletableFuture.completedFuture(
            |                       new ApiHttpResponse<>(400, null, "".getBytes(StandardCharsets.UTF_8), "Oops!")));
            |                       
            |        Assertions.assertThatThrownBy(
            |           () -> client.execute(httpRequest).toCompletableFuture().get()).hasCauseInstanceOf(ApiClientException.class);
            |    }""".trimMargin() else ""}
            |    
            |    public static Object[][] requestWithMethodParameters() {
            |       return new Object [][] {
            |               <<${type.methods.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |       };
            |    }
            |    
            |    public static Object[][] executeMethodParameters() {
            |       return new Object [][] {
            |               <<${type.methods.flatMap { method -> method.queryParameters.map { requestTestProvider(type, method, it) }.plus(requestTestProvider(type, method)) }.joinToString(",\n")}>>
            |       };
            |    }
            |}
        """.trimMargin().keepAngleIndent()

        val relativePath = "${vrapType.`package`}.resource.${type.toResourceName()}Test".replace(".", "/") + ".java"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${bodyContent(resource, method)})")
                .plus("createHttpRequest()")

        return """
            |new Object[] {           
            |    apiRoot
            |    <<${builderChain.joinToString("\n.", ".")}>>,
            |    "${method.method}",
            |    "${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}",
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


        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${bodyContent(resource, method)})")
                .plus("${parameter.methodName()}(${methodValue})")
                .plus("createHttpRequest()")
        return """
                |new Object[] {           
                |    apiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,
                |    "${method.method}",
                |    "${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}?${paramName}=${queryParamValueString(paramName, parameter.type, Random(paramName.hashCode()))}",
                |}
            """.trimMargin().keepAngleIndent()
    }

    private fun bodyContent(resource: Resource, method: Method): String {
        val bodyDef = method.firstBody()
        return if (bodyDef != null) {
            if (bodyDef.type.isFile()) {
                "FileTestUtils.testFileFor(${resource.toResourceName()}Test.class)"
            }
            else if (bodyDef.type is ObjectType) {
                if ((bodyDef.type as ObjectType).discriminator != null) {
                    val bodyType = if (bodyDef.type.isInlineType) { bodyDef.type.type } else bodyDef.type
                    "${(bodyType.subTypes.first().toVrapType() as VrapObjectType).fullClassName()}.of()"
                } else {
                    "${(bodyDef.type.toVrapType() as VrapObjectType).fullClassName()}.of()"
                }
            } else {
                "null"
            }
        } else ""
    }

    private fun requestTestProvider(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${bodyContent(resource, method)})")

        return """
                |new Object[] {           
                |    apiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,
                |}
        """.trimMargin().keepIndentation()
    }

    private fun requestTestProvider(resource: Resource, method: Method, parameter: QueryParameter): String {
        val anno = parameter.getAnnotation("placeholderParam", true)

        val paramName: String
        var methodValue = parameter.template()

        if (anno != null) {
            val o = anno.value as ObjectInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            val placeholderTemplate = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            paramName = placeholderTemplate.value.replace("<${placeholder.value}>", placeholder.value)
            methodValue = "\"${placeholder.value}\", \"${paramName}\""
        }

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${bodyContent(resource, method)})")
                .plus("${parameter.methodName()}(${methodValue})")
        return """
                |new Object[] {           
                |    apiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,
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
                is VrapEnumType -> "${vrapType.fullClassName()}.findEnum(\"${name}\")"
                else -> "\"${name}\""
            }
            else -> "\"${name}\""
        }
    }
}
