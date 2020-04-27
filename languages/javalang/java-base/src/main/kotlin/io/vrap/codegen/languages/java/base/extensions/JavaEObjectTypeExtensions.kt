package io.vrap.codegen.languages.java.base.extensions

import io.vrap.codegen.languages.java.base.ExtensionsBase
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.FileType
import org.eclipse.emf.ecore.EObject

interface JavaEObjectTypeExtensions : ExtensionsBase {
    fun EObject?.toVrapType(): VrapType {
        return if (this != null) {
            vrapTypeProvider.doSwitch(this).toJavaVType()
        } else{
            VrapNilType()
        }

    }
}
fun EObject?.isFile(): Boolean = this is FileType