package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.raml.model.modules.Api

class TsClientFileProducer @Inject constructor(val api:Api): FileProducer{


    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile{
        var content = """

import * as Axios from 'axios';


const client =  Axios.default

const baseUri = '${api.baseUri.template}'
const access_token = ''

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
  constructor(private readonly commonRequest: T) {}

  async execute(): Promise<O> {
    switch(this.commonRequest.method){
      case 'GET':{
        return client.get(this.getURI(),{
          headers:{
            'Authorization': `Bearer ${'$'}{access_token}`,
          }
        }).then(res=> res.data )
      }
      case 'DELETE':{
        return client.delete(this.getURI(),{
          headers:{
            'Authorization': `Bearer ${'$'}{access_token}`,
          }
        }).then(res=> res.data )
      }
      case 'POST':{
        return client.post(this.getURI(),this.commonRequest.payload,{
          headers:{
            'Authorization': `Bearer ${'$'}{access_token}`,
          },
          
        }).then(res=> res.data )
      }
      default: return Promise.reject(new Error(`method not supported`))
    }
  }

private getURI(): string {
    const pathMap = this.commonRequest.pathVariables
    const queryMap = this.commonRequest.queryParams
    var uri: String = this.commonRequest.uriTemplate
    var queryParams = []
    for(const param in pathMap){
      uri = uri.replace(`{${'$'}{param}}`,`${'$'}{pathMap[param]}`)
    }
    for(const query in queryMap){
      queryParams = [...queryParams,`${'$'}{query}=${'$'}{queryMap[query]}`]
    }
    const resQuery = queryParams.join('&')
    if(resQuery == ''){
      return `${'$'}{baseUri}${'$'}{uri}`
    }
    return `${'$'}{baseUri}${'$'}{uri}?${'$'}{resQuery}`
  }
}

        """


        return TemplateFile(content = content,relativePath = "base/requests-utils.ts")

    }
}