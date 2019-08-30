/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { facetResultsType } from './Product-types'
import { customFieldsType } from './Type-types'
import { customFieldsDraftType } from './Type-types'
import { customObjectType } from './CustomObject-types'
import { messageType } from './Message-types'
import { productProjectionType } from './Product-types'
import { shippingMethodType } from './ShippingMethod-types'
import { storeType } from './Store-types'
import { zoneType } from './Zone-types'
import { customerReferenceType } from './Customer-types'
import { productDiscountReferenceType } from './ProductDiscount-types'
import { storeKeyReferenceType } from './Store-types'
import { customerGroupType } from './CustomerGroup-types'
import { customerType } from './Customer-types'
import { discountCodeType } from './DiscountCode-types'
import { extensionType } from './Extension-types'
import { inventoryEntryType } from './Inventory-types'
import { orderEditType } from './OrderEdit-types'
import { orderType } from './Order-types'
import { paymentType } from './Payment-types'
import { productDiscountType } from './ProductDiscount-types'
import { productTypeType } from './ProductType-types'
import { productType } from './Product-types'
import { reviewType } from './Review-types'
import { shoppingListType } from './ShoppingList-types'
import { stateType } from './State-types'
import { subscriptionType } from './Subscription-types'
import { taxCategoryType } from './TaxCategory-types'
import { typeType } from './Type-types'
import { cartDiscountType } from './CartDiscount-types'
import { cartType } from './Cart-types'
import { categoryType } from './Category-types'
import { channelType } from './Channel-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { channelReferenceType } from './Channel-types'
import { customerGroupResourceIdentifierType } from './CustomerGroup-types'
import { channelResourceIdentifierType } from './Channel-types'
import { customObjectReferenceType } from './CustomObject-types'
import { discountCodeReferenceType } from './DiscountCode-types'
import { inventoryEntryReferenceType } from './Inventory-types'
import { orderEditReferenceType } from './OrderEdit-types'
import { orderReferenceType } from './Order-types'
import { paymentReferenceType } from './Payment-types'
import { productTypeReferenceType } from './ProductType-types'
import { productReferenceType } from './Product-types'
import { reviewReferenceType } from './Review-types'
import { shippingMethodReferenceType } from './ShippingMethod-types'
import { shoppingListReferenceType } from './ShoppingList-types'
import { stateReferenceType } from './State-types'
import { storeReferenceType } from './Store-types'
import { taxCategoryReferenceType } from './TaxCategory-types'
import { typeReferenceType } from './Type-types'
import { zoneReferenceType } from './Zone-types'
import { cartDiscountReferenceType } from './CartDiscount-types'
import { cartReferenceType } from './Cart-types'
import { categoryReferenceType } from './Category-types'
import { customerResourceIdentifierType } from './Customer-types'
import { discountCodeResourceIdentifierType } from './DiscountCode-types'
import { inventoryEntryResourceIdentifierType } from './Inventory-types'
import { orderEditResourceIdentifierType } from './OrderEdit-types'
import { orderResourceIdentifierType } from './Order-types'
import { paymentResourceIdentifierType } from './Payment-types'
import { productDiscountResourceIdentifierType } from './ProductDiscount-types'
import { productTypeResourceIdentifierType } from './ProductType-types'
import { productResourceIdentifierType } from './Product-types'
import { reviewResourceIdentifierType } from './Review-types'
import { shippingMethodResourceIdentifierType } from './ShippingMethod-types'
import { shoppingListResourceIdentifierType } from './ShoppingList-types'
import { stateResourceIdentifierType } from './State-types'
import { storeResourceIdentifierType } from './Store-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { typeResourceIdentifierType } from './Type-types'
import { zoneResourceIdentifierType } from './Zone-types'
import { cartDiscountResourceIdentifierType } from './CartDiscount-types'
import { cartResourceIdentifierType } from './Cart-types'
import { categoryResourceIdentifierType } from './Category-types'

export function pagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(baseResourceType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             facets: facetResultsType().optional(),
             total: Joi.number().optional(),
             meta: Joi.any().optional()
          })
}

export function updateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(updateActionType()).required(),
             version: Joi.number().required()
          })
}

export function updateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required()
          })
}

export function addressType(): Joi.AnySchema {
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
          })
}

export function assetType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             sources: Joi.array().items(assetSourceType()).required(),
             name: localizedStringType().required(),
             id: Joi.string().required(),
             tags: Joi.array().items(Joi.string()).optional(),
             custom: customFieldsType().optional(),
             description: localizedStringType().optional(),
             key: Joi.string().optional()
          })
}

export function assetDimensionsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             h: Joi.number().required(),
             w: Joi.number().required()
          })
}

export function assetDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             sources: Joi.array().items(assetSourceType()).required(),
             name: localizedStringType().required(),
             tags: Joi.array().items(Joi.string()).optional(),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             key: Joi.string().optional()
          })
}

export function assetSourceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             uri: Joi.string().required(),
             dimensions: assetDimensionsType().optional(),
             contentType: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function baseResourceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required()
          })
}

export function centPrecisionMoneyType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currencyCode: Joi.string().required(),
             type: moneyTypeType().required().only('centPrecision'),
             centAmount: Joi.number().required(),
             fractionDigits: Joi.number().required()
          })
}

