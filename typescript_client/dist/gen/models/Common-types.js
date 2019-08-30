"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Product_types_1 = require("./Product-types");
const Type_types_1 = require("./Type-types");
const Type_types_2 = require("./Type-types");
const Customer_types_1 = require("./Customer-types");
const ProductDiscount_types_1 = require("./ProductDiscount-types");
const Store_types_1 = require("./Store-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Channel_types_1 = require("./Channel-types");
const CustomerGroup_types_2 = require("./CustomerGroup-types");
const Channel_types_2 = require("./Channel-types");
const CustomObject_types_1 = require("./CustomObject-types");
const DiscountCode_types_1 = require("./DiscountCode-types");
const Inventory_types_1 = require("./Inventory-types");
const OrderEdit_types_1 = require("./OrderEdit-types");
const Order_types_1 = require("./Order-types");
const Payment_types_1 = require("./Payment-types");
const ProductType_types_1 = require("./ProductType-types");
const Product_types_2 = require("./Product-types");
const Review_types_1 = require("./Review-types");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const ShoppingList_types_1 = require("./ShoppingList-types");
const State_types_1 = require("./State-types");
const Store_types_2 = require("./Store-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const Type_types_3 = require("./Type-types");
const Zone_types_1 = require("./Zone-types");
const CartDiscount_types_1 = require("./CartDiscount-types");
const Cart_types_1 = require("./Cart-types");
const Category_types_1 = require("./Category-types");
const Customer_types_2 = require("./Customer-types");
const DiscountCode_types_2 = require("./DiscountCode-types");
const Inventory_types_2 = require("./Inventory-types");
const OrderEdit_types_2 = require("./OrderEdit-types");
const Order_types_2 = require("./Order-types");
const Payment_types_2 = require("./Payment-types");
const ProductDiscount_types_2 = require("./ProductDiscount-types");
const ProductType_types_2 = require("./ProductType-types");
const Product_types_3 = require("./Product-types");
const Review_types_2 = require("./Review-types");
const ShippingMethod_types_2 = require("./ShippingMethod-types");
const ShoppingList_types_2 = require("./ShoppingList-types");
const State_types_2 = require("./State-types");
const Store_types_3 = require("./Store-types");
const TaxCategory_types_2 = require("./TaxCategory-types");
const Type_types_4 = require("./Type-types");
const Zone_types_2 = require("./Zone-types");
const CartDiscount_types_2 = require("./CartDiscount-types");
const Cart_types_2 = require("./Cart-types");
const Category_types_2 = require("./Category-types");
function pagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(baseResourceType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        facets: Product_types_1.facetResultsType().optional(),
        total: Joi.number().optional(),
        meta: Joi.any().optional()
    });
}
exports.pagedQueryResponseType = pagedQueryResponseType;
function updateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(updateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.updateType = updateType;
function updateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required()
    });
}
exports.updateActionType = updateActionType;
function addressType() {
    return Joi.object().unknown().keys({
        country: Joi.string().required(),
        additionalAddressInfo: Joi.string().optional(),
        additionalStreetInfo: Joi.string().optional(),
        apartment: Joi.string().optional(),
        building: Joi.string().optional(),
        city: Joi.string().optional(),
        company: Joi.string().optional(),
        department: Joi.string().optional(),
        email: Joi.string().optional(),
        externalId: Joi.string().optional(),
        fax: Joi.string().optional(),
        firstName: Joi.string().optional(),
        id: Joi.string().optional(),
        key: Joi.string().optional(),
        lastName: Joi.string().optional(),
        mobile: Joi.string().optional(),
        pOBox: Joi.string().optional(),
        phone: Joi.string().optional(),
        postalCode: Joi.string().optional(),
        region: Joi.string().optional(),
        salutation: Joi.string().optional(),
        state: Joi.string().optional(),
        streetName: Joi.string().optional(),
        streetNumber: Joi.string().optional(),
        title: Joi.string().optional()
    });
}
exports.addressType = addressType;
function assetType() {
    return Joi.object().unknown().keys({
        sources: Joi.array().items(assetSourceType()).required(),
        name: localizedStringType().required(),
        id: Joi.string().required(),
        tags: Joi.array().items(Joi.string()).optional(),
        custom: Type_types_1.customFieldsType().optional(),
        description: localizedStringType().optional(),
        key: Joi.string().optional()
    });
}
exports.assetType = assetType;
function assetDimensionsType() {
    return Joi.object().unknown().keys({
        h: Joi.number().required(),
        w: Joi.number().required()
    });
}
exports.assetDimensionsType = assetDimensionsType;
function assetDraftType() {
    return Joi.object().unknown().keys({
        sources: Joi.array().items(assetSourceType()).required(),
        name: localizedStringType().required(),
        tags: Joi.array().items(Joi.string()).optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        description: localizedStringType().optional(),
        key: Joi.string().optional()
    });
}
exports.assetDraftType = assetDraftType;
function assetSourceType() {
    return Joi.object().unknown().keys({
        uri: Joi.string().required(),
        dimensions: assetDimensionsType().optional(),
        contentType: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.assetSourceType = assetSourceType;
function baseResourceType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required()
    });
}
exports.baseResourceType = baseResourceType;
function centPrecisionMoneyType() {
    return Joi.object().unknown().keys({
        currencyCode: Joi.string().required(),
        type: moneyTypeType().required().only('centPrecision'),
        centAmount: Joi.number().required(),
        fractionDigits: Joi.number().required()
    });
}
exports.centPrecisionMoneyType = centPrecisionMoneyType;
function clientLoggingType() {
    return Joi.object().unknown().keys({
        customer: Customer_types_1.customerReferenceType().optional(),
        anonymousId: Joi.string().optional(),
        clientId: Joi.string().optional(),
        externalUserId: Joi.string().optional()
    });
}
exports.clientLoggingType = clientLoggingType;
function createdByType() {
    return Joi.object().unknown().keys({
        customer: Customer_types_1.customerReferenceType().optional(),
        anonymousId: Joi.string().optional(),
        clientId: Joi.string().optional(),
        externalUserId: Joi.string().optional()
    });
}
exports.createdByType = createdByType;
function discountedPriceType() {
    return Joi.object().unknown().keys({
        value: moneyType().required(),
        discount: ProductDiscount_types_1.productDiscountReferenceType().required()
    });
}
exports.discountedPriceType = discountedPriceType;
function geoJsonType() {
    return Joi.alternatives([geoJsonPointType()]);
}
exports.geoJsonType = geoJsonType;
function geoJsonPointType() {
    return Joi.object().unknown().keys({
        coordinates: Joi.array().items(Joi.number()).required(),
        type: Joi.string().required().only('Point')
    });
}
exports.geoJsonPointType = geoJsonPointType;
function highPrecisionMoneyType() {
    return Joi.object().unknown().keys({
        currencyCode: Joi.string().required(),
        type: moneyTypeType().required().only('highPrecision'),
        centAmount: Joi.number().required(),
        fractionDigits: Joi.number().required(),
        preciseAmount: Joi.number().required()
    });
}
exports.highPrecisionMoneyType = highPrecisionMoneyType;
function imageType() {
    return Joi.object().unknown().keys({
        dimensions: imageDimensionsType().required(),
        url: Joi.string().required(),
        label: Joi.string().optional()
    });
}
exports.imageType = imageType;
function imageDimensionsType() {
    return Joi.object().unknown().keys({
        h: Joi.number().required(),
        w: Joi.number().required()
    });
}
exports.imageDimensionsType = imageDimensionsType;
function keyReferenceType() {
    return Joi.alternatives([Store_types_1.storeKeyReferenceType()]);
}
exports.keyReferenceType = keyReferenceType;
function lastModifiedByType() {
    return Joi.object().unknown().keys({
        customer: Customer_types_1.customerReferenceType().optional(),
        anonymousId: Joi.string().optional(),
        clientId: Joi.string().optional(),
        externalUserId: Joi.string().optional()
    });
}
exports.lastModifiedByType = lastModifiedByType;
function localizedStringType() {
    return Joi.object().pattern(new RegExp('/^[a-z]{2}(-[A-Z]{2})?$/'), Joi.string());
}
exports.localizedStringType = localizedStringType;
function loggedResourceType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        createdBy: createdByType().optional(),
        lastModifiedBy: lastModifiedByType().optional()
    });
}
exports.loggedResourceType = loggedResourceType;
function moneyType() {
    return Joi.object().unknown().keys({
        currencyCode: Joi.string().required(),
        centAmount: Joi.number().required()
    });
}
exports.moneyType = moneyType;
const moneyTypeTypeValues = [
    'centPrecision',
    'highPrecision'
];
function moneyTypeType() {
    return Joi.string().only(moneyTypeTypeValues);
}
exports.moneyTypeType = moneyTypeType;
function priceType() {
    return Joi.object().unknown().keys({
        value: moneyType().required(),
        tiers: Joi.array().items(priceTierType()).optional(),
        channel: Channel_types_1.channelReferenceType().optional(),
        country: Joi.string().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        discounted: discountedPriceType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional(),
        id: Joi.string().optional()
    });
}
exports.priceType = priceType;
function priceDraftType() {
    return Joi.object().unknown().keys({
        value: moneyType().required(),
        tiers: Joi.array().items(priceTierType()).optional(),
        channel: Channel_types_2.channelResourceIdentifierType().optional(),
        country: Joi.string().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional()
    });
}
exports.priceDraftType = priceDraftType;
function priceTierType() {
    return Joi.object().unknown().keys({
        value: moneyType().required(),
        minimumQuantity: Joi.number().required()
    });
}
exports.priceTierType = priceTierType;
function referenceType() {
    return Joi.alternatives([CustomObject_types_1.customObjectReferenceType(), CustomerGroup_types_1.customerGroupReferenceType(), Customer_types_1.customerReferenceType(), DiscountCode_types_1.discountCodeReferenceType(), Inventory_types_1.inventoryEntryReferenceType(), OrderEdit_types_1.orderEditReferenceType(), Order_types_1.orderReferenceType(), Payment_types_1.paymentReferenceType(), ProductDiscount_types_1.productDiscountReferenceType(), ProductType_types_1.productTypeReferenceType(), Product_types_2.productReferenceType(), Review_types_1.reviewReferenceType(), ShippingMethod_types_1.shippingMethodReferenceType(), ShoppingList_types_1.shoppingListReferenceType(), State_types_1.stateReferenceType(), Store_types_2.storeReferenceType(), TaxCategory_types_1.taxCategoryReferenceType(), Type_types_3.typeReferenceType(), Zone_types_1.zoneReferenceType(), CartDiscount_types_1.cartDiscountReferenceType(), Cart_types_1.cartReferenceType(), Category_types_1.categoryReferenceType(), Channel_types_1.channelReferenceType()]);
}
exports.referenceType = referenceType;
const referenceTypeIdTypeValues = [
    'cart',
    'cart-discount',
    'category',
    'channel',
    'customer',
    'customer-group',
    'discount-code',
    'key-value-document',
    'payment',
    'product',
    'product-type',
    'product-discount',
    'order',
    'review',
    'shopping-list',
    'shipping-method',
    'state',
    'store',
    'tax-category',
    'type',
    'zone',
    'inventory-entry',
    'order-edit'
];
function referenceTypeIdType() {
    return Joi.string().only(referenceTypeIdTypeValues);
}
exports.referenceTypeIdType = referenceTypeIdType;
function resourceIdentifierType() {
    return Joi.alternatives([CustomerGroup_types_2.customerGroupResourceIdentifierType(), Customer_types_2.customerResourceIdentifierType(), DiscountCode_types_2.discountCodeResourceIdentifierType(), Inventory_types_2.inventoryEntryResourceIdentifierType(), OrderEdit_types_2.orderEditResourceIdentifierType(), Order_types_2.orderResourceIdentifierType(), Payment_types_2.paymentResourceIdentifierType(), ProductDiscount_types_2.productDiscountResourceIdentifierType(), ProductType_types_2.productTypeResourceIdentifierType(), Product_types_3.productResourceIdentifierType(), Review_types_2.reviewResourceIdentifierType(), ShippingMethod_types_2.shippingMethodResourceIdentifierType(), ShoppingList_types_2.shoppingListResourceIdentifierType(), State_types_2.stateResourceIdentifierType(), Store_types_3.storeResourceIdentifierType(), TaxCategory_types_2.taxCategoryResourceIdentifierType(), Type_types_4.typeResourceIdentifierType(), Zone_types_2.zoneResourceIdentifierType(), CartDiscount_types_2.cartDiscountResourceIdentifierType(), Cart_types_2.cartResourceIdentifierType(), Category_types_2.categoryResourceIdentifierType(), Channel_types_2.channelResourceIdentifierType()]);
}
exports.resourceIdentifierType = resourceIdentifierType;
function scopedPriceType() {
    return Joi.object().unknown().keys({
        currentValue: typedMoneyType().required(),
        value: typedMoneyType().required(),
        id: Joi.string().required(),
        channel: Channel_types_1.channelReferenceType().optional(),
        country: Joi.string().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        discounted: discountedPriceType().optional(),
        validFrom: Joi.date().iso().optional(),
        validUntil: Joi.date().iso().optional()
    });
}
exports.scopedPriceType = scopedPriceType;
function typedMoneyType() {
    return Joi.alternatives([centPrecisionMoneyType(), highPrecisionMoneyType()]);
}
exports.typedMoneyType = typedMoneyType;
