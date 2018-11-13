package io.vrap.codegen.kt.languages.php.extensions

import io.vrap.rmf.codegen.kt.types.VrapArrayType
import io.vrap.rmf.codegen.kt.types.VrapNilType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapType

fun String.toNamespaceName():String{
    val `package` = this.split(".")
    return `package`.takeLast(maxOf(`package`.size - 1, 1)).map { s -> s.capitalize() }.joinToString("\\")
}

fun String.toNamespaceDir():String{
    val `package` = this.split(".")
    return `package`.takeLast(maxOf(`package`.size - 1, 1)).map { s -> s.capitalize() }.joinToString("/")
}

fun VrapType.namespaceName():String{
    return when(this){
        is VrapObjectType -> this.`package`.toNamespaceName()
        is VrapArrayType -> this.itemType.namespaceName()
        else -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.simpleName():String{
    return when(this){
        is VrapObjectType -> this.simpleClassName
        is VrapArrayType -> "${this.itemType.simpleName()}${if (!this.itemType.isScalar()) "Collection" else "[]"}"
        is VrapNilType -> throw IllegalStateException("$this has no simple class name.")
    }
}

fun VrapType.fullClassName():String{
    return when(this){
        is VrapObjectType -> "${this.namespaceName()}\\\\${this.simpleClassName}"
        is VrapArrayType -> "${this.itemType.fullClassName()}${if (!this.itemType.isScalar()) "Collection" else "[]"}"
        is VrapNilType -> throw IllegalStateException("$this has no full class name.")
    }
}

fun scalarTypes():Array<String> { return arrayOf("string", "int", "float", "bool", "array") }

fun VrapType.isScalar(): Boolean {
    return when(this){
        is VrapObjectType -> when(this.simpleClassName) {
            in scalarTypes() -> true
            else -> false
        }
        is VrapArrayType -> when(this.itemType.simpleName()) {
            in scalarTypes() -> true
            else -> false
        }
        else -> false
    }
}
