package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.Inject
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource

class RamlResourceRenderer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : ResourceRenderer {
    override fun render(type: Resource): TemplateFile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
