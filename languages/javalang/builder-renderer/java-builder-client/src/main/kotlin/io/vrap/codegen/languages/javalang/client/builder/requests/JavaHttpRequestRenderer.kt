package io.vrap.codegen.languages.javalang.client.builder.requests

import com.google.common.collect.Lists
import com.google.common.net.MediaType
import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

/**
 * Query parameters with this annotation should be ignored by JVM sdk.
 */
const val PLACEHOLDER_PARAM_ANNOTATION = "placeholderParam"

class JavaHttpRequestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, JavaObjectTypeExtensions, JavaEObjectTypeExtensions {

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
                        type.`is`.map { "${it.trait.className()}<${type.toRequestName()}>".escapeAll() }
                )
                .filterNotNull()

        val content = """
            |package ${vrapType.`package`.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.utils.Utils;
            |
            |import java.io.InputStream;
            |import java.io.IOException;
            |
            |import java.net.URI;
            |import java.nio.charset.StandardCharsets;
            |import java.nio.file.Files;
            |
            |import java.time.Duration;
            |import java.util.ArrayList;
            |import java.util.List;
            |import java.util.Map;
            |import java.util.HashMap;
            |import java.util.stream.Collectors;
            |import java.util.concurrent.CompletableFuture;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |import javax.annotation.Nullable;
            |
            |import java.io.UnsupportedEncodingException;
            |import java.net.URLEncoder;
            |import io.vrap.rmf.base.client.*;
            |${type.imports()}
            |
            |import static io.vrap.rmf.base.client.utils.ClientUtils.blockingWait;
            |
            |<${type.toComment().escapeAll()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |public class ${type.toRequestName()} extends ApiMethod\<${type.toRequestName()}, ${type.javaReturnType(vrapTypeProvider)}\>${if (implements.isNotEmpty()) " implements ${implements.joinToString(", ")}" else ""} {
            |
            |    <${type.fields()}>
            |
            |    <${type.constructor()}>
            |
            |    <${type.copyConstructor()}>
            |
            |    <${type.createRequestMethod()}>
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
            |    <${type.formParamMethods()}>
            |
            |    @Override
            |    protected ${type.toRequestName()} copy()
            |    {
            |        return new ${type.toRequestName()}(this);
            |    }
            |}
        """.trimMargin()
                .keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${type.toRequestName()}".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun Trait.className(): String {
        val vrapType = vrapTypeProvider.doSwitch(this as EObject) as VrapObjectType

        return "${vrapType.`package`.toJavaPackage()}.${vrapType.simpleClassName}"
    }

    private fun Method.constructor(): String? {
        val constructorArguments = mutableListOf("final ApiHttpClient apiHttpClient")
        val constructorAssignments = mutableListOf("super(apiHttpClient);")

        this.pathArguments().map { "String $it" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.$it = $it;" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyArgument =
                    "${methodBodyVrapType.`package`.toJavaPackage()}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()}"
                constructorArguments.add(methodBodyArgument)
                val methodBodyAssignment =
                    "this.${methodBodyVrapType.simpleClassName.decapitalize()} = ${methodBodyVrapType.simpleClassName.decapitalize()};"
                constructorAssignments.add(methodBodyAssignment)
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)){
                constructorArguments.add("List<ParamEntry<String, String>> formParams".escapeAll())
                constructorAssignments[0] = "super(apiHttpClient, new ApiHttpHeaders(new ApiHttpHeaders.StringHeaderEntry(ApiHttpHeaders.CONTENT_TYPE, \"application/x-www-form-urlencoded\")), new ArrayList<>());".escapeAll()
                constructorAssignments.add("this.formParams = formParams;")
            }else {
                constructorArguments.add("Object obj")
                constructorAssignments.add("this.obj = obj;")
            }
        }

        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation().escapeAll()
    }

    private fun Method.copyConstructor(): String? {
        val constructorAssignments = mutableListOf("super(t);")

        this.pathArguments().map { "this.$it = t.$it;" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.decapitalize()} = t.${methodBodyVrapType.simpleClassName.decapitalize()};"
                constructorAssignments.add(methodBodyAssignment)
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)){
                constructorAssignments.add("this.formParams = new ArrayList<>(t.formParams);".escapeAll())
            } else {
                constructorAssignments.add("this.obj = t.obj;")
            }
        }

        return """
            |public ${this.toRequestName()}(${this.toRequestName()} t) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation().escapeAll()
    }

    private fun Method.fields(): String? {

        val pathArgs = this.pathArguments().map { "private String $it;" }.joinToString(separator = "\n")

        val body: String = if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                "private ${methodBodyVrapType.`package`.toJavaPackage()}.${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.decapitalize()};"
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
                "private List<ParamEntry<String, String>> formParams;".escapeAll()
            } else {
                "private Object obj;"
            }
        }else{
            ""
        }

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

    private fun Method.createRequestMethod() : String {

        val pathArguments = this.pathArguments().map { "{$it}" }
        var stringFormat = this.resource().fullUri.template
        pathArguments.forEach { stringFormat = stringFormat.replace(it, "%s") }
        val stringFormatArgs = pathArguments
                .map { it.replace("{", "").replace("}", "") }
                .map { "this.$it" }
                .joinToString(separator = ", ")

        val bodyName : String? = if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                methodBodyVrapType.simpleClassName.decapitalize()
            }else {
                "obj"
            }
        }else {
            null
        }

        val requestPathGeneration : String = """
            |List<String> params = new ArrayList<>(getQueryParamUriStrings());
            |String httpRequestPath = String.format("$stringFormat", $stringFormatArgs);
            |if(!params.isEmpty()){
            |    httpRequestPath += "?" + String.join("&", params);
            |}
        """.trimMargin().escapeAll()
        val bodySetter: String = if(bodyName != null){
            if(this.bodies[0].type.isFile())
                """
                |try {
                |    return new ApiHttpRequest(ApiHttpMethod.${this.method.name}, URI.create(httpRequestPath), getHeaders(), Files.readAllBytes(file.toPath()));
                |} catch (Exception e) {
                |    e.printStackTrace();
                |}
                |""".trimMargin()
            else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA))
                """
                |
                |try {
                |    return new ApiHttpRequest(ApiHttpMethod.${this.method.name}, URI.create(httpRequestPath), getHeaders(), getFormParamUriString().getBytes(StandardCharsets.UTF_8));
                |} catch (Exception e) {
                |    e.printStackTrace();
                |}
                """.trimMargin()
            else
                """
                |try {
                |    final byte[] body = apiHttpClient().getSerializerService().toJsonByteArray($bodyName);
                |    return new ApiHttpRequest(ApiHttpMethod.${this.method.name}, URI.create(httpRequestPath), getHeaders(), body);
                |} catch(Exception e) {
                |    e.printStackTrace();
                |}
                |""".trimMargin()
        } else ""

        return """
            |@Override
            |public ApiHttpRequest createHttpRequest() {
            |    <$requestPathGeneration>
            |    $bodySetter
            |    return new ApiHttpRequest(ApiHttpMethod.${this.method.name}, URI.create(httpRequestPath), getHeaders(), null);
            |}
        """.trimMargin()
    }

    private fun Method.executeBlockingMethod() : String {
        return """
            |@Override
            |public ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\> executeBlocking(Duration timeout){
            |    return blockingWait(execute(), timeout);
            |}
        """.trimMargin()
    }

    private fun Method.executeMethod() : String {
        return """
            |@Override
            |public CompletableFuture\<ApiHttpResponse\<${this.javaReturnType(vrapTypeProvider)}\>\> execute(){
            |    return apiHttpClient().execute(this.createHttpRequest(), ${this.javaReturnType(vrapTypeProvider)}.class);
            |}
        """.trimMargin()
    }

    private fun Method.pathArgumentsGetters() : String = this.pathArguments()
            .map { "public String get${it.capitalize()}() {return this.$it;}" }
            .joinToString(separator = "\n")

    private fun Method.pathArgumentsSetters() : String = this.pathArguments()
            .map { "public void set${it.capitalize()}(final String $it) { this.$it = $it; }" }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public List<String> get${it.fieldName().capitalize()}() {
                |    return this.getQueryParam("${it.name}");
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type;
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

                val methodName = StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
                val parameters =  "final String " + StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value) + ", final ${it.witherType()} " + paramName.value

                return """
                |public ${this.toRequestName()} with$methodName($parameters){
                |    return copy().withQueryParam($value, ${paramName.value});
                |}
                |
                |public ${this.toRequestName()} add$methodName($parameters){
                |    return copy().addQueryParam($value, ${paramName.value});
                |}

            """.trimMargin().escapeAll()

            }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public ${this.toRequestName()} with${it.fieldName().capitalize()}(final ${it.witherType()} ${it.fieldName()}){
                |    return copy().withQueryParam("${it.name}", ${it.fieldName()});
                |}
                |
                |public ${this.toRequestName()} add${it.fieldName().capitalize()}(final ${it.witherType()} ${it.fieldName()}){
                |    return copy().addQueryParam("${it.name}", ${it.fieldName()});
                |}
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Method.formParamMethods() : String =
        if (this.bodies != null && !this.bodies.isEmpty() && this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
            """
                /**
                 * add an additional form parameter
                 * @param key form parameter name
                 * @param value form parameter value
                 * @param <V> value type
                 * @return T
                 */
                public <V> ByProjectKeyProductProjectionsSearchPost addFormParam(final String key, final V value) {
                    ByProjectKeyProductProjectionsSearchPost c = copy();
                    c.formParams.add(new ParamEntry<>(key, value.toString()));
                    return c;
                }
            
                /**
                 * set the form parameter with the specified value
                 * @param key form parameter name
                 * @param value form parameter value
                 * @param <V> value type
                 * @return T
                 */
                public <V> ByProjectKeyProductProjectionsSearchPost withFormParam(final String key, final V value) {
                    return withoutFormParam(key).addFormParam(key, value);
                }
            
                /**
                 * removes the specified form parameter
                 * @param key form parameter name
                 * @return T
                 */
                public ByProjectKeyProductProjectionsSearchPost withoutFormParam(final String key) {
                    ByProjectKeyProductProjectionsSearchPost c = copy();
                    c.formParams = c.formParams.stream()
                            .filter(e -> !e.getKey().equalsIgnoreCase(key))
                            .collect(Collectors.toList());
                    return c;
                }
            
                /**
                 * set the form parameters
                 * @param formParams list of form parameters
                 * @return T
                 */
                public ByProjectKeyProductProjectionsSearchPost withFormParams(final List<ParamEntry<String, String>> formParams) {
                    ByProjectKeyProductProjectionsSearchPost c = copy();
                    c.formParams = formParams;
                    return c;
                }
            
                public List<ParamEntry<String, String>> getFormParams() {
                    return new ArrayList<>(this.formParams);
                }
            
                public List<String> getFormParam(final String key) {
                    return this.formParams.stream().filter(e -> e.getKey().equals(key)).map(ParamEntry::getValue).collect(Collectors.toList());
                }
                
                public List<String> getFormParamUriStrings() {
                    return this.formParams.stream().map(ParamEntry::toUriString).collect(Collectors.toList());
                }

                public String getFormParamUriString() {
                    return this.formParams.stream().map(ParamEntry::toUriString).collect(Collectors.joining("&"));
                }

                @Nullable
                public String getFirstFormParam(final String key) {
                    return this.formParams.stream()
                            .filter(e -> e.getKey().equals(key))
                            .map(Map.Entry::getValue)
                            .findFirst()
                            .orElse(null);
                }
            """.trimIndent().escapeAll()
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
