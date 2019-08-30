"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
function customFieldBooleanTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('Boolean')
    });
}
exports.customFieldBooleanTypeType = customFieldBooleanTypeType;
function customFieldDateTimeTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('DateTime')
    });
}
exports.customFieldDateTimeTypeType = customFieldDateTimeTypeType;
function customFieldDateTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('Date')
    });
}
exports.customFieldDateTypeType = customFieldDateTypeType;
function customFieldEnumTypeType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(customFieldEnumValueType()).required(),
        name: Joi.string().required().only('Enum')
    });
}
exports.customFieldEnumTypeType = customFieldEnumTypeType;
function customFieldEnumValueType() {
    return Joi.object().unknown().keys({
        key: Joi.string().required(),
        label: Joi.string().required()
    });
}
exports.customFieldEnumValueType = customFieldEnumValueType;
function customFieldLocalizedEnumTypeType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(customFieldLocalizedEnumValueType()).required(),
        name: Joi.string().required().only('LocalizedEnum')
    });
}
exports.customFieldLocalizedEnumTypeType = customFieldLocalizedEnumTypeType;
function customFieldLocalizedEnumValueType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        key: Joi.string().required()
    });
}
exports.customFieldLocalizedEnumValueType = customFieldLocalizedEnumValueType;
function customFieldLocalizedStringTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('LocalizedString')
    });
}
exports.customFieldLocalizedStringTypeType = customFieldLocalizedStringTypeType;
function customFieldMoneyTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('Money')
    });
}
exports.customFieldMoneyTypeType = customFieldMoneyTypeType;
function customFieldNumberTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('Number')
    });
}
exports.customFieldNumberTypeType = customFieldNumberTypeType;
function customFieldReferenceTypeType() {
    return Joi.object().unknown().keys({
        referenceTypeId: Common_types_2.referenceTypeIdType().required(),
        name: Joi.string().required().only('Reference')
    });
}
exports.customFieldReferenceTypeType = customFieldReferenceTypeType;
function customFieldSetTypeType() {
    return Joi.object().unknown().keys({
        elementType: fieldTypeType().required(),
        name: Joi.string().required().only('Set')
    });
}
exports.customFieldSetTypeType = customFieldSetTypeType;
function customFieldStringTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('String')
    });
}
exports.customFieldStringTypeType = customFieldStringTypeType;
function customFieldTimeTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('Time')
    });
}
exports.customFieldTimeTypeType = customFieldTimeTypeType;
function customFieldsType() {
    return Joi.object().unknown().keys({
        fields: fieldContainerType().required(),
        type: typeReferenceType().required()
    });
}
exports.customFieldsType = customFieldsType;
function customFieldsDraftType() {
    return Joi.object().unknown().keys({
        type: typeResourceIdentifierType().required(),
        fields: fieldContainerType().optional()
    });
}
exports.customFieldsDraftType = customFieldsDraftType;
function fieldContainerType() {
    return Joi.object().pattern(new RegExp('//'), Joi.any());
}
exports.fieldContainerType = fieldContainerType;
function fieldDefinitionType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        required: Joi.boolean().required(),
        type: Joi.any().required(),
        name: Joi.string().required(),
        inputHint: typeTextInputHintType().optional()
    });
}
exports.fieldDefinitionType = fieldDefinitionType;
function fieldTypeType() {
    return Joi.alternatives([customFieldBooleanTypeType(), customFieldDateTimeTypeType(), customFieldDateTypeType(), customFieldEnumTypeType(), customFieldLocalizedEnumTypeType(), customFieldLocalizedStringTypeType(), customFieldMoneyTypeType(), customFieldNumberTypeType(), customFieldReferenceTypeType(), customFieldSetTypeType(), customFieldStringTypeType(), customFieldTimeTypeType()]);
}
exports.fieldTypeType = fieldTypeType;
const resourceTypeIdTypeValues = [
    'asset',
    'category',
    'channel',
    'customer',
    'order',
    'order-edit',
    'inventory-entry',
    'line-item',
    'custom-line-item',
    'product-price',
    'payment',
    'payment-interface-interaction',
    'review',
    'shopping-list',
    'shopping-list-text-line-item',
    'discount-code',
    'cart-discount',
    'customer-group'
];
function resourceTypeIdType() {
    return Joi.string().only(resourceTypeIdTypeValues);
}
exports.resourceTypeIdType = resourceTypeIdType;
function typeType() {
    return Joi.object().unknown().keys({
        fieldDefinitions: Joi.array().items(fieldDefinitionType()).required(),
        resourceTypeIds: Joi.array().items(resourceTypeIdType()).required(),
        name: Common_types_1.localizedStringType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        key: Joi.string().required(),
        createdBy: Common_types_3.createdByType().optional(),
        lastModifiedBy: Common_types_4.lastModifiedByType().optional(),
        description: Common_types_1.localizedStringType().optional()
    });
}
exports.typeType = typeType;
function typeDraftType() {
    return Joi.object().unknown().keys({
        resourceTypeIds: Joi.array().items(resourceTypeIdType()).required(),
        name: Common_types_1.localizedStringType().required(),
        key: Joi.string().required(),
        fieldDefinitions: Joi.array().items(fieldDefinitionType()).optional(),
        description: Common_types_1.localizedStringType().optional()
    });
}
exports.typeDraftType = typeDraftType;
function typePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(typeType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.typePagedQueryResponseType = typePagedQueryResponseType;
function typeReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().required().only('type'),
        id: Joi.string().required(),
        obj: typeType().optional()
    });
}
exports.typeReferenceType = typeReferenceType;
function typeResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().optional().only('type'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.typeResourceIdentifierType = typeResourceIdentifierType;
const typeTextInputHintTypeValues = [
    'SingleLine',
    'MultiLine'
];
function typeTextInputHintType() {
    return Joi.string().only(typeTextInputHintTypeValues);
}
exports.typeTextInputHintType = typeTextInputHintType;
function typeUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(typeUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.typeUpdateType = typeUpdateType;
function typeUpdateActionType() {
    return Joi.alternatives([typeAddEnumValueActionType(), typeAddFieldDefinitionActionType(), typeAddLocalizedEnumValueActionType(), typeChangeEnumValueLabelActionType(), typeChangeEnumValueOrderActionType(), typeChangeFieldDefinitionLabelActionType(), typeChangeFieldDefinitionOrderActionType(), typeChangeInputHintActionType(), typeChangeKeyActionType(), typeChangeLabelActionType(), typeChangeLocalizedEnumValueLabelActionType(), typeChangeLocalizedEnumValueOrderActionType(), typeChangeNameActionType(), typeRemoveFieldDefinitionActionType(), typeSetDescriptionActionType()]);
}
exports.typeUpdateActionType = typeUpdateActionType;
function typeAddEnumValueActionType() {
    return Joi.object().unknown().keys({
        value: customFieldEnumValueType().required(),
        action: Joi.string().required().only('addEnumValue'),
        fieldName: Joi.string().required()
    });
}
exports.typeAddEnumValueActionType = typeAddEnumValueActionType;
function typeAddFieldDefinitionActionType() {
    return Joi.object().unknown().keys({
        fieldDefinition: fieldDefinitionType().required(),
        action: Joi.string().required().only('addFieldDefinition')
    });
}
exports.typeAddFieldDefinitionActionType = typeAddFieldDefinitionActionType;
function typeAddLocalizedEnumValueActionType() {
    return Joi.object().unknown().keys({
        value: customFieldLocalizedEnumValueType().required(),
        action: Joi.string().required().only('addLocalizedEnumValue'),
        fieldName: Joi.string().required()
    });
}
exports.typeAddLocalizedEnumValueActionType = typeAddLocalizedEnumValueActionType;
function typeChangeEnumValueLabelActionType() {
    return Joi.object().unknown().keys({
        value: customFieldEnumValueType().required(),
        action: Joi.string().required().only('changeEnumValueLabel'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeEnumValueLabelActionType = typeChangeEnumValueLabelActionType;
function typeChangeEnumValueOrderActionType() {
    return Joi.object().unknown().keys({
        keys: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeEnumValueOrder'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeEnumValueOrderActionType = typeChangeEnumValueOrderActionType;
function typeChangeFieldDefinitionLabelActionType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        action: Joi.string().required().only('changeFieldDefinitionLabel'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeFieldDefinitionLabelActionType = typeChangeFieldDefinitionLabelActionType;
function typeChangeFieldDefinitionOrderActionType() {
    return Joi.object().unknown().keys({
        fieldNames: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeFieldDefinitionOrder')
    });
}
exports.typeChangeFieldDefinitionOrderActionType = typeChangeFieldDefinitionOrderActionType;
function typeChangeInputHintActionType() {
    return Joi.object().unknown().keys({
        inputHint: typeTextInputHintType().required(),
        action: Joi.string().required().only('changeInputHint'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeInputHintActionType = typeChangeInputHintActionType;
function typeChangeKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeKey'),
        key: Joi.string().required()
    });
}
exports.typeChangeKeyActionType = typeChangeKeyActionType;
function typeChangeLabelActionType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        action: Joi.string().required().only('changeLabel'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeLabelActionType = typeChangeLabelActionType;
function typeChangeLocalizedEnumValueLabelActionType() {
    return Joi.object().unknown().keys({
        value: customFieldLocalizedEnumValueType().required(),
        action: Joi.string().required().only('changeLocalizedEnumValueLabel'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeLocalizedEnumValueLabelActionType = typeChangeLocalizedEnumValueLabelActionType;
function typeChangeLocalizedEnumValueOrderActionType() {
    return Joi.object().unknown().keys({
        keys: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeLocalizedEnumValueOrder'),
        fieldName: Joi.string().required()
    });
}
exports.typeChangeLocalizedEnumValueOrderActionType = typeChangeLocalizedEnumValueOrderActionType;
function typeChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_1.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.typeChangeNameActionType = typeChangeNameActionType;
function typeRemoveFieldDefinitionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeFieldDefinition'),
        fieldName: Joi.string().required()
    });
}
exports.typeRemoveFieldDefinitionActionType = typeRemoveFieldDefinitionActionType;
function typeSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_1.localizedStringType().optional()
    });
}
exports.typeSetDescriptionActionType = typeSetDescriptionActionType;
