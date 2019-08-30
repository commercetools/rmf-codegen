"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Order_types_1 = require("./Order-types");
const Type_types_1 = require("./Type-types");
const Order_types_2 = require("./Order-types");
const Type_types_2 = require("./Type-types");
const Error_types_1 = require("./Error-types");
const Message_types_1 = require("./Message-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
const Cart_types_1 = require("./Cart-types");
const Order_types_3 = require("./Order-types");
const Cart_types_2 = require("./Cart-types");
const Cart_types_3 = require("./Cart-types");
const Cart_types_4 = require("./Cart-types");
const Cart_types_5 = require("./Cart-types");
const Cart_types_6 = require("./Cart-types");
const Order_types_4 = require("./Order-types");
const Order_types_5 = require("./Order-types");
const Cart_types_7 = require("./Cart-types");
const Cart_types_8 = require("./Cart-types");
const Common_types_5 = require("./Common-types");
const State_types_1 = require("./State-types");
const Order_types_6 = require("./Order-types");
const Cart_types_9 = require("./Cart-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Cart_types_10 = require("./Cart-types");
const Store_types_1 = require("./Store-types");
const Order_types_7 = require("./Order-types");
const Cart_types_11 = require("./Cart-types");
const Cart_types_12 = require("./Cart-types");
const Order_types_8 = require("./Order-types");
const Type_types_3 = require("./Type-types");
const Cart_types_13 = require("./Cart-types");
const Common_types_6 = require("./Common-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const Order_types_9 = require("./Order-types");
const Order_types_10 = require("./Order-types");
const Cart_types_14 = require("./Cart-types");
const Cart_types_15 = require("./Cart-types");
const Channel_types_1 = require("./Channel-types");
const Order_types_11 = require("./Order-types");
const Order_types_12 = require("./Order-types");
const Payment_types_1 = require("./Payment-types");
const Order_types_13 = require("./Order-types");
const ShoppingList_types_1 = require("./ShoppingList-types");
const Order_types_14 = require("./Order-types");
const DiscountCode_types_1 = require("./DiscountCode-types");
const Type_types_4 = require("./Type-types");
const Cart_types_16 = require("./Cart-types");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const CustomerGroup_types_2 = require("./CustomerGroup-types");
const Cart_types_17 = require("./Cart-types");
const Order_types_15 = require("./Order-types");
const Order_types_16 = require("./Order-types");
const ShippingMethod_types_2 = require("./ShippingMethod-types");
const Cart_types_18 = require("./Cart-types");
const State_types_2 = require("./State-types");
function orderEditType() {
    return Joi.object().unknown().keys({
        stagedActions: Joi.array().items(Order_types_2.stagedOrderUpdateActionType()).required(),
        result: orderEditResultType().required(),
        resource: Order_types_1.orderReferenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        comment: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.orderEditType = orderEditType;
function orderEditAppliedType() {
    return Joi.object().unknown().keys({
        excerptAfterEdit: orderExcerptType().required(),
        excerptBeforeEdit: orderExcerptType().required(),
        appliedAt: Joi.date().iso().required(),
        type: Joi.string().required().only('Applied')
    });
}
exports.orderEditAppliedType = orderEditAppliedType;
function orderEditApplyType() {
    return Joi.object().unknown().keys({
        editVersion: Joi.number().required(),
        resourceVersion: Joi.number().required()
    });
}
exports.orderEditApplyType = orderEditApplyType;
function orderEditDraftType() {
    return Joi.object().unknown().keys({
        resource: Order_types_1.orderReferenceType().required(),
        stagedActions: Joi.array().items(Order_types_2.stagedOrderUpdateActionType()).optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        dryRun: Joi.boolean().optional(),
        comment: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.orderEditDraftType = orderEditDraftType;
function orderEditNotProcessedType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('NotProcessed')
    });
}
exports.orderEditNotProcessedType = orderEditNotProcessedType;
function orderEditPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(orderEditType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.orderEditPagedQueryResponseType = orderEditPagedQueryResponseType;
function orderEditPreviewFailureType() {
    return Joi.object().unknown().keys({
        errors: Joi.array().items(Error_types_1.errorObjectType()).required(),
        type: Joi.string().required().only('PreviewFailure')
    });
}
exports.orderEditPreviewFailureType = orderEditPreviewFailureType;
function orderEditPreviewSuccessType() {
    return Joi.object().unknown().keys({
        messagePayloads: Joi.array().items(Message_types_1.messagePayloadType()).required(),
        preview: stagedOrderType().required(),
        type: Joi.string().required().only('PreviewSuccess')
    });
}
exports.orderEditPreviewSuccessType = orderEditPreviewSuccessType;
function orderEditReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().required().only('order-edit'),
        id: Joi.string().required(),
        obj: orderEditType().optional()
    });
}
exports.orderEditReferenceType = orderEditReferenceType;
function orderEditResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().optional().only('order-edit'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.orderEditResourceIdentifierType = orderEditResourceIdentifierType;
function orderEditResultType() {
    return Joi.alternatives([orderEditAppliedType(), orderEditNotProcessedType(), orderEditPreviewFailureType(), orderEditPreviewSuccessType()]);
}
exports.orderEditResultType = orderEditResultType;
function orderEditUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(orderEditUpdateActionType()).required(),
        dryRun: Joi.boolean().required(),
        version: Joi.number().required()
    });
}
exports.orderEditUpdateType = orderEditUpdateType;
function orderEditUpdateActionType() {
    return Joi.alternatives([orderEditAddStagedActionActionType(), orderEditSetCommentActionType(), orderEditSetCustomFieldActionType(), orderEditSetCustomTypeActionType(), orderEditSetKeyActionType(), orderEditSetStagedActionsActionType()]);
}
exports.orderEditUpdateActionType = orderEditUpdateActionType;
function orderExcerptType() {
    return Joi.object().unknown().keys({
        totalPrice: Common_types_4.moneyType().required(),
        version: Joi.number().required(),
        taxedPrice: Cart_types_1.taxedPriceType().optional()
    });
}
exports.orderExcerptType = orderExcerptType;
function stagedOrderType() {
    return Joi.object().unknown().keys({
        customLineItems: Joi.array().items(Cart_types_8.customLineItemType()).required(),
        lineItems: Joi.array().items(Cart_types_7.lineItemType()).required(),
        syncInfo: Joi.array().items(Order_types_7.syncInfoType()).required(),
        origin: Cart_types_3.cartOriginType().required(),
        totalPrice: Common_types_4.moneyType().required(),
        orderState: Order_types_4.orderStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        lastMessageSequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        discountCodes: Joi.array().items(Cart_types_9.discountCodeInfoType()).optional(),
        itemShippingAddresses: Joi.array().items(Common_types_5.addressType()).optional(),
        returnInfo: Joi.array().items(Order_types_5.returnInfoType()).optional(),
        billingAddress: Common_types_5.addressType().optional(),
        shippingAddress: Common_types_5.addressType().optional(),
        cart: Cart_types_5.cartReferenceType().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        inventoryMode: Cart_types_6.inventoryModeType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        paymentInfo: Order_types_8.paymentInfoType().optional(),
        paymentState: Order_types_6.paymentStateType().optional(),
        taxRoundingMode: Cart_types_11.roundingModeType().optional(),
        shipmentState: Order_types_3.shipmentStateType().optional(),
        shippingInfo: Cart_types_4.shippingInfoType().optional(),
        shippingRateInput: Cart_types_2.shippingRateInputType().optional(),
        state: State_types_1.stateReferenceType().optional(),
        store: Store_types_1.storeKeyReferenceType().optional(),
        taxCalculationMode: Cart_types_10.taxCalculationModeType().optional(),
        taxMode: Cart_types_12.taxModeType().optional(),
        taxedPrice: Cart_types_1.taxedPriceType().optional(),
        completedAt: Joi.date().iso().optional(),
        anonymousId: Joi.string().optional(),
        country: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        customerId: Joi.string().optional(),
        locale: Joi.string().optional(),
        orderNumber: Joi.string().optional()
    });
}
exports.stagedOrderType = stagedOrderType;
function orderEditAddStagedActionActionType() {
    return Joi.object().unknown().keys({
        stagedAction: Order_types_2.stagedOrderUpdateActionType().required(),
        action: Joi.string().required().only('addStagedAction')
    });
}
exports.orderEditAddStagedActionActionType = orderEditAddStagedActionActionType;
function orderEditSetCommentActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setComment'),
        comment: Joi.string().optional()
    });
}
exports.orderEditSetCommentActionType = orderEditSetCommentActionType;
function orderEditSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.orderEditSetCustomFieldActionType = orderEditSetCustomFieldActionType;
function orderEditSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        type: Type_types_3.typeResourceIdentifierType().optional(),
        fields: Joi.any().optional()
    });
}
exports.orderEditSetCustomTypeActionType = orderEditSetCustomTypeActionType;
function orderEditSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.orderEditSetKeyActionType = orderEditSetKeyActionType;
function orderEditSetStagedActionsActionType() {
    return Joi.object().unknown().keys({
        stagedActions: Joi.array().items(Order_types_2.stagedOrderUpdateActionType()).required(),
        action: Joi.string().required().only('setStagedActions')
    });
}
exports.orderEditSetStagedActionsActionType = orderEditSetStagedActionsActionType;
function stagedOrderAddCustomLineItemActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_6.localizedStringType().required(),
        money: Common_types_4.moneyType().required(),
        action: Joi.string().required().only('addCustomLineItem'),
        slug: Joi.string().required(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        taxCategory: TaxCategory_types_1.taxCategoryResourceIdentifierType().optional(),
        quantity: Joi.number().optional()
    });
}
exports.stagedOrderAddCustomLineItemActionType = stagedOrderAddCustomLineItemActionType;
function stagedOrderAddDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addDelivery'),
        items: Joi.array().items(Order_types_9.deliveryItemType()).optional(),
        parcels: Joi.array().items(Order_types_10.parcelDraftType()).optional(),
        address: Common_types_5.addressType().optional()
    });
}
exports.stagedOrderAddDeliveryActionType = stagedOrderAddDeliveryActionType;
function stagedOrderAddDiscountCodeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addDiscountCode'),
        code: Joi.string().required()
    });
}
exports.stagedOrderAddDiscountCodeActionType = stagedOrderAddDiscountCodeActionType;
function stagedOrderAddItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_5.addressType().required(),
        action: Joi.string().required().only('addItemShippingAddress')
    });
}
exports.stagedOrderAddItemShippingAddressActionType = stagedOrderAddItemShippingAddressActionType;
function stagedOrderAddLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addLineItem'),
        distributionChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        externalTotalPrice: Cart_types_15.externalLineItemTotalPriceType().optional(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        shippingDetails: Cart_types_14.itemShippingDetailsDraftType().optional(),
        externalPrice: Common_types_4.moneyType().optional(),
        quantity: Joi.number().optional(),
        variantId: Joi.number().optional(),
        productId: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.stagedOrderAddLineItemActionType = stagedOrderAddLineItemActionType;
function stagedOrderAddParcelToDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addParcelToDelivery'),
        deliveryId: Joi.string().required(),
        items: Joi.array().items(Order_types_9.deliveryItemType()).optional(),
        measurements: Order_types_12.parcelMeasurementsType().optional(),
        trackingData: Order_types_11.trackingDataType().optional()
    });
}
exports.stagedOrderAddParcelToDeliveryActionType = stagedOrderAddParcelToDeliveryActionType;
function stagedOrderAddPaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('addPayment')
    });
}
exports.stagedOrderAddPaymentActionType = stagedOrderAddPaymentActionType;
function stagedOrderAddReturnInfoActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_13.returnItemDraftType()).required(),
        action: Joi.string().required().only('addReturnInfo'),
        returnDate: Joi.date().iso().optional(),
        returnTrackingId: Joi.string().optional()
    });
}
exports.stagedOrderAddReturnInfoActionType = stagedOrderAddReturnInfoActionType;
function stagedOrderAddShoppingListActionType() {
    return Joi.object().unknown().keys({
        shoppingList: ShoppingList_types_1.shoppingListResourceIdentifierType().required(),
        action: Joi.string().required().only('addShoppingList'),
        distributionChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional()
    });
}
exports.stagedOrderAddShoppingListActionType = stagedOrderAddShoppingListActionType;
function stagedOrderChangeCustomLineItemMoneyActionType() {
    return Joi.object().unknown().keys({
        money: Common_types_4.moneyType().required(),
        action: Joi.string().required().only('changeCustomLineItemMoney'),
        customLineItemId: Joi.string().required()
    });
}
exports.stagedOrderChangeCustomLineItemMoneyActionType = stagedOrderChangeCustomLineItemMoneyActionType;
function stagedOrderChangeCustomLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeCustomLineItemQuantity'),
        customLineItemId: Joi.string().required()
    });
}
exports.stagedOrderChangeCustomLineItemQuantityActionType = stagedOrderChangeCustomLineItemQuantityActionType;
function stagedOrderChangeLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeLineItemQuantity'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: Cart_types_15.externalLineItemTotalPriceType().optional(),
        externalPrice: Common_types_4.moneyType().optional()
    });
}
exports.stagedOrderChangeLineItemQuantityActionType = stagedOrderChangeLineItemQuantityActionType;
function stagedOrderChangeOrderStateActionType() {
    return Joi.object().unknown().keys({
        orderState: Order_types_4.orderStateType().required(),
        action: Joi.string().required().only('changeOrderState')
    });
}
exports.stagedOrderChangeOrderStateActionType = stagedOrderChangeOrderStateActionType;
function stagedOrderChangePaymentStateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changePaymentState'),
        paymentState: Order_types_6.paymentStateType().optional()
    });
}
exports.stagedOrderChangePaymentStateActionType = stagedOrderChangePaymentStateActionType;
function stagedOrderChangeShipmentStateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeShipmentState'),
        shipmentState: Order_types_3.shipmentStateType().optional()
    });
}
exports.stagedOrderChangeShipmentStateActionType = stagedOrderChangeShipmentStateActionType;
function stagedOrderChangeTaxCalculationModeActionType() {
    return Joi.object().unknown().keys({
        taxCalculationMode: Cart_types_10.taxCalculationModeType().required(),
        action: Joi.string().required().only('changeTaxCalculationMode')
    });
}
exports.stagedOrderChangeTaxCalculationModeActionType = stagedOrderChangeTaxCalculationModeActionType;
function stagedOrderChangeTaxModeActionType() {
    return Joi.object().unknown().keys({
        taxMode: Cart_types_12.taxModeType().required(),
        action: Joi.string().required().only('changeTaxMode')
    });
}
exports.stagedOrderChangeTaxModeActionType = stagedOrderChangeTaxModeActionType;
function stagedOrderChangeTaxRoundingModeActionType() {
    return Joi.object().unknown().keys({
        taxRoundingMode: Cart_types_11.roundingModeType().required(),
        action: Joi.string().required().only('changeTaxRoundingMode')
    });
}
exports.stagedOrderChangeTaxRoundingModeActionType = stagedOrderChangeTaxRoundingModeActionType;
function stagedOrderImportCustomLineItemStateActionType() {
    return Joi.object().unknown().keys({
        state: Joi.array().items(Order_types_14.itemStateType()).required(),
        action: Joi.string().required().only('importCustomLineItemState'),
        customLineItemId: Joi.string().required()
    });
}
exports.stagedOrderImportCustomLineItemStateActionType = stagedOrderImportCustomLineItemStateActionType;
function stagedOrderImportLineItemStateActionType() {
    return Joi.object().unknown().keys({
        state: Joi.array().items(Order_types_14.itemStateType()).required(),
        action: Joi.string().required().only('importLineItemState'),
        lineItemId: Joi.string().required()
    });
}
exports.stagedOrderImportLineItemStateActionType = stagedOrderImportLineItemStateActionType;
function stagedOrderRemoveCustomLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeCustomLineItem'),
        customLineItemId: Joi.string().required()
    });
}
exports.stagedOrderRemoveCustomLineItemActionType = stagedOrderRemoveCustomLineItemActionType;
function stagedOrderRemoveDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeDelivery'),
        deliveryId: Joi.string().required()
    });
}
exports.stagedOrderRemoveDeliveryActionType = stagedOrderRemoveDeliveryActionType;
function stagedOrderRemoveDiscountCodeActionType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        action: Joi.string().required().only('removeDiscountCode')
    });
}
exports.stagedOrderRemoveDiscountCodeActionType = stagedOrderRemoveDiscountCodeActionType;
function stagedOrderRemoveItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeItemShippingAddress'),
        addressKey: Joi.string().required()
    });
}
exports.stagedOrderRemoveItemShippingAddressActionType = stagedOrderRemoveItemShippingAddressActionType;
function stagedOrderRemoveLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeLineItem'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: Cart_types_15.externalLineItemTotalPriceType().optional(),
        shippingDetailsToRemove: Cart_types_14.itemShippingDetailsDraftType().optional(),
        externalPrice: Common_types_4.moneyType().optional(),
        quantity: Joi.number().optional()
    });
}
exports.stagedOrderRemoveLineItemActionType = stagedOrderRemoveLineItemActionType;
function stagedOrderRemoveParcelFromDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeParcelFromDelivery'),
        parcelId: Joi.string().required()
    });
}
exports.stagedOrderRemoveParcelFromDeliveryActionType = stagedOrderRemoveParcelFromDeliveryActionType;
function stagedOrderRemovePaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('removePayment')
    });
}
exports.stagedOrderRemovePaymentActionType = stagedOrderRemovePaymentActionType;
function stagedOrderSetBillingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setBillingAddress'),
        address: Common_types_5.addressType().optional()
    });
}
exports.stagedOrderSetBillingAddressActionType = stagedOrderSetBillingAddressActionType;
function stagedOrderSetCountryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCountry'),
        country: Joi.string().optional()
    });
}
exports.stagedOrderSetCountryActionType = stagedOrderSetCountryActionType;
function stagedOrderSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.stagedOrderSetCustomFieldActionType = stagedOrderSetCustomFieldActionType;
function stagedOrderSetCustomLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomField'),
        customLineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.stagedOrderSetCustomLineItemCustomFieldActionType = stagedOrderSetCustomLineItemCustomFieldActionType;
function stagedOrderSetCustomLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomType'),
        customLineItemId: Joi.string().required(),
        fields: Type_types_4.fieldContainerType().optional(),
        type: Type_types_3.typeResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetCustomLineItemCustomTypeActionType = stagedOrderSetCustomLineItemCustomTypeActionType;
function stagedOrderSetCustomLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemShippingDetails'),
        customLineItemId: Joi.string().required(),
        shippingDetails: Cart_types_14.itemShippingDetailsDraftType().optional()
    });
}
exports.stagedOrderSetCustomLineItemShippingDetailsActionType = stagedOrderSetCustomLineItemShippingDetailsActionType;
function stagedOrderSetCustomLineItemTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemTaxAmount'),
        customLineItemId: Joi.string().required(),
        externalTaxAmount: Cart_types_16.externalTaxAmountDraftType().optional()
    });
}
exports.stagedOrderSetCustomLineItemTaxAmountActionType = stagedOrderSetCustomLineItemTaxAmountActionType;
function stagedOrderSetCustomLineItemTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemTaxRate'),
        customLineItemId: Joi.string().required(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional()
    });
}
exports.stagedOrderSetCustomLineItemTaxRateActionType = stagedOrderSetCustomLineItemTaxRateActionType;
function stagedOrderSetCustomShippingMethodActionType() {
    return Joi.object().unknown().keys({
        shippingRate: ShippingMethod_types_1.shippingRateDraftType().required(),
        action: Joi.string().required().only('setCustomShippingMethod'),
        shippingMethodName: Joi.string().required(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        taxCategory: TaxCategory_types_1.taxCategoryResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetCustomShippingMethodActionType = stagedOrderSetCustomShippingMethodActionType;
function stagedOrderSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_4.fieldContainerType().optional(),
        type: Type_types_3.typeResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetCustomTypeActionType = stagedOrderSetCustomTypeActionType;
function stagedOrderSetCustomerEmailActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerEmail'),
        email: Joi.string().optional()
    });
}
exports.stagedOrderSetCustomerEmailActionType = stagedOrderSetCustomerEmailActionType;
function stagedOrderSetCustomerGroupActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerGroup'),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetCustomerGroupActionType = stagedOrderSetCustomerGroupActionType;
function stagedOrderSetCustomerIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerId'),
        customerId: Joi.string().optional()
    });
}
exports.stagedOrderSetCustomerIdActionType = stagedOrderSetCustomerIdActionType;
function stagedOrderSetDeliveryAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDeliveryAddress'),
        deliveryId: Joi.string().required(),
        address: Common_types_5.addressType().optional()
    });
}
exports.stagedOrderSetDeliveryAddressActionType = stagedOrderSetDeliveryAddressActionType;
function stagedOrderSetDeliveryItemsActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_9.deliveryItemType()).required(),
        action: Joi.string().required().only('setDeliveryItems'),
        deliveryId: Joi.string().required()
    });
}
exports.stagedOrderSetDeliveryItemsActionType = stagedOrderSetDeliveryItemsActionType;
function stagedOrderSetLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomField'),
        lineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.stagedOrderSetLineItemCustomFieldActionType = stagedOrderSetLineItemCustomFieldActionType;
function stagedOrderSetLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomType'),
        lineItemId: Joi.string().required(),
        fields: Type_types_4.fieldContainerType().optional(),
        type: Type_types_3.typeResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetLineItemCustomTypeActionType = stagedOrderSetLineItemCustomTypeActionType;
function stagedOrderSetLineItemPriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemPrice'),
        lineItemId: Joi.string().required(),
        externalPrice: Common_types_4.moneyType().optional()
    });
}
exports.stagedOrderSetLineItemPriceActionType = stagedOrderSetLineItemPriceActionType;
function stagedOrderSetLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemShippingDetails'),
        lineItemId: Joi.string().required(),
        shippingDetails: Cart_types_14.itemShippingDetailsDraftType().optional()
    });
}
exports.stagedOrderSetLineItemShippingDetailsActionType = stagedOrderSetLineItemShippingDetailsActionType;
function stagedOrderSetLineItemTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTaxAmount'),
        lineItemId: Joi.string().required(),
        externalTaxAmount: Cart_types_16.externalTaxAmountDraftType().optional()
    });
}
exports.stagedOrderSetLineItemTaxAmountActionType = stagedOrderSetLineItemTaxAmountActionType;
function stagedOrderSetLineItemTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTaxRate'),
        lineItemId: Joi.string().required(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional()
    });
}
exports.stagedOrderSetLineItemTaxRateActionType = stagedOrderSetLineItemTaxRateActionType;
function stagedOrderSetLineItemTotalPriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTotalPrice'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: Cart_types_15.externalLineItemTotalPriceType().optional()
    });
}
exports.stagedOrderSetLineItemTotalPriceActionType = stagedOrderSetLineItemTotalPriceActionType;
function stagedOrderSetLocaleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLocale'),
        locale: Joi.string().optional()
    });
}
exports.stagedOrderSetLocaleActionType = stagedOrderSetLocaleActionType;
function stagedOrderSetOrderNumberActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setOrderNumber'),
        orderNumber: Joi.string().optional()
    });
}
exports.stagedOrderSetOrderNumberActionType = stagedOrderSetOrderNumberActionType;
function stagedOrderSetOrderTotalTaxActionType() {
    return Joi.object().unknown().keys({
        externalTotalGross: Common_types_4.moneyType().required(),
        action: Joi.string().required().only('setOrderTotalTax'),
        externalTaxPortions: Joi.array().items(Cart_types_17.taxPortionType()).optional()
    });
}
exports.stagedOrderSetOrderTotalTaxActionType = stagedOrderSetOrderTotalTaxActionType;
function stagedOrderSetParcelItemsActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(Order_types_9.deliveryItemType()).required(),
        action: Joi.string().required().only('setParcelItems'),
        parcelId: Joi.string().required()
    });
}
exports.stagedOrderSetParcelItemsActionType = stagedOrderSetParcelItemsActionType;
function stagedOrderSetParcelMeasurementsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setParcelMeasurements'),
        parcelId: Joi.string().required(),
        measurements: Order_types_12.parcelMeasurementsType().optional()
    });
}
exports.stagedOrderSetParcelMeasurementsActionType = stagedOrderSetParcelMeasurementsActionType;
function stagedOrderSetParcelTrackingDataActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setParcelTrackingData'),
        parcelId: Joi.string().required(),
        trackingData: Order_types_11.trackingDataType().optional()
    });
}
exports.stagedOrderSetParcelTrackingDataActionType = stagedOrderSetParcelTrackingDataActionType;
function stagedOrderSetReturnPaymentStateActionType() {
    return Joi.object().unknown().keys({
        paymentState: Order_types_15.returnPaymentStateType().required(),
        action: Joi.string().required().only('setReturnPaymentState'),
        returnItemId: Joi.string().required()
    });
}
exports.stagedOrderSetReturnPaymentStateActionType = stagedOrderSetReturnPaymentStateActionType;
function stagedOrderSetReturnShipmentStateActionType() {
    return Joi.object().unknown().keys({
        shipmentState: Order_types_16.returnShipmentStateType().required(),
        action: Joi.string().required().only('setReturnShipmentState'),
        returnItemId: Joi.string().required()
    });
}
exports.stagedOrderSetReturnShipmentStateActionType = stagedOrderSetReturnShipmentStateActionType;
function stagedOrderSetShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingAddress'),
        address: Common_types_5.addressType().optional()
    });
}
exports.stagedOrderSetShippingAddressActionType = stagedOrderSetShippingAddressActionType;
function stagedOrderSetShippingAddressAndCustomShippingMethodActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_5.addressType().required(),
        shippingRate: ShippingMethod_types_1.shippingRateDraftType().required(),
        action: Joi.string().required().only('setShippingAddressAndCustomShippingMethod'),
        shippingMethodName: Joi.string().required(),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        taxCategory: TaxCategory_types_1.taxCategoryResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetShippingAddressAndCustomShippingMethodActionType = stagedOrderSetShippingAddressAndCustomShippingMethodActionType;
