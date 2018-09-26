package io.vrap.rmf.codegen.kt.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
import io.vrap.rmf.codegen.kt.types.LanguageBaseTypes
import io.vrap.rmf.codegen.kt.types.VrapTypeSwitch
import io.vrap.rmf.codegen.kt.types.VrapType
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.elements.NamedElement
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch
import org.slf4j.LoggerFactory


class GeneratorModule constructor(
        @get:Provides val generatorConfig: CodeGeneratorConfig,
        @get:Provides val languageBaseTypes: LanguageBaseTypes) : AbstractModule() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GeneratorModule::class.java)
    }

    private val filterSwitch = FilterSwitch()

    @Provides
    @Singleton
    fun customTypeMapping(): Map<String, VrapType> = generatorConfig.customTypeMapping

    @Provides
    @Singleton
    @Named(VrapConstants.PACKAGE_NAME)
    fun providePackageName(): String = generatorConfig.packagePrefix


    @Provides
    @Singleton
    fun provideRamlModel(): Api {
        val modelResult = RamlModelBuilder().buildApi(generatorConfig.ramlFileLocation)
        val validationResults = modelResult.validationResults
        if (!validationResults.isEmpty()) {
            validationResults.stream().forEach { validationResult -> LOGGER.warn("Error encountered while checking Raml API " + validationResult.toString()) }
            throw RuntimeException("Error while paring raml input")
        }
        return modelResult.rootObject
    }

    @Provides
    @Singleton
    fun provideRamlEntitiesObjects(ramlApi: Api): List<AnyType> {
        val result = mutableListOf<AnyType>()
        ramlApi.types?.forEach { result.add(it) }
        ramlApi.uses?.flatMap { it.library.types }?.forEach { result.add(it) }
        return result.filter { filterSwitch.doSwitch(it) }
    }

    @Provides
    @Singleton
    fun allResources(ramlApi: Api): List<Resource> = ramlApi.allContainedResources.filter { filterSwitch.doSwitch(it) }


    @Provides
    @Singleton
    fun resourceCollection(resources: MutableList<Resource>, vrapTypeSwitch: VrapTypeSwitch): List<ResourceCollection> = resources.groupBy { vrapTypeSwitch.doSwitch(it) }
                                                                                                                            .map { entry: Map.Entry<VrapType, List<Resource>> -> ResourceCollection(entry.key, entry.value) }
                                                                                                                            .toList()


    private inner class FilterSwitch : ComposedSwitch<Boolean>() {

        init {
            addSwitch(FilterTypeSwitch())
            addSwitch(FilterResourcesSwitch())
        }

        /**
         * This filter is used to filter files that are explicitly provided by the sdk developer
         */
        private inner class FilterTypeSwitch : TypesSwitch<Boolean>() {
            override fun caseNamedElement(`object`: NamedElement): Boolean = generatorConfig.customTypeMapping[`object`.name]?.let { false }
                    ?: true

            override fun caseStringType(stringType: StringType): Boolean = stringType.enum?.isNotEmpty() ?: false
            override fun defaultCase(`object`: EObject?): Boolean? = false
        }

        private inner class FilterResourcesSwitch : ResourcesSwitch<Boolean>() {
            override fun caseResource(resource: Resource): Boolean = resource.resourcePathName?.isNotBlank() ?: false
            override fun defaultCase(`object`: EObject?): Boolean = false
        }
    }

}
