"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const TaxCategory_types_2 = require("./TaxCategory-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Zone_types_1 = require("./Zone-types");
const Zone_types_2 = require("./Zone-types");
function cartClassificationTierType() {
    return Joi.object().unknown().keys({
        price: Common_types_1.moneyType().required(),
        type: shippingRateTierTypeType().required().only('CartClassification'),
        value: Joi.string().required(),
        isMatching: Joi.boolean().optional()
    });
}
exports.cartClassificationTierType = cartClassificationTierType;
function cartScoreTierType() {
    return Joi.object().unknown().keys({
        type: shippingRateTierTypeType().required().only('CartScore'),
        score: Joi.number().required(),
        price: Common_types_1.moneyType().optional(),
        priceFunction: priceFunctionType().optional(),
        isMatching: Joi.boolean().optional()
    });
}
exports.cartScoreTierType = cartScoreTierType;
function cartValueTierType() {
    return Joi.object().unknown().keys({
        price: Common_types_1.moneyType().required(),
        type: shippingRateTierTypeType().required().only('CartValue'),
        minimumCentAmount: Joi.number().required(),
        isMatching: Joi.boolean().optional()
    });
}
exports.cartValueTierType = cartValueTierType;
function priceFunctionType() {
    return Joi.object().unknown().keys({
        currencyCode: Joi.string().required(),
        function: Joi.string().required()
    });
}
exports.priceFunctionType = priceFunctionType;
function shippingMethodType() {
    return Joi.object().unknown().keys({
        zoneRates: Joi.array().items(zoneRateType()).required(),
        taxCategory: TaxCategory_types_1.taxCategoryReferenceType().required(),
        isDefault: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        name: Joi.string().required(),
        description: Joi.string().optional(),
        key: Joi.string().optional(),
        predicate: Joi.string().optional()
    });
}
exports.shippingMethodType = shippingMethodType;
function shippingMethodDraftType() {
    return Joi.object().unknown().keys({
        zoneRates: Joi.array().items(zoneRateDraftType()).required(),
        taxCategory: TaxCategory_types_2.taxCategoryResourceIdentifierType().required(),
        isDefault: Joi.boolean().required(),
        name: Joi.string().required(),
        description: Joi.string().optional(),
        key: Joi.string().optional(),
        predicate: Joi.string().optional()
    });
}
exports.shippingMethodDraftType = shippingMethodDraftType;
function shippingMethodPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(shippingMethodType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.shippingMethodPagedQueryResponseType = shippingMethodPagedQueryResponseType;
function shippingMethodReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().required().only('shipping-method'),
        id: Joi.string().required(),
        obj: shippingMethodType().optional()
    });
}
exports.shippingMethodReferenceType = shippingMethodReferenceType;
function shippingMethodResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().optional().only('shipping-method'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.shippingMethodResourceIdentifierType = shippingMethodResourceIdentifierType;
function shippingMethodUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(shippingMethodUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.shippingMethodUpdateType = shippingMethodUpdateType;
function shippingMethodUpdateActionType() {
    return Joi.alternatives([shippingMethodAddShippingRateActionType(), shippingMethodAddZoneActionType(), shippingMethodChangeIsDefaultActionType(), shippingMethodChangeNameActionType(), shippingMethodChangeTaxCategoryActionType(), shippingMethodRemoveShippingRateActionType(), shippingMethodRemoveZoneActionType(), shippingMethodSetDescriptionActionType(), shippingMethodSetKeyActionType(), shippingMethodSetPredicateActionType()]);
}
exports.shippingMethodUpdateActionType = shippingMethodUpdateActionType;
function shippingRateType() {
    return Joi.object().unknown().keys({
        tiers: Joi.array().items(shippingRatePriceTierType()).required(),
        price: Common_types_3.typedMoneyType().required(),
        freeAbove: Common_types_3.typedMoneyType().optional(),
        isMatching: Joi.boolean().optional()
    });
}
exports.shippingRateType = shippingRateType;
function shippingRateDraftType() {
    return Joi.object().unknown().keys({
        price: Common_types_1.moneyType().required(),
        tiers: Joi.array().items(shippingRatePriceTierType()).optional(),
        freeAbove: Common_types_1.moneyType().optional()
    });
}
exports.shippingRateDraftType = shippingRateDraftType;
function shippingRatePriceTierType() {
    return Joi.alternatives([cartClassificationTierType(), cartScoreTierType(), cartValueTierType()]);
}
exports.shippingRatePriceTierType = shippingRatePriceTierType;
const shippingRateTierTypeTypeValues = [
    'CartValue',
    'CartClassification',
    'CartScore'
];
function shippingRateTierTypeType() {
    return Joi.string().only(shippingRateTierTypeTypeValues);
}
exports.shippingRateTierTypeType = shippingRateTierTypeType;
function zoneRateType() {
    return Joi.object().unknown().keys({
        shippingRates: Joi.array().items(shippingRateType()).required(),
        zone: Zone_types_1.zoneReferenceType().required()
    });
}
exports.zoneRateType = zoneRateType;
function zoneRateDraftType() {
    return Joi.object().unknown().keys({
        shippingRates: Joi.array().items(shippingRateDraftType()).required(),
        zone: Zone_types_2.zoneResourceIdentifierType().required()
    });
}
exports.zoneRateDraftType = zoneRateDraftType;
function shippingMethodAddShippingRateActionType() {
    return Joi.object().unknown().keys({
        shippingRate: shippingRateDraftType().required(),
        zone: Zone_types_2.zoneResourceIdentifierType().required(),
        action: Joi.string().required().only('addShippingRate')
    });
}
exports.shippingMethodAddShippingRateActionType = shippingMethodAddShippingRateActionType;
function shippingMethodAddZoneActionType() {
    return Joi.object().unknown().keys({
        zone: Zone_types_2.zoneResourceIdentifierType().required(),
        action: Joi.string().required().only('addZone')
    });
}
exports.shippingMethodAddZoneActionType = shippingMethodAddZoneActionType;
function shippingMethodChangeIsDefaultActionType() {
    return Joi.object().unknown().keys({
        isDefault: Joi.boolean().required(),
        action: Joi.string().required().only('changeIsDefault')
    });
}
exports.shippingMethodChangeIsDefaultActionType = shippingMethodChangeIsDefaultActionType;
function shippingMethodChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.shippingMethodChangeNameActionType = shippingMethodChangeNameActionType;
function shippingMethodChangeTaxCategoryActionType() {
    return Joi.object().unknown().keys({
        taxCategory: TaxCategory_types_2.taxCategoryResourceIdentifierType().required(),
        action: Joi.string().required().only('changeTaxCategory')
    });
}
exports.shippingMethodChangeTaxCategoryActionType = shippingMethodChangeTaxCategoryActionType;
function shippingMethodRemoveShippingRateActionType() {
    return Joi.object().unknown().keys({
        shippingRate: shippingRateDraftType().required(),
        zone: Zone_types_2.zoneResourceIdentifierType().required(),
        action: Joi.string().required().only('removeShippingRate')
    });
}
exports.shippingMethodRemoveShippingRateActionType = shippingMethodRemoveShippingRateActionType;
function shippingMethodRemoveZoneActionType() {
    return Joi.object().unknown().keys({
        zone: Zone_types_2.zoneResourceIdentifierType().required(),
        action: Joi.string().required().only('removeZone')
    });
}
exports.shippingMethodRemoveZoneActionType = shippingMethodRemoveZoneActionType;
function shippingMethodSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Joi.string().optional()
    });
}
exports.shippingMethodSetDescriptionActionType = shippingMethodSetDescriptionActionType;
function shippingMethodSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.shippingMethodSetKeyActionType = shippingMethodSetKeyActionType;
function shippingMethodSetPredicateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setPredicate'),
        predicate: Joi.string().optional()
    });
}
exports.shippingMethodSetPredicateActionType = shippingMethodSetPredicateActionType;
