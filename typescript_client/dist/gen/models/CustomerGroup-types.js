"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_3 = require("./Common-types");
const Type_types_2 = require("./Type-types");
const Type_types_3 = require("./Type-types");
function customerGroupType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        name: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        key: Joi.string().optional()
    });
}
exports.customerGroupType = customerGroupType;
function customerGroupDraftType() {
    return Joi.object().unknown().keys({
        groupName: Joi.string().required(),
        custom: Type_types_1.customFieldsType().optional(),
        key: Joi.string().optional()
    });
}
exports.customerGroupDraftType = customerGroupDraftType;
function customerGroupPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(customerGroupType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.customerGroupPagedQueryResponseType = customerGroupPagedQueryResponseType;
function customerGroupReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().required().only('customer-group'),
        id: Joi.string().required(),
        obj: customerGroupType().optional()
    });
}
exports.customerGroupReferenceType = customerGroupReferenceType;
function customerGroupResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().optional().only('customer-group'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.customerGroupResourceIdentifierType = customerGroupResourceIdentifierType;
function customerGroupUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(customerGroupUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.customerGroupUpdateType = customerGroupUpdateType;
function customerGroupUpdateActionType() {
    return Joi.alternatives([customerGroupChangeNameActionType(), customerGroupSetCustomFieldActionType(), customerGroupSetCustomTypeActionType(), customerGroupSetKeyActionType()]);
}
exports.customerGroupUpdateActionType = customerGroupUpdateActionType;
function customerGroupChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.customerGroupChangeNameActionType = customerGroupChangeNameActionType;
function customerGroupSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.customerGroupSetCustomFieldActionType = customerGroupSetCustomFieldActionType;
function customerGroupSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_2.fieldContainerType().optional(),
        type: Type_types_3.typeResourceIdentifierType().optional()
    });
}
exports.customerGroupSetCustomTypeActionType = customerGroupSetCustomTypeActionType;
function customerGroupSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.customerGroupSetKeyActionType = customerGroupSetKeyActionType;
