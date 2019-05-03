package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.types.Property

/**
 * Returns true if this property is a pattern property.
 *
 * @return true iff. this is a pattern property
 */
fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")
