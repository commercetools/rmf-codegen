/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { customFieldsType } from './Type-types'
import { loggedResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function customerGroupType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             name: Joi.string().required(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             key: Joi.string().optional()
          })
}

export function customerGroupDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             groupName: Joi.string().required(),
             custom: customFieldsType().optional(),
             key: Joi.string().optional()
          })
}

export function customerGroupPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(customerGroupType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function customerGroupReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('customer-group'),
             id: Joi.string().required(),
             obj: customerGroupType().optional()
          })
}

export function customerGroupResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('customer-group'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function customerGroupUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(customerGroupUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function customerGroupUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([customerGroupChangeNameActionType(), customerGroupSetCustomFieldActionType(), customerGroupSetCustomTypeActionType(), customerGroupSetKeyActionType()])
}

export function customerGroupChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function customerGroupSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function customerGroupSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function customerGroupSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}