package io.vrap.codegen.languages.javalang.client.builder.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.codegen.languages.javalang.client.builder.requests.PLACEHOLDER_PARAM_ANNOTATION
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.TraitRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

class JavaTraitRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, TraitRenderer {

    override fun render(type: Trait): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType


        val content= """
            |package ${vrapType.`package`.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.utils.Generated;
            |import java.util.List;
            |
            |<${type.toComment().escapeAll()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |public interface ${vrapType.simpleClassName}\<T\> {
            |    <${type.queryParamsGetters()}>
            |
            |    <${type.queryParamsSetters()}>
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun Trait.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |List<String> get${it.fieldName().capitalize()}();
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Trait.queryParamsSetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |T with${it.fieldName().capitalize()}(final ${it.witherType()} ${it.fieldName()});
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type;
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName()
            else -> type.toVrapType().simpleName()
        }
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }
}
