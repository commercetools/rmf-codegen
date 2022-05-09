package io.vrap.codegen.languages.go

import io.vrap.rmf.codegen.types.*

fun VrapType.goTypeName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName.exportName()
        is VrapObjectType -> this.simpleClassName.exportName()
        is VrapArrayType -> "[]${this.itemType.goTypeName()}"
        is VrapNilType -> "nil"
    }
}

fun VrapType.simpleGoName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName.exportName()
        is VrapObjectType -> this.simpleClassName.exportName()
        is VrapArrayType -> this.itemType.simpleGoName()
        is VrapNilType -> "nil"
    }
}

fun VrapType.flattenVrapType(): VrapType {
    return when (this) {
        is VrapArrayType -> {
            this.itemType.flattenVrapType()
        }
        else -> this
    }
}
