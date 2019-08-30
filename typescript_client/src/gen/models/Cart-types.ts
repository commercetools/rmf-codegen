/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { typedMoneyType } from './Common-types'
import { cartDiscountReferenceType } from './CartDiscount-types'
import { addressType } from './Common-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { customFieldsType } from './Type-types'
import { storeKeyReferenceType } from './Store-types'
import { paymentInfoType } from './Order-types'
import { loggedResourceType } from './Common-types'
import { customerGroupResourceIdentifierType } from './CustomerGroup-types'
import { customFieldsDraftType } from './Type-types'
import { shippingMethodResourceIdentifierType } from './ShippingMethod-types'
import { storeResourceIdentifierType } from './Store-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { localizedStringType } from './Common-types'
import { taxRateType } from './TaxCategory-types'
import { itemStateType } from './Order-types'
import { taxCategoryReferenceType } from './TaxCategory-types'
import { moneyType } from './Common-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { discountCodeReferenceType } from './DiscountCode-types'
import { subRateType } from './TaxCategory-types'
import { priceType } from './Common-types'
import { productVariantType } from './Product-types'
import { channelReferenceType } from './Channel-types'
import { productTypeReferenceType } from './ProductType-types'
import { channelResourceIdentifierType } from './Channel-types'
import { shippingRateType } from './ShippingMethod-types'
import { shippingMethodReferenceType } from './ShippingMethod-types'
import { deliveryType } from './Order-types'
import { paymentResourceIdentifierType } from './Payment-types'
import { shoppingListResourceIdentifierType } from './ShoppingList-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'
import { shippingRateDraftType } from './ShippingMethod-types'

export function cartType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customLineItems: Joi.array().items(customLineItemType()).required(),
             lineItems: Joi.array().items(lineItemType()).required(),
             refusedGifts: Joi.array().items(cartDiscountReferenceType()).required(),
             origin: cartOriginType().required(),
             cartState: cartStateType().required(),
             taxRoundingMode: roundingModeType().required(),
             taxCalculationMode: taxCalculationModeType().required(),
             taxMode: taxModeType().required(),
             totalPrice: typedMoneyType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             discountCodes: Joi.array().items(discountCodeInfoType()).optional(),
             itemShippingAddresses: Joi.array().items(addressType()).optional(),
             billingAddress: addressType().optional(),
             shippingAddress: addressType().optional(),
             country: Joi.string().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             inventoryMode: inventoryModeType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             paymentInfo: paymentInfoType().optional(),
             shippingInfo: shippingInfoType().optional(),
             shippingRateInput: shippingRateInputType().optional(),
             store: storeKeyReferenceType().optional(),
             taxedPrice: taxedPriceType().optional(),
             deleteDaysAfterLastModification: Joi.number().optional(),
             anonymousId: Joi.string().optional(),
             customerEmail: Joi.string().optional(),
             customerId: Joi.string().optional(),
             locale: Joi.string().optional()
          })
}

export function cartDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currency: Joi.string().required(),
             customLineItems: Joi.array().items(customLineItemDraftType()).optional(),
             itemShippingAddresses: Joi.array().items(addressType()).optional(),
             lineItems: Joi.array().items(lineItemDraftType()).optional(),
             billingAddress: addressType().optional(),
             shippingAddress: addressType().optional(),
             origin: cartOriginType().optional(),
             custom: customFieldsDraftType().optional(),
             customerGroup: customerGroupResourceIdentifierType().optional(),
             externalTaxRateForShippingMethod: externalTaxRateDraftType().optional(),
             inventoryMode: inventoryModeType().optional(),
             taxRoundingMode: roundingModeType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional(),
             shippingRateInput: shippingRateInputDraftType().optional(),
             store: storeResourceIdentifierType().optional(),
             taxCalculationMode: taxCalculationModeType().optional(),
             taxMode: taxModeType().optional(),
             deleteDaysAfterLastModification: Joi.number().optional(),
             anonymousId: Joi.string().optional(),
             country: Joi.string().optional(),
             customerEmail: Joi.string().optional(),
             customerId: Joi.string().optional(),
             locale: Joi.string().optional()
          })
}

