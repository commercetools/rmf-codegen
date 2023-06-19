package io.vrap.codegen.languages.csharp.requests

import com.google.common.collect.Lists
import com.google.common.net.MediaType
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.MethodRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.resources.impl.ResourceImpl
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.util.*

const val PLACEHOLDER_PARAM_ANNOTATION = "placeholderParam"

class CsharpHttpRequestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, private val basePackagePrefix: String) : MethodRenderer, CsharpObjectTypeExtensions, EObjectExtensions {

    override fun render(type: Method): TemplateFile {


        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType
        var entityFolder = (type.resource() as ResourceImpl).GetNameAsPlural()
        val cPackage = vrapType.requestBuildersPackage(entityFolder)

        val implements = Lists.newArrayList<String>()
            .plus(
                when (val ex = type.getAnnotation("csharp-implements") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            )
            .plus(
                type.`is`.distinctBy { it.trait.name }.map { "${it.trait.className()}<${type.toRequestName()}>".escapeAll() }
            )
            .filterNotNull()
        val hasReturnPayload = type.hasReturnPayload();

        val content = """
            |using System;
            |using System.IO;
            |using System.Collections.Generic;
            |using System.Linq;
            |using System.Net;
            |using System.Net.Http;
            |using System.Net.Http.Headers;
            |using System.Text;
            |using System.Threading.Tasks;
            |using System.Threading;
            |using System.Text.Json;
            |using commercetools.Base.Client;
            |using commercetools.Base.Serialization;
            |${type.usings()}
            |
            |// ReSharper disable CheckNamespace
            |namespace ${cPackage}
            |{
            |   ${if (type.markDeprecated()) "[Obsolete(\"usage of this endpoint has been deprecated.\", false)]" else ""}
            |   public partial class ${type.toRequestName()} : ApiMethod\<${type.toRequestName()}\>, IApiMethod\<${type.toRequestName()}, ${if (hasReturnPayload) type.csharpReturnType(vrapTypeProvider) else "string"}\>${if (implements.isNotEmpty()) ", ${implements.joinToString(", ")}" else ""} {
            |
            |       <${type.properties()}>
            |   
            |       <${type.constructor()}>
            |   
            |       <${type.queryParamsGetters()}>
            |   
            |       <${type.queryParamsSetters()}>
            |       
            |       <${type.queryParamsTemplateSetters()}>
            |
            |       <${type.executeAndBuild()}>
            |
            |       <${type.formParamMethods()}>
            |   }
            |}
        """.trimMargin()
                .keepIndentation()

        val relativePath = cPackage
                .replace(basePackagePrefix, "").replace(".", "/")
                .trimStart('/').trimEnd('/')

        return TemplateFile(
                relativePath = "${relativePath}/${type.toRequestName()}.cs",
                content = content
        )
    }

    private fun Trait.className(): String {
        val vrapType = vrapTypeProvider.doSwitch(this as EObject) as VrapObjectType

        return "${vrapType.`package`.toCsharpPackage().replace("Clients", "Client")}.I${vrapType.simpleClassName}"
    }

    private fun Method.properties(): String? {

        var props = "private IClient ApiHttpClient { get; }"+ "\n\n" + """public override HttpMethod Method =\>\ HttpMethod.${methodName.firstUpperCase()};"""
        //only for post methods
        if (this.methodName.lowercase(Locale.getDefault()) == "post") {
            props = "private ISerializerService SerializerService { get; }\n\n$props";
        }
        val pathArgs = props + "\n\n" + this.pathArguments().map { "private string ${it.firstUpperCase()} { get; }" }.joinToString(separator = "\n\n")

        val body: String = if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType){
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                if(methodBodyVrapType.`package`=="")
                    "private ${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstUpperCase()};"
                else
                    "private ${methodBodyVrapType.`package`.toCsharpPackage()}.I${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstUpperCase()};"
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
                "private List<KeyValuePair<string, string>> _formParams;".escapeAll()
            }else {
                "private JsonElement? jsonNode;"
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

    private fun Method.constructor(): String? {
        val pathArguments = this.pathArguments().map { "{$it}" }
        var requestUrl = this.resource().fullUri.template
        pathArguments.forEach { requestUrl = requestUrl.replace(it, "{"+it.replace("{","").replace("}","").firstUpperCase()+"}") }

        val constructorArguments = mutableListOf("IClient apiHttpClient")
        val constructorAssignments = mutableListOf("this.ApiHttpClient = apiHttpClient;")
        //only for post methods
        if (this.methodName.lowercase(Locale.getDefault()) == "post") {
            constructorArguments.add("ISerializerService serializerService")
            constructorAssignments.add("this.SerializerService = serializerService;")
        }

        this.pathArguments().map { "string ${it.lowerCamelCase()}" }.forEach { constructorArguments.add(it) }
        this.pathArguments().map { "this.${it.firstUpperCase()} = ${it.lowerCamelCase()};" }.forEach { constructorAssignments.add(it) }

        if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                val methodBodyArgument: String
                if(methodBodyVrapType.`package`=="")
                    methodBodyArgument = "${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstLowerCase()}"
                else
                    methodBodyArgument = "${methodBodyVrapType.`package`.toCsharpPackage()}.I${methodBodyVrapType.simpleClassName} ${methodBodyVrapType.simpleClassName.firstLowerCase()}"
                constructorArguments.add(methodBodyArgument)
                val methodBodyAssignment = "this.${methodBodyVrapType.simpleClassName.firstUpperCase()} = ${methodBodyVrapType.simpleClassName.firstLowerCase()};"
                constructorAssignments.add(methodBodyAssignment)
            } else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)){
                constructorArguments.add("List<KeyValuePair<string, string>> formParams = null".escapeAll())
                constructorAssignments.add("this._formParams = formParams ?? new List<KeyValuePair<string, string>>();".escapeAll())
            }else {
                constructorArguments.add("JsonElement? jsonNode")
                constructorAssignments.add("this.jsonNode = jsonNode;")
            }
        }

        constructorAssignments.add("this.RequestUrl = $\"${requestUrl}\";")

        return """
            |public ${this.toRequestName()}(${constructorArguments.joinToString(separator = ", ")}) {
            |    <${constructorAssignments.joinToString(separator = "\n")}>
            |}
        """.trimMargin().keepIndentation().escapeAll()

    }

    private fun Method.pathArguments() : List<String> {
        return this.resource().fullUri.variables.toList()
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }

    private fun QueryParameter.fieldNameAsString(type: String): String {
        var fieldName = this.fieldName()
        if(this.type.toVrapType() is VrapEnumType)
            return "$fieldName.JsonName"
        if(type == "string")
            return fieldName
        else
            return "$fieldName.ToString()"
    }

    private fun Method.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public List<string> Get${it.fieldName().firstUpperCase()}() {
                |    return this.GetQueryParam("${it.name}");
                |}
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Method.queryParamsSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |public ${this.toRequestName()} With${it.fieldName().firstUpperCase()}(${it.witherType()} ${it.fieldName()}){
                |    return this.AddQueryParam("${it.name}", ${it.fieldNameAsString(it.witherType())});
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

    private fun Method.executeAndBuild() : String {
        val hasReturnPayload = this.hasReturnPayload();
        val executeBlock =
                """
            |public async Task\<${if (hasReturnPayload) this.csharpReturnType(vrapTypeProvider) else "string"}\> ExecuteAsync(CancellationToken cancellationToken = default)
            |{
            |   ${if (this.hasReturnPayload()) """
            |   var requestMessage = Build();
            |   return await ApiHttpClient.ExecuteAsync\<${this.csharpReturnType(vrapTypeProvider)}\>(requestMessage, cancellationToken);
            |   """ else """
            |   return await ExecuteAsJsonAsync(cancellationToken);
            |   """}
            |}
            |
            |public async Task\<string\> ExecuteAsJsonAsync(CancellationToken cancellationToken = default)
            |{
            |   var requestMessage = Build();
            |   return await ApiHttpClient.ExecuteAsJsonAsync(requestMessage, cancellationToken);
            |}
            |
            |public async Task\<IApiResponse\<${if (hasReturnPayload) this.csharpReturnType(vrapTypeProvider) else "string"}\>\> SendAsync(CancellationToken cancellationToken = default)
            |{
            |   ${if (this.hasReturnPayload()) """
            |   var requestMessage = Build();
            |   return await ApiHttpClient.SendAsync\<${this.csharpReturnType(vrapTypeProvider)}\>(requestMessage, cancellationToken);
            |   """ else """
            |   return await SendAsJsonAsync(cancellationToken);
            |   """}
            |}
            |
            |public async Task\<IApiResponse\<string\>\> SendAsJsonAsync(CancellationToken cancellationToken = default)
            |{
            |   var requestMessage = Build();
            |   return await ApiHttpClient.SendAsJsonAsync(requestMessage, cancellationToken);
            |}
        """.trimMargin()

        var bodyBlock = "";
        val bodyName : String? = if(this.bodies != null && this.bodies.isNotEmpty()){
            if(this.bodies[0].type.toVrapType() is VrapObjectType) {
                val methodBodyVrapType = this.bodies[0].type.toVrapType() as VrapObjectType
                methodBodyVrapType.simpleClassName.firstUpperCase()
            } else {
                "jsonNode"
            }
        }else {
            null
        }
        //only for post methods
        if(this.methodName.lowercase(Locale.getDefault()) == "post" && bodyName != null)
        {
            if(this.bodies[0].type.isFile())
            {
                bodyBlock = """
                    |
                    |public override HttpRequestMessage Build()
                    |{
                    |   var request = base.Build();
                    |   if ($bodyName != null && $bodyName.Length \> 0)
                    |   {
                    |       request.Content = new StreamContent($bodyName);
                    |       if (Headers.HasHeader(ApiHttpHeaders.CONTENT_TYPE))
                    |       {
                    |           request.Content.Headers.ContentType =
                    |               new MediaTypeHeaderValue(Headers.GetFirst(ApiHttpHeaders.CONTENT_TYPE));
                    |       }
                    |   }
                    |   return request;
                    |}
                """.trimMargin()
            }
            else if (this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA))
                bodyBlock = """
                    |
                    |public override HttpRequestMessage Build()
                    |{
                    |   var request = base.Build();
                    |
                    |   request.Content = new FormUrlEncodedContent(_formParams);
                    |   return request;
                    |}
                """.trimMargin()
            else
            {
                bodyBlock = """
                    |
                    |public override HttpRequestMessage Build()
                    |{
                    |   var request = base.Build();
                    |   if (SerializerService != null)
                    |   {
                    |       var body = this.SerializerService.Serialize(${bodyName});
                    |       if(!string.IsNullOrEmpty(body))
                    |       {
                    |           request.Content = new StringContent(body, Encoding.UTF8, "application/json");
                    |       }
                    |   }
                    |   return request;
                    |}
                """.trimMargin()
            }
        }
        return executeBlock + bodyBlock
    }

    private fun Method.formParamMethods() : String =
        if (this.bodies != null && !this.bodies.isEmpty() && this.bodies[0].contentMediaType.`is`(MediaType.FORM_DATA)) {
            """
                public ${this.toRequestName()} AddFormParam<TValue>(string key, TValue value) {
                    this._formParams.Add(new KeyValuePair<string, string>(key, value.ToString()));
                    return this;
                }
            
                public ${this.toRequestName()} WithFormParam<TValue>(string key, TValue value) {
                    return WithoutFormParam(key).AddFormParam(key, value);
                }
            
                /**
                 * removes the specified form parameter
                 * @param key form parameter name
                 * @return T
                 */
                public ${this.toRequestName()} WithoutFormParam(string key) {
                    this._formParams = this._formParams.FindAll(pair => !pair.Key.Equals(key, StringComparison.InvariantCultureIgnoreCase));
                    return this;
                }
            
                /**
                 * set the form parameters
                 * @param formParams list of form parameters
                 * @return T
                 */
                public ${this.toRequestName()} WithFormParams(List<KeyValuePair<string, string>> formParams) {
                    this._formParams = formParams;
                    return this;
                }
            
                public List<KeyValuePair<string, string>> GetFormParams() {
                    return this._formParams.ToList();
                }
            
                public List<string> GetFormParam(string key) {
                    return this._formParams.FindAll(pair => pair.Key.Equals(key, StringComparison.InvariantCultureIgnoreCase)).Select(pair => pair.Value).ToList();
                }
                
                public List<string> GetFormParamUriStrings() {
                    return this._formParams.Select(ToUriString).ToList();
                }

                public string GetFormParamUriString() {
                    return string.Join('&', this._formParams.Select(ToUriString));
                }
                
                private static string ToUriString(KeyValuePair<string, string> entry) {
                    return entry.Key + "=" + WebUtility.UrlEncode(entry.Value);
                }

                #nullable enable
                public string? GetFirstFormParam(string key)
                {
                    return this._formParams
                        .FirstOrDefault(pair => pair.Key.Equals(key, StringComparison.InvariantCultureIgnoreCase)).Value;
                }
                #nullable disable
            """.trimIndent().escapeAll()
        } else ""

    private fun Method.usings(): String {
        return this.queryParameters
                .map {
                    it.type.toVrapType()
                }
                .filter { it !is VrapScalarType }
                .map {
                    getUsingsForType(it)
                }
                .filter { !it.isNullOrBlank() }
                .map { "using ${it};" }
                .joinToString(separator = "\n")

    }

    private fun Method.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun Method.queryParamsTemplateSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) != null }
            .map {
                val anno = it.getAnnotation("placeholderParam", true)
                val o = anno.value as ObjectInstance
                val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
                val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance

                val template = o.value.stream().filter { propertyValue -> propertyValue.name == "template" }.findFirst().orElse(null).value as StringInstance
                val value = "$\"" + template.value.replace("<" + placeholder.value + ">", "{"+ placeholder.value + "}") + "\""

                val methodName = StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
                val parameters =  "string " + StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value) + ", ${it.witherType()} " + paramName.value

                return """
                |public ${this.toRequestName()} With$methodName($parameters){
                |    return this.AddQueryParam($value, ${paramName.value});
                |}
            """.trimMargin().escapeAll()

            }
            .joinToString(separator = "\n\n")
}
