package io.vrap.codegen.languages.go.client

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.go.*
import io.vrap.codegen.languages.go.exportName
import io.vrap.codegen.languages.go.snakeCase
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType

fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"

fun Resource.toStructName(): String {
    return this.toRequestBuilderName().exportName()
}

fun Method.toStructName(): String {
    return "${this.resource().toResourceName()}RequestMethod${this.methodName.exportName()}".exportName()
}

fun Resource.goClientFileName(): String {
    return listOf<String>(
        "client",
        resourcePathName.snakeCase(),
        this.toResourceName().snakeCase()
    ).filter { x -> x != "" }.joinToString(separator = "_")
}

fun Method.goClientFileName(): String {
    return listOf<String>(
        "client",
        resource().resourcePathName.snakeCase(),
        "${this.resource().toResourceName()}${this.methodName.exportName()}".snakeCase()
    ).filter { x -> x != "" }.joinToString(separator = "_")
}

fun Method.bodyType(): AnyType? {
    if (bodies.isNotEmpty()) {
        return bodies[0].type
    }
    return null
}
