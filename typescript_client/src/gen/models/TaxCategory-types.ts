/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'

export function subRateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             amount: Joi.number().required(),
             name: Joi.string().required()
          })
}

export function taxCategoryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             rates: Joi.array().items(taxRateType()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             name: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function taxCategoryDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             rates: Joi.array().items(taxRateDraftType()).required(),
             name: Joi.string().required(),
             description: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function taxCategoryPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(taxCategoryType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function taxCategoryReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('tax-category'),
             id: Joi.string().required(),
             obj: taxCategoryType().optional()
          })
}

export function taxCategoryResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('tax-category'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function taxCategoryUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(taxCategoryUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function taxCategoryUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([taxCategoryAddTaxRateActionType(), taxCategoryChangeNameActionType(), taxCategoryRemoveTaxRateActionType(), taxCategoryReplaceTaxRateActionType(), taxCategorySetDescriptionActionType(), taxCategorySetKeyActionType()])
}

export function taxRateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             country: Joi.string().required(),
             includedInPrice: Joi.boolean().required(),
             amount: Joi.number().required(),
             name: Joi.string().required(),
             subRates: Joi.array().items(subRateType()).optional(),
             id: Joi.string().optional(),
             state: Joi.string().optional()
          })
}

export function taxRateDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             country: Joi.string().required(),
             includedInPrice: Joi.boolean().required(),
             name: Joi.string().required(),
             subRates: Joi.array().items(subRateType()).optional(),
             amount: Joi.number().optional(),
             state: Joi.string().optional()
          })
}

export function taxCategoryAddTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxRate: taxRateDraftType().required(),
             action: Joi.string().required().only('addTaxRate')
          })
}

export function taxCategoryChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function taxCategoryRemoveTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeTaxRate'),
             taxRateId: Joi.string().required()
          })
}

export function taxCategoryReplaceTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxRate: taxRateDraftType().required(),
             action: Joi.string().required().only('replaceTaxRate'),
             taxRateId: Joi.string().required()
          })
}

export function taxCategorySetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: Joi.string().optional()
          })
}

export function taxCategorySetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}