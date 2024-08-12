package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.BooleanInstance
import io.vrap.rmf.raml.model.types.IntersectionType
import io.vrap.rmf.raml.model.types.UnionType

/**
 * Returns the list of all super types of this type.
 * This is the opposite of [AnyType.getSubTypes]
 *
 * @return the list of super types
 */
fun AnyType.getSuperTypes() : List<AnyType> {
    return when(this) {
        is IntersectionType -> allOf
        is UnionType -> oneOf
        else -> listOf(type)
    }
}

fun AnyType.deprecated() : Boolean {
    val anno = this.getAnnotation("deprecated")
    return (anno != null && (anno.value as BooleanInstance).value)
}

fun AnyType.markDeprecated() : Boolean {
    val anno = this.getAnnotation("markDeprecated")
    return (anno != null && (anno.value as BooleanInstance).value)
}
