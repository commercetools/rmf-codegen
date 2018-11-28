package io.vrap.codegen.kt.languages.php.extensions

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet

fun DescriptionFacet.toPhpComment() = this.toHtml()?.let { it.lines().map { "* "+it }.joinToString(separator = "\n") }?:"*"
