package io.vrap.rmf.codegen.common.generator.core

import io.vrap.rmf.codegen.kt.types.VrapType
import io.vrap.rmf.raml.model.resources.Resource

class ResourceCollection(val className: VrapType, val resources: List<Resource>) {

    val sample: Resource
        get() = resources[0]

    init {
        if (resources.isEmpty()) {
            throw IllegalArgumentException("The resource collection is supposed to be non empty")
        }
    }

    override fun toString(): String {
        return "ResourceCollection(className=$className, resources=$resources)"
    }


}