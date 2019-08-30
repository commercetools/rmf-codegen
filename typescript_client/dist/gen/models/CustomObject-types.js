"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
function customObjectType() {
    return Joi.object().unknown().keys({
        value: Joi.any().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        container: Joi.string().required(),
        id: Joi.string().required(),
        key: Joi.string().required()
    });
}
exports.customObjectType = customObjectType;
function customObjectDraftType() {
    return Joi.object().unknown().keys({
        value: Joi.any().required(),
        container: Joi.string().required(),
        key: Joi.string().required(),
        version: Joi.number().optional()
    });
}
exports.customObjectDraftType = customObjectDraftType;
function customObjectPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(customObjectType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.customObjectPagedQueryResponseType = customObjectPagedQueryResponseType;
function customObjectReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_1.referenceTypeIdType().required().only('key-value-document'),
        id: Joi.string().required(),
        obj: customObjectType().optional()
    });
}
exports.customObjectReferenceType = customObjectReferenceType;
