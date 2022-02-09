package io.vrap.codegen.languages.csharp.model

import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.impl.ObjectTypeImpl


class CsharpObjectTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @BasePackageName private val basePackagePrefix: String) : CsharpObjectTypeExtensions, EObjectExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        var content : String = """
            |${type.usings()}
            |
            |namespace ${vrapType.csharpPackage()}
            |{
            |    public partial class ${type.objectClassName()} : I${vrapType.simpleClassName}
            |    {
            |        <${if(type.isADictionaryType()) "" else type.toProperties()}>
            |        <${type.renderConstructor(vrapType.simpleClassName)}>
            |    }
            |}
            |
        """.trimMargin().keepIndentation()

        if(type.isADictionaryType())
        {
            content = type.renderTypeAsADictionaryType().trimMargin().keepIndentation()
        }

        val relativePath = vrapType.csharpClassRelativePath(false).replace(basePackagePrefix.replace(".", "/"), "").trimStart('/')

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    fun ObjectType.renderTypeAsADictionaryType() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType

        var property = this.properties[0]

        return  """
            |${this.usings()}
            |
            |namespace ${vrapType.csharpPackage()}
            |{
            |    public partial class ${vrapType.simpleClassName} : Dictionary\<string, ${property.type.toVrapType().simpleName()}\>, I${vrapType.simpleClassName}
            |    {
            |    }
            |}
            |
        """
    }

    private fun ObjectType.toProperties() : String = this.allProperties
        .filterNot { property -> property.isPatternProperty() }
        .map { it.toCsharpProperty() }.joinToString(separator = "\n\n")

    private fun Property.toCsharpProperty(): String {
        val propName = this.name.firstUpperCase()
        val typeName = this.type.toVrapType().simpleName()

        var nullableChar = if(!this.required && this.type.isNullableScalar()) "?" else ""
        var deprecationAttr = if(this.deprecationAnnotation() == "") "" else this.deprecationAnnotation()+"\n";

        return """
            |${deprecationAttr}public ${typeName}$nullableChar $propName { get; set;}
            """.trimMargin()
    }

    fun ObjectType.renderConstructor(className: String) : String {
        var isEmptyConstructor = this.getConstructorContentForDiscriminator() == "";
        return if(!isEmptyConstructor)
            """public ${className}()
                |{ 
                |   ${this.getConstructorContentForDiscriminator()}
                |}"""
        else
            ""
        }
    fun ObjectType.getConstructorContentForDiscriminator(): String {
        val content: String
        if (this.discriminator() != null && this.discriminatorValue != null)
        {
            val enumName : String = this.allProperties.filter { it.name == this.discriminator() }.get(0).type.toVrapType().simpleName()
            if(enumName == "string") {
                content = "this.${(this.type as ObjectTypeImpl).discriminator().firstUpperCase()} = \"${this.discriminatorValue}\";"
            }
            else
            {
                content = "this.${(this.type as ObjectTypeImpl).discriminator().firstUpperCase()} = $enumName.FindEnum(\"${this.discriminatorValue}\");"
            }

        }
        else {
            content = ""
        }
        return content
    }
}
