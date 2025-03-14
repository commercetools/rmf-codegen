
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { Channel, ChannelUpdate } from 'models/channel'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyChannelsByIDRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                ID: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    public get(
               methodArgs?:{
                  
                  queryArgs?: {
                     'expand'?: string | string[]
                     [key: string]: QueryParam
                  },
                  headers?: {
                     [key:string]: string | string[]
                  },
               }): ApiRequest<Channel> {
       return new ApiRequest<Channel>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/channels/{ID}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
              queryParams: methodArgs?.queryArgs,
           },
           this.args.executeRequest
       )
    }
    /**
    *	Checks if a Channel exists for a given `id`. Returns a `200 OK` status if the Channel exists or a `404 Not Found` otherwise.
    */
    public head(
                methodArgs?:{
                   
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<void> {
       return new ApiRequest<void>(
           {
              baseUri: this.args.baseUri,
              method: 'HEAD',
              uriTemplate: '/{projectKey}/channels/{ID}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
           },
           this.args.executeRequest
       )
    }
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: ChannelUpdate,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Channel> {
       return new ApiRequest<Channel>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/channels/{ID}',
              pathVariables: this.args.pathArgs,
              headers: {
                  'Content-Type': 'application/json',
                  ...methodArgs?.headers
              },
              queryParams: methodArgs?.queryArgs,
              body: methodArgs?.body,
           },
           this.args.executeRequest
       )
    }
    /**
    *	Returns a [ReferenceExists](ctp:api:type:ReferenceExistsError) error if other resources reference the Channel to be deleted.
    *	
    */
    public delete(
                  methodArgs:{
                     
                     queryArgs: {
                        'version': number
                        'expand'?: string | string[]
                        [key: string]: QueryParam
                     },
                     headers?: {
                        [key:string]: string | string[]
                     },
                  }): ApiRequest<Channel> {
       return new ApiRequest<Channel>(
           {
              baseUri: this.args.baseUri,
              method: 'DELETE',
              uriTemplate: '/{projectKey}/channels/{ID}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
              queryParams: methodArgs?.queryArgs,
           },
           this.args.executeRequest
       )
    }
    

}
