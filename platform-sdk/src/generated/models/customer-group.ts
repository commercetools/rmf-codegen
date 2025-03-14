/**
* Code generated by [commercetools RMF-Codegen](https://github.com/commercetools/rmf-codegen). DO NOT EDIT.
* Please don't change this file manually but run `rmf-codegen generate raml_file_path -o output_path -t typescript_client` to update it.
* For more information about the commercetools platform APIs, visit https://docs.commercetools.com/.
*/

import { BaseResource, CreatedBy, LastModifiedBy, Reference, ReferenceTypeId, ResourceIdentifier, _BaseResource } from 'models/common'
import { CustomFields, CustomFieldsDraft, FieldContainer, TypeResourceIdentifier } from 'models/type'

export interface CustomerGroup extends BaseResource  {
  /**
  *	Unique identifier of the CustomerGroup.
  *	
  *	
  */
  readonly id: string;
  /**
  *	Current version of the CustomerGroup.
  *	
  *	
  */
  readonly version: number;
  /**
  *	Date and time (UTC) the CustomerGroup was initially created.
  *	
  *	
  */
  readonly createdAt: string;
  /**
  *	Date and time (UTC) the CustomerGroup was last updated.
  *	
  *	
  */
  readonly lastModifiedAt: string;
  /**
  *	IDs and references that last modified the CustomerGroup.
  *	
  *	
  */
  readonly lastModifiedBy?: LastModifiedBy;
  /**
  *	IDs and references that created the CustomerGroup.
  *	
  *	
  */
  readonly createdBy?: CreatedBy;
  /**
  *	User-defined unique identifier for the CustomerGroup.
  *	
  *	
  */
  readonly key?: string;
  /**
  *	Unique name of the CustomerGroup.
  *	
  *	
  */
  readonly name: string;
  /**
  *	Custom Fields for the CustomerGroup.
  *	
  *	
  */
  readonly custom?: CustomFields
}
export interface CustomerGroupDraft  {
  /**
  *	User-defined unique identifier for the CustomerGroup.
  *	
  *	
  */
  readonly key?: string;
  /**
  *	Unique value which must be different from any value used for `name` in [CustomerGroup](ctp:api:type:CustomerGroup) in the Project.
  *	If not, a [DuplicateField](ctp:api:type:DuplicateFieldError) error is returned.
  *	
  *	
  */
  readonly groupName: string;
  /**
  *	Custom Fields for the CustomerGroup.
  *	
  *	
  */
  readonly custom?: CustomFieldsDraft
}
/**
*	[PagedQueryResult](/general-concepts#pagedqueryresult) with `results` containing an array of [CustomerGroup](ctp:api:type:CustomerGroup).
*	
*/
export interface CustomerGroupPagedQueryResponse  {
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
  *	Actual number of results returned.
  *	
  *	
  */
  readonly count: number;
  /**
  *	Total number of results matching the query.
  *	This number is an estimation that is not [strongly consistent](/../api/general-concepts#strong-consistency).
  *	This field is returned by default.
  *	For improved performance, calculating this field can be deactivated by using the query parameter `withTotal=false`.
  *	When the results are filtered with a [Query Predicate](/../api/predicates/query), `total` is subject to a [limit](/../api/limits#queries).
  *	
  *	
  */
  readonly total?: number;
  /**
  *	[CustomerGroups](ctp:api:type:CustomerGroup) matching the query.
  *	
  *	
  */
  readonly results: CustomerGroup[]
}
/**
*	[Reference](ctp:api:type:Reference) to a [CustomerGroup](ctp:api:type:CustomerGroup).
*	
*/
export interface CustomerGroupReference {
  readonly typeId: "customer-group";
  /**
  *	Unique identifier of the referenced [CustomerGroup](ctp:api:type:CustomerGroup).
  *	
  *	
  */
  readonly id: string;
  /**
  *	Contains the representation of the expanded CustomerGroup. Only present in responses to requests with [Reference Expansion](/../api/general-concepts#reference-expansion) for CustomerGroups.
  *	
  *	
  */
  readonly obj?: CustomerGroup
}
/**
*	[ResourceIdentifier](ctp:api:type:ResourceIdentifier) to a [CustomerGroup](ctp:api:type:CustomerGroup). Either `id` or `key` is required. If both are set, an [InvalidJsonInput](/../api/errors#invalidjsoninput) error is returned.
*	
*/
export interface CustomerGroupResourceIdentifier {
  readonly typeId: "customer-group";
  /**
  *	Unique identifier of the referenced [CustomerGroup](ctp:api:type:CustomerGroup). Required if `key` is absent.
  *	
  *	
  */
  readonly id?: string;
  /**
  *	User-defined unique identifier of the referenced [CustomerGroup](ctp:api:type:CustomerGroup). Required if `id` is absent.
  *	
  *	
  */
  readonly key?: string
}
export interface CustomerGroupUpdate  {
  /**
  *	Expected version of the CustomerGroup on which the changes should be applied.
  *	If the expected version does not match the actual version, a [ConcurrentModification](ctp:api:type:ConcurrentModificationError) error will be returned.
  *	
  *	
  */
  readonly version: number;
  /**
  *	Update actions to be performed on the CustomerGroup.
  *	
  *	
  */
  readonly actions: CustomerGroupUpdateAction[]
}
export type CustomerGroupUpdateAction =
  CustomerGroupChangeNameAction |
  CustomerGroupSetCustomFieldAction |
  CustomerGroupSetCustomTypeAction |
  CustomerGroupSetKeyAction
;
export interface CustomerGroupChangeNameAction {
  readonly action: "changeName";
  /**
  *	New name of the CustomerGroup.
  *	
  *	
  */
  readonly name: string
}
export interface CustomerGroupSetCustomFieldAction {
  readonly action: "setCustomField";
  /**
  *	Name of the [Custom Field](/../api/projects/custom-fields).
  *	
  *	
  */
  readonly name: string;
  /**
  *	If `value` is absent or `null`, this field will be removed if it exists.
  *	Removing a field that does not exist returns an [InvalidOperation](ctp:api:type:InvalidOperationError) error.
  *	If `value` is provided, it is set for the field defined by `name`.
  *	
  *	
  */
  readonly value?: any
}
/**
*	This action sets or removes the custom type for an existing CustomerGroup.
*	If present, this action overwrites any existing [custom](/../api/projects/custom-fields) type and fields.
*	
*/
export interface CustomerGroupSetCustomTypeAction {
  readonly action: "setCustomType";
  /**
  *	Defines the [Type](ctp:api:type:Type) that extends the CustomerGroup with [Custom Fields](/../api/projects/custom-fields).
  *	If absent, any existing Type and Custom Fields are removed from the CustomerGroup.
  *	
  *	
  */
  readonly type?: TypeResourceIdentifier;
  /**
  *	Sets the [Custom Fields](/../api/projects/custom-fields) fields for the CustomerGroup.
  *	
  *	
  */
  readonly fields?: FieldContainer
}
export interface CustomerGroupSetKeyAction {
  readonly action: "setKey";
  /**
  *	If `key` is absent or `null`, the existing key, if any, will be removed.
  *	
  *	
  */
  readonly key?: string
}
