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
  baseURL: string;
  url?: string,
  headers?: { [key: string]: string };
  method: HttpMethod;
  uriTemplate: string;
  pathVariables?: { [key: string]: string | number | boolean };
  queryParams?: { [key: string]: string | number | boolean };
  payload?: T;
  dataType?: 'TEXT'|'BINARY';
}

export type MiddlewareArg = {
  request: CommonRequest<any>;
  response: any;
  error: Error;
  next: Middleware;
};

export type Middleware = (arg: MiddlewareArg) => Promise<MiddlewareArg>;

export type FilePath = {
  filePath: string
}

export type VFile = {
  filePath: string
}

"""


        return TemplateFile(content = content,relativePath = "base/common-types.ts")

    }
}