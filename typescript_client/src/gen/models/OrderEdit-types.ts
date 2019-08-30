/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { orderReferenceType } from './Order-types'
import { customFieldsType } from './Type-types'
import { stagedOrderUpdateActionType } from './Order-types'
import { loggedResourceType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { errorObjectType } from './Error-types'
import { messagePayloadType } from './Message-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { moneyType } from './Common-types'
import { taxedPriceType } from './Cart-types'
import { shipmentStateType } from './Order-types'
import { shippingRateInputType } from './Cart-types'
import { cartOriginType } from './Cart-types'
import { shippingInfoType } from './Cart-types'
import { cartReferenceType } from './Cart-types'
import { inventoryModeType } from './Cart-types'
import { orderStateType } from './Order-types'
import { returnInfoType } from './Order-types'
import { lineItemType } from './Cart-types'
import { customLineItemType } from './Cart-types'
import { addressType } from './Common-types'
import { stateReferenceType } from './State-types'
import { paymentStateType } from './Order-types'
import { discountCodeInfoType } from './Cart-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { taxCalculationModeType } from './Cart-types'
import { storeKeyReferenceType } from './Store-types'
import { syncInfoType } from './Order-types'
import { roundingModeType } from './Cart-types'
import { taxModeType } from './Cart-types'
import { paymentInfoType } from './Order-types'
import { orderType } from './Order-types'
import { typeResourceIdentifierType } from './Type-types'
import { externalTaxRateDraftType } from './Cart-types'
import { localizedStringType } from './Common-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { deliveryItemType } from './Order-types'
import { parcelDraftType } from './Order-types'
import { itemShippingDetailsDraftType } from './Cart-types'
import { externalLineItemTotalPriceType } from './Cart-types'
import { channelResourceIdentifierType } from './Channel-types'
import { trackingDataType } from './Order-types'
import { parcelMeasurementsType } from './Order-types'
import { paymentResourceIdentifierType } from './Payment-types'
import { returnItemDraftType } from './Order-types'
import { shoppingListResourceIdentifierType } from './ShoppingList-types'
import { itemStateType } from './Order-types'
import { discountCodeReferenceType } from './DiscountCode-types'
import { fieldContainerType } from './Type-types'
import { externalTaxAmountDraftType } from './Cart-types'
import { shippingRateDraftType } from './ShippingMethod-types'
import { customerGroupResourceIdentifierType } from './CustomerGroup-types'
import { taxPortionType } from './Cart-types'
import { returnPaymentStateType } from './Order-types'
import { returnShipmentStateType } from './Order-types'
import { shippingMethodResourceIdentifierType } from './ShippingMethod-types'
import { shippingRateInputDraftType } from './Cart-types'
import { stateResourceIdentifierType } from './State-types'

export function orderEditType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             stagedActions: Joi.array().items(stagedOrderUpdateActionType()).required(),
             result: orderEditResultType().required(),
             resource: orderReferenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             comment: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function orderEditAppliedType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             excerptAfterEdit: orderExcerptType().required(),
             excerptBeforeEdit: orderExcerptType().required(),
             appliedAt: Joi.date().iso().required(),
             type: Joi.string().required().only('Applied')
          })
}

export function orderEditApplyType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             editVersion: Joi.number().required(),
             resourceVersion: Joi.number().required()
          })
}

export function orderEditDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: orderReferenceType().required(),
             stagedActions: Joi.array().items(stagedOrderUpdateActionType()).optional(),
             custom: customFieldsDraftType().optional(),
             dryRun: Joi.boolean().optional(),
             comment: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function orderEditNotProcessedType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('NotProcessed')
          })
}

export function orderEditPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(orderEditType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function orderEditPreviewFailureType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             errors: Joi.array().items(errorObjectType()).required(),
             type: Joi.string().required().only('PreviewFailure')
          })
}

export function orderEditPreviewSuccessType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             messagePayloads: Joi.array().items(messagePayloadType()).required(),
             preview: stagedOrderType().required(),
             type: Joi.string().required().only('PreviewSuccess')
          })
}

