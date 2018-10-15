package io.vrap.codegen.kt.languages.java.extensions

import io.vrap.codegen.kt.languages.ExtensionsBase
import org.eclipse.emf.ecore.EObject

interface EObjectTypeExtensions : ExtensionsBase {
    fun EObject.toVrapType() = vrapTypeProvider.doSwitch(this)

}
