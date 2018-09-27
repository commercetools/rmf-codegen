package io.vrap.codegen.kt.languages.java.extensions

import io.vrap.codegen.kt.languages.ExtensionsBase
import io.vrap.rmf.raml.model.types.AnyType

interface AnyTypeExtensions : ExtensionsBase {
    fun AnyType.toVrapType() = vrapTypeSwitch.doSwitch(this)

}
