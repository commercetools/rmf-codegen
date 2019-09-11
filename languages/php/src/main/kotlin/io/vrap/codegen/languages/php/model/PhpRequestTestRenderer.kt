package io.vrap.codegen.languages.php.model

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.damnhandy.uri.template.impl.VarSpec
import com.google.common.collect.Lists
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import org.eclipse.emf.ecore.EObject
import java.util.stream.Collectors

class PhpRequestTestRenderer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : ResourceRenderer, AbstractRequestBuilder(api, vrapTypeProvider), EObjectTypeExtensions {

    private val resourcePackage = "Resource";

    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val clientTestPackageName = basePackagePrefix + "/test" + clientPackageName.replace(basePackagePrefix, "");
        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${clientTestPackageName.toNamespaceName().escapeAll()}\\$resourcePackage;
            |
            |use PHPUnit\\Framework\\TestCase;
            |use ${clientPackageName.toNamespaceName().escapeAll()}\\ApiRoot;
            |use ${clientPackageName.toNamespaceName().escapeAll()}\\$resourcePackage\\${type.resourceBuilderName()};
            |use Psr\\Http\\Message\\RequestInterface;
            |
            |class ${type.resourceBuilderName()}Test extends TestCase
            |{
            |    public function getRequests()
            |    {
            |        return [
            |           <<${type.methods.flatMap { method -> method.queryParameters.map { parameterTestProvider(type, method, it) }.plus(parameterTestProvider(type, method)) }.joinToString(",\n")}>>
            |        ];
            |    }
            |    
            |    /**
            |     * @dataProvider getRequests()
            |     */
            |    public function testBuilder(callable $!builderFunction, string $!method, string $!relativeUri, string $!body = null)
            |    {
            |        $!builder = new ApiRoot();
            |        $!request = $!builderFunction($!builder);
            |        $!this->assertSame(strtolower($!method), strtolower($!request->getMethod()));
            |        $!this->assertStringContainsString(str_replace(['{', '}'], '', $!relativeUri), (string)$!request->getUri());
            |        if (!is_null($!body)) {
            |            $!this->assertJsonStringEqualsJsonString($!body, (string)$!request->getBody());
            |        };
            |    }
            |}
        """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape()

        val relativeTypeNamespace = vrapType.`package`.toNamespaceName().replace(basePackagePrefix.toNamespaceName() + "\\", "").replace("\\", "/") + "/$resourcePackage"
        val relativePath = "test/unit/" + relativeTypeNamespace + "/" + type.resourceBuilderName() + "Test.php"

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun parameterTestProvider(resource: Resource, method: Method): String {
        return """
            |'${method.toRequestName()}' => [
            |    function(ApiRoot $!builder): RequestInterface {
            |        return $!builder
            |            <<->${resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.uriParameters.isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }.joinToString("\n->")}>>
            |            ->${method.method}(${if (method.firstBody() != null) "null" else ""});
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.template}',
            |]
        """.trimMargin()
    }

    private fun parameterTestProvider(resource: Resource, method: Method, parameter: QueryParameter): String {
        val anno = parameter.getAnnotation("placeholderParam");

        var paramName: String = parameter.name;
        var template = parameter.template()
        if (anno != null) {
            val o = anno.value as ObjectInstance
            val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance
            val placeholderTemplate = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
            paramName = placeholderTemplate.value.replace("<${placeholder.value}>", placeholder.value);
            template = "'${placeholder.value}', '${paramName}'"
        }

        return """
            |'${method.toRequestName()}_${parameter.methodName()}' => [
            |    function(ApiRoot $!builder): RequestInterface {
            |        return $!builder
            |            <<->${resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.uriParameters.isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }.joinToString("\n->")}>>
            |            ->${method.method}(${if (method.firstBody() != null) "null" else ""})
            |            ->${parameter.methodName()}(${template});
            |    },
            |    '${method.method}',
            |    '${resource.fullUri.template}?${paramName}=${paramName}',
            |]
        """.trimMargin()
    }



    fun Resource.resourcePathList(): List<Resource> {
        val path = Lists.newArrayList<Resource>()
        path.add(this)
        var t = this.eContainer()
        while (t is Resource) {
            path.add(t)
            t = t.eContainer()
        }
        return Lists.reverse(path)
    }
}
