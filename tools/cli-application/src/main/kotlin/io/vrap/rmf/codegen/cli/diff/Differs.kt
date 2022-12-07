package io.vrap.rmf.codegen.cli.diff

import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.responses.Body
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.BuiltinType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.StringType

interface Differ<T>{
    val diffDataType: DiffDataType
    val severity: CheckSeverity

    fun diff(data: DiffData<T>): List<Diff<Any>>
}

abstract class DiffCheck<T>(override val severity: CheckSeverity): Differ<T> {}

class TypeAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.TYPE, it.key, "added type `${it.key}`", it.value, severity, DiffData(null, it.value)) }
    }
}

class TypeRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP
    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.TYPE, it.key, "removed type `${it.key}`", it.value, severity, DiffData(it.value, null)) }
    }
}

class ResourceAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Resource>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.RESOURCE, it.key, "added resource `${it.key}`", it.value, severity, DiffData(null, it.value)) }
    }
}

class ResourceRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Resource>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.RESOURCES_MAP

    override fun diff(data: DiffData<Map<String, Resource>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.RESOURCE, it.key, "removed resource `${it.key}`", it.value, severity, DiffData(it.value, null)) }
    }
}

class MethodAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key).not() }.map { Diff(DiffType.ADDED, Scope.METHOD, it.key, "added method `${it.key}`", it.value, severity, DiffData(null, it.value)) }
    }
}

class MethodRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP
    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key).not() }.map { Diff(DiffType.REMOVED, Scope.METHOD, it.key, "removed method `${it.key}`", it.value, severity, DiffData(it.value, null)) }
    }
}

class PropertyAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, ObjectType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.flatMap { (typeName, objectType) ->
                objectType.allProperties.toPropertyMap().filter { data.original[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                    DiffType.ADDED, Scope.PROPERTY, propertyName, "added property `${propertyName}` to type `${typeName}`", property, severity, DiffData(null, property)) }
        }
    }
}

class PropertyRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, ObjectType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.OBJECT_TYPES_MAP
    override fun diff(data: DiffData<Map<String, ObjectType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }.flatMap { (typeName, objectType) ->
            objectType.allProperties.toPropertyMap().filter { data.changed[typeName]!!.allProperties.toPropertyMap().containsKey(it.key).not() }.map { (propertyName,property) -> Diff(
                DiffType.REMOVED, Scope.PROPERTY, propertyName, "removed property `${propertyName}` from type `${typeName}`", property, severity, DiffData(property, null)
            ) }
        }
    }
}

class QueryParameterAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP

    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.flatMap { (uri, method) ->
            method.queryParameters.toParameterMap()
                .filter { data.original[uri]!!.queryParameters.toParameterMap().contains(it.key).not() }
                .map { (parameterName, parameter) ->
                    Diff(
                        DiffType.ADDED,
                        Scope.QUERY_PARAMETER,
                        parameterName,
                        "added query parameter `${parameterName}` to method `${uri}`",
                        parameter,
                        severity,
                        DiffData(null, parameter)
                    )
                }
        }
    }
}

class QueryParameterRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, Method>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHODS_MAP

    override fun diff(data: DiffData<Map<String, Method>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }.flatMap { (uri, method) ->
            method.queryParameters.toParameterMap()
                .filter { data.changed[uri]!!.queryParameters.toParameterMap().contains(it.key).not() }
                .map { (parameterName, parameter) ->
                    Diff(
                        DiffType.REMOVED,
                        Scope.QUERY_PARAMETER,
                        parameterName,
                        "removed query parameter `${parameterName}` from method `${uri}`",
                        parameter,
                        severity,
                        DiffData(parameter, null)
                    )
                }
        }
    }
}

class PropertyTypeChangedCheck(override val severity: CheckSeverity): DiffCheck<Map<PropertyReference, Property>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.PROPERTIES_MAP

    override fun diff(data: DiffData<Map<PropertyReference, Property>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.filter { (key, property) -> property.typeName() != data.original[key]!!.typeName() }.map { (propertyRef, property) ->
            Diff(DiffType.CHANGED, Scope.PROPERTY, DiffData(data.original[propertyRef]!!.typeName(), property.typeName()), "changed property `${propertyRef.property}` of type `${propertyRef.objectType}` from type `${data.original[propertyRef]!!.typeName()}` to `${property.typeName()}`", property, severity, DiffData(data.original[propertyRef], property))
        }
    }
}