export function orderEditReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('order-edit'),
             id: Joi.string().required(),
             obj: orderEditType().optional()
          })
}

export function orderEditResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('order-edit'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function orderEditResultType(): Joi.AnySchema {
   return Joi.alternatives([orderEditAppliedType(), orderEditNotProcessedType(), orderEditPreviewFailureType(), orderEditPreviewSuccessType()])
}

export function orderEditUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(orderEditUpdateActionType()).required(),
             dryRun: Joi.boolean().required(),
             version: Joi.number().required()
          })
}

export function orderEditUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([orderEditAddStagedActionActionType(), orderEditSetCommentActionType(), orderEditSetCustomFieldActionType(), orderEditSetCustomTypeActionType(), orderEditSetKeyActionType(), orderEditSetStagedActionsActionType()])
}

export function orderExcerptType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             totalPrice: moneyType().required(),
             version: Joi.number().required(),
             taxedPrice: taxedPriceType().optional()
          })
}

export function stagedOrderType(): Joi.AnySchema {
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

export function orderEditAddStagedActionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             stagedAction: stagedOrderUpdateActionType().required(),
             action: Joi.string().required().only('addStagedAction')
          })
}

export function orderEditSetCommentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setComment'),
             comment: Joi.string().optional()
          })
}

export function orderEditSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function orderEditSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             type: typeResourceIdentifierType().optional(),
             fields: Joi.any().optional()
          })
}

export function orderEditSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function orderEditSetStagedActionsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             stagedActions: Joi.array().items(stagedOrderUpdateActionType()).required(),
             action: Joi.string().required().only('setStagedActions')
          })
}

export function stagedOrderAddCustomLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             money: moneyType().required(),
             action: Joi.string().required().only('addCustomLineItem'),
             slug: Joi.string().required(),
             custom: customFieldsDraftType().optional(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional(),
             quantity: Joi.number().optional()
          })
}

export function stagedOrderAddDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addDelivery'),
             items: Joi.array().items(deliveryItemType()).optional(),
             parcels: Joi.array().items(parcelDraftType()).optional(),
             address: addressType().optional()
          })
}

export function stagedOrderAddDiscountCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addDiscountCode'),
             code: Joi.string().required()
          })
}

export function stagedOrderAddItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('addItemShippingAddress')
          })
}

export function stagedOrderAddLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addLineItem'),
             distributionChannel: channelResourceIdentifierType().optional(),
             supplyChannel: channelResourceIdentifierType().optional(),
             custom: customFieldsDraftType().optional(),
             externalTotalPrice: externalLineItemTotalPriceType().optional(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             shippingDetails: itemShippingDetailsDraftType().optional(),
             externalPrice: moneyType().optional(),
             quantity: Joi.number().optional(),
             variantId: Joi.number().optional(),
             productId: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function stagedOrderAddParcelToDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addParcelToDelivery'),
             deliveryId: Joi.string().required(),
             items: Joi.array().items(deliveryItemType()).optional(),
             measurements: parcelMeasurementsType().optional(),
             trackingData: trackingDataType().optional()
          })
}

export function stagedOrderAddPaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('addPayment')
          })
}

export function stagedOrderAddReturnInfoActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(returnItemDraftType()).required(),
             action: Joi.string().required().only('addReturnInfo'),
             returnDate: Joi.date().iso().optional(),
             returnTrackingId: Joi.string().optional()
          })
}

export function stagedOrderAddShoppingListActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shoppingList: shoppingListResourceIdentifierType().required(),
             action: Joi.string().required().only('addShoppingList'),
             distributionChannel: channelResourceIdentifierType().optional(),
             supplyChannel: channelResourceIdentifierType().optional()
          })
}

export function stagedOrderChangeCustomLineItemMoneyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             money: moneyType().required(),
             action: Joi.string().required().only('changeCustomLineItemMoney'),
             customLineItemId: Joi.string().required()
          })
}

export function stagedOrderChangeCustomLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeCustomLineItemQuantity'),
             customLineItemId: Joi.string().required()
          })
}

export function stagedOrderChangeLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeLineItemQuantity'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional(),
             externalPrice: moneyType().optional()
          })
}

export function stagedOrderChangeOrderStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             orderState: orderStateType().required(),
             action: Joi.string().required().only('changeOrderState')
          })
}

export function stagedOrderChangePaymentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changePaymentState'),
             paymentState: paymentStateType().optional()
          })
}

export function stagedOrderChangeShipmentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeShipmentState'),
             shipmentState: shipmentStateType().optional()
          })
}

export function stagedOrderChangeTaxCalculationModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxCalculationMode: taxCalculationModeType().required(),
             action: Joi.string().required().only('changeTaxCalculationMode')
          })
}

export function stagedOrderChangeTaxModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxMode: taxModeType().required(),
             action: Joi.string().required().only('changeTaxMode')
          })
}

export function stagedOrderChangeTaxRoundingModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxRoundingMode: roundingModeType().required(),
             action: Joi.string().required().only('changeTaxRoundingMode')
          })
}

export function stagedOrderImportCustomLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: Joi.array().items(itemStateType()).required(),
             action: Joi.string().required().only('importCustomLineItemState'),
             customLineItemId: Joi.string().required()
          })
}

export function stagedOrderImportLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: Joi.array().items(itemStateType()).required(),
             action: Joi.string().required().only('importLineItemState'),
             lineItemId: Joi.string().required()
          })
}

export function stagedOrderRemoveCustomLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeCustomLineItem'),
             customLineItemId: Joi.string().required()
          })
}

export function stagedOrderRemoveDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeDelivery'),
             deliveryId: Joi.string().required()
          })
}

export function stagedOrderRemoveDiscountCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             action: Joi.string().required().only('removeDiscountCode')
          })
}

export function stagedOrderRemoveItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeItemShippingAddress'),
             addressKey: Joi.string().required()
          })
}

export function stagedOrderRemoveLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeLineItem'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional(),
             shippingDetailsToRemove: itemShippingDetailsDraftType().optional(),
             externalPrice: moneyType().optional(),
             quantity: Joi.number().optional()
          })
}

export function stagedOrderRemoveParcelFromDeliveryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeParcelFromDelivery'),
             parcelId: Joi.string().required()
          })
}

export function stagedOrderRemovePaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('removePayment')
          })
}

export function stagedOrderSetBillingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setBillingAddress'),
             address: addressType().optional()
          })
}

export function stagedOrderSetCountryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCountry'),
             country: Joi.string().optional()
          })
}

export function stagedOrderSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function stagedOrderSetCustomLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomField'),
             customLineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function stagedOrderSetCustomLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomType'),
             customLineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function stagedOrderSetCustomLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemShippingDetails'),
             customLineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function stagedOrderSetCustomLineItemTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemTaxAmount'),
             customLineItemId: Joi.string().required(),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function stagedOrderSetCustomLineItemTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemTaxRate'),
             customLineItemId: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function stagedOrderSetCustomShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRate: shippingRateDraftType().required(),
             action: Joi.string().required().only('setCustomShippingMethod'),
             shippingMethodName: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function stagedOrderSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function stagedOrderSetCustomerEmailActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerEmail'),
             email: Joi.string().optional()
          })
}

export function stagedOrderSetCustomerGroupActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerGroup'),
             customerGroup: customerGroupResourceIdentifierType().optional()
          })
}

export function stagedOrderSetCustomerIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerId'),
             customerId: Joi.string().optional()
          })
}

export function stagedOrderSetDeliveryAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDeliveryAddress'),
             deliveryId: Joi.string().required(),
             address: addressType().optional()
          })
}

export function stagedOrderSetDeliveryItemsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             action: Joi.string().required().only('setDeliveryItems'),
             deliveryId: Joi.string().required()
          })
}

