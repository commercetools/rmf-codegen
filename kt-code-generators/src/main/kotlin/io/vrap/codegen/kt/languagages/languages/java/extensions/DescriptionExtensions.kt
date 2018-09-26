package io.vrap.codegen.kt.languagages.languages.java.extensions

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.raml.model.types.DescriptionFacet

fun DescriptionFacet.toJavaComment() = this.toHtml()?.let {"/**\n\t$it\n*/"}?:""