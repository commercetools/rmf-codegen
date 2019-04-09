package io.vrap.codegen.languages.extensions

import io.vrap.rmf.codegen.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet

/**
 * This extension method converts the description from markdown to html and wraps the result in doc comment.
 *
 * @return the description of this as a doc comment
 */
fun DescriptionFacet.toComment() = this.toHtml()?.let {"/**\n${it.lines().map { '\t'+it }.joinToString(separator = "\n")}\n*/"}?:""