export function stagedOrderSetLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomField'),
             lineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function stagedOrderSetLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomType'),
             lineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function stagedOrderSetLineItemPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemPrice'),
             lineItemId: Joi.string().required(),
             externalPrice: moneyType().optional()
          })
}

export function stagedOrderSetLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemShippingDetails'),
             lineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function stagedOrderSetLineItemTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTaxAmount'),
             lineItemId: Joi.string().required(),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function stagedOrderSetLineItemTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTaxRate'),
             lineItemId: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function stagedOrderSetLineItemTotalPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTotalPrice'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional()
          })
}

export function stagedOrderSetLocaleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLocale'),
             locale: Joi.string().optional()
          })
}

export function stagedOrderSetOrderNumberActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setOrderNumber'),
             orderNumber: Joi.string().optional()
          })
}

export function stagedOrderSetOrderTotalTaxActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             externalTotalGross: moneyType().required(),
             action: Joi.string().required().only('setOrderTotalTax'),
             externalTaxPortions: Joi.array().items(taxPortionType()).optional()
          })
}

export function stagedOrderSetParcelItemsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             action: Joi.string().required().only('setParcelItems'),
             parcelId: Joi.string().required()
          })
}

export function stagedOrderSetParcelMeasurementsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setParcelMeasurements'),
             parcelId: Joi.string().required(),
             measurements: parcelMeasurementsType().optional()
          })
}

export function stagedOrderSetParcelTrackingDataActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setParcelTrackingData'),
             parcelId: Joi.string().required(),
             trackingData: trackingDataType().optional()
          })
}

export function stagedOrderSetReturnPaymentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             paymentState: returnPaymentStateType().required(),
             action: Joi.string().required().only('setReturnPaymentState'),
             returnItemId: Joi.string().required()
          })
}

export function stagedOrderSetReturnShipmentStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shipmentState: returnShipmentStateType().required(),
             action: Joi.string().required().only('setReturnShipmentState'),
             returnItemId: Joi.string().required()
          })
}

export function stagedOrderSetShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingAddress'),
             address: addressType().optional()
          })
}

export function stagedOrderSetShippingAddressAndCustomShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             shippingRate: shippingRateDraftType().required(),
             action: Joi.string().required().only('setShippingAddressAndCustomShippingMethod'),
             shippingMethodName: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function stagedOrderSetShippingAddressAndShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('setShippingAddressAndShippingMethod'),
             externalTaxRate: externalTaxRateDraftType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional()
          })
}

export function stagedOrderSetShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethod'),
             externalTaxRate: externalTaxRateDraftType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional()
          })
}

export function stagedOrderSetShippingMethodTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethodTaxAmount'),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function stagedOrderSetShippingMethodTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethodTaxRate'),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function stagedOrderSetShippingRateInputActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingRateInput'),
             shippingRateInput: shippingRateInputDraftType().optional()
          })
}

export function stagedOrderTransitionCustomLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateResourceIdentifierType().required(),
             toState: stateResourceIdentifierType().required(),
             quantity: Joi.number().required(),
             action: Joi.string().required().only('transitionCustomLineItemState'),
             customLineItemId: Joi.string().required(),
             actualTransitionDate: Joi.date().iso().optional()
          })
}

export function stagedOrderTransitionLineItemStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateResourceIdentifierType().required(),
             toState: stateResourceIdentifierType().required(),
             quantity: Joi.number().required(),
             action: Joi.string().required().only('transitionLineItemState'),
             lineItemId: Joi.string().required(),
             actualTransitionDate: Joi.date().iso().optional()
          })
}

export function stagedOrderTransitionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateResourceIdentifierType().required(),
             action: Joi.string().required().only('transitionState'),
             force: Joi.boolean().optional()
          })
}

export function stagedOrderUpdateItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('updateItemShippingAddress')
          })
}

export function stagedOrderUpdateSyncInfoActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             channel: channelResourceIdentifierType().required(),
             action: Joi.string().required().only('updateSyncInfo'),
             syncedAt: Joi.date().iso().optional(),
             externalId: Joi.string().optional()
          })
}