package io.vrap.rmf.codegen.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.RamlModelBuilder
import io.vrap.rmf.raml.model.elements.NamedElement
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.ComposedSwitch
import org.slf4j.LoggerFactory
import java.net.URI
import java.nio.file.Path

@SuppressWarnings("unused")
class GeneratorModule constructor(
        @get:Provides val generatorConfig: io.vrap.rmf.codegen.CodeGeneratorConfig,
        @get:Provides val languageBaseTypes: LanguageBaseTypes) : AbstractModule() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(io.vrap.rmf.codegen.di.GeneratorModule::class.java)
    }

    private val filterSwitch = UserProvidedResourcesFilterSwitch()


    @Provides
    @Singleton
    @Named(io.vrap.rmf.codegen.di.VrapConstants.OUTPUT_FOLDER)
    fun outpuFolder():Path = generatorConfig.outputFolder

    @Provides
    @Singleton
    fun dataSink(@Named(io.vrap.rmf.codegen.di.VrapConstants.OUTPUT_FOLDER) path: Path):DataSink = FileDataSink(path)

    @Provides
    @Singleton
    fun customTypeMapping(): Map<String, VrapType> = generatorConfig.customTypeMapping

    @Provides
    @Singleton
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    fun providePackageName(api: Api): String {
        if (generatorConfig.basePackageName == null && api.baseUri == null) {
            io.vrap.rmf.codegen.di.GeneratorModule.Companion.LOGGER.warn("Could not find proper package name configuration. Using default ${io.vrap.rmf.codegen.di.VrapConstants.PACKAGE_DEFAULT}")
            return io.vrap.rmf.codegen.di.VrapConstants.PACKAGE_DEFAULT
        }
        return generatorConfig.basePackageName?: URI(api.baseUri.expand()).host.split(".").reversed().joinToString(".")
    }

    @Provides
    @Singleton
    @Named(io.vrap.rmf.codegen.di.VrapConstants.MODEL_PACKAGE_NAME)
    fun provideModelPackageName(@Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME) basePackageName:String): String = generatorConfig.modelPackage?: "$basePackageName.models"

    @Provides
    @Singleton
    @Named(io.vrap.rmf.codegen.di.VrapConstants.CLIENT_PACKAGE_NAME)
    fun provideClientPackageName(@Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME) basePackageName:String): String = generatorConfig.clientPackage?: "$basePackageName.client"

    @Provides
    @Singleton
    fun provideRamlModel(): Api {
        val modelResult = RamlModelBuilder().buildApi(generatorConfig.ramlFileLocation)
        val validationResults = modelResult.validationResults
        if (!validationResults.isEmpty()) {
            validationResults.stream().forEach { validationResult -> io.vrap.rmf.codegen.di.GeneratorModule.Companion.LOGGER.warn("Error encountered while checking Raml API " + validationResult.toString()) }
            throw RuntimeException("Error while paring raml input")
        }
        return modelResult.rootObject
    }

    @Provides
    @Singleton
    fun allAnyTypes(ramlApi: Api): List<AnyType> {
        val result = mutableListOf<AnyType>()
        ramlApi.types?.forEach { result.add(it) }
        ramlApi.uses?.flatMap { it.library.types }?.forEach { result.add(it) }
        return result.filter { filterSwitch.doSwitch(it) }
    }

    @Provides
    @Singleton
    fun allObjectTypes(anyTypeList: MutableList<AnyType>):List<ObjectType> = anyTypeList.filter { it is ObjectType }.map { it as ObjectType }

    @Provides
    @Singleton
    fun allStringTypes(anyTypeList: MutableList<AnyType>):List<StringType> = anyTypeList.filter { it is StringType }.map { it as StringType }

    @Provides
    @Singleton
    fun allResources(ramlApi: Api): List<Resource> = ramlApi.allContainedResources.filter { filterSwitch.doSwitch(it) }

    @Provides
    @Singleton
    fun allResourceMethods(ramlApi: Api): List<Method> = ramlApi.allContainedResources.flatMap { it.methods }

    @Provides
    @Singleton
    fun resourceCollection(resources: MutableList<Resource>, vrapTypeProvider: VrapTypeProvider): List<ResourceCollection> {
        return resources.groupBy { (vrapTypeProvider.doSwitch(it) as VrapObjectType).simpleClassName }
                .map { entry: Map.Entry<String, List<Resource>> ->
                    ResourceCollection(vrapTypeProvider.doSwitch(entry.value[0]), entry.value)
                }
                .toList()
    }


    /**
     * This filter is used to filter files that are explicitly provided by the sdk developer
     */
    private inner class UserProvidedResourcesFilterSwitch : ComposedSwitch<Boolean>() {

        init {
            addSwitch(FilterTypeSwitch())
            addSwitch(FilterResourcesSwitch())
        }

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
