package io.vrap.codegen.languages.typescript.client

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class TsClientFileProducer : FileProducer{

    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile{
        var content = """

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
  method: HttpMethod;
  uriTemplate: string;
  mediaType?: string;
  pathVariables?: { [key: string]: string | number | boolean };
  queryParams?: { [key: string]: string | number | boolean };
  payload?: T;
}

export class ApiRequest<I, O, T extends CommonRequest<I>> {
  constructor(readonly commonRequest: T) {}

  execute(): Promise<O> {
    return null;
  }
}

        """


        return TemplateFile(content = content,relativePath = "base/requests-utils.ts")

    }
}