"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
function stateType() {
    return Joi.object().unknown().keys({
        type: stateTypeEnumType().required(),
        builtIn: Joi.boolean().required(),
        initial: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        key: Joi.string().required(),
        roles: Joi.array().items(stateRoleEnumType()).optional(),
        transitions: Joi.array().items(stateReferenceType()).optional(),
        createdBy: Common_types_1.createdByType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        name: Common_types_3.localizedStringType().optional()
    });
}
exports.stateType = stateType;
function stateDraftType() {
    return Joi.object().unknown().keys({
        type: stateTypeEnumType().required(),
        key: Joi.string().required(),
        roles: Joi.array().items(stateRoleEnumType()).optional(),
        transitions: Joi.array().items(stateResourceIdentifierType()).optional(),
        description: Common_types_3.localizedStringType().optional(),
        name: Common_types_3.localizedStringType().optional(),
        initial: Joi.boolean().optional()
    });
}
exports.stateDraftType = stateDraftType;
function statePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(stateType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.statePagedQueryResponseType = statePagedQueryResponseType;
function stateReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().required().only('state'),
        id: Joi.string().required(),
        obj: stateType().optional()
    });
}
exports.stateReferenceType = stateReferenceType;
function stateResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().optional().only('state'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.stateResourceIdentifierType = stateResourceIdentifierType;
const stateRoleEnumTypeValues = [
    'ReviewIncludedInStatistics'
];
function stateRoleEnumType() {
    return Joi.string().only(stateRoleEnumTypeValues);
}
exports.stateRoleEnumType = stateRoleEnumType;
const stateTypeEnumTypeValues = [
    'OrderState',
    'LineItemState',
    'ProductState',
    'ReviewState',
    'PaymentState'
];
function stateTypeEnumType() {
    return Joi.string().only(stateTypeEnumTypeValues);
}
exports.stateTypeEnumType = stateTypeEnumType;
function stateUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(stateUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.stateUpdateType = stateUpdateType;
function stateUpdateActionType() {
    return Joi.alternatives([stateAddRolesActionType(), stateChangeInitialActionType(), stateChangeKeyActionType(), stateChangeTypeActionType(), stateRemoveRolesActionType(), stateSetDescriptionActionType(), stateSetNameActionType(), stateSetRolesActionType(), stateSetTransitionsActionType()]);
}
exports.stateUpdateActionType = stateUpdateActionType;
function stateAddRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(stateRoleEnumType()).required(),
        action: Joi.string().required().only('addRoles')
    });
}
exports.stateAddRolesActionType = stateAddRolesActionType;
function stateChangeInitialActionType() {
    return Joi.object().unknown().keys({
        initial: Joi.boolean().required(),
        action: Joi.string().required().only('changeInitial')
    });
}
exports.stateChangeInitialActionType = stateChangeInitialActionType;
function stateChangeKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeKey'),
        key: Joi.string().required()
    });
}
exports.stateChangeKeyActionType = stateChangeKeyActionType;
function stateChangeTypeActionType() {
    return Joi.object().unknown().keys({
        type: stateTypeEnumType().required(),
        action: Joi.string().required().only('changeType')
    });
}
exports.stateChangeTypeActionType = stateChangeTypeActionType;
function stateRemoveRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(stateRoleEnumType()).required(),
        action: Joi.string().required().only('removeRoles')
    });
}
exports.stateRemoveRolesActionType = stateRemoveRolesActionType;
function stateSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        description: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('setDescription')
    });
}
exports.stateSetDescriptionActionType = stateSetDescriptionActionType;
function stateSetNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('setName')
    });
}
exports.stateSetNameActionType = stateSetNameActionType;
function stateSetRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(stateRoleEnumType()).required(),
        action: Joi.string().required().only('setRoles')
    });
}
exports.stateSetRolesActionType = stateSetRolesActionType;
function stateSetTransitionsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTransitions'),
        transitions: Joi.array().items(stateResourceIdentifierType()).optional()
    });
}
exports.stateSetTransitionsActionType = stateSetTransitionsActionType;
