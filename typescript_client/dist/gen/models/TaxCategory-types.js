"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
function subRateType() {
    return Joi.object().unknown().keys({
        amount: Joi.number().required(),
        name: Joi.string().required()
    });
}
exports.subRateType = subRateType;
function taxCategoryType() {
    return Joi.object().unknown().keys({
        rates: Joi.array().items(taxRateType()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        name: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.taxCategoryType = taxCategoryType;
function taxCategoryDraftType() {
    return Joi.object().unknown().keys({
        rates: Joi.array().items(taxRateDraftType()).required(),
        name: Joi.string().required(),
        description: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.taxCategoryDraftType = taxCategoryDraftType;
function taxCategoryPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(taxCategoryType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.taxCategoryPagedQueryResponseType = taxCategoryPagedQueryResponseType;
function taxCategoryReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().required().only('tax-category'),
        id: Joi.string().required(),
        obj: taxCategoryType().optional()
    });
}
exports.taxCategoryReferenceType = taxCategoryReferenceType;
function taxCategoryResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().optional().only('tax-category'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.taxCategoryResourceIdentifierType = taxCategoryResourceIdentifierType;
function taxCategoryUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(taxCategoryUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.taxCategoryUpdateType = taxCategoryUpdateType;
function taxCategoryUpdateActionType() {
    return Joi.alternatives([taxCategoryAddTaxRateActionType(), taxCategoryChangeNameActionType(), taxCategoryRemoveTaxRateActionType(), taxCategoryReplaceTaxRateActionType(), taxCategorySetDescriptionActionType(), taxCategorySetKeyActionType()]);
}
exports.taxCategoryUpdateActionType = taxCategoryUpdateActionType;
function taxRateType() {
    return Joi.object().unknown().keys({
        country: Joi.string().required(),
        includedInPrice: Joi.boolean().required(),
        amount: Joi.number().required(),
        name: Joi.string().required(),
        subRates: Joi.array().items(subRateType()).optional(),
        id: Joi.string().optional(),
        state: Joi.string().optional()
    });
}
exports.taxRateType = taxRateType;
function taxRateDraftType() {
    return Joi.object().unknown().keys({
        country: Joi.string().required(),
        includedInPrice: Joi.boolean().required(),
        name: Joi.string().required(),
        subRates: Joi.array().items(subRateType()).optional(),
        amount: Joi.number().optional(),
        state: Joi.string().optional()
    });
}
exports.taxRateDraftType = taxRateDraftType;
function taxCategoryAddTaxRateActionType() {
    return Joi.object().unknown().keys({
        taxRate: taxRateDraftType().required(),
        action: Joi.string().required().only('addTaxRate')
    });
}
exports.taxCategoryAddTaxRateActionType = taxCategoryAddTaxRateActionType;
function taxCategoryChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.taxCategoryChangeNameActionType = taxCategoryChangeNameActionType;
function taxCategoryRemoveTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeTaxRate'),
        taxRateId: Joi.string().required()
    });
}
exports.taxCategoryRemoveTaxRateActionType = taxCategoryRemoveTaxRateActionType;
function taxCategoryReplaceTaxRateActionType() {
    return Joi.object().unknown().keys({
        taxRate: taxRateDraftType().required(),
        action: Joi.string().required().only('replaceTaxRate'),
        taxRateId: Joi.string().required()
    });
}
exports.taxCategoryReplaceTaxRateActionType = taxCategoryReplaceTaxRateActionType;
function taxCategorySetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Joi.string().optional()
    });
}
exports.taxCategorySetDescriptionActionType = taxCategorySetDescriptionActionType;
function taxCategorySetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.taxCategorySetKeyActionType = taxCategorySetKeyActionType;
