package io.vrap.codegen.languages.python.server

import com.google.inject.Inject
import io.vrap.codegen.languages.python.tsGeneratedComment
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
$tsGeneratedComment

import { Lifecycle } from "@hapi/hapi";

export type MethodType =
  | "GET"
  | "HEAD"
  | "POST"
  | "PUT"
  | "PATCH"
  | "DELETE"
  | "CONNECT"
  | "OPTIONS"
  | "TRACE";

export type ScalarValue = string | number | boolean | undefined;

export type HttpResponse = {
  body?: any;
  headers?: { [key: string]: string };
  statusCode: number;
};

export type HttpInput = {
  body: any;
  headers: { [key: string]: string };
  pathParams: { [key: string]: string };
  queryParams: {
    [key: string]: string | string[];
  };
};

export type Resource = {
  uri: string;
  method: MethodType;
  handler: (input: HttpInput) => HttpResponse;
};

export type ErrorHandler = ({
  request: Request,
  responseToolkit: ResponseToolkit,
  error: Error
}) => Lifecycle.ReturnValue;
""".trimIndent(),
                relativePath = "$moduleName.ts"
        )
    }
}
