package io.vrap.codegen.languages.typescript

import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}[]"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}