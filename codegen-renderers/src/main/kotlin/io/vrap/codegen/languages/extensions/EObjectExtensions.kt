package io.vrap.codegen.languages.extensions

import org.eclipse.emf.ecore.EObject

interface EObjectExtensions : ExtensionsBase {
    fun EObject.toVrapType() = vrapTypeProvider.doSwitch(this)
}
