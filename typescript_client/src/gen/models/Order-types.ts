/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { stagedOrderAddCustomLineItemActionType } from './OrderEdit-types'
import { stagedOrderAddDeliveryActionType } from './OrderEdit-types'
import { stagedOrderAddDiscountCodeActionType } from './OrderEdit-types'
import { stagedOrderAddItemShippingAddressActionType } from './OrderEdit-types'
import { stagedOrderAddLineItemActionType } from './OrderEdit-types'
import { stagedOrderAddParcelToDeliveryActionType } from './OrderEdit-types'
import { stagedOrderAddPaymentActionType } from './OrderEdit-types'
import { stagedOrderAddReturnInfoActionType } from './OrderEdit-types'
import { stagedOrderAddShoppingListActionType } from './OrderEdit-types'
import { stagedOrderChangeCustomLineItemMoneyActionType } from './OrderEdit-types'
import { stagedOrderChangeCustomLineItemQuantityActionType } from './OrderEdit-types'
import { stagedOrderChangeLineItemQuantityActionType } from './OrderEdit-types'
import { stagedOrderChangeOrderStateActionType } from './OrderEdit-types'
import { stagedOrderChangePaymentStateActionType } from './OrderEdit-types'
import { stagedOrderChangeShipmentStateActionType } from './OrderEdit-types'
import { stagedOrderChangeTaxCalculationModeActionType } from './OrderEdit-types'
import { stagedOrderChangeTaxModeActionType } from './OrderEdit-types'
import { stagedOrderChangeTaxRoundingModeActionType } from './OrderEdit-types'
import { stagedOrderImportCustomLineItemStateActionType } from './OrderEdit-types'
import { stagedOrderImportLineItemStateActionType } from './OrderEdit-types'
import { stagedOrderRemoveCustomLineItemActionType } from './OrderEdit-types'
import { stagedOrderRemoveDeliveryActionType } from './OrderEdit-types'
import { stagedOrderRemoveDiscountCodeActionType } from './OrderEdit-types'
import { stagedOrderRemoveItemShippingAddressActionType } from './OrderEdit-types'
import { stagedOrderRemoveLineItemActionType } from './OrderEdit-types'
import { stagedOrderRemoveParcelFromDeliveryActionType } from './OrderEdit-types'
import { stagedOrderRemovePaymentActionType } from './OrderEdit-types'
import { stagedOrderSetBillingAddressActionType } from './OrderEdit-types'
import { stagedOrderSetCountryActionType } from './OrderEdit-types'
import { stagedOrderSetCustomFieldActionType } from './OrderEdit-types'
import { stagedOrderSetCustomLineItemCustomFieldActionType } from './OrderEdit-types'
import { stagedOrderSetCustomLineItemCustomTypeActionType } from './OrderEdit-types'
import { stagedOrderSetCustomLineItemShippingDetailsActionType } from './OrderEdit-types'
import { stagedOrderSetCustomLineItemTaxAmountActionType } from './OrderEdit-types'
import { stagedOrderSetCustomLineItemTaxRateActionType } from './OrderEdit-types'
import { stagedOrderSetCustomShippingMethodActionType } from './OrderEdit-types'
import { stagedOrderSetCustomTypeActionType } from './OrderEdit-types'
import { stagedOrderSetCustomerEmailActionType } from './OrderEdit-types'
import { stagedOrderSetCustomerGroupActionType } from './OrderEdit-types'
import { stagedOrderSetCustomerIdActionType } from './OrderEdit-types'
import { stagedOrderSetDeliveryAddressActionType } from './OrderEdit-types'
import { stagedOrderSetDeliveryItemsActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemCustomFieldActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemCustomTypeActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemPriceActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemShippingDetailsActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemTaxAmountActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemTaxRateActionType } from './OrderEdit-types'
import { stagedOrderSetLineItemTotalPriceActionType } from './OrderEdit-types'
import { stagedOrderSetLocaleActionType } from './OrderEdit-types'
import { stagedOrderSetOrderNumberActionType } from './OrderEdit-types'
import { stagedOrderSetOrderTotalTaxActionType } from './OrderEdit-types'
import { stagedOrderSetParcelItemsActionType } from './OrderEdit-types'
import { stagedOrderSetParcelMeasurementsActionType } from './OrderEdit-types'
import { stagedOrderSetParcelTrackingDataActionType } from './OrderEdit-types'
import { stagedOrderSetReturnPaymentStateActionType } from './OrderEdit-types'
import { stagedOrderSetReturnShipmentStateActionType } from './OrderEdit-types'
import { stagedOrderSetShippingAddressActionType } from './OrderEdit-types'
import { stagedOrderSetShippingAddressAndCustomShippingMethodActionType } from './OrderEdit-types'
import { stagedOrderSetShippingAddressAndShippingMethodActionType } from './OrderEdit-types'
import { stagedOrderSetShippingMethodActionType } from './OrderEdit-types'
import { stagedOrderSetShippingMethodTaxAmountActionType } from './OrderEdit-types'
import { stagedOrderSetShippingMethodTaxRateActionType } from './OrderEdit-types'
import { stagedOrderSetShippingRateInputActionType } from './OrderEdit-types'
import { stagedOrderTransitionCustomLineItemStateActionType } from './OrderEdit-types'
import { stagedOrderTransitionLineItemStateActionType } from './OrderEdit-types'
import { stagedOrderTransitionStateActionType } from './OrderEdit-types'
import { stagedOrderUpdateItemShippingAddressActionType } from './OrderEdit-types'
import { stagedOrderUpdateSyncInfoActionType } from './OrderEdit-types'
import { addressType } from './Common-types'
import { discountedLineItemPortionType } from './Cart-types'
import { moneyType } from './Common-types'
import { stateReferenceType } from './State-types'
import { taxRateType } from './TaxCategory-types'
import { itemShippingDetailsDraftType } from './Cart-types'
import { priceDraftType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { localizedStringType } from './Common-types'
import { channelResourceIdentifierType } from './Channel-types'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { shippingRateInputType } from './Cart-types'
import { taxedPriceType } from './Cart-types'
import { cartOriginType } from './Cart-types'
import { shippingInfoType } from './Cart-types'
import { cartReferenceType } from './Cart-types'
import { inventoryModeType } from './Cart-types'
import { lineItemType } from './Cart-types'
import { customLineItemType } from './Cart-types'
import { discountCodeInfoType } from './Cart-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { customFieldsType } from './Type-types'
import { taxCalculationModeType } from './Cart-types'
import { storeKeyReferenceType } from './Store-types'
import { roundingModeType } from './Cart-types'
import { taxModeType } from './Cart-types'
import { stagedOrderType } from './OrderEdit-types'
import { loggedResourceType } from './Common-types'
import { customerGroupResourceIdentifierType } from './CustomerGroup-types'
import { customLineItemDraftType } from './Cart-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { paymentReferenceType } from './Payment-types'
import { imageType } from './Common-types'
import { attributeType } from './Product-types'
import { shippingRateDraftType } from './ShippingMethod-types'
import { shippingMethodStateType } from './Cart-types'
import { shippingMethodResourceIdentifierType } from './ShippingMethod-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { channelReferenceType } from './Channel-types'
import { paymentResourceIdentifierType } from './Payment-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'
import { stateResourceIdentifierType } from './State-types'

export function stagedOrderUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([stagedOrderAddCustomLineItemActionType(), stagedOrderAddDeliveryActionType(), stagedOrderAddDiscountCodeActionType(), stagedOrderAddItemShippingAddressActionType(), stagedOrderAddLineItemActionType(), stagedOrderAddParcelToDeliveryActionType(), stagedOrderAddPaymentActionType(), stagedOrderAddReturnInfoActionType(), stagedOrderAddShoppingListActionType(), stagedOrderChangeCustomLineItemMoneyActionType(), stagedOrderChangeCustomLineItemQuantityActionType(), stagedOrderChangeLineItemQuantityActionType(), stagedOrderChangeOrderStateActionType(), stagedOrderChangePaymentStateActionType(), stagedOrderChangeShipmentStateActionType(), stagedOrderChangeTaxCalculationModeActionType(), stagedOrderChangeTaxModeActionType(), stagedOrderChangeTaxRoundingModeActionType(), stagedOrderImportCustomLineItemStateActionType(), stagedOrderImportLineItemStateActionType(), stagedOrderRemoveCustomLineItemActionType(), stagedOrderRemoveDeliveryActionType(), stagedOrderRemoveDiscountCodeActionType(), stagedOrderRemoveItemShippingAddressActionType(), stagedOrderRemoveLineItemActionType(), stagedOrderRemoveParcelFromDeliveryActionType(), stagedOrderRemovePaymentActionType(), stagedOrderSetBillingAddressActionType(), stagedOrderSetCountryActionType(), stagedOrderSetCustomFieldActionType(), stagedOrderSetCustomLineItemCustomFieldActionType(), stagedOrderSetCustomLineItemCustomTypeActionType(), stagedOrderSetCustomLineItemShippingDetailsActionType(), stagedOrderSetCustomLineItemTaxAmountActionType(), stagedOrderSetCustomLineItemTaxRateActionType(), stagedOrderSetCustomShippingMethodActionType(), stagedOrderSetCustomTypeActionType(), stagedOrderSetCustomerEmailActionType(), stagedOrderSetCustomerGroupActionType(), stagedOrderSetCustomerIdActionType(), stagedOrderSetDeliveryAddressActionType(), stagedOrderSetDeliveryItemsActionType(), stagedOrderSetLineItemCustomFieldActionType(), stagedOrderSetLineItemCustomTypeActionType(), stagedOrderSetLineItemPriceActionType(), stagedOrderSetLineItemShippingDetailsActionType(), stagedOrderSetLineItemTaxAmountActionType(), stagedOrderSetLineItemTaxRateActionType(), stagedOrderSetLineItemTotalPriceActionType(), stagedOrderSetLocaleActionType(), stagedOrderSetOrderNumberActionType(), stagedOrderSetOrderTotalTaxActionType(), stagedOrderSetParcelItemsActionType(), stagedOrderSetParcelMeasurementsActionType(), stagedOrderSetParcelTrackingDataActionType(), stagedOrderSetReturnPaymentStateActionType(), stagedOrderSetReturnShipmentStateActionType(), stagedOrderSetShippingAddressActionType(), stagedOrderSetShippingAddressAndCustomShippingMethodActionType(), stagedOrderSetShippingAddressAndShippingMethodActionType(), stagedOrderSetShippingMethodActionType(), stagedOrderSetShippingMethodTaxAmountActionType(), stagedOrderSetShippingMethodTaxRateActionType(), stagedOrderSetShippingRateInputActionType(), stagedOrderTransitionCustomLineItemStateActionType(), stagedOrderTransitionLineItemStateActionType(), stagedOrderTransitionStateActionType(), stagedOrderUpdateItemShippingAddressActionType(), stagedOrderUpdateSyncInfoActionType()])
}

