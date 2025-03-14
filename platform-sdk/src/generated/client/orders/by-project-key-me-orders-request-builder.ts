
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyMeOrdersByIDRequestBuilder } from 'client/orders/by-project-key-me-orders-by-id-request-builder'
import { ByProjectKeyMeOrdersQuotesRequestBuilder } from 'client/quotes/by-project-key-me-orders-quotes-request-builder'
import { MyOrderFromCartDraft } from 'models/me'
import { Order, OrderPagedQueryResponse } from 'models/order'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyMeOrdersRequestBuilder {

    
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
    ): ByProjectKeyMeOrdersByIDRequestBuilder {
       return new ByProjectKeyMeOrdersByIDRequestBuilder(
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
    public orderQuote(): ByProjectKeyMeOrdersQuotesRequestBuilder {
       return new ByProjectKeyMeOrdersQuotesRequestBuilder(
             {
                pathArgs: {
                   ...this.args.pathArgs,
                },
                executeRequest: this.args.executeRequest,
                baseUri: this.args.baseUri
             }
       )
    }
    
    /**
    *	Returns all Orders that match a given Query Predicate.
    *	
    *	A [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error is returned in the following scenarios:
    *	
    *	- If no Orders exist for a given Query Predicate.
    *	- If the Order exists but does not have a `customerId` that matches the [customer:{id}](/scopes#composable-commerce-oauth) scope, or `anonymousId` that matches the [anonymous_id:{id}](/scopes#composable-commerce-oauth) scope.
    *	
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
               }): ApiRequest<OrderPagedQueryResponse> {
       return new ApiRequest<OrderPagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/me/orders',
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
    *	Checks if an Order exists for a given Query Predicate. Returns a `200 OK` status if successful.
    *	
    *	A [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error is returned in the following scenarios:
    *	
    *	- If no Order exists that matches the Query Predicate.
    *	- If one or more Orders exist but don't have either a `customerId` that matches the [customer:{id}](/scopes#composable-commerce-oauth) scope, or an `anonymousId` that matches the [anonymous_id:{id}](/scopes#composable-commerce-oauth) scope.
    *	
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
              uriTemplate: '/{projectKey}/me/orders',
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
    *	
    *	Creates an Order from a Cart for the Customer or anonymous user. The `customerId` or `anonymousId` field on the Order is automatically set based on the [customer:{id}](/scopes#composable-commerce-oauth) or [anonymous_id:{id}](/scopes#composable-commerce-oauth) scope.
    *	
    *	The Cart must have a [shipping address set](ctp:api:type:CartSetShippingAddressAction) for taxes to be calculated. When creating [B2B Orders](/associates-overview#b2b-resources), the Customer must have the `CreateMyOrdersFromMyCarts` [Permission](ctp:api:type:Permission).
    *	Creating an Order produces the [OrderCreated](ctp:api:type:OrderCreatedMessage) Message.
    *	
    *	If the Cart's `customerId` does not match the [customer:{id}](/scopes#composable-commerce-oauth) scope, or the `anonymousId` does not match the [anonymous_id:{id}](/scopes#composable-commerce-oauth) scope, a [ResourceNotFound](ctp:api:type:ResourceNotFoundError) error is returned.
    *	
    *	If a server-side problem occurs, indicated by a 500 Internal Server Error HTTP response, the Order creation may still successfully complete after the error is returned.
    *	If you receive this error, you should verify the status of the Order by querying a unique identifier supplied during the creation request, such as the Order number.
    *	
    *	Specific Error Codes:
    *	
    *	- [AssociateMissingPermission](ctp:api:type:AssociateMissingPermissionError)
    *	- [DiscountCodeNonApplicable](ctp:api:type:DiscountCodeNonApplicableError)
    *	- [InvalidItemShippingDetails](ctp:api:type:InvalidItemShippingDetailsError)
    *	- [OutOfStock](ctp:api:type:OutOfStockError)
    *	- [PriceChanged](ctp:api:type:PriceChangedError)
    *	- [ShippingMethodDoesNotMatchCart](ctp:api:type:ShippingMethodDoesNotMatchCartError)
    *	- [MatchingPriceNotFound](ctp:api:type:MatchingPriceNotFoundError)
    *	- [MissingTaxRateForCountry](ctp:api:type:MissingTaxRateForCountryError)
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: MyOrderFromCartDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Order> {
       return new ApiRequest<Order>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/me/orders',
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
