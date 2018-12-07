package io.vrap.rmf.codegen.rendring

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType
import org.slf4j.LoggerFactory

class CoreCodeGenerator @Inject constructor(val dataSink: DataSink
                                            ,private val allObjectTypes: MutableList<ObjectType>
                                            ,private val allStringTypes : MutableList<StringType>
                                            ,private val allResourceCollections: MutableList<ResourceCollection>
                                            ,private val allResourceMethods: MutableList<Method>
                                            ) {

    private val LOGGER = LoggerFactory.getLogger(CoreCodeGenerator::class.java)

    @Inject(optional = true)
    lateinit var objectTypeGenerators: MutableSet<ObjectTypeRenderer>

    @Inject(optional = true)
    lateinit var stringTypeGenerators: MutableSet<StringTypeRenderer>

    @Inject(optional = true)
    lateinit var allResourcesGenerators: MutableSet<ResourceCollectionRenderer>

    @Inject(optional = true)
    lateinit var allResourceMethodGenerators: MutableSet<MethodRenderer>

    @Inject(optional = true)
    lateinit var fileProducers: MutableSet<FileProducer>

    fun generate() {

        if(dataSink.clean()){
            LOGGER.info("data sink cleanup successful")
        } else {
            LOGGER.info("data sink cleanup unsuccessful")
        }

        if (::objectTypeGenerators.isInitialized) {
            LOGGER.info("generating files for object types")
            objectTypeGenerators.flatMap { objectTypeRenderer ->
                allObjectTypes.map { objectTypeRenderer.render(it) }
            }.map { dataSink.write(it) }
        }

        if (::stringTypeGenerators.isInitialized) {
            LOGGER.info("generating files for string types")
            stringTypeGenerators.flatMap { stringTypeRenderer ->
                allStringTypes.map { stringTypeRenderer.render(it) }
            }.map { dataSink.write(it) }
        }

        if (::allResourcesGenerators.isInitialized) {
            LOGGER.info("generating files for resource collections")
            allResourcesGenerators.flatMap { resCollectionRenderer ->
                allResourceCollections.map { resCollectionRenderer.render(it) }
            }.map { dataSink.write(it) }
        }

        if (::allResourceMethodGenerators.isInitialized) {
            LOGGER.info("generating files for resource methods")
            allResourceMethodGenerators.flatMap { resMethodRenderer ->
                allResourceMethods.map { resMethodRenderer.render(it) }
            }.map { dataSink.write(it) }
        }

        if(::fileProducers.isInitialized){
            LOGGER.info("generating types for file producers")
            fileProducers.flatMap { it.produceFiles() }.map { dataSink.write(it) }
        }

        LOGGER.info("files generation ended")
    }

}
