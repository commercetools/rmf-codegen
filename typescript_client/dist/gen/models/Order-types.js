"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const OrderEdit_types_1 = require("./OrderEdit-types");
const OrderEdit_types_2 = require("./OrderEdit-types");
const OrderEdit_types_3 = require("./OrderEdit-types");
const OrderEdit_types_4 = require("./OrderEdit-types");
const OrderEdit_types_5 = require("./OrderEdit-types");
const OrderEdit_types_6 = require("./OrderEdit-types");
const OrderEdit_types_7 = require("./OrderEdit-types");
const OrderEdit_types_8 = require("./OrderEdit-types");
const OrderEdit_types_9 = require("./OrderEdit-types");
const OrderEdit_types_10 = require("./OrderEdit-types");
const OrderEdit_types_11 = require("./OrderEdit-types");
const OrderEdit_types_12 = require("./OrderEdit-types");
const OrderEdit_types_13 = require("./OrderEdit-types");
const OrderEdit_types_14 = require("./OrderEdit-types");
const OrderEdit_types_15 = require("./OrderEdit-types");
const OrderEdit_types_16 = require("./OrderEdit-types");
const OrderEdit_types_17 = require("./OrderEdit-types");
const OrderEdit_types_18 = require("./OrderEdit-types");
const OrderEdit_types_19 = require("./OrderEdit-types");
const OrderEdit_types_20 = require("./OrderEdit-types");
const OrderEdit_types_21 = require("./OrderEdit-types");
const OrderEdit_types_22 = require("./OrderEdit-types");
const OrderEdit_types_23 = require("./OrderEdit-types");
const OrderEdit_types_24 = require("./OrderEdit-types");
const OrderEdit_types_25 = require("./OrderEdit-types");
const OrderEdit_types_26 = require("./OrderEdit-types");
const OrderEdit_types_27 = require("./OrderEdit-types");
const OrderEdit_types_28 = require("./OrderEdit-types");
const OrderEdit_types_29 = require("./OrderEdit-types");
const OrderEdit_types_30 = require("./OrderEdit-types");
const OrderEdit_types_31 = require("./OrderEdit-types");
const OrderEdit_types_32 = require("./OrderEdit-types");
const OrderEdit_types_33 = require("./OrderEdit-types");
const OrderEdit_types_34 = require("./OrderEdit-types");
const OrderEdit_types_35 = require("./OrderEdit-types");
const OrderEdit_types_36 = require("./OrderEdit-types");
const OrderEdit_types_37 = require("./OrderEdit-types");
const OrderEdit_types_38 = require("./OrderEdit-types");
const OrderEdit_types_39 = require("./OrderEdit-types");
const OrderEdit_types_40 = require("./OrderEdit-types");
const OrderEdit_types_41 = require("./OrderEdit-types");
const OrderEdit_types_42 = require("./OrderEdit-types");
const OrderEdit_types_43 = require("./OrderEdit-types");
const OrderEdit_types_44 = require("./OrderEdit-types");
const OrderEdit_types_45 = require("./OrderEdit-types");
const OrderEdit_types_46 = require("./OrderEdit-types");
const OrderEdit_types_47 = require("./OrderEdit-types");
const OrderEdit_types_48 = require("./OrderEdit-types");
const OrderEdit_types_49 = require("./OrderEdit-types");
const OrderEdit_types_50 = require("./OrderEdit-types");
const OrderEdit_types_51 = require("./OrderEdit-types");
const OrderEdit_types_52 = require("./OrderEdit-types");
const OrderEdit_types_53 = require("./OrderEdit-types");
const OrderEdit_types_54 = require("./OrderEdit-types");
const OrderEdit_types_55 = require("./OrderEdit-types");
const OrderEdit_types_56 = require("./OrderEdit-types");
const OrderEdit_types_57 = require("./OrderEdit-types");
const OrderEdit_types_58 = require("./OrderEdit-types");
const OrderEdit_types_59 = require("./OrderEdit-types");
const OrderEdit_types_60 = require("./OrderEdit-types");
const OrderEdit_types_61 = require("./OrderEdit-types");
const OrderEdit_types_62 = require("./OrderEdit-types");
const OrderEdit_types_63 = require("./OrderEdit-types");
const OrderEdit_types_64 = require("./OrderEdit-types");
const OrderEdit_types_65 = require("./OrderEdit-types");
const OrderEdit_types_66 = require("./OrderEdit-types");
const OrderEdit_types_67 = require("./OrderEdit-types");
const OrderEdit_types_68 = require("./OrderEdit-types");
const OrderEdit_types_69 = require("./OrderEdit-types");
const Common_types_1 = require("./Common-types");
const Cart_types_1 = require("./Cart-types");
const Common_types_2 = require("./Common-types");
const State_types_1 = require("./State-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const Cart_types_2 = require("./Cart-types");
const Common_types_3 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Common_types_4 = require("./Common-types");
const Channel_types_1 = require("./Channel-types");
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const Cart_types_3 = require("./Cart-types");
const Cart_types_4 = require("./Cart-types");
const Cart_types_5 = require("./Cart-types");
const Cart_types_6 = require("./Cart-types");
const Cart_types_7 = require("./Cart-types");
const Cart_types_8 = require("./Cart-types");
const Cart_types_9 = require("./Cart-types");
const Cart_types_10 = require("./Cart-types");
const Cart_types_11 = require("./Cart-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Type_types_2 = require("./Type-types");
const Cart_types_12 = require("./Cart-types");
const Store_types_1 = require("./Store-types");
const Cart_types_13 = require("./Cart-types");
const Cart_types_14 = require("./Cart-types");
const CustomerGroup_types_2 = require("./CustomerGroup-types");
const Cart_types_15 = require("./Cart-types");
const Common_types_7 = require("./Common-types");
const Payment_types_1 = require("./Payment-types");
const Common_types_8 = require("./Common-types");
const Product_types_1 = require("./Product-types");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const Cart_types_16 = require("./Cart-types");
const ShippingMethod_types_2 = require("./ShippingMethod-types");
const TaxCategory_types_2 = require("./TaxCategory-types");
const Channel_types_2 = require("./Channel-types");
const Payment_types_2 = require("./Payment-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
const State_types_2 = require("./State-types");
function stagedOrderUpdateActionType() {
    return Joi.alternatives([OrderEdit_types_1.stagedOrderAddCustomLineItemActionType(), OrderEdit_types_2.stagedOrderAddDeliveryActionType(), OrderEdit_types_3.stagedOrderAddDiscountCodeActionType(), OrderEdit_types_4.stagedOrderAddItemShippingAddressActionType(), OrderEdit_types_5.stagedOrderAddLineItemActionType(), OrderEdit_types_6.stagedOrderAddParcelToDeliveryActionType(), OrderEdit_types_7.stagedOrderAddPaymentActionType(), OrderEdit_types_8.stagedOrderAddReturnInfoActionType(), OrderEdit_types_9.stagedOrderAddShoppingListActionType(), OrderEdit_types_10.stagedOrderChangeCustomLineItemMoneyActionType(), OrderEdit_types_11.stagedOrderChangeCustomLineItemQuantityActionType(), OrderEdit_types_12.stagedOrderChangeLineItemQuantityActionType(), OrderEdit_types_13.stagedOrderChangeOrderStateActionType(), OrderEdit_types_14.stagedOrderChangePaymentStateActionType(), OrderEdit_types_15.stagedOrderChangeShipmentStateActionType(), OrderEdit_types_16.stagedOrderChangeTaxCalculationModeActionType(), OrderEdit_types_17.stagedOrderChangeTaxModeActionType(), OrderEdit_types_18.stagedOrderChangeTaxRoundingModeActionType(), OrderEdit_types_19.stagedOrderImportCustomLineItemStateActionType(), OrderEdit_types_20.stagedOrderImportLineItemStateActionType(), OrderEdit_types_21.stagedOrderRemoveCustomLineItemActionType(), OrderEdit_types_22.stagedOrderRemoveDeliveryActionType(), OrderEdit_types_23.stagedOrderRemoveDiscountCodeActionType(), OrderEdit_types_24.stagedOrderRemoveItemShippingAddressActionType(), OrderEdit_types_25.stagedOrderRemoveLineItemActionType(), OrderEdit_types_26.stagedOrderRemoveParcelFromDeliveryActionType(), OrderEdit_types_27.stagedOrderRemovePaymentActionType(), OrderEdit_types_28.stagedOrderSetBillingAddressActionType(), OrderEdit_types_29.stagedOrderSetCountryActionType(), OrderEdit_types_30.stagedOrderSetCustomFieldActionType(), OrderEdit_types_31.stagedOrderSetCustomLineItemCustomFieldActionType(), OrderEdit_types_32.stagedOrderSetCustomLineItemCustomTypeActionType(), OrderEdit_types_33.stagedOrderSetCustomLineItemShippingDetailsActionType(), OrderEdit_types_34.stagedOrderSetCustomLineItemTaxAmountActionType(), OrderEdit_types_35.stagedOrderSetCustomLineItemTaxRateActionType(), OrderEdit_types_36.stagedOrderSetCustomShippingMethodActionType(), OrderEdit_types_37.stagedOrderSetCustomTypeActionType(), OrderEdit_types_38.stagedOrderSetCustomerEmailActionType(), OrderEdit_types_39.stagedOrderSetCustomerGroupActionType(), OrderEdit_types_40.stagedOrderSetCustomerIdActionType(), OrderEdit_types_41.stagedOrderSetDeliveryAddressActionType(), OrderEdit_types_42.stagedOrderSetDeliveryItemsActionType(), OrderEdit_types_43.stagedOrderSetLineItemCustomFieldActionType(), OrderEdit_types_44.stagedOrderSetLineItemCustomTypeActionType(), OrderEdit_types_45.stagedOrderSetLineItemPriceActionType(), OrderEdit_types_46.stagedOrderSetLineItemShippingDetailsActionType(), OrderEdit_types_47.stagedOrderSetLineItemTaxAmountActionType(), OrderEdit_types_48.stagedOrderSetLineItemTaxRateActionType(), OrderEdit_types_49.stagedOrderSetLineItemTotalPriceActionType(), OrderEdit_types_50.stagedOrderSetLocaleActionType(), OrderEdit_types_51.stagedOrderSetOrderNumberActionType(), OrderEdit_types_52.stagedOrderSetOrderTotalTaxActionType(), OrderEdit_types_53.stagedOrderSetParcelItemsActionType(), OrderEdit_types_54.stagedOrderSetParcelMeasurementsActionType(), OrderEdit_types_55.stagedOrderSetParcelTrackingDataActionType(), OrderEdit_types_56.stagedOrderSetReturnPaymentStateActionType(), OrderEdit_types_57.stagedOrderSetReturnShipmentStateActionType(), OrderEdit_types_58.stagedOrderSetShippingAddressActionType(), OrderEdit_types_59.stagedOrderSetShippingAddressAndCustomShippingMethodActionType(), OrderEdit_types_60.stagedOrderSetShippingAddressAndShippingMethodActionType(), OrderEdit_types_61.stagedOrderSetShippingMethodActionType(), OrderEdit_types_62.stagedOrderSetShippingMethodTaxAmountActionType(), OrderEdit_types_63.stagedOrderSetShippingMethodTaxRateActionType(), OrderEdit_types_64.stagedOrderSetShippingRateInputActionType(), OrderEdit_types_65.stagedOrderTransitionCustomLineItemStateActionType(), OrderEdit_types_66.stagedOrderTransitionLineItemStateActionType(), OrderEdit_types_67.stagedOrderTransitionStateActionType(), OrderEdit_types_68.stagedOrderUpdateItemShippingAddressActionType(), OrderEdit_types_69.stagedOrderUpdateSyncInfoActionType()]);
}
exports.stagedOrderUpdateActionType = stagedOrderUpdateActionType;
function customLineItemReturnItemType() {
    return Joi.object().unknown().keys({
        paymentState: returnPaymentStateType().required(),
        shipmentState: returnShipmentStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        customLineItemId: Joi.string().required(),
        id: Joi.string().required(),
        type: Joi.string().required().only('CustomLineItemReturnItem'),
        comment: Joi.string().optional()
    });
}
exports.customLineItemReturnItemType = customLineItemReturnItemType;
function deliveryType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(deliveryItemType()).required(),
        parcels: Joi.array().items(parcelType()).required(),
        createdAt: Joi.date().iso().required(),
        id: Joi.string().required(),
        address: Common_types_1.addressType().optional()
    });
}
exports.deliveryType = deliveryType;
function deliveryItemType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        id: Joi.string().required()
    });
}
exports.deliveryItemType = deliveryItemType;
function discountedLineItemPriceDraftType() {
    return Joi.object().unknown().keys({
        includedDiscounts: Joi.array().items(Cart_types_1.discountedLineItemPortionType()).required(),
        value: Common_types_2.moneyType().required()
    });
}
exports.discountedLineItemPriceDraftType = discountedLineItemPriceDraftType;
function itemStateType() {
    return Joi.object().unknown().keys({
        state: State_types_1.stateReferenceType().required(),
        quantity: Joi.number().required()
    });
}
exports.itemStateType = itemStateType;
function lineItemImportDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_4.localizedStringType().required(),
        price: Common_types_3.priceDraftType().required(),
        variant: productVariantImportDraftType().required(),
        quantity: Joi.number().required(),
        state: Joi.array().items(itemStateType()).optional(),
        distributionChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_1.channelResourceIdentifierType().optional(),
        custom: Type_types_1.customFieldsDraftType().optional(),
        shippingDetails: Cart_types_2.itemShippingDetailsDraftType().optional(),
        taxRate: TaxCategory_types_1.taxRateType().optional(),
        productId: Joi.string().optional()
    });
}
exports.lineItemImportDraftType = lineItemImportDraftType;
function lineItemReturnItemType() {
    return Joi.object().unknown().keys({
        paymentState: returnPaymentStateType().required(),
        shipmentState: returnShipmentStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        quantity: Joi.number().required(),
        id: Joi.string().required(),
        lineItemId: Joi.string().required(),
        type: Joi.string().required().only('LineItemReturnItem'),
        comment: Joi.string().optional()
    });
}
exports.lineItemReturnItemType = lineItemReturnItemType;
function orderType() {
    return Joi.object().unknown().keys({
        customLineItems: Joi.array().items(Cart_types_10.customLineItemType()).required(),
        lineItems: Joi.array().items(Cart_types_9.lineItemType()).required(),
        syncInfo: Joi.array().items(syncInfoType()).required(),
        origin: Cart_types_5.cartOriginType().required(),
        totalPrice: Common_types_2.moneyType().required(),
        orderState: orderStateType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        lastMessageSequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        discountCodes: Joi.array().items(Cart_types_11.discountCodeInfoType()).optional(),
        itemShippingAddresses: Joi.array().items(Common_types_1.addressType()).optional(),
        returnInfo: Joi.array().items(returnInfoType()).optional(),
        billingAddress: Common_types_1.addressType().optional(),
        shippingAddress: Common_types_1.addressType().optional(),
        cart: Cart_types_7.cartReferenceType().optional(),
        createdBy: Common_types_5.createdByType().optional(),
        custom: Type_types_2.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        inventoryMode: Cart_types_8.inventoryModeType().optional(),
        lastModifiedBy: Common_types_6.lastModifiedByType().optional(),
        paymentInfo: paymentInfoType().optional(),
        paymentState: paymentStateType().optional(),
        taxRoundingMode: Cart_types_13.roundingModeType().optional(),
        shipmentState: shipmentStateType().optional(),
        shippingInfo: Cart_types_6.shippingInfoType().optional(),
        shippingRateInput: Cart_types_3.shippingRateInputType().optional(),
        state: State_types_1.stateReferenceType().optional(),
        store: Store_types_1.storeKeyReferenceType().optional(),
        taxCalculationMode: Cart_types_12.taxCalculationModeType().optional(),
        taxMode: Cart_types_14.taxModeType().optional(),
        taxedPrice: Cart_types_4.taxedPriceType().optional(),
        completedAt: Joi.date().iso().optional(),
        anonymousId: Joi.string().optional(),
        country: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        customerId: Joi.string().optional(),
        locale: Joi.string().optional(),
        orderNumber: Joi.string().optional()
    });
}
exports.orderType = orderType;
function orderFromCartDraftType() {
    return Joi.object().unknown().keys({
        version: Joi.number().required(),
        id: Joi.string().required(),
        paymentState: paymentStateType().optional(),
        orderNumber: Joi.string().optional()
    });
}
exports.orderFromCartDraftType = orderFromCartDraftType;
function orderImportDraftType() {
    return Joi.object().unknown().keys({
        totalPrice: Common_types_2.moneyType().required(),
        customLineItems: Joi.array().items(Cart_types_15.customLineItemDraftType()).optional(),
        itemShippingAddresses: Joi.array().items(Common_types_1.addressType()).optional(),
        lineItems: Joi.array().items(lineItemImportDraftType()).optional(),
        billingAddress: Common_types_1.addressType().optional(),
        shippingAddress: Common_types_1.addressType().optional(),
        custom: Type_types_1.customFieldsDraftType().optional(),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional(),
        inventoryMode: Cart_types_8.inventoryModeType().optional(),
        orderState: orderStateType().optional(),
        paymentState: paymentStateType().optional(),
        taxRoundingMode: Cart_types_13.roundingModeType().optional(),
        shipmentState: shipmentStateType().optional(),
        shippingInfo: shippingInfoImportDraftType().optional(),
        taxedPrice: Cart_types_4.taxedPriceType().optional(),
        completedAt: Joi.date().iso().optional(),
        country: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        customerId: Joi.string().optional(),
        orderNumber: Joi.string().optional()
    });
}
exports.orderImportDraftType = orderImportDraftType;
function orderPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(orderType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.orderPagedQueryResponseType = orderPagedQueryResponseType;
function orderReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_7.referenceTypeIdType().required().only('order'),
        id: Joi.string().required(),
        obj: orderType().optional()
    });
}
exports.orderReferenceType = orderReferenceType;
function orderResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_7.referenceTypeIdType().optional().only('order'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.orderResourceIdentifierType = orderResourceIdentifierType;
const orderStateTypeValues = [
    'Open',
    'Confirmed',
    'Complete',
    'Cancelled'
];
function orderStateType() {
    return Joi.string().only(orderStateTypeValues);
}
exports.orderStateType = orderStateType;
function orderUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(orderUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.orderUpdateType = orderUpdateType;
function orderUpdateActionType() {
    return Joi.alternatives([orderAddDeliveryActionType(), orderAddItemShippingAddressActionType(), orderAddParcelToDeliveryActionType(), orderAddPaymentActionType(), orderAddReturnInfoActionType(), orderChangeOrderStateActionType(), orderChangePaymentStateActionType(), orderChangeShipmentStateActionType(), orderImportCustomLineItemStateActionType(), orderImportLineItemStateActionType(), orderRemoveDeliveryActionType(), orderRemoveItemShippingAddressActionType(), orderRemoveParcelFromDeliveryActionType(), orderRemovePaymentActionType(), orderSetBillingAddressActionType(), orderSetCustomFieldActionType(), orderSetCustomLineItemCustomFieldActionType(), orderSetCustomLineItemCustomTypeActionType(), orderSetCustomLineItemShippingDetailsActionType(), orderSetCustomTypeActionType(), orderSetCustomerEmailActionType(), orderSetCustomerIdActionType(), orderSetDeliveryAddressActionType(), orderSetDeliveryItemsActionType(), orderSetLineItemCustomFieldActionType(), orderSetLineItemCustomTypeActionType(), orderSetLineItemShippingDetailsActionType(), orderSetLocaleActionType(), orderSetOrderNumberActionType(), orderSetParcelItemsActionType(), orderSetParcelMeasurementsActionType(), orderSetParcelTrackingDataActionType(), orderSetReturnPaymentStateActionType(), orderSetReturnShipmentStateActionType(), orderSetShippingAddressActionType(), orderTransitionCustomLineItemStateActionType(), orderTransitionLineItemStateActionType(), orderTransitionStateActionType(), orderUpdateItemShippingAddressActionType(), orderUpdateSyncInfoActionType()]);
}
exports.orderUpdateActionType = orderUpdateActionType;
function parcelType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        id: Joi.string().required(),
        items: Joi.array().items(deliveryItemType()).optional(),
        measurements: parcelMeasurementsType().optional(),
        trackingData: trackingDataType().optional()
    });
}
exports.parcelType = parcelType;
function parcelDraftType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(deliveryItemType()).optional(),
        measurements: parcelMeasurementsType().optional(),
        trackingData: trackingDataType().optional()
    });
}
exports.parcelDraftType = parcelDraftType;
function parcelMeasurementsType() {
    return Joi.object().unknown().keys({
        heightInMillimeter: Joi.number().optional(),
        lengthInMillimeter: Joi.number().optional(),
        weightInGram: Joi.number().optional(),
        widthInMillimeter: Joi.number().optional()
    });
}
exports.parcelMeasurementsType = parcelMeasurementsType;
function paymentInfoType() {
    return Joi.object().unknown().keys({
        payments: Joi.array().items(Payment_types_1.paymentReferenceType()).required()
    });
}
exports.paymentInfoType = paymentInfoType;
const paymentStateTypeValues = [
    'BalanceDue',
    'Failed',
    'Pending',
    'CreditOwed',
    'Paid'
];
function paymentStateType() {
    return Joi.string().only(paymentStateTypeValues);
}
exports.paymentStateType = paymentStateType;
function productVariantImportDraftType() {
    return Joi.object().unknown().keys({
        attributes: Joi.array().items(Product_types_1.attributeType()).optional(),
        images: Joi.array().items(Common_types_8.imageType()).optional(),
        prices: Joi.array().items(Common_types_3.priceDraftType()).optional(),
        id: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productVariantImportDraftType = productVariantImportDraftType;
function returnInfoType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(returnItemType()).required(),
        returnDate: Joi.date().iso().optional(),
        returnTrackingId: Joi.string().optional()
    });
}
exports.returnInfoType = returnInfoType;
function returnItemType() {
    return Joi.alternatives([customLineItemReturnItemType(), lineItemReturnItemType()]);
}
exports.returnItemType = returnItemType;
function returnItemDraftType() {
    return Joi.object().unknown().keys({
        shipmentState: returnShipmentStateType().required(),
        quantity: Joi.number().required(),
        comment: Joi.string().optional(),
        customLineItemId: Joi.string().optional(),
        lineItemId: Joi.string().optional()
    });
}
exports.returnItemDraftType = returnItemDraftType;
const returnPaymentStateTypeValues = [
    'NonRefundable',
    'Initial',
    'Refunded',
    'NotRefunded'
];
function returnPaymentStateType() {
    return Joi.string().only(returnPaymentStateTypeValues);
}
exports.returnPaymentStateType = returnPaymentStateType;
const returnShipmentStateTypeValues = [
    'Advised',
    'Returned',
    'BackInStock',
    'Unusable'
];
function returnShipmentStateType() {
    return Joi.string().only(returnShipmentStateTypeValues);
}
exports.returnShipmentStateType = returnShipmentStateType;
const shipmentStateTypeValues = [
    'Shipped',
    'Ready',
    'Pending',
    'Delayed',
    'Partial',
    'Backorder'
];
function shipmentStateType() {
    return Joi.string().only(shipmentStateTypeValues);
}
exports.shipmentStateType = shipmentStateType;
function shippingInfoImportDraftType() {
    return Joi.object().unknown().keys({
        price: Common_types_2.moneyType().required(),
        shippingRate: ShippingMethod_types_1.shippingRateDraftType().required(),
        shippingMethodName: Joi.string().required(),
        deliveries: Joi.array().items(deliveryType()).optional(),
        discountedPrice: discountedLineItemPriceDraftType().optional(),
        shippingMethod: ShippingMethod_types_2.shippingMethodResourceIdentifierType().optional(),
        shippingMethodState: Cart_types_16.shippingMethodStateType().optional(),
        taxCategory: TaxCategory_types_2.taxCategoryResourceIdentifierType().optional(),
        taxRate: TaxCategory_types_1.taxRateType().optional()
    });
}
exports.shippingInfoImportDraftType = shippingInfoImportDraftType;
function syncInfoType() {
    return Joi.object().unknown().keys({
        channel: Channel_types_2.channelReferenceType().required(),
        syncedAt: Joi.date().iso().required(),
        externalId: Joi.string().optional()
    });
}
exports.syncInfoType = syncInfoType;
function taxedItemPriceDraftType() {
    return Joi.object().unknown().keys({
        totalGross: Common_types_2.moneyType().required(),
        totalNet: Common_types_2.moneyType().required()
    });
}
exports.taxedItemPriceDraftType = taxedItemPriceDraftType;
function trackingDataType() {
    return Joi.object().unknown().keys({
        isReturn: Joi.boolean().optional(),
        carrier: Joi.string().optional(),
        provider: Joi.string().optional(),
        providerTransaction: Joi.string().optional(),
        trackingId: Joi.string().optional()
    });
}
exports.trackingDataType = trackingDataType;
function orderAddDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addDelivery'),
        items: Joi.array().items(deliveryItemType()).optional(),
        parcels: Joi.array().items(parcelDraftType()).optional(),
        address: Common_types_1.addressType().optional()
    });
}
exports.orderAddDeliveryActionType = orderAddDeliveryActionType;
function orderAddItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_1.addressType().required(),
        action: Joi.string().required().only('addItemShippingAddress')
    });
}
exports.orderAddItemShippingAddressActionType = orderAddItemShippingAddressActionType;
function orderAddParcelToDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addParcelToDelivery'),
        deliveryId: Joi.string().required(),
        items: Joi.array().items(deliveryItemType()).optional(),
        measurements: parcelMeasurementsType().optional(),
        trackingData: trackingDataType().optional()
    });
}
exports.orderAddParcelToDeliveryActionType = orderAddParcelToDeliveryActionType;
function orderAddPaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_2.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('addPayment')
    });
}
exports.orderAddPaymentActionType = orderAddPaymentActionType;
function orderAddReturnInfoActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(returnItemDraftType()).required(),
        action: Joi.string().required().only('addReturnInfo'),
        returnDate: Joi.date().iso().optional(),
        returnTrackingId: Joi.string().optional()
    });
}
exports.orderAddReturnInfoActionType = orderAddReturnInfoActionType;
function orderChangeOrderStateActionType() {
    return Joi.object().unknown().keys({
        orderState: orderStateType().required(),
        action: Joi.string().required().only('changeOrderState')
    });
}
exports.orderChangeOrderStateActionType = orderChangeOrderStateActionType;
function orderChangePaymentStateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changePaymentState'),
        paymentState: paymentStateType().optional()
    });
}
exports.orderChangePaymentStateActionType = orderChangePaymentStateActionType;
function orderChangeShipmentStateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeShipmentState'),
        shipmentState: shipmentStateType().optional()
    });
}
exports.orderChangeShipmentStateActionType = orderChangeShipmentStateActionType;
function orderImportCustomLineItemStateActionType() {
    return Joi.object().unknown().keys({
        state: Joi.array().items(itemStateType()).required(),
        action: Joi.string().required().only('importCustomLineItemState'),
        customLineItemId: Joi.string().required()
    });
}
exports.orderImportCustomLineItemStateActionType = orderImportCustomLineItemStateActionType;
function orderImportLineItemStateActionType() {
    return Joi.object().unknown().keys({
        state: Joi.array().items(itemStateType()).required(),
        action: Joi.string().required().only('importLineItemState'),
        lineItemId: Joi.string().required()
    });
}
exports.orderImportLineItemStateActionType = orderImportLineItemStateActionType;
function orderRemoveDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeDelivery'),
        deliveryId: Joi.string().required()
    });
}
exports.orderRemoveDeliveryActionType = orderRemoveDeliveryActionType;
function orderRemoveItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeItemShippingAddress'),
        addressKey: Joi.string().required()
    });
}
exports.orderRemoveItemShippingAddressActionType = orderRemoveItemShippingAddressActionType;
function orderRemoveParcelFromDeliveryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeParcelFromDelivery'),
        parcelId: Joi.string().required()
    });
}
exports.orderRemoveParcelFromDeliveryActionType = orderRemoveParcelFromDeliveryActionType;
function orderRemovePaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_2.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('removePayment')
    });
}
exports.orderRemovePaymentActionType = orderRemovePaymentActionType;
function orderSetBillingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setBillingAddress'),
        address: Common_types_1.addressType().optional()
    });
}
exports.orderSetBillingAddressActionType = orderSetBillingAddressActionType;
function orderSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.orderSetCustomFieldActionType = orderSetCustomFieldActionType;
function orderSetCustomLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomField'),
        customLineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.orderSetCustomLineItemCustomFieldActionType = orderSetCustomLineItemCustomFieldActionType;
function orderSetCustomLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomType'),
        customLineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.orderSetCustomLineItemCustomTypeActionType = orderSetCustomLineItemCustomTypeActionType;
function orderSetCustomLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemShippingDetails'),
        customLineItemId: Joi.string().required(),
        shippingDetails: Cart_types_2.itemShippingDetailsDraftType().optional()
    });
}
exports.orderSetCustomLineItemShippingDetailsActionType = orderSetCustomLineItemShippingDetailsActionType;
function orderSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.orderSetCustomTypeActionType = orderSetCustomTypeActionType;
function orderSetCustomerEmailActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerEmail'),
        email: Joi.string().optional()
    });
}
exports.orderSetCustomerEmailActionType = orderSetCustomerEmailActionType;
function orderSetCustomerIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerId'),
        customerId: Joi.string().optional()
    });
}
exports.orderSetCustomerIdActionType = orderSetCustomerIdActionType;
function orderSetDeliveryAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDeliveryAddress'),
        deliveryId: Joi.string().required(),
        address: Common_types_1.addressType().optional()
    });
}
exports.orderSetDeliveryAddressActionType = orderSetDeliveryAddressActionType;
function orderSetDeliveryItemsActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(deliveryItemType()).required(),
        action: Joi.string().required().only('setDeliveryItems'),
        deliveryId: Joi.string().required()
    });
}
exports.orderSetDeliveryItemsActionType = orderSetDeliveryItemsActionType;
function orderSetLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomField'),
        lineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.orderSetLineItemCustomFieldActionType = orderSetLineItemCustomFieldActionType;
function orderSetLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomType'),
        lineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.orderSetLineItemCustomTypeActionType = orderSetLineItemCustomTypeActionType;
function orderSetLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemShippingDetails'),
        lineItemId: Joi.string().required(),
        shippingDetails: Cart_types_2.itemShippingDetailsDraftType().optional()
    });
}
exports.orderSetLineItemShippingDetailsActionType = orderSetLineItemShippingDetailsActionType;
function orderSetLocaleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLocale'),
        locale: Joi.string().optional()
    });
}
exports.orderSetLocaleActionType = orderSetLocaleActionType;
function orderSetOrderNumberActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setOrderNumber'),
        orderNumber: Joi.string().optional()
    });
}
exports.orderSetOrderNumberActionType = orderSetOrderNumberActionType;
function orderSetParcelItemsActionType() {
    return Joi.object().unknown().keys({
        items: Joi.array().items(deliveryItemType()).required(),
        action: Joi.string().required().only('setParcelItems'),
        parcelId: Joi.string().required()
    });
}
exports.orderSetParcelItemsActionType = orderSetParcelItemsActionType;
function orderSetParcelMeasurementsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setParcelMeasurements'),
        parcelId: Joi.string().required(),
        measurements: parcelMeasurementsType().optional()
    });
}
exports.orderSetParcelMeasurementsActionType = orderSetParcelMeasurementsActionType;
function orderSetParcelTrackingDataActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setParcelTrackingData'),
        parcelId: Joi.string().required(),
        trackingData: trackingDataType().optional()
    });
}
exports.orderSetParcelTrackingDataActionType = orderSetParcelTrackingDataActionType;
function orderSetReturnPaymentStateActionType() {
    return Joi.object().unknown().keys({
        paymentState: returnPaymentStateType().required(),
        action: Joi.string().required().only('setReturnPaymentState'),
        returnItemId: Joi.string().required()
    });
}
exports.orderSetReturnPaymentStateActionType = orderSetReturnPaymentStateActionType;
function orderSetReturnShipmentStateActionType() {
    return Joi.object().unknown().keys({
        shipmentState: returnShipmentStateType().required(),
        action: Joi.string().required().only('setReturnShipmentState'),
        returnItemId: Joi.string().required()
    });
}
exports.orderSetReturnShipmentStateActionType = orderSetReturnShipmentStateActionType;
function orderSetShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingAddress'),
        address: Common_types_1.addressType().optional()
    });
}
exports.orderSetShippingAddressActionType = orderSetShippingAddressActionType;
function orderTransitionCustomLineItemStateActionType() {
    return Joi.object().unknown().keys({
        fromState: State_types_2.stateResourceIdentifierType().required(),
        toState: State_types_2.stateResourceIdentifierType().required(),
        quantity: Joi.number().required(),
        action: Joi.string().required().only('transitionCustomLineItemState'),
        customLineItemId: Joi.string().required(),
        actualTransitionDate: Joi.date().iso().optional()
    });
}
exports.orderTransitionCustomLineItemStateActionType = orderTransitionCustomLineItemStateActionType;
function orderTransitionLineItemStateActionType() {
    return Joi.object().unknown().keys({
        fromState: State_types_2.stateResourceIdentifierType().required(),
        toState: State_types_2.stateResourceIdentifierType().required(),
        quantity: Joi.number().required(),
        action: Joi.string().required().only('transitionLineItemState'),
        lineItemId: Joi.string().required(),
        actualTransitionDate: Joi.date().iso().optional()
    });
}
exports.orderTransitionLineItemStateActionType = orderTransitionLineItemStateActionType;
function orderTransitionStateActionType() {
    return Joi.object().unknown().keys({
        state: State_types_2.stateResourceIdentifierType().required(),
        action: Joi.string().required().only('transitionState'),
        force: Joi.boolean().optional()
    });
}
exports.orderTransitionStateActionType = orderTransitionStateActionType;
function orderUpdateItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_1.addressType().required(),
        action: Joi.string().required().only('updateItemShippingAddress')
    });
}
exports.orderUpdateItemShippingAddressActionType = orderUpdateItemShippingAddressActionType;
function orderUpdateSyncInfoActionType() {
    return Joi.object().unknown().keys({
        channel: Channel_types_1.channelResourceIdentifierType().required(),
        action: Joi.string().required().only('updateSyncInfo'),
        syncedAt: Joi.date().iso().optional(),
        externalId: Joi.string().optional()
    });
}
exports.orderUpdateSyncInfoActionType = orderUpdateSyncInfoActionType;
