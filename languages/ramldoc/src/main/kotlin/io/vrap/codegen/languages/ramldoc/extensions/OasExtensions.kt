package io.vrap.codegen.languages.ramldoc.extensions

import com.damnhandy.uri.template.UriTemplate
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import io.swagger.v3.oas.models.servers.ServerVariable
import io.vrap.codegen.languages.extensions.toParamName
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent

fun UriTemplate.toResourceName(): String {
    return this.normalize().toParamName("By")
}

fun Map.Entry<String, ServerVariable>.renderUriParameter(): String {
    val examples = this.value.extensions?.get("x-annotation-examples")
    return """
            |${this.key.replace("ID", "id", ignoreCase = true)}:${if (this.value.enum.size > 0) """
            |  required: true
            |  enum:
            |    <<${this.value.enum.joinToString("\n") { "- ${it}"}}>>""" else ""}${if (examples != null) """
            |  examples:
            |    <<${examples.toYaml()}>>""" else ""}
        """.trimMargin().keepAngleIndent()
}

fun Any.toYaml(): String {
    val mapper = YAMLMapper()
    mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
    return mapper.writeValueAsString(this).trim()
}
