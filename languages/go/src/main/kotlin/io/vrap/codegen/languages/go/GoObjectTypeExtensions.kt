package io.vrap.codegen.languages.go

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import org.eclipse.emf.ecore.EObject
import java.util.*

interface GoObjectTypeExtensions : ExtensionsBase {

    fun AnyType.renderTypeExpr(): String {
        return when (this) {
            is UnionType -> {
                // Go has no concept of unions so we look for the shared common
                // type. If none found we should use interface{}
                if (oneOf.size > 1) {
                    val common = commonType(oneOf)
                    if (common != null) {
                        return common.renderTypeExpr()
                    }
                    return "interface{}"
                }
                return oneOf[0].renderTypeExpr()
            }
            is IntersectionType -> allOf.map { it.renderTypeExpr() }.joinToString(" & ")
            is NilType -> "None"
            else -> toVrapType().goTypeName()
        }
    }

    private fun commonType(types: List<AnyType>): AnyType? {
        val baseTypes = types.map {
            it.type
        }.filterNotNull()

        if (baseTypes.isEmpty()) return null
        if (baseTypes.size > 1) {
            return commonType(baseTypes)
        }
        return baseTypes[0]
    }

    fun AnyType.moduleName(): String {
        val type = this.toVrapType()
        return when (type) {
            is VrapObjectType -> type.`package`
            is VrapEnumType -> type.`package`
            else -> ""
        }
    }

    private fun ObjectType.getTypeDependencies(): List<VrapType> {
        return this.allProperties
            .map { it.type }
            .flatMap { if (it is UnionType) it.oneOf else Collections.singletonList(it) }
            .filterNotNull()
            .map { it.toVrapType() }
            .map { it.flattenVrapType() }
            .filterNotNull()
            .filter { it !is VrapScalarType }
    }

    fun EObject?.toVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.createGoVrapType()
    }

    fun VrapType.createGoVrapType(): VrapType {
        return when (this) {
            is VrapObjectType -> {
                VrapObjectType(`package` = this.`package`.goModelFileName(), simpleClassName = this.simpleClassName)
            }
            is VrapEnumType -> {
                VrapEnumType(`package` = this.`package`.goModelFileName(), simpleClassName = this.simpleClassName)
            }
            is VrapArrayType -> {
                VrapArrayType(itemType = this.itemType.createGoVrapType())
            }
            else -> this
        }
    }

    fun List<AnyType>.getEnumVrapTypes(): List<VrapType> {
        return this
            .filterIsInstance<ObjectType>()
            .flatMap { it.allProperties }
            .map { it.type.toVrapType() }
            .map {
                when (it) {
                    is VrapEnumType -> it
                    is VrapArrayType ->
                        when (it.itemType) {
                            is VrapEnumType -> it
                            else -> null
                        }
                    else -> null
                }
            }
            .filterNotNull()
    }

    fun List<VrapType>.getImportsForModelVrapTypes(moduleName: String): List<String> {
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
                val allImportedClasses = it.value.map { it.simpleGoName() }.sorted().joinToString(", ")
                "from ${it.key.toRelativePackageName(moduleName)} import $allImportedClasses"
            }
    }

    fun List<AnyType>.getTypeInheritance(type: AnyType): List<AnyType> {
        return this
            .filter { it.type != null && it.type.name == type.name }
        // TODO: Shouldn't this be necessary?
        // .plus(
        //     this
        //     .filter { it.type != null && it.type.name == type.name }
        //     .flatMap { this.getTypeInheritance(it.type) }
        // )
    }

    fun ObjectType.goStructFields(all: Boolean): List<Property> {
        var props: List<Property> = allProperties

        if (!all) {
            val parentProps = getSuperProperties().map { it.name }
            props = allProperties.filter { !parentProps.contains(it.name) }
        }
        return props.filter {
            (
                (discriminator() == null || it.name != discriminator()) ||
                    (it.name == discriminator() && discriminatorValue == null)
                )
        }
    }

    fun ObjectType.getSuperProperties(): List<Property> {
        return when (this.type) {
            is ObjectType -> (this.type as ObjectType).allProperties
            else -> emptyList<Property>()
        }
    }

    fun AnyType.isDiscriminated(): Boolean {
        if (this !is ObjectType) {
            return false
        }
        if (this.discriminator() != null && this.discriminatorValue.isNullOrEmpty()) {
            val parentType = this.type
            if (parentType is ObjectType && !parentType.discriminatorValue.isNullOrEmpty()) {
                return false
            }
            return true
        }
        return false
    }

    fun ObjectType.isErrorObject(): Boolean {
        if (!name.lowercase().contains("error")) {
            return false
        }

        return goStructFields(true)
            .any {
                it.type is StringType && it.name.lowercase() == "message"
            }
    }

    fun ObjectType.isMap(): Boolean {

        if (this.type != null && this.type.getAnnotation("asMap") != null) {
            return true
        }
        if (this.getAnnotation("asMap") != null) {
            return true
        }
        return false
    }
}
