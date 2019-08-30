/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { localizedStringType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'

export function stateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: stateTypeEnumType().required(),
             builtIn: Joi.boolean().required(),
             initial: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             key: Joi.string().required(),
             roles: Joi.array().items(stateRoleEnumType()).optional(),
             transitions: Joi.array().items(stateReferenceType()).optional(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional()
          })
}

export function stateDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: stateTypeEnumType().required(),
             key: Joi.string().required(),
             roles: Joi.array().items(stateRoleEnumType()).optional(),
             transitions: Joi.array().items(stateResourceIdentifierType()).optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional(),
             initial: Joi.boolean().optional()
          })
}

export function statePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(stateType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function stateReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('state'),
             id: Joi.string().required(),
             obj: stateType().optional()
          })
}

export function stateResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('state'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

const stateRoleEnumTypeValues = [

   'ReviewIncludedInStatistics'

]

export function stateRoleEnumType(): Joi.AnySchema {
   return Joi.string().only(stateRoleEnumTypeValues)
}

const stateTypeEnumTypeValues = [

   'OrderState',
   'LineItemState',
   'ProductState',
   'ReviewState',
   'PaymentState'

]

export function stateTypeEnumType(): Joi.AnySchema {
   return Joi.string().only(stateTypeEnumTypeValues)
}

export function stateUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(stateUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function stateUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([stateAddRolesActionType(), stateChangeInitialActionType(), stateChangeKeyActionType(), stateChangeTypeActionType(), stateRemoveRolesActionType(), stateSetDescriptionActionType(), stateSetNameActionType(), stateSetRolesActionType(), stateSetTransitionsActionType()])
}

export function stateAddRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(stateRoleEnumType()).required(),
             action: Joi.string().required().only('addRoles')
          })
}

export function stateChangeInitialActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             initial: Joi.boolean().required(),
             action: Joi.string().required().only('changeInitial')
          })
}

export function stateChangeKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeKey'),
             key: Joi.string().required()
          })
}

export function stateChangeTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: stateTypeEnumType().required(),
             action: Joi.string().required().only('changeType')
          })
}

export function stateRemoveRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(stateRoleEnumType()).required(),
             action: Joi.string().required().only('removeRoles')
          })
}

export function stateSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             description: localizedStringType().required(),
             action: Joi.string().required().only('setDescription')
          })
}

export function stateSetNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('setName')
          })
}

export function stateSetRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(stateRoleEnumType()).required(),
             action: Joi.string().required().only('setRoles')
          })
}

export function stateSetTransitionsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTransitions'),
             transitions: Joi.array().items(stateResourceIdentifierType()).optional()
          })
}