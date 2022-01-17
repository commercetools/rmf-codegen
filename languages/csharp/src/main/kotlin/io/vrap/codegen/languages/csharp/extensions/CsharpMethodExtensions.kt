package io.vrap.codegen.languages.csharp.extensions

import io.vrap.codegen.languages.extensions.returnType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Method

fun Method.csharpReturnType(vrapTypeProvider: VrapTypeProvider) : String {
    val returnType = vrapTypeProvider.doSwitch(this.returnType())
    if(returnType is VrapObjectType) {
        val simpleClassNameAsInterface = if(returnType.simpleClassName == "Object") "Object" else "I${returnType.simpleClassName}"
        if(returnType.`package`=="")
            return simpleClassNameAsInterface
        else
            return "${returnType.`package`.toCsharpPackage()}.${simpleClassNameAsInterface}"
    }
    return "JsonElement"
}
