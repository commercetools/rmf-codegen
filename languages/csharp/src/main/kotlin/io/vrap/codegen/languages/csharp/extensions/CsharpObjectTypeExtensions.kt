package io.vrap.codegen.languages.csharp.extensions

import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.BooleanInstance
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

const val ANNOTATION_ABSTRACT = "abstract"

interface CsharpObjectTypeExtensions : ExtensionsBase {


    fun ObjectType.getUsings(): List<String> {
        var usingsList =  this.allProperties
                .map { it.type }
                //If the subtypes are in the same package they should be imported
                //.plus(this.namedSubTypes())
                .plus(this.type)
                .plus(discriminatorProperty()?.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .map { getUsingsForType(it) }
                .filterNotNull()
                .sortedBy { it }
                .distinct()
                .toList()
        return usingsList
    }

    fun ObjectType.usings() : String {
        var usingsAsList = this.getUsings()
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        var packageName = vrapType.`package`
        usingsAsList = usingsAsList.dropLastWhile { it== packageName }
        var usings= usingsAsList.map { "using $it;" }.joinToString(separator = "\n")
        var commonUsings = this.getCommonUsings()

        var allusings = if(usings.isNotEmpty()) usings +"\n"+ commonUsings else usings+ commonUsings

        return allusings
    }

    fun ObjectType.getCommonUsings() : String {
        return   """using System;
                    |using System.Collections.Generic;
                    |using System.Linq;
                    |using System.Text.Json.Serialization;
                    |using commercetools.Base.CustomAttributes;
                    |"""
    }

    public fun ObjectType.objectClassName(): String {
        val simpleClassName = (vrapTypeProvider.doSwitch(this) as VrapObjectType).simpleClassName

        if (this.allProperties.firstOrNull { property -> property.name.firstUpperCase() == simpleClassName } != null) {
            return simpleClassName + "Model"
        }
        return simpleClassName
    }

    fun ObjectType.isAbstract() : Boolean = this.annotations.find { it.type.name == ANNOTATION_ABSTRACT } != null || !this.discriminator.isNullOrEmpty()

    //Check if the type has one property and it's a dictionary property
    fun ObjectType.isADictionaryType() : Boolean {
        return this.properties.count() == 1 && this.properties[0].isPatternProperty()
    }
    fun ObjectType.isTypeHaveAttributeProperty() : Boolean {
        return this.properties.any { p -> p.isAttributeProperty()}
    }
}

fun getUsingsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> return if (!vrapType.csharpPackage().isNullOrEmpty()) vrapType.csharpPackage() else {
            null
        }
        is VrapArrayType -> getUsingsForType(vrapType.itemType)
        is VrapEnumType -> "${vrapType.csharpPackage()}"
        else -> null

    }
}
fun Property.deprecationAnnotation(): String {
    val anno = this.getAnnotation("markDeprecated", true)
    if (anno != null && (anno.value as BooleanInstance).value == true) {
        return """
                |[ObsoleteAttribute("This property is obsolete", false)]""".trimMargin()
    }
    return "";
}
