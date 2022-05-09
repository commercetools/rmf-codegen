package io.vrap.codegen.languages.go

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.DescriptionFacet
import java.util.*

fun DescriptionFacet.toBlockComment(): String {
    val description = this.description
    return if (description?.value.isNullOrBlank()) {
        ""
    } else description.value
        .lines()
        .joinToString(prefix = "/**\n*\t", postfix = "\n*/", separator = "\n*\t")
}

fun DescriptionFacet.toLineComment(): String {
    val description = this.description
    return if (description?.value.isNullOrBlank()) {
        ""
    } else description.value
        .lines()
        .joinToString(separator = "\n")
        .trimMargin()
        .prependIndent("// ")
}
