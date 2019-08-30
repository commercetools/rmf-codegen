"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_5 = require("./Common-types");
const Type_types_2 = require("./Type-types");
const Common_types_6 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Common_types_7 = require("./Common-types");
const Type_types_4 = require("./Type-types");
function categoryType() {
    return Joi.object().unknown().keys({
        ancestors: Joi.array().items(categoryReferenceType()).required(),
        name: Common_types_4.localizedStringType().required(),
        slug: Common_types_4.localizedStringType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        orderHint: Joi.string().required(),
        assets: Joi.array().items(Common_types_3.assetType()).optional(),
        parent: categoryReferenceType().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        metaDescription: Common_types_4.localizedStringType().optional(),
        metaKeywords: Common_types_4.localizedStringType().optional(),
        metaTitle: Common_types_4.localizedStringType().optional(),
        externalId: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.categoryType = categoryType;
function categoryDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        slug: Common_types_4.localizedStringType().required(),
        assets: Joi.array().items(Common_types_5.assetDraftType()).optional(),
        parent: categoryResourceIdentifierType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        metaDescription: Common_types_4.localizedStringType().optional(),
        metaKeywords: Common_types_4.localizedStringType().optional(),
        metaTitle: Common_types_4.localizedStringType().optional(),
        externalId: Joi.string().optional(),
        key: Joi.string().optional(),
        orderHint: Joi.string().optional()
    });
}
exports.categoryDraftType = categoryDraftType;
function categoryPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(categoryType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.categoryPagedQueryResponseType = categoryPagedQueryResponseType;
function categoryReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().required().only('category'),
        id: Joi.string().required(),
        obj: categoryType().optional()
    });
}
exports.categoryReferenceType = categoryReferenceType;
function categoryResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().optional().only('category'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.categoryResourceIdentifierType = categoryResourceIdentifierType;
function categoryUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(categoryUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.categoryUpdateType = categoryUpdateType;
function categoryUpdateActionType() {
    return Joi.alternatives([categoryAddAssetActionType(), categoryChangeAssetNameActionType(), categoryChangeAssetOrderActionType(), categoryChangeNameActionType(), categoryChangeOrderHintActionType(), categoryChangeParentActionType(), categoryChangeSlugActionType(), categoryRemoveAssetActionType(), categorySetAssetCustomFieldActionType(), categorySetAssetCustomTypeActionType(), categorySetAssetDescriptionActionType(), categorySetAssetKeyActionType(), categorySetAssetSourcesActionType(), categorySetAssetTagsActionType(), categorySetCustomFieldActionType(), categorySetCustomTypeActionType(), categorySetDescriptionActionType(), categorySetExternalIdActionType(), categorySetKeyActionType(), categorySetMetaDescriptionActionType(), categorySetMetaKeywordsActionType(), categorySetMetaTitleActionType()]);
}
exports.categoryUpdateActionType = categoryUpdateActionType;
function categoryAddAssetActionType() {
    return Joi.object().unknown().keys({
        asset: Common_types_5.assetDraftType().required(),
        action: Joi.string().required().only('addAsset'),
        position: Joi.number().optional()
    });
}
exports.categoryAddAssetActionType = categoryAddAssetActionType;
function categoryChangeAssetNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeAssetName'),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categoryChangeAssetNameActionType = categoryChangeAssetNameActionType;
function categoryChangeAssetOrderActionType() {
    return Joi.object().unknown().keys({
        assetOrder: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeAssetOrder')
    });
}
exports.categoryChangeAssetOrderActionType = categoryChangeAssetOrderActionType;
function categoryChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.categoryChangeNameActionType = categoryChangeNameActionType;
function categoryChangeOrderHintActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeOrderHint'),
        orderHint: Joi.string().required()
    });
}
exports.categoryChangeOrderHintActionType = categoryChangeOrderHintActionType;
function categoryChangeParentActionType() {
    return Joi.object().unknown().keys({
        parent: categoryResourceIdentifierType().required(),
        action: Joi.string().required().only('changeParent')
    });
}
exports.categoryChangeParentActionType = categoryChangeParentActionType;
function categoryChangeSlugActionType() {
    return Joi.object().unknown().keys({
        slug: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeSlug')
    });
}
exports.categoryChangeSlugActionType = categoryChangeSlugActionType;
function categoryRemoveAssetActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeAsset'),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categoryRemoveAssetActionType = categoryRemoveAssetActionType;
function categorySetAssetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetCustomFieldActionType = categorySetAssetCustomFieldActionType;
function categorySetAssetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetCustomType'),
        type: Type_types_3.typeResourceIdentifierType().optional(),
        fields: Joi.any().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetCustomTypeActionType = categorySetAssetCustomTypeActionType;
function categorySetAssetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetDescription'),
        description: Common_types_4.localizedStringType().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetDescriptionActionType = categorySetAssetDescriptionActionType;
function categorySetAssetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetKey'),
        assetId: Joi.string().required(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetKeyActionType = categorySetAssetKeyActionType;
function categorySetAssetSourcesActionType() {
    return Joi.object().unknown().keys({
        sources: Joi.array().items(Common_types_7.assetSourceType()).required(),
        action: Joi.string().required().only('setAssetSources'),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetSourcesActionType = categorySetAssetSourcesActionType;
function categorySetAssetTagsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetTags'),
        tags: Joi.array().items(Joi.string()).optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional()
    });
}
exports.categorySetAssetTagsActionType = categorySetAssetTagsActionType;
function categorySetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.categorySetCustomFieldActionType = categorySetCustomFieldActionType;
function categorySetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_4.fieldContainerType().optional(),
        type: Type_types_3.typeResourceIdentifierType().optional()
    });
}
exports.categorySetCustomTypeActionType = categorySetCustomTypeActionType;
function categorySetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_4.localizedStringType().optional()
    });
}
exports.categorySetDescriptionActionType = categorySetDescriptionActionType;
function categorySetExternalIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setExternalId'),
        externalId: Joi.string().optional()
    });
}
exports.categorySetExternalIdActionType = categorySetExternalIdActionType;
function categorySetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.categorySetKeyActionType = categorySetKeyActionType;
function categorySetMetaDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaDescription'),
        metaDescription: Common_types_4.localizedStringType().optional()
    });
}
exports.categorySetMetaDescriptionActionType = categorySetMetaDescriptionActionType;
function categorySetMetaKeywordsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaKeywords'),
        metaKeywords: Common_types_4.localizedStringType().optional()
    });
}
exports.categorySetMetaKeywordsActionType = categorySetMetaKeywordsActionType;
function categorySetMetaTitleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaTitle'),
        metaTitle: Common_types_4.localizedStringType().optional()
    });
}
exports.categorySetMetaTitleActionType = categorySetMetaTitleActionType;