class PropertyOptionalCheck(override val severity: CheckSeverity): DiffCheck<Map<PropertyReference, Property>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.PROPERTIES_MAP

    override fun diff(data: DiffData<Map<PropertyReference, Property>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) && data.original[it.key]!!.required == true }.filter { (key, property) -> property.required != data.original[key]!!.required }.map { (propertyRef, property) ->
            Diff(DiffType.REQUIRED, Scope.PROPERTY, DiffData(data.original[propertyRef]!!.required, property.required), "changed property `${propertyRef.property}` of type `${propertyRef.objectType}` to be optional", property, severity, DiffData(data.original[propertyRef], property))
        }
    }
}
class PropertyRequiredCheck(override val severity: CheckSeverity): DiffCheck<Map<PropertyReference, Property>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.PROPERTIES_MAP

    override fun diff(data: DiffData<Map<PropertyReference, Property>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) && data.original[it.key]!!.required == false }.filter { (key, property) -> property.required != data.original[key]!!.required }.map { (propertyRef, property) ->
            Diff(DiffType.REQUIRED, Scope.PROPERTY, DiffData(data.original[propertyRef]!!.required, property.required), "changed property `${propertyRef.property}` of type `${propertyRef.objectType}` to be required", property, severity, DiffData(data.original[propertyRef], property))
        }
    }
}

class TypeChangedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP

    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.filter { (key, type) ->
            type.eClass() != data.original[key]!!.eClass() || type.typeName() != data.original[key]!!.typeName() }.map { (typeName, type) ->
                val originalTypeName = data.original[typeName]!!.typeName() ?: BuiltinType.of(data.original[typeName]!!.eClass()).map { it.getName() }.orElse(BuiltinType.ANY.getName())
                val changedTypeName = type.typeName() ?: BuiltinType.of(type.eClass()).map { it.getName() }.orElse(BuiltinType.ANY.getName())
                val diff: Diff<Any> = Diff(
                    DiffType.CHANGED,
                    Scope.TYPE,
                    DiffData(originalTypeName, changedTypeName),
                    "changed type `${typeName}` from type `${originalTypeName}` to `${changedTypeName}`",
                    type,
                    severity,
                    DiffData(data.original[typeName], type)
                )
                diff
        }
    }
}

class MarkDeprecatedAddedTypeCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP

    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }
            .filter { (key, _) ->
                data.original[key]!!.getAnnotation("markDeprecated", true)?.value?.value != true
                    && data.original[key]!!.getAnnotation("deprecated")?.value?.value != true
            }.filter { (_, type) ->
                type.getAnnotation("markDeprecated", true)?.value?.value == true
            }.map { (typeName, type) ->
                val originalAnno = data.original[typeName]!!.getAnnotation("markDeprecated", true)
                val changedAnno = type.getAnnotation("markDeprecated", true)
                Diff(
                    DiffType.MARK_DEPRECATED,
                    Scope.TYPE,
                    DiffData(originalAnno?.value?.value, changedAnno.value.value),
                    "marked type `${typeName}` as deprecated",
                    type,
                    severity,
                    DiffData(data.original[typeName], type)
                )
            }
    }
}

class MarkDeprecatedRemovedTypeCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP

    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }
            .filter { (key, _) ->
                data.changed[key]!!.getAnnotation("markDeprecated", true)?.value?.value != true
                    && data.changed[key]!!.getAnnotation("deprecated")?.value?.value != true
            }.filter { (_, type) ->
                type.getAnnotation("markDeprecated", true)?.value?.value == true
            }.map { (typeName, type) ->
                val originalAnno = data.original[typeName]!!.getAnnotation("markDeprecated", true)
                val changedAnno = type.getAnnotation("markDeprecated", true)
                Diff(
                    DiffType.MARK_DEPRECATED,
                    Scope.TYPE,
                    DiffData(originalAnno?.value?.value, changedAnno.value.value),
                    "removed deprecation mark from type `${typeName}`",
                    type,
                    severity,
                    DiffData(type, data.changed[typeName])
                )
            }
    }
}

class DeprecatedRemovedTypeCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP

    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }
            .filter { (key, _) ->
                data.changed[key]!!.getAnnotation("deprecated", true)?.value?.value != true
            }.filter { (_, type) ->
                type.getAnnotation("deprecated", true)?.value?.value == true
            }.map { (typeName, type) ->
                val originalAnno = data.original[typeName]!!.getAnnotation("deprecated", true)
                val changedAnno = type.getAnnotation("deprecated", true)
                Diff(
                    DiffType.DEPRECATED,
                    Scope.TYPE,
                    DiffData(originalAnno?.value?.value, changedAnno.value.value),
                    "removed deprecation from type `${typeName}`",
                    type,
                    severity,
                    DiffData(type, data.changed[typeName])
                )
            }
    }
}

