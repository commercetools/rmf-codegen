package io.vrap.rmf.codegen.types

import com.google.common.base.CaseFormat
import com.google.inject.Inject
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch

class ResourcesTypeProvider @Inject constructor(val packageProvider: PackageProvider) : ResourcesSwitch<VrapType>() {

    private val classNameMapper = CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL)

    override fun caseResource(resource: Resource): VrapType {
        return VrapObjectType(packageProvider.doSwitch(resource), "${classNameMapper.convert(resource.resourcePathName)}Requests")
    }

    override fun caseMethod(method: Method): VrapType {
        val resource = method.eContainer() as Resource
        return VrapObjectType(packageProvider.doSwitch(method), "${classNameMapper.convert(resource.resourcePathName)}${method.methodName.capitalize()}")
    }
}
