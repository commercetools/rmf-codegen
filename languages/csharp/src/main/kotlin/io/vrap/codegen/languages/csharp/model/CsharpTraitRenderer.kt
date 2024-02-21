package io.vrap.codegen.languages.csharp.model

import io.vrap.codegen.languages.csharp.CsharpBaseTypes
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.csharp.requests.PLACEHOLDER_PARAM_ANNOTATION
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.di.BasePackageName
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

class CsharpTraitRenderer  constructor(override val vrapTypeProvider: VrapTypeProvider, @BasePackageName private val basePackagePrefix: String) : CsharpObjectTypeExtensions, EObjectExtensions, TraitRenderer {

    override fun render(type: Trait): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType
        val cPackage = vrapType.`package`.toCsharpPackage().replace("Clients", "Client");
        val content= """
            |using System.Collections.Generic;
            |
            |// ReSharper disable CheckNamespace
            |namespace ${cPackage}
            |{
            |    public interface I${vrapType.simpleClassName}\<T\> where T: I${vrapType.simpleClassName}\<T\> {
            |        <${type.queryParamsGetters()}>
            |
            |        <${type.queryParamsSetters(vrapType.simpleClassName)}>
            |
            |        <${type.queryParamsTemplateSetters(vrapType.simpleClassName)}>
            |    
            |        I${vrapType.simpleClassName}\<T\> As${vrapType.simpleClassName.firstUpperCase()}() {
            |            return this;
            |        }
            |
            |        T As${vrapType.simpleClassName}ToBaseType() {
            |            return (T)this;
            |        }
            |    }
            |}
        """.trimMargin().keepIndentation()

        val relativePath = vrapType.csharpClassRelativePath(true).replace("Clients","Client").replace(basePackagePrefix.replace(".", "/"), "").trimStart('/')

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun Trait.queryParamsGetters() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |List<string> Get${it.fieldName().firstUpperCase()}();
                """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun Trait.queryParamsSetters(simpleClassName: String) : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |/**
                | * set ${it.fieldName()} with the specificied value
                | */
                |T With${it.fieldName().firstUpperCase()}(${it.witherType()} ${it.fieldName()});
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
                val parameters =  "string " + StringCaseFormat.LOWER_CAMEL_CASE.apply(placeholder.value) + ", ${it.witherType()} " + paramName.value

                return """
                |/**
                | * set ${paramName.value} with the specificied value
                | */
                |T With$methodName($parameters);
            """.trimMargin().escapeAll()

            }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type;
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName()
            else -> {
                val vrapType = type.toVrapType().simpleName()
                if (vrapType == CsharpBaseTypes.integerType.simpleName())
                    CsharpBaseTypes.longType.simpleName()
                else vrapType
            }
        }
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }
}
