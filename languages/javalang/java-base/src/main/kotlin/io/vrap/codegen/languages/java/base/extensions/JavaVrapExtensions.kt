package io.vrap.codegen.languages.java.base.extensions

import io.vrap.rmf.codegen.types.*

fun VrapType.simpleName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> this.simpleClassName
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "List<${this.itemType.simpleName()}>"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName():String{
    return when(this){
        is VrapScalarType -> this.scalarType
        is VrapEnumType -> "${this.`package`}.${this.simpleClassName}"
        is VrapObjectType -> "${this.`package`}.${this.simpleClassName}"
        is VrapArrayType -> "java.util.List<${this.itemType.fullClassName()}>"
        is VrapNilType -> "void"
    }
}

fun VrapType.toJavaVType():VrapType {
    return when(this){
        is VrapObjectType -> {
            return VrapObjectType(`package` = this.`package`.toJavaPackage(), simpleClassName = this.simpleClassName)
        }
        is VrapEnumType -> {
            return VrapEnumType(`package` = this.`package`.toJavaPackage(), simpleClassName = this.simpleClassName)
        }
        else -> {
            this
        }

    }
}

fun String.toJavaPackage() = this.replace("/",".")