const cartOriginTypeValues = [

   'Customer',
   'Merchant'

]

export function cartOriginType(): Joi.AnySchema {
   return Joi.string().only(cartOriginTypeValues)
}

export function cartPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(cartType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function cartReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('cart'),
             id: Joi.string().required(),
             obj: cartType().optional()
          })
}

export function cartResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('cart'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

const cartStateTypeValues = [

   'Active',
   'Merged',
   'Ordered'

]

export function cartStateType(): Joi.AnySchema {
   return Joi.string().only(cartStateTypeValues)
}

export function cartUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(cartUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function cartUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([cartAddCustomLineItemActionType(), cartAddDiscountCodeActionType(), cartAddItemShippingAddressActionType(), cartAddLineItemActionType(), cartAddPaymentActionType(), cartAddShoppingListActionType(), cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType(), cartApplyDeltaToLineItemShippingDetailsTargetsActionType(), cartChangeCustomLineItemMoneyActionType(), cartChangeCustomLineItemQuantityActionType(), cartChangeLineItemQuantityActionType(), cartChangeTaxCalculationModeActionType(), cartChangeTaxModeActionType(), cartChangeTaxRoundingModeActionType(), cartRecalculateActionType(), cartRemoveCustomLineItemActionType(), cartRemoveDiscountCodeActionType(), cartRemoveItemShippingAddressActionType(), cartRemoveLineItemActionType(), cartRemovePaymentActionType(), cartSetAnonymousIdActionType(), cartSetBillingAddressActionType(), cartSetCartTotalTaxActionType(), cartSetCountryActionType(), cartSetCustomFieldActionType(), cartSetCustomLineItemCustomFieldActionType(), cartSetCustomLineItemCustomTypeActionType(), cartSetCustomLineItemShippingDetailsActionType(), cartSetCustomLineItemTaxAmountActionType(), cartSetCustomLineItemTaxRateActionType(), cartSetCustomShippingMethodActionType(), cartSetCustomTypeActionType(), cartSetCustomerEmailActionType(), cartSetCustomerGroupActionType(), cartSetCustomerIdActionType(), cartSetDeleteDaysAfterLastModificationActionType(), cartSetLineItemCustomFieldActionType(), cartSetLineItemCustomTypeActionType(), cartSetLineItemPriceActionType(), cartSetLineItemShippingDetailsActionType(), cartSetLineItemTaxAmountActionType(), cartSetLineItemTaxRateActionType(), cartSetLineItemTotalPriceActionType(), cartSetLocaleActionType(), cartSetShippingAddressActionType(), cartSetShippingMethodActionType(), cartSetShippingMethodTaxAmountActionType(), cartSetShippingMethodTaxRateActionType(), cartSetShippingRateInputActionType(), cartUpdateItemShippingAddressActionType()])
}

export function classificationShippingRateInputType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             key: Joi.string().required(),
             type: Joi.string().required().only('Classification')
          })
}

export function classificationShippingRateInputDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             key: Joi.string().required(),
             type: Joi.string().required().only('Classification')
          })
}

export function customLineItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             state: Joi.array().items(itemStateType()).required(),
             name: localizedStringType().required(),
             money: typedMoneyType().required(),
             totalPrice: typedMoneyType().required(),
             quantity: Joi.number().required(),
             id: Joi.string().required(),
             slug: Joi.string().required(),
             custom: customFieldsType().optional(),
             shippingDetails: itemShippingDetailsType().optional(),
             taxCategory: taxCategoryReferenceType().optional(),
             taxRate: taxRateType().optional(),
             taxedPrice: taxedItemPriceType().optional()
          })
}

export function customLineItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             money: moneyType().required(),
             quantity: Joi.number().required(),
             slug: Joi.string().required(),
             custom: customFieldsType().optional(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             shippingDetails: itemShippingDetailsDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function discountCodeInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             state: discountCodeStateType().required()
          })
}

