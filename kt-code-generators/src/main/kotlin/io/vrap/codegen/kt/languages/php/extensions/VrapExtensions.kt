package io.vrap.codegen.kt.languages.php.extensions

import io.vrap.rmf.codegen.kt.types.VrapArrayType
import io.vrap.rmf.codegen.kt.types.VrapNilType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapType

fun VrapType.namespaceName():String{
    return when(this){
        is VrapObjectType -> this.`package`.split(".").map { s -> s.capitalize() }.joinToString("\\\\")
        is VrapArrayType -> this.itemType.namespaceName()
        else -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.simpleName():String{
    return when(this){
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}Collection"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName():String{
    return when(this){
        is VrapObjectType -> "${this.`package`}\\\\${this.simpleClassName}"
        is VrapArrayType -> "${this.itemType.fullClassName()}Collection"
        is VrapNilType -> throw IllegalStateException("$this has no full class name.")
    }
}
