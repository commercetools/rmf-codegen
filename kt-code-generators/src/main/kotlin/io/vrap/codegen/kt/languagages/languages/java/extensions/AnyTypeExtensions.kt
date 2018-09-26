package io.vrap.codegen.kt.languagages.languages.java.extensions

import io.vrap.codegen.kt.languagages.languages.ExtensionsBase
import io.vrap.rmf.raml.model.types.AnyType

interface AnyTypeExtensions : ExtensionsBase {
    fun AnyType.toVrapType() = vrapTypeSwitch.doSwitch(this)

}