const discountCodeStateTypeValues = [

   'NotActive',
   'DoesNotMatchCart',
   'MatchesCart',
   'MaxApplicationReached'

]

export function discountCodeStateType(): Joi.AnySchema {
   return Joi.string().only(discountCodeStateTypeValues)
}

export function discountedLineItemPortionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discount: cartDiscountReferenceType().required(),
             discountedAmount: moneyType().required()
          })
}

export function discountedLineItemPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             includedDiscounts: Joi.array().items(discountedLineItemPortionType()).required(),
             value: typedMoneyType().required()
          })
}

export function discountedLineItemPriceForQuantityType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPrice: discountedLineItemPriceType().required(),
             quantity: Joi.number().required()
          })
}

export function externalLineItemTotalPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: moneyType().required(),
             totalPrice: moneyType().required()
          })
}

export function externalTaxAmountDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxRate: externalTaxRateDraftType().required(),
             totalGross: moneyType().required()
          })
}

export function externalTaxRateDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             country: Joi.string().required(),
             name: Joi.string().required(),
             subRates: Joi.array().items(subRateType()).optional(),
             includedInPrice: Joi.boolean().optional(),
             amount: Joi.number().optional(),
             state: Joi.string().optional()
          })
}

const inventoryModeTypeValues = [

   'TrackOnly',
   'ReserveOnOrder',
   'None'

]

export function inventoryModeType(): Joi.AnySchema {
   return Joi.string().only(inventoryModeTypeValues)
}

export function itemShippingDetailsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             targets: Joi.array().items(itemShippingTargetType()).required(),
             valid: Joi.boolean().required()
          })
}

export function itemShippingDetailsDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             targets: Joi.array().items(itemShippingTargetType()).required()
          })
}

export function itemShippingTargetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             addressKey: Joi.string().required()
          })
}

export function lineItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
             state: Joi.array().items(itemStateType()).required(),
             lineItemMode: lineItemModeType().required(),
             priceMode: lineItemPriceModeType().required(),
             name: localizedStringType().required(),
             totalPrice: moneyType().required(),
             price: priceType().required(),
             productType: productTypeReferenceType().required(),
             variant: productVariantType().required(),
             quantity: Joi.number().required(),
             id: Joi.string().required(),
             productId: Joi.string().required(),
             distributionChannel: channelReferenceType().optional(),
             supplyChannel: channelReferenceType().optional(),
             custom: customFieldsType().optional(),
             shippingDetails: itemShippingDetailsType().optional(),
             productSlug: localizedStringType().optional(),
             taxRate: taxRateType().optional(),
             taxedPrice: taxedItemPriceType().optional()
          })
}

export function lineItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
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

const lineItemModeTypeValues = [

   'Standard',
   'GiftLineItem'

]

export function lineItemModeType(): Joi.AnySchema {
   return Joi.string().only(lineItemModeTypeValues)
}

const lineItemPriceModeTypeValues = [

   'Platform',
   'ExternalTotal',
   'ExternalPrice'

]

export function lineItemPriceModeType(): Joi.AnySchema {
   return Joi.string().only(lineItemPriceModeTypeValues)
}

export function replicaCartDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             reference: Joi.any().required()
          })
}

const roundingModeTypeValues = [

   'HalfEven',
   'HalfUp',
   'HalfDown'

]

export function roundingModeType(): Joi.AnySchema {
   return Joi.string().only(roundingModeTypeValues)
}

export function scoreShippingRateInputType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             score: Joi.number().required(),
             type: Joi.string().required().only('Score')
          })
}

export function scoreShippingRateInputDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             score: Joi.number().required(),
             type: Joi.string().required().only('Score')
          })
}

export function shippingInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingMethodState: shippingMethodStateType().required(),
             shippingRate: shippingRateType().required(),
             price: typedMoneyType().required(),
             shippingMethodName: Joi.string().required(),
             deliveries: Joi.array().items(deliveryType()).optional(),
             discountedPrice: discountedLineItemPriceType().optional(),
             shippingMethod: shippingMethodReferenceType().optional(),
             taxCategory: taxCategoryReferenceType().optional(),
             taxRate: taxRateType().optional(),
             taxedPrice: taxedItemPriceType().optional()
          })
}

