package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.ObjectType

class DataProvider(private val original: Api, private val changed: Api) {

    private val allMethods by lazy {
        DiffData(original.allResourceMethods(), changed.allResourceMethods())
    }

    private val allMethodsMap by lazy {
        DiffData(original.allResourceMethods().toMethodMap(), changed.allResourceMethods().toMethodMap())
    }

    private val allResources by lazy {
        DiffData(original.allContainedResources, changed.allContainedResources)
    }

    private val allResourcesMap by lazy {
        DiffData(original.allContainedResources.toResourcesMap(), changed.allContainedResources.toResourcesMap())
    }

    private val allAnyTypes by lazy {
        DiffData(original.allAnyTypes(), changed.allAnyTypes())
    }

    private val allAnyTypesMap by lazy {
        DiffData(original.allAnyTypes().toAnyTypeMap(), changed.allAnyTypes().toAnyTypeMap())
    }

    private val allObjectTypes by lazy {
        DiffData(
            original.allAnyTypes().filterIsInstance<ObjectType>(),
            changed.allAnyTypes().filterIsInstance<ObjectType>()
        )
    }

    private val allObjectTypesMap by lazy {
        DiffData(allObjectTypes.original.toObjectTypeMap(), allObjectTypes.changed.toObjectTypeMap())
    }

    private val allPropertiesMap by lazy {
        DiffData(
            allObjectTypes.original.flatMap { objectType ->
                objectType.allProperties.map {
                    Pair(
                        objectType.name,
                        it.name
                    ) to it
                }
            }.toMap(),
            allObjectTypes.changed.flatMap { objectType ->
                objectType.allProperties.map {
                    Pair(
                        objectType.name,
                        it.name
                    ) to it
                }
            }.toMap()
        )
    }

    fun by(type: DiffDataType): DiffData<out Any> {
        return when (type) {
            DiffDataType.API -> DiffData(original, changed)
            DiffDataType.RESOURCES -> allResources
            DiffDataType.RESOURCES_MAP -> allResourcesMap
            DiffDataType.TYPES -> allAnyTypes
            DiffDataType.TYPES_MAP -> allAnyTypesMap
            DiffDataType.OBJECT_TYPES -> allObjectTypes
            DiffDataType.OBJECT_TYPES_MAP -> allObjectTypesMap
            DiffDataType.METHODS -> allMethods
            DiffDataType.METHODS_MAP -> allMethodsMap
            DiffDataType.PROPERTIES_MAP -> allPropertiesMap
        }
    }
}
