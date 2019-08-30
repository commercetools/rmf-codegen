/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { customFieldsType } from './Type-types'
import { channelResourceIdentifierType } from './Channel-types'
import { loggedResourceType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function inventoryEntryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             availableQuantity: Joi.number().required(),
             quantityOnStock: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             sku: Joi.string().required(),
             supplyChannel: channelResourceIdentifierType().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             expectedDelivery: Joi.date().iso().optional(),
             restockableInDays: Joi.number().optional()
          })
}

export function inventoryEntryDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantityOnStock: Joi.number().required(),
             sku: Joi.string().required(),
             supplyChannel: channelResourceIdentifierType().optional(),
             custom: customFieldsDraftType().optional(),
             expectedDelivery: Joi.date().iso().optional(),
             restockableInDays: Joi.number().optional()
          })
}

export function inventoryEntryReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('inventory-entry'),
             id: Joi.string().required(),
             obj: inventoryEntryType().optional()
          })
}

export function inventoryEntryResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('inventory-entry'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function inventoryPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(inventoryEntryType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function inventoryUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(inventoryUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function inventoryUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([inventoryAddQuantityActionType(), inventoryChangeQuantityActionType(), inventoryRemoveQuantityActionType(), inventorySetCustomFieldActionType(), inventorySetCustomTypeActionType(), inventorySetExpectedDeliveryActionType(), inventorySetRestockableInDaysActionType(), inventorySetSupplyChannelActionType()])
}

export function inventoryAddQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('addQuantity')
          })
}

export function inventoryChangeQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeQuantity')
          })
}

export function inventoryRemoveQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('removeQuantity')
          })
}

export function inventorySetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function inventorySetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function inventorySetExpectedDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setExpectedDelivery'),
             expectedDelivery: Joi.date().iso().optional()
          })
}

export function inventorySetRestockableInDaysActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setRestockableInDays'),
             restockableInDays: Joi.number().optional()
          })
}

export function inventorySetSupplyChannelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setSupplyChannel'),
             supplyChannel: channelResourceIdentifierType().optional()
          })
}