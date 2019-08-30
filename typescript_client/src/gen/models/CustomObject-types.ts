/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { baseResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'

export function customObjectType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: Joi.any().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             container: Joi.string().required(),
             id: Joi.string().required(),
             key: Joi.string().required()
          })
}

export function customObjectDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: Joi.any().required(),
             container: Joi.string().required(),
             key: Joi.string().required(),
             version: Joi.number().optional()
          })
}

export function customObjectPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(customObjectType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function customObjectReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('key-value-document'),
             id: Joi.string().required(),
             obj: customObjectType().optional()
          })
}