
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { DiscountCode, DiscountCodeUpdate } from 'models/discount-code'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyDiscountCodesKeyByKeyRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                key: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    /**
    *	Deprecated OAuth 2.0 Scope: `view_orders:{projectKey}`
    */
    public get(
               methodArgs?:{
                  
                  queryArgs?: {
                     'expand'?: string | string[]
                     [key: string]: QueryParam
                  },
                  headers?: {
                     [key:string]: string | string[]
                  },
               }): ApiRequest<DiscountCode> {
       return new ApiRequest<DiscountCode>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/discount-codes/key={key}',
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
    *	Checks if a DiscountCode exists for a given `key`. Returns a `200 OK` status if the DiscountCode exists or a `404 Not Found` otherwise.
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
              uriTemplate: '/{projectKey}/discount-codes/key={key}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
           },
           this.args.executeRequest
       )
    }
    /**
    *	Deprecated OAuth 2.0 Scope: `manage_orders:{projectKey}`
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: DiscountCodeUpdate,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<DiscountCode> {
       return new ApiRequest<DiscountCode>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/discount-codes/key={key}',
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
    *	Deleting a Discount Code produces the [DiscountCodeDeleted](ctp:api:type:DiscountCodeDeletedMessage) Message.
    *	
    *	Deprecated OAuth 2.0 Scope: `manage_orders:{projectKey}`
    *	
    */
    public delete(
                  methodArgs:{
                     
                     queryArgs: {
                        'dataErasure'?: boolean
                        'version': number
                        'expand'?: string | string[]
                        [key: string]: QueryParam
                     },
                     headers?: {
                        [key:string]: string | string[]
                     },
                  }): ApiRequest<DiscountCode> {
       return new ApiRequest<DiscountCode>(
           {
              baseUri: this.args.baseUri,
              method: 'DELETE',
              uriTemplate: '/{projectKey}/discount-codes/key={key}',
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
