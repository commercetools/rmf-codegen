
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ProductTailoring, ProductTailoringInStoreDraft, ProductTailoringPagedQueryResponse } from 'models/product-tailoring'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyInStoreKeyByStoreKeyProductTailoringRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                storeKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    /**
    *	Queries Product Tailoring in a specific [Store](ctp:api:type:Store).
    *	
    */
    public get(
               methodArgs?:{
                  
                  queryArgs?: {
                     'limit'?: number
                     'offset'?: number
                     'withTotal'?: boolean
                     'expand'?: string | string[]
                     'where'?: string | string[]
                     'sort'?: string | string[]
                     [key: string]: QueryParam
                  },
                  headers?: {
                     [key:string]: string | string[]
                  },
               }): ApiRequest<ProductTailoringPagedQueryResponse> {
       return new ApiRequest<ProductTailoringPagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/product-tailoring',
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
    *	Creates a [ProductTailoring](ctp:api:type:ProductTailoring) in the [Store](ctp:api:type:Store) specified by `storeKey`.
    *	When using this endpoint the ProductTailoring's `store` field is always set to the [Store](ctp:api:type:Store) specified in the path parameter.
    *	
    *	Generates the [ProductTailoringCreated](ctp:api:type:ProductTailoringCreatedMessage) Message.
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: ProductTailoringInStoreDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<ProductTailoring> {
       return new ApiRequest<ProductTailoring>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/product-tailoring',
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
