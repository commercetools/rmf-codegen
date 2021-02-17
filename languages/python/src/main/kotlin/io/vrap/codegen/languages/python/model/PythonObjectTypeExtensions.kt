/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.python.lowerCasePackage
import io.vrap.codegen.languages.python.toRelativePackageName
import io.vrap.codegen.languages.python.toSchemaPackageName
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapNilType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.UnionType
import org.eclipse.emf.ecore.EObject
import java.util.Collections

interface PyObjectTypeExtensions : ExtensionsBase {

    fun List<AnyType>.getImportsForModule(moduleName: String): String {
        val imports = this
            .filterIsInstance<ObjectType>()
            .map { it.type.toPythonVrapType() }
            .plus(getEnumVrapTypes())
            .getImportsForModelVrapTypes(moduleName)
            .joinToString(separator = "\n")
        return """
        |import datetime
        |import enum
        |import typing
        |
        |from ._abstract import _BaseType
        |$imports
        """.trimMargin()
    }

    fun List<AnyType>.getTypeImportsForModule(moduleName: String): String {
        val imports = this
            .filterIsInstance<ObjectType>()
            .flatMap { it.getTypeDependencies() }
            .getImportsForModelVrapTypes(moduleName)
            .joinToString(separator = "\n")

        if (imports != "") {
            return """
            |if typing.TYPE_CHECKING:
            |    <$imports>
            """.trimMargin()
        }
        return ""
    }

    fun List<AnyType>.getSchemaImportsForModule(moduleName: String): String {

        val fieldTypes = this
            .filterIsInstance<ObjectType>()
            .flatMap { it.getSchemaDependencies() }

        val enumImports = this
            .getEnumVrapTypes()
            .getImportsForModelVrapTypes(moduleName)

        return this
            .filterIsInstance<ObjectType>()
            .map { it.type.toSchemaVrapType() }
            .plus(fieldTypes)
            .getImportsForSchemaVrapTypes(moduleName)
            .plus(enumImports)
            .plus("from ... import models")
            .plus("import typing")
            .joinToString(separator = "\n")
    }

    fun AnyType.moduleName(): String {
        val type = this.toPythonVrapType()
        return when (type) {
            is VrapObjectType -> type.`package`
            is VrapEnumType -> type.`package`
            else -> ""
        }
    }

    fun AnyType.schemaModuleName(): String {
        val type = this.toSchemaVrapType()
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
            .map { it.toPythonVrapType() }
            .map { it.flattenVrapType() }
            .filterNotNull()
            .filter { it !is VrapScalarType }
    }

    fun EObject?.toVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.createPythonVrapType()
    }

    fun EObject?.toPythonVrapType(): VrapType {
        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.createPythonVrapType()
    }

    fun EObject?.toSchemaVrapType(): VrapType {

        // TODO: Return the base type fo the first item
        if (this is UnionType) {
            return this.oneOf[0].type.toSchemaVrapType()
        }

        val vrapType = if (this != null) vrapTypeProvider.doSwitch(this) else VrapNilType()
        return vrapType.createSchemaVrapType()
    }

    fun VrapType.createPythonVrapType(): VrapType {
        return when (this) {
            is VrapObjectType -> {
                VrapObjectType(`package` = this.`package`.lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapEnumType -> {
                VrapEnumType(`package` = this.`package`.lowerCasePackage(), simpleClassName = this.simpleClassName)
            }
            is VrapArrayType -> {
                VrapArrayType(itemType = this.itemType.createPythonVrapType())
            }
            else -> this
        }
    }

    private fun VrapType.createSchemaVrapType(): VrapType {
        return when (this) {
            is VrapObjectType -> {
                VrapObjectType(`package` = this.`package`.lowerCasePackage().toSchemaPackageName(), simpleClassName = "${this.simpleClassName}Schema")
            }
            is VrapEnumType -> {
                VrapEnumType(`package` = this.`package`.lowerCasePackage().toSchemaPackageName(), simpleClassName = "${this.simpleClassName}Schema")
            }
            is VrapArrayType -> {
                VrapArrayType(itemType = this.itemType.createSchemaVrapType())
            }
            else -> this
        }
    }

    fun List<AnyType>.getEnumVrapTypes(): List<VrapType> {
        return this
            .filterIsInstance<ObjectType>()
            .flatMap { it.allProperties }
            .map { it.type.toPythonVrapType() }
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

    private fun ObjectType.getSchemaDependencies(): List<VrapType> {
        return this.allProperties
            .filter { it.type is ObjectType }
            .map { it.type as ObjectType }
            .filter {
                it.isDict()
            }
            .filterNotNull()
            .map {
                val vrapType = it.toSchemaVrapType()
                if (vrapType is VrapObjectType) {
                    VrapObjectType(`package` = vrapType.`package`, simpleClassName = "${it.name}Field")
                } else null
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
                val allImportedClasses = it.value.map { it.simplePyName() }.sorted().joinToString(", ")
                "from ${it.key.toRelativePackageName(moduleName)} import $allImportedClasses"
            }
    }

    fun List<VrapType>.getImportsForSchemaVrapTypes(moduleName: String): List<String> {
        return this
            .map { it.flattenVrapType() }
            .distinct()
            .filter {
                when (it) {
                    is VrapObjectType -> it.`package` != moduleName
                    is VrapEnumType -> false
                    else -> false
                }
            }
            .groupBy {
                when (it) {
                    is VrapObjectType -> it.`package`
                    else -> throw IllegalStateException("this case should have been filtered")
                }
            }
            .toSortedMap()
            .map {
                val allImportedClasses = it.value.map { it.PySchemaNameReference() }.sorted().joinToString(", ")
                val importPath = it.key.toRelativePackageName(moduleName)
                "from $importPath import $allImportedClasses"
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

    fun ObjectType.PyClassProperties(all: Boolean): List<Property> {
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

    fun ObjectType.isDict(): Boolean {

        if (this.type != null && this.type.getAnnotation("asMap") != null) {
            return true
        }
        if (this.getAnnotation("asMap") != null) {
            return true
        }
        return false
    }
}
