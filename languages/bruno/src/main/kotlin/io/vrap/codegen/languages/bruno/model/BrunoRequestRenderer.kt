package io.vrap.codegen.languages.bruno.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.raml.model.resources.Method

object BrunoRequestRenderer {
    fun renderRequest(name: String, method: Method, url: BrunoUrl, body: String?, index: Int): String {
        val bodyStr = if (body != null) """
            |body:json {
            |  <<${body}>>
            |}    
            """.trimMargin().keepAngleIndent() else ""
        return """
            |meta {
            |  name: $name
            |  type: http
            |  seq: $index
            |}
            | 
            |${method.methodName} {
            |  url: ${url.raw()}
            |  body: ${if (body != null) "json" else "none"}
            |  auth: inherit
            |}
            | 
            |<<${bodyStr}>>
            |
            |query {
            |  <<${url.query()}>>
            |}
            |
            |script:post-response {
            |  <<${method.resource().testScript()}>>
            |}
            |
            |assert {
            |  res.status: in [200, 201]
            |}
        """.trimMargin().keepAngleIndent()
    }
}