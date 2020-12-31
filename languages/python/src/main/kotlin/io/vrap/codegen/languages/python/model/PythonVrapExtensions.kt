package io.vrap.codegen.languages.python.model

import io.vrap.rmf.codegen.types.*

fun VrapType.simpleTSName():String{
    return when(this){
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleTSName()}[]"
        is VrapNilType -> this.name
    }
}