const shippingMethodStateTypeValues = [

   'DoesNotMatchCart',
   'MatchesCart'

]

export function shippingMethodStateType(): Joi.AnySchema {
   return Joi.string().only(shippingMethodStateTypeValues)
}

export function shippingRateInputType(): Joi.AnySchema {
   return Joi.alternatives([classificationShippingRateInputType(), scoreShippingRateInputType()])
}

export function shippingRateInputDraftType(): Joi.AnySchema {
   return Joi.alternatives([classificationShippingRateInputDraftType(), scoreShippingRateInputDraftType()])
}

const taxCalculationModeTypeValues = [

   'LineItemLevel',
   'UnitPriceLevel'

]

export function taxCalculationModeType(): Joi.AnySchema {
   return Joi.string().only(taxCalculationModeTypeValues)
}

const taxModeTypeValues = [

   'Platform',
   'External',
   'ExternalAmount',
   'Disabled'

]

export function taxModeType(): Joi.AnySchema {
   return Joi.string().only(taxModeTypeValues)
}

export function taxPortionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             amount: moneyType().required(),
             rate: Joi.number().required(),
             name: Joi.string().optional()
          })
}

export function taxedItemPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             totalGross: typedMoneyType().required(),
             totalNet: typedMoneyType().required()
          })
}

export function taxedPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxPortions: Joi.array().items(taxPortionType()).required(),
             totalGross: moneyType().required(),
             totalNet: moneyType().required()
          })
}

export function cartAddCustomLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             money: moneyType().required(),
             quantity: Joi.number().required(),
             action: Joi.string().required().only('addCustomLineItem'),
             slug: Joi.string().required(),
             custom: customFieldsDraftType().optional(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function cartAddDiscountCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addDiscountCode'),
             code: Joi.string().required()
          })
}

export function cartAddItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('addItemShippingAddress')
          })
}

export function cartAddLineItemActionType(): Joi.AnySchema {
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

export function cartAddPaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('addPayment')
          })
}

export function cartAddShoppingListActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shoppingList: shoppingListResourceIdentifierType().required(),
             action: Joi.string().required().only('addShoppingList'),
             distributionChannel: channelResourceIdentifierType().optional(),
             supplyChannel: channelResourceIdentifierType().optional()
          })
}

export function cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             targetsDelta: Joi.array().items(itemShippingTargetType()).required(),
             action: Joi.string().required().only('applyDeltaToCustomLineItemShippingDetailsTargets'),
             customLineItemId: Joi.string().required()
          })
}

export function cartApplyDeltaToLineItemShippingDetailsTargetsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             targetsDelta: Joi.array().items(itemShippingTargetType()).required(),
             action: Joi.string().required().only('applyDeltaToLineItemShippingDetailsTargets'),
             lineItemId: Joi.string().required()
          })
}

export function cartChangeCustomLineItemMoneyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             money: moneyType().required(),
             action: Joi.string().required().only('changeCustomLineItemMoney'),
             customLineItemId: Joi.string().required()
          })
}

export function cartChangeCustomLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeCustomLineItemQuantity'),
             customLineItemId: Joi.string().required()
          })
}

export function cartChangeLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeLineItemQuantity'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional(),
             externalPrice: moneyType().optional()
          })
}

export function cartChangeTaxCalculationModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxCalculationMode: taxCalculationModeType().required(),
             action: Joi.string().required().only('changeTaxCalculationMode')
          })
}

export function cartChangeTaxModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxMode: taxModeType().required(),
             action: Joi.string().required().only('changeTaxMode')
          })
}

export function cartChangeTaxRoundingModeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             taxRoundingMode: roundingModeType().required(),
             action: Joi.string().required().only('changeTaxRoundingMode')
          })
}

export function cartRecalculateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('recalculate'),
             updateProductData: Joi.boolean().optional()
          })
}

