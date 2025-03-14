
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { Order, OrderFromQuoteDraft } from 'models/order'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyAsAssociateByAssociateIdInBusinessUnitKeyByBusinessUnitKeyOrdersQuotesRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                associateId: string,
                businessUnitKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    /**
    *	Creates an Order from a [Quote](ctp:api:type:Cart) in a [BusinessUnit](ctp:api:type:BusinessUnit).
    *	Creating an Order fails with an [InvalidOperation](ctp:api:type:InvalidOperationError) if the Quote does not reference the same BusinessUnit as the `businessUnitKey` path parameter.
    *	
    *	Specific Error Codes:
    *	
    *	- [InvalidItemShippingDetails](ctp:api:type:InvalidItemShippingDetailsError)
    *	- [OutOfStock](ctp:api:type:OutOfStockError)
    *	
    */
    public post(
                methodArgs:{
                   
                   body: OrderFromQuoteDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Order> {
       return new ApiRequest<Order>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/as-associate/{associateId}/in-business-unit/key={businessUnitKey}/orders/quotes',
              pathVariables: this.args.pathArgs,
              headers: {
                  'Content-Type': 'application/json',
                  ...methodArgs?.headers
              },
              body: methodArgs?.body,
           },
           this.args.executeRequest
       )
    }
    

}
