package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.common.collect.Lists
import com.google.common.net.MediaType
import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.MethodRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

/**
 * Query parameters with this annotation should be ignored by JVM sdk.
 */

class JavaStringHttpRequestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, JavaEObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val implements = Lists.newArrayList<String>()
                .plus(
                        when (val ex = type.getAnnotation("java-implements") ) {
                            is Annotation -> {
                                (ex.value as StringInstance).value.escapeAll()
                            }
                            else -> null
                        }
                )
                .plus(
                        type.`is`.distinctBy { it.trait.name }.map { "${it.trait.className()}<${type.toStringRequestName()}>".escapeAll() }
                )
                .filterNotNull()
        if (type.bodies == null || type.bodies.isEmpty() || type.bodies[0].type.toVrapType() !is VrapObjectType) {
            return TemplateFile("", "ignore")
        }

        val content = """
            |package ${vrapType.`package`.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.utils.Utils;
            |
            |import java.io.InputStream;
            |import java.io.IOException;
            |
            |import java.net.URI;
            |import java.net.URLConnection;
            |import java.nio.charset.StandardCharsets;
            |import java.nio.file.Files;
            |
            |import java.time.Duration;
            |import java.util.ArrayList;
            |import java.util.Collection;
            |import java.util.List;
            |import java.util.Map;
            |import java.util.HashMap;
            |import java.util.Optional;
            |import java.util.function.Function;
            |import java.util.function.Supplier;
            |import java.util.stream.Collectors;
            |import java.util.concurrent.CompletableFuture;
            |import io.vrap.rmf.base.client.utils.Generated;
            |import com.fasterxml.jackson.core.type.TypeReference;
            |
            |import javax.annotation.Nullable;
            |
            |import java.io.UnsupportedEncodingException;
            |import java.net.URLEncoder;
            |import io.vrap.rmf.base.client.*;
            |${type.imports()}
            |import org.apache.commons.lang3.builder.EqualsBuilder;
            |import org.apache.commons.lang3.builder.HashCodeBuilder;
            |
            |import static io.vrap.rmf.base.client.utils.ClientUtils.blockingWait;
            |
            |/**
            |${type.toComment(" *").escapeAll()}
            | *
            | * \<hr\>
            | <${type.builderComment().escapeAll()}>
            | */
            |<${JavaSubTemplates.generatedAnnotation}>${if (type.markDeprecated() ) """
            |@Deprecated""" else ""}
            |public class ${type.toStringRequestName()} extends StringBodyApiMethod\<${type.toStringRequestName()}, ${type.javaReturnType(vrapTypeProvider)}\>${if (implements.isNotEmpty()) " implements ${implements.joinToString(", ")}" else ""} {
            |
            |    @Override
            |    public TypeReference\<${type.javaReturnType(vrapTypeProvider)}\> resultType() {
            |        return new TypeReference\<${type.javaReturnType(vrapTypeProvider)}\>() {
            |        };
            |    }
            |
            |    <${type.fields()}>
            |
            |    <${type.constructor()}>
            |
            |    <${type.copyConstructor()}>
            |
            |    <${type.buildRequestMethod()}>
            |
            |    <${type.executeBlockingMethod()}>
            |
            |    <${type.executeMethod()}>
            |
            |    <${type.pathArgumentsGetters()}>
            |
            |    <${type.queryParamsGetters()}>
            |
            |    <${type.pathArgumentsSetters()}>
            |
            |    <${type.queryParamsSetters()}>
            |
            |    <${type.queryParamsTemplateSetters()}>
            |    
            |    <${type.bodyMethods()}>
            |
            |    <${type.equalsMethod()}>
            |
            |    @Override
            |    protected ${type.toStringRequestName()} copy() {
            |        return new ${type.toStringRequestName()}(this);
            |    }
            |}
        """.trimMargin()
                .keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${type.toStringRequestName()}".replace(".", "/") + ".java",
                content = content
        )
    }

    fun Method.builderComment(): String {
        val resource = this.eContainer() as Resource
        return """
            |\<div class=code-example\>
            |\<pre\>\<code class='java'\>{@code
            |  CompletableFuture\<ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\>\> result = apiRoot
            |           <${builderComment(resource, this)}>
            |}\</code\>\</pre\>
            |\</div\>
        """.trimMargin().keepIndentation().split("\n").joinToString("\n", transform = { "* $it"})
    }

    private fun builderComment(resource: Resource, method: Method) : String {
        return resource.resourcePathList().map { r -> ".${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") { p -> "{$p}"} }\"" else ""})" }
            .plus(".${method.method}(\"\")")
            .plus(method.queryParameters.filter { it.required }.map { ".with${it.fieldName().firstUpperCase()}(${it.fieldName()})" })
            .plus(".execute()")
            .joinToString("\n")
    }

    private fun Method.equalsMethod() : String {
        val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType

        val body: String = methodBodyVrapType.simpleClassName.firstLowerCase()
        val fields = this.pathArguments().plus(body).filterNot { s -> s.isEmpty() }

        return """
            |@Override
            |public boolean equals(Object o) {
            |    if (this == o) return true;
            |
            |    if (o == null || getClass() != o.getClass()) return false;
            |
            |    ${this.toStringRequestName()} that = (${this.toStringRequestName()}) o;
            |
            |    return new EqualsBuilder()
            |            <${fields.joinToString("\n") { ".append(${it}, that.${it})" }}>
            |            .isEquals();
            |}
            |
            |@Override
            |public int hashCode() {
            |    return new HashCodeBuilder(17, 37)
            |        <${fields.joinToString("\n") { ".append(${it})" }}>
            |        .toHashCode();
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Trait.className(): String {
        val vrapType = vrapTypeProvider.doSwitch(this as EObject) as VrapObjectType

        return "${vrapType.`package`.toJavaPackage()}.${vrapType.simpleClassName}"
    }

    private fun Method.constructor(): String {
        val constructorArguments = mutableListOf("final ApiHttpClient apiHttpClient")
        val constructorAssignments = mutableListOf("super(apiHttpClient);")

        this.pathArguments().map { "String $it" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.$it = $it;" }.forEach { constructorAssignments.add(it) }

        val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
        val methodBodyArgument =
            "String ${methodBodyVrapType.simpleClassName.firstLowerCase()}"
        constructorArguments.add(methodBodyArgument)
        val methodBodyAssignment =
            "this.${methodBodyVrapType.simpleClassName.firstLowerCase()} = ${methodBodyVrapType.simpleClassName.firstLowerCase()};"
        constructorAssignments.add(methodBodyAssignment)

        return """
            |public ${this.toStringRequestName()}(${constructorArguments.joinToString(separator = ", ")}) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation().escapeAll()
    }

    private fun Method.copyConstructor(): String {
        val constructorAssignments = mutableListOf("super(t);")

        this.pathArguments().map { "this.$it = t.$it;" }.forEach { constructorAssignments.add(it) }

        val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
        val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.firstLowerCase()} = t.${methodBodyVrapType.simpleClassName.firstLowerCase()};"
        constructorAssignments.add(methodBodyAssignment)

        return """
            |public ${this.toStringRequestName()}(${this.toStringRequestName()} t) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation().escapeAll()
    }

    private fun Method.fields(): String {

        val pathArgs = this.pathArguments().map { "private String $it;" }.joinToString(separator = "\n")

        val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
        val body = "private String ${methodBodyVrapType.simpleClassName.firstLowerCase()};"

        return """|
            |<$pathArgs>
            |
            |<$body>
        """.trimMargin()
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }

    private fun Method.pathArguments() : List<String> {
        return this.resource().fullUri.variables.toList()
    }

    private fun Method.buildRequestMethod() : String {

        val pathArguments = this.pathArguments().map { "{$it}" }
        var stringFormat = this.resource().fullUri.template
        pathArguments.forEach { stringFormat = stringFormat.replace(it, "%s") }
        val stringFormatArgs = pathArguments
                .map { it.replace("{", "").replace("}", "") }
                .map { "this.$it" }
                .joinToString(separator = ", ")
        stringFormat = stringFormat.trimStart('/')
        val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
        val bodyName : String = methodBodyVrapType.simpleClassName.firstLowerCase()

        val requestPathGeneration : String = """
            |List<String> params = new ArrayList<>(getQueryParamUriStrings());
            |String httpRequestPath = String.format("$stringFormat", $stringFormatArgs);
            |if (!params.isEmpty()) {
            |    httpRequestPath += "?" + String.join("&", params);
            |}
        """.trimMargin().escapeAll()
        val bodySetter: String = """
                |return new ApiHttpRequest(ApiHttpMethod.${this.method.name}, URI.create(httpRequestPath), getHeaders(), $bodyName.getBytes(StandardCharsets.UTF_8));
                |""".trimMargin()

        return """
            |@Override
            |protected ApiHttpRequest buildHttpRequest() {
            |    <$requestPathGeneration>
            |    $bodySetter
            |}
        """.trimMargin()
    }

    private fun Method.executeBlockingMethod() : String {
        return """
            |@Override
            |public ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\> executeBlocking(final ApiHttpClient client, final Duration timeout) {
            |    return executeBlocking(client, timeout, ${this.javaReturnType(vrapTypeProvider)}.class);
            |}
        """.trimMargin()
    }

    private fun Method.executeMethod() : String {
        return """
            |@Override
            |public CompletableFuture\<ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\>\> execute(final ApiHttpClient client) {
            |    return execute(client, ${this.javaReturnType(vrapTypeProvider)}.class);
            |}
        """.trimMargin()
    }

    private fun Method.pathArgumentsGetters() : String = this.pathArguments()
            .map { """
                |public String get${it.firstUpperCase()}() {
                |    return this.$it;
                |}""".trimMargin() }
            .joinToString(separator = "\n")

    private fun Method.pathArgumentsSetters() : String = this.pathArguments()
            .map { """
                |public void set${it.firstUpperCase()}(final String $it) {
                |    this.$it = $it;
                |}""".trimMargin() }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public List<String> get${it.fieldName().firstUpperCase()}() {
                |    return this.getQueryParam("${it.name}");
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName().toScalarType()
            else -> type.toVrapType().simpleName().toScalarType()
        }
    }

    private fun QueryParameter.witherBoxedType() : String {
        val type = this.type
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName()
            else -> type.toVrapType().simpleName()
        }
    }

    private fun Method.queryParamsTemplateSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) != null }
            .map {
                val anno = it.getAnnotation("placeholderParam", true)
                val o = anno.value as ObjectInstance
                val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
                val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance

                val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
                val value = "String.format(\"" + template.value.replace("<" + placeholder.value + ">", "%s") + "\", " + placeholder.value + ")"
                val placeholderValue = StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value)

                val methodName = StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
                val parameters =  "final String $placeholderValue, final TValue ${paramName.value}"
                val listParameters =  "final String $placeholderValue, final Collection<TValue> ${paramName.value}"

                return """
                |/**
                | * set ${paramName.value} with the specificied value
                | * @param <TValue> value type
                | * @param $placeholderValue parameter name
                | * @param ${paramName.value} parameter value
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} with$methodName($parameters) {
                |    return copy().withQueryParam($value, ${paramName.value});
                |}
                |
                |/**
                | * add additional ${paramName.value} query parameter
                | * @param <TValue> value type
                | * @param $placeholderValue parameter name
                | * @param ${paramName.value} parameter value
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} add$methodName($parameters) {
                |    return copy().addQueryParam($value, ${paramName.value});
                |}
                |
                |/**
                | * set ${paramName.value} with the specificied values
                | * @param <TValue> value type
                | * @param $placeholderValue parameter name
                | * @param ${paramName.value} parameter values
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} with$methodName($listParameters) {
                |    final String placeholderName = String.format("var.%s", ${StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value)});
                |    return copy().withoutQueryParam(placeholderName).addQueryParams(${paramName.value}.stream().map(s -> new ParamEntry<>(placeholderName, s.toString())).collect(Collectors.toList()));
                |}
                |
                |/**
                | * add additional ${paramName.value} query parameters
                | * @param <TValue> value type
                | * @param $placeholderValue parameter name
                | * @param ${paramName.value} parameter values
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} add$methodName($listParameters) {
                |    final String placeholderName = String.format("var.%s", ${StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value)});
                |    return copy().addQueryParams(${paramName.value}.stream().map(s -> new ParamEntry<>(placeholderName, s.toString())).collect(Collectors.toList()));
                |}
            """.trimMargin().escapeAll()

            }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |/**
                | * set ${it.fieldName()} with the specified value
                | * @param <TValue> value type
                | * @param ${it.fieldName()} value to be set
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} with${it.fieldName().firstUpperCase()}(final TValue ${it.fieldName()}) {
                |    return copy().withQueryParam("${it.name}", ${it.fieldName()});
                |}
                |
                |/**
                | * add additional ${it.fieldName()} query parameter
                | * @param <TValue> value type
                | * @param ${it.fieldName()} value to be added
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} add${it.fieldName().firstUpperCase()}(final TValue ${it.fieldName()}) {
                |    return copy().addQueryParam("${it.name}", ${it.fieldName()});
                |}
                |
                |/**
                | * set ${it.fieldName()} with the specified value
                | * @param supplier supplier for the value to be set
                | * @return ${this.toStringRequestName()}
                | */
                |public ${this.toStringRequestName()} with${it.fieldName().firstUpperCase()}(final Supplier<${it.witherBoxedType()}> supplier) {
                |    return copy().withQueryParam("${it.name}", supplier.get());
                |}
                |
                |/**
                | * add additional ${it.fieldName()} query parameter
                | * @param supplier supplier for the value to be added
                | * @return ${this.toStringRequestName()}
                | */
                |public ${this.toStringRequestName()} add${it.fieldName().firstUpperCase()}(final Supplier<${it.witherBoxedType()}> supplier) {
                |    return copy().addQueryParam("${it.name}", supplier.get());
                |}
                |
                |/**
                | * set ${it.fieldName()} with the specified value
                | * @param op builder for the value to be set
                | * @return ${this.toStringRequestName()}
                | */
                |public ${this.toStringRequestName()} with${it.fieldName().firstUpperCase()}(final Function<StringBuilder, StringBuilder> op) {
                |    return copy().withQueryParam("${it.name}", op.apply(new StringBuilder()));
                |}
                |
                |/**
                | * add additional ${it.fieldName()} query parameter
                | * @param op builder for the value to be added
                | * @return ${this.toStringRequestName()}
                | */
                |public ${this.toStringRequestName()} add${it.fieldName().firstUpperCase()}(final Function<StringBuilder, StringBuilder> op) {
                |    return copy().addQueryParam("${it.name}", op.apply(new StringBuilder()));
                |}
                |
                |/**
                | * set ${it.fieldName()} with the specified values
                | * @param <TValue> value type
                | * @param ${it.fieldName()} values to be set
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} with${it.fieldName().firstUpperCase()}(final Collection<TValue> ${it.fieldName()}) {
                |    return copy().withoutQueryParam("${it.name}").addQueryParams(${it.fieldName()}.stream().map(s -> new ParamEntry<>("${it.name}", s.toString())).collect(Collectors.toList())); 
                |}
                |
                |/**
                | * add additional ${it.fieldName()} query parameters
                | * @param <TValue> value type
                | * @param ${it.fieldName()} values to be added
                | * @return ${this.toStringRequestName()}
                | */
                |public <TValue> ${this.toStringRequestName()} add${it.fieldName().firstUpperCase()}(final Collection<TValue> ${it.fieldName()}) {
                |    return copy().addQueryParams(${it.fieldName()}.stream().map(s -> new ParamEntry<>("${it.name}", s.toString())).collect(Collectors.toList())); 
                |}
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Method.bodyMethods() : String =
        if(this.bodies != null && this.bodies.isNotEmpty() && this.bodies[0].type.toVrapType() is VrapObjectType) {
            val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
            """
                |public String getBody() {
                |    return ${methodBodyVrapType.simpleClassName.firstLowerCase()};
                |}
                |
                |public ${this.toStringRequestName()} withBody(String ${methodBodyVrapType.simpleClassName.firstLowerCase()}) {
                |    ${this.toStringRequestName()} t = copy();
                |    t.${methodBodyVrapType.simpleClassName.firstLowerCase()} = ${methodBodyVrapType.simpleClassName.firstLowerCase()};
                |    return t;
                |}
            """.trimMargin()
        } else ""

    private fun Method.imports(): String {
        return this.queryParameters
                .map {
                    it.type.toVrapType()
                }
                .filter { it !is VrapScalarType }
                .map {
                    getImportsForType(it)
                }
                .filter { !it.isNullOrBlank() }
                .map { "import ${it};" }
                .joinToString(separator = "\n")

    }
}