export function cartRemoveCustomLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeCustomLineItem'),
             customLineItemId: Joi.string().required()
          })
}

export function cartRemoveDiscountCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             discountCode: discountCodeReferenceType().required(),
             action: Joi.string().required().only('removeDiscountCode')
          })
}

export function cartRemoveItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeItemShippingAddress'),
             addressKey: Joi.string().required()
          })
}

export function cartRemoveLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeLineItem'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional(),
             shippingDetailsToRemove: itemShippingDetailsDraftType().optional(),
             externalPrice: moneyType().optional(),
             quantity: Joi.number().optional()
          })
}

export function cartRemovePaymentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payment: paymentResourceIdentifierType().required(),
             action: Joi.string().required().only('removePayment')
          })
}

export function cartSetAnonymousIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAnonymousId'),
             anonymousId: Joi.string().optional()
          })
}

export function cartSetBillingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setBillingAddress'),
             address: addressType().optional()
          })
}

export function cartSetCartTotalTaxActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             externalTotalGross: moneyType().required(),
             action: Joi.string().required().only('setCartTotalTax'),
             externalTaxPortions: Joi.array().items(taxPortionType()).optional()
          })
}

export function cartSetCountryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCountry'),
             country: Joi.string().optional()
          })
}

export function cartSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function cartSetCustomLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomField'),
             customLineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function cartSetCustomLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemCustomType'),
             customLineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function cartSetCustomLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemShippingDetails'),
             customLineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function cartSetCustomLineItemTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemTaxAmount'),
             customLineItemId: Joi.string().required(),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function cartSetCustomLineItemTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomLineItemTaxRate'),
             customLineItemId: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function cartSetCustomShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             shippingRate: shippingRateDraftType().required(),
             action: Joi.string().required().only('setCustomShippingMethod'),
             shippingMethodName: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function cartSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function cartSetCustomerEmailActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerEmail'),
             email: Joi.string().required()
          })
}

export function cartSetCustomerGroupActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerGroup'),
             customerGroup: customerGroupResourceIdentifierType().optional()
          })
}

export function cartSetCustomerIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerId'),
             customerId: Joi.string().optional()
          })
}

export function cartSetDeleteDaysAfterLastModificationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDeleteDaysAfterLastModification'),
             deleteDaysAfterLastModification: Joi.number().optional()
          })
}

export function cartSetLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomField'),
             lineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function cartSetLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomType'),
             lineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function cartSetLineItemPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemPrice'),
             lineItemId: Joi.string().required(),
             externalPrice: moneyType().optional()
          })
}

export function cartSetLineItemShippingDetailsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemShippingDetails'),
             lineItemId: Joi.string().required(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function cartSetLineItemTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTaxAmount'),
             lineItemId: Joi.string().required(),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function cartSetLineItemTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTaxRate'),
             lineItemId: Joi.string().required(),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function cartSetLineItemTotalPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemTotalPrice'),
             lineItemId: Joi.string().required(),
             externalTotalPrice: externalLineItemTotalPriceType().optional()
          })
}

export function cartSetLocaleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLocale'),
             locale: Joi.string().optional()
          })
}

export function cartSetShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingAddress'),
             address: addressType().optional()
          })
}

export function cartSetShippingMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethod'),
             externalTaxRate: externalTaxRateDraftType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional()
          })
}

export function cartSetShippingMethodTaxAmountActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethodTaxAmount'),
             externalTaxAmount: externalTaxAmountDraftType().optional()
          })
}

export function cartSetShippingMethodTaxRateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingMethodTaxRate'),
             externalTaxRate: externalTaxRateDraftType().optional()
          })
}

export function cartSetShippingRateInputActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingRateInput'),
             shippingRateInput: shippingRateInputDraftType().optional()
          })
}

export function cartUpdateItemShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('updateItemShippingAddress')
          })
}

const productPublishScopeTypeValues = [

   'All',
   'Prices'

]

export function productPublishScopeType(): Joi.AnySchema {
   return Joi.string().only(productPublishScopeTypeValues)
}