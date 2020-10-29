package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.elements.NamedElement

/**
 * Returns true if this property is a pattern property.
 *
 * @return true iff. this is a pattern property
 */
fun NamedElement.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")


/**
 * Returns true if this property is an Attribute property.
 *
 * need to be implemented
 */
fun NamedElement.isAttributeProperty() = this.name.toLowerCase().contains("attribute")
