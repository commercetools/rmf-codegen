package io.vrap.codegen.languages.php.extensions

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.UnionType


interface  ObjectTypeExtensions : ExtensionsBase {

    fun ObjectType.getImports(): List<String> {
        return getImports(this.properties)
    }

    fun ObjectType.getImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { it.type }
                //If the subtipes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .filter { !it.isScalar() }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .filter { !it.contains("DateTimeImmutableCollection") }
                .sortedBy { it }
                .distinct()
                .toList()

        if(result.contains("")){
            println()
        }

        return result
    }

    fun ObjectType.getPropertyImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { it.type }
                .flatMap { when (it) {
                    is UnionType -> it.oneOf.map { t -> t }
                    else -> listOf(it)
                } }
                .map { vrapTypeProvider.doSwitch(it) }
                .filter { s -> when(s) {
                        is VrapObjectType -> true
                        is VrapArrayType -> when (s.itemType) {
                            is ObjectType -> true
                            else -> false
                        }
                        else -> false
                    }
                }
                .filter { !it.isScalar() }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .filter { !it.contains("DateTimeImmutableCollection") }
                .sortedBy { it }
                .distinct()
                .toList()

        if(result.contains("")){
            println()
        }

        return result
    }

    fun ObjectType.getObjectImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { it.type }
                .flatMap { when (it) {
                    is UnionType -> it.oneOf.map { t -> t }
                    else -> listOf(it)
                } }
                //If the subtipes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .filter { !it.isScalar() }
                .filter { s -> when(s) {
                        is VrapObjectType -> true
                        is VrapArrayType -> when (s.itemType) {
                            is ObjectType -> true
                            else -> false
                        }
                        else -> false
                    }
                }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .filter { !it.contains("DateTimeImmutableCollection") && !it.contains("DateTimeImmutable") }
                .sortedBy { it }
                .distinct()
                .toList()

        return result
    }
}

fun getImportsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> "${if (vrapType.namespaceName().isNotEmpty()) "${vrapType.namespaceName()}\\" else "" }${vrapType.simpleName()}"
        is VrapArrayType -> when (vrapType.itemType) {
            is VrapObjectType -> "${if (vrapType.namespaceName().isNotEmpty()) "${vrapType.namespaceName()}\\" else "" }${vrapType.simpleName()}"
            else -> null
        }
        else -> null

    }
}

fun ObjectType.namedSubTypes() = this.subTypes.plus( this.subTypes.flatMap { it.subTypes } ).filterNot { it.isInlineType }
