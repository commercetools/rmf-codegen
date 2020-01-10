package io.vrap.codegen.languages.php.model

import com.google.common.collect.Lists
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource

class DocsProducer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider) {

    override fun produceFiles(): List<TemplateFile> = listOf(
            requestBuilder(api)
    )

    private fun requestBuilder(api: Api): TemplateFile {
        val resources = api.allContainedResources.associate { it.toResourceName() to it }.toSortedMap()
        return TemplateFile(relativePath = "docs/RequestBuilder.md",
                content = """
                    |# RequestBuilder
                    |
                    |In order to be able to build request objects you can use the RequestBuilder. The following methods return a HTTP request instance of Guzzle [PSR-7](https://github.com/guzzle/psr7).
                    |
                    |```php
                    |use ${clientPackageName.toNamespaceName().escapeAll()}\\${rootResource()};
                    |
                    |$!root = new ${rootResource()}();
                    |```
                    |
                    |<<${resources.values.flatMap { resource -> resource.methods.map { method -> resourceInfo(resource, method) }}.joinToString("\n")}>>
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
    }

    private fun resourceInfo(resource: Resource, method: Method): String {
        val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}(${if (r.relativeUri.paramValues().isNotEmpty()) "\"${r.relativeUri.paramValues().joinToString("\", \"") }\"" else ""})" }
                .plus("${method.method}(${if (method.firstBody() != null) "null" else ""})")

        return """
            |## `${builderChain.joinToString("->")}`
            |
            |${method.description?.value}
            |
            |### Example
            |```php
            |$!builder =  new ${rootResource()}();
            |$!request = $!builder
            |                <<${builderChain.joinToString("\n->", "->")}>>;
            |```
        """.trimMargin()
    }

    fun Resource.resourcePathList(): List<Resource> {
        val path = Lists.newArrayList<Resource>()
        if (this.fullUri.template == "/") {
            return path
        }
        path.add(this)
        var t = this.eContainer()
        while (t is Resource) {
            val template = t.fullUri.template
            if (template != "/") {
                path.add(t)
            }
            t = t.eContainer()
        }
        return Lists.reverse(path)
    }
}
