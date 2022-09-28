package io.vrap.rmf.codegen.cli

import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.AnyType

interface Differ<T> {
    fun diff(original: T, changed: T): List<Diff<Any>>
}

class ApiDiffer: Differ<Api> {
    override fun diff(original: Api, changed: Api): List<Diff<Any>> {
        return listOf<Diff<Any>>()
            .plus(TypesDiffer().diff(original.allAnyTypes(), changed.allAnyTypes()))
    }
}

class TypesDiffer: Differ<List<AnyType>> {
    override fun diff(original: List<AnyType>, changed: List<AnyType>): List<Diff<Any>> {
        val originalTypesMap = original.allAnyTypeMap()
        val changedTypesMap = changed.allAnyTypeMap()
        return listOf<Diff<Any>>()
            .plus(TypesAddedCheck().diff(originalTypesMap, changedTypesMap))
            .plus(TypesRemovedCheck().diff(originalTypesMap, changedTypesMap))
    }
}

class TypesAddedCheck: Differ<Map<String, AnyType>> {
    override fun diff(original: Map<String, AnyType>, changed: Map<String, AnyType>): List<Diff<Any>> {
        return changed.filter { original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.TYPE, it.key, "added type `${it.key}`", it.value, it.value.getSource()) }
    }
}

class TypesRemovedCheck: Differ<Map<String, AnyType>> {
    override fun diff(original: Map<String, AnyType>, changed: Map<String, AnyType>): List<Diff<Any>> {
        return original.filter { changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.TYPE, it.key, "removed type `${it.key}`", it.value, it.value.getSource()) }
    }
}


