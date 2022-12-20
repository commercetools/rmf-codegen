package com.commercetools.rmf.diff

import com.damnhandy.uri.template.Expression
import com.damnhandy.uri.template.UriTemplate
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.CaseFormat
import com.google.common.collect.Lists
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.*
import java.util.stream.Collectors

interface FormatPrinter {
    fun print(diffResult: List<Diff<Any>>): String
}

class CliFormatPrinter: FormatPrinter {
    override fun print(diffResult: List<Diff<Any>>): String {
        return diffResult.joinToString("\n") { "${it.message} (${it.source})" }
    }
}

class MarkdownFormatPrinter: FormatPrinter {
    override fun print(diffResult: List<Diff<Any>>): String {

        val map = diffResult.groupBy { it.scope }.map { it.key to it.value.groupBy { it.diffType } }.toMap()

        return map.entries.joinToString("\n\n") { scope -> """
                |${scope.value.entries.joinToString("\n\n") { type -> """
                |<details>
                |<summary>${type.key.type.firstUpperCase()} ${scope.key.scope.firstUpperCase()}(s)</summary>
                |
                |${type.value.joinToString("\n") { "- ${it.severity.asSign()}${it.message} (${it.source?.location}:${it.source?.position?.line}:${it.source?.position?.charPositionInLine})" }}
                |</details>
                |
                """.trimMargin() }}
            """.trimMargin() }
    }

    fun CheckSeverity.asSign(): String {
        return when(this) {
            CheckSeverity.FATAL -> ":red_circle: "
            CheckSeverity.ERROR -> ":warning: "
            CheckSeverity.WARN -> ":warning: "
            else -> ""

        }
    }
}

class JsonFormatPrinter: FormatPrinter {
    override fun print(diffResult: List<Diff<Any>>): String {
        val mapper = ObjectMapper()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(diffResult)
    }
}

sealed class LanguageMarkdownFormatPrinter(val separator: String = ".", val prefix: String = "apiRoot"): FormatPrinter {
    private fun replaceMessage(diff: Diff<Any>, separator: String): Diff<Any> {
        return when (diff.eObject) {
            is Method -> if (diff.scope == Scope.METHOD && (diff.diffType == DiffType.REMOVED || diff.diffType == DiffType.ADDED)) {
                val message = "${diff.diffType.toString().lowercase()} ${diff.scope.toString().lowercase()} `${requestChain(
                    (diff.eObject as Method).resource(), diff.eObject as Method, separator, prefix)}`"
                Diff(
                    diff.diffType,
                    diff.scope,
                    diff.value,
                    message,
                    diff.eObject,
                    diff.severity,
                    diff.diffEObject,
                    diff.source
                )
            } else
                diff
            else -> diff
        }
    }

    override fun print(diffResult: List<Diff<Any>>): String {

        val map = diffResult.map { replaceMessage(it, separator) }.groupBy { it.scope }.map { it.key to it.value.groupBy { it.diffType } }.toMap()


        return map.entries.joinToString("\n\n") { scope -> """
                |${scope.value.entries.joinToString("\n\n") { type -> """
                    |<details>
                    |<summary>${type.key.type.firstUpperCase()} ${scope.key.scope.firstUpperCase()}(s)</summary>
                    |
                |${type.value.joinToString("\n") { "- ${it.severity.asSign()}${it.message}" }}
                |</details>
                        |
                        """.trimMargin() }}
                    """.trimMargin() }
    }

    fun CheckSeverity.asSign(): String {
        return when(this) {
            CheckSeverity.FATAL -> ":red_circle: "
            CheckSeverity.ERROR -> ":warning: "
            CheckSeverity.WARN -> ":warning: "
            else -> ""

        }
    }
}

class JavaMarkdownFormatPrinter: LanguageMarkdownFormatPrinter()

class PHPMarkdownFormatPrinter: LanguageMarkdownFormatPrinter("->", "${"$"}apiRoot")

class TSMarkdownFormatPrinter: LanguageMarkdownFormatPrinter()

class DotNetMarkdownFormatPrinter: LanguageMarkdownFormatPrinter()

fun requestChain(resource: Resource, method: Method, separator: String = ".", prefix: String = "apiRoot"): String {
    val builderChain = resource.resourcePathList().map { r -> "${r.getMethodName()}()" }
        .plus("${method.method}()")
    return builderChain.joinToString(separator, prefix = "$prefix$separator")
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

fun Resource.getMethodName(): String {
    val annotation = this.getAnnotation("methodName")
    if (annotation != null) {
        return (annotation.getValue() as StringInstance).value
    }
    val parts = this.relativeUri.components
        .filter { uriTemplatePart -> uriTemplatePart is Expression }
        .map { uriTemplatePart -> uriTemplatePart as Expression }
        .toList()
    if (parts.isNotEmpty()) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.relativeUri.toParamName("With", "Value"))
    }
    val uri = this.relativeUri.template
    return StringCaseFormat.LOWER_CAMEL_CASE.apply(uri.replaceFirst("/".toRegex(), "").replace("/", "_"))
}

fun UriTemplate.toParamName(delimiter: String, suffix: String): String {
    return this.components.stream().map { uriTemplatePart ->
        if (uriTemplatePart is Expression) {
            return@map uriTemplatePart.varSpecs.stream()
                .map { s -> delimiter + s.variableName.firstUpperCase() + suffix }.collect(Collectors.joining())
        }
        StringCaseFormat.UPPER_CAMEL_CASE.apply(uriTemplatePart.toString().replace("/", "-"))
    }.collect(Collectors.joining()).replace("[^\\p{L}\\p{Nd}]+".toRegex(), "").firstUpperCase()
}

fun String.firstUpperCase(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
