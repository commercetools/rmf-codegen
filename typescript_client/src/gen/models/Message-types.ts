/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { referenceType } from './Common-types'
import { categoryType } from './Category-types'
import { localizedStringType } from './Common-types'
import { stateReferenceType } from './State-types'
import { addressType } from './Common-types'
import { customerType } from './Customer-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { deliveryType } from './Order-types'
import { deliveryItemType } from './Order-types'
import { channelReferenceType } from './Channel-types'
import { baseResourceType } from './Common-types'
import { orderType } from './Order-types'
import { taxedItemPriceType } from './Cart-types'
import { discountedLineItemPriceForQuantityType } from './Cart-types'
import { customerReferenceType } from './Customer-types'
import { discountCodeReferenceType } from './DiscountCode-types'
import { discountCodeStateType } from './Cart-types'
import { orderEditAppliedType } from './OrderEdit-types'
import { orderEditReferenceType } from './OrderEdit-types'
import { lineItemType } from './Cart-types'
import { moneyType } from './Common-types'
import { paymentStateType } from './Order-types'
import { returnInfoType } from './Order-types'
import { returnShipmentStateType } from './Order-types'
import { shipmentStateType } from './Order-types'
import { shippingInfoType } from './Cart-types'
import { shippingRateInputType } from './Cart-types'
import { orderStateType } from './Order-types'
import { parcelType } from './Order-types'
import { parcelMeasurementsType } from './Order-types'
import { trackingDataType } from './Order-types'
import { paymentType } from './Payment-types'
import { customFieldsType } from './Type-types'
import { transactionType } from './Payment-types'
import { transactionStateType } from './Payment-types'
import { productProjectionType } from './Product-types'
import { imageType } from './Common-types'
import { discountedPriceType } from './Common-types'
import { productPublishScopeType } from './Cart-types'
import { productVariantType } from './Product-types'
import { reviewType } from './Review-types'

export function categoryCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             category: categoryType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function categorySlugChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customLineItemStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             fromState: stateReferenceType().required(),
             toState: stateReferenceType().required(),
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
          })
}

export function customerAddressAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerAddressChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerAddressRemovedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerCompanyNameSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             companyName: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerDateOfBirthSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             dateOfBirth: Joi.date().iso().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerEmailChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             email: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerEmailVerifiedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function customerGroupSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customerGroup: customerGroupReferenceType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function deliveryAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function deliveryAddressSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             deliveryId: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             address: addressType().optional(),
             oldAddress: addressType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function deliveryItemsUpdatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             oldItems: Joi.array().items(deliveryItemType()).required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             deliveryId: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function deliveryRemovedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function inventoryEntryDeletedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             supplyChannel: channelReferenceType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             sku: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function lineItemStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             fromState: stateReferenceType().required(),
             toState: stateReferenceType().required(),
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
          })
}

export function messageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function messageConfigurationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             enabled: Joi.boolean().required(),
             deleteDaysAfterCreation: Joi.number().optional()
          })
}

export function messageConfigurationDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             enabled: Joi.boolean().required(),
             deleteDaysAfterCreation: Joi.number().required()
          })
}

export function messagePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(messageType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function orderBillingAddressSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             address: addressType().optional(),
             oldAddress: addressType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderCustomLineItemDiscountSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             customLineItemId: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             taxedPrice: taxedItemPriceType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderCustomerEmailSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
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
          })
}

export function orderCustomerSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             customerGroup: customerGroupReferenceType().optional(),
             oldCustomerGroup: customerGroupReferenceType().optional(),
             customer: customerReferenceType().optional(),
             oldCustomer: customerReferenceType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderDeletedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderDiscountCodeAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderDiscountCodeRemovedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderDiscountCodeStateSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             state: discountCodeStateType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             oldState: discountCodeStateType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderEditAppliedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             result: orderEditAppliedType().required(),
             edit: orderEditReferenceType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderImportedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderLineItemAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             lineItem: lineItemType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             addedQuantity: Joi.number().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderLineItemDiscountSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             totalPrice: moneyType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             lineItemId: Joi.string().required(),
             type: Joi.string().required(),
             taxedPrice: taxedItemPriceType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderPaymentStateChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             oldPaymentState: paymentStateType().required(),
             paymentState: paymentStateType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderReturnInfoAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             returnInfo: returnInfoType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderReturnShipmentStateChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             returnShipmentState: returnShipmentStateType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             returnItemId: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderShipmentStateChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             oldShipmentState: shipmentStateType().required(),
             shipmentState: shipmentStateType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderShippingAddressSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             address: addressType().optional(),
             oldAddress: addressType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderShippingInfoSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             oldShippingInfo: shippingInfoType().optional(),
             shippingInfo: shippingInfoType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderShippingRateInputSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             oldShippingRateInput: shippingRateInputType().optional(),
             shippingRateInput: shippingRateInputType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderStateChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             oldOrderState: orderStateType().required(),
             orderState: orderStateType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function orderStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function parcelAddedToDeliveryMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             parcel: parcelType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function parcelItemsUpdatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             oldItems: Joi.array().items(deliveryItemType()).required(),
             resource: referenceType().required(),
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
          })
}

export function parcelMeasurementsUpdatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             deliveryId: Joi.string().required(),
             id: Joi.string().required(),
             parcelId: Joi.string().required(),
             type: Joi.string().required(),
             measurements: parcelMeasurementsType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function parcelRemovedFromDeliveryMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             parcel: parcelType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             deliveryId: Joi.string().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function parcelTrackingDataUpdatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             deliveryId: Joi.string().required(),
             id: Joi.string().required(),
             parcelId: Joi.string().required(),
             type: Joi.string().required(),
             trackingData: trackingDataType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function paymentCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function paymentInteractionAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             interaction: customFieldsType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function paymentStatusInterfaceCodeSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
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
          })
}

export function paymentStatusStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function paymentTransactionAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             transaction: transactionType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function paymentTransactionStateChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             state: transactionStateType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             transactionId: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             productProjection: productProjectionType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productDeletedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currentProjection: productProjectionType().required(),
             resource: referenceType().required(),
             removedImageUrls: Joi.array().items(Joi.string()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productImageAddedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             image: imageType().required(),
             resource: referenceType().required(),
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
          })
}

export function productPriceDiscountsSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             updatedPrices: Joi.array().items(productPriceDiscountsSetUpdatedPriceType()).required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productPriceDiscountsSetUpdatedPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             staged: Joi.boolean().required(),
             variantId: Joi.number().required(),
             priceId: Joi.string().required(),
             discounted: discountedPriceType().optional(),
             sku: Joi.string().optional(),
             variantKey: Joi.string().optional()
          })
}

export function productPriceExternalDiscountSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
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
             discounted: discountedPriceType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
             sku: Joi.string().optional(),
             variantKey: Joi.string().optional()
          })
}

export function productPublishedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             productProjection: productProjectionType().required(),
             scope: productPublishScopeType().required(),
             resource: referenceType().required(),
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productRevertedStagedChangesMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productSlugChangedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productUnpublishedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function productVariantDeletedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variant: productVariantType().required(),
             resource: referenceType().required(),
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function reviewCreatedMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             review: reviewType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function reviewRatingSetMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             includedInStatistics: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             type: Joi.string().required(),
             target: referenceType().optional(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional(),
             newRating: Joi.number().optional(),
             oldRating: Joi.number().optional()
          })
}

export function reviewStateTransitionMessageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             target: referenceType().required(),
             newState: stateReferenceType().required(),
             oldState: stateReferenceType().required(),
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
          })
}

export function userProvidedIdentifiersType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().optional(),
             customerNumber: Joi.string().optional(),
             externalId: Joi.string().optional(),
             key: Joi.string().optional(),
             orderNumber: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function categoryCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             category: categoryType().required(),
             type: Joi.string().required().only('CategoryCreated')
          })
}

export function categorySlugChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             type: Joi.string().required().only('CategorySlugChanged')
          })
}

export function customLineItemStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateReferenceType().required(),
             toState: stateReferenceType().required(),
             transitionDate: Joi.date().iso().required(),
             quantity: Joi.number().required(),
             customLineItemId: Joi.string().required(),
             type: Joi.string().required().only('CustomLineItemStateTransition')
          })
}

export function customerAddressAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             type: Joi.string().required().only('CustomerAddressAdded')
          })
}

export function customerAddressChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             type: Joi.string().required().only('CustomerAddressChanged')
          })
}

export function customerAddressRemovedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             type: Joi.string().required().only('CustomerAddressRemoved')
          })
}

export function customerCompanyNameSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             companyName: Joi.string().required(),
             type: Joi.string().required().only('CustomerCompanyNameSet')
          })
}

export function customerCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerType().required(),
             type: Joi.string().required().only('CustomerCreated')
          })
}

export function customerDateOfBirthSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             dateOfBirth: Joi.date().iso().required(),
             type: Joi.string().required().only('CustomerDateOfBirthSet')
          })
}

