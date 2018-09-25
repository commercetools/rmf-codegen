package io.vrap.rmf.codegen.kt.languages.java.extensions

import io.vrap.rmf.codegen.kt.types.TypeNameSwitch
import io.vrap.rmf.raml.model.types.AnyType

fun AnyType.toVrapObject(typeNameSwitch: TypeNameSwitch) = typeNameSwitch.doSwitch(this)