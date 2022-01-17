package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.executeAndMeasureTimeMillis
import io.vrap.rmf.codegen.toSeconds
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.modules.Api
import org.eclipse.emf.common.util.URI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Path

class RamlApiProvider constructor(private val ramlFileLocation: Path): ApiProvider {

    private val logger : Logger = LoggerFactory.getLogger(RamlApiProvider::class.java)

    override val gitHash: String by lazy {
        try {
            Runtime.getRuntime().exec("git -C ${ramlFileLocation.parent} rev-parse HEAD").inputStream.bufferedReader().readLine().orEmpty()
        } catch (e: IOException) {
            logger.info("Couldn't retrieve git hash for {}", ramlFileLocation.parent, e)
            ""
        }
    }

    val api: Api by lazy {
        val fileURI = URI.createURI(ramlFileLocation.toUri().toString())
        val (modelResult, duration) = executeAndMeasureTimeMillis {
            RamlModelBuilder().buildApi(fileURI)
        }
        logger.info("Parsing API took ${duration.toSeconds(3)}s")
        val validationResults = modelResult.validationResults
        if (!validationResults.isEmpty()) {
            logger.warn("Error(s) found validating ${fileURI.toFileString()}:")
            validationResults.stream().forEach { logger.warn(it.toString()) }
            throw RuntimeException("Error while parsing file ${fileURI.toFileString()}")
        }
        modelResult.rootObject
    }
}
