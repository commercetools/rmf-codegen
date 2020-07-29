package io.vrap.codegen.languages.csharp.model

import com.google.inject.Inject
import io.vrap.codegen.languages.csharp.extensions.CsharpObjectTypeExtensions
import io.vrap.codegen.languages.csharp.extensions.NAMESPACE_SUFFIX
import io.vrap.codegen.languages.csharp.extensions.isValueType
import io.vrap.codegen.languages.csharp.extensions.simpleName
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property


class CsharpModelClassRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : CsharpObjectTypeExtensions, EObjectExtensions, ObjectTypeRenderer {


    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        var content : String = """
            |${type.usings()}
            |
            |namespace ${vrapType.`package`}$NAMESPACE_SUFFIX
            |{
            |    <${type.subTypesAttributes()}>
            |    public${ if(type.isAbstract())" abstract" else ""} class ${vrapType.simpleClassName} ${type.type?.toVrapType()?.simpleName()?.let { ": $it" } ?: ""}
            |    {
            |        <${type.toProperties()}>
            |    }
            |}
            |
        """.trimMargin().keepIndentation()


        if(type.isADictionaryType())
        {
            content = type.renderTypeAsADictionaryType().trimMargin().keepIndentation()
        }

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".cs",
                content = content
        )
    }

    private fun ObjectType.toProperties() : String = this.properties
            .map { it.toCsharpProperty() }.joinToString(separator = "\n\n")

    private fun Property.toCsharpProperty(): String {

        var nullableChar = if(!this.required && this.type.toVrapType().isValueType()) "?" else ""
        return "public ${this.type.toVrapType().simpleName()}$nullableChar ${this.name.capitalize()} { get; set;}"
    }

    fun ObjectType.renderTypeAsADictionaryType() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType

        var property = this.properties[0]

        return  """
            |${this.usings()}
            |
            |namespace ${vrapType.`package`}$NAMESPACE_SUFFIX
            |{
            |    public class ${toVrapType().simpleName()} : Dictionary\<string, ${property.type.toVrapType().simpleName()}\>
            |    {
            |        <${this.renderConstructor()}>
            |    }
            |}
            |
        """
    }
    fun ObjectType.renderConstructor() : String {
        return   """public ${toVrapType().simpleName()}() : base()
            |{
            |}"""
        }
    fun ObjectType.subTypesAttributes(): String {
        return if (hasSubtypes())
            """
            |[JsonConverter(typeof(JsonSubtypes), "${this.discriminator}")]
            |<${namedSubTypes().map { "[JsonSubtypes.KnownSubType(typeof(${it.toVrapType().simpleName()}), \"${(it as ObjectType).discriminatorValue}\")]" }.joinToString(separator = "\n")}>
            """.trimMargin()
        else
            ""
    }
}
