/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { moneyType } from './Common-types'
import { taxCategoryReferenceType } from './TaxCategory-types'
import { baseResourceType } from './Common-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { typedMoneyType } from './Common-types'
import { zoneReferenceType } from './Zone-types'
import { zoneResourceIdentifierType } from './Zone-types'

export function cartClassificationTierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: moneyType().required(),
             type: shippingRateTierTypeType().required().only('CartClassification'),
             value: Joi.string().required(),
             isMatching: Joi.boolean().optional()
          })
}

export function cartScoreTierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: shippingRateTierTypeType().required().only('CartScore'),
             score: Joi.number().required(),
             price: moneyType().optional(),
             priceFunction: priceFunctionType().optional(),
             isMatching: Joi.boolean().optional()
          })
}

export function cartValueTierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: moneyType().required(),
             type: shippingRateTierTypeType().required().only('CartValue'),
             minimumCentAmount: Joi.number().required(),
             isMatching: Joi.boolean().optional()
          })
}

export function priceFunctionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currencyCode: Joi.string().required(),
             function: Joi.string().required()
          })
}

export function shippingMethodType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             zoneRates: Joi.array().items(zoneRateType()).required(),
             taxCategory: taxCategoryReferenceType().required(),
             isDefault: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             name: Joi.string().required(),
             description: Joi.string().optional(),
             key: Joi.string().optional(),
             predicate: Joi.string().optional()
          })
}

export function shippingMethodDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             zoneRates: Joi.array().items(zoneRateDraftType()).required(),
             taxCategory: taxCategoryResourceIdentifierType().required(),
             isDefault: Joi.boolean().required(),
             name: Joi.string().required(),
             description: Joi.string().optional(),
             key: Joi.string().optional(),
             predicate: Joi.string().optional()
          })
}

export function shippingMethodPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(shippingMethodType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function shippingMethodReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('shipping-method'),
             id: Joi.string().required(),
             obj: shippingMethodType().optional()
          })
}

export function shippingMethodResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('shipping-method'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function shippingMethodUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(shippingMethodUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function shippingMethodUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([shippingMethodAddShippingRateActionType(), shippingMethodAddZoneActionType(), shippingMethodChangeIsDefaultActionType(), shippingMethodChangeNameActionType(), shippingMethodChangeTaxCategoryActionType(), shippingMethodRemoveShippingRateActionType(), shippingMethodRemoveZoneActionType(), shippingMethodSetDescriptionActionType(), shippingMethodSetKeyActionType(), shippingMethodSetPredicateActionType()])
}

export function shippingRateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             tiers: Joi.array().items(shippingRatePriceTierType()).required(),
             price: typedMoneyType().required(),
             freeAbove: typedMoneyType().optional(),
             isMatching: Joi.boolean().optional()
          })
}

export function shippingRateDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: moneyType().required(),
             tiers: Joi.array().items(shippingRatePriceTierType()).optional(),
             freeAbove: moneyType().optional()
          })
}

export function shippingRatePriceTierType(): Joi.AnySchema {
   return Joi.alternatives([cartClassificationTierType(), cartScoreTierType(), cartValueTierType()])
}

const shippingRateTierTypeTypeValues = [

   'CartValue',
   'CartClassification',
   'CartScore'

]

export function shippingRateTierTypeType(): Joi.AnySchema {
   return Joi.string().only(shippingRateTierTypeTypeValues)
}

export function zoneRateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRates: Joi.array().items(shippingRateType()).required(),
             zone: zoneReferenceType().required()
          })
}

export function zoneRateDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRates: Joi.array().items(shippingRateDraftType()).required(),
             zone: zoneResourceIdentifierType().required()
          })
}

export function shippingMethodAddShippingRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRate: shippingRateDraftType().required(),
             zone: zoneResourceIdentifierType().required(),
             action: Joi.string().required().only('addShippingRate')
          })
}

export function shippingMethodAddZoneActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             zone: zoneResourceIdentifierType().required(),
             action: Joi.string().required().only('addZone')
          })
}

export function shippingMethodChangeIsDefaultActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isDefault: Joi.boolean().required(),
             action: Joi.string().required().only('changeIsDefault')
          })
}

export function shippingMethodChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function shippingMethodChangeTaxCategoryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxCategory: taxCategoryResourceIdentifierType().required(),
             action: Joi.string().required().only('changeTaxCategory')
          })
}

export function shippingMethodRemoveShippingRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRate: shippingRateDraftType().required(),
             zone: zoneResourceIdentifierType().required(),
             action: Joi.string().required().only('removeShippingRate')
          })
}

export function shippingMethodRemoveZoneActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             zone: zoneResourceIdentifierType().required(),
             action: Joi.string().required().only('removeZone')
          })
}

export function shippingMethodSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: Joi.string().optional()
          })
}

export function shippingMethodSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function shippingMethodSetPredicateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setPredicate'),
             predicate: Joi.string().optional()
          })
}