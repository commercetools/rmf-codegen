/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { addressType } from './Common-types'
import { taxModeType } from './Cart-types'
import { customFieldsDraftType } from './Type-types'
import { shippingMethodResourceIdentifierType } from './ShippingMethod-types'
import { inventoryModeType } from './Cart-types'
import { customFieldsType } from './Type-types'
import { itemShippingDetailsDraftType } from './Cart-types'
import { channelResourceIdentifierType } from './Channel-types'

export function myCartDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currency: Joi.string().required(),
             itemShippingAddresses: Joi.array().items(addressType()).optional(),
             lineItems: Joi.array().items(myLineItemDraftType()).optional(),
             billingAddress: addressType().optional(),
             shippingAddress: addressType().optional(),
             custom: customFieldsDraftType().optional(),
             inventoryMode: inventoryModeType().optional(),
             shippingMethod: shippingMethodResourceIdentifierType().optional(),
             taxMode: taxModeType().optional(),
             deleteDaysAfterLastModification: Joi.number().optional(),
             country: Joi.string().optional(),
             customerEmail: Joi.string().optional(),
             locale: Joi.string().optional()
          })
}

export function myCustomerDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             email: Joi.string().required(),
             password: Joi.string().required(),
             addresses: Joi.array().items(addressType()).optional(),
             custom: customFieldsType().optional(),
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
          })
}

export function myLineItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             variantId: Joi.number().required(),
             productId: Joi.string().required(),
             distributionChannel: channelResourceIdentifierType().optional(),
             supplyChannel: channelResourceIdentifierType().optional(),
             custom: customFieldsDraftType().optional(),
             shippingDetails: itemShippingDetailsDraftType().optional()
          })
}

export function myOrderFromCartDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             version: Joi.number().required(),
             id: Joi.string().required()
          })
}