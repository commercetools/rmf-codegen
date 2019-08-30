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
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const Product_types_1 = require("./Product-types");
const Channel_types_1 = require("./Channel-types");
const Type_types_2 = require("./Type-types");
function cartDiscountType() {
    return Joi.object().unknown().keys({
        references: Joi.array().items(Common_types_3.referenceType()).required(),
        value: cartDiscountValueType().required(),
        name: Common_types_4.localizedStringType().required(),
        stackingMode: stackingModeType().required(),
        isActive: Joi.boolean().required(),
        requiresDiscountCode: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        cartPredicate: Joi.string().required(),
        id: Joi.string().required(),
        sortOrder: Joi.string().required(),
        target: cartDiscountTargetType().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        key: Joi.string().optional()
    });
}
exports.cartDiscountType = cartDiscountType;
function cartDiscountCustomLineItemsTargetType() {
    return Joi.object().unknown().keys({
        predicate: Joi.string().required(),
        type: Joi.string().required().only('customLineItems')
    });
}
exports.cartDiscountCustomLineItemsTargetType = cartDiscountCustomLineItemsTargetType;
function cartDiscountDraftType() {
    return Joi.object().unknown().keys({
        value: cartDiscountValueType().required(),
        name: Common_types_4.localizedStringType().required(),
        requiresDiscountCode: Joi.boolean().required(),
        cartPredicate: Joi.string().required(),
        sortOrder: Joi.string().required(),
        target: cartDiscountTargetType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        description: Common_types_4.localizedStringType().optional(),
        stackingMode: stackingModeType().optional(),
        isActive: Joi.boolean().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        key: Joi.string().optional()
    });
}
exports.cartDiscountDraftType = cartDiscountDraftType;
function cartDiscountLineItemsTargetType() {
    return Joi.object().unknown().keys({
        predicate: Joi.string().required(),
        type: Joi.string().required().only('lineItems')
    });
}
exports.cartDiscountLineItemsTargetType = cartDiscountLineItemsTargetType;
function cartDiscountPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(cartDiscountType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.cartDiscountPagedQueryResponseType = cartDiscountPagedQueryResponseType;
function cartDiscountReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().required().only('cart-discount'),
        id: Joi.string().required(),
        obj: cartDiscountType().optional()
    });
}
exports.cartDiscountReferenceType = cartDiscountReferenceType;
function cartDiscountResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().optional().only('cart-discount'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.cartDiscountResourceIdentifierType = cartDiscountResourceIdentifierType;
function cartDiscountShippingCostTargetType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('shipping')
    });
}
exports.cartDiscountShippingCostTargetType = cartDiscountShippingCostTargetType;
function cartDiscountTargetType() {
    return Joi.alternatives([multiBuyCustomLineItemsTargetType(), multiBuyLineItemsTargetType(), cartDiscountCustomLineItemsTargetType(), cartDiscountLineItemsTargetType(), cartDiscountShippingCostTargetType()]);
}
exports.cartDiscountTargetType = cartDiscountTargetType;
function cartDiscountUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(cartDiscountUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.cartDiscountUpdateType = cartDiscountUpdateType;
function cartDiscountUpdateActionType() {
    return Joi.alternatives([cartDiscountChangeCartPredicateActionType(), cartDiscountChangeIsActiveActionType(), cartDiscountChangeNameActionType(), cartDiscountChangeRequiresDiscountCodeActionType(), cartDiscountChangeSortOrderActionType(), cartDiscountChangeStackingModeActionType(), cartDiscountChangeTargetActionType(), cartDiscountChangeValueActionType(), cartDiscountSetCustomFieldActionType(), cartDiscountSetCustomTypeActionType(), cartDiscountSetDescriptionActionType(), cartDiscountSetKeyActionType(), cartDiscountSetValidFromActionType(), cartDiscountSetValidFromAndUntilActionType(), cartDiscountSetValidUntilActionType()]);
}
exports.cartDiscountUpdateActionType = cartDiscountUpdateActionType;
function cartDiscountValueType() {
    return Joi.alternatives([cartDiscountValueAbsoluteType(), cartDiscountValueGiftLineItemType(), cartDiscountValueRelativeType()]);
}
exports.cartDiscountValueType = cartDiscountValueType;
function cartDiscountValueAbsoluteType() {
    return Joi.object().unknown().keys({
        money: Joi.array().items(Common_types_6.moneyType()).required(),
        type: Joi.string().required().only('absolute')
    });
}
exports.cartDiscountValueAbsoluteType = cartDiscountValueAbsoluteType;
function cartDiscountValueGiftLineItemType() {
    return Joi.object().unknown().keys({
        product: Product_types_1.productReferenceType().required(),
        variantId: Joi.number().required(),
        type: Joi.string().required().only('giftLineItem'),
        distributionChannel: Channel_types_1.channelReferenceType().optional(),
        supplyChannel: Channel_types_1.channelReferenceType().optional()
    });
}
exports.cartDiscountValueGiftLineItemType = cartDiscountValueGiftLineItemType;
function cartDiscountValueRelativeType() {
    return Joi.object().unknown().keys({
        permyriad: Joi.number().required(),
        type: Joi.string().required().only('relative')
    });
}
exports.cartDiscountValueRelativeType = cartDiscountValueRelativeType;
function multiBuyCustomLineItemsTargetType() {
    return Joi.object().unknown().keys({
        selectionMode: selectionModeType().required(),
        discountedQuantity: Joi.number().required(),
        triggerQuantity: Joi.number().required(),
        predicate: Joi.string().required(),
        type: Joi.string().required().only('multiBuyCustomLineItems'),
        maxOccurrence: Joi.number().optional()
    });
}
exports.multiBuyCustomLineItemsTargetType = multiBuyCustomLineItemsTargetType;
function multiBuyLineItemsTargetType() {
    return Joi.object().unknown().keys({
        selectionMode: selectionModeType().required(),
        discountedQuantity: Joi.number().required(),
        triggerQuantity: Joi.number().required(),
        predicate: Joi.string().required(),
        type: Joi.string().required().only('multiBuyLineItems'),
        maxOccurrence: Joi.number().optional()
    });
}
exports.multiBuyLineItemsTargetType = multiBuyLineItemsTargetType;
const selectionModeTypeValues = [
    'Cheapest',
    'MostExpensive'
];
function selectionModeType() {
    return Joi.string().only(selectionModeTypeValues);
}
exports.selectionModeType = selectionModeType;
const stackingModeTypeValues = [
    'Stacking',
    'StopAfterThisDiscount'
];
function stackingModeType() {
    return Joi.string().only(stackingModeTypeValues);
}
exports.stackingModeType = stackingModeType;
function cartDiscountChangeCartPredicateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeCartPredicate'),
        cartPredicate: Joi.string().required()
    });
}
exports.cartDiscountChangeCartPredicateActionType = cartDiscountChangeCartPredicateActionType;
function cartDiscountChangeIsActiveActionType() {
    return Joi.object().unknown().keys({
        isActive: Joi.boolean().required(),
        action: Joi.string().required().only('changeIsActive')
    });
}
exports.cartDiscountChangeIsActiveActionType = cartDiscountChangeIsActiveActionType;
function cartDiscountChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        action: Joi.string().required().only('changeName')
    });
}
exports.cartDiscountChangeNameActionType = cartDiscountChangeNameActionType;
function cartDiscountChangeRequiresDiscountCodeActionType() {
    return Joi.object().unknown().keys({
        requiresDiscountCode: Joi.boolean().required(),
        action: Joi.string().required().only('changeRequiresDiscountCode')
    });
}
exports.cartDiscountChangeRequiresDiscountCodeActionType = cartDiscountChangeRequiresDiscountCodeActionType;
function cartDiscountChangeSortOrderActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeSortOrder'),
        sortOrder: Joi.string().required()
    });
}
exports.cartDiscountChangeSortOrderActionType = cartDiscountChangeSortOrderActionType;
function cartDiscountChangeStackingModeActionType() {
    return Joi.object().unknown().keys({
        stackingMode: stackingModeType().required(),
        action: Joi.string().required().only('changeStackingMode')
    });
}
exports.cartDiscountChangeStackingModeActionType = cartDiscountChangeStackingModeActionType;
function cartDiscountChangeTargetActionType() {
    return Joi.object().unknown().keys({
        target: cartDiscountTargetType().required(),
        action: Joi.string().required().only('changeTarget')
    });
}
exports.cartDiscountChangeTargetActionType = cartDiscountChangeTargetActionType;
function cartDiscountChangeValueActionType() {
    return Joi.object().unknown().keys({
        value: cartDiscountValueType().required(),
        action: Joi.string().required().only('changeValue')
    });
}
exports.cartDiscountChangeValueActionType = cartDiscountChangeValueActionType;
function cartDiscountSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.cartDiscountSetCustomFieldActionType = cartDiscountSetCustomFieldActionType;
function cartDiscountSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        type: Type_types_2.typeResourceIdentifierType().optional(),
        fields: Joi.any().optional()
    });
}
exports.cartDiscountSetCustomTypeActionType = cartDiscountSetCustomTypeActionType;
function cartDiscountSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_4.localizedStringType().optional()
    });
}
exports.cartDiscountSetDescriptionActionType = cartDiscountSetDescriptionActionType;
function cartDiscountSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.cartDiscountSetKeyActionType = cartDiscountSetKeyActionType;
function cartDiscountSetValidFromActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFrom'),
        validFrom: Joi.date().iso().optional()
    });
}
exports.cartDiscountSetValidFromActionType = cartDiscountSetValidFromActionType;
function cartDiscountSetValidFromAndUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidFromAndUntil'),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional()
    });
}
exports.cartDiscountSetValidFromAndUntilActionType = cartDiscountSetValidFromAndUntilActionType;
function cartDiscountSetValidUntilActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setValidUntil'),
        validUntil: Joi.date().iso().optional()
    });
}
exports.cartDiscountSetValidUntilActionType = cartDiscountSetValidUntilActionType;
