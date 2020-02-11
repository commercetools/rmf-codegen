package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer

class ClientFileProducer @Inject constructor(val clientConstants: ClientConstants) : FileProducer {


    override fun produceFiles(): List<TemplateFile> {
        return listOf(commonTypes(), requestUtilsFile(),uriUtilsFile())
    }

    fun commonTypes() = TemplateFile(relativePath = "${clientConstants.commonTypesPackage}.ts", content = """
export type MethodType =
  | "GET"
  | "HEAD"
  | "POST"
  | "PUT"
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
import { ClientResponse, VariableMap, executeRequest, ClientRequest } from '${clientConstants.commonTypesPackage}'
import { buildRelativeUri } from '${clientConstants.uriUtilsPackage}'
import { stringify } from "querystring"

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

  public execute(): Promise<ClientResponse<O>> {
    return this.requestExecutor(this.request)
  }
}
""")

    fun uriUtilsFile() = TemplateFile(relativePath = "${clientConstants.uriUtilsPackage}.ts", content = """
import { stringify } from 'querystring'
import {
  VariableMap,
  ClientRequest,
} from '${clientConstants.commonTypesPackage}'

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
  const result = stringify(map)
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
}