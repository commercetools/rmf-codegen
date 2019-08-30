"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
function attributeBooleanTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('boolean')
    });
}
exports.attributeBooleanTypeType = attributeBooleanTypeType;
const attributeConstraintEnumTypeValues = [
    'None',
    'Unique',
    'CombinationUnique',
    'SameForAll'
];
function attributeConstraintEnumType() {
    return Joi.string().only(attributeConstraintEnumTypeValues);
}
exports.attributeConstraintEnumType = attributeConstraintEnumType;
const attributeConstraintEnumDraftTypeValues = [
    'None'
];
function attributeConstraintEnumDraftType() {
    return Joi.string().only(attributeConstraintEnumDraftTypeValues);
}
exports.attributeConstraintEnumDraftType = attributeConstraintEnumDraftType;
function attributeDateTimeTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('datetime')
    });
}
exports.attributeDateTimeTypeType = attributeDateTimeTypeType;
function attributeDateTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('date')
    });
}
exports.attributeDateTypeType = attributeDateTypeType;
function attributeDefinitionType() {
    return Joi.object().unknown().keys({
        attributeConstraint: attributeConstraintEnumType().required(),
        type: attributeTypeType().required(),
        label: Common_types_1.localizedStringType().required(),
        inputHint: textInputHintType().required(),
        isRequired: Joi.boolean().required(),
        isSearchable: Joi.boolean().required(),
        name: Joi.string().required(),
        inputTip: Common_types_1.localizedStringType().optional()
    });
}
exports.attributeDefinitionType = attributeDefinitionType;
function attributeDefinitionDraftType() {
    return Joi.object().unknown().keys({
        type: attributeTypeType().required(),
        label: Common_types_1.localizedStringType().required(),
        isRequired: Joi.boolean().required(),
        name: Joi.string().required(),
        attributeConstraint: attributeConstraintEnumType().optional(),
        inputTip: Common_types_1.localizedStringType().optional(),
        inputHint: textInputHintType().optional(),
        isSearchable: Joi.boolean().optional()
    });
}
exports.attributeDefinitionDraftType = attributeDefinitionDraftType;
function attributeEnumTypeType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(attributePlainEnumValueType()).required(),
        name: Joi.string().required().only('enum')
    });
}
exports.attributeEnumTypeType = attributeEnumTypeType;
function attributeLocalizableTextTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('ltext')
    });
}
exports.attributeLocalizableTextTypeType = attributeLocalizableTextTypeType;
function attributeLocalizedEnumTypeType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(attributeLocalizedEnumValueType()).required(),
        name: Joi.string().required().only('lenum')
    });
}
exports.attributeLocalizedEnumTypeType = attributeLocalizedEnumTypeType;
function attributeLocalizedEnumValueType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        key: Joi.string().required()
    });
}
exports.attributeLocalizedEnumValueType = attributeLocalizedEnumValueType;
function attributeMoneyTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('money')
    });
}
exports.attributeMoneyTypeType = attributeMoneyTypeType;
function attributeNestedTypeType() {
    return Joi.object().unknown().keys({
        typeReference: productTypeReferenceType().required(),
        name: Joi.string().required().only('nested')
    });
}
exports.attributeNestedTypeType = attributeNestedTypeType;
function attributeNumberTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('number')
    });
}
exports.attributeNumberTypeType = attributeNumberTypeType;
function attributePlainEnumValueType() {
    return Joi.object().unknown().keys({
        key: Joi.string().required(),
        label: Joi.string().required()
    });
}
exports.attributePlainEnumValueType = attributePlainEnumValueType;
function attributeReferenceTypeType() {
    return Joi.object().unknown().keys({
        referenceTypeId: Common_types_2.referenceTypeIdType().required(),
        name: Joi.string().required().only('reference')
    });
}
exports.attributeReferenceTypeType = attributeReferenceTypeType;
function attributeSetTypeType() {
    return Joi.object().unknown().keys({
        elementType: attributeTypeType().required(),
        name: Joi.string().required().only('set')
    });
}
exports.attributeSetTypeType = attributeSetTypeType;
function attributeTextTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('text')
    });
}
exports.attributeTextTypeType = attributeTextTypeType;
function attributeTimeTypeType() {
    return Joi.object().unknown().keys({
        name: Joi.string().required().only('time')
    });
}
exports.attributeTimeTypeType = attributeTimeTypeType;
function attributeTypeType() {
    return Joi.alternatives([attributeBooleanTypeType(), attributeDateTimeTypeType(), attributeDateTypeType(), attributeEnumTypeType(), attributeLocalizableTextTypeType(), attributeLocalizedEnumTypeType(), attributeMoneyTypeType(), attributeNestedTypeType(), attributeNumberTypeType(), attributeReferenceTypeType(), attributeSetTypeType(), attributeTextTypeType(), attributeTimeTypeType()]);
}
exports.attributeTypeType = attributeTypeType;
function productTypeType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        description: Joi.string().required(),
        id: Joi.string().required(),
        name: Joi.string().required(),
        attributes: Joi.array().items(attributeDefinitionType()).optional(),
        createdBy: Common_types_3.createdByType().optional(),
        lastModifiedBy: Common_types_4.lastModifiedByType().optional(),
        key: Joi.string().optional()
    });
}
exports.productTypeType = productTypeType;
function productTypeDraftType() {
    return Joi.object().unknown().keys({
        description: Joi.string().required(),
        name: Joi.string().required(),
        attributes: Joi.array().items(attributeDefinitionDraftType()).optional(),
        key: Joi.string().optional()
    });
}
exports.productTypeDraftType = productTypeDraftType;
function productTypePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(productTypeType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.productTypePagedQueryResponseType = productTypePagedQueryResponseType;
function productTypeReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().required().only('product-type'),
        id: Joi.string().required(),
        obj: productTypeType().optional()
    });
}
exports.productTypeReferenceType = productTypeReferenceType;
function productTypeResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_2.referenceTypeIdType().optional().only('product-type'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.productTypeResourceIdentifierType = productTypeResourceIdentifierType;
function productTypeUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(productTypeUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.productTypeUpdateType = productTypeUpdateType;
function productTypeUpdateActionType() {
    return Joi.alternatives([productTypeAddAttributeDefinitionActionType(), productTypeAddLocalizedEnumValueActionType(), productTypeAddPlainEnumValueActionType(), productTypeChangeAttributeConstraintActionType(), productTypeChangeAttributeNameActionType(), productTypeChangeAttributeOrderActionType(), productTypeChangeAttributeOrderByNameActionType(), productTypeChangeDescriptionActionType(), productTypeChangeEnumKeyActionType(), productTypeChangeInputHintActionType(), productTypeChangeIsSearchableActionType(), productTypeChangeLabelActionType(), productTypeChangeLocalizedEnumValueLabelActionType(), productTypeChangeLocalizedEnumValueOrderActionType(), productTypeChangeNameActionType(), productTypeChangePlainEnumValueLabelActionType(), productTypeChangePlainEnumValueOrderActionType(), productTypeRemoveAttributeDefinitionActionType(), productTypeRemoveEnumValuesActionType(), productTypeSetInputTipActionType(), productTypeSetKeyActionType()]);
}
exports.productTypeUpdateActionType = productTypeUpdateActionType;
const textInputHintTypeValues = [
    'SingleLine',
    'MultiLine'
];
function textInputHintType() {
    return Joi.string().only(textInputHintTypeValues);
}
exports.textInputHintType = textInputHintType;
function productTypeAddAttributeDefinitionActionType() {
    return Joi.object().unknown().keys({
        attribute: attributeDefinitionDraftType().required(),
        action: Joi.string().required().only('addAttributeDefinition')
    });
}
exports.productTypeAddAttributeDefinitionActionType = productTypeAddAttributeDefinitionActionType;
function productTypeAddLocalizedEnumValueActionType() {
    return Joi.object().unknown().keys({
        value: attributeLocalizedEnumValueType().required(),
        action: Joi.string().required().only('addLocalizedEnumValue'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeAddLocalizedEnumValueActionType = productTypeAddLocalizedEnumValueActionType;
function productTypeAddPlainEnumValueActionType() {
    return Joi.object().unknown().keys({
        value: attributePlainEnumValueType().required(),
        action: Joi.string().required().only('addPlainEnumValue'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeAddPlainEnumValueActionType = productTypeAddPlainEnumValueActionType;
function productTypeChangeAttributeConstraintActionType() {
    return Joi.object().unknown().keys({
        newValue: attributeConstraintEnumDraftType().required(),
        action: Joi.string().required().only('changeAttributeConstraint'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeAttributeConstraintActionType = productTypeChangeAttributeConstraintActionType;
function productTypeChangeAttributeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeAttributeName'),
        attributeName: Joi.string().required(),
        newAttributeName: Joi.string().required()
    });
}
exports.productTypeChangeAttributeNameActionType = productTypeChangeAttributeNameActionType;
function productTypeChangeAttributeOrderActionType() {
    return Joi.object().unknown().keys({
        attributes: Joi.array().items(attributeDefinitionType()).required(),
        action: Joi.string().required().only('changeAttributeOrder')
    });
}
exports.productTypeChangeAttributeOrderActionType = productTypeChangeAttributeOrderActionType;
function productTypeChangeAttributeOrderByNameActionType() {
    return Joi.object().unknown().keys({
        attributeNames: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeAttributeOrderByName')
    });
}
exports.productTypeChangeAttributeOrderByNameActionType = productTypeChangeAttributeOrderByNameActionType;
function productTypeChangeDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeDescription'),
        description: Joi.string().required()
    });
}
exports.productTypeChangeDescriptionActionType = productTypeChangeDescriptionActionType;
function productTypeChangeEnumKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeEnumKey'),
        attributeName: Joi.string().required(),
        key: Joi.string().required(),
        newKey: Joi.string().required()
    });
}
exports.productTypeChangeEnumKeyActionType = productTypeChangeEnumKeyActionType;
function productTypeChangeInputHintActionType() {
    return Joi.object().unknown().keys({
        newValue: textInputHintType().required(),
        action: Joi.string().required().only('changeInputHint'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeInputHintActionType = productTypeChangeInputHintActionType;
function productTypeChangeIsSearchableActionType() {
    return Joi.object().unknown().keys({
        isSearchable: Joi.boolean().required(),
        action: Joi.string().required().only('changeIsSearchable'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeIsSearchableActionType = productTypeChangeIsSearchableActionType;
function productTypeChangeLabelActionType() {
    return Joi.object().unknown().keys({
        label: Common_types_1.localizedStringType().required(),
        action: Joi.string().required().only('changeLabel'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeLabelActionType = productTypeChangeLabelActionType;
function productTypeChangeLocalizedEnumValueLabelActionType() {
    return Joi.object().unknown().keys({
        newValue: attributeLocalizedEnumValueType().required(),
        action: Joi.string().required().only('changeLocalizedEnumValueLabel'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeLocalizedEnumValueLabelActionType = productTypeChangeLocalizedEnumValueLabelActionType;
function productTypeChangeLocalizedEnumValueOrderActionType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(attributeLocalizedEnumValueType()).required(),
        action: Joi.string().required().only('changeLocalizedEnumValueOrder'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangeLocalizedEnumValueOrderActionType = productTypeChangeLocalizedEnumValueOrderActionType;
function productTypeChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.productTypeChangeNameActionType = productTypeChangeNameActionType;
function productTypeChangePlainEnumValueLabelActionType() {
    return Joi.object().unknown().keys({
        newValue: attributePlainEnumValueType().required(),
        action: Joi.string().required().only('changePlainEnumValueLabel'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangePlainEnumValueLabelActionType = productTypeChangePlainEnumValueLabelActionType;
function productTypeChangePlainEnumValueOrderActionType() {
    return Joi.object().unknown().keys({
        values: Joi.array().items(attributePlainEnumValueType()).required(),
        action: Joi.string().required().only('changePlainEnumValueOrder'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeChangePlainEnumValueOrderActionType = productTypeChangePlainEnumValueOrderActionType;
function productTypeRemoveAttributeDefinitionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeAttributeDefinition'),
        name: Joi.string().required()
    });
}
exports.productTypeRemoveAttributeDefinitionActionType = productTypeRemoveAttributeDefinitionActionType;
function productTypeRemoveEnumValuesActionType() {
    return Joi.object().unknown().keys({
        keys: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('removeEnumValues'),
        attributeName: Joi.string().required()
    });
}
exports.productTypeRemoveEnumValuesActionType = productTypeRemoveEnumValuesActionType;
function productTypeSetInputTipActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setInputTip'),
        attributeName: Joi.string().required(),
        inputTip: Common_types_1.localizedStringType().optional()
    });
}
exports.productTypeSetInputTipActionType = productTypeSetInputTipActionType;
function productTypeSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.productTypeSetKeyActionType = productTypeSetKeyActionType;
