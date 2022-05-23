package io.vrap.codegen.languages.javalang.client.builder.model

import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.codegen.languages.javalang.client.builder.requests.PLACEHOLDER_PARAM_ANNOTATION
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.TraitRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectInstance
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

class JavaTraitRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, TraitRenderer {

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
            |public interface ${vrapType.simpleClassName}\<T extends ${vrapType.simpleClassName}\<T\>\> {
            |    <${type.queryParamsGetters()}>
            |
            |    <${type.queryParamsSetters(vrapType.simpleClassName)}>
            |
            |    <${type.queryParamsTemplateSetters(vrapType.simpleClassName)}>
            |    
            |    default ${vrapType.simpleClassName}\<T\> as${vrapType.simpleClassName.firstUpperCase()}() {
            |        return this;
            |    }
            |    
            |    @SuppressWarnings("unchecked")
            |    default T as${vrapType.simpleClassName}ToBaseType() {
            |        return (T)this;
            |    }
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
                |List<String> get${it.fieldName().firstUpperCase()}();
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Trait.queryParamsSetters(simpleClassName: String) : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |/**
                | * set ${it.fieldName()} with the specificied value
                | */
                |<TValue> ${simpleClassName}<T> with${it.fieldName().firstUpperCase()}(final TValue ${it.fieldName()});
                |
                |/**
                | * add additional ${it.fieldName()} query parameter
                | */
                |<TValue> ${simpleClassName}<T> add${it.fieldName().firstUpperCase()}(final TValue ${it.fieldName()});
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Trait.queryParamsTemplateSetters(simpleClassName: String) : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) != null }
            .map {
                val anno = it.getAnnotation("placeholderParam", true)
                val o = anno.value as ObjectInstance
                val paramName = o.value.stream().filter { propertyValue -> propertyValue.name == "paramName" }.findFirst().orElse(null).value as StringInstance
                val placeholder = o.value.stream().filter { propertyValue -> propertyValue.name == "placeholder" }.findFirst().orElse(null).value as StringInstance

                val methodName = StringCaseFormat.UPPER_CAMEL_CASE.apply(paramName.value)
                val parameters =  "final String " + StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value) + ", final TValue " + paramName.value

                return """
                |/**
                | * set ${paramName.value} with the specificied value
                | */
                |<TValue> ${simpleClassName}<T> with$methodName($parameters);
                |
                |/**
                | * add additional ${paramName.value} query parameter
                | */
                |<TValue> ${simpleClassName}<T> add$methodName($parameters);
            """.trimMargin().escapeAll()

            }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type;
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName().toScalarType()
            else -> type.toVrapType().simpleName().toScalarType()
        }
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }
}
