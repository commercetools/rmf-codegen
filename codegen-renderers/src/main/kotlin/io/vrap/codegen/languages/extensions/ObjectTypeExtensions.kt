package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

/**
 * Returns all non anymous sub types of this type.
 */
fun ObjectType.namedSubTypes() = this.subTypes.asSequence().filter { it.getAnnotation("deprecated") == null }
    .filter { !it.isInlineType }.sortedBy { it.name }.toList()

fun ObjectType.hasSubtypes(): Boolean = this.discriminator?.isNotBlank()?:false && this.namedSubTypes().isNotEmpty()

/**
 * Returns the discriminator property of this type or null.
 */
fun ObjectType.discriminatorProperty() : Property? = allProperties.filter { it.name == discriminator() }.firstOrNull()
