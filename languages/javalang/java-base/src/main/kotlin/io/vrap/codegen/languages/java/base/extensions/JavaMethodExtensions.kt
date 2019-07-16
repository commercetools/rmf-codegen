package io.vrap.codegen.languages.java.base.extensions

import io.vrap.codegen.languages.extensions.returnType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method

fun Method.javaReturnType(vrapTypeProvider: VrapTypeProvider) : String {
    val commandReturnType = vrapTypeProvider.doSwitch(this.returnType()) as VrapObjectType
    return "${commandReturnType.`package`}.${commandReturnType.simpleClassName}"
}