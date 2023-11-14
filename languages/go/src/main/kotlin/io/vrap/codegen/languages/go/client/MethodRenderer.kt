package io.vrap.codegen.languages.go.client
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.returnType
import io.vrap.codegen.languages.go.*
import io.vrap.codegen.languages.go.GoObjectTypeExtensions
import io.vrap.codegen.languages.go.goTypeName
import io.vrap.codegen.languages.go.simpleGoName
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.MethodRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.FileType

class GoMethodRenderer(
        override val vrapTypeProvider: VrapTypeProvider,
        @BasePackageName val basePackageName: String
) : MethodRenderer, GoObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {

        val filename = type.goClientFileName()
        return TemplateFile(
            relativePath = "$basePackageName/$filename.go",
            content = """|
                |package $basePackageName
                |
                |$goGeneratedComment
                |
                |<${type.importStatement()}>
                |
                |<${type.constructor()}>
                |<${type.renderFuncWithQueryParams()}>
                |<${type.renderFuncWithHeaders()}>
                |<${type.renderFuncExecute()}>
            """.trimMargin().keepIndentation()
        )
    }

    private fun Method.importStatement(): String {
        val modules = mutableListOf<String>(
            "context",
            "fmt",
            "io/ioutil",
            "net/http",
            "net/url"
        )

        val bodies = this.responses
            .filter { it.bodies.isNotEmpty() }
            .map { it.bodies[0].type.toVrapType() }

        if (bodies.isNotEmpty()) {
            modules.add("encoding/json")
        }

        if (queryParameters.any {
            val type = it.type.toVrapType()
            when (type) {
                is VrapArrayType -> when (type.itemType) {
                    GoBaseTypes.integerType -> true
                    GoBaseTypes.longType -> true
                    else -> false
                }
                GoBaseTypes.integerType -> true
                GoBaseTypes.longType -> true
                else -> false
            }
        }
        ) {
            modules.add("strconv")
        }

        if (this.bodyType() is FileType) {
            modules.add("io")
        }

        return modules
            .map { "    \"$it\"" }
            .joinToString(prefix = "import(\n", separator = "\n", postfix = "\n)")
    }

    protected fun Method.constructor(): String {
        val bodyVrapType = this.vrapType()

        val params = if (this.queryParameters.isNotEmpty()) "params *${toStructName()}Input" else ""

        return """
            |type ${toStructName()} struct {
            |   ${if (bodyVrapType != null) "body    ${this.vrapType()?.goTypeName()}" else ""}
            |   url    string
            |   client *Client
            |   headers http.Header
            |   <$params>
            |}
            |
            |func (r *${toStructName()}) Dump() map[string]interface{} {
            |    return map[string]interface{}{
            |        "url": r.url,
            |        ${if (this.queryParameters.isNotEmpty()) "\"params\": r.params," else ""}
            |    }
            |}
            |
            """.trimMargin()
    }

    private fun Method.renderQueryParamsFunc(): String {
        val structName = "${toStructName()}Input"
        return this.queryParameters.map {
            val funcName = it.paramName().exportName()
            val valType = if (it.isPatternProperty()) "map[string]${it.type.renderTypeExpr()}" else it.type.renderTypeExpr()

            val ref = if (it.isPatternProperty() || it.required || it.type is ArrayType) "" else "&"
            """
            |func (rb *${toStructName()}) $funcName(v $valType) *${this.toStructName()} {
            |    if (rb.params == nil) { rb.params = &$structName{} }
            |    rb.params.${it.paramName().exportName()} = ${ref}v
            |    return rb
            |}
            |
            """
        }.joinToString(separator = "\n")
    }

    private fun Method.renderQueryParamInput(): String {
        val structName = "${toStructName()}Input"

        val fields = this.queryParameters.map {
            if (it.isPatternProperty())
                "${it.paramName().exportName()} map[string]${it.type.renderTypeExpr()}"
            else if (it.required || it.type is ArrayType)
                "${it.name.exportName()} ${it.type.renderTypeExpr()}"
            else
                "${it.name.exportName()} *${it.type.renderTypeExpr()}"
        }.joinToString(separator = "\n")

        val setters = this.queryParameters.map {

            if (it.isPatternProperty()) {
                val name = it.paramName().exportName()
                // TODO: We could validate the key against the regex
                """
                |for k, v := range input.$name {
                |    for _, x := range v {
                |        values.Set(k, x)
                |    }
                |}
                """.trimMargin()
            } else {
                val name = it.name.exportName()
                val addStatement = {
                    key: String, input: String, type: VrapType ->
                    when (type) {
                        GoBaseTypes.integerType -> """values.Add("$key", strconv.Itoa($input))"""
                        GoBaseTypes.longType -> """values.Add("$key", strconv.Itoa($input))"""
                        GoBaseTypes.doubleType -> """values.Add("$key", fmt.Sprintf("%f", $input))"""
                        GoBaseTypes.booleanType -> {
                            """
                            |if ($input) {
                            |    values.Add("$key", "true")
                            |} else {
                            |    values.Add("$key", "false")
                            |}
                            """.trimMargin()
                        }
                        else -> """values.Add("$key", fmt.Sprintf("%v", $input))"""
                    }
                }

                val vrapType = it.type.toVrapType()
                if (vrapType is VrapArrayType) {
                    """
                    |for _, v := range input.$name {
                    |   ${addStatement(it.name, "v", vrapType.itemType)}
                    |}
                    """.trimMargin()
                } else if (it.required) {
                    addStatement(it.name, "input.$name", vrapType)
                } else {
                    """
                    |if (input.$name != nil) {
                    |    ${addStatement(it.name, "*input.$name", vrapType)}
                    |}
                    """.trimMargin()
                }
            }
        }.joinToString(separator = "\n")

        return """
        |type $structName struct {
        |    <$fields>
        |}
        |
        |func (input *$structName) Values() url.Values {
        |    values := url.Values{}
        |    <$setters>
        |    return values
        |}
        |
        """.trimMargin()
    }

    private fun Method.renderFuncWithQueryParams(): String {
        if (this.queryParameters.isEmpty())
            return ""

        return """
        |${this.renderQueryParamInput()}
        |
        |${this.renderQueryParamsFunc()}
        |
        |func (rb *${toStructName()}) WithQueryParams(input ${toStructName()}Input) *${this.toStructName()} {
        |    rb.params = &input
        |    return rb
        |}
        """.trimMargin()
    }

    private fun Method.renderFuncWithHeaders(): String {
        return """
        |func (rb *${toStructName()}) WithHeaders(headers http.Header) *${this.toStructName()} {
        |    rb.headers = headers
        |    return rb
        |}
        """.trimMargin()
    }

    private fun Method.renderFuncExecute(): String {
        val methodReturn = if (this.returnType().toVrapType().goTypeName() != "nil")
            "(result *${this.returnType().toVrapType().goTypeName()}, err error)"
        else
            "error"

        var bodyExpr = ""
        val bodyVrapType = this.vrapType()
        if (this.methodName.lowercase() != "get") {
            if (bodyVrapType is VrapScalarType && bodyVrapType.scalarType == "io.Reader") {
                bodyExpr = "data := rb.body"
            } else if (bodyVrapType is VrapObjectType) {
                bodyExpr = """
                |data, err := serializeInput(rb.body)
                |if err != nil {
                |    return ${if (hasReturnValue()) "nil, " else ""}err
                |}
                """.trimMargin()
            }
        }

        var bodyArg = ""
        if (this.methodName.lowercase() != "get" && this.methodName.lowercase() != "head") {
            bodyArg = if (bodyExpr != "") "data," else "nil,"
        }

        val paramsExpr = if (this.queryParameters.isNotEmpty())
            """
            |    var queryParams url.Values
            |    if (rb.params != nil) {
            |        queryParams = rb.params.Values()
            |    } else {
            |        queryParams = url.Values{}
            |    }
            """.trimMargin()
        else "queryParams := url.Values{}"

        return """
        |<${this.toBlockComment().escapeAll()}>
        |func (rb *${this.toStructName()}) Execute(ctx context.Context) $methodReturn {
        |    <$bodyExpr>
        |    <$paramsExpr>
        |    resp, err := rb.client.${this.methodName.lowercase()}(
        |        ctx,
        |        rb.url,
        |        queryParams,
        |        rb.headers,
        |        <$bodyArg>
        |    )
        |    <${this.responseHandler()}>
        |}
        """.trimMargin()
    }

    fun Method.hasReturnValue(): Boolean {
        return this.returnType().toVrapType().goTypeName() != "nil"
    }

    fun Method.responseHandler(): String {
        val returnValue = if (this.hasReturnValue()) "nil, " else ""
        val switchStatements = this.responses
            .map {
                val statusCode = it.statusCode
                if (it.bodies.isNotEmpty()) {
                    val vrap = it.bodies[0].type.toVrapType()
                    vrap.simpleGoName() to statusCode
                } else {
                    "nil" to statusCode
                }
            }
            .map {
                val isSuccess = it.second.toInt() in (200..399)

                if (it.first == "nil") {
                    if (isSuccess) {
                        """
                        |case ${it.second.toInt()}:
                        |    return ${returnValue}nil
                        """.trimMargin()
                    } else {
                        ""
                    }
                } else {
                    if (isSuccess) {
                        """
                        |case ${it.second.toInt()}:
                        |    err = json.Unmarshal(content, &result)
                        |    if (err != nil) {
                        |        return nil, err
                        |    }
                        |    return result, nil
                        """.trimMargin()
                    } else if (it.second.toInt() == 404) {
                        """
                        |case ${it.second.toInt()}:
                        |    return nil, ErrNotFound
                        """.trimMargin()
                    } else {
                        """
                        |case ${it.second.toInt()}:
                        |    errorObj := ${it.first}{}
                        |    err = json.Unmarshal(content, &errorObj)
                        |    if (err != nil) {
                        |        return ${returnValue}err
                        |    }
                        |    return ${returnValue}errorObj
                        """.trimMargin()
                    }
                }
            }.joinToString("\n")

        return """
        |if (err != nil) {
        |    return ${returnValue}err
        |}
    	|content, err := ioutil.ReadAll(resp.Body)
        |if err != nil {
        |    return ${returnValue}err
        |}
	    |defer resp.Body.Close()
        |switch resp.StatusCode {
        |    <${switchStatements.trimEnd()}>
        |    default:
        |        result := GenericRequestError{
        |            StatusCode: resp.StatusCode,
        |            Content:    content,
        |            Response:   resp,
        |        }
        |        return ${returnValue}result
        |}
        """
    }

    fun Method.vrapType(): VrapType? {
        val bodyType = this.bodyType()
        if (bodyType != null) {
            return bodyType.toVrapType()
        }
        return null
    }
}
