package io.vrap.codegen.kt.languagages.languages.java.extensions

import io.vrap.codegen.kt.languagages.languages.ExtensionsBase
import io.vrap.rmf.codegen.kt.types.*
import io.vrap.rmf.raml.model.types.ObjectType

interface  ObjectTypeExtensions : ExtensionsBase {

    fun ObjectType.getImports(): List<String> {

        val result =  this.properties
                .map { it.type }
                .plus(this.subTypes)
                .plus(this.type)
                .filterNotNull()
                .map { vrapTypeSwitch.doSwitch(it) }
                .filter { it !is VrapDefaultObjectType }
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

fun ObjectType.hasSubtypes(): Boolean = this.discriminator?.isNotBlank()?:false && (this.subTypes?.isNotEmpty() ?: false)
