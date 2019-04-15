package io.vrap.codegen.languages.java.extensions

import io.vrap.codegen.languages.ExtensionsBase
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.ObjectType

interface JavaObjectTypeExtensions : ExtensionsBase {

    fun ObjectType.getImports(): List<String> = this.properties
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

}


fun getImportsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> "${vrapType.`package`}.${vrapType.simpleClassName}"
        is VrapArrayType -> getImportsForType(vrapType.itemType)
        is VrapEnumType -> "${vrapType.`package`}.${vrapType.simpleClassName}"
        else -> null

    }
}
