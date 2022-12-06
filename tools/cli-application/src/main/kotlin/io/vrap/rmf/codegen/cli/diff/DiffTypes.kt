package io.vrap.rmf.codegen.cli.diff

import com.fasterxml.jackson.annotation.JsonIgnore
import org.eclipse.emf.ecore.EObject

data class Diff<T>(
    val diffType: DiffType,
    val scope: Scope,
    val value: T,
    val message: String,
    @get:JsonIgnore val eObject: EObject,
    val severity: CheckSeverity = CheckSeverity.INFO,
    @get:JsonIgnore val diffEObject: DiffData<EObject?>,
    val source: Source? = eObject.getSource(),
)

data class Source(val location: String, val position: Position) {
    override fun toString(): String {
        return "Source(location='$location', line=${position.line}, charPositionInLine=${position.charPositionInLine})"
    }
}

data class Position(var line: Long, val charPositionInLine: Long)

enum class DiffType(val type: String) {
    ADDED("added"),
    REMOVED("removed"),
    CHANGED("changed"),
    DEPRECATED("deprecated"),
    MARK_DEPRECATED("markDeprecated"),
    REQUIRED("required")
}

enum class Scope(val scope: String) {
    RESOURCE("resource"),
    METHOD("method"),
    TYPE("type"),
    PROPERTY("property"),
    URI_PARAMETER("uriParameter"),
    QUERY_PARAMETER("queryParameter"),
    ENUM("enum"),
    METHOD_BODY("methodBody"),
    METHOD_RESPONSE_BODY("methodResponseBody"),
}

data class DiffData<T>(
    val original: T,
    val changed: T,
)

enum class DiffDataType {
    API,
    RESOURCES,
    RESOURCES_MAP,
    ANY_TYPES,
    ANY_TYPES_MAP,
    STRING_TYPES,
    STRING_TYPES_MAP,
    METHODS,
    METHODS_MAP,
    OBJECT_TYPES,
    OBJECT_TYPES_MAP,
    PROPERTIES_MAP,
    METHOD_TYPES_MAP,
    METHOD_RESPONSE_TYPES_MAP,
}

data class PropertyReference(
    val objectType: String,
    val property: String
)

data class MethodBodyTypeReference(
    val fullUri: String,
    val method: String,
    val mediaType: String
)

data class MethodResponseBodyTypeReference(
    val fullUri: String,
    val method: String,
    val status: String,
    val mediaType: String
)
