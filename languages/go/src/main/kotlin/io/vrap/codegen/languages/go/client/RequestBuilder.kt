package io.vrap.codegen.languages.go.client

import io.vrap.codegen.languages.go.*
import io.vrap.codegen.languages.go.GoObjectTypeExtensions
import io.vrap.codegen.languages.go.exportName
import io.vrap.codegen.languages.go.goName
import io.vrap.codegen.languages.go.goTypeName
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ResourceRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.FileType

class RequestBuilder constructor(
    private val clientConstants: ClientConstants,
    val api: Api,
    override val vrapTypeProvider: VrapTypeProvider,
    @BasePackageName val basePackageName: String
) : ResourceRenderer, GoObjectTypeExtensions {

    override fun render(type: Resource): TemplateFile {

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
                |<${type.subResources(type.toRequestBuilderName().exportName())}>
                |<${type.methods()}>
            """.trimMargin().keepIndentation()
        )
    }

    protected fun Resource.constructor(): String {
        val pathArgs = if (!this.fullUri.variables.isEmpty()) {
            this
                .fullUri
                .variables
                .map { it.goName() }
                .map { "$it   string" }
                .joinToString(separator = "\n")
        } else { "" }

        return """
            |type ${toStructName()} struct {
            |   <$pathArgs>
            |   client *Client
            |}
            """.trimMargin()
    }

    private fun Resource.importStatement(): String {
        val modules = mutableListOf<String>()

        if (this.methods.size > 0) {
            modules.add("fmt")
        }

        if (this.methods.any { it.bodyType() is FileType }) {
            modules.add("io")
        }

        return modules
            .map { "    \"$it\"" }
            .joinToString(prefix = "import(\n", separator = "\n", postfix = "\n)")
    }

    protected fun Resource.methods(): String {
        return this.methods.map { renderMethod(it) }.joinToString(separator = "\n\n")
    }
    /*
     * Render the method func
     *
     * For example:
     *   func (rb *ByProjectKeyProductsRequestBuilder) Get() *ByProjectKeyProductsRequestMethodGet {
     *        return &ByProjectKeyProductsRequestMethodGet{
     *            url:    fmt.Sprintf("/%s/products", rb.projectKey),
     *            client: rb.client,
     *        }
     *    }
     */
    private fun Resource.renderMethod(method: Method): String {
        val bodyVrapType = method.vrapType()
        val methodKwargs = listOf<String>()
            .plus(
                {
                    if (bodyVrapType != null && bodyVrapType !is VrapNilType) "body ${method.vrapType()?.goTypeName()}" else ""
                }()
            )
            .filter {
                it != ""
            }
            .joinToString(separator = ", ")

        // val assignments =
        //     this.relativeUri.variables
        //         .map { it.goName() }
        //         .map { "$it: rb.$it," }
        //         .plus(
        //             (this.fullUri.variables.asList() - this.relativeUri.variables.asList())
        //                 .map { it.goName() }
        //                 .map { "$it: rb.$it," }
        //         )
        //         .joinToString(separator = "\n")

        val endpoint = transformUriTemplate(this.fullUri.template)
        return """
        |<${method.toBlockComment().escapeAll()}>
        |func (rb *${this.toStructName()}) ${method.methodName.exportName()}(<$methodKwargs>) *${method.toStructName()} {
        |    return &${method.toStructName()}{
        |        <${if (bodyVrapType != null) "body: body," else ""}>
        |        url: $endpoint,
        |        client: rb.client,
        |    }
        |}
        """.trimMargin()
    }

    fun transformUriTemplate(template: String): String {
        val regex = "\\{([^}]+)}".toRegex()
        val matches = regex.findAll(template)

        var pattern = template
        val args = mutableListOf<String>()
        matches.map { it.groupValues[1] }.forEach {
            pattern = pattern.replace("{$it}", "%s")
            args.add("rb.${it.goName()}")
        }
        return "fmt.Sprintf(\"${pattern}\", ${args.joinToString(", ")})"
    }

    fun Method.vrapType(): VrapType? {
        val bodyType = this.bodyType()
        if (bodyType != null) {
            return bodyType.toVrapType()
        }
        return null
    }
}
