/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/

import { CustomerIndexingStatus } from 'models/project'
import { SearchQuery, SearchSorting, _SearchQuery } from 'models/search'

export interface CustomerIndexingProgress  {
  /**
  *	The number of Customers successfully indexed.
  *	
  */
  readonly indexed: number;
  /**
  *	The number of Customers that failed to be indexed.
  *	
  */
  readonly failed: number;
  /**
  *	The estimated total number of Customers to be indexed.
  *	
  */
  readonly estimatedTotal: number
}
export interface CustomerPagedSearchResponse  {
  /**
  *	Total number of results matching the query.
  *	
  */
  readonly total: number;
  /**
  *	Number of [results requested](/../api/general-concepts#limit).
  *	
  *	
  */
  readonly limit: number;
  /**
  *	Number of [elements skipped](/../api/general-concepts#offset).
  *	
  *	
  */
  readonly offset: number;
  /**
  *	Search result containing the Customers matching the search query.
  *	
  *	
  */
  readonly results: CustomerSearchResult[]
}
export interface CustomerSearchIndexingStatusResponse  {
  /**
  *	Current status of indexing the Customer Search.
  *	
  */
  readonly status: CustomerIndexingStatus;
  /**
  *	Progress of indexing. Only available when indexing is in progress.
  *	
  */
  readonly states?: CustomerIndexingProgress;
  /**
  *	Date and time (UTC) when the last indexing started.
  *	
  */
  readonly startedAt?: string;
  /**
  *	Time when the status was last modified.
  *	
  */
  readonly lastModifiedAt?: string;
  /**
  *	Indicates how many times the system tried to start indexing after failed attempts. The counter is set to null after an indexing finished successfully.
  *	
  *	
  */
  readonly retryCount?: number
}
export interface CustomerSearchRequest  {
  /**
  *	The Customer search query.
  *	
  */
  readonly query?: _SearchQuery;
  /**
  *	Controls how results to your query are sorted. If not provided, the results are sorted by relevance in descending order.
  *	
  */
  readonly sort?: SearchSorting[];
  /**
  *	The maximum number of search results to be returned.
  *	
  */
  readonly limit?: number;
  /**
  *	The number of search results to be skipped in the response for pagination.
  *	
  */
  readonly offset?: number
}
export interface CustomerSearchResult  {
  /**
  *	`id` of the [Customer](ctp:api:type:Customer) matching the search query.
  *	
  *	
  */
  readonly id: string;
  /**
  *	How closely this customer matches the search query.
  *	
  *	
  */
  readonly relevance: number
}
