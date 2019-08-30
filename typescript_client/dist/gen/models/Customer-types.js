"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Type_types_1 = require("./Type-types");
const CustomerGroup_types_2 = require("./CustomerGroup-types");
const Type_types_2 = require("./Type-types");
const Common_types_4 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
const anonymousCartSignInModeTypeValues = [
    'MergeWithExistingCustomerCart',
    'UseAsNewActiveCustomerCart'
];
function anonymousCartSignInModeType() {
    return Joi.string().only(anonymousCartSignInModeTypeValues);
}
exports.anonymousCartSignInModeType = anonymousCartSignInModeType;
function customerType() {
    return Joi.object().unknown().keys({
        addresses: Joi.array().items(Common_types_3.addressType()).required(),
        isEmailVerified: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        email: Joi.string().required(),
        id: Joi.string().required(),
        password: Joi.string().required(),
        billingAddressIds: Joi.array().items(Joi.string()).optional(),
        shippingAddressIds: Joi.array().items(Joi.string()).optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
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
    });
}
exports.customerType = customerType;
function customerChangePasswordType() {
    return Joi.object().unknown().keys({
        version: Joi.number().required(),
        currentPassword: Joi.string().required(),
        id: Joi.string().required(),
        newPassword: Joi.string().required()
    });
}
exports.customerChangePasswordType = customerChangePasswordType;
function customerCreateEmailTokenType() {
    return Joi.object().unknown().keys({
        ttlMinutes: Joi.number().required(),
        id: Joi.string().required(),
        version: Joi.number().optional()
    });
}
exports.customerCreateEmailTokenType = customerCreateEmailTokenType;
function customerCreatePasswordResetTokenType() {
    return Joi.object().unknown().keys({
        email: Joi.string().required(),
        ttlMinutes: Joi.number().optional()
    });
}
exports.customerCreatePasswordResetTokenType = customerCreatePasswordResetTokenType;
function customerDraftType() {
    return Joi.object().unknown().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
        addresses: Joi.array().items(Common_types_3.addressType()).optional(),
        billingAddresses: Joi.array().items(Joi.number()).optional(),
        shippingAddresses: Joi.array().items(Joi.number()).optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional(),
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
    });
}
exports.customerDraftType = customerDraftType;
function customerEmailVerifyType() {
    return Joi.object().unknown().keys({
        tokenValue: Joi.string().required(),
        version: Joi.number().optional()
    });
}
exports.customerEmailVerifyType = customerEmailVerifyType;
function customerPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(customerType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.customerPagedQueryResponseType = customerPagedQueryResponseType;
function customerReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().required().only('customer'),
        id: Joi.string().required(),
        obj: customerType().optional()
    });
}
exports.customerReferenceType = customerReferenceType;
function customerResetPasswordType() {
    return Joi.object().unknown().keys({
        newPassword: Joi.string().required(),
        tokenValue: Joi.string().required(),
        version: Joi.number().optional()
    });
}
exports.customerResetPasswordType = customerResetPasswordType;
function customerResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_4.referenceTypeIdType().optional().only('customer'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.customerResourceIdentifierType = customerResourceIdentifierType;
function customerSignInResultType() {
    return Joi.object().unknown().keys({
        customer: customerType().required(),
        cart: Joi.any().optional()
    });
}
exports.customerSignInResultType = customerSignInResultType;
function customerSigninType() {
    return Joi.object().unknown().keys({
        email: Joi.string().required(),
        password: Joi.string().required(),
        anonymousCartSignInMode: anonymousCartSignInModeType().optional(),
        anonymousCartId: Joi.string().optional(),
        anonymousId: Joi.string().optional()
    });
}
exports.customerSigninType = customerSigninType;
function customerTokenType() {
    return Joi.object().unknown().keys({
        createdAt: Joi.date().iso().required(),
        expiresAt: Joi.date().iso().required(),
        customerId: Joi.string().required(),
        id: Joi.string().required(),
        value: Joi.string().required(),
        lastModifiedAt: Joi.date().iso().optional()
    });
}
exports.customerTokenType = customerTokenType;
function customerUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(customerUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.customerUpdateType = customerUpdateType;
function customerUpdateActionType() {
    return Joi.alternatives([customerAddAddressActionType(), customerAddBillingAddressIdActionType(), customerAddShippingAddressIdActionType(), customerChangeAddressActionType(), customerChangeEmailActionType(), customerRemoveAddressActionType(), customerRemoveBillingAddressIdActionType(), customerRemoveShippingAddressIdActionType(), customerSetCompanyNameActionType(), customerSetCustomFieldActionType(), customerSetCustomTypeActionType(), customerSetCustomerGroupActionType(), customerSetCustomerNumberActionType(), customerSetDateOfBirthActionType(), customerSetDefaultBillingAddressActionType(), customerSetDefaultShippingAddressActionType(), customerSetExternalIdActionType(), customerSetFirstNameActionType(), customerSetKeyActionType(), customerSetLastNameActionType(), customerSetLocaleActionType(), customerSetMiddleNameActionType(), customerSetSalutationActionType(), customerSetTitleActionType(), customerSetVatIdActionType()]);
}
exports.customerUpdateActionType = customerUpdateActionType;
function customerAddAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        action: Joi.string().required().only('addAddress')
    });
}
exports.customerAddAddressActionType = customerAddAddressActionType;
function customerAddBillingAddressIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addBillingAddressId'),
        addressId: Joi.string().required()
    });
}
exports.customerAddBillingAddressIdActionType = customerAddBillingAddressIdActionType;
function customerAddShippingAddressIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addShippingAddressId'),
        addressId: Joi.string().required()
    });
}
exports.customerAddShippingAddressIdActionType = customerAddShippingAddressIdActionType;
function customerChangeAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_3.addressType().required(),
        action: Joi.string().required().only('changeAddress'),
        addressId: Joi.string().required()
    });
}
exports.customerChangeAddressActionType = customerChangeAddressActionType;
function customerChangeEmailActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeEmail'),
        email: Joi.string().required()
    });
}
exports.customerChangeEmailActionType = customerChangeEmailActionType;
function customerRemoveAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeAddress'),
        addressId: Joi.string().required()
    });
}
exports.customerRemoveAddressActionType = customerRemoveAddressActionType;
function customerRemoveBillingAddressIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeBillingAddressId'),
        addressId: Joi.string().required()
    });
}
exports.customerRemoveBillingAddressIdActionType = customerRemoveBillingAddressIdActionType;
function customerRemoveShippingAddressIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeShippingAddressId'),
        addressId: Joi.string().required()
    });
}
exports.customerRemoveShippingAddressIdActionType = customerRemoveShippingAddressIdActionType;
function customerSetCompanyNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCompanyName'),
        companyName: Joi.string().optional()
    });
}
exports.customerSetCompanyNameActionType = customerSetCompanyNameActionType;
function customerSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.customerSetCustomFieldActionType = customerSetCustomFieldActionType;
function customerSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.customerSetCustomTypeActionType = customerSetCustomTypeActionType;
function customerSetCustomerGroupActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerGroup'),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional()
    });
}
exports.customerSetCustomerGroupActionType = customerSetCustomerGroupActionType;
function customerSetCustomerNumberActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerNumber'),
        customerNumber: Joi.string().optional()
    });
}
exports.customerSetCustomerNumberActionType = customerSetCustomerNumberActionType;
function customerSetDateOfBirthActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDateOfBirth'),
        dateOfBirth: Joi.date().iso().optional()
    });
}
exports.customerSetDateOfBirthActionType = customerSetDateOfBirthActionType;
function customerSetDefaultBillingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDefaultBillingAddress'),
        addressId: Joi.string().optional()
    });
}
exports.customerSetDefaultBillingAddressActionType = customerSetDefaultBillingAddressActionType;
function customerSetDefaultShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDefaultShippingAddress'),
        addressId: Joi.string().optional()
    });
}
exports.customerSetDefaultShippingAddressActionType = customerSetDefaultShippingAddressActionType;
function customerSetExternalIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setExternalId'),
        externalId: Joi.string().optional()
    });
}
exports.customerSetExternalIdActionType = customerSetExternalIdActionType;
function customerSetFirstNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setFirstName'),
        firstName: Joi.string().optional()
    });
}
exports.customerSetFirstNameActionType = customerSetFirstNameActionType;
function customerSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.customerSetKeyActionType = customerSetKeyActionType;
function customerSetLastNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLastName'),
        lastName: Joi.string().optional()
    });
}
exports.customerSetLastNameActionType = customerSetLastNameActionType;
function customerSetLocaleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLocale'),
        locale: Joi.string().optional()
    });
}
exports.customerSetLocaleActionType = customerSetLocaleActionType;
function customerSetMiddleNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMiddleName'),
        middleName: Joi.string().optional()
    });
}
exports.customerSetMiddleNameActionType = customerSetMiddleNameActionType;
function customerSetSalutationActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setSalutation'),
        salutation: Joi.string().optional()
    });
}
exports.customerSetSalutationActionType = customerSetSalutationActionType;
function customerSetTitleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTitle'),
        title: Joi.string().optional()
    });
}
exports.customerSetTitleActionType = customerSetTitleActionType;
function customerSetVatIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setVatId'),
        vatId: Joi.string().optional()
    });
}
exports.customerSetVatIdActionType = customerSetVatIdActionType;
