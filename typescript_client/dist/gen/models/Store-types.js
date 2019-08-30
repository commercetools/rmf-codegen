"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
function storeType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        key: Joi.string().required(),
        name: Common_types_1.localizedStringType().optional()
    });
}
exports.storeType = storeType;
function storeDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_1.localizedStringType().required(),
        key: Joi.string().required()
    });
}
exports.storeDraftType = storeDraftType;
function storeKeyReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().required().only('store'),
        key: Joi.string().required()
    });
}
exports.storeKeyReferenceType = storeKeyReferenceType;
function storePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(storeType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.storePagedQueryResponseType = storePagedQueryResponseType;
function storeReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().required().only('store'),
        id: Joi.string().required(),
        obj: storeType().optional()
    });
}
exports.storeReferenceType = storeReferenceType;
function storeResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().optional().only('store'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.storeResourceIdentifierType = storeResourceIdentifierType;
function storeUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(storeUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.storeUpdateType = storeUpdateType;
function storeUpdateActionType() {
    return Joi.alternatives([storeSetNameActionType()]);
}
exports.storeUpdateActionType = storeUpdateActionType;
function storeSetNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setName'),
        name: Common_types_1.localizedStringType().optional()
    });
}
exports.storeSetNameActionType = storeSetNameActionType;
