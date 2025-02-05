package io.vrap.codegen.languages.typescript.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.UnionType
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.util.*

interface TsObjectTypeExtensions : ExtensionsBase {

    fun List<AnyType>.getImportsForModule(moduleName: String): String {
        val objectTypes = this
            .filterIsInstance<ObjectType>()
        val dependentSubTypes = objectTypes.flatMap { it.getDependentTypes() }.map { if (it.type != null && it.isInlineType) it.type else it }.filterIsInstance<ObjectType>();
        return objectTypes
                .flatMap { it.getDependencies() }
                .getImportsForModuleVrapTypes(moduleName, dependentSubTypes.filter { it.discriminator == null && it.namedSubTypes().isNotEmpty() }.map { it.name }, dependentSubTypes.filter { it.discriminator != null }.map { it.name }.distinct())
    }

    fun AnyType.moduleName(): String {
        val type = this.toVrapType()
        return when (type) {
            is VrapObjectType -> type.`package`
            is VrapEnumType -> type.`package`
            is VrapScalarType -> "models/scalar-types"
            else -> ""
        }
    }

    fun List<VrapType>.getImportsForModuleVrapTypes(moduleName: String, dependentSubTypes: List<String>, discriminatorTypes: List<String>): String {
        return this
                .map { it.flattenVrapType() }
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
                    val allImportedClasses = it.value.map { it.simpleTSName() }
                        .plus(it.value.filter {dependentSubTypes.contains(it.simpleTSName()) }.map { "_${it.simpleTSName()}" })
                        .plus(it.value.filter {discriminatorTypes.contains(it.simpleTSName()) }.map { "I${it.simpleTSName()}" })
                        .sorted().joinToString(", ")
                    "import { $allImportedClasses } from '${it.key}'"
                }
                .joinToString(separator = "\n")
    }

    private fun ObjectType.getDependentTypes(): List<AnyType> {
        return this.allProperties
            .map { it.type }
            .plus(subTypes.plus(subTypes.flatMap { it.subTypes }).distinctBy { it.name })
            .plus(type)
            .flatMap { if (it is UnionType) it.oneOf else Collections.singletonList(it) }
            .filterNotNull()
    }

    private fun ObjectType.getDependencies(): List<VrapType> {
        return getDependentTypes()
            .map { it.toVrapType() }
            .map { it.flattenVrapType() }
            .filterNotNull()
            .filter { it !is VrapScalarType }
    }

    fun EObject?.toVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.normalizeModuleName()
    }

    fun VrapType.normalizeModuleName(): VrapType {
        return when (this) {
            is VrapObjectType -> {
                VrapObjectType(`package` = this.`package`.modelNormalizer().lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapEnumType -> {
                VrapEnumType(`package` = this.`package`.modelNormalizer().lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapArrayType -> {
                VrapArrayType(itemType = this.itemType.normalizeModuleName())
            }
            else -> this
        }
    }

    private fun String.modelNormalizer(): String {
        return when(this) {
            "models" -> "models/common"
            else -> this;
        }
    }

    private fun String.lowerCasePackage(): String {
        return this.split("/").map { StringCaseFormat.LOWER_HYPHEN_CASE.apply(it) }.joinToString(separator = "/")
    }

    private fun VrapType.flattenVrapType(): VrapType {
        return when (this) {
            is VrapArrayType -> {
                this.itemType.flattenVrapType()
            }
            else -> this

        }
    }
}
