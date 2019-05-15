package io.vrap.codegen.languages.typescript.client

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class TsClientFileProducer @Inject constructor(@Named(VrapConstants.CLIENT_PACKAGE_NAME) val clientPackage : String ): FileProducer{

    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile{
        var content = """
            |
            |type HttpMethod =
            |  | "GET"
            |  | "HEAD"
            |  | "POST"
            |  | "PUT"
            |  | "DELETE"
            |  | "CONNECT"
            |  | "OPTIONS"
            |  | "TRACE";
            |
            |export interface CommonRequest<T, R> {
            |  method: HttpMethod;
            |  uriTemplate: string;
            |  pathVariables?: { [key: string]: string | number | boolean };
            |  queryParams?: { [key: string]: string | number | boolean };
            |  payload?: T;
            |}
            |
        """.trimMargin()


        return TemplateFile(content = content,relativePath = "base/requests-utils.ts")

    }
}