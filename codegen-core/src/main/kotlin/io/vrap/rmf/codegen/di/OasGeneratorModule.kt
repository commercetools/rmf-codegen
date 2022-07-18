package io.vrap.rmf.codegen.di

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.ComposedSchema
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.AbstractMap.SimpleEntry

class OasGeneratorModule constructor(
    override val apiProvider: OasProvider,
    override val generatorConfig: io.vrap.rmf.codegen.CodeGeneratorConfig,
    override val languageBaseTypes: LanguageBaseTypes,
    override val defaultPackage: String = "io/vrap/rmf",
    override val dataSink: DataSink =  FileDataSink(generatorConfig.outputFolder)
): GeneratorModule {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(OasGeneratorModule::class.java)
    }

    @OasApi
    fun provideOasModel(): OpenAPI = apiProvider.api

    @AllSchemaTypes
    fun allAnyTypes(): List<Map.Entry<String, Schema<Any>>> {
        val openAPI = provideOasModel()
        return openAPI.components.schemas.toMap().entries.toList()
    }

    @AllObjectSchemaTypes
    fun allObjectTypes(): List<Map.Entry<String, ObjectSchema>> = allAnyTypes()
        .filter { it.value is ObjectSchema }
        .map { SimpleEntry(it.key, it.value as ObjectSchema) }

    @AllStringSchemaTypes
    fun allStringTypes(): List<Map.Entry<String, StringSchema>> = allAnyTypes()
        .filter { it.value is StringSchema }
        .map { SimpleEntry(it.key, it.value as StringSchema) }

    @AllComposedSchemaTypes
    fun allComposedTypes(): List<Map.Entry<String, ComposedSchema>> = allAnyTypes()
        .filter { it.value is ComposedSchema }
        .map { SimpleEntry(it.key, it.value as ComposedSchema) }

    @AllPathItems
    fun allPathItems(): List<Map.Entry<String, PathItem>> = provideOasModel().paths.entries.toList()

    fun providePackageName(): String {
        val defaultPackage = defaultPackage();
        val api = provideOasModel();
        if (generatorConfig.basePackageName == null && api.servers == null) {
            LOGGER.warn("Could not find proper package name configuration. Using default $defaultPackage")
            return defaultPackage
        }
        return generatorConfig.basePackageName ?: return try {
            URI(api.servers.get(0).url).host.split(".").reversed().joinToString("/")
        } catch (e: Exception) {
            LOGGER.warn("Error while trying to extract base package from url, resolving to default package '$defaultPackage'",e)
            defaultPackage
        }
    }

    fun provideModelPackageName(): String {
        val basePackageName = providePackageName()
        return generatorConfig.modelPackage
            ?: if (basePackageName.isBlank()) "models" else "$basePackageName/models"
    }
}
