package io.vrap.rmf.codegen.di

import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import org.eclipse.emf.common.util.URI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

class ApiProvider constructor(private val ramlFileLocation: Path) {

    private val logger : Logger = LoggerFactory.getLogger(ApiProvider::class.java)

    val api: Api by lazy {
        val fileURI = URI.createURI(ramlFileLocation.toUri().toString())
        val modelResult = RamlModelBuilder().buildApi(fileURI)
        val validationResults = modelResult.validationResults
        if (!validationResults.isEmpty()) {
            validationResults.stream().forEach { validationResult -> logger.warn("Error encountered while checking Raml API " + validationResult.toString()) }
            throw RuntimeException("Error while paring raml input")
        }
        modelResult.rootObject
    }
}
