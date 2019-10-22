package io.vrap.codegen.languages.typescript.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.UnionType
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

interface TsObjectTypeExtensions : ExtensionsBase {

    fun List<AnyType>.getImportsForModule(moduleName: String): String {
        return this
                .filter { it is ObjectType }
                .map { it as ObjectType }
                .flatMap { it.getDependencies() }
                .getImportsForModuleVrapTypes(moduleName)
    }


    fun List<VrapType>.getImportsForModuleVrapTypes(moduleName: String): String {
        return this
                .distinct()
                .filter {
                    when (it) {
                        is VrapObjectType -> it.`package` != moduleName
                        is VrapEnumType -> it.`package` != moduleName
                        else -> false
                    }
                }
                .groupBy {
                    when (it) {
                        is VrapObjectType -> it.`package`
                        is VrapEnumType -> it.`package`
                        else -> throw IllegalStateException("this case should have been filtered")
                    }
                }
                .toSortedMap()
                .map {
                    val allImportedClasses = it.value.map { it.simpleTSName() }.sorted().joinToString(", ")
                    "import { $allImportedClasses } from '${relativizePaths(moduleName, it.key)}'"
                }
                .joinToString(separator = "\n")
    }

    private fun relativizePaths(currentModule: String, targetModule: String): String {
        val currentRelative: Path = Paths.get(currentModule.replace(".", "/"))
        val targetRelative: Path = Paths.get(targetModule.replace(".", "/"))
        return currentRelative.relativize(targetRelative).toString().replaceFirst("../", "./")
    }

    private fun ObjectType.getDependencies(): List<VrapType> {
        var dependentTypes = this.allProperties
                .map { it.type }
                .plus(subTypes)
                .plus(type)
                .flatMap { if (it is UnionType) it.oneOf else Collections.singletonList(it) }
                .filterNotNull()

        val result = dependentTypes
                .map { it.toVrapType() }
                .map { it.flattenVrapType() }
                .filterNotNull()
                .filter { it !is VrapScalarType }

        return result
    }

    fun EObject?.toVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.normalizeModuleName()
    }

    fun VrapType.normalizeModuleName(): VrapType {
        return when (this) {
            is VrapObjectType -> {
                VrapObjectType(`package` = this.`package`.lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapEnumType -> {
                VrapEnumType(`package` = this.`package`.lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapArrayType -> {
                VrapArrayType(itemType = this.itemType.normalizeModuleName())
            }
            else -> this
        }
    }


    private fun String.lowerCasePackage(): String {
        return this.split("/").map { StringCaseFormat.LOWER_HYPHEN_CASE.apply(it) }.joinToString(separator = "/")
    }

    private fun VrapType.flattenVrapType(): VrapType? {
        return when (this) {
            is VrapObjectType -> this
            is VrapEnumType -> this
            is VrapArrayType -> {
                this.itemType.flattenVrapType()
            }
            is VrapNilType -> this
            else -> null

        }
    }
}






