"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Cart_types_1 = require("./Cart-types");
const Type_types_1 = require("./Type-types");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const Cart_types_2 = require("./Cart-types");
const Type_types_2 = require("./Type-types");
const Cart_types_3 = require("./Cart-types");
const Channel_types_1 = require("./Channel-types");
function myCartDraftType() {
    return Joi.object().unknown().keys({
        currency: Joi.string().required(),
        itemShippingAddresses: Joi.array().items(Common_types_1.addressType()).optional(),
        lineItems: Joi.array().items(myLineItemDraftType()).optional(),
        billingAddress: Common_types_1.addressType().optional(),
        shippingAddress: Common_types_1.addressType().optional(),
        custom: Type_types_1.customFieldsDraftType().optional(),
        inventoryMode: Cart_types_2.inventoryModeType().optional(),
        shippingMethod: ShippingMethod_types_1.shippingMethodResourceIdentifierType().optional(),
        taxMode: Cart_types_1.taxModeType().optional(),
        deleteDaysAfterLastModification: Joi.number().optional(),
        country: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        locale: Joi.string().optional()
    });
}
exports.myCartDraftType = myCartDraftType;
function myCustomerDraftType() {
    return Joi.object().unknown().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
        addresses: Joi.array().items(Common_types_1.addressType()).optional(),
        custom: Type_types_2.customFieldsType().optional(),
        dateOfBirth: Joi.date().iso().optional(),
        defaultBillingAddress: Joi.number().optional(),
        defaultShippingAddress: Joi.number().optional(),
        companyName: Joi.string().optional(),
        firstName: Joi.string().optional(),
        lastName: Joi.string().optional(),
        locale: Joi.string().optional(),
        middleName: Joi.string().optional(),
        title: Joi.string().optional(),
        vatId: Joi.string().optional()
    });
}
exports.myCustomerDraftType = myCustomerDraftType;
function myLineItemDraftType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        variantId: Joi.number().required(),
        productId: Joi.string().required(),
        distributionChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        custom: Type_types_1.customFieldsDraftType().optional(),
        shippingDetails: Cart_types_3.itemShippingDetailsDraftType().optional()
    });
}
exports.myLineItemDraftType = myLineItemDraftType;
function myOrderFromCartDraftType() {
    return Joi.object().unknown().keys({
        version: Joi.number().required(),
        id: Joi.string().required()
    });
}
exports.myOrderFromCartDraftType = myOrderFromCartDraftType;
