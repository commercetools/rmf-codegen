package io.vrap.rmf.codegen.rendering

import io.vrap.rmf.raml.model.types.AnyType

interface NamedScalarTypeRenderer<T: AnyType> : Renderer<T>