export function customLineItemReturnItemType(): Joi.AnySchema {
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
          })
}

export function deliveryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             parcels: Joi.array().items(parcelType()).required(),
             createdAt: Joi.date().iso().required(),
             id: Joi.string().required(),
             address: addressType().optional()
          })
}

export function deliveryItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             id: Joi.string().required()
          })
}

export function discountedLineItemPriceDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             includedDiscounts: Joi.array().items(discountedLineItemPortionType()).required(),
             value: moneyType().required()
          })
}

export function itemStateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateReferenceType().required(),
             quantity: Joi.number().required()
          })
}

export function lineItemImportDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             price: priceDraftType().required(),
             variant: productVariantImportDraftType().required(),
             quantity: Joi.number().required(),
             state: Joi.array().items(itemStateType()).optional(),
             distributionChannel: channelResourceIdentifierType().optional(),
             supplyChannel: channelResourceIdentifierType().optional(),
             custom: customFieldsDraftType().optional(),
             shippingDetails: itemShippingDetailsDraftType().optional(),
             taxRate: taxRateType().optional(),
             productId: Joi.string().optional()
          })
}

export function lineItemReturnItemType(): Joi.AnySchema {
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
          })
}

export function orderType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customLineItems: Joi.array().items(customLineItemType()).required(),
             lineItems: Joi.array().items(lineItemType()).required(),
             syncInfo: Joi.array().items(syncInfoType()).required(),
             origin: cartOriginType().required(),
             totalPrice: moneyType().required(),
             orderState: orderStateType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             lastMessageSequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             discountCodes: Joi.array().items(discountCodeInfoType()).optional(),
             itemShippingAddresses: Joi.array().items(addressType()).optional(),
             returnInfo: Joi.array().items(returnInfoType()).optional(),
             billingAddress: addressType().optional(),
             shippingAddress: addressType().optional(),
             cart: cartReferenceType().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             inventoryMode: inventoryModeType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             paymentInfo: paymentInfoType().optional(),
             paymentState: paymentStateType().optional(),
             taxRoundingMode: roundingModeType().optional(),
             shipmentState: shipmentStateType().optional(),
             shippingInfo: shippingInfoType().optional(),
             shippingRateInput: shippingRateInputType().optional(),
             state: stateReferenceType().optional(),
             store: storeKeyReferenceType().optional(),
             taxCalculationMode: taxCalculationModeType().optional(),
             taxMode: taxModeType().optional(),
             taxedPrice: taxedPriceType().optional(),
             completedAt: Joi.date().iso().optional(),
             anonymousId: Joi.string().optional(),
             country: Joi.string().optional(),
             customerEmail: Joi.string().optional(),
             customerId: Joi.string().optional(),
             locale: Joi.string().optional(),
             orderNumber: Joi.string().optional()
          })
}

