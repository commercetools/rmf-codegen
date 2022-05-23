package io.vrap.codegen.languages.java.base

import io.vrap.rmf.codegen.rendering.CoreCodeGenerator

object JavaSubTemplates {

    val generatedAnnotation = """
        |@Generated(
        |    value = "${CoreCodeGenerator::class.java.canonicalName}",
        |    comments = "https://github.com/commercetools/rmf-codegen"
        |)
    """.trimMargin()

}
