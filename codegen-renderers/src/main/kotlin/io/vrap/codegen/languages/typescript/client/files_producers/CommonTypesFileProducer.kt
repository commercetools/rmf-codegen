package io.vrap.codegen.languages.typescript.client.files_producers

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class CommonTypesFileProducer : FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile {
        val content = """
type HttpMethod =
  | "GET"
  | "HEAD"
  | "POST"
  | "PUT"
  | "DELETE"
  | "CONNECT"
  | "OPTIONS"
  | "TRACE";

export interface CommonRequest<T> {
  baseUri: string;
  headers?: { [key: string]: string | number | boolean };
  method: HttpMethod;
  uriTemplate: string;
  mediaType?: string;
  pathVariables?: { [key: string]: string | number | boolean };
  queryParams?: { [key: string]: string | number | boolean };
  payload?: T;
}

export type MiddlewareArg = {
  request: CommonRequest<any>;
  response: any;
  error: Error;
  next: Middleware;
};

export type Middleware = (arg: MiddlewareArg) => Promise<MiddlewareArg>;


"""


        return TemplateFile(content = content,relativePath = "base/common-types.ts")

    }
}