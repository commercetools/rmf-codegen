package io.vrap.codegen.languages.extensions

import io.vrap.rmf.raml.model.types.Property

fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")
