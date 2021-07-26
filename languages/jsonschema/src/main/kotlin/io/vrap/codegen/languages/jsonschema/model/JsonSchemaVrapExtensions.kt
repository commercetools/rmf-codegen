package io.vrap.codegen.languages.jsonschema.model

import io.vrap.rmf.codegen.types.*

fun VrapType.simpleTypeName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleTypeName()}[]"
        is VrapNilType -> this.name
    }
}
