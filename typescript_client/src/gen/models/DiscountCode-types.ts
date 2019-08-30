/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { cartDiscountReferenceType } from './CartDiscount-types'
import { referenceType } from './Common-types'
import { customFieldsType } from './Type-types'
import { localizedStringType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { cartDiscountResourceIdentifierType } from './CartDiscount-types'
import { customFieldsDraftType } from './Type-types'
import { referenceTypeIdType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function discountCodeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             cartDiscounts: Joi.array().items(cartDiscountReferenceType()).required(),
             groups: Joi.array().items(Joi.string()).required(),
             references: Joi.array().items(referenceType()).required(),
             isActive: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             code: Joi.string().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             maxApplications: Joi.number().optional(),
             maxApplicationsPerCustomer: Joi.number().optional(),
             cartPredicate: Joi.string().optional()
          })
}

export function discountCodeDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             cartDiscounts: Joi.array().items(cartDiscountResourceIdentifierType()).required(),
             code: Joi.string().required(),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             name: localizedStringType().optional(),
             groups: Joi.array().items(Joi.string()).optional(),
             isActive: Joi.boolean().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             maxApplications: Joi.number().optional(),
             maxApplicationsPerCustomer: Joi.number().optional(),
             cartPredicate: Joi.string().optional()
          })
}

export function discountCodePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(discountCodeType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function discountCodeReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('discount-code'),
             id: Joi.string().required(),
             obj: discountCodeType().optional()
          })
}

export function discountCodeResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('discount-code'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function discountCodeUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(discountCodeUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function discountCodeUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([discountCodeChangeCartDiscountsActionType(), discountCodeChangeGroupsActionType(), discountCodeChangeIsActiveActionType(), discountCodeSetCartPredicateActionType(), discountCodeSetCustomFieldActionType(), discountCodeSetCustomTypeActionType(), discountCodeSetDescriptionActionType(), discountCodeSetMaxApplicationsActionType(), discountCodeSetMaxApplicationsPerCustomerActionType(), discountCodeSetNameActionType(), discountCodeSetValidFromActionType(), discountCodeSetValidFromAndUntilActionType(), discountCodeSetValidUntilActionType()])
}

export function discountCodeChangeCartDiscountsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             cartDiscounts: Joi.array().items(cartDiscountResourceIdentifierType()).required(),
             action: Joi.string().required().only('changeCartDiscounts')
          })
}

export function discountCodeChangeGroupsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             groups: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeGroups')
          })
}

export function discountCodeChangeIsActiveActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isActive: Joi.boolean().required(),
             action: Joi.string().required().only('changeIsActive')
          })
}

export function discountCodeSetCartPredicateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCartPredicate'),
             cartPredicate: Joi.string().optional()
          })
}

export function discountCodeSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function discountCodeSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function discountCodeSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}

export function discountCodeSetMaxApplicationsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMaxApplications'),
             maxApplications: Joi.number().optional()
          })
}

export function discountCodeSetMaxApplicationsPerCustomerActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMaxApplicationsPerCustomer'),
             maxApplicationsPerCustomer: Joi.number().optional()
          })
}

export function discountCodeSetNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setName'),
             name: localizedStringType().optional()
          })
}

export function discountCodeSetValidFromActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFrom'),
             validFrom: Joi.date().iso().optional()
          })
}

export function discountCodeSetValidFromAndUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidFromAndUntil'),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional()
          })
}

export function discountCodeSetValidUntilActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setValidUntil'),
             validUntil: Joi.date().iso().optional()
          })
}