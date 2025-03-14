
/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/
import { ByProjectKeyCategoriesByIDRequestBuilder } from 'client/categories/by-project-key-categories-by-id-request-builder'
import { ByProjectKeyCategoriesKeyByKeyRequestBuilder } from 'client/categories/by-project-key-categories-key-by-key-request-builder'
import { Category, CategoryDraft, CategoryPagedQueryResponse } from 'models/category'
import { QueryParam, executeRequest } from 'shared/utils/common-types'
import { ApiRequest } from 'shared/utils/requests-utils'
/**
**/
export class ByProjectKeyCategoriesRequestBuilder {

    
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
    ): ByProjectKeyCategoriesKeyByKeyRequestBuilder {
       return new ByProjectKeyCategoriesKeyByKeyRequestBuilder(
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
    ): ByProjectKeyCategoriesByIDRequestBuilder {
       return new ByProjectKeyCategoriesByIDRequestBuilder(
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
    *	Either the [scope](/../api/scopes) `view_products:{projectKey}` or `view_categories:{projectKey}` is required.
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
               }): ApiRequest<CategoryPagedQueryResponse> {
       return new ApiRequest<CategoryPagedQueryResponse>(
           {
              baseUri: this.args.baseUri,
              method: 'GET',
              uriTemplate: '/{projectKey}/categories',
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
    *	Checks if a Category exists for on a given Query Predicate. Returns a `200 OK` status if any Categories match the Query Predicate or a `404 Not Found` otherwise.
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
              uriTemplate: '/{projectKey}/categories',
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
    *	Either the [scope](/../api/scopes) `manage_products:{projectKey}` or `manage_categories:{projectKey}` is required.
    *	
    *	Creating a Category produces the [CategoryCreated](ctp:api:type:CategoryCreatedMessage) Message.
    *	
    */
    public post(
                methodArgs:{
                   
                   queryArgs?: {
                      'expand'?: string | string[]
                      [key: string]: QueryParam
                   },
                   body: CategoryDraft,
                   headers?: {
                      [key:string]: string | string[]
                   },
                }): ApiRequest<Category> {
       return new ApiRequest<Category>(
           {
              baseUri: this.args.baseUri,
              method: 'POST',
              uriTemplate: '/{projectKey}/categories',
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
