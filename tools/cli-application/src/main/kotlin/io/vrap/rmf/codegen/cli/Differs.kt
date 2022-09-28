package io.vrap.rmf.codegen.cli

import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType

interface Differ<T> {
    fun diff(original: T, changed: T): List<Diff<Any>>
}

class ApiDiffer: Differ<Api> {
    override fun diff(original: Api, changed: Api): List<Diff<Any>> {
        return listOf<Diff<Any>>()
            .plus(TypesDiffer().diff(original.allAnyTypes(), changed.allAnyTypes()))
            .plus(ResourcesDiffer().diff(original.allContainedResources, changed.allContainedResources))
    }
}

class TypesDiffer: Differ<List<AnyType>> {
    override fun diff(original: List<AnyType>, changed: List<AnyType>): List<Diff<Any>> {
        val originalMap = original.allAnyTypeMap()
        val changedMap = changed.allAnyTypeMap()
        return listOf<Diff<Any>>()
            .plus(TypesAddedCheck().diff(originalMap, changedMap))
            .plus(TypesRemovedCheck().diff(originalMap, changedMap))
    }
}

class ResourcesDiffer: Differ<List<Resource>> {
    override fun diff(original: List<Resource>, changed: List<Resource>): List<Diff<Any>> {
        val originalMap = original.allResourcesMap()
        val changedMap = changed.allResourcesMap()
        return listOf<Diff<Any>>()
            .plus(ResourceAddedCheck().diff(originalMap, changedMap))
            .plus(ResourceRemovedCheck().diff(originalMap, changedMap))
    }
}

class TypesAddedCheck: Differ<Map<String, AnyType>> {
    override fun diff(original: Map<String, AnyType>, changed: Map<String, AnyType>): List<Diff<Any>> {
        return changed.filter { original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.TYPE, it.key, "added type `${it.key}`", it.value) }
    }
}

class TypesRemovedCheck: Differ<Map<String, AnyType>> {
    override fun diff(original: Map<String, AnyType>, changed: Map<String, AnyType>): List<Diff<Any>> {
        return original.filter { changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.TYPE, it.key, "removed type `${it.key}`", it.value) }
    }
}

class ResourceAddedCheck: Differ<Map<String, Resource>> {
    override fun diff(original: Map<String, Resource>, changed: Map<String, Resource>): List<Diff<Any>> {
        return changed.filter { original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.RESOURCE, it.key, "added resource `${it.key}`", it.value) }
    }
}

class ResourceRemovedCheck: Differ<Map<String, Resource>> {
    override fun diff(original: Map<String, Resource>, changed: Map<String, Resource>): List<Diff<Any>> {
        return original.filter { changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.RESOURCE, it.key, "removed resource `${it.key}`", it.value) }
    }
}
