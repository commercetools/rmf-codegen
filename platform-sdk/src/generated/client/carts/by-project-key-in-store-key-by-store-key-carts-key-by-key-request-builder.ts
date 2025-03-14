
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { Cart, CartUpdate } from 'models/cart'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyInStoreKeyByStoreKeyCartsKeyByKeyRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                storeKey: string,
                key: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    /**
    *	If the Cart exists in the Project but does not have a `store` specified, or the `store` field references a different Store, this method returns a [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error.
    *	
    *	To ensure the Cart is up-to-date with current values (such as Prices and Discounts), use the [Recalculate](ctp:api:type:CartRecalculateAction) update action.
    *	
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
               }): ApiRequest<Cart> {
       return new ApiRequest<Cart>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/carts/key={key}',
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
    *	Checks if a Cart exists for a given `key`. Returns a `200 OK` status if the Cart exists or a [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error otherwise.
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
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/carts/key={key}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
           },
           this.args.executeRequest
       )
    }
    /**
    *	If the Cart exists in the Project but does not have a `store` specified, or the `store` field references a different Store, this method returns a [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error.
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: CartUpdate,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Cart> {
       return new ApiRequest<Cart>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/carts/key={key}',
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
    *	If the Cart exists in the Project but does not have a `store` specified, or the `store` field references a different Store, this method returns a [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error.
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
                  }): ApiRequest<Cart> {
       return new ApiRequest<Cart>(
           {
              baseUri: this.args.baseUri,
              method: 'DELETE',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/carts/key={key}',
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
