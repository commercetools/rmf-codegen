package io.vrap.codegen.kt.languages.java

import io.vrap.rmf.codegen.kt.rendring.CoreCodeGenerator

object JavaSubTemplates {

        val generatedAnnotation = """
        |@Generated(
        |    value = "${CoreCodeGenerator::class.java.canonicalName}",
        |    comments = "https:/github.comvrapiormf-codegen"
        |)
    """.trimMargin()




}