export function customerEmailChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             email: Joi.string().required(),
             type: Joi.string().required().only('CustomerEmailChanged')
          })
}

export function customerEmailVerifiedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('CustomerEmailVerified')
          })
}

export function customerGroupSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customerGroup: customerGroupReferenceType().required(),
             type: Joi.string().required().only('CustomerGroupSet')
          })
}

export function deliveryAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             type: Joi.string().required().only('DeliveryAdded')
          })
}

export function deliveryAddressSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             deliveryId: Joi.string().required(),
             type: Joi.string().required().only('DeliveryAddressSet'),
             address: addressType().optional(),
             oldAddress: addressType().optional()
          })
}

export function deliveryItemsUpdatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             oldItems: Joi.array().items(deliveryItemType()).required(),
             deliveryId: Joi.string().required(),
             type: Joi.string().required().only('DeliveryItemsUpdated')
          })
}

export function deliveryRemovedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             type: Joi.string().required().only('DeliveryRemoved')
          })
}

export function inventoryEntryDeletedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             supplyChannel: channelReferenceType().required(),
             sku: Joi.string().required(),
             type: Joi.string().required().only('InventoryEntryDeleted')
          })
}

export function lineItemStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fromState: stateReferenceType().required(),
             toState: stateReferenceType().required(),
             transitionDate: Joi.date().iso().required(),
             quantity: Joi.number().required(),
             lineItemId: Joi.string().required(),
             type: Joi.string().required().only('LineItemStateTransition')
          })
}

export function messagePayloadType(): Joi.AnySchema {
   return Joi.alternatives([orderBillingAddressSetMessagePayloadType(), orderCreatedMessagePayloadType(), orderCustomLineItemDiscountSetMessagePayloadType(), orderCustomerEmailSetMessagePayloadType(), orderCustomerSetMessagePayloadType(), orderDeletedMessagePayloadType(), orderDiscountCodeAddedMessagePayloadType(), orderDiscountCodeRemovedMessagePayloadType(), orderDiscountCodeStateSetMessagePayloadType(), orderEditAppliedMessagePayloadType(), orderImportedMessagePayloadType(), orderLineItemAddedMessagePayloadType(), orderLineItemDiscountSetMessagePayloadType(), orderPaymentStateChangedMessagePayloadType(), orderReturnInfoAddedMessagePayloadType(), orderReturnShipmentStateChangedMessagePayloadType(), orderShipmentStateChangedMessagePayloadType(), orderShippingAddressSetMessagePayloadType(), orderShippingInfoSetMessagePayloadType(), orderShippingRateInputSetMessagePayloadType(), orderStateChangedMessagePayloadType(), orderStateTransitionMessagePayloadType(), parcelAddedToDeliveryMessagePayloadType(), parcelItemsUpdatedMessagePayloadType(), parcelMeasurementsUpdatedMessagePayloadType(), parcelRemovedFromDeliveryMessagePayloadType(), parcelTrackingDataUpdatedMessagePayloadType(), paymentCreatedMessagePayloadType(), paymentInteractionAddedMessagePayloadType(), paymentStatusInterfaceCodeSetMessagePayloadType(), paymentStatusStateTransitionMessagePayloadType(), paymentTransactionAddedMessagePayloadType(), paymentTransactionStateChangedMessagePayloadType(), productCreatedMessagePayloadType(), productDeletedMessagePayloadType(), productImageAddedMessagePayloadType(), productPriceDiscountsSetMessagePayloadType(), productPriceExternalDiscountSetMessagePayloadType(), productPublishedMessagePayloadType(), productRevertedStagedChangesMessagePayloadType(), productSlugChangedMessagePayloadType(), productStateTransitionMessagePayloadType(), productUnpublishedMessagePayloadType(), productVariantDeletedMessagePayloadType(), reviewCreatedMessagePayloadType(), reviewRatingSetMessagePayloadType(), reviewStateTransitionMessagePayloadType(), categoryCreatedMessagePayloadType(), categorySlugChangedMessagePayloadType(), customLineItemStateTransitionMessagePayloadType(), customerAddressAddedMessagePayloadType(), customerAddressChangedMessagePayloadType(), customerAddressRemovedMessagePayloadType(), customerCompanyNameSetMessagePayloadType(), customerCreatedMessagePayloadType(), customerDateOfBirthSetMessagePayloadType(), customerEmailChangedMessagePayloadType(), customerEmailVerifiedMessagePayloadType(), customerGroupSetMessagePayloadType(), deliveryAddedMessagePayloadType(), deliveryAddressSetMessagePayloadType(), deliveryItemsUpdatedMessagePayloadType(), deliveryRemovedMessagePayloadType(), inventoryEntryDeletedMessagePayloadType(), lineItemStateTransitionMessagePayloadType()])
}

export function orderBillingAddressSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderBillingAddressSet'),
             address: addressType().optional(),
             oldAddress: addressType().optional()
          })
}

export function orderCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             type: Joi.string().required().only('OrderCreated')
          })
}

export function orderCustomLineItemDiscountSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             customLineItemId: Joi.string().required(),
             type: Joi.string().required().only('OrderCustomLineItemDiscountSet'),
             taxedPrice: taxedItemPriceType().optional()
          })
}

export function orderCustomerEmailSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderCustomerEmailSet'),
             email: Joi.string().optional(),
             oldEmail: Joi.string().optional()
          })
}

export function orderCustomerSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderCustomerSet'),
             customerGroup: customerGroupReferenceType().optional(),
             oldCustomerGroup: customerGroupReferenceType().optional(),
             customer: customerReferenceType().optional(),
             oldCustomer: customerReferenceType().optional()
          })
}

export function orderDeletedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             type: Joi.string().required().only('OrderDeleted')
          })
}

export function orderDiscountCodeAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             type: Joi.string().required().only('OrderDiscountCodeAdded')
          })
}

export function orderDiscountCodeRemovedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             type: Joi.string().required().only('OrderDiscountCodeRemoved')
          })
}

export function orderDiscountCodeStateSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             state: discountCodeStateType().required(),
             type: Joi.string().required().only('OrderDiscountCodeStateSet'),
             oldState: discountCodeStateType().optional()
          })
}

export function orderEditAppliedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             result: orderEditAppliedType().required(),
             edit: orderEditReferenceType().required(),
             type: Joi.string().required().only('OrderEditApplied')
          })
}

export function orderImportedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             order: orderType().required(),
             type: Joi.string().required().only('OrderImported')
          })
}

export function orderLineItemAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             lineItem: lineItemType().required(),
             addedQuantity: Joi.number().required(),
             type: Joi.string().required().only('OrderLineItemAdded')
          })
}

export function orderLineItemDiscountSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             totalPrice: moneyType().required(),
             lineItemId: Joi.string().required(),
             type: Joi.string().required().only('OrderLineItemDiscountSet'),
             taxedPrice: taxedItemPriceType().optional()
          })
}

export function orderPaymentStateChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             oldPaymentState: paymentStateType().required(),
             paymentState: paymentStateType().required(),
             type: Joi.string().required().only('OrderPaymentStateChanged')
          })
}

export function orderReturnInfoAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             returnInfo: returnInfoType().required(),
             type: Joi.string().required().only('ReturnInfoAdded')
          })
}

export function orderReturnShipmentStateChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             returnShipmentState: returnShipmentStateType().required(),
             returnItemId: Joi.string().required(),
             type: Joi.string().required().only('OrderReturnShipmentStateChanged')
          })
}

export function orderShipmentStateChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             oldShipmentState: shipmentStateType().required(),
             shipmentState: shipmentStateType().required(),
             type: Joi.string().required().only('OrderShipmentStateChanged')
          })
}

export function orderShippingAddressSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderShippingAddressSet'),
             address: addressType().optional(),
             oldAddress: addressType().optional()
          })
}

export function orderShippingInfoSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderShippingInfoSet'),
             oldShippingInfo: shippingInfoType().optional(),
             shippingInfo: shippingInfoType().optional()
          })
}

export function orderShippingRateInputSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('OrderShippingRateInputSet'),
             oldShippingRateInput: shippingRateInputType().optional(),
             shippingRateInput: shippingRateInputType().optional()
          })
}

export function orderStateChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             oldOrderState: orderStateType().required(),
             orderState: orderStateType().required(),
             type: Joi.string().required().only('OrderStateChanged')
          })
}

export function orderStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             type: Joi.string().required().only('OrderStateTransition')
          })
}

export function parcelAddedToDeliveryMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             delivery: deliveryType().required(),
             parcel: parcelType().required(),
             type: Joi.string().required().only('ParcelAddedToDelivery')
          })
}

export function parcelItemsUpdatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             items: Joi.array().items(deliveryItemType()).required(),
             oldItems: Joi.array().items(deliveryItemType()).required(),
             parcelId: Joi.string().required(),
             type: Joi.string().required().only('ParcelItemsUpdated'),
             deliveryId: Joi.string().optional()
          })
}

export function parcelMeasurementsUpdatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             deliveryId: Joi.string().required(),
             parcelId: Joi.string().required(),
             type: Joi.string().required().only('ParcelMeasurementsUpdated'),
             measurements: parcelMeasurementsType().optional()
          })
}

export function parcelRemovedFromDeliveryMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             parcel: parcelType().required(),
             deliveryId: Joi.string().required(),
             type: Joi.string().required().only('ParcelRemovedFromDelivery')
          })
}

export function parcelTrackingDataUpdatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             deliveryId: Joi.string().required(),
             parcelId: Joi.string().required(),
             type: Joi.string().required().only('ParcelTrackingDataUpdated'),
             trackingData: trackingDataType().optional()
          })
}

export function paymentCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentType().required(),
             type: Joi.string().required().only('PaymentCreated')
          })
}

export function paymentInteractionAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             interaction: customFieldsType().required(),
             type: Joi.string().required().only('PaymentInteractionAdded')
          })
}

export function paymentStatusInterfaceCodeSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             interfaceCode: Joi.string().required(),
             paymentId: Joi.string().required(),
             type: Joi.string().required().only('PaymentStatusInterfaceCodeSet')
          })
}

export function paymentStatusStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             type: Joi.string().required().only('PaymentStatusStateTransition')
          })
}

export function paymentTransactionAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             transaction: transactionType().required(),
             type: Joi.string().required().only('PaymentTransactionAdded')
          })
}

export function paymentTransactionStateChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: transactionStateType().required(),
             transactionId: Joi.string().required(),
             type: Joi.string().required().only('PaymentTransactionStateChanged')
          })
}

export function productCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             productProjection: productProjectionType().required(),
             type: Joi.string().required().only('ProductCreated')
          })
}

export function productDeletedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currentProjection: productProjectionType().required(),
             removedImageUrls: Joi.array().items(Joi.string()).required(),
             type: Joi.string().required().only('ProductDeleted')
          })
}

export function productImageAddedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             image: imageType().required(),
             staged: Joi.boolean().required(),
             variantId: Joi.number().required(),
             type: Joi.string().required().only('ProductImageAdded')
          })
}

export function productPriceDiscountsSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             updatedPrices: Joi.array().items(productPriceDiscountsSetUpdatedPriceType()).required(),
             type: Joi.string().required().only('ProductPriceDiscountsSet')
          })
}

export function productPriceExternalDiscountSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             staged: Joi.boolean().required(),
             variantId: Joi.number().required(),
             priceId: Joi.string().required(),
             type: Joi.string().required().only('ProductPriceExternalDiscountSet'),
             discounted: discountedPriceType().optional(),
             sku: Joi.string().optional(),
             variantKey: Joi.string().optional()
          })
}

export function productPublishedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             productProjection: productProjectionType().required(),
             scope: productPublishScopeType().required(),
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             type: Joi.string().required().only('ProductPublished')
          })
}

export function productRevertedStagedChangesMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             type: Joi.string().required().only('ProductRevertedStagedChanges')
          })
}

export function productSlugChangedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             type: Joi.string().required().only('ProductSlugChanged')
          })
}

export function productStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateReferenceType().required(),
             force: Joi.boolean().required(),
             type: Joi.string().required().only('ProductStateTransition')
          })
}

export function productUnpublishedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('ProductUnpublished')
          })
}

export function productVariantDeletedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variant: productVariantType().required(),
             removedImageUrls: Joi.array().items(Joi.any()).required(),
             type: Joi.string().required().only('ProductVariantDeleted')
          })
}

export function reviewCreatedMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             review: reviewType().required(),
             type: Joi.string().required().only('ReviewCreated')
          })
}

export function reviewRatingSetMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             includedInStatistics: Joi.boolean().required(),
             type: Joi.string().required().only('ReviewRatingSet'),
             target: referenceType().optional(),
             newRating: Joi.number().optional(),
             oldRating: Joi.number().optional()
          })
}

export function reviewStateTransitionMessagePayloadType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             target: referenceType().required(),
             newState: stateReferenceType().required(),
             oldState: stateReferenceType().required(),
             force: Joi.boolean().required(),
             newIncludedInStatistics: Joi.boolean().required(),
             oldIncludedInStatistics: Joi.boolean().required(),
             type: Joi.string().required().only('ReviewStateTransition')
          })
}