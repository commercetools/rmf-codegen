package io.vrap.rmf.codegen.cli

import org.eclipse.emf.ecore.EObject

data class Diff<T>(val diffType: DiffType, val scope: Scope, val value: T, val message: String, val eObject: EObject, val source: Source? = null)

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
    TYPE("type"),
    PROPERTY("property"),
    URI_PARAMETER("uriParameter"),
    QUERY_PARAMETER("queryParameter"),
    ENUM("enum")
}