export function orderFromCartDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             version: Joi.number().required(),
             id: Joi.string().required(),
             paymentState: paymentStateType().optional(),
             orderNumber: Joi.string().optional()
          })
}

export function orderImportDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             totalPrice: moneyType().required(),
             customLineItems: Joi.array().items(customLineItemDraftType()).optional(),
             itemShippingAddresses: Joi.array().items(addressType()).optional(),
             lineItems: Joi.array().items(lineItemImportDraftType()).optional(),
             billingAddress: addressType().optional(),
             shippingAddress: addressType().optional(),
             custom: customFieldsDraftType().optional(),
             customerGroup: customerGroupResourceIdentifierType().optional(),
             inventoryMode: inventoryModeType().optional(),
             orderState: orderStateType().optional(),
             paymentState: paymentStateType().optional(),
             taxRoundingMode: roundingModeType().optional(),
             shipmentState: shipmentStateType().optional(),
             shippingInfo: shippingInfoImportDraftType().optional(),
             taxedPrice: taxedPriceType().optional(),
             completedAt: Joi.date().iso().optional(),
             country: Joi.string().optional(),
             customerEmail: Joi.string().optional(),
             customerId: Joi.string().optional(),
             orderNumber: Joi.string().optional()
          })
}

export function orderPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(orderType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function orderReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('order'),
             id: Joi.string().required(),
             obj: orderType().optional()
          })
}

