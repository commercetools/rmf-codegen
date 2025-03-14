
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder } from 'client/custom-objects/by-project-key-custom-objects-by-container-by-key-request-builder'
import { ByProjectKeyCustomObjectsByContainerRequestBuilder } from 'client/custom-objects/by-project-key-custom-objects-by-container-request-builder'
import { CustomObject, CustomObjectDraft, CustomObjectPagedQueryResponse } from 'models/custom-object'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyCustomObjectsRequestBuilder {

    
      constructor(
        protected readonly args: {
          pathArgs: {
                projectKey: string
           },
          executeRequest: executeRequest,
          baseUri?: string
        }
      ) {}
    public withContainerAndKey(
       childPathArgs: {
           container: string
           key: string
       }
    ): ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder {
       return new ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder(
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
    public withContainer(
       childPathArgs: {
           container: string
       }
    ): ByProjectKeyCustomObjectsByContainerRequestBuilder {
       return new ByProjectKeyCustomObjectsByContainerRequestBuilder(
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
    
    /**
    *	For performance reasons, it is highly advisable to query for Custom Objects in a container by using the `container` field in the `where` predicate.
    *	
    */
    /** 
    * @deprecated
    **/
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
               }): ApiRequest<CustomObjectPagedQueryResponse> {
       return new ApiRequest<CustomObjectPagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/custom-objects',
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
    *	Checks if a CustomObject exists for a given Query Predicate. Returns a `200 OK` status if any CustomObjects match the Query Predicate or a `404 Not Found` otherwise.
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
              uriTemplate: '/{projectKey}/custom-objects',
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
    *	If an object with the given container/key exists, the object will be replaced with the new value and the version is incremented.
    *	If the request contains a version and an object with the given container/key, then the version must match the version of the existing object. Concurrent updates to the same Custom Object returns a [ConcurrentModification](ctp:api:type:ConcurrentModificationError) error even if the version is not provided.
    *	
    *	Fields within `value` that have `null` values **are not saved**.
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: CustomObjectDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<CustomObject> {
       return new ApiRequest<CustomObject>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/custom-objects',
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
