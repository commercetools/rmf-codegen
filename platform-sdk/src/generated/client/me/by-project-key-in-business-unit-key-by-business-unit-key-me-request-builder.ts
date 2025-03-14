
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyInBusinessUnitKeyByBusinessUnitKeyMeCustomersRequestBuilder } from 'client/customers/by-project-key-in-business-unit-key-by-business-unit-key-me-customers-request-builder'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyInBusinessUnitKeyByBusinessUnitKeyMeRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string,
                businessUnitKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    public customers(): ByProjectKeyInBusinessUnitKeyByBusinessUnitKeyMeCustomersRequestBuilder {
       return new ByProjectKeyInBusinessUnitKeyByBusinessUnitKeyMeCustomersRequestBuilder(
             {
                pathArgs: {
                   ...this.args.pathArgs,
                },
                executeRequest: this.args.executeRequest,
                baseUri: this.args.baseUri
             }
       )
    }
    

}
