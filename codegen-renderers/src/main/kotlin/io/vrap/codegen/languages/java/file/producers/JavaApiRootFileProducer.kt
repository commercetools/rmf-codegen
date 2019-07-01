package io.vrap.codegen.languages.java.file.producers

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api

class JavaApiRootFileProducer @Inject constructor(@Named(VrapConstants.CLIENT_PACKAGE_NAME) val clientPackage: String, val api: Api, val vrapTypeProvider: VrapTypeProvider) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return emptyList()
    }
}