function stagedOrderSetShippingAddressAndShippingMethodActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_5.addressType().required(),
        action: Joi.string().required().only('setShippingAddressAndShippingMethod'),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        shippingMethod: ShippingMethod_types_2.shippingMethodResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetShippingAddressAndShippingMethodActionType = stagedOrderSetShippingAddressAndShippingMethodActionType;
function stagedOrderSetShippingMethodActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethod'),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional(),
        shippingMethod: ShippingMethod_types_2.shippingMethodResourceIdentifierType().optional()
    });
}
exports.stagedOrderSetShippingMethodActionType = stagedOrderSetShippingMethodActionType;
function stagedOrderSetShippingMethodTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethodTaxAmount'),
        externalTaxAmount: Cart_types_16.externalTaxAmountDraftType().optional()
    });
}
exports.stagedOrderSetShippingMethodTaxAmountActionType = stagedOrderSetShippingMethodTaxAmountActionType;
function stagedOrderSetShippingMethodTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethodTaxRate'),
        externalTaxRate: Cart_types_13.externalTaxRateDraftType().optional()
    });
}
exports.stagedOrderSetShippingMethodTaxRateActionType = stagedOrderSetShippingMethodTaxRateActionType;
function stagedOrderSetShippingRateInputActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingRateInput'),
        shippingRateInput: Cart_types_18.shippingRateInputDraftType().optional()
    });
}
exports.stagedOrderSetShippingRateInputActionType = stagedOrderSetShippingRateInputActionType;
function stagedOrderTransitionCustomLineItemStateActionType() {
    return Joi.object().unknown().keys({
        fromState: State_types_2.stateResourceIdentifierType().required(),
        toState: State_types_2.stateResourceIdentifierType().required(),
        quantity: Joi.number().required(),
        action: Joi.string().required().only('transitionCustomLineItemState'),
        customLineItemId: Joi.string().required(),
        actualTransitionDate: Joi.date().iso().optional()
    });
}
exports.stagedOrderTransitionCustomLineItemStateActionType = stagedOrderTransitionCustomLineItemStateActionType;
function stagedOrderTransitionLineItemStateActionType() {
    return Joi.object().unknown().keys({
        fromState: State_types_2.stateResourceIdentifierType().required(),
        toState: State_types_2.stateResourceIdentifierType().required(),
        quantity: Joi.number().required(),
        action: Joi.string().required().only('transitionLineItemState'),
        lineItemId: Joi.string().required(),
        actualTransitionDate: Joi.date().iso().optional()
    });
}
exports.stagedOrderTransitionLineItemStateActionType = stagedOrderTransitionLineItemStateActionType;
function stagedOrderTransitionStateActionType() {
    return Joi.object().unknown().keys({
        state: State_types_2.stateResourceIdentifierType().required(),
        action: Joi.string().required().only('transitionState'),
        force: Joi.boolean().optional()
    });
}
exports.stagedOrderTransitionStateActionType = stagedOrderTransitionStateActionType;
function stagedOrderUpdateItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_5.addressType().required(),
        action: Joi.string().required().only('updateItemShippingAddress')
    });
}
exports.stagedOrderUpdateItemShippingAddressActionType = stagedOrderUpdateItemShippingAddressActionType;
function stagedOrderUpdateSyncInfoActionType() {
    return Joi.object().unknown().keys({
        channel: Channel_types_1.channelResourceIdentifierType().required(),
        action: Joi.string().required().only('updateSyncInfo'),
        syncedAt: Joi.date().iso().optional(),
        externalId: Joi.string().optional()
    });
}
exports.stagedOrderUpdateSyncInfoActionType = stagedOrderUpdateSyncInfoActionType;
