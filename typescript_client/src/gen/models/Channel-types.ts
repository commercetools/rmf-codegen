/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { addressType } from './Common-types'
import { customFieldsType } from './Type-types'
import { localizedStringType } from './Common-types'
import { reviewRatingStatisticsType } from './Review-types'
import { loggedResourceType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function channelType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(channelRoleEnumType()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             key: Joi.string().required(),
             geoLocation: Joi.any().optional(),
             address: addressType().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional(),
             reviewRatingStatistics: reviewRatingStatisticsType().optional()
          })
}

export function channelDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             key: Joi.string().required(),
             geoLocation: Joi.any().optional(),
             roles: Joi.array().items(channelRoleEnumType()).optional(),
             address: addressType().optional(),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional()
          })
}

export function channelPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(channelType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function channelReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('channel'),
             id: Joi.string().required(),
             obj: channelType().optional()
          })
}

export function channelResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('channel'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

const channelRoleEnumTypeValues = [

   'InventorySupply',
   'ProductDistribution',
   'OrderExport',
   'OrderImport',
   'Primary'

]

export function channelRoleEnumType(): Joi.AnySchema {
   return Joi.string().only(channelRoleEnumTypeValues)
}

export function channelUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(channelUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function channelUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([channelAddRolesActionType(), channelChangeDescriptionActionType(), channelChangeKeyActionType(), channelChangeNameActionType(), channelRemoveRolesActionType(), channelSetAddressActionType(), channelSetCustomFieldActionType(), channelSetCustomTypeActionType(), channelSetGeoLocationActionType(), channelSetRolesActionType()])
}

export function channelAddRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(channelRoleEnumType()).required(),
             action: Joi.string().required().only('addRoles')
          })
}

export function channelChangeDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             description: localizedStringType().required(),
             action: Joi.string().required().only('changeDescription')
          })
}

export function channelChangeKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeKey'),
             key: Joi.string().required()
          })
}

export function channelChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function channelRemoveRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(channelRoleEnumType()).required(),
             action: Joi.string().required().only('removeRoles')
          })
}

export function channelSetAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAddress'),
             address: addressType().optional()
          })
}

export function channelSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function channelSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function channelSetGeoLocationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setGeoLocation'),
             geoLocation: Joi.any().optional()
          })
}

export function channelSetRolesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             roles: Joi.array().items(channelRoleEnumType()).required(),
             action: Joi.string().required().only('setRoles')
          })
}