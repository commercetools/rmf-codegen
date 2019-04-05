package io.vrap.codegen.languages.typescript.model

import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.UnionType
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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
                        is VrapObjectType -> "import { ${it.simpleClassName} } from '${relativizePaths(moduleName,it.`package`)}'"
                        is VrapEnumType -> "import { ${it.simpleClassName} } from '${relativizePaths(moduleName,it.`package`)}'"
                        else -> throw IllegalStateException("Unhandled case $it")
                    }
                }
                .joinToString(separator = "\n")
    }


    fun relativizePaths(currentModule:String, targetModule : String) : String {
        val currentRelative :Path = Paths.get(currentModule.replace(".","/"))
        val targetRelative : Path = Paths.get(targetModule.replace(".","/"))
        return currentRelative.relativize(targetRelative).toString().replaceFirst("../","./")
    }

    private fun ObjectType.getDependencies(): List<VrapType> {


        var dependentTypes = this.allProperties
            .map { it.type }
            .flatMap { if (it is UnionType) it.oneOf else Collections.singletonList(it) }
            //If the subtipes are in the same package they should be imported
            .plus(this.subTypes)
            .plus(this.type)
            .filterNotNull()

        if (this.discriminator() != null) {
            val property = this.getProperty(this.discriminator())
            if (property.type.enum.size > 0) {
                dependentTypes = dependentTypes.plus(property.type)
            }
        }
        val result = dependentTypes
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
