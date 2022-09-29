package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType

interface Differ<T>{
    val diffDataType: DiffDataType

    fun diff(data: DiffData<T>): List<Diff<Any>>
}

class TypeAddedCheck: Differ<Map<String, AnyType>> {
    override val diffDataType: DiffDataType = DiffDataType.TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.TYPE, it.key, "added type `${it.key}`", it.value) }
    }
}

class TypeRemovedCheck: Differ<Map<String, AnyType>> {
    override val diffDataType: DiffDataType = DiffDataType.TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.TYPE, it.key, "removed type `${it.key}`", it.value) }
    }
}

class ResourceAddedCheck: Differ<Map<String, Resource>> {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.RESOURCE, it.key, "added resource `${it.key}`", it.value) }
    }
}

class ResourceRemovedCheck: Differ<Map<String, Resource>> {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.RESOURCE, it.key, "removed resource `${it.key}`", it.value) }
    }
}

class MethodAddedCheck: Differ<Map<String, Method>> {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.METHOD, it.key, "added method `${it.key}`", it.value) }
    }
}

class MethodRemovedCheck: Differ<Map<String, Method>> {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.METHOD, it.key, "removed method `${it.key}`", it.value) }
    }
}

class PropertyAddedCheck: Differ<Map<String, ObjectType>> {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.flatMap { (typeName, objectType) ->
                objectType.allProperties.toPropertyMap().filter { data.original[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                    DiffType.ADDED, Scope.PROPERTY, propertyName, "added property `${propertyName}` to type `${typeName}`", property) }
        }
    }
}

class PropertyRemovedCheck: Differ<Map<String, ObjectType>> {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }.flatMap { (typeName, objectType) ->
            objectType.allProperties.toPropertyMap().filter { data.changed[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                DiffType.REMOVED, Scope.PROPERTY, propertyName, "removed property `${propertyName}` to type `${typeName}`", property) }
        }
    }
}
