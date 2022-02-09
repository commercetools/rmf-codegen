package io.vrap.codegen.languages.csharp.model

import com.google.common.collect.Lists
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.*


class CsharpModelInterfaceRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @BasePackageName private val basePackagePrefix: String) : CsharpObjectTypeExtensions, EObjectExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val extends = Lists.newArrayList(type.type?.toVrapType()?.simpleName())
            .plus(
                when (val ex = type.getAnnotation("csharp-extends") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            ).filterNotNull()

        var content : String = """
            |${type.usings()}
            |
            |namespace ${vrapType.csharpPackage()}
            |{
            |    <${type.DeserializationAttributes()}>
            |    public partial interface I${vrapType.simpleClassName} ${if (extends.isNotEmpty()) { ": ${extends.joinToString(separator = ", ")}" } else ""}
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
            .filterNot { property -> property.isPatternProperty() }
            .map { it.toCsharpProperty(this) }.joinToString(separator = "\n\n")

    private fun Property.toCsharpProperty(objectType: ObjectType): String {
        val propName = this.name.firstUpperCase()
        val typeName = this.type.toVrapType().simpleName()
        var overrideProp = this.shouldOverrideThisProperty(objectType)

        var nullableChar = if(!this.required && this.type.isNullableScalar()) "?" else ""
        var newKeyword = if(overrideProp) "new " else ""
        var deprecationAttr = if(this.deprecationAnnotation() == "") "" else this.deprecationAnnotation()+"\n";

        return """
            |${deprecationAttr}${newKeyword}${typeName}$nullableChar $propName { get; set;}
            """.trimMargin()
    }

    //override if it's already exists in the parent type
    private fun Property.shouldOverrideThisProperty(objectType: ObjectType): Boolean  {
        var hasParent = objectType.type != null;
        if(hasParent)
        {
            var parent = objectType.type as ObjectType;
            if(parent.properties.any { it.name.equals(this.name) })
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
            |    <${this.DeserializationAttributes()}>
            |    public interface I${vrapType.simpleClassName} : IDictionary\<string, ${property.type.toVrapType().simpleName()}\>
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
            |[TypeDiscriminator(nameof(${this.discriminator.firstUpperCase()}))]
            |[DefaultTypeDiscriminator(typeof(${vrapType.csharpPackage()}.${this.objectClassName()}))]
            |<${this.subTypes.plus(this.subTypes.flatMap { it.subTypes }).distinctBy { it.name }
                    .asSequence()
                    .filterIsInstance<ObjectType>()
                    .filter { it.discriminatorValue != null }
                    .sortedBy { anyType -> anyType.discriminatorValue.lowercase(Locale.getDefault()) }
                    .map {
                        val vrapObjectType = vrapTypeProvider.doSwitch(it) as VrapObjectType
                        "[SubTypeDiscriminator(\"${it.discriminatorValue}\", typeof(${vrapObjectType.`package`.toCsharpPackage()}.${it.objectClassName()}))]"
                    }
                    .joinToString(separator = "\n")}>
            """.trimMargin()
        else
            """
            |[DeserializeAs(typeof(${vrapType.`package`.toCsharpPackage()}.${this.objectClassName()}))]
            """.trimMargin()
    }

}
