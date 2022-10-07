package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType

interface Differ<T>{
    val diffDataType: DiffDataType
    val severity: CheckSeverity

    fun diff(data: DiffData<T>): List<Diff<Any>>
}

abstract class DiffCheck<T>(override val severity: CheckSeverity): Differ<T> {}

class TypeAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.TYPE, it.key, "added type `${it.key}`", it.value, severity) }
    }
}

class TypeRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.TYPE, it.key, "removed type `${it.key}`", it.value, severity) }
    }
}

class ResourceAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Resource>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.RESOURCE, it.key, "added resource `${it.key}`", it.value, severity) }
    }
}

class ResourceRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Resource>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.RESOURCE, it.key, "removed resource `${it.key}`", it.value, severity) }
    }
}

class MethodAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.METHOD, it.key, "added method `${it.key}`", it.value, severity) }
    }
}

class MethodRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.METHOD, it.key, "removed method `${it.key}`", it.value, severity) }
    }
}

class PropertyAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, ObjectType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.flatMap { (typeName, objectType) ->
                objectType.allProperties.toPropertyMap().filter { data.original[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                    DiffType.ADDED, Scope.PROPERTY, propertyName, "added property `${propertyName}` to type `${typeName}`", property, severity) }
        }
    }
}

class PropertyRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, ObjectType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }.flatMap { (typeName, objectType) ->
            objectType.allProperties.toPropertyMap().filter { data.changed[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                DiffType.REMOVED, Scope.PROPERTY, propertyName, "removed property `${propertyName}` from type `${typeName}`", property, severity) }
        }
    }
}
