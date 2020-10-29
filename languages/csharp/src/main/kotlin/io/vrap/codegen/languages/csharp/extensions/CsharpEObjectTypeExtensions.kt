package io.vrap.codegen.languages.csharp.extensions

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.FileType
import org.eclipse.emf.ecore.EObject

interface CsharpEObjectTypeExtensions : ExtensionsBase {
    fun EObject?.toVrapType(): VrapType {
        return if (this != null) {
            vrapTypeProvider.doSwitch(this).toCsharpVType()
        } else{
            VrapNilType()
        }

    }
}
fun EObject?.isFile(): Boolean = this is FileType