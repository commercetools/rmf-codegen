package io.vrap.codegen.languages.java

import io.vrap.rmf.codegen.rendring.CoreCodeGenerator

object JavaSubTemplates {

        val generatedAnnotation = """
        |@Generated(
        |    value = "${CoreCodeGenerator::class.java.canonicalName}",
        |    comments = "https://github.com/vrapio/rmf-codegen"
        |)
    """.trimMargin()




}
