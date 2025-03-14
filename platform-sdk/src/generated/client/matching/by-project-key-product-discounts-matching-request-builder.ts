
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ProductDiscount, ProductDiscountMatchQuery } from 'models/product-discount'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyProductDiscountsMatchingRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    /**
    *	This endpoint can be used to simulate which Product Discounts would be applied if a specified Product Variant had a specified Price.
    *	Given Product and Product Variant IDs and a Price, this endpoint will return the [ProductDiscount](ctp:api:type:ProductDiscount) that would have been applied to that Price.
    *	
    *	If a Product Discount could not be found that could be applied to the Price of a Product Variant, a [NoMatchingProductDiscountFound](ctp:api:type:NoMatchingProductDiscountFoundError) error is returned.
    *	
    */
    public post(
                methodArgs:{
                   
                   body: ProductDiscountMatchQuery,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<ProductDiscount> {
       return new ApiRequest<ProductDiscount>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/product-discounts/matching',
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