class DeprecatedAddedTypeCheck(override val severity: CheckSeverity): DiffCheck<Map<String, AnyType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.ANY_TYPES_MAP

    override fun diff(data: DiffData<Map<String, AnyType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }
            .filter { (key, _) ->
                data.original[key]!!.getAnnotation("deprecated", true)?.value?.value != true
            }.filter { (_, type) ->
                type.getAnnotation("deprecated", true)?.value?.value == true
            }.map { (typeName, type) ->
                val originalAnno = data.original[typeName]!!.getAnnotation("deprecated", true)
                val changedAnno = type.getAnnotation("deprecated", true)
                Diff(
                    DiffType.DEPRECATED,
                    Scope.TYPE,
                    DiffData(originalAnno?.value?.value, changedAnno.value.value),
                    "type `${typeName}` is deprecated",
                    type,
                    severity,
                    DiffData(data.original[typeName], type)
                )
            }
    }
}

class EnumAddedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, StringType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.STRING_TYPES_MAP
    override fun diff(data: DiffData<Map<String, StringType>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.flatMap { (typeName, stringType) ->
            stringType.enum.toInstanceMap().filter { data.original[typeName]!!.enum.toInstanceMap().containsKey(it.key).not() }.map { (enumValue, instance) -> Diff(
                DiffType.ADDED, Scope.ENUM, enumValue, "added enum `${enumValue}` to type `${typeName}`", instance, severity, DiffData(data.original[typeName], stringType)) }
        }
    }
}

class EnumRemovedCheck(override val severity: CheckSeverity): DiffCheck<Map<String, StringType>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.STRING_TYPES_MAP
    override fun diff(data: DiffData<Map<String, StringType>>): List<Diff<Any>> {
        return data.original.filter { data.changed.containsKey(it.key) }.flatMap { (typeName, stringType) ->
            stringType.enum.toInstanceMap().filter { data.changed[typeName]!!.enum.toInstanceMap().containsKey(it.key).not() }.map { (enumValue, instance) -> Diff(
                DiffType.REMOVED, Scope.ENUM, enumValue, "removed enum `${enumValue}` from type `${typeName}`", instance, severity, DiffData(stringType, data.changed[typeName])) }
        }
    }
}

class MethodBodyTypeChangedCheck(override val severity: CheckSeverity): DiffCheck<Map<MethodBodyTypeReference, Body>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHOD_TYPES_MAP

    override fun diff(data: DiffData<Map<MethodBodyTypeReference, Body>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.filter { (key, body) -> body.type.name != data.original[key]!!.type.name }.map { (methodRef, body) ->
            Diff(DiffType.CHANGED, Scope.METHOD_BODY, DiffData(data.original[methodRef]!!.type.name, body.type.name), "changed body for `${methodRef.mediaType}` of method `${methodRef.method} ${methodRef.fullUri}` from type `${data.original[methodRef]!!.type.name}` to `${body.type.name}`", body, severity, DiffData(data.original[methodRef], body))
        }
    }
}

class MethodResponseBodyTypeChangedCheck(override val severity: CheckSeverity): DiffCheck<Map<MethodResponseBodyTypeReference, Body>>(severity) {
    override val diffDataType: DiffDataType = DiffDataType.METHOD_RESPONSE_TYPES_MAP

    override fun diff(data: DiffData<Map<MethodResponseBodyTypeReference, Body>>): List<Diff<Any>> {
        return data.changed.filter { data.original.containsKey(it.key) }.filter { (key, body) -> body.type.name != data.original[key]!!.type.name }.map { (methodResponseRef, body) ->
            Diff(DiffType.CHANGED, Scope.METHOD_RESPONSE_BODY, DiffData(data.original[methodResponseRef]!!.type.name, body.type.name), "changed response body for `${methodResponseRef.status}: ${methodResponseRef.mediaType}` of method `${methodResponseRef.method} ${methodResponseRef.fullUri}` from type `${data.original[methodResponseRef]!!.type.name}` to `${body.type.name}`", body, severity, DiffData(data.original[methodResponseRef], body))
        }
    }
}
