"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
function locationType() {
    return Joi.object().unknown().keys({
        country: Joi.string().required(),
        state: Joi.string().optional()
    });
}
exports.locationType = locationType;
function zoneType() {
    return Joi.object().unknown().keys({
        locations: Joi.array().items(locationType()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        name: Joi.string().required(),
        description: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.zoneType = zoneType;
function zoneDraftType() {
    return Joi.object().unknown().keys({
        locations: Joi.array().items(locationType()).required(),
        name: Joi.string().required(),
        description: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.zoneDraftType = zoneDraftType;
function zonePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(zoneType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.zonePagedQueryResponseType = zonePagedQueryResponseType;
function zoneReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_1.referenceTypeIdType().required().only('zone'),
        id: Joi.string().required(),
        obj: zoneType().optional()
    });
}
exports.zoneReferenceType = zoneReferenceType;
function zoneResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_1.referenceTypeIdType().optional().only('zone'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.zoneResourceIdentifierType = zoneResourceIdentifierType;
function zoneUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(zoneUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.zoneUpdateType = zoneUpdateType;
function zoneUpdateActionType() {
    return Joi.alternatives([zoneAddLocationActionType(), zoneChangeNameActionType(), zoneRemoveLocationActionType(), zoneSetDescriptionActionType(), zoneSetKeyActionType()]);
}
exports.zoneUpdateActionType = zoneUpdateActionType;
function zoneAddLocationActionType() {
    return Joi.object().unknown().keys({
        location: locationType().required(),
        action: Joi.string().required().only('addLocation')
    });
}
exports.zoneAddLocationActionType = zoneAddLocationActionType;
function zoneChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.zoneChangeNameActionType = zoneChangeNameActionType;
function zoneRemoveLocationActionType() {
    return Joi.object().unknown().keys({
        location: locationType().required(),
        action: Joi.string().required().only('removeLocation')
    });
}
exports.zoneRemoveLocationActionType = zoneRemoveLocationActionType;
function zoneSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Joi.string().optional()
    });
}
exports.zoneSetDescriptionActionType = zoneSetDescriptionActionType;
function zoneSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.zoneSetKeyActionType = zoneSetKeyActionType;
