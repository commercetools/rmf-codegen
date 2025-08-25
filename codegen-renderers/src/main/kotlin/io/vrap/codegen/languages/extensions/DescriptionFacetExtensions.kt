package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.types.AnnotationsFacet
import io.vrap.rmf.raml.model.types.DescriptionFacet
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.StringInstance
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document.OutputSettings
import org.jsoup.nodes.Entities
import org.jsoup.safety.Safelist
import java.nio.charset.StandardCharsets

private val HTML_RENDERER = HtmlRenderer.builder().build()
private val PARSER = Parser.builder().build()
private val outputSettings = OutputSettings()
    .escapeMode(Entities.EscapeMode.extended)
    .charset(StandardCharsets.UTF_8)

/**
 * This extension method converts the description from markdown to html and wraps the result in doc comment.
 *
 * @return the description of this as a doc comment
 */
fun DescriptionFacet.toComment(empty: String = ""): String {
    val htmlString = this.toHtml()
    return if(htmlString.isNullOrBlank()){
        empty
    }else{
        htmlString.filterLinks().let { it.lines().map { " *  $it" }.joinToString(separator = "\n") }
    }
}

fun DescriptionFacet.toHtml() = this
        .description
        ?.value
        ?.let(PARSER::parse)
        ?.let(HTML_RENDERER::render)
        ?.trim()

fun String.filterLinks(): String {
    val doc = Jsoup.parse(this)

    val links = doc.select("a[href]")

    for( link in links ) {
        if (link.attr("href").startsWith("ctp:")) {
            link.attr("href", "https://docs.commercetools.com/apis/${link.attr("href")}")
        } else {
            link.tagName("span")
        }
    }

    return Jsoup
        .clean(doc.html(), "", Safelist.basic(), outputSettings)
        .replace("€", "&euro;")
}

/**
 * This method extracts the javadoc comment from the "enumDescriptions" annotation - which is of object type.
 * Each key corresponds to an enum value and the value contains the markdown description of the enum.
 * <pre>
 * ResourceType:
 *    (enumDescriptions):
 *       channel: The channel resource type.
 *    type: string
 *    enum:
 *     - channel
 *     - cart-discount
 * </pre>
 */
fun StringInstance.toComment(): String? {
    val enumValues = (eContainer() as AnnotationsFacet).getAnnotation("enumDescriptions")
    if (enumValues?.value is ObjectInstance) {
        val description = (enumValues.value as ObjectInstance).getValue(value)
        return if (description is StringInstance) {
            description.value?.let(PARSER::parse)?.let(HTML_RENDERER::render)?.let { "/**\n${it.lines().map { '\t' + it }.joinToString(separator = "\n")}\n*/" }?.filterLinks()
        } else {
            null
        }
    }
    return null
}
