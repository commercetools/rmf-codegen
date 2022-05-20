package io.vrap.codegen.languages.extensions

import io.vrap.rmf.codegen.doc.toHtml
import io.vrap.rmf.raml.model.types.AnnotationsFacet
import io.vrap.rmf.raml.model.types.DescriptionFacet
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.StringInstance
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

private val HTML_RENDERER = HtmlRenderer.builder().build()
private val PARSER = Parser.builder().build()

/**
 * This extension method converts the description from markdown to html and wraps the result in doc comment.
 *
 * @return the description of this as a doc comment
 */
fun DescriptionFacet.toComment(): String {
    val htmlString = this.toHtml()
    return if(htmlString.isNullOrBlank()){
        ""
    }else{
        Jsoup.clean(htmlString, Safelist.basic().removeTags("a"))
        htmlString.let {"/**\n${it.lines().map { "*  $it" }.joinToString(separator = "\n")}\n*/"}
    }
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
            description.value?.let(PARSER::parse)?.let(HTML_RENDERER::render)?.let { "/**\n${it.lines().map { '\t' + it }.joinToString(separator = "\n")}\n*/" }
        } else {
            null
        }
    }
    return null
}
