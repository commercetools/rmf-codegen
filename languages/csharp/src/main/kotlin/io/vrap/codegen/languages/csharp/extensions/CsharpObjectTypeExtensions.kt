package io.vrap.codegen.languages.csharp.extensions

import io.vrap.codegen.languages.extensions.*
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.ObjectType

const val ANNOTATION_ABSTRACT = "abstract"
const val NAMESPACE_SUFFIX : String = "Domain"

interface CsharpObjectTypeExtensions : ExtensionsBase {


    fun ObjectType.getUsings(): List<String> {
        var usingsList =  this.properties
                .map { it.type }
                //If the subtypes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .plus(discriminatorProperty()?.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .map { getUsingsForType(it) }
                .filterNotNull()
                .sortedBy { it }
                .distinct()
                .toList()
        if(this.hasSubtypes()) {
            usingsList=usingsList.plusElement("Newtonsoft.Json")
            usingsList=usingsList.plusElement("JsonSubTypes")
        }
        //TODO: Get the package name of Attribute Type Dynamic
        if(this.isTypeHaveAttributeProperty()) {
            usingsList=usingsList.plusElement("Attribute = commercetools.Sdk.models.Product$NAMESPACE_SUFFIX.Attribute")
        }
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
                    |using System.Linq;"""
    }

    fun ObjectType.isAbstract() : Boolean = this.annotations.find { it.type.name == ANNOTATION_ABSTRACT } != null

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
        is VrapObjectType -> return if (!vrapType.`package`.isNullOrEmpty()) vrapType.`package`+NAMESPACE_SUFFIX else {
            null
        }
        is VrapArrayType -> getUsingsForType(vrapType.itemType)
        is VrapEnumType -> "${vrapType.`package`+NAMESPACE_SUFFIX}"
        else -> null

    }
}
