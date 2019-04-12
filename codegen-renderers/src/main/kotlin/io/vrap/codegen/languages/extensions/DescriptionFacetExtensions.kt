package io.vrap.codegen.languages.extensions

import io.vrap.rmf.codegen.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet
import io.vrap.rmf.raml.model.types.StringInstance
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

private val HTML_RENDERER = HtmlRenderer.builder().build()
private val PARSER = Parser.builder().build()

/**
 * This extension method converts the description from markdown to html and wraps the result in doc comment.
 *
 * @return the description of this as a doc comment
 */
fun DescriptionFacet.toComment() = this.toHtml()?.let {"/**\n${it.lines().map { '\t'+it }.joinToString(separator = "\n")}\n*/"}?:""

/**
 * This method extracts the comment from the "doc" annotation - which value needs to be a {@link StringInstance} and
 * is useful to extract doc for enum values:
 * <pre>
 * ResourceType:
 *    type: string
 *    enum:
 *     - value: channel
 *       (generator.doc): The channel resource type.
 *     - cart-discount
 * </pre>
 */
fun StringInstance.toComment(): String? {
    val doc = getAnnotation("doc")?.value?.value
    return doc?.let { it as String }?.let(PARSER::parse)?.let(HTML_RENDERER::render)?.let { "/**\n${it.lines().map { '\t' + it }.joinToString(separator = "\n")}\n*/" }
}
