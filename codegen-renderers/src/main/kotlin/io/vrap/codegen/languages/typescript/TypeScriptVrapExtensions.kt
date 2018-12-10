package io.vrap.codegen.languages.typescript

import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.types.*

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}[]"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}
