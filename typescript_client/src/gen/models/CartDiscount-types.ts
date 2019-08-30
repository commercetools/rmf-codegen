/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { referenceType } from './Common-types'
import { customFieldsType } from './Type-types'
import { localizedStringType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { moneyType } from './Common-types'
import { productReferenceType } from './Product-types'
import { channelReferenceType } from './Channel-types'
import { typeResourceIdentifierType } from './Type-types'

export function cartDiscountType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             references: Joi.array().items(referenceType()).required(),
             value: cartDiscountValueType().required(),
             name: localizedStringType().required(),
             stackingMode: stackingModeType().required(),
             isActive: Joi.boolean().required(),
             requiresDiscountCode: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             cartPredicate: Joi.string().required(),
             id: Joi.string().required(),
             sortOrder: Joi.string().required(),
             target: cartDiscountTargetType().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             key: Joi.string().optional()
          })
}

export function cartDiscountCustomLineItemsTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             predicate: Joi.string().required(),
             type: Joi.string().required().only('customLineItems')
          })
}

export function cartDiscountDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: cartDiscountValueType().required(),
             name: localizedStringType().required(),
             requiresDiscountCode: Joi.boolean().required(),
             cartPredicate: Joi.string().required(),
             sortOrder: Joi.string().required(),
             target: cartDiscountTargetType().optional(),
             custom: customFieldsType().optional(),
             description: localizedStringType().optional(),
             stackingMode: stackingModeType().optional(),
             isActive: Joi.boolean().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             key: Joi.string().optional()
          })
}

export function cartDiscountLineItemsTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             predicate: Joi.string().required(),
             type: Joi.string().required().only('lineItems')
          })
}

export function cartDiscountPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(cartDiscountType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function cartDiscountReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('cart-discount'),
             id: Joi.string().required(),
             obj: cartDiscountType().optional()
          })
}

export function cartDiscountResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('cart-discount'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function cartDiscountShippingCostTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('shipping')
          })
}

export function cartDiscountTargetType(): Joi.AnySchema {
   return Joi.alternatives([multiBuyCustomLineItemsTargetType(), multiBuyLineItemsTargetType(), cartDiscountCustomLineItemsTargetType(), cartDiscountLineItemsTargetType(), cartDiscountShippingCostTargetType()])
}

export function cartDiscountUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(cartDiscountUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function cartDiscountUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([cartDiscountChangeCartPredicateActionType(), cartDiscountChangeIsActiveActionType(), cartDiscountChangeNameActionType(), cartDiscountChangeRequiresDiscountCodeActionType(), cartDiscountChangeSortOrderActionType(), cartDiscountChangeStackingModeActionType(), cartDiscountChangeTargetActionType(), cartDiscountChangeValueActionType(), cartDiscountSetCustomFieldActionType(), cartDiscountSetCustomTypeActionType(), cartDiscountSetDescriptionActionType(), cartDiscountSetKeyActionType(), cartDiscountSetValidFromActionType(), cartDiscountSetValidFromAndUntilActionType(), cartDiscountSetValidUntilActionType()])
}

export function cartDiscountValueType(): Joi.AnySchema {
   return Joi.alternatives([cartDiscountValueAbsoluteType(), cartDiscountValueGiftLineItemType(), cartDiscountValueRelativeType()])
}

export function cartDiscountValueAbsoluteType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             money: Joi.array().items(moneyType()).required(),
             type: Joi.string().required().only('absolute')
          })
}

export function cartDiscountValueGiftLineItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             product: productReferenceType().required(),
             variantId: Joi.number().required(),
             type: Joi.string().required().only('giftLineItem'),
             distributionChannel: channelReferenceType().optional(),
             supplyChannel: channelReferenceType().optional()
          })
}

export function cartDiscountValueRelativeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             permyriad: Joi.number().required(),
             type: Joi.string().required().only('relative')
          })
}

export function multiBuyCustomLineItemsTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             selectionMode: selectionModeType().required(),
             discountedQuantity: Joi.number().required(),
             triggerQuantity: Joi.number().required(),
             predicate: Joi.string().required(),
             type: Joi.string().required().only('multiBuyCustomLineItems'),
             maxOccurrence: Joi.number().optional()
          })
}

export function multiBuyLineItemsTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             selectionMode: selectionModeType().required(),
             discountedQuantity: Joi.number().required(),
             triggerQuantity: Joi.number().required(),
             predicate: Joi.string().required(),
             type: Joi.string().required().only('multiBuyLineItems'),
             maxOccurrence: Joi.number().optional()
          })
}

const selectionModeTypeValues = [

   'Cheapest',
   'MostExpensive'

]

export function selectionModeType(): Joi.AnySchema {
   return Joi.string().only(selectionModeTypeValues)
}

const stackingModeTypeValues = [

   'Stacking',
   'StopAfterThisDiscount'

]

export function stackingModeType(): Joi.AnySchema {
   return Joi.string().only(stackingModeTypeValues)
}

export function cartDiscountChangeCartPredicateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeCartPredicate'),
             cartPredicate: Joi.string().required()
          })
}

export function cartDiscountChangeIsActiveActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isActive: Joi.boolean().required(),
             action: Joi.string().required().only('changeIsActive')
          })
}

export function cartDiscountChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function cartDiscountChangeRequiresDiscountCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             requiresDiscountCode: Joi.boolean().required(),
             action: Joi.string().required().only('changeRequiresDiscountCode')
          })
}

export function cartDiscountChangeSortOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeSortOrder'),
             sortOrder: Joi.string().required()
          })
}

export function cartDiscountChangeStackingModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             stackingMode: stackingModeType().required(),
             action: Joi.string().required().only('changeStackingMode')
          })
}

export function cartDiscountChangeTargetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             target: cartDiscountTargetType().required(),
             action: Joi.string().required().only('changeTarget')
          })
}

export function cartDiscountChangeValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: cartDiscountValueType().required(),
             action: Joi.string().required().only('changeValue')
          })
}

export function cartDiscountSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function cartDiscountSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             type: typeResourceIdentifierType().optional(),
             fields: Joi.any().optional()
          })
}

export function cartDiscountSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}

export function cartDiscountSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function cartDiscountSetValidFromActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFrom'),
             validFrom: Joi.date().iso().optional()
          })
}

export function cartDiscountSetValidFromAndUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFromAndUntil'),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional()
          })
}

export function cartDiscountSetValidUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidUntil'),
             validUntil: Joi.date().iso().optional()
          })
}