/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { localizedStringType } from './Common-types'
import { baseResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { keyReferenceType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'

export function storeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             key: Joi.string().required(),
             name: localizedStringType().optional()
          })
}

export function storeDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             key: Joi.string().required()
          })
}

export function storeKeyReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('store'),
             key: Joi.string().required()
          })
}

export function storePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(storeType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function storeReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('store'),
             id: Joi.string().required(),
             obj: storeType().optional()
          })
}

export function storeResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('store'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function storeUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(storeUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function storeUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([storeSetNameActionType()])
}

export function storeSetNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setName'),
             name: localizedStringType().optional()
          })
}