export function orderResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('order'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

const orderStateTypeValues = [

   'Open',
   'Confirmed',
   'Complete',
   'Cancelled'

]

export function orderStateType(): Joi.AnySchema {
   return Joi.string().only(orderStateTypeValues)
}

export function orderUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(orderUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function orderUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([orderAddDeliveryActionType(), orderAddItemShippingAddressActionType(), orderAddParcelToDeliveryActionType(), orderAddPaymentActionType(), orderAddReturnInfoActionType(), orderChangeOrderStateActionType(), orderChangePaymentStateActionType(), orderChangeShipmentStateActionType(), orderImportCustomLineItemStateActionType(), orderImportLineItemStateActionType(), orderRemoveDeliveryActionType(), orderRemoveItemShippingAddressActionType(), orderRemoveParcelFromDeliveryActionType(), orderRemovePaymentActionType(), orderSetBillingAddressActionType(), orderSetCustomFieldActionType(), orderSetCustomLineItemCustomFieldActionType(), orderSetCustomLineItemCustomTypeActionType(), orderSetCustomLineItemShippingDetailsActionType(), orderSetCustomTypeActionType(), orderSetCustomerEmailActionType(), orderSetCustomerIdActionType(), orderSetDeliveryAddressActionType(), orderSetDeliveryItemsActionType(), orderSetLineItemCustomFieldActionType(), orderSetLineItemCustomTypeActionType(), orderSetLineItemShippingDetailsActionType(), orderSetLocaleActionType(), orderSetOrderNumberActionType(), orderSetParcelItemsActionType(), orderSetParcelMeasurementsActionType(), orderSetParcelTrackingDataActionType(), orderSetReturnPaymentStateActionType(), orderSetReturnShipmentStateActionType(), orderSetShippingAddressActionType(), orderTransitionCustomLineItemStateActionType(), orderTransitionLineItemStateActionType(), orderTransitionStateActionType(), orderUpdateItemShippingAddressActionType(), orderUpdateSyncInfoActionType()])
}

export function parcelType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             id: Joi.string().required(),
             items: Joi.array().items(deliveryItemType()).optional(),
             measurements: parcelMeasurementsType().optional(),
             trackingData: trackingDataType().optional()
          })
}

