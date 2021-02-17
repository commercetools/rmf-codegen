/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toRequestName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.python.snakeCase
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.StringType

fun Method.pyMediaType(): String {

    val headers = mutableMapOf<String, List<String>>()
    if (!this.bodies.isNullOrEmpty() && !this.bodies[0].contentMediaType.type().isNullOrEmpty()) {
        headers["Content-Type"] = listOf(this.bodies[0].contentMediaType.toString())
    }
    this.headers
        .map {
            it.name to (it.type as StringType).enum.map { it.value as String }
        }
        .toMap(headers)

    return headers
        .filter { it.value.size == 1 }
        .map {
            "'${it.key}': '${it.value[0]}'"
        }.joinToString(separator = "\n")
}

fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"

fun Method.pyRequestModuleName(clientPackageName: String): String {
    return listOf<String>(
        clientPackageName,
        this.resource().resourcePathName,
        "${this.toRequestName()}Request"
    )
        .filter { !it.isNullOrEmpty() }
        .map { it.snakeCase() }
        .joinToString(separator = ".")
}

fun Resource.pyRequestModuleName(clientPackageName: String): String {
    return listOf<String>(
        clientPackageName,
        resourcePathName,
        this.toRequestBuilderName()
    )
        .filter { !it.isNullOrEmpty() }
        .map { it.snakeCase() }
        .joinToString(separator = ".")
}

fun Resource.pyRequestVrapType(clientPackageName: String): VrapObjectType {
    return VrapObjectType(
        `package` = this.pyRequestModuleName(clientPackageName),
        simpleClassName = this.toRequestBuilderName()
    )
}
