"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Channel_types_1 = require("./Channel-types");
const Type_types_2 = require("./Type-types");
const Common_types_3 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function inventoryEntryType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        availableQuantity: Joi.number().required(),
        quantityOnStock: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        sku: Joi.string().required(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        expectedDelivery: Joi.date().iso().optional(),
        restockableInDays: Joi.number().optional()
    });
}
exports.inventoryEntryType = inventoryEntryType;
function inventoryEntryDraftType() {
    return Joi.object().unknown().keys({
        quantityOnStock: Joi.number().required(),
        sku: Joi.string().required(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        expectedDelivery: Joi.date().iso().optional(),
        restockableInDays: Joi.number().optional()
    });
}
exports.inventoryEntryDraftType = inventoryEntryDraftType;
function inventoryEntryReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().required().only('inventory-entry'),
        id: Joi.string().required(),
        obj: inventoryEntryType().optional()
    });
}
exports.inventoryEntryReferenceType = inventoryEntryReferenceType;
function inventoryEntryResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().optional().only('inventory-entry'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.inventoryEntryResourceIdentifierType = inventoryEntryResourceIdentifierType;
function inventoryPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(inventoryEntryType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.inventoryPagedQueryResponseType = inventoryPagedQueryResponseType;
function inventoryUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(inventoryUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.inventoryUpdateType = inventoryUpdateType;
function inventoryUpdateActionType() {
    return Joi.alternatives([inventoryAddQuantityActionType(), inventoryChangeQuantityActionType(), inventoryRemoveQuantityActionType(), inventorySetCustomFieldActionType(), inventorySetCustomTypeActionType(), inventorySetExpectedDeliveryActionType(), inventorySetRestockableInDaysActionType(), inventorySetSupplyChannelActionType()]);
}
exports.inventoryUpdateActionType = inventoryUpdateActionType;
function inventoryAddQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('addQuantity')
    });
}
exports.inventoryAddQuantityActionType = inventoryAddQuantityActionType;
function inventoryChangeQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeQuantity')
    });
}
exports.inventoryChangeQuantityActionType = inventoryChangeQuantityActionType;
function inventoryRemoveQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('removeQuantity')
    });
}
exports.inventoryRemoveQuantityActionType = inventoryRemoveQuantityActionType;
function inventorySetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.inventorySetCustomFieldActionType = inventorySetCustomFieldActionType;
function inventorySetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.inventorySetCustomTypeActionType = inventorySetCustomTypeActionType;
function inventorySetExpectedDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setExpectedDelivery'),
        expectedDelivery: Joi.date().iso().optional()
    });
}
exports.inventorySetExpectedDeliveryActionType = inventorySetExpectedDeliveryActionType;
function inventorySetRestockableInDaysActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setRestockableInDays'),
        restockableInDays: Joi.number().optional()
    });
}
exports.inventorySetRestockableInDaysActionType = inventorySetRestockableInDaysActionType;
function inventorySetSupplyChannelActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setSupplyChannel'),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional()
    });
}
exports.inventorySetSupplyChannelActionType = inventorySetSupplyChannelActionType;
