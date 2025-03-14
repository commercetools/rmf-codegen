
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { Customer, CustomerEmailVerify } from 'models/customer'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyCustomersEmailConfirmRequestBuilder {

    
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
    *	Verifying the email of the Customer produces the [CustomerEmailVerified](ctp:api:type:CustomerEmailVerifiedMessage) Message.
    *	
    */
    public post(
                methodArgs:{
                   
                   body: CustomerEmailVerify,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Customer> {
       return new ApiRequest<Customer>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/customers/email/confirm',
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
