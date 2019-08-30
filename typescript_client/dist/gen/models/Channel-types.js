"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_4 = require("./Common-types");
const Review_types_1 = require("./Review-types");
const Type_types_2 = require("./Type-types");
const Common_types_5 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function channelType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(channelRoleEnumType()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        key: Joi.string().required(),
        geoLocation: Joi.any().optional(),
        address: Common_types_3.addressType().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        name: Common_types_4.localizedStringType().optional(),
        reviewRatingStatistics: Review_types_1.reviewRatingStatisticsType().optional()
    });
}
exports.channelType = channelType;
function channelDraftType() {
    return Joi.object().unknown().keys({
        key: Joi.string().required(),
        geoLocation: Joi.any().optional(),
        roles: Joi.array().items(channelRoleEnumType()).optional(),
        address: Common_types_3.addressType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        name: Common_types_4.localizedStringType().optional()
    });
}
exports.channelDraftType = channelDraftType;
function channelPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(channelType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.channelPagedQueryResponseType = channelPagedQueryResponseType;
function channelReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().required().only('channel'),
        id: Joi.string().required(),
        obj: channelType().optional()
    });
}
exports.channelReferenceType = channelReferenceType;
function channelResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().optional().only('channel'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.channelResourceIdentifierType = channelResourceIdentifierType;
const channelRoleEnumTypeValues = [
    'InventorySupply',
    'ProductDistribution',
    'OrderExport',
    'OrderImport',
    'Primary'
];
function channelRoleEnumType() {
    return Joi.string().only(channelRoleEnumTypeValues);
}
exports.channelRoleEnumType = channelRoleEnumType;
function channelUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(channelUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.channelUpdateType = channelUpdateType;
function channelUpdateActionType() {
    return Joi.alternatives([channelAddRolesActionType(), channelChangeDescriptionActionType(), channelChangeKeyActionType(), channelChangeNameActionType(), channelRemoveRolesActionType(), channelSetAddressActionType(), channelSetCustomFieldActionType(), channelSetCustomTypeActionType(), channelSetGeoLocationActionType(), channelSetRolesActionType()]);
}
exports.channelUpdateActionType = channelUpdateActionType;
function channelAddRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(channelRoleEnumType()).required(),
        action: Joi.string().required().only('addRoles')
    });
}
exports.channelAddRolesActionType = channelAddRolesActionType;
function channelChangeDescriptionActionType() {
    return Joi.object().unknown().keys({
        description: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeDescription')
    });
}
exports.channelChangeDescriptionActionType = channelChangeDescriptionActionType;
function channelChangeKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeKey'),
        key: Joi.string().required()
    });
}
exports.channelChangeKeyActionType = channelChangeKeyActionType;
function channelChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.channelChangeNameActionType = channelChangeNameActionType;
function channelRemoveRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(channelRoleEnumType()).required(),
        action: Joi.string().required().only('removeRoles')
    });
}
exports.channelRemoveRolesActionType = channelRemoveRolesActionType;
function channelSetAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAddress'),
        address: Common_types_3.addressType().optional()
    });
}
exports.channelSetAddressActionType = channelSetAddressActionType;
function channelSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.channelSetCustomFieldActionType = channelSetCustomFieldActionType;
function channelSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.channelSetCustomTypeActionType = channelSetCustomTypeActionType;
function channelSetGeoLocationActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setGeoLocation'),
        geoLocation: Joi.any().optional()
    });
}
exports.channelSetGeoLocationActionType = channelSetGeoLocationActionType;
function channelSetRolesActionType() {
    return Joi.object().unknown().keys({
        roles: Joi.array().items(channelRoleEnumType()).required(),
        action: Joi.string().required().only('setRoles')
    });
}
exports.channelSetRolesActionType = channelSetRolesActionType;
