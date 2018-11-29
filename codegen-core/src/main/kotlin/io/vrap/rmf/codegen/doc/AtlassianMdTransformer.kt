package io.vrap.rmf.codegen.doc

import io.vrap.rmf.raml.model.types.DescriptionFacet
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer


private val HTML_RENDERER = HtmlRenderer.builder().build()
private val PARSER = Parser.builder().build()

fun DescriptionFacet.toHtml() = this
        .description
        ?.value
        ?.let(PARSER::parse)
        ?.let(HTML_RENDERER::render)
        ?.trim()






