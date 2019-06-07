package io.vrap.codegen.languages.typescript.client

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.resource
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.tsMediaType
import io.vrap.codegen.languages.typescript.tsRemoveRegexp
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.codegen.languages.typescript.tsRequestName
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ResourceRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Resource
import java.nio.file.Path
import java.nio.file.Paths


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
                |import { ApiRequest } from '${relativizePaths(type.tsRequestModuleName(pakage),"base.requests-utils")}'
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



