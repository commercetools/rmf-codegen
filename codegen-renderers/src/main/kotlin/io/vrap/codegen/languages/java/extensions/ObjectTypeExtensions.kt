package io.vrap.codegen.languages.java.extensions

import io.vrap.codegen.languages.ExtensionsBase
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.ObjectType

interface  ObjectTypeExtensions : io.vrap.codegen.languages.ExtensionsBase {

    fun ObjectType.getImports(): List<String> {

        val result =  this.properties
                .map { it.type }
                //If the subtipes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .map { getImportsForType(it) }
                .filterNotNull()
                .sortedBy { it }
                .distinct()
                .toList()

        if(result.contains("")){
            println()
        }

        return result
    }
}


fun getImportsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> "${vrapType.`package`}.${vrapType.simpleClassName}"
        is VrapArrayType -> getImportsForType(vrapType.itemType)
        else -> null

    }
}