export function parcelDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).optional(),
             measurements: parcelMeasurementsType().optional(),
             trackingData: trackingDataType().optional()
          })
}

export function parcelMeasurementsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             heightInMillimeter: Joi.number().optional(),
             lengthInMillimeter: Joi.number().optional(),
             weightInGram: Joi.number().optional(),
             widthInMillimeter: Joi.number().optional()
          })
}

export function paymentInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payments: Joi.array().items(paymentReferenceType()).required()
          })
}

const paymentStateTypeValues = [

   'BalanceDue',
   'Failed',
   'Pending',
   'CreditOwed',
   'Paid'

]

export function paymentStateType(): Joi.AnySchema {
   return Joi.string().only(paymentStateTypeValues)
}

export function productVariantImportDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributes: Joi.array().items(attributeType()).optional(),
             images: Joi.array().items(imageType()).optional(),
             prices: Joi.array().items(priceDraftType()).optional(),
             id: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function returnInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(returnItemType()).required(),
             returnDate: Joi.date().iso().optional(),
             returnTrackingId: Joi.string().optional()
          })
}

export function returnItemType(): Joi.AnySchema {
   return Joi.alternatives([customLineItemReturnItemType(), lineItemReturnItemType()])
}

export function returnItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shipmentState: returnShipmentStateType().required(),
             quantity: Joi.number().required(),
             comment: Joi.string().optional(),
             customLineItemId: Joi.string().optional(),
             lineItemId: Joi.string().optional()
          })
}

const returnPaymentStateTypeValues = [

   'NonRefundable',
   'Initial',
   'Refunded',
   'NotRefunded'

]

export function returnPaymentStateType(): Joi.AnySchema {
   return Joi.string().only(returnPaymentStateTypeValues)
}

const returnShipmentStateTypeValues = [

   'Advised',
   'Returned',
   'BackInStock',
   'Unusable'

]

export function returnShipmentStateType(): Joi.AnySchema {
   return Joi.string().only(returnShipmentStateTypeValues)
}

const shipmentStateTypeValues = [

   'Shipped',
   'Ready',
   'Pending',
   'Delayed',
   'Partial',
   'Backorder'

]

export function shipmentStateType(): Joi.AnySchema {
   return Joi.string().only(shipmentStateTypeValues)
}

export function shippingInfoImportDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: moneyType().required(),
             shippingRate: shippingRateDraftType().required(),
             shippingMethodName: Joi.string().required(),
             deliveries: Joi.array().items(deliveryType()).optional(),
             discountedPrice: discountedLineItemPriceDraftType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional(),
             shippingMethodState: shippingMethodStateType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional(),
             taxRate: taxRateType().optional()
          })
}

export function syncInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             channel: channelReferenceType().required(),
             syncedAt: Joi.date().iso().required(),
             externalId: Joi.string().optional()
          })
}

export function taxedItemPriceDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             totalGross: moneyType().required(),
             totalNet: moneyType().required()
          })
}

export function trackingDataType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isReturn: Joi.boolean().optional(),
             carrier: Joi.string().optional(),
             provider: Joi.string().optional(),
             providerTransaction: Joi.string().optional(),
             trackingId: Joi.string().optional()
          })
}

export function orderAddDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addDelivery'),
             items: Joi.array().items(deliveryItemType()).optional(),
             parcels: Joi.array().items(parcelDraftType()).optional(),
             address: addressType().optional()
          })
}

export function orderAddItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('addItemShippingAddress')
          })
}

export function orderAddParcelToDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addParcelToDelivery'),
             deliveryId: Joi.string().required(),
             items: Joi.array().items(deliveryItemType()).optional(),
             measurements: parcelMeasurementsType().optional(),
             trackingData: trackingDataType().optional()
          })
}

export function orderAddPaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('addPayment')
          })
}

export function orderAddReturnInfoActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(returnItemDraftType()).required(),
             action: Joi.string().required().only('addReturnInfo'),
             returnDate: Joi.date().iso().optional(),
             returnTrackingId: Joi.string().optional()
          })
}

