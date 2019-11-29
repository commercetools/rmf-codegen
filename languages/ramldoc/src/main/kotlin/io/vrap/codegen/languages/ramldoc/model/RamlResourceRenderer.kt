package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.UriParameter
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.QueryParameter
import org.eclipse.emf.ecore.EObject

class RamlResourceRenderer @Inject constructor(val api: Api, val vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val content = """
            |#${type.toResourceName()}
            |(resourcePath): ${type.fullUri.template}
            |${if (type.fullUriParameters.size > 0) """uriParameters:
            |  <<${type.fullUriParameters.joinToString("\n") { renderUriParameter(it) }}>>""" else ""}
            |${type.methods.joinToString("\n") { renderMethod(it) }}
        """.trimMargin().keepIndentation("<<", ">>")
        val relativePath = "resources/" + type.toResourceName()+ ".raml"
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun renderMethod(method: Method): String {
        return """
            |${method.methodName}:
            |  ${if (method.queryParameters.size > 0) """queryParameters:
            |    <<${method.queryParameters.joinToString("\n") { renderQueryParameter(it) }}>>""" else ""}
        """.trimMargin()
    }

    private fun renderUriParameter(uriParameter: UriParameter): String {
        return """
            |${uriParameter.name}:
            |  <<${uriParameter.type.renderType()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }

    private fun renderQueryParameter(queryParameter: QueryParameter): String {
        return """
            |${queryParameter.name}:
            |  <<${queryParameter.type.renderType()}>>
        """.trimMargin().keepIndentation("<<", ">>")
    }
}

private fun AnyType.renderScalarType(): String {
    if (!this.isInlineType) {
        return "type: ${this.name}"
    }
    return this.renderEAttributes().plus("type: ${this.name}").joinToString("\n")
}

private fun AnyType.renderEAttributes(): List<String> {
    val eAttributes = this.eClass().eAllAttributes
    return eAttributes.filter { eAttribute -> eAttribute.name != "name" && this.eGet(eAttribute) != null}
            .map { eAttribute -> "${eAttribute.name}: ${this.eGet(eAttribute)}" }

}

private fun ArrayType.renderArrayType(): String {
    var t = this.renderEAttributes().plus("type: array").joinToString("\n")
    if (this.items != null) {
        t += """
                |items:
                |  <<${this.items.renderScalarType()}>>
            """
    }
    return t.trimMargin().keepIndentation("<<", ">>")
}

private fun AnyType.renderType(): String {
    when (this) {
        is ArrayType -> return this.renderArrayType()
        else ->
            return this.renderScalarType();
    }
}
