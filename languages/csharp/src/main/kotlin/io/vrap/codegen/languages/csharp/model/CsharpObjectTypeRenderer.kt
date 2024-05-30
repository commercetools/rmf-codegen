package io.vrap.codegen.languages.csharp.model

import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.BooleanInstance
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
            |    ${if (type.markDeprecated()) "[Obsolete(\"usage of this endpoint has been deprecated.\", false)]" else ""}
            |    public partial class ${type.objectClassName()} : I${vrapType.simpleClassName}
            |    {
            |        ${if(type.isADictionaryType()) "" else type.toProperties("        ")}
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
            |// ReSharper disable CheckNamespace
            |namespace ${vrapType.csharpPackage()}
            |{
            |    ${if (this.markDeprecated()) "[Obsolete(\"usage of this endpoint has been deprecated.\", false)]" else ""}
            |    public partial class ${vrapType.simpleClassName} : Dictionary\<string, ${property.type.toVrapType().simpleName()}\>, I${vrapType.simpleClassName}
            |    {
            |    }
            |}
            |
        """
    }

    private fun ObjectType.toProperties(indent: String = "") : String = this.allProperties
        .filterNot { it.deprecated() }
        .filterNot { property -> property.isPatternProperty() }
        .map { it.toCsharpProperty(this) }.joinToString(separator = "\n\n$indent")

    private fun Property.toCsharpProperty(objectType: ObjectType): String {
        val propName = this.name.firstUpperCase()
        val typeName = this.type.toVrapType().simpleName()

        val nullableChar = if(!this.required && this.type.isNullableScalar() && !this.parentRequired(objectType)) "?" else ""
        val deprecationAttr = if(this.deprecationAnnotation() == "") "" else this.deprecationAnnotation()+"\n";

        return """
            |${deprecationAttr}public ${typeName}$nullableChar $propName { get; set; }${if (this.type.toVrapType() is VrapArrayType) """
            |${deprecationAttr}public IEnumerable\<${(this.type.toVrapType() as VrapArrayType).itemType.simpleName()}\>$nullableChar ${propName}Enumerable { set =\> $propName = value$nullableChar.ToList(); }
            |""" else ""}
            """.trimMargin()
    }

    private fun Property.parentRequired(objectType: ObjectType): Boolean  {
        val hasParent = objectType.type != null
        if(hasParent)
        {
            val parent = objectType.type as ObjectType
            return parent.allProperties.find { it.name.equals(this.name) }?.parentRequired(parent) ?: false
        }
        return this.required
    }

    fun ObjectType.renderConstructor(className: String) : String {
        val isEmptyConstructor = this.getConstructorContentForDiscriminator() == "";
        return if(!isEmptyConstructor)
            """public ${className}()
                |{
                |    ${this.getConstructorContentForDiscriminator()}
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

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }
}
