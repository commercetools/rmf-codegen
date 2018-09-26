package io.vrap.rmf.codegen.kt.languages.java.extensions

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.codegen.kt.rendring.escapeAll
import io.vrap.rmf.raml.model.types.DescriptionFacet

fun DescriptionFacet.toJavaComment() = this.toHtml()?.let {"/**\n\t$it\n*/"}?:""