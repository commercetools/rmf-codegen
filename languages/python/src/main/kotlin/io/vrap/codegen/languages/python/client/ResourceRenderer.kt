/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.client

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.python.snakeCase
import io.vrap.codegen.languages.python.toDocString
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.raml.model.resources.ResourceContainer

fun ResourceContainer.subResources(clientName: String): String {
    return this.resources
        .map {
            var args = if (!it.relativeUri.variables.isNullOrEmpty()) {
                "self, " +
                    it.relativeUri.variables
                        .map { it.snakeCase() }
                        .map { "$it: str" }
                        .joinToString(separator = ", ")
            } else { "self" }

            val assignments =
                it.relativeUri.variables
                    .map { it.snakeCase() }
                    .map { "$it=$it," }
                    .plus(
                        (it.fullUri.variables.asList() - it.relativeUri.variables.asList())
                            .map { it.snakeCase() }
                            .map { "$it=self._$it," }
                    )
                    .joinToString(separator = "\n")

            """
            |def ${it.getMethodName().snakeCase()}($args) -\> ${it.toRequestBuilderName()}:
            |    <${it.toDocString().escapeAll()}>
            |    return ${it.toRequestBuilderName()}(
            |        <$assignments>
            |        client=$clientName,
            |    )
            |
            """.trimMargin().keepIndentation()
        }.joinToString(separator = "\n")
}
