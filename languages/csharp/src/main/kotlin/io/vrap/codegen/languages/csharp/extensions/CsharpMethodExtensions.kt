package io.vrap.codegen.languages.csharp.extensions

import io.vrap.codegen.languages.extensions.returnType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method

fun Method.csharpReturnType(vrapTypeProvider: VrapTypeProvider) : String {
    val returnType = vrapTypeProvider.doSwitch(this.returnType())
    if(returnType is VrapObjectType) {
        if((returnType as VrapObjectType).`package`=="")
            return "I${returnType.simpleClassName}"
        else
            return "${returnType.`package`.toCsharpPackage()}.I${returnType.simpleClassName}"
    }
    return "JsonElement"
}