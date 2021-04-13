package io.vrap.codegen.languages.javalang.client.builder.test

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.*
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.impl.NumberTypeImpl
import kotlin.random.Random

class JavaRequestTestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider): ResourceRenderer, JavaEObjectTypeExtensions, JavaObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content = """
            |package ${vrapType.`package`}.resource;
            |
            |import com.commercetools.api.client.ApiRoot;
            |import com.commercetools.api.defaultconfig.ApiFactory;
            |import com.commercetools.api.defaultconfig.ServiceRegion;
            |import io.vrap.rmf.base.client.oauth2.ClientCredentials;
            |import junitparams.JUnitParamsRunner;
            |import junitparams.Parameters;
            |import org.junit.Assert;
            |import org.junit.Test;
            |import org.junit.runner.RunWith;
            |import org.mockito.Mockito;
            |import io.vrap.rmf.base.client.utils.Generated;
            |import io.vrap.rmf.base.client.ApiHttpClient;
            |import io.vrap.rmf.base.client.ApiHttpRequest;
            |import io.vrap.rmf.base.client.ApiHttpMethod;
            |import com.commercetools.api.client.*;
            |
            |${JavaSubTemplates.generatedAnnotation}
            |@RunWith(JUnitParamsRunner.class)
            |public class ${type.toResourceName()}Test {
            |    private final ApiHttpClient apiHttpClientMock = Mockito.mock(ApiHttpClient.class);
            |    private final String projectKey = "test_projectKey";
            |    private final ApiRoot apiRoot = createClient();
            |
            |    private ApiRoot createClient() {  
            |        return ApiFactory.create(
            |           ClientCredentials.of().withClientId("your-client-id").withClientSecret("your-client-secret").withScopes("your-scopes").build(),
            |               ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(), ServiceRegion.GCP_EUROPE_WEST1.getApiUrl());
            |    }
            |
            |    ${if (type.methods.size > 0) """@Test
            |    @Parameters(method = "requestWithMethodParameters")
            |    public void withMethods(ApiHttpRequest request, String httpMethod, String uri) {
            |        Assert.assertEquals(httpMethod, request.getMethod().name().toLowerCase());
            |        Assert.assertEquals(uri, request.getUri().toString());
            |    }""".trimMargin() else ""}
            |    
            |    ${if (type.methods.size > 0) """@Test
            |    @Parameters(method = "executeMethodParameters")
            |    public void executeWithNullPointerException(ApiHttpRequest httpRequest) throws Exception{
            |        Mockito.when(apiHttpClientMock.execute(httpRequest)).thenThrow(NullPointerException.class);   
            |    }""".trimMargin() else ""}
            |    
            |    private Object[] requestWithMethodParameters() {
            |       return new Object [] {
            |               <<${type.methods.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |       };
            |    }
            |    
            |    private Object[] executeMethodParameters() {
            |       return new Object [] {
            |               <<${type.methods.flatMap { m -> m.responses.map { r -> requestTestProvider(type, m) }.plus(requestTestProvider(type, m)) }.joinToString(",\n")}>>
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
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")
                .plus("createHttpRequest()")

        return """
            |new Object[] {           
            |    apiRoot
            |    <<${builderChain.joinToString("\n.", ".")}>>,
            |    "${method.method}",
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

        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "test_$p"} }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")
                .plus("${parameter.methodName()}(${methodValue})")
                .plus("createHttpRequest()")
        return """
                |new Object[] {           
                |    apiRoot
                |    <<${builderChain.joinToString("\n.", ".")}>>,
                |    "${method.method}",
                |    "/${resource.fullUri.expand(resource.fullUriParameters.map { it.name to "test_${it.name}" }.toMap()).trimStart('/')}?${paramName}=${if (parameter.type.name != "boolean" && parameter.type.name != "number") "${paramName}" else "$methodValue" }",
                |}
            """.trimMargin().keepAngleIndent()
    }

    private fun requestTestProvider(resource: Resource, method: Method): String {
        val constructorArguments = mutableListOf("apiHttpClientMock")
        method.pathArguments().map { it }.forEach {
            if (it != "projectKey"){
                constructorArguments.add("null")
            } else {
                constructorArguments.add(it)
            }
        }

        if (method.bodies != null && method.bodies.isNotEmpty()){
            constructorArguments.add("null")
        }

        return """
            |new Object[] {
            |       new ApiHttpRequest(ApiHttpMethod.${method.method.name},
            |       new ${method.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}).createHttpRequest().getUri(), null, null)
            |    }
        """.trimMargin().keepIndentation()
    }

    private fun QueryParameter.template(): Any {
        val anno = this.getAnnotation("placeholderParam", true)

        if (anno != null) {
            val o = anno.value as ObjectInstance
            val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            return "sprintf('" + template.value.replace("<" + placeholder.value + ">", "%s") + "', $" + placeholder.value + ")"
        }

        val type = this.type
        when (type) {
            is ArrayType -> type.items.toVrapType().simpleName()
            else -> type.toVrapType().simpleName()
        }

        if (type.name == "boolean") {
            return true
        } else if (type.name == "number") {
            return if ((type as NumberTypeImpl).format.name == "INT64") {
                Random.nextInt(1, 10)
            } else if ((type as NumberTypeImpl).format.name == "FLOAT") {
                Random.nextFloat()
            } else if ((type as NumberTypeImpl).format.name == "DOUBLE") {
                Random.nextDouble()
            } else if ((type as NumberTypeImpl).format.name == "INT32") {
                    Random.nextInt(1, 10)
            }  else {
                Random.nextInt(1, 10)
            }
        } else {
            return "\"" + this.name + "\""
        }
    }
}
