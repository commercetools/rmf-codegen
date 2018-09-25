package io.vrap.rmf.codegen.kt.languages.java.extensions

import io.vrap.rmf.codegen.kt.types.*
import io.vrap.rmf.raml.model.types.ObjectType


fun ObjectType.getImports(typeNameSwitch: TypeNameSwitch): List<String> {

    val result =  this.properties
            .map { it.type }
            .plus(this.subTypes)
            .plus(this.type)
            .filterNotNull()
            .map { typeNameSwitch.doSwitch(it) }
            .filter { it !is VrapDefaultObjectType }
            .map { getImportsForType(it) }
            .filterNotNull()
            .sortedBy { it }
            .distinct()
            .toList()

    if(result.contains("")){
        println()
    }

    return result;

}

fun getImportsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> "${vrapType.`package`}.${vrapType.simpleClassName}"
        is VrapArrayType -> getImportsForType(vrapType.itemType)
        else -> null

    }
}

fun hasSubtypes(objectType: ObjectType): Boolean = objectType.discriminator.isNotBlank() && objectType.subTypes?.isEmpty() ?: false
