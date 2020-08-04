package io.vrap.codegen.languages.csharp.extensions

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat

fun VrapType.simpleName(): String {
    return when (this) {
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapAnyType -> this.baseType
        is VrapArrayType -> """List\<${this.itemType.simpleName()}\>"""
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName(): String {
    return when (this) {
        is VrapAnyType -> this.baseType
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "${this.`package`}.${this.simpleClassName}"
        is VrapObjectType -> "${this.`package`}.${this.simpleClassName}"
        is VrapArrayType -> """"System.Collections.Generic.List\<${this.itemType.fullClassName()}\>"""
        is VrapNilType -> "void"
    }
}

fun VrapType.toCsharpVType(): VrapType {
    return when (this) {
        is VrapObjectType -> {
            VrapObjectType(`package` = this.`package`.toCsharpPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapEnumType -> {
            VrapEnumType(`package` = this.`package`.toCsharpPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapArrayType -> {
            VrapArrayType(this.itemType.toCsharpVType())
        }
        else -> {
            this
        }

    }
}

fun String.toCsharpPackage(): String {
    return this.split('.', '/')
            .map(
                    StringCaseFormat.LOWER_UNDERSCORE_CASE::apply
            )
            .joinToString(separator = ".")
}

fun VrapType.csharpPackage(): String {
  var packageName = ""
    if (this is VrapObjectType) packageName = this.`package`.toCsharpPackage()
    else if (this is VrapEnumType) packageName = this.`package`.toCsharpPackage()

  return packageName
}
//need to be implemented
fun VrapType.isValueType(): Boolean {
   return false
}