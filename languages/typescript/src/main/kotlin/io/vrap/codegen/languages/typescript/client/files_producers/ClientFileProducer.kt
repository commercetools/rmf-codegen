package io.vrap.codegen.languages.typescript.client.files_producers

import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer

class ClientFileProducer constructor(val clientConstants: ClientConstants) : FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                commonTypes(),
                requestUtilsFile(),
                uriUtilsFile(),
                middleware()
        )
    }

    fun commonTypes() = TemplateFile(relativePath = "${clientConstants.commonTypesPackage}.ts", content = """
$tsGeneratedComment


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

export type QueryParam =
  | string
  | string[]
  | number
  | number[]
  | boolean
  | boolean[]
  | undefined;

export type VariableMap = {
  [key: string]: QueryParam;
};

export interface ClientRequest {
  baseUri?: string;
  uri?: string,
  headers?: VariableMap;
  method: MethodType;
  uriTemplate?: string;
  pathVariables?: VariableMap;
  queryParams?: VariableMap;
  body?: any
}

export type ClientResponse<T = any> = {
  body: T;
  statusCode?: number;
  headers?: Object;
};

export type executeRequest = (request: ClientRequest) => Promise<ClientResponse>
""")

    fun requestUtilsFile() = TemplateFile(relativePath = "${clientConstants.requestUtilsPackage}.ts", content = """
$tsGeneratedComment


import { ClientResponse, executeRequest, ClientRequest } from '${clientConstants.commonTypesPackage}'
import { buildRelativeUri } from '${clientConstants.uriUtilsPackage}'

export class ApiRequest<O> {
  private request: ClientRequest
  constructor(
    request: ClientRequest,
    private readonly requestExecutor: executeRequest
  ) { 
    this.request = {
      ...request,
      uri: buildRelativeUri(request)
    }
  }

  public clientRequest(): ClientRequest {
    return this.request;
  }

  public execute(): Promise<ClientResponse<O>> {
    return this.requestExecutor(this.request)
  }
}
""")

    fun uriUtilsFile() = TemplateFile(relativePath = "${clientConstants.uriUtilsPackage}.ts", content = """
$tsGeneratedComment


import { stringify } from 'qs'
import {
  VariableMap,
  ClientRequest,
} from '${clientConstants.commonTypesPackage}'

const qsOptions = {
  indices: false,
  encodeValuesOnly: true
}

function isDefined<T>(value: T | undefined | null): value is T {
  return typeof value !== 'undefined' && value !== null
}

function cleanObject<T extends VariableMap>(obj: T): T {
  return Object.keys(obj).reduce<T>((result, key) => {
    const value = obj[key]

    if (Array.isArray(value)) {
      const values = (value as unknown[]).filter(isDefined)
      if (!values.length) {
        return result
      }

      return {
        ...result,
        [key]: values,
      }
    }

    if (isDefined(value)) {
      return { ...result, [key]: value }
    }

    return result
  }, {} as T)
}

function formatQueryString(variableMap: VariableMap) {
  const map = cleanObject(variableMap)
  const result = stringify(map, qsOptions)
  if (result === '') {
    return ''
  }
  return `?${'$'}{result}`
}

export function buildRelativeUri(commonRequest: ClientRequest): string {
  const pathMap = commonRequest.pathVariables
  var uri: string = commonRequest.uriTemplate as string

  for (const param in pathMap) {
    uri = uri.replace(`{${'$'}{param}}`, `${'$'}{pathMap[param]}`)
  }

  const resQuery = formatQueryString(commonRequest.queryParams || {})
  return `${'$'}{uri}${'$'}{resQuery}`
}

""")

    fun middleware() = TemplateFile(relativePath = "${clientConstants.middlewarePackage}.ts", content = """
$tsGeneratedComment


import {
  executeRequest,
  ClientResponse,
  ClientRequest
} from '${clientConstants.commonTypesPackage}';

export type Middleware = (
  request: ClientRequest,
  executor: executeRequest
) => Promise<ClientResponse>;

export const createExecutorFromMiddlewares = (
  executor: executeRequest,
  midds?: Middleware[]
) => {
  if (!midds || midds.length == 0) {
    return executor;
  }
  const reduced: Middleware = midds.reduce(reduceMiddleware);
  return middlewareToExecutor(reduced, executor);
};

function reduceMiddleware(
  middleware1: Middleware,
  middleware2: Middleware
): Middleware {
  return (request: ClientRequest, executor: executeRequest) =>
    middleware1(request, middlewareToExecutor(middleware2, executor));
}

function middlewareToExecutor(
  middleware: Middleware,
  executor: executeRequest
): executeRequest {
  return (request: ClientRequest) => middleware(request, executor);
}
""")
}
