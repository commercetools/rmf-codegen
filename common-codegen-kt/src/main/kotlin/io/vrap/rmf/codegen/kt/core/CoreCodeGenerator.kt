package io.vrap.rmf.codegen.kt.core

import com.google.inject.Inject
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.kt.io.DataSink
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringType

class CoreCodeGenerator @Inject constructor(private val allObjectTypes: MutableList<ObjectType>
                                            ,private val allStringTypes :MutableList<StringType>
                                            , private val allResourceCollections: MutableList<ResourceCollection>) {

    @Inject(optional = true)
    lateinit var objectTypeGenerators: MutableSet<ObjectTypeRenderer>

    @Inject(optional = true)
    lateinit var fileProducers: MutableSet<FileProducer>

    fun generate() {

        if (::objectTypeGenerators.isInitialized) {
            objectTypeGenerators.flatMap { objectTypeRenderer ->
                allObjectTypes.map { objectTypeRenderer.render(it) }
            }.map { DataSink.save(it) }
        }


        if(::fileProducers.isInitialized){
            fileProducers.flatMap { it.produceFiles() }.map { DataSink.save(it) }
        }
    }

}