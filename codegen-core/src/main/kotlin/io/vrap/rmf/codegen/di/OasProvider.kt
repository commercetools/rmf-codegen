package io.vrap.rmf.codegen.di

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path


class OasProvider constructor(private val oasFileLocation: Path): ApiProvider {

    private val logger : Logger = LoggerFactory.getLogger(RamlApiProvider::class.java)

    override val gitHash: String = ""

    val api: OpenAPI by lazy {
        val parseOptions = ParseOptions()
        parseOptions.isResolve = true
        parseOptions.isResolveFully = true
        val result = OpenAPIParser().readLocation(oasFileLocation.toAbsolutePath().toString(), null, parseOptions)

        val openAPI = result.openAPI

        openAPI
    }
}
