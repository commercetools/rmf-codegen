package io.vrap.rmf.codegen.kt.types

import com.google.common.base.CaseFormat
import com.google.inject.Inject
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch

class ResourcesTypeSwitch @Inject constructor( val packageSwitch: PackageSwitch) : ResourcesSwitch<VrapType>() {

    private val classNameMapper = CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL)

    override fun caseResource(resource: Resource): VrapType {
        return VrapObjectType(packageSwitch.doSwitch(resource), "${classNameMapper.convert(resource.resourcePathName)}Requests")
    }

}
