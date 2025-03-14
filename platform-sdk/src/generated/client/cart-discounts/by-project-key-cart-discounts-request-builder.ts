
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyCartDiscountsByIDRequestBuilder } from 'client/cart-discounts/by-project-key-cart-discounts-by-id-request-builder'
import { ByProjectKeyCartDiscountsKeyByKeyRequestBuilder } from 'client/cart-discounts/by-project-key-cart-discounts-key-by-key-request-builder'
import { CartDiscount, CartDiscountDraft, CartDiscountPagedQueryResponse } from 'models/cart-discount'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyCartDiscountsRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    public withKey(
       childPathArgs: {
           key: string
       }
    ): ByProjectKeyCartDiscountsKeyByKeyRequestBuilder {
       return new ByProjectKeyCartDiscountsKeyByKeyRequestBuilder(
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
    public withId(
       childPathArgs: {
           ID: string
       }
    ): ByProjectKeyCartDiscountsByIDRequestBuilder {
       return new ByProjectKeyCartDiscountsByIDRequestBuilder(
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
               }): ApiRequest<CartDiscountPagedQueryResponse> {
       return new ApiRequest<CartDiscountPagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/cart-discounts',
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
    *	Checks if a CartDiscount exists for a given Query Predicate. Returns a `200 OK` status if any CartDiscounts match the Query Predicate, or a `404 Not Found` otherwise.
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
              uriTemplate: '/{projectKey}/cart-discounts',
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
    *	Creating a Cart Discount produces the [CartDiscountCreated](ctp:api:type:CartDiscountCreatedMessage) Message.
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: CartDiscountDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<CartDiscount> {
       return new ApiRequest<CartDiscount>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/cart-discounts',
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
