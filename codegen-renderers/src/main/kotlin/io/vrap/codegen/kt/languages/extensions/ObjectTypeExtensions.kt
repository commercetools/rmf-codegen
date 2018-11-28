package io.vrap.codegen.kt.languages.extensions

import io.vrap.rmf.raml.model.types.ObjectType

fun ObjectType.namedSubTypes() = this.subTypes.asSequence().filter { !it.isInlineType }.toList()

fun ObjectType.hasSubtypes(): Boolean = this.discriminator?.isNotBlank()?:false && this.namedSubTypes().isNotEmpty()
