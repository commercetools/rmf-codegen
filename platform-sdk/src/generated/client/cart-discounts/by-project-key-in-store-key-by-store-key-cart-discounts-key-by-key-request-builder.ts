
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { CartDiscount, CartDiscountUpdate } from 'models/cart-discount'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyInStoreKeyByStoreKeyCartDiscountsKeyByKeyRequestBuilder {

    
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
    public get(
               methodArgs?:{
                  
                  queryArgs?: {
                     'expand'?: string | string[]
                     [key: string]: QueryParam
                  },
                  headers?: {
                     [key:string]: string | string[]
                  },
               }): ApiRequest<CartDiscount> {
       return new ApiRequest<CartDiscount>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/cart-discounts/key={key}',
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
    *	Checks if a CartDiscount exists for a given `key`. Returns a `200 OK` status if the CartDiscount exists or a `404 Not Found` otherwise.
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
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/cart-discounts/key={key}',
              pathVariables: this.args.pathArgs,
              headers: {
                  ...methodArgs?.headers
              },
           },
           this.args.executeRequest
       )
    }
    /**
    *	To update a CartDiscount, you must have permissions for all Stores the CartDiscount is associated with, except when [removing a Store](ctp:api:type:CartDiscountRemoveStoreAction).
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: CartDiscountUpdate,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<CartDiscount> {
       return new ApiRequest<CartDiscount>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/cart-discounts/key={key}',
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
    *	To delete a CartDiscount, specify the `manage_cart_discounts:{projectKey}:{storeKey}` scope for all Stores associated with the CartDiscount.
    *	
    *	Deleting a Cart Discount produces the [CartDiscountDeleted](ctp:api:type:CartDiscountDeletedMessage) Message.
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
                  }): ApiRequest<CartDiscount> {
       return new ApiRequest<CartDiscount>(
           {
              baseUri: this.args.baseUri,
              method: 'DELETE',
              uriTemplate: '/{projectKey}/in-store/key={storeKey}/cart-discounts/key={key}',
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