export function clientLoggingType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerReferenceType().optional(),
             anonymousId: Joi.string().optional(),
             clientId: Joi.string().optional(),
             externalUserId: Joi.string().optional()
          })
}

export function createdByType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerReferenceType().optional(),
             anonymousId: Joi.string().optional(),
             clientId: Joi.string().optional(),
             externalUserId: Joi.string().optional()
          })
}

export function discountedPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: moneyType().required(),
             discount: productDiscountReferenceType().required()
          })
}

export function geoJsonType(): Joi.AnySchema {
   return Joi.alternatives([geoJsonPointType()])
}

export function geoJsonPointType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             coordinates: Joi.array().items(Joi.number()).required(),
             type: Joi.string().required().only('Point')
          })
}

export function highPrecisionMoneyType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currencyCode: Joi.string().required(),
             type: moneyTypeType().required().only('highPrecision'),
             centAmount: Joi.number().required(),
             fractionDigits: Joi.number().required(),
             preciseAmount: Joi.number().required()
          })
}

export function imageType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             dimensions: imageDimensionsType().required(),
             url: Joi.string().required(),
             label: Joi.string().optional()
          })
}

export function imageDimensionsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             h: Joi.number().required(),
             w: Joi.number().required()
          })
}

export function keyReferenceType(): Joi.AnySchema {
   return Joi.alternatives([storeKeyReferenceType()])
}

export function lastModifiedByType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerReferenceType().optional(),
             anonymousId: Joi.string().optional(),
             clientId: Joi.string().optional(),
             externalUserId: Joi.string().optional()
          })
}

export function localizedStringType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('/^[a-z]{2}(-[A-Z]{2})?$/'), Joi.string())
}

export function loggedResourceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional()
          })
}

export function moneyType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currencyCode: Joi.string().required(),
             centAmount: Joi.number().required()
          })
}

const moneyTypeTypeValues = [

   'centPrecision',
   'highPrecision'

]

export function moneyTypeType(): Joi.AnySchema {
   return Joi.string().only(moneyTypeTypeValues)
}

export function priceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: moneyType().required(),
             tiers: Joi.array().items(priceTierType()).optional(),
             channel: channelReferenceType().optional(),
             country: Joi.string().optional(),
             custom: customFieldsType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             discounted: discountedPriceType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional(),
             id: Joi.string().optional()
          })
}

export function priceDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: moneyType().required(),
             tiers: Joi.array().items(priceTierType()).optional(),
             channel: channelResourceIdentifierType().optional(),
             country: Joi.string().optional(),
             custom: customFieldsDraftType().optional(),
             customerGroup: customerGroupResourceIdentifierType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional()
          })
}

export function priceTierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: moneyType().required(),
             minimumQuantity: Joi.number().required()
          })
}

export function referenceType(): Joi.AnySchema {
   return Joi.alternatives([customObjectReferenceType(), customerGroupReferenceType(), customerReferenceType(), discountCodeReferenceType(), inventoryEntryReferenceType(), orderEditReferenceType(), orderReferenceType(), paymentReferenceType(), productDiscountReferenceType(), productTypeReferenceType(), productReferenceType(), reviewReferenceType(), shippingMethodReferenceType(), shoppingListReferenceType(), stateReferenceType(), storeReferenceType(), taxCategoryReferenceType(), typeReferenceType(), zoneReferenceType(), cartDiscountReferenceType(), cartReferenceType(), categoryReferenceType(), channelReferenceType()])
}

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

]

export function referenceTypeIdType(): Joi.AnySchema {
   return Joi.string().only(referenceTypeIdTypeValues)
}

export function resourceIdentifierType(): Joi.AnySchema {
   return Joi.alternatives([customerGroupResourceIdentifierType(), customerResourceIdentifierType(), discountCodeResourceIdentifierType(), inventoryEntryResourceIdentifierType(), orderEditResourceIdentifierType(), orderResourceIdentifierType(), paymentResourceIdentifierType(), productDiscountResourceIdentifierType(), productTypeResourceIdentifierType(), productResourceIdentifierType(), reviewResourceIdentifierType(), shippingMethodResourceIdentifierType(), shoppingListResourceIdentifierType(), stateResourceIdentifierType(), storeResourceIdentifierType(), taxCategoryResourceIdentifierType(), typeResourceIdentifierType(), zoneResourceIdentifierType(), cartDiscountResourceIdentifierType(), cartResourceIdentifierType(), categoryResourceIdentifierType(), channelResourceIdentifierType()])
}

export function scopedPriceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currentValue: typedMoneyType().required(),
             value: typedMoneyType().required(),
             id: Joi.string().required(),
             channel: channelReferenceType().optional(),
             country: Joi.string().optional(),
             custom: customFieldsType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             discounted: discountedPriceType().optional(),
             validFrom: Joi.date().iso().optional(),
             validUntil: Joi.date().iso().optional()
          })
}

export function typedMoneyType(): Joi.AnySchema {
   return Joi.alternatives([centPrecisionMoneyType(), highPrecisionMoneyType()])
}