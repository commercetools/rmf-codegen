package io.vrap.codegen.languages.typescript.client.files_producers

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class ClientFileProducer : FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(commonTypes(), localCommonTypes(),produceClient())
    }

    fun commonTypes() = TemplateFile(relativePath = "base/common-types.ts", content = """
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

""")
}

fun localCommonTypes() = TemplateFile(relativePath = "base/local-common-types.ts", content = """
import { PathLike } from "fs";

export type VFile = {
    filePath: PathLike
}

""")

fun produceClient() = TemplateFile(relativePath = "base/requests-utils.ts", content = """
import { Middleware, MiddlewareArg, CommonRequest } from "./common-types";

export class ApiRequest<I, O, T extends CommonRequest<I>> {
  private middleware: Middleware;
  constructor(private readonly commonRequest: T, middlewares: Middleware[]) {
    if (!middlewares || middlewares.length == 0) {
      middlewares = [noOpMiddleware];
    }
    this.middleware = middlewares.reduce(reduceMiddleware);
  }

  async execute(): Promise<O> {
    const req = {
      ...this.commonRequest,
      url: getURI(this.commonRequest)
    };

    const res = await this.middleware({
      request: req,
      error: null,
      next: noOpMiddleware,
      response: null
    });

    if (res.error) {
      throw res.error;
    }
    return res.response;
  }
}

function reduceMiddleware(op1: Middleware, op2: Middleware): Middleware {
  return async (arg: MiddlewareArg) => {
    const { next, ...rest } = arg;
    const intermediateOp: Middleware = (tmpArg: MiddlewareArg) => {
      const { next, ...rest } = tmpArg;
      return op2({ ...rest, next: arg.next });
    };

    return op1({
      ...rest,
      next: intermediateOp
    });
  };
}

function getURI(commonRequest: CommonRequest<any>): string {
  const pathMap = commonRequest.pathVariables;
  const queryMap = commonRequest.queryParams;
  var uri: String = commonRequest.uriTemplate;
  var queryParams = [];
  for (const param in pathMap) {
    uri = uri.replace(`{${'$'}{param}}`, `${'$'}{pathMap[param]}`);
  }
  for (const query in queryMap) {
    queryParams = [
      ...queryParams,
      `${'$'}{query}=${'$'}{encodeURIComponent(`${'$'}{queryMap[query]}`)}`
    ];
  }
  const resQuery = queryParams.join("&");
  if (resQuery == "") {
    return `${'$'}{commonRequest.baseURL}${'$'}{uri}`;
  }
  return `${'$'}{commonRequest.baseURL}${'$'}{uri}?${'$'}{resQuery}`;
}

const noOpMiddleware = async (x: MiddlewareArg) => x;

""")