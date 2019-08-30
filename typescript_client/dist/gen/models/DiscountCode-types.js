"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const CartDiscount_types_1 = require("./CartDiscount-types");
const Common_types_3 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_4 = require("./Common-types");
const CartDiscount_types_2 = require("./CartDiscount-types");
const Type_types_2 = require("./Type-types");
const Common_types_5 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function discountCodeType() {
    return Joi.object().unknown().keys({
        cartDiscounts: Joi.array().items(CartDiscount_types_1.cartDiscountReferenceType()).required(),
        groups: Joi.array().items(Joi.string()).required(),
        references: Joi.array().items(Common_types_3.referenceType()).required(),
        isActive: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        code: Joi.string().required(),
        id: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        name: Common_types_4.localizedStringType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        maxApplications: Joi.number().optional(),
        maxApplicationsPerCustomer: Joi.number().optional(),
        cartPredicate: Joi.string().optional()
    });
}
exports.discountCodeType = discountCodeType;
function discountCodeDraftType() {
    return Joi.object().unknown().keys({
        cartDiscounts: Joi.array().items(CartDiscount_types_2.cartDiscountResourceIdentifierType()).required(),
        code: Joi.string().required(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        name: Common_types_4.localizedStringType().optional(),
        groups: Joi.array().items(Joi.string()).optional(),
        isActive: Joi.boolean().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        maxApplications: Joi.number().optional(),
        maxApplicationsPerCustomer: Joi.number().optional(),
        cartPredicate: Joi.string().optional()
    });
}
exports.discountCodeDraftType = discountCodeDraftType;
function discountCodePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(discountCodeType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.discountCodePagedQueryResponseType = discountCodePagedQueryResponseType;
function discountCodeReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().required().only('discount-code'),
        id: Joi.string().required(),
        obj: discountCodeType().optional()
    });
}
exports.discountCodeReferenceType = discountCodeReferenceType;
function discountCodeResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().optional().only('discount-code'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.discountCodeResourceIdentifierType = discountCodeResourceIdentifierType;
function discountCodeUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(discountCodeUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.discountCodeUpdateType = discountCodeUpdateType;
function discountCodeUpdateActionType() {
    return Joi.alternatives([discountCodeChangeCartDiscountsActionType(), discountCodeChangeGroupsActionType(), discountCodeChangeIsActiveActionType(), discountCodeSetCartPredicateActionType(), discountCodeSetCustomFieldActionType(), discountCodeSetCustomTypeActionType(), discountCodeSetDescriptionActionType(), discountCodeSetMaxApplicationsActionType(), discountCodeSetMaxApplicationsPerCustomerActionType(), discountCodeSetNameActionType(), discountCodeSetValidFromActionType(), discountCodeSetValidFromAndUntilActionType(), discountCodeSetValidUntilActionType()]);
}
exports.discountCodeUpdateActionType = discountCodeUpdateActionType;
function discountCodeChangeCartDiscountsActionType() {
    return Joi.object().unknown().keys({
        cartDiscounts: Joi.array().items(CartDiscount_types_2.cartDiscountResourceIdentifierType()).required(),
        action: Joi.string().required().only('changeCartDiscounts')
    });
}
exports.discountCodeChangeCartDiscountsActionType = discountCodeChangeCartDiscountsActionType;
function discountCodeChangeGroupsActionType() {
    return Joi.object().unknown().keys({
        groups: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeGroups')
    });
}
exports.discountCodeChangeGroupsActionType = discountCodeChangeGroupsActionType;
function discountCodeChangeIsActiveActionType() {
    return Joi.object().unknown().keys({
        isActive: Joi.boolean().required(),
        action: Joi.string().required().only('changeIsActive')
    });
}
exports.discountCodeChangeIsActiveActionType = discountCodeChangeIsActiveActionType;
function discountCodeSetCartPredicateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCartPredicate'),
        cartPredicate: Joi.string().optional()
    });
}
exports.discountCodeSetCartPredicateActionType = discountCodeSetCartPredicateActionType;
function discountCodeSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.discountCodeSetCustomFieldActionType = discountCodeSetCustomFieldActionType;
function discountCodeSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.discountCodeSetCustomTypeActionType = discountCodeSetCustomTypeActionType;
function discountCodeSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_4.localizedStringType().optional()
    });
}
exports.discountCodeSetDescriptionActionType = discountCodeSetDescriptionActionType;
function discountCodeSetMaxApplicationsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMaxApplications'),
        maxApplications: Joi.number().optional()
    });
}
exports.discountCodeSetMaxApplicationsActionType = discountCodeSetMaxApplicationsActionType;
function discountCodeSetMaxApplicationsPerCustomerActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMaxApplicationsPerCustomer'),
        maxApplicationsPerCustomer: Joi.number().optional()
    });
}
exports.discountCodeSetMaxApplicationsPerCustomerActionType = discountCodeSetMaxApplicationsPerCustomerActionType;
function discountCodeSetNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setName'),
        name: Common_types_4.localizedStringType().optional()
    });
}
exports.discountCodeSetNameActionType = discountCodeSetNameActionType;
function discountCodeSetValidFromActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFrom'),
        validFrom: Joi.date().iso().optional()
    });
}
exports.discountCodeSetValidFromActionType = discountCodeSetValidFromActionType;
function discountCodeSetValidFromAndUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFromAndUntil'),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional()
    });
}
exports.discountCodeSetValidFromAndUntilActionType = discountCodeSetValidFromAndUntilActionType;
function discountCodeSetValidUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidUntil'),
        validUntil: Joi.date().iso().optional()
    });
}
exports.discountCodeSetValidUntilActionType = discountCodeSetValidUntilActionType;
