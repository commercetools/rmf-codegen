"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Category_types_1 = require("./Category-types");
const Common_types_2 = require("./Common-types");
const State_types_1 = require("./State-types");
const Common_types_3 = require("./Common-types");
const Customer_types_1 = require("./Customer-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Order_types_1 = require("./Order-types");
const Order_types_2 = require("./Order-types");
const Channel_types_1 = require("./Channel-types");
const Order_types_3 = require("./Order-types");
const Cart_types_1 = require("./Cart-types");
const Cart_types_2 = require("./Cart-types");
const Customer_types_2 = require("./Customer-types");
const DiscountCode_types_1 = require("./DiscountCode-types");
const Cart_types_3 = require("./Cart-types");
const OrderEdit_types_1 = require("./OrderEdit-types");
const OrderEdit_types_2 = require("./OrderEdit-types");
const Cart_types_4 = require("./Cart-types");
const Common_types_4 = require("./Common-types");
const Order_types_4 = require("./Order-types");
const Order_types_5 = require("./Order-types");
const Order_types_6 = require("./Order-types");
const Order_types_7 = require("./Order-types");
const Cart_types_5 = require("./Cart-types");
const Cart_types_6 = require("./Cart-types");
const Order_types_8 = require("./Order-types");
const Order_types_9 = require("./Order-types");
const Order_types_10 = require("./Order-types");
const Order_types_11 = require("./Order-types");
const Payment_types_1 = require("./Payment-types");
const Type_types_1 = require("./Type-types");
const Payment_types_2 = require("./Payment-types");
const Payment_types_3 = require("./Payment-types");
const Product_types_1 = require("./Product-types");
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const Cart_types_7 = require("./Cart-types");
const Product_types_2 = require("./Product-types");
const Review_types_1 = require("./Review-types");
function categoryCreatedMessageType() {
    return Joi.object().unknown().keys({
        category: Category_types_1.categoryType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.categoryCreatedMessageType = categoryCreatedMessageType;
function categorySlugChangedMessageType() {
    return Joi.object().unknown().keys({
        slug: Common_types_2.localizedStringType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.categorySlugChangedMessageType = categorySlugChangedMessageType;
function customLineItemStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        fromState: State_types_1.stateReferenceType().required(),
        toState: State_types_1.stateReferenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        transitionDate: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        customLineItemId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customLineItemStateTransitionMessageType = customLineItemStateTransitionMessageType;
function customerAddressAddedMessageType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerAddressAddedMessageType = customerAddressAddedMessageType;
function customerAddressChangedMessageType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerAddressChangedMessageType = customerAddressChangedMessageType;
function customerAddressRemovedMessageType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerAddressRemovedMessageType = customerAddressRemovedMessageType;
function customerCompanyNameSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        companyName: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerCompanyNameSetMessageType = customerCompanyNameSetMessageType;
function customerCreatedMessageType() {
    return Joi.object().unknown().keys({
        customer: Customer_types_1.customerType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerCreatedMessageType = customerCreatedMessageType;
function customerDateOfBirthSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        dateOfBirth: Joi.date().iso().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerDateOfBirthSetMessageType = customerDateOfBirthSetMessageType;
function customerEmailChangedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        email: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerEmailChangedMessageType = customerEmailChangedMessageType;
function customerEmailVerifiedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerEmailVerifiedMessageType = customerEmailVerifiedMessageType;
function customerGroupSetMessageType() {
    return Joi.object().unknown().keys({
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.customerGroupSetMessageType = customerGroupSetMessageType;
function deliveryAddedMessageType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.deliveryAddedMessageType = deliveryAddedMessageType;
function deliveryAddressSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        deliveryId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.deliveryAddressSetMessageType = deliveryAddressSetMessageType;
function deliveryItemsUpdatedMessageType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        oldItems: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        deliveryId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.deliveryItemsUpdatedMessageType = deliveryItemsUpdatedMessageType;
function deliveryRemovedMessageType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.deliveryRemovedMessageType = deliveryRemovedMessageType;
function inventoryEntryDeletedMessageType() {
    return Joi.object().unknown().keys({
        supplyChannel: Channel_types_1.channelReferenceType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        sku: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.inventoryEntryDeletedMessageType = inventoryEntryDeletedMessageType;
function lineItemStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        fromState: State_types_1.stateReferenceType().required(),
        toState: State_types_1.stateReferenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        transitionDate: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        lineItemId: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.lineItemStateTransitionMessageType = lineItemStateTransitionMessageType;
function messageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.messageType = messageType;
function messageConfigurationType() {
    return Joi.object().unknown().keys({
        enabled: Joi.boolean().required(),
        deleteDaysAfterCreation: Joi.number().optional()
    });
}
exports.messageConfigurationType = messageConfigurationType;
function messageConfigurationDraftType() {
    return Joi.object().unknown().keys({
        enabled: Joi.boolean().required(),
        deleteDaysAfterCreation: Joi.number().required()
    });
}
exports.messageConfigurationDraftType = messageConfigurationDraftType;
function messagePagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(messageType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.messagePagedQueryResponseType = messagePagedQueryResponseType;
function orderBillingAddressSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderBillingAddressSetMessageType = orderBillingAddressSetMessageType;
function orderCreatedMessageType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderCreatedMessageType = orderCreatedMessageType;
function orderCustomLineItemDiscountSetMessageType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(Cart_types_2.discountedLineItemPriceForQuantityType()).required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        customLineItemId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        taxedPrice: Cart_types_1.taxedItemPriceType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderCustomLineItemDiscountSetMessageType = orderCustomLineItemDiscountSetMessageType;
function orderCustomerEmailSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
        email: Joi.string().optional(),
        oldEmail: Joi.string().optional()
    });
}
exports.orderCustomerEmailSetMessageType = orderCustomerEmailSetMessageType;
function orderCustomerSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        oldCustomerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        customer: Customer_types_2.customerReferenceType().optional(),
        oldCustomer: Customer_types_2.customerReferenceType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderCustomerSetMessageType = orderCustomerSetMessageType;
function orderDeletedMessageType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderDeletedMessageType = orderDeletedMessageType;
function orderDiscountCodeAddedMessageType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderDiscountCodeAddedMessageType = orderDiscountCodeAddedMessageType;
function orderDiscountCodeRemovedMessageType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderDiscountCodeRemovedMessageType = orderDiscountCodeRemovedMessageType;
function orderDiscountCodeStateSetMessageType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        state: Cart_types_3.discountCodeStateType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        oldState: Cart_types_3.discountCodeStateType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderDiscountCodeStateSetMessageType = orderDiscountCodeStateSetMessageType;
function orderEditAppliedMessageType() {
    return Joi.object().unknown().keys({
        result: OrderEdit_types_1.orderEditAppliedType().required(),
        edit: OrderEdit_types_2.orderEditReferenceType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderEditAppliedMessageType = orderEditAppliedMessageType;
function orderImportedMessageType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderImportedMessageType = orderImportedMessageType;
function orderLineItemAddedMessageType() {
    return Joi.object().unknown().keys({
        lineItem: Cart_types_4.lineItemType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        addedQuantity: Joi.number().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderLineItemAddedMessageType = orderLineItemAddedMessageType;
function orderLineItemDiscountSetMessageType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(Cart_types_2.discountedLineItemPriceForQuantityType()).required(),
        totalPrice: Common_types_4.moneyType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        lineItemId: Joi.string().required(),
        type: Joi.string().required(),
        taxedPrice: Cart_types_1.taxedItemPriceType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderLineItemDiscountSetMessageType = orderLineItemDiscountSetMessageType;
function orderPaymentStateChangedMessageType() {
    return Joi.object().unknown().keys({
        oldPaymentState: Order_types_4.paymentStateType().required(),
        paymentState: Order_types_4.paymentStateType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderPaymentStateChangedMessageType = orderPaymentStateChangedMessageType;
function orderReturnInfoAddedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        returnInfo: Order_types_5.returnInfoType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderReturnInfoAddedMessageType = orderReturnInfoAddedMessageType;
function orderReturnShipmentStateChangedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        returnShipmentState: Order_types_6.returnShipmentStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        returnItemId: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderReturnShipmentStateChangedMessageType = orderReturnShipmentStateChangedMessageType;
function orderShipmentStateChangedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        oldShipmentState: Order_types_7.shipmentStateType().required(),
        shipmentState: Order_types_7.shipmentStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderShipmentStateChangedMessageType = orderShipmentStateChangedMessageType;
function orderShippingAddressSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderShippingAddressSetMessageType = orderShippingAddressSetMessageType;
function orderShippingInfoSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        oldShippingInfo: Cart_types_5.shippingInfoType().optional(),
        shippingInfo: Cart_types_5.shippingInfoType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderShippingInfoSetMessageType = orderShippingInfoSetMessageType;
function orderShippingRateInputSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        oldShippingRateInput: Cart_types_6.shippingRateInputType().optional(),
        shippingRateInput: Cart_types_6.shippingRateInputType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderShippingRateInputSetMessageType = orderShippingRateInputSetMessageType;
function orderStateChangedMessageType() {
    return Joi.object().unknown().keys({
        oldOrderState: Order_types_8.orderStateType().required(),
        orderState: Order_types_8.orderStateType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderStateChangedMessageType = orderStateChangedMessageType;
function orderStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.orderStateTransitionMessageType = orderStateTransitionMessageType;
function parcelAddedToDeliveryMessageType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        parcel: Order_types_9.parcelType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.parcelAddedToDeliveryMessageType = parcelAddedToDeliveryMessageType;
function parcelItemsUpdatedMessageType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        oldItems: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
        deliveryId: Joi.string().optional()
    });
}
exports.parcelItemsUpdatedMessageType = parcelItemsUpdatedMessageType;
function parcelMeasurementsUpdatedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        deliveryId: Joi.string().required(),
        id: Joi.string().required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required(),
        measurements: Order_types_10.parcelMeasurementsType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.parcelMeasurementsUpdatedMessageType = parcelMeasurementsUpdatedMessageType;
function parcelRemovedFromDeliveryMessageType() {
    return Joi.object().unknown().keys({
        parcel: Order_types_9.parcelType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        deliveryId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.parcelRemovedFromDeliveryMessageType = parcelRemovedFromDeliveryMessageType;
function parcelTrackingDataUpdatedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        deliveryId: Joi.string().required(),
        id: Joi.string().required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required(),
        trackingData: Order_types_11.trackingDataType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.parcelTrackingDataUpdatedMessageType = parcelTrackingDataUpdatedMessageType;
function paymentCreatedMessageType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentCreatedMessageType = paymentCreatedMessageType;
function paymentInteractionAddedMessageType() {
    return Joi.object().unknown().keys({
        interaction: Type_types_1.customFieldsType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentInteractionAddedMessageType = paymentInteractionAddedMessageType;
function paymentStatusInterfaceCodeSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        interfaceCode: Joi.string().required(),
        paymentId: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentStatusInterfaceCodeSetMessageType = paymentStatusInterfaceCodeSetMessageType;
function paymentStatusStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentStatusStateTransitionMessageType = paymentStatusStateTransitionMessageType;
function paymentTransactionAddedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        transaction: Payment_types_2.transactionType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentTransactionAddedMessageType = paymentTransactionAddedMessageType;
function paymentTransactionStateChangedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        state: Payment_types_3.transactionStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        transactionId: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.paymentTransactionStateChangedMessageType = paymentTransactionStateChangedMessageType;
function productCreatedMessageType() {
    return Joi.object().unknown().keys({
        productProjection: Product_types_1.productProjectionType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productCreatedMessageType = productCreatedMessageType;
function productDeletedMessageType() {
    return Joi.object().unknown().keys({
        currentProjection: Product_types_1.productProjectionType().required(),
        resource: Common_types_1.referenceType().required(),
        removedImageUrls: Joi.array().items(Joi.string()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productDeletedMessageType = productDeletedMessageType;
function productImageAddedMessageType() {
    return Joi.object().unknown().keys({
        image: Common_types_5.imageType().required(),
        resource: Common_types_1.referenceType().required(),
        staged: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        variantId: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productImageAddedMessageType = productImageAddedMessageType;
function productPriceDiscountsSetMessageType() {
    return Joi.object().unknown().keys({
        updatedPrices: Joi.array().items(productPriceDiscountsSetUpdatedPriceType()).required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productPriceDiscountsSetMessageType = productPriceDiscountsSetMessageType;
function productPriceDiscountsSetUpdatedPriceType() {
    return Joi.object().unknown().keys({
        staged: Joi.boolean().required(),
        variantId: Joi.number().required(),
        priceId: Joi.string().required(),
        discounted: Common_types_6.discountedPriceType().optional(),
        sku: Joi.string().optional(),
        variantKey: Joi.string().optional()
    });
}
exports.productPriceDiscountsSetUpdatedPriceType = productPriceDiscountsSetUpdatedPriceType;
function productPriceExternalDiscountSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        staged: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        variantId: Joi.number().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        priceId: Joi.string().required(),
        type: Joi.string().required(),
        discounted: Common_types_6.discountedPriceType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
        sku: Joi.string().optional(),
        variantKey: Joi.string().optional()
    });
}
exports.productPriceExternalDiscountSetMessageType = productPriceExternalDiscountSetMessageType;
function productPublishedMessageType() {
    return Joi.object().unknown().keys({
        productProjection: Product_types_1.productProjectionType().required(),
        scope: Cart_types_7.productPublishScopeType().required(),
        resource: Common_types_1.referenceType().required(),
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productPublishedMessageType = productPublishedMessageType;
function productRevertedStagedChangesMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productRevertedStagedChangesMessageType = productRevertedStagedChangesMessageType;
function productSlugChangedMessageType() {
    return Joi.object().unknown().keys({
        slug: Common_types_2.localizedStringType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productSlugChangedMessageType = productSlugChangedMessageType;
function productStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productStateTransitionMessageType = productStateTransitionMessageType;
function productUnpublishedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productUnpublishedMessageType = productUnpublishedMessageType;
function productVariantDeletedMessageType() {
    return Joi.object().unknown().keys({
        variant: Product_types_2.productVariantType().required(),
        resource: Common_types_1.referenceType().required(),
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.productVariantDeletedMessageType = productVariantDeletedMessageType;
function reviewCreatedMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        review: Review_types_1.reviewType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.reviewCreatedMessageType = reviewCreatedMessageType;
function reviewRatingSetMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        includedInStatistics: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        target: Common_types_1.referenceType().optional(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
        newRating: Joi.number().optional(),
        oldRating: Joi.number().optional()
    });
}
exports.reviewRatingSetMessageType = reviewRatingSetMessageType;
function reviewStateTransitionMessageType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        target: Common_types_1.referenceType().required(),
        newState: State_types_1.stateReferenceType().required(),
        oldState: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        newIncludedInStatistics: Joi.boolean().required(),
        oldIncludedInStatistics: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        type: Joi.string().required(),
        resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
    });
}
exports.reviewStateTransitionMessageType = reviewStateTransitionMessageType;
function userProvidedIdentifiersType() {
    return Joi.object().unknown().keys({
        slug: Common_types_2.localizedStringType().optional(),
        customerNumber: Joi.string().optional(),
        externalId: Joi.string().optional(),
        key: Joi.string().optional(),
        orderNumber: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.userProvidedIdentifiersType = userProvidedIdentifiersType;
function categoryCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        category: Category_types_1.categoryType().required(),
        type: Joi.string().required().only('CategoryCreated')
    });
}
exports.categoryCreatedMessagePayloadType = categoryCreatedMessagePayloadType;
function categorySlugChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        slug: Common_types_2.localizedStringType().required(),
        type: Joi.string().required().only('CategorySlugChanged')
    });
}
exports.categorySlugChangedMessagePayloadType = categorySlugChangedMessagePayloadType;
function customLineItemStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        fromState: State_types_1.stateReferenceType().required(),
        toState: State_types_1.stateReferenceType().required(),
        transitionDate: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        customLineItemId: Joi.string().required(),
        type: Joi.string().required().only('CustomLineItemStateTransition')
    });
}
exports.customLineItemStateTransitionMessagePayloadType = customLineItemStateTransitionMessagePayloadType;
function customerAddressAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        type: Joi.string().required().only('CustomerAddressAdded')
    });
}
exports.customerAddressAddedMessagePayloadType = customerAddressAddedMessagePayloadType;
function customerAddressChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        type: Joi.string().required().only('CustomerAddressChanged')
    });
}
exports.customerAddressChangedMessagePayloadType = customerAddressChangedMessagePayloadType;
function customerAddressRemovedMessagePayloadType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        type: Joi.string().required().only('CustomerAddressRemoved')
    });
}
exports.customerAddressRemovedMessagePayloadType = customerAddressRemovedMessagePayloadType;
function customerCompanyNameSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        companyName: Joi.string().required(),
        type: Joi.string().required().only('CustomerCompanyNameSet')
    });
}
exports.customerCompanyNameSetMessagePayloadType = customerCompanyNameSetMessagePayloadType;
function customerCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        customer: Customer_types_1.customerType().required(),
        type: Joi.string().required().only('CustomerCreated')
    });
}
exports.customerCreatedMessagePayloadType = customerCreatedMessagePayloadType;
function customerDateOfBirthSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        dateOfBirth: Joi.date().iso().required(),
        type: Joi.string().required().only('CustomerDateOfBirthSet')
    });
}
exports.customerDateOfBirthSetMessagePayloadType = customerDateOfBirthSetMessagePayloadType;
function customerEmailChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        email: Joi.string().required(),
        type: Joi.string().required().only('CustomerEmailChanged')
    });
}
exports.customerEmailChangedMessagePayloadType = customerEmailChangedMessagePayloadType;
function customerEmailVerifiedMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('CustomerEmailVerified')
    });
}
exports.customerEmailVerifiedMessagePayloadType = customerEmailVerifiedMessagePayloadType;
function customerGroupSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().required(),
        type: Joi.string().required().only('CustomerGroupSet')
    });
}
exports.customerGroupSetMessagePayloadType = customerGroupSetMessagePayloadType;
function deliveryAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        type: Joi.string().required().only('DeliveryAdded')
    });
}
exports.deliveryAddedMessagePayloadType = deliveryAddedMessagePayloadType;
function deliveryAddressSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        deliveryId: Joi.string().required(),
        type: Joi.string().required().only('DeliveryAddressSet'),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional()
    });
}
exports.deliveryAddressSetMessagePayloadType = deliveryAddressSetMessagePayloadType;
function deliveryItemsUpdatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        oldItems: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        deliveryId: Joi.string().required(),
        type: Joi.string().required().only('DeliveryItemsUpdated')
    });
}
exports.deliveryItemsUpdatedMessagePayloadType = deliveryItemsUpdatedMessagePayloadType;
function deliveryRemovedMessagePayloadType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        type: Joi.string().required().only('DeliveryRemoved')
    });
}
exports.deliveryRemovedMessagePayloadType = deliveryRemovedMessagePayloadType;
function inventoryEntryDeletedMessagePayloadType() {
    return Joi.object().unknown().keys({
        supplyChannel: Channel_types_1.channelReferenceType().required(),
        sku: Joi.string().required(),
        type: Joi.string().required().only('InventoryEntryDeleted')
    });
}
exports.inventoryEntryDeletedMessagePayloadType = inventoryEntryDeletedMessagePayloadType;
function lineItemStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        fromState: State_types_1.stateReferenceType().required(),
        toState: State_types_1.stateReferenceType().required(),
        transitionDate: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        lineItemId: Joi.string().required(),
        type: Joi.string().required().only('LineItemStateTransition')
    });
}
exports.lineItemStateTransitionMessagePayloadType = lineItemStateTransitionMessagePayloadType;
function messagePayloadType() {
    return Joi.alternatives([orderBillingAddressSetMessagePayloadType(), orderCreatedMessagePayloadType(), orderCustomLineItemDiscountSetMessagePayloadType(), orderCustomerEmailSetMessagePayloadType(), orderCustomerSetMessagePayloadType(), orderDeletedMessagePayloadType(), orderDiscountCodeAddedMessagePayloadType(), orderDiscountCodeRemovedMessagePayloadType(), orderDiscountCodeStateSetMessagePayloadType(), orderEditAppliedMessagePayloadType(), orderImportedMessagePayloadType(), orderLineItemAddedMessagePayloadType(), orderLineItemDiscountSetMessagePayloadType(), orderPaymentStateChangedMessagePayloadType(), orderReturnInfoAddedMessagePayloadType(), orderReturnShipmentStateChangedMessagePayloadType(), orderShipmentStateChangedMessagePayloadType(), orderShippingAddressSetMessagePayloadType(), orderShippingInfoSetMessagePayloadType(), orderShippingRateInputSetMessagePayloadType(), orderStateChangedMessagePayloadType(), orderStateTransitionMessagePayloadType(), parcelAddedToDeliveryMessagePayloadType(), parcelItemsUpdatedMessagePayloadType(), parcelMeasurementsUpdatedMessagePayloadType(), parcelRemovedFromDeliveryMessagePayloadType(), parcelTrackingDataUpdatedMessagePayloadType(), paymentCreatedMessagePayloadType(), paymentInteractionAddedMessagePayloadType(), paymentStatusInterfaceCodeSetMessagePayloadType(), paymentStatusStateTransitionMessagePayloadType(), paymentTransactionAddedMessagePayloadType(), paymentTransactionStateChangedMessagePayloadType(), productCreatedMessagePayloadType(), productDeletedMessagePayloadType(), productImageAddedMessagePayloadType(), productPriceDiscountsSetMessagePayloadType(), productPriceExternalDiscountSetMessagePayloadType(), productPublishedMessagePayloadType(), productRevertedStagedChangesMessagePayloadType(), productSlugChangedMessagePayloadType(), productStateTransitionMessagePayloadType(), productUnpublishedMessagePayloadType(), productVariantDeletedMessagePayloadType(), reviewCreatedMessagePayloadType(), reviewRatingSetMessagePayloadType(), reviewStateTransitionMessagePayloadType(), categoryCreatedMessagePayloadType(), categorySlugChangedMessagePayloadType(), customLineItemStateTransitionMessagePayloadType(), customerAddressAddedMessagePayloadType(), customerAddressChangedMessagePayloadType(), customerAddressRemovedMessagePayloadType(), customerCompanyNameSetMessagePayloadType(), customerCreatedMessagePayloadType(), customerDateOfBirthSetMessagePayloadType(), customerEmailChangedMessagePayloadType(), customerEmailVerifiedMessagePayloadType(), customerGroupSetMessagePayloadType(), deliveryAddedMessagePayloadType(), deliveryAddressSetMessagePayloadType(), deliveryItemsUpdatedMessagePayloadType(), deliveryRemovedMessagePayloadType(), inventoryEntryDeletedMessagePayloadType(), lineItemStateTransitionMessagePayloadType()]);
}
exports.messagePayloadType = messagePayloadType;
function orderBillingAddressSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderBillingAddressSet'),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional()
    });
}
exports.orderBillingAddressSetMessagePayloadType = orderBillingAddressSetMessagePayloadType;
function orderCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        type: Joi.string().required().only('OrderCreated')
    });
}
exports.orderCreatedMessagePayloadType = orderCreatedMessagePayloadType;
function orderCustomLineItemDiscountSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(Cart_types_2.discountedLineItemPriceForQuantityType()).required(),
        customLineItemId: Joi.string().required(),
        type: Joi.string().required().only('OrderCustomLineItemDiscountSet'),
        taxedPrice: Cart_types_1.taxedItemPriceType().optional()
    });
}
exports.orderCustomLineItemDiscountSetMessagePayloadType = orderCustomLineItemDiscountSetMessagePayloadType;
function orderCustomerEmailSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderCustomerEmailSet'),
        email: Joi.string().optional(),
        oldEmail: Joi.string().optional()
    });
}
exports.orderCustomerEmailSetMessagePayloadType = orderCustomerEmailSetMessagePayloadType;
function orderCustomerSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderCustomerSet'),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        oldCustomerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        customer: Customer_types_2.customerReferenceType().optional(),
        oldCustomer: Customer_types_2.customerReferenceType().optional()
    });
}
exports.orderCustomerSetMessagePayloadType = orderCustomerSetMessagePayloadType;
function orderDeletedMessagePayloadType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        type: Joi.string().required().only('OrderDeleted')
    });
}
exports.orderDeletedMessagePayloadType = orderDeletedMessagePayloadType;
function orderDiscountCodeAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        type: Joi.string().required().only('OrderDiscountCodeAdded')
    });
}
exports.orderDiscountCodeAddedMessagePayloadType = orderDiscountCodeAddedMessagePayloadType;
function orderDiscountCodeRemovedMessagePayloadType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        type: Joi.string().required().only('OrderDiscountCodeRemoved')
    });
}
exports.orderDiscountCodeRemovedMessagePayloadType = orderDiscountCodeRemovedMessagePayloadType;
function orderDiscountCodeStateSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        state: Cart_types_3.discountCodeStateType().required(),
        type: Joi.string().required().only('OrderDiscountCodeStateSet'),
        oldState: Cart_types_3.discountCodeStateType().optional()
    });
}
exports.orderDiscountCodeStateSetMessagePayloadType = orderDiscountCodeStateSetMessagePayloadType;
function orderEditAppliedMessagePayloadType() {
    return Joi.object().unknown().keys({
        result: OrderEdit_types_1.orderEditAppliedType().required(),
        edit: OrderEdit_types_2.orderEditReferenceType().required(),
        type: Joi.string().required().only('OrderEditApplied')
    });
}
exports.orderEditAppliedMessagePayloadType = orderEditAppliedMessagePayloadType;
function orderImportedMessagePayloadType() {
    return Joi.object().unknown().keys({
        order: Order_types_3.orderType().required(),
        type: Joi.string().required().only('OrderImported')
    });
}
exports.orderImportedMessagePayloadType = orderImportedMessagePayloadType;
function orderLineItemAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        lineItem: Cart_types_4.lineItemType().required(),
        addedQuantity: Joi.number().required(),
        type: Joi.string().required().only('OrderLineItemAdded')
    });
}
exports.orderLineItemAddedMessagePayloadType = orderLineItemAddedMessagePayloadType;
function orderLineItemDiscountSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(Cart_types_2.discountedLineItemPriceForQuantityType()).required(),
        totalPrice: Common_types_4.moneyType().required(),
        lineItemId: Joi.string().required(),
        type: Joi.string().required().only('OrderLineItemDiscountSet'),
        taxedPrice: Cart_types_1.taxedItemPriceType().optional()
    });
}
exports.orderLineItemDiscountSetMessagePayloadType = orderLineItemDiscountSetMessagePayloadType;
function orderPaymentStateChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        oldPaymentState: Order_types_4.paymentStateType().required(),
        paymentState: Order_types_4.paymentStateType().required(),
        type: Joi.string().required().only('OrderPaymentStateChanged')
    });
}
exports.orderPaymentStateChangedMessagePayloadType = orderPaymentStateChangedMessagePayloadType;
function orderReturnInfoAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        returnInfo: Order_types_5.returnInfoType().required(),
        type: Joi.string().required().only('ReturnInfoAdded')
    });
}
exports.orderReturnInfoAddedMessagePayloadType = orderReturnInfoAddedMessagePayloadType;
function orderReturnShipmentStateChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        returnShipmentState: Order_types_6.returnShipmentStateType().required(),
        returnItemId: Joi.string().required(),
        type: Joi.string().required().only('OrderReturnShipmentStateChanged')
    });
}
exports.orderReturnShipmentStateChangedMessagePayloadType = orderReturnShipmentStateChangedMessagePayloadType;
function orderShipmentStateChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        oldShipmentState: Order_types_7.shipmentStateType().required(),
        shipmentState: Order_types_7.shipmentStateType().required(),
        type: Joi.string().required().only('OrderShipmentStateChanged')
    });
}
exports.orderShipmentStateChangedMessagePayloadType = orderShipmentStateChangedMessagePayloadType;
function orderShippingAddressSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderShippingAddressSet'),
        address: Common_types_3.addressType().optional(),
        oldAddress: Common_types_3.addressType().optional()
    });
}
exports.orderShippingAddressSetMessagePayloadType = orderShippingAddressSetMessagePayloadType;
function orderShippingInfoSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderShippingInfoSet'),
        oldShippingInfo: Cart_types_5.shippingInfoType().optional(),
        shippingInfo: Cart_types_5.shippingInfoType().optional()
    });
}
exports.orderShippingInfoSetMessagePayloadType = orderShippingInfoSetMessagePayloadType;
function orderShippingRateInputSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('OrderShippingRateInputSet'),
        oldShippingRateInput: Cart_types_6.shippingRateInputType().optional(),
        shippingRateInput: Cart_types_6.shippingRateInputType().optional()
    });
}
exports.orderShippingRateInputSetMessagePayloadType = orderShippingRateInputSetMessagePayloadType;
function orderStateChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        oldOrderState: Order_types_8.orderStateType().required(),
        orderState: Order_types_8.orderStateType().required(),
        type: Joi.string().required().only('OrderStateChanged')
    });
}
exports.orderStateChangedMessagePayloadType = orderStateChangedMessagePayloadType;
function orderStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        type: Joi.string().required().only('OrderStateTransition')
    });
}
exports.orderStateTransitionMessagePayloadType = orderStateTransitionMessagePayloadType;
function parcelAddedToDeliveryMessagePayloadType() {
    return Joi.object().unknown().keys({
        delivery: Order_types_1.deliveryType().required(),
        parcel: Order_types_9.parcelType().required(),
        type: Joi.string().required().only('ParcelAddedToDelivery')
    });
}
exports.parcelAddedToDeliveryMessagePayloadType = parcelAddedToDeliveryMessagePayloadType;
function parcelItemsUpdatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        oldItems: Joi.array().items(Order_types_2.deliveryItemType()).required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required().only('ParcelItemsUpdated'),
        deliveryId: Joi.string().optional()
    });
}
exports.parcelItemsUpdatedMessagePayloadType = parcelItemsUpdatedMessagePayloadType;
function parcelMeasurementsUpdatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        deliveryId: Joi.string().required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required().only('ParcelMeasurementsUpdated'),
        measurements: Order_types_10.parcelMeasurementsType().optional()
    });
}
exports.parcelMeasurementsUpdatedMessagePayloadType = parcelMeasurementsUpdatedMessagePayloadType;
function parcelRemovedFromDeliveryMessagePayloadType() {
    return Joi.object().unknown().keys({
        parcel: Order_types_9.parcelType().required(),
        deliveryId: Joi.string().required(),
        type: Joi.string().required().only('ParcelRemovedFromDelivery')
    });
}
exports.parcelRemovedFromDeliveryMessagePayloadType = parcelRemovedFromDeliveryMessagePayloadType;
function parcelTrackingDataUpdatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        deliveryId: Joi.string().required(),
        parcelId: Joi.string().required(),
        type: Joi.string().required().only('ParcelTrackingDataUpdated'),
        trackingData: Order_types_11.trackingDataType().optional()
    });
}
exports.parcelTrackingDataUpdatedMessagePayloadType = parcelTrackingDataUpdatedMessagePayloadType;
function paymentCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentType().required(),
        type: Joi.string().required().only('PaymentCreated')
    });
}
exports.paymentCreatedMessagePayloadType = paymentCreatedMessagePayloadType;
function paymentInteractionAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        interaction: Type_types_1.customFieldsType().required(),
        type: Joi.string().required().only('PaymentInteractionAdded')
    });
}
exports.paymentInteractionAddedMessagePayloadType = paymentInteractionAddedMessagePayloadType;
function paymentStatusInterfaceCodeSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        interfaceCode: Joi.string().required(),
        paymentId: Joi.string().required(),
        type: Joi.string().required().only('PaymentStatusInterfaceCodeSet')
    });
}
exports.paymentStatusInterfaceCodeSetMessagePayloadType = paymentStatusInterfaceCodeSetMessagePayloadType;
function paymentStatusStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        type: Joi.string().required().only('PaymentStatusStateTransition')
    });
}
exports.paymentStatusStateTransitionMessagePayloadType = paymentStatusStateTransitionMessagePayloadType;
function paymentTransactionAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        transaction: Payment_types_2.transactionType().required(),
        type: Joi.string().required().only('PaymentTransactionAdded')
    });
}
exports.paymentTransactionAddedMessagePayloadType = paymentTransactionAddedMessagePayloadType;
function paymentTransactionStateChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        state: Payment_types_3.transactionStateType().required(),
        transactionId: Joi.string().required(),
        type: Joi.string().required().only('PaymentTransactionStateChanged')
    });
}
exports.paymentTransactionStateChangedMessagePayloadType = paymentTransactionStateChangedMessagePayloadType;
function productCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        productProjection: Product_types_1.productProjectionType().required(),
        type: Joi.string().required().only('ProductCreated')
    });
}
exports.productCreatedMessagePayloadType = productCreatedMessagePayloadType;
function productDeletedMessagePayloadType() {
    return Joi.object().unknown().keys({
        currentProjection: Product_types_1.productProjectionType().required(),
        removedImageUrls: Joi.array().items(Joi.string()).required(),
        type: Joi.string().required().only('ProductDeleted')
    });
}
exports.productDeletedMessagePayloadType = productDeletedMessagePayloadType;
function productImageAddedMessagePayloadType() {
    return Joi.object().unknown().keys({
        image: Common_types_5.imageType().required(),
        staged: Joi.boolean().required(),
        variantId: Joi.number().required(),
        type: Joi.string().required().only('ProductImageAdded')
    });
}
exports.productImageAddedMessagePayloadType = productImageAddedMessagePayloadType;
function productPriceDiscountsSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        updatedPrices: Joi.array().items(productPriceDiscountsSetUpdatedPriceType()).required(),
        type: Joi.string().required().only('ProductPriceDiscountsSet')
    });
}
exports.productPriceDiscountsSetMessagePayloadType = productPriceDiscountsSetMessagePayloadType;
function productPriceExternalDiscountSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        staged: Joi.boolean().required(),
        variantId: Joi.number().required(),
        priceId: Joi.string().required(),
        type: Joi.string().required().only('ProductPriceExternalDiscountSet'),
        discounted: Common_types_6.discountedPriceType().optional(),
        sku: Joi.string().optional(),
        variantKey: Joi.string().optional()
    });
}
exports.productPriceExternalDiscountSetMessagePayloadType = productPriceExternalDiscountSetMessagePayloadType;
function productPublishedMessagePayloadType() {
    return Joi.object().unknown().keys({
        productProjection: Product_types_1.productProjectionType().required(),
        scope: Cart_types_7.productPublishScopeType().required(),
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        type: Joi.string().required().only('ProductPublished')
    });
}
exports.productPublishedMessagePayloadType = productPublishedMessagePayloadType;
function productRevertedStagedChangesMessagePayloadType() {
    return Joi.object().unknown().keys({
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        type: Joi.string().required().only('ProductRevertedStagedChanges')
    });
}
exports.productRevertedStagedChangesMessagePayloadType = productRevertedStagedChangesMessagePayloadType;
function productSlugChangedMessagePayloadType() {
    return Joi.object().unknown().keys({
        slug: Common_types_2.localizedStringType().required(),
        type: Joi.string().required().only('ProductSlugChanged')
    });
}
exports.productSlugChangedMessagePayloadType = productSlugChangedMessagePayloadType;
function productStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        state: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        type: Joi.string().required().only('ProductStateTransition')
    });
}
exports.productStateTransitionMessagePayloadType = productStateTransitionMessagePayloadType;
function productUnpublishedMessagePayloadType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('ProductUnpublished')
    });
}
exports.productUnpublishedMessagePayloadType = productUnpublishedMessagePayloadType;
function productVariantDeletedMessagePayloadType() {
    return Joi.object().unknown().keys({
        variant: Product_types_2.productVariantType().required(),
        removedImageUrls: Joi.array().items(Joi.any()).required(),
        type: Joi.string().required().only('ProductVariantDeleted')
    });
}
exports.productVariantDeletedMessagePayloadType = productVariantDeletedMessagePayloadType;
function reviewCreatedMessagePayloadType() {
    return Joi.object().unknown().keys({
        review: Review_types_1.reviewType().required(),
        type: Joi.string().required().only('ReviewCreated')
    });
}
exports.reviewCreatedMessagePayloadType = reviewCreatedMessagePayloadType;
function reviewRatingSetMessagePayloadType() {
    return Joi.object().unknown().keys({
        includedInStatistics: Joi.boolean().required(),
        type: Joi.string().required().only('ReviewRatingSet'),
        target: Common_types_1.referenceType().optional(),
        newRating: Joi.number().optional(),
        oldRating: Joi.number().optional()
    });
}
exports.reviewRatingSetMessagePayloadType = reviewRatingSetMessagePayloadType;
function reviewStateTransitionMessagePayloadType() {
    return Joi.object().unknown().keys({
        target: Common_types_1.referenceType().required(),
        newState: State_types_1.stateReferenceType().required(),
        oldState: State_types_1.stateReferenceType().required(),
        force: Joi.boolean().required(),
        newIncludedInStatistics: Joi.boolean().required(),
        oldIncludedInStatistics: Joi.boolean().required(),
        type: Joi.string().required().only('ReviewStateTransition')
    });
}
exports.reviewStateTransitionMessagePayloadType = reviewStateTransitionMessagePayloadType;
