package io.vrap.codegen.languages.php.extensions

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
                .filter { !it.isScalar() }
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
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
        is VrapObjectType -> "${vrapType.namespaceName()}\\${vrapType.simpleName()}"
        is VrapArrayType -> "${vrapType.namespaceName()}\\${vrapType.simpleName()}"
        else -> null

    }
}

fun ObjectType.hasSubtypes(): Boolean = this.discriminator?.isNotBlank()?:false && (this.subTypes?.isNotEmpty() ?: false)
