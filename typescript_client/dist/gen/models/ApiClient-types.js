"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
function apiClientType() {
    return Joi.object().unknown().keys({
        id: Joi.string().required(),
        name: Joi.string().required(),
        scope: Joi.string().required(),
        lastUsedAt: Joi.date().iso().optional(),
        createdAt: Joi.date().iso().optional(),
        deleteAt: Joi.date().iso().optional(),
        secret: Joi.string().optional()
    });
}
exports.apiClientType = apiClientType;
function apiClientDraftType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required(),
        scope: Joi.string().required(),
        deleteDaysAfterCreation: Joi.number().optional()
    });
}
exports.apiClientDraftType = apiClientDraftType;
function apiClientPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(apiClientType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.apiClientPagedQueryResponseType = apiClientPagedQueryResponseType;
