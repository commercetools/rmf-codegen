package io.vrap.codegen.kt.languages.typescript

import io.vrap.codegen.kt.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.kt.types.*

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}[]"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}
