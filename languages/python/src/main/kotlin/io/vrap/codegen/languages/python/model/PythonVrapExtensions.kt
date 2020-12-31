/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.rmf.codegen.types.VrapAnyType
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapType

fun VrapType.pyTypeName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> "${this.scalarType}"
        is VrapEnumType -> "\"${this.simpleClassName}\""
        is VrapObjectType -> "\"${this.simpleClassName}\""
        is VrapArrayType -> "typing.List[\"${this.itemType.simplePyName()}\"]"
        is VrapNilType -> "None"
    }
}

fun VrapType.simplePyName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> this.itemType.simplePyName()
        is VrapNilType -> "None"
    }
}

fun VrapType.PySchemaName(): String {
    return when (this) {
        is VrapAnyType -> "marshmallow.fields.Raw"
        is VrapScalarType -> {
            return when (this.scalarType) {
                "str" -> "marshmallow.fields.String"
                "int" -> "marshmallow.fields.Integer"
                "float" -> "marshmallow.fields.Float"
                "typing.Any" -> "marshmallow.fields.Raw"
                "object" -> "marshmallow.fields.Raw"
                "datetime.date" -> "marshmallow.fields.Date"
                "datetime.datetime" -> "marshmallow.fields.DateTime"
                "datetime.time" -> "marshmallow.fields.Time"
                "bool" -> "marshmallow.fields.Boolean"
                else -> {
                    println("No PySchemaName for ${this.scalarType}")
                    "marshmallow.fields.ToDo"
                }
            }
        }
        is VrapEnumType -> "marshmallow_enum.EnumField"
        is VrapObjectType -> {
            "${this.`package`}.${this.simpleClassName}"
        }
        is VrapArrayType -> this.itemType.PySchemaName()
        is VrapNilType -> "None"
    }
}

fun VrapType.PySchemaNameReference(): String {
    if (this is VrapObjectType) {
        return "${this.simpleClassName}"
    } else {
        return this.PySchemaName()
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
