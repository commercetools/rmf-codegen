package io.vrap.codegen.languages.php.extensions

import io.vrap.codegen.languages.ExtensionsBase
import org.eclipse.emf.ecore.EObject

interface EObjectTypeExtensions : io.vrap.codegen.languages.ExtensionsBase {
    fun EObject.toVrapType() = vrapTypeProvider.doSwitch(this)

}
