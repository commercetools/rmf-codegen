package io.vrap.codegen.languages.csharp.model

import com.google.inject.Inject
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.impl.ObjectTypeImpl


class CsharpModelInterfaceRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : CsharpObjectTypeExtensions, EObjectExtensions, ObjectTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix:String

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        var content : String = """
            |${type.usings()}
            |
            |namespace ${vrapType.csharpPackage()}
            |{
            |    <${type.DeserializationAttributes()}>
            |    public partial interface I${vrapType.simpleClassName} ${type.type?.toVrapType()?.simpleName()?.let { ": $it" } ?: ""}
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

        val relativePath = vrapType.csharpClassRelativePath(true).replace(basePackagePrefix.replace(".", "/"), "").trimStart('/')
        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun ObjectType.toProperties() : String = this.properties
            .map { it.toCsharpProperty(this) }.joinToString(separator = "\n\n")

    private fun Property.toCsharpProperty(objectType: ObjectType): String {
        val propName = this.name.capitalize()
        val typeName = this.type.toVrapType().simpleName()
        var overrideProp = this.shouldOverrideThisProperty(objectType)

        var nullableChar = if(!this.required && this.type.toVrapType().isValueType()) "?" else ""
        var newKeyword = if(overrideProp) "new " else ""
        return "${newKeyword}${typeName}$nullableChar $propName { get; set;}"
    }

    //override if it's already exists in the parent type
    private fun Property.shouldOverrideThisProperty(objectType: ObjectType): Boolean  {
        var hasParent = objectType.type != null;
        if(hasParent)
        {
            var parent = objectType.type as ObjectType;
            if(parent?.properties.any { it.name.equals(this.name) })
                return true
        }
        return false;
    }

    fun ObjectType.renderTypeAsADictionaryType() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType

        var property = this.properties[0]

        return  """
            |${this.usings()}
            |
            |namespace ${vrapType.csharpPackage()}
            |{
            |    public class I${vrapType.simpleClassName} : Dictionary\<string, ${property.type.toVrapType().simpleName()}\>
            |    {
            |    }
            |}
            |
        """
    }

    fun ObjectType.DeserializationAttributes(): String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType

        return if (hasSubtypes())
            """
            |[TypeDiscriminator(nameof(${this.discriminator.capitalize()}))]
            |<${this.subTypes
                    .filter { (it as ObjectType).discriminatorValue != null }
                    .map {
                        val vrapType = vrapTypeProvider.doSwitch(it) as VrapObjectType
                        "[SubTypeDiscriminator(\"${(it as ObjectType).discriminatorValue}\", typeof(${vrapType.`package`.toCsharpPackage()}.${vrapType.simpleClassName}))]"
                    }
                    .joinToString(separator = "\n")}>
            """.trimMargin()
        else
            """
            |[DeserializeAs(typeof(${vrapType.`package`.toCsharpPackage()}.${vrapType.simpleClassName}))]
            """.trimMargin()
    }

}
