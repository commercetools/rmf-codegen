/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { attributeType } from './Product-types'
import { referenceType } from './Common-types'
import { priceType } from './Common-types'
import { localizedStringType } from './Common-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { channelReferenceType } from './Channel-types'
import { referenceTypeIdType } from './Common-types'

export function accessDeniedErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('access_denied'),
             message: Joi.string().required()
          })
}

export function concurrentModificationErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('ConcurrentModification'),
             message: Joi.string().required(),
             currentVersion: Joi.number().optional()
          })
}

export function discountCodeNonApplicableErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('DiscountCodeNonApplicable'),
             message: Joi.string().required(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             validityCheckTime: Joi.date().iso().optional(),
             dicountCodeId: Joi.string().optional(),
             discountCode: Joi.string().optional(),
             reason: Joi.string().optional()
          })
}

export function duplicateAttributeValueErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attribute: attributeType().required(),
             code: Joi.string().required().only('DuplicateAttributeValue'),
             message: Joi.string().required()
          })
}

export function duplicateAttributeValuesErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributes: Joi.array().items(attributeType()).required(),
             code: Joi.string().required().only('DuplicateAttributeValues'),
             message: Joi.string().required()
          })
}

export function duplicateFieldErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('DuplicateField'),
             message: Joi.string().required(),
             duplicateValue: Joi.any().optional(),
             field: Joi.string().optional()
          })
}

export function duplicateFieldWithConflictingResourceErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             conflictingResource: referenceType().required(),
             duplicateValue: Joi.any().required(),
             code: Joi.string().required().only('DuplicateFieldWithConflictingResource'),
             field: Joi.string().required(),
             message: Joi.string().required()
          })
}

export function duplicatePriceScopeErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             conflictingPrices: Joi.array().items(priceType()).required(),
             code: Joi.string().required().only('DuplicatePriceScope'),
             message: Joi.string().required()
          })
}

export function duplicateVariantValuesErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variantValues: variantValuesType().required(),
             code: Joi.string().required().only('DuplicateVariantValues'),
             message: Joi.string().required()
          })
}

export function enumValueIsUsedErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('EnumValueIsUsed'),
             message: Joi.string().required()
          })
}

export function errorByExtensionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             id: Joi.string().required(),
             key: Joi.string().optional()
          })
}

export function errorObjectType(): Joi.AnySchema {
   return Joi.alternatives([extensionBadResponseErrorType(), extensionNoResponseErrorType(), extensionUpdateActionsFailedErrorType(), insufficientScopeErrorType(), invalidCredentialsErrorType(), invalidCurrentPasswordErrorType(), invalidFieldErrorType(), invalidInputErrorType(), invalidItemShippingDetailsErrorType(), invalidJsonInputErrorType(), invalidOperationErrorType(), invalidSubjectErrorType(), invalidTokenErrorType(), matchingPriceNotFoundErrorType(), missingTaxRateForCountryErrorType(), noMatchingProductDiscountFoundErrorType(), outOfStockErrorType(), priceChangedErrorType(), referenceExistsErrorType(), requiredFieldErrorType(), resourceNotFoundErrorType(), shippingMethodDoesNotMatchCartErrorType(), accessDeniedErrorType(), concurrentModificationErrorType(), discountCodeNonApplicableErrorType(), duplicateAttributeValueErrorType(), duplicateAttributeValuesErrorType(), duplicateFieldErrorType(), duplicateFieldWithConflictingResourceErrorType(), duplicatePriceScopeErrorType(), duplicateVariantValuesErrorType(), enumValueIsUsedErrorType()])
}

export function errorResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             statusCode: Joi.number().required(),
             message: Joi.string().required(),
             errors: Joi.array().items(errorObjectType()).optional(),
             error: Joi.string().optional(),
             error_description: Joi.string().optional()
          })
}

export function extensionBadResponseErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             errorByExtension: errorByExtensionType().required(),
             code: Joi.string().required().only('ExtensionBadResponse'),
             message: Joi.string().required(),
             localizedMessage: localizedStringType().optional(),
             extensionExtraInfo: Joi.any().optional()
          })
}

export function extensionNoResponseErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             errorByExtension: errorByExtensionType().required(),
             code: Joi.string().required().only('ExtensionNoResponse'),
             message: Joi.string().required(),
             localizedMessage: localizedStringType().optional(),
             extensionExtraInfo: Joi.any().optional()
          })
}

export function extensionUpdateActionsFailedErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             errorByExtension: errorByExtensionType().required(),
             code: Joi.string().required().only('ExtensionUpdateActionsFailed'),
             message: Joi.string().required(),
             localizedMessage: localizedStringType().optional(),
             extensionExtraInfo: Joi.any().optional()
          })
}

export function insufficientScopeErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('insufficient_scope'),
             message: Joi.string().required()
          })
}

export function invalidCredentialsErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidCredentials'),
             message: Joi.string().required()
          })
}

export function invalidCurrentPasswordErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidCurrentPassword'),
             message: Joi.string().required()
          })
}

export function invalidFieldErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             invalidValue: Joi.any().required(),
             code: Joi.string().required().only('InvalidField'),
             field: Joi.string().required(),
             message: Joi.string().required(),
             allowedValues: Joi.array().items(Joi.any()).optional()
          })
}

export function invalidInputErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidInput'),
             message: Joi.string().required()
          })
}

export function invalidItemShippingDetailsErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidItemShippingDetails'),
             itemId: Joi.string().required(),
             message: Joi.string().required(),
             subject: Joi.string().required()
          })
}

export function invalidJsonInputErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidJsonInput'),
             message: Joi.string().required()
          })
}

export function invalidOperationErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidOperation'),
             message: Joi.string().required()
          })
}

export function invalidSubjectErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('InvalidSubject'),
             message: Joi.string().required()
          })
}

export function invalidTokenErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('invalid_token'),
             message: Joi.string().required()
          })
}

export function matchingPriceNotFoundErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variantId: Joi.number().required(),
             code: Joi.string().required().only('MatchingPriceNotFound'),
             message: Joi.string().required(),
             productId: Joi.string().required(),
             channel: channelReferenceType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             country: Joi.string().optional(),
             currency: Joi.string().optional()
          })
}

export function missingTaxRateForCountryErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('MissingTaxRateForCountry'),
             message: Joi.string().required(),
             taxCategoryId: Joi.string().required(),
             country: Joi.string().optional(),
             state: Joi.string().optional()
          })
}

export function noMatchingProductDiscountFoundErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('NoMatchingProductDiscountFound'),
             message: Joi.string().required()
          })
}

export function outOfStockErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             lineItems: Joi.array().items(Joi.string()).required(),
             skus: Joi.array().items(Joi.string()).required(),
             code: Joi.string().required().only('OutOfStock'),
             message: Joi.string().required()
          })
}

export function priceChangedErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             lineItems: Joi.array().items(Joi.string()).required(),
             shipping: Joi.boolean().required(),
             code: Joi.string().required().only('PriceChanged'),
             message: Joi.string().required()
          })
}

export function referenceExistsErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('ReferenceExists'),
             message: Joi.string().required(),
             referencedBy: referenceTypeIdType().optional()
          })
}

export function requiredFieldErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('RequiredField'),
             field: Joi.string().required(),
             message: Joi.string().required()
          })
}

export function resourceNotFoundErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('ResourceNotFound'),
             message: Joi.string().required()
          })
}

export function shippingMethodDoesNotMatchCartErrorType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             code: Joi.string().required().only('ShippingMethodDoesNotMatchCart'),
             message: Joi.string().required()
          })
}

export function variantValuesType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributes: Joi.array().items(attributeType()).required(),
             prices: Joi.array().items(priceType()).required(),
             sku: Joi.string().optional()
          })
}