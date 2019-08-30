"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Product_types_1 = require("./Product-types");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Channel_types_1 = require("./Channel-types");
const Common_types_4 = require("./Common-types");
function accessDeniedErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('access_denied'),
        message: Joi.string().required()
    });
}
exports.accessDeniedErrorType = accessDeniedErrorType;
function concurrentModificationErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('ConcurrentModification'),
        message: Joi.string().required(),
        currentVersion: Joi.number().optional()
    });
}
exports.concurrentModificationErrorType = concurrentModificationErrorType;
function discountCodeNonApplicableErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('DiscountCodeNonApplicable'),
        message: Joi.string().required(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        validityCheckTime: Joi.date().iso().optional(),
        dicountCodeId: Joi.string().optional(),
        discountCode: Joi.string().optional(),
        reason: Joi.string().optional()
    });
}
exports.discountCodeNonApplicableErrorType = discountCodeNonApplicableErrorType;
function duplicateAttributeValueErrorType() {
    return Joi.object().unknown().keys({
        attribute: Product_types_1.attributeType().required(),
        code: Joi.string().required().only('DuplicateAttributeValue'),
        message: Joi.string().required()
    });
}
exports.duplicateAttributeValueErrorType = duplicateAttributeValueErrorType;
function duplicateAttributeValuesErrorType() {
    return Joi.object().unknown().keys({
        attributes: Joi.array().items(Product_types_1.attributeType()).required(),
        code: Joi.string().required().only('DuplicateAttributeValues'),
        message: Joi.string().required()
    });
}
exports.duplicateAttributeValuesErrorType = duplicateAttributeValuesErrorType;
function duplicateFieldErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('DuplicateField'),
        message: Joi.string().required(),
        duplicateValue: Joi.any().optional(),
        field: Joi.string().optional()
    });
}
exports.duplicateFieldErrorType = duplicateFieldErrorType;
function duplicateFieldWithConflictingResourceErrorType() {
    return Joi.object().unknown().keys({
        conflictingResource: Common_types_1.referenceType().required(),
        duplicateValue: Joi.any().required(),
        code: Joi.string().required().only('DuplicateFieldWithConflictingResource'),
        field: Joi.string().required(),
        message: Joi.string().required()
    });
}
exports.duplicateFieldWithConflictingResourceErrorType = duplicateFieldWithConflictingResourceErrorType;
function duplicatePriceScopeErrorType() {
    return Joi.object().unknown().keys({
        conflictingPrices: Joi.array().items(Common_types_2.priceType()).required(),
        code: Joi.string().required().only('DuplicatePriceScope'),
        message: Joi.string().required()
    });
}
exports.duplicatePriceScopeErrorType = duplicatePriceScopeErrorType;
function duplicateVariantValuesErrorType() {
    return Joi.object().unknown().keys({
        variantValues: variantValuesType().required(),
        code: Joi.string().required().only('DuplicateVariantValues'),
        message: Joi.string().required()
    });
}
exports.duplicateVariantValuesErrorType = duplicateVariantValuesErrorType;
function enumValueIsUsedErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('EnumValueIsUsed'),
        message: Joi.string().required()
    });
}
exports.enumValueIsUsedErrorType = enumValueIsUsedErrorType;
function errorByExtensionType() {
    return Joi.object().unknown().keys({
        id: Joi.string().required(),
        key: Joi.string().optional()
    });
}
exports.errorByExtensionType = errorByExtensionType;
function errorObjectType() {
    return Joi.alternatives([extensionBadResponseErrorType(), extensionNoResponseErrorType(), extensionUpdateActionsFailedErrorType(), insufficientScopeErrorType(), invalidCredentialsErrorType(), invalidCurrentPasswordErrorType(), invalidFieldErrorType(), invalidInputErrorType(), invalidItemShippingDetailsErrorType(), invalidJsonInputErrorType(), invalidOperationErrorType(), invalidSubjectErrorType(), invalidTokenErrorType(), matchingPriceNotFoundErrorType(), missingTaxRateForCountryErrorType(), noMatchingProductDiscountFoundErrorType(), outOfStockErrorType(), priceChangedErrorType(), referenceExistsErrorType(), requiredFieldErrorType(), resourceNotFoundErrorType(), shippingMethodDoesNotMatchCartErrorType(), accessDeniedErrorType(), concurrentModificationErrorType(), discountCodeNonApplicableErrorType(), duplicateAttributeValueErrorType(), duplicateAttributeValuesErrorType(), duplicateFieldErrorType(), duplicateFieldWithConflictingResourceErrorType(), duplicatePriceScopeErrorType(), duplicateVariantValuesErrorType(), enumValueIsUsedErrorType()]);
}
exports.errorObjectType = errorObjectType;
function errorResponseType() {
    return Joi.object().unknown().keys({
        statusCode: Joi.number().required(),
        message: Joi.string().required(),
        errors: Joi.array().items(errorObjectType()).optional(),
        error: Joi.string().optional(),
        error_description: Joi.string().optional()
    });
}
exports.errorResponseType = errorResponseType;
function extensionBadResponseErrorType() {
    return Joi.object().unknown().keys({
        errorByExtension: errorByExtensionType().required(),
        code: Joi.string().required().only('ExtensionBadResponse'),
        message: Joi.string().required(),
        localizedMessage: Common_types_3.localizedStringType().optional(),
        extensionExtraInfo: Joi.any().optional()
    });
}
exports.extensionBadResponseErrorType = extensionBadResponseErrorType;
function extensionNoResponseErrorType() {
    return Joi.object().unknown().keys({
        errorByExtension: errorByExtensionType().required(),
        code: Joi.string().required().only('ExtensionNoResponse'),
        message: Joi.string().required(),
        localizedMessage: Common_types_3.localizedStringType().optional(),
        extensionExtraInfo: Joi.any().optional()
    });
}
exports.extensionNoResponseErrorType = extensionNoResponseErrorType;
function extensionUpdateActionsFailedErrorType() {
    return Joi.object().unknown().keys({
        errorByExtension: errorByExtensionType().required(),
        code: Joi.string().required().only('ExtensionUpdateActionsFailed'),
        message: Joi.string().required(),
        localizedMessage: Common_types_3.localizedStringType().optional(),
        extensionExtraInfo: Joi.any().optional()
    });
}
exports.extensionUpdateActionsFailedErrorType = extensionUpdateActionsFailedErrorType;
function insufficientScopeErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('insufficient_scope'),
        message: Joi.string().required()
    });
}
exports.insufficientScopeErrorType = insufficientScopeErrorType;
function invalidCredentialsErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidCredentials'),
        message: Joi.string().required()
    });
}
exports.invalidCredentialsErrorType = invalidCredentialsErrorType;
function invalidCurrentPasswordErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidCurrentPassword'),
        message: Joi.string().required()
    });
}
exports.invalidCurrentPasswordErrorType = invalidCurrentPasswordErrorType;
function invalidFieldErrorType() {
    return Joi.object().unknown().keys({
        invalidValue: Joi.any().required(),
        code: Joi.string().required().only('InvalidField'),
        field: Joi.string().required(),
        message: Joi.string().required(),
        allowedValues: Joi.array().items(Joi.any()).optional()
    });
}
exports.invalidFieldErrorType = invalidFieldErrorType;
function invalidInputErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidInput'),
        message: Joi.string().required()
    });
}
exports.invalidInputErrorType = invalidInputErrorType;
function invalidItemShippingDetailsErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidItemShippingDetails'),
        itemId: Joi.string().required(),
        message: Joi.string().required(),
        subject: Joi.string().required()
    });
}
exports.invalidItemShippingDetailsErrorType = invalidItemShippingDetailsErrorType;
function invalidJsonInputErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidJsonInput'),
        message: Joi.string().required()
    });
}
exports.invalidJsonInputErrorType = invalidJsonInputErrorType;
function invalidOperationErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidOperation'),
        message: Joi.string().required()
    });
}
exports.invalidOperationErrorType = invalidOperationErrorType;
function invalidSubjectErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('InvalidSubject'),
        message: Joi.string().required()
    });
}
exports.invalidSubjectErrorType = invalidSubjectErrorType;
function invalidTokenErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('invalid_token'),
        message: Joi.string().required()
    });
}
exports.invalidTokenErrorType = invalidTokenErrorType;
function matchingPriceNotFoundErrorType() {
    return Joi.object().unknown().keys({
        variantId: Joi.number().required(),
        code: Joi.string().required().only('MatchingPriceNotFound'),
        message: Joi.string().required(),
        productId: Joi.string().required(),
        channel: Channel_types_1.channelReferenceType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        country: Joi.string().optional(),
        currency: Joi.string().optional()
    });
}
exports.matchingPriceNotFoundErrorType = matchingPriceNotFoundErrorType;
function missingTaxRateForCountryErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('MissingTaxRateForCountry'),
        message: Joi.string().required(),
        taxCategoryId: Joi.string().required(),
        country: Joi.string().optional(),
        state: Joi.string().optional()
    });
}
exports.missingTaxRateForCountryErrorType = missingTaxRateForCountryErrorType;
function noMatchingProductDiscountFoundErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('NoMatchingProductDiscountFound'),
        message: Joi.string().required()
    });
}
exports.noMatchingProductDiscountFoundErrorType = noMatchingProductDiscountFoundErrorType;
function outOfStockErrorType() {
    return Joi.object().unknown().keys({
        lineItems: Joi.array().items(Joi.string()).required(),
        skus: Joi.array().items(Joi.string()).required(),
        code: Joi.string().required().only('OutOfStock'),
        message: Joi.string().required()
    });
}
exports.outOfStockErrorType = outOfStockErrorType;
function priceChangedErrorType() {
    return Joi.object().unknown().keys({
        lineItems: Joi.array().items(Joi.string()).required(),
        shipping: Joi.boolean().required(),
        code: Joi.string().required().only('PriceChanged'),
        message: Joi.string().required()
    });
}
exports.priceChangedErrorType = priceChangedErrorType;
function referenceExistsErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('ReferenceExists'),
        message: Joi.string().required(),
        referencedBy: Common_types_4.referenceTypeIdType().optional()
    });
}
exports.referenceExistsErrorType = referenceExistsErrorType;
function requiredFieldErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('RequiredField'),
        field: Joi.string().required(),
        message: Joi.string().required()
    });
}
exports.requiredFieldErrorType = requiredFieldErrorType;
function resourceNotFoundErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('ResourceNotFound'),
        message: Joi.string().required()
    });
}
exports.resourceNotFoundErrorType = resourceNotFoundErrorType;
function shippingMethodDoesNotMatchCartErrorType() {
    return Joi.object().unknown().keys({
        code: Joi.string().required().only('ShippingMethodDoesNotMatchCart'),
        message: Joi.string().required()
    });
}
exports.shippingMethodDoesNotMatchCartErrorType = shippingMethodDoesNotMatchCartErrorType;
function variantValuesType() {
    return Joi.object().unknown().keys({
        attributes: Joi.array().items(Product_types_1.attributeType()).required(),
        prices: Joi.array().items(Common_types_2.priceType()).required(),
        sku: Joi.string().optional()
    });
}
exports.variantValuesType = variantValuesType;
