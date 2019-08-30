/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { addressType } from './Common-types'
import { customerGroupReferenceType } from './CustomerGroup-types'
import { customFieldsType } from './Type-types'
import { loggedResourceType } from './Common-types'
import { customerGroupResourceIdentifierType } from './CustomerGroup-types'
import { customFieldsDraftType } from './Type-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

const anonymousCartSignInModeTypeValues = [

   'MergeWithExistingCustomerCart',
   'UseAsNewActiveCustomerCart'

]

export function anonymousCartSignInModeType(): Joi.AnySchema {
   return Joi.string().only(anonymousCartSignInModeTypeValues)
}

export function customerType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             addresses: Joi.array().items(addressType()).required(),
             isEmailVerified: Joi.boolean().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             email: Joi.string().required(),
             id: Joi.string().required(),
             password: Joi.string().required(),
             billingAddressIds: Joi.array().items(Joi.string()).optional(),
             shippingAddressIds: Joi.array().items(Joi.string()).optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customerGroup: customerGroupReferenceType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             dateOfBirth: Joi.date().iso().optional(),
             companyName: Joi.string().optional(),
             customerNumber: Joi.string().optional(),
             defaultBillingAddressId: Joi.string().optional(),
             defaultShippingAddressId: Joi.string().optional(),
             externalId: Joi.string().optional(),
             firstName: Joi.string().optional(),
             key: Joi.string().optional(),
             lastName: Joi.string().optional(),
             locale: Joi.string().optional(),
             middleName: Joi.string().optional(),
             salutation: Joi.string().optional(),
             title: Joi.string().optional(),
             vatId: Joi.string().optional()
          })
}

export function customerChangePasswordType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             version: Joi.number().required(),
             currentPassword: Joi.string().required(),
             id: Joi.string().required(),
             newPassword: Joi.string().required()
          })
}

export function customerCreateEmailTokenType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             ttlMinutes: Joi.number().required(),
             id: Joi.string().required(),
             version: Joi.number().optional()
          })
}

export function customerCreatePasswordResetTokenType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             email: Joi.string().required(),
             ttlMinutes: Joi.number().optional()
          })
}

export function customerDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             email: Joi.string().required(),
             password: Joi.string().required(),
             addresses: Joi.array().items(addressType()).optional(),
             billingAddresses: Joi.array().items(Joi.number()).optional(),
             shippingAddresses: Joi.array().items(Joi.number()).optional(),
             custom: customFieldsDraftType().optional(),
             customerGroup: customerGroupResourceIdentifierType().optional(),
             isEmailVerified: Joi.boolean().optional(),
             dateOfBirth: Joi.date().iso().optional(),
             defaultBillingAddress: Joi.number().optional(),
             defaultShippingAddress: Joi.number().optional(),
             anonymousCartId: Joi.string().optional(),
             anonymousId: Joi.string().optional(),
             companyName: Joi.string().optional(),
             customerNumber: Joi.string().optional(),
             externalId: Joi.string().optional(),
             firstName: Joi.string().optional(),
             key: Joi.string().optional(),
             lastName: Joi.string().optional(),
             locale: Joi.string().optional(),
             middleName: Joi.string().optional(),
             salutation: Joi.string().optional(),
             title: Joi.string().optional(),
             vatId: Joi.string().optional()
          })
}

export function customerEmailVerifyType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             tokenValue: Joi.string().required(),
             version: Joi.number().optional()
          })
}

export function customerPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(customerType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function customerReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('customer'),
             id: Joi.string().required(),
             obj: customerType().optional()
          })
}

export function customerResetPasswordType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             newPassword: Joi.string().required(),
             tokenValue: Joi.string().required(),
             version: Joi.number().optional()
          })
}

export function customerResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('customer'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function customerSignInResultType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             customer: customerType().required(),
             cart: Joi.any().optional()
          })
}

export function customerSigninType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             email: Joi.string().required(),
             password: Joi.string().required(),
             anonymousCartSignInMode: anonymousCartSignInModeType().optional(),
             anonymousCartId: Joi.string().optional(),
             anonymousId: Joi.string().optional()
          })
}

export function customerTokenType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             expiresAt: Joi.date().iso().required(),
             customerId: Joi.string().required(),
             id: Joi.string().required(),
             value: Joi.string().required(),
             lastModifiedAt: Joi.date().iso().optional()
          })
}

export function customerUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(customerUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function customerUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([customerAddAddressActionType(), customerAddBillingAddressIdActionType(), customerAddShippingAddressIdActionType(), customerChangeAddressActionType(), customerChangeEmailActionType(), customerRemoveAddressActionType(), customerRemoveBillingAddressIdActionType(), customerRemoveShippingAddressIdActionType(), customerSetCompanyNameActionType(), customerSetCustomFieldActionType(), customerSetCustomTypeActionType(), customerSetCustomerGroupActionType(), customerSetCustomerNumberActionType(), customerSetDateOfBirthActionType(), customerSetDefaultBillingAddressActionType(), customerSetDefaultShippingAddressActionType(), customerSetExternalIdActionType(), customerSetFirstNameActionType(), customerSetKeyActionType(), customerSetLastNameActionType(), customerSetLocaleActionType(), customerSetMiddleNameActionType(), customerSetSalutationActionType(), customerSetTitleActionType(), customerSetVatIdActionType()])
}

export function customerAddAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('addAddress')
          })
}

export function customerAddBillingAddressIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addBillingAddressId'),
             addressId: Joi.string().required()
          })
}

export function customerAddShippingAddressIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addShippingAddressId'),
             addressId: Joi.string().required()
          })
}

export function customerChangeAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             address: addressType().required(),
             action: Joi.string().required().only('changeAddress'),
             addressId: Joi.string().required()
          })
}

export function customerChangeEmailActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeEmail'),
             email: Joi.string().required()
          })
}

export function customerRemoveAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeAddress'),
             addressId: Joi.string().required()
          })
}

export function customerRemoveBillingAddressIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeBillingAddressId'),
             addressId: Joi.string().required()
          })
}

export function customerRemoveShippingAddressIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeShippingAddressId'),
             addressId: Joi.string().required()
          })
}

export function customerSetCompanyNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCompanyName'),
             companyName: Joi.string().optional()
          })
}

export function customerSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function customerSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function customerSetCustomerGroupActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerGroup'),
             customerGroup: customerGroupResourceIdentifierType().optional()
          })
}

export function customerSetCustomerNumberActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomerNumber'),
             customerNumber: Joi.string().optional()
          })
}

export function customerSetDateOfBirthActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDateOfBirth'),
             dateOfBirth: Joi.date().iso().optional()
          })
}

export function customerSetDefaultBillingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDefaultBillingAddress'),
             addressId: Joi.string().optional()
          })
}

export function customerSetDefaultShippingAddressActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDefaultShippingAddress'),
             addressId: Joi.string().optional()
          })
}

export function customerSetExternalIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setExternalId'),
             externalId: Joi.string().optional()
          })
}

export function customerSetFirstNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setFirstName'),
             firstName: Joi.string().optional()
          })
}

export function customerSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function customerSetLastNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLastName'),
             lastName: Joi.string().optional()
          })
}

export function customerSetLocaleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLocale'),
             locale: Joi.string().optional()
          })
}

export function customerSetMiddleNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMiddleName'),
             middleName: Joi.string().optional()
          })
}

export function customerSetSalutationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setSalutation'),
             salutation: Joi.string().optional()
          })
}

export function customerSetTitleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTitle'),
             title: Joi.string().optional()
          })
}

export function customerSetVatIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setVatId'),
             vatId: Joi.string().optional()
          })
}