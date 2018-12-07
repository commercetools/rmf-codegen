package io.vrap.codegen.languages.java.extensions

import io.vrap.rmf.codegen.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet

fun DescriptionFacet.toComment() = this.toHtml()?.let {"/**\n${it.lines().map { '\t'+it }.joinToString(separator = "\n")}\n*/"}?:""