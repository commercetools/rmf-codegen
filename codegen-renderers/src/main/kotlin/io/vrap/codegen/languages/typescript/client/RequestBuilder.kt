package io.vrap.codegen.languages.typescript.client

import com.google.inject.Inject
import io.vrap.codegen.languages.typescript.client.files_producers.apiRequest
import io.vrap.codegen.languages.typescript.client.files_producers.commonRequest
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource


class RequestBuilder @Inject constructor(
        api:Api,
        vrapTypeProvider: VrapTypeProvider
) : ResourceRenderer, AbstractRequestBuilder(api,vrapTypeProvider) {

    override fun render(type: Resource): TemplateFile {

        val pakage = (type.toVrapType() as VrapObjectType).`package`

        return TemplateFile(
                relativePath = type.tsRequestModuleName(pakage).replace(".", "/") + ".ts",
                content = """|
                |${type.imports(type.tsRequestModuleName(pakage))}
                |import { ${commonRequest.simpleClassName} } from '${relativizePaths(type.tsRequestModuleName(pakage), commonRequest.`package`)}'
                |import { ${apiRequest.simpleClassName} } from '${relativizePaths(type.tsRequestModuleName(pakage), apiRequest.`package`)}'
                |
                |export class ${type.toRequestBuilderName()} {
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



