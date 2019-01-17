package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import java.lang.IllegalStateException
import java.nio.file.Path
import java.nio.file.Paths

interface TsObjectTypeExtensions : io.vrap.codegen.languages.ExtensionsBase {

    fun getImportsForModule(moduleName: String, types: List<AnyType>): String {

        return types
                .filter { it is ObjectType }
                .map { it as ObjectType }
                .flatMap { it.getDependencies() }
                .distinct()
                .filter {
                    when (it) {
                        is VrapObjectType -> it.`package` != moduleName
                        is VrapEnumType -> it.`package` != moduleName
                        else -> false
                    }
                }
                .map {
                    when (it) {
                        is VrapObjectType -> "import { ${it.simpleClassName} } from '${relativisePaths(moduleName,it.`package`)}'"
                        is VrapEnumType -> "import { ${it.simpleClassName} } from '${relativisePaths(moduleName,it.`package`)}'"
                        else -> throw IllegalStateException("Unhandled case $it")
                    }
                }
                .joinToString(separator = "\n")
    }


    fun relativisePaths(currentModule:String, targetModule : String) : String {
        val currentRelative :Path = Paths.get(currentModule.replace(".","/"))
        val targetRelative : Path = Paths.get(targetModule.replace(".","/"))
        return currentRelative.relativize(targetRelative).toString().replaceFirst("../","./")
    }

    private fun ObjectType.getDependencies(): List<VrapType> {


        val result = this.properties
                .map { it.type }
                //If the subtipes are in the same package they should be imported
                .plus(this.subTypes)
                .plus(this.type)
                .filterNotNull()
                .map { vrapTypeProvider.doSwitch(it) }
                .map { toVrapType(it) }
                .filterNotNull()
                .filter { it !is VrapScalarType }

        return result
    }
}


private fun toVrapType(vrapType: VrapType): VrapType? {
    return when (vrapType) {
        is VrapObjectType -> vrapType
        is VrapEnumType -> vrapType
        is VrapArrayType -> {
            toVrapType(vrapType.itemType)
        }
        else -> null

    }
}
