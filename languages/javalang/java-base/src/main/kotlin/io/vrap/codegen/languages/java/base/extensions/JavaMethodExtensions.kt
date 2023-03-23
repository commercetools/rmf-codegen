package io.vrap.codegen.languages.java.base.extensions

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.returnType
import io.vrap.codegen.languages.extensions.toParamName
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method

fun Method.javaReturnType(vrapTypeProvider: VrapTypeProvider) : String {
    val returnType = vrapTypeProvider.doSwitch(this.returnType())
    if(returnType is VrapObjectType) {
        return "${returnType.`package`.toJavaPackage()}.${returnType.simpleClassName}"
    }
    return "com.fasterxml.jackson.databind.JsonNode"
}

fun Method.toStringRequestName(): String {
    return this.resource().fullUri.toParamName("By") + this.method.toString().firstUpperCase() + "String"
}
