package io.vrap.codegen.languages.extensions

import com.damnhandy.uri.template.Expression
import com.google.common.base.CaseFormat
import io.vrap.codegen.languages.php.extensions.toParamName
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.StringInstance

fun Resource.getMethodName(): String {
    val annotation = this.getAnnotation("methodName")
    if (annotation != null) {
        return (annotation!!.getValue() as StringInstance).value
    }
    val parts = this.relativeUri.components
            .filter{ uriTemplatePart -> uriTemplatePart is Expression }
            .map{ uriTemplatePart -> uriTemplatePart as Expression }
            .toList()
    if (parts.isNotEmpty()) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.relativeUri.toParamName("With", "Value"))
    }
    val uri = this.relativeUri.template
    return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, uri.replaceFirst("/".toRegex(), ""))
}