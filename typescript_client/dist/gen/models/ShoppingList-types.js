"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_3 = require("./Common-types");
const Customer_types_1 = require("./Customer-types");
const Type_types_2 = require("./Type-types");
const Customer_types_2 = require("./Customer-types");
const Product_types_1 = require("./Product-types");
const ProductType_types_1 = require("./ProductType-types");
const Common_types_4 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function shoppingListType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        lineItems: Joi.array().items(shoppingListLineItemType()).optional(),
        textLineItems: Joi.array().items(textLineItemType()).optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customer: Customer_types_1.customerReferenceType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        slug: Common_types_3.localizedStringType().optional(),
        deleteDaysAfterLastModification: Joi.number().optional(),
        anonymousId: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.shoppingListType = shoppingListType;
function shoppingListDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        lineItems: Joi.array().items(shoppingListLineItemDraftType()).optional(),
        textLineItems: Joi.array().items(textLineItemDraftType()).optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customer: Customer_types_2.customerResourceIdentifierType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        slug: Common_types_3.localizedStringType().optional(),
        deleteDaysAfterLastModification: Joi.number().optional(),
        anonymousId: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.shoppingListDraftType = shoppingListDraftType;
function shoppingListLineItemType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        productType: ProductType_types_1.productTypeReferenceType().required(),
        addedAt: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        id: Joi.string().required(),
        productId: Joi.string().required(),
        custom: Type_types_1.customFieldsType().optional(),
        productSlug: Common_types_3.localizedStringType().optional(),
        variant: Product_types_1.productVariantType().optional(),
        deactivatedAt: Joi.date().iso().optional(),
        variantId: Joi.number().optional()
    });
}
exports.shoppingListLineItemType = shoppingListLineItemType;
function shoppingListLineItemDraftType() {
    return Joi.object().unknown().keys({
        custom: Type_types_2.customFieldsDraftType().optional(),
        addedAt: Joi.date().iso().optional(),
        quantity: Joi.number().optional(),
        variantId: Joi.number().optional(),
        productId: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.shoppingListLineItemDraftType = shoppingListLineItemDraftType;
function shoppingListPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(shoppingListType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.shoppingListPagedQueryResponseType = shoppingListPagedQueryResponseType;
function shoppingListReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().required().only('shopping-list'),
        id: Joi.string().required(),
        obj: shoppingListType().optional()
    });
}
exports.shoppingListReferenceType = shoppingListReferenceType;
function shoppingListResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().optional().only('shopping-list'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.shoppingListResourceIdentifierType = shoppingListResourceIdentifierType;
function shoppingListUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(shoppingListUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.shoppingListUpdateType = shoppingListUpdateType;
function shoppingListUpdateActionType() {
    return Joi.alternatives([shoppingListAddLineItemActionType(), shoppingListAddTextLineItemActionType(), shoppingListChangeLineItemQuantityActionType(), shoppingListChangeLineItemsOrderActionType(), shoppingListChangeNameActionType(), shoppingListChangeTextLineItemNameActionType(), shoppingListChangeTextLineItemQuantityActionType(), shoppingListChangeTextLineItemsOrderActionType(), shoppingListRemoveLineItemActionType(), shoppingListRemoveTextLineItemActionType(), shoppingListSetAnonymousIdActionType(), shoppingListSetCustomFieldActionType(), shoppingListSetCustomTypeActionType(), shoppingListSetCustomerActionType(), shoppingListSetDeleteDaysAfterLastModificationActionType(), shoppingListSetDescriptionActionType(), shoppingListSetKeyActionType(), shoppingListSetLineItemCustomFieldActionType(), shoppingListSetLineItemCustomTypeActionType(), shoppingListSetSlugActionType(), shoppingListSetTextLineItemCustomFieldActionType(), shoppingListSetTextLineItemCustomTypeActionType(), shoppingListSetTextLineItemDescriptionActionType()]);
}
exports.shoppingListUpdateActionType = shoppingListUpdateActionType;
function textLineItemType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        addedAt: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        id: Joi.string().required(),
        custom: Type_types_1.customFieldsType().optional(),
        description: Common_types_3.localizedStringType().optional()
    });
}
exports.textLineItemType = textLineItemType;
function textLineItemDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        addedAt: Joi.date().iso().optional(),
        quantity: Joi.number().optional()
    });
}
exports.textLineItemDraftType = textLineItemDraftType;
function shoppingListAddLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addLineItem'),
        custom: Type_types_2.customFieldsDraftType().optional(),
        addedAt: Joi.date().iso().optional(),
        quantity: Joi.number().optional(),
        variantId: Joi.number().optional(),
        productId: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.shoppingListAddLineItemActionType = shoppingListAddLineItemActionType;
function shoppingListAddTextLineItemActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('addTextLineItem'),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        addedAt: Joi.date().iso().optional(),
        quantity: Joi.number().optional()
    });
}
exports.shoppingListAddTextLineItemActionType = shoppingListAddTextLineItemActionType;
function shoppingListChangeLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeLineItemQuantity'),
        lineItemId: Joi.string().required()
    });
}
exports.shoppingListChangeLineItemQuantityActionType = shoppingListChangeLineItemQuantityActionType;
function shoppingListChangeLineItemsOrderActionType() {
    return Joi.object().unknown().keys({
        lineItemOrder: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeLineItemsOrder')
    });
}
exports.shoppingListChangeLineItemsOrderActionType = shoppingListChangeLineItemsOrderActionType;
function shoppingListChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.shoppingListChangeNameActionType = shoppingListChangeNameActionType;
function shoppingListChangeTextLineItemNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('changeTextLineItemName'),
        textLineItemId: Joi.string().required()
    });
}
exports.shoppingListChangeTextLineItemNameActionType = shoppingListChangeTextLineItemNameActionType;
function shoppingListChangeTextLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeTextLineItemQuantity'),
        textLineItemId: Joi.string().required()
    });
}
exports.shoppingListChangeTextLineItemQuantityActionType = shoppingListChangeTextLineItemQuantityActionType;
function shoppingListChangeTextLineItemsOrderActionType() {
    return Joi.object().unknown().keys({
        textLineItemOrder: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeTextLineItemsOrder')
    });
}
exports.shoppingListChangeTextLineItemsOrderActionType = shoppingListChangeTextLineItemsOrderActionType;
function shoppingListRemoveLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeLineItem'),
        lineItemId: Joi.string().required(),
        quantity: Joi.number().optional()
    });
}
exports.shoppingListRemoveLineItemActionType = shoppingListRemoveLineItemActionType;
function shoppingListRemoveTextLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeTextLineItem'),
        textLineItemId: Joi.string().required(),
        quantity: Joi.number().optional()
    });
}
exports.shoppingListRemoveTextLineItemActionType = shoppingListRemoveTextLineItemActionType;
function shoppingListSetAnonymousIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAnonymousId'),
        anonymousId: Joi.string().optional()
    });
}
exports.shoppingListSetAnonymousIdActionType = shoppingListSetAnonymousIdActionType;
function shoppingListSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.shoppingListSetCustomFieldActionType = shoppingListSetCustomFieldActionType;
function shoppingListSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.shoppingListSetCustomTypeActionType = shoppingListSetCustomTypeActionType;
function shoppingListSetCustomerActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomer'),
        customer: Customer_types_2.customerResourceIdentifierType().optional()
    });
}
exports.shoppingListSetCustomerActionType = shoppingListSetCustomerActionType;
function shoppingListSetDeleteDaysAfterLastModificationActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDeleteDaysAfterLastModification'),
        deleteDaysAfterLastModification: Joi.number().optional()
    });
}
exports.shoppingListSetDeleteDaysAfterLastModificationActionType = shoppingListSetDeleteDaysAfterLastModificationActionType;
function shoppingListSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_3.localizedStringType().optional()
    });
}
exports.shoppingListSetDescriptionActionType = shoppingListSetDescriptionActionType;
function shoppingListSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.shoppingListSetKeyActionType = shoppingListSetKeyActionType;
function shoppingListSetLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomField'),
        lineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.shoppingListSetLineItemCustomFieldActionType = shoppingListSetLineItemCustomFieldActionType;
function shoppingListSetLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomType'),
        lineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.shoppingListSetLineItemCustomTypeActionType = shoppingListSetLineItemCustomTypeActionType;
function shoppingListSetSlugActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setSlug'),
        slug: Common_types_3.localizedStringType().optional()
    });
}
exports.shoppingListSetSlugActionType = shoppingListSetSlugActionType;
function shoppingListSetTextLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTextLineItemCustomField'),
        name: Joi.string().required(),
        textLineItemId: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.shoppingListSetTextLineItemCustomFieldActionType = shoppingListSetTextLineItemCustomFieldActionType;
function shoppingListSetTextLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTextLineItemCustomType'),
        textLineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.shoppingListSetTextLineItemCustomTypeActionType = shoppingListSetTextLineItemCustomTypeActionType;
function shoppingListSetTextLineItemDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTextLineItemDescription'),
        textLineItemId: Joi.string().required(),
        description: Common_types_3.localizedStringType().optional()
    });
}
exports.shoppingListSetTextLineItemDescriptionActionType = shoppingListSetTextLineItemDescriptionActionType;
