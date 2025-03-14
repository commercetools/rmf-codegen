
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyDiscountCodesByIDRequestBuilder } from 'client/discount-codes/by-project-key-discount-codes-by-id-request-builder'
import { ByProjectKeyDiscountCodesKeyByKeyRequestBuilder } from 'client/discount-codes/by-project-key-discount-codes-key-by-key-request-builder'
import { DiscountCode, DiscountCodeDraft, DiscountCodePagedQueryResponse } from 'models/discount-code'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyDiscountCodesRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    public withId(
       childPathArgs: {
           ID: string
       }
    ): ByProjectKeyDiscountCodesByIDRequestBuilder {
       return new ByProjectKeyDiscountCodesByIDRequestBuilder(
             {
                pathArgs: {
                   ...this.args.pathArgs,
                   ...childPathArgs
                },
                executeRequest: this.args.executeRequest,
                baseUri: this.args.baseUri
             }
       )
    }
    public withKey(
       childPathArgs: {
           key: string
       }
    ): ByProjectKeyDiscountCodesKeyByKeyRequestBuilder {
       return new ByProjectKeyDiscountCodesKeyByKeyRequestBuilder(
             {
                pathArgs: {
                   ...this.args.pathArgs,
                   ...childPathArgs
                },
                executeRequest: this.args.executeRequest,
                baseUri: this.args.baseUri
             }
       )
    }
    
    /**
    *	Deprecated OAuth 2.0 Scope: `view_orders:{projectKey}`
    */
    public get(
               methodArgs?:{
                  
                  queryArgs?: {
                     'expand'?: string | string[]
                     'sort'?: string | string[]
                     'limit'?: number
                     'offset'?: number
                     'withTotal'?: boolean
                     'where'?: string | string[]
                     [key: string]: QueryParam
                  },
                  headers?: {
                     [key:string]: string | string[]
                  },
               }): ApiRequest<DiscountCodePagedQueryResponse> {
       return new ApiRequest<DiscountCodePagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/discount-codes',
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
    *	Checks if a DiscountCode exists for a given Query Predicate. Returns a `200 OK` status if any DiscountCodes match the Query Predicate, or a `404 Not Found` otherwise.
    */
    public head(
                methodArgs?:{
                   
                   queryArgs?: {
                      'where'?: string | string[]
                      [key: string]: QueryParam
                   },
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<void> {
       return new ApiRequest<void>(
           {
              baseUri: this.args.baseUri,
              method: 'HEAD',
              uriTemplate: '/{projectKey}/discount-codes',
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
    *	Creating a Discount Code produces the [DiscountCodeCreated](ctp:api:type:DiscountCodeCreatedMessage) Message.
    *	
    *	Deprecated OAuth 2.0 Scope: `manage_orders:{projectKey}`
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: DiscountCodeDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<DiscountCode> {
       return new ApiRequest<DiscountCode>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/discount-codes',
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
    

}