export function orderChangeOrderStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             orderState: orderStateType().required(),
             action: Joi.string().required().only('changeOrderState')
          })
}

export function orderChangePaymentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changePaymentState'),
             paymentState: paymentStateType().optional()
          })
}

export function orderChangeShipmentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeShipmentState'),
             shipmentState: shipmentStateType().optional()
          })
}

export function orderImportCustomLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: Joi.array().items(itemStateType()).required(),
             action: Joi.string().required().only('importCustomLineItemState'),
             customLineItemId: Joi.string().required()
          })
}

export function orderImportLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: Joi.array().items(itemStateType()).required(),
             action: Joi.string().required().only('importLineItemState'),
             lineItemId: Joi.string().required()
          })
}

export function orderRemoveDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeDelivery'),
             deliveryId: Joi.string().required()
          })
}

export function orderRemoveItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeItemShippingAddress'),
             addressKey: Joi.string().required()
          })
}

export function orderRemoveParcelFromDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeParcelFromDelivery'),
             parcelId: Joi.string().required()
          })
}

export function orderRemovePaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('removePayment')
          })
}

export function orderSetBillingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setBillingAddress'),
             address: addressType().optional()
          })
}

export function orderSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function orderSetCustomLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomField'),
             customLineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function orderSetCustomLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomType'),
             customLineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function orderSetCustomLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemShippingDetails'),
             customLineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function orderSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function orderSetCustomerEmailActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerEmail'),
             email: Joi.string().optional()
          })
}

export function orderSetCustomerIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerId'),
             customerId: Joi.string().optional()
          })
}

export function orderSetDeliveryAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDeliveryAddress'),
             deliveryId: Joi.string().required(),
             address: addressType().optional()
          })
}

export function orderSetDeliveryItemsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             action: Joi.string().required().only('setDeliveryItems'),
             deliveryId: Joi.string().required()
          })
}

export function orderSetLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomField'),
             lineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function orderSetLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomType'),
             lineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function orderSetLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemShippingDetails'),
             lineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function orderSetLocaleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLocale'),
             locale: Joi.string().optional()
          })
}

export function orderSetOrderNumberActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setOrderNumber'),
             orderNumber: Joi.string().optional()
          })
}

export function orderSetParcelItemsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             action: Joi.string().required().only('setParcelItems'),
             parcelId: Joi.string().required()
          })
}

export function orderSetParcelMeasurementsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setParcelMeasurements'),
             parcelId: Joi.string().required(),
             measurements: parcelMeasurementsType().optional()
          })
}

export function orderSetParcelTrackingDataActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setParcelTrackingData'),
             parcelId: Joi.string().required(),
             trackingData: trackingDataType().optional()
          })
}

export function orderSetReturnPaymentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             paymentState: returnPaymentStateType().required(),
             action: Joi.string().required().only('setReturnPaymentState'),
             returnItemId: Joi.string().required()
          })
}

export function orderSetReturnShipmentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shipmentState: returnShipmentStateType().required(),
             action: Joi.string().required().only('setReturnShipmentState'),
             returnItemId: Joi.string().required()
          })
}

export function orderSetShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingAddress'),
             address: addressType().optional()
          })
}

export function orderTransitionCustomLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateResourceIdentifierType().required(),
             toState: stateResourceIdentifierType().required(),
             quantity: Joi.number().required(),
             action: Joi.string().required().only('transitionCustomLineItemState'),
             customLineItemId: Joi.string().required(),
             actualTransitionDate: Joi.date().iso().optional()
          })
}

export function orderTransitionLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateResourceIdentifierType().required(),
             toState: stateResourceIdentifierType().required(),
             quantity: Joi.number().required(),
             action: Joi.string().required().only('transitionLineItemState'),
             lineItemId: Joi.string().required(),
             actualTransitionDate: Joi.date().iso().optional()
          })
}

export function orderTransitionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateResourceIdentifierType().required(),
             action: Joi.string().required().only('transitionState'),
             force: Joi.boolean().optional()
          })
}

export function orderUpdateItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('updateItemShippingAddress')
          })
}

export function orderUpdateSyncInfoActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             channel: channelResourceIdentifierType().required(),
             action: Joi.string().required().only('updateSyncInfo'),
             syncedAt: Joi.date().iso().optional(),
             externalId: Joi.string().optional()
          })
}