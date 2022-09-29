package io.vrap.rmf.codegen.cli.diff

import com.fasterxml.jackson.annotation.JsonIgnore
import org.eclipse.emf.ecore.EObject

data class Diff<T>(
    val diffType: DiffType,
    val scope: Scope,
    val value: T,
    val message: String,
    @get:JsonIgnore val eObject: EObject,
    val source: Source? = eObject.getSource()
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
    CHANGED("changed")
}

enum class Scope(val scope: String) {
    RESOURCE("resource"),
    METHOD("method"),
    TYPE("type"),
    PROPERTY("property"),
    URI_PARAMETER("uriParameter"),
    QUERY_PARAMETER("queryParameter"),
    ENUM("enum")
}

data class DiffData<T>(
    val original: T,
    val changed: T,
)

enum class DiffDataType {
    API,
    RESOURCES,
    RESOURCES_MAP,
    TYPES,
    TYPES_MAP,
    METHODS,
    METHODS_MAP,
    OBJECT_TYPES,
    OBJECT_TYPES_MAP,
    PROPERTIES_MAP,
}
