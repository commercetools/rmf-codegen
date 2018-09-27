package io.vrap.codegen.kt.languages.java.extensions

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet

fun DescriptionFacet.toJavaComment() = this.toHtml()?.let {"/**\n${it.lines().map { '\t'+it }.joinToString(separator = "\n")}\n*/"}?:""