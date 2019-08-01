package io.vrap.codegen.languages.typescript.server

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class ServerHelpers @Inject constructor(
        @ClientPackageName val client_package: String,
        val constantsProvider: ConstantsProvider
) : FileProducer {

    override fun produceFiles(): List<TemplateFile> = listOf(commonFile())


    fun commonFile(): TemplateFile {
        val moduleName = constantsProvider.commonModule
        return TemplateFile(
                content = """
export type MethodType =
  | "GET"
  | "HEAD"
  | "POST"
  | "PUT"
  | "DELETE"
  | "CONNECT"
  | "OPTIONS"
  | "TRACE";

export type VariableMap =  { [key: string]: string | number | boolean | undefined }

export type HttpResponse = {
    body?: any,
    headers?: {(key:string):string}
    statusCode: number
}

export type HttpInput = {
    body: any,
    headers: {(key:string):string}
    pathParams: {(key:string):string}
    queryParams: {(key:string):VariableMap}
}

export type Resource = {
    uri: string
    method: MethodType
    handler: (input:HttpInput) => HttpResponse
}
""".trimIndent(),
                relativePath = "$moduleName.ts"
        )
    }
}