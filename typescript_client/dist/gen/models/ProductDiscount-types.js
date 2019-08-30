"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const Common_types_7 = require("./Common-types");
function productDiscountType() {
    return Joi.object().unknown().keys({
        references: Joi.array().items(Common_types_3.referenceType()).required(),
        name: Common_types_4.localizedStringType().required(),
        value: productDiscountValueType().required(),
        isActive: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        predicate: Joi.string().required(),
        sortOrder: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        key: Joi.string().optional()
    });
}
exports.productDiscountType = productDiscountType;
function productDiscountDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        value: productDiscountValueType().required(),
        isActive: Joi.boolean().required(),
        predicate: Joi.string().required(),
        sortOrder: Joi.string().required(),
        description: Common_types_4.localizedStringType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        key: Joi.string().optional()
    });
}
exports.productDiscountDraftType = productDiscountDraftType;
function productDiscountMatchQueryType() {
    return Joi.object().unknown().keys({
        price: Common_types_5.priceType().required(),
        staged: Joi.boolean().required(),
        variantId: Joi.number().required(),
        productId: Joi.string().required()
    });
}
exports.productDiscountMatchQueryType = productDiscountMatchQueryType;
function productDiscountPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(productDiscountType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.productDiscountPagedQueryResponseType = productDiscountPagedQueryResponseType;
function productDiscountReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().required().only('product-discount'),
        id: Joi.string().required(),
        obj: productDiscountType().optional()
    });
}
exports.productDiscountReferenceType = productDiscountReferenceType;
function productDiscountResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().optional().only('product-discount'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.productDiscountResourceIdentifierType = productDiscountResourceIdentifierType;
function productDiscountUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(productDiscountUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.productDiscountUpdateType = productDiscountUpdateType;
function productDiscountUpdateActionType() {
    return Joi.alternatives([productDiscountChangeIsActiveActionType(), productDiscountChangeNameActionType(), productDiscountChangePredicateActionType(), productDiscountChangeSortOrderActionType(), productDiscountChangeValueActionType(), productDiscountSetDescriptionActionType(), productDiscountSetKeyActionType(), productDiscountSetValidFromActionType(), productDiscountSetValidFromAndUntilActionType(), productDiscountSetValidUntilActionType()]);
}
exports.productDiscountUpdateActionType = productDiscountUpdateActionType;
function productDiscountValueType() {
    return Joi.alternatives([productDiscountValueAbsoluteType(), productDiscountValueExternalType(), productDiscountValueRelativeType()]);
}
exports.productDiscountValueType = productDiscountValueType;
function productDiscountValueAbsoluteType() {
    return Joi.object().unknown().keys({
        money: Joi.array().items(Common_types_7.moneyType()).required(),
        type: Joi.string().required().only('absolute')
    });
}
exports.productDiscountValueAbsoluteType = productDiscountValueAbsoluteType;
function productDiscountValueExternalType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('external')
    });
}
exports.productDiscountValueExternalType = productDiscountValueExternalType;
function productDiscountValueRelativeType() {
    return Joi.object().unknown().keys({
        permyriad: Joi.number().required(),
        type: Joi.string().required().only('relative')
    });
}
exports.productDiscountValueRelativeType = productDiscountValueRelativeType;
function productDiscountChangeIsActiveActionType() {
    return Joi.object().unknown().keys({
        isActive: Joi.boolean().required(),
        action: Joi.string().required().only('changeIsActive')
    });
}
exports.productDiscountChangeIsActiveActionType = productDiscountChangeIsActiveActionType;
function productDiscountChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.productDiscountChangeNameActionType = productDiscountChangeNameActionType;
function productDiscountChangePredicateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changePredicate'),
        predicate: Joi.string().required()
    });
}
exports.productDiscountChangePredicateActionType = productDiscountChangePredicateActionType;
function productDiscountChangeSortOrderActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeSortOrder'),
        sortOrder: Joi.string().required()
    });
}
exports.productDiscountChangeSortOrderActionType = productDiscountChangeSortOrderActionType;
function productDiscountChangeValueActionType() {
    return Joi.object().unknown().keys({
        value: productDiscountValueType().required(),
        action: Joi.string().required().only('changeValue')
    });
}
exports.productDiscountChangeValueActionType = productDiscountChangeValueActionType;
function productDiscountSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_4.localizedStringType().optional()
    });
}
exports.productDiscountSetDescriptionActionType = productDiscountSetDescriptionActionType;
function productDiscountSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.productDiscountSetKeyActionType = productDiscountSetKeyActionType;
function productDiscountSetValidFromActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFrom'),
        validFrom: Joi.date().iso().optional()
    });
}
exports.productDiscountSetValidFromActionType = productDiscountSetValidFromActionType;
function productDiscountSetValidFromAndUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFromAndUntil'),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional()
    });
}
exports.productDiscountSetValidFromAndUntilActionType = productDiscountSetValidFromAndUntilActionType;
function productDiscountSetValidUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidUntil'),
        validUntil: Joi.date().iso().optional()
    });
}
exports.productDiscountSetValidUntilActionType = productDiscountSetValidUntilActionType;
