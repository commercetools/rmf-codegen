package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.typescript.client.AbstractRequestBuilder
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource

class ApiRootFileProducer @Inject constructor(
        @Named(VrapConstants.CLIENT_PACKAGE_NAME) val client_package: String,
        api: Api,
        vrapTypeProvider: VrapTypeProvider
) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider) {


    val rootResources = api.resources

    override fun produceFiles(): List<TemplateFile> {

        return listOf(produceApiRoot(rootResources[0]))
    }

    fun produceApiRoot(type: Resource): TemplateFile {
        val modeuleName = "$client_package.ApiRoot"
        return TemplateFile(
                relativePath = modeuleName.replace(".", "/") + ".ts",
                content = """|
                |${type.imports(modeuleName)}
                |import { ApiRequest } from '${relativizePaths(modeuleName,"base.requests-utils")}'
                |
                |export class ApiRoot {
                |
                |    <${type.constructor()}>
                |    <${type.subResources()}>
                |    <${type.methods()}>
                |
                |}
                |
            """.trimMargin().keepIndentation()

        )

    }
}