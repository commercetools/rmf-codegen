package io.vrap.rmf.codegen.cli.diff

import io.vrap.codegen.languages.extensions.resource
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType

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

    private val allStringTypes by lazy {
        DiffData(
            original.allAnyTypes().filterIsInstance<StringType>(),
            changed.allAnyTypes().filterIsInstance<StringType>()
        )
    }

    private val allStringTypesMap by lazy {
        DiffData(allStringTypes.original.toStringTypeMap(), allStringTypes.changed.toStringTypeMap())
    }

    private val allPropertiesMap by lazy {
        DiffData(
            allObjectTypes.original.flatMap { objectType ->
                objectType.allProperties.map {
                    PropertyReference(
                        objectType.name,
                        it.name
                    ) to it
                }
            }.toMap(),
            allObjectTypes.changed.flatMap { objectType ->
                objectType.allProperties.map {
                    PropertyReference(
                        objectType.name,
                        it.name
                    ) to it
                }
            }.toMap()
        )
    }

    private val allMethodTypesMap by lazy {
        DiffData(
            allMethods.original.flatMap { method ->
                method.bodies.map {
                    MethodBodyTypeReference(
                        method.resource().fullUri.template,
                        method.methodName,
                        it.contentType
                    ) to it
                }
            }.toMap(),
            allMethods.changed.flatMap { method ->
                method.bodies.map {
                    MethodBodyTypeReference(
                        method.resource().fullUri.template,
                        method.methodName,
                        it.contentType
                    ) to it
                }
            }.toMap()
        )
    }

    private val allMethodResponseTypesMap by lazy {
        DiffData(
            allMethods.original.flatMap { method ->
                method.responses.flatMap { response ->
                    response.bodies.map {
                        MethodResponseBodyTypeReference(
                            method.resource().fullUri.template,
                            method.methodName,
                            response.statusCode,
                            it.contentType
                        ) to it
                    }
                }
            }.toMap(),
            allMethods.changed.flatMap { method ->
                method.responses.flatMap { response ->
                    response.bodies.map {
                        MethodResponseBodyTypeReference(
                            method.resource().fullUri.template,
                            method.methodName,
                            response.statusCode,
                            it.contentType
                        ) to it
                    }
                }
            }.toMap()
        )
    }

    fun by(type: DiffDataType): DiffData<out Any> {
        return when (type) {
            DiffDataType.API -> DiffData(original, changed)
            DiffDataType.RESOURCES -> allResources
            DiffDataType.RESOURCES_MAP -> allResourcesMap
            DiffDataType.ANY_TYPES -> allAnyTypes
            DiffDataType.ANY_TYPES_MAP -> allAnyTypesMap
            DiffDataType.STRING_TYPES -> allStringTypes
            DiffDataType.STRING_TYPES_MAP -> allStringTypesMap
            DiffDataType.OBJECT_TYPES -> allObjectTypes
            DiffDataType.OBJECT_TYPES_MAP -> allObjectTypesMap
            DiffDataType.METHODS -> allMethods
            DiffDataType.METHODS_MAP -> allMethodsMap
            DiffDataType.PROPERTIES_MAP -> allPropertiesMap
            DiffDataType.METHOD_TYPES_MAP -> allMethodTypesMap
            DiffDataType.METHOD_RESPONSE_TYPES_MAP -> allMethodResponseTypesMap
        }
    }
}
