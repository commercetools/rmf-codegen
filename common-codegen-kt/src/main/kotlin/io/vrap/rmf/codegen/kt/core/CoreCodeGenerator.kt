package io.vrap.rmf.codegen.kt.core

import com.google.inject.Inject
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.codegen.kt.io.DataSink
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ObjectType

class CoreCodeGenerator @Inject constructor(private val allTypes: MutableList<AnyType>
                                            , private val allResourceCollection: MutableList<ResourceCollection>) {

    @Inject(optional = true)
    lateinit var objectTypeGenerators: MutableSet<ObjectTypeRenderer>

    fun generate() {
        if (::objectTypeGenerators.isInitialized) {
            objectTypeGenerators.flatMap { objectTypeRenderer ->
                allTypes.filter { it is ObjectType }.map { it as ObjectType }.map { objectTypeRenderer.render(it) }
            }.map { DataSink.save(it) }
        }
    }

}