/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { referenceType } from './Common-types'
import { localizedStringType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { priceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { moneyType } from './Common-types'

export function productDiscountType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             references: Joi.array().items(referenceType()).required(),
             name: localizedStringType().required(),
             value: productDiscountValueType().required(),
             isActive: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             predicate: Joi.string().required(),
             sortOrder: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             key: Joi.string().optional()
          })
}

export function productDiscountDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             value: productDiscountValueType().required(),
             isActive: Joi.boolean().required(),
             predicate: Joi.string().required(),
             sortOrder: Joi.string().required(),
             description: localizedStringType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             key: Joi.string().optional()
          })
}

export function productDiscountMatchQueryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: priceType().required(),
             staged: Joi.boolean().required(),
             variantId: Joi.number().required(),
             productId: Joi.string().required()
          })
}

export function productDiscountPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(productDiscountType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function productDiscountReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('product-discount'),
             id: Joi.string().required(),
             obj: productDiscountType().optional()
          })
}

export function productDiscountResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('product-discount'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function productDiscountUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(productDiscountUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function productDiscountUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([productDiscountChangeIsActiveActionType(), productDiscountChangeNameActionType(), productDiscountChangePredicateActionType(), productDiscountChangeSortOrderActionType(), productDiscountChangeValueActionType(), productDiscountSetDescriptionActionType(), productDiscountSetKeyActionType(), productDiscountSetValidFromActionType(), productDiscountSetValidFromAndUntilActionType(), productDiscountSetValidUntilActionType()])
}

export function productDiscountValueType(): Joi.AnySchema {
   return Joi.alternatives([productDiscountValueAbsoluteType(), productDiscountValueExternalType(), productDiscountValueRelativeType()])
}

export function productDiscountValueAbsoluteType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             money: Joi.array().items(moneyType()).required(),
             type: Joi.string().required().only('absolute')
          })
}

export function productDiscountValueExternalType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('external')
          })
}

export function productDiscountValueRelativeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             permyriad: Joi.number().required(),
             type: Joi.string().required().only('relative')
          })
}

export function productDiscountChangeIsActiveActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isActive: Joi.boolean().required(),
             action: Joi.string().required().only('changeIsActive')
          })
}

export function productDiscountChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function productDiscountChangePredicateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changePredicate'),
             predicate: Joi.string().required()
          })
}

export function productDiscountChangeSortOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeSortOrder'),
             sortOrder: Joi.string().required()
          })
}

export function productDiscountChangeValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: productDiscountValueType().required(),
             action: Joi.string().required().only('changeValue')
          })
}

export function productDiscountSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}

export function productDiscountSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function productDiscountSetValidFromActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFrom'),
             validFrom: Joi.date().iso().optional()
          })
}

export function productDiscountSetValidFromAndUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFromAndUntil'),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional()
          })
}

export function productDiscountSetValidUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidUntil'),
             validUntil: Joi.date().iso().optional()
          })
}