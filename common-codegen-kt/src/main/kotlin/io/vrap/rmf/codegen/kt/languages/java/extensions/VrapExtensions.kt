package io.vrap.rmf.codegen.kt.languages.java.extensions

import io.vrap.rmf.codegen.kt.types.VrapArrayType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapType

fun VrapType.simpleName():String{
    return when(this){
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "List<${this.itemType.simpleName()}>"
        else -> throw IllegalStateException("$this has no simple class name.")
    }
}
