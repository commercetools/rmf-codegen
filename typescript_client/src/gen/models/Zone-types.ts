/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { baseResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'

export function locationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             country: Joi.string().required(),
             state: Joi.string().optional()
          })
}

export function zoneType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             locations: Joi.array().items(locationType()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             name: Joi.string().required(),
             description: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function zoneDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             locations: Joi.array().items(locationType()).required(),
             name: Joi.string().required(),
             description: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function zonePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(zoneType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function zoneReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('zone'),
             id: Joi.string().required(),
             obj: zoneType().optional()
          })
}

export function zoneResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('zone'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function zoneUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(zoneUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function zoneUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([zoneAddLocationActionType(), zoneChangeNameActionType(), zoneRemoveLocationActionType(), zoneSetDescriptionActionType(), zoneSetKeyActionType()])
}

export function zoneAddLocationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             location: locationType().required(),
             action: Joi.string().required().only('addLocation')
          })
}

export function zoneChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function zoneRemoveLocationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             location: locationType().required(),
             action: Joi.string().required().only('removeLocation')
          })
}

export function zoneSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: Joi.string().optional()
          })
}

export function zoneSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}