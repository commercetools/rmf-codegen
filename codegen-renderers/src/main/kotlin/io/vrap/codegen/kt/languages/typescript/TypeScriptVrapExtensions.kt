package io.vrap.codegen.kt.languages.typescript

import io.vrap.codegen.kt.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.kt.types.VrapArrayType
import io.vrap.rmf.codegen.kt.types.VrapNilType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapType

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}[]"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}