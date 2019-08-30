"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const Type_types_1 = require("./Type-types");
const Message_types_1 = require("./Message-types");
const Message_types_2 = require("./Message-types");
function cartClassificationTypeType() {
    return Joi.object().unknown().keys({
        type: ShippingMethod_types_1.shippingRateTierTypeType().required().only('CartClassification'),
        values: Joi.array().items(Type_types_1.customFieldLocalizedEnumValueType()).required()
    });
}
exports.cartClassificationTypeType = cartClassificationTypeType;
function cartScoreTypeType() {
    return Joi.object().unknown().keys({
        type: ShippingMethod_types_1.shippingRateTierTypeType().required().only('CartScore')
    });
}
exports.cartScoreTypeType = cartScoreTypeType;
function cartValueTypeType() {
    return Joi.object().unknown().keys({
        type: ShippingMethod_types_1.shippingRateTierTypeType().required().only('CartValue')
    });
}
exports.cartValueTypeType = cartValueTypeType;
function externalOAuthType() {
    return Joi.object().unknown().keys({
        authorizationHeader: Joi.string().required(),
        url: Joi.string().required()
    });
}
exports.externalOAuthType = externalOAuthType;
function projectType() {
    return Joi.object().unknown().keys({
        countries: Joi.array().items(Joi.string()).required(),
        currencies: Joi.array().items(Joi.string()).required(),
        languages: Joi.array().items(Joi.string()).required(),
        messages: Message_types_1.messageConfigurationType().required(),
        createdAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        key: Joi.string().required(),
        name: Joi.string().required(),
        externalOAuth: externalOAuthType().optional(),
        shippingRateInputType: shippingRateInputTypeType().optional(),
        trialUntil: Joi.string().optional()
    });
}
exports.projectType = projectType;
function projectUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(projectUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.projectUpdateType = projectUpdateType;
function projectUpdateActionType() {
    return Joi.alternatives([projectChangeCountriesActionType(), projectChangeCurrenciesActionType(), projectChangeLanguagesActionType(), projectChangeMessagesConfigurationActionType(), projectChangeMessagesEnabledActionType(), projectChangeNameActionType(), projectSetExternalOAuthActionType(), projectSetShippingRateInputTypeActionType()]);
}
exports.projectUpdateActionType = projectUpdateActionType;
function shippingRateInputTypeType() {
    return Joi.alternatives([cartClassificationTypeType(), cartScoreTypeType(), cartValueTypeType()]);
}
exports.shippingRateInputTypeType = shippingRateInputTypeType;
function projectChangeCountriesActionType() {
    return Joi.object().unknown().keys({
        countries: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeCountries')
    });
}
exports.projectChangeCountriesActionType = projectChangeCountriesActionType;
function projectChangeCurrenciesActionType() {
    return Joi.object().unknown().keys({
        currencies: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeCurrencies')
    });
}
exports.projectChangeCurrenciesActionType = projectChangeCurrenciesActionType;
function projectChangeLanguagesActionType() {
    return Joi.object().unknown().keys({
        languages: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeLanguages')
    });
}
exports.projectChangeLanguagesActionType = projectChangeLanguagesActionType;
function projectChangeMessagesConfigurationActionType() {
    return Joi.object().unknown().keys({
        messagesConfiguration: Message_types_2.messageConfigurationDraftType().required(),
        action: Joi.string().required().only('changeMessagesConfiguration')
    });
}
exports.projectChangeMessagesConfigurationActionType = projectChangeMessagesConfigurationActionType;
function projectChangeMessagesEnabledActionType() {
    return Joi.object().unknown().keys({
        messagesEnabled: Joi.boolean().required(),
        action: Joi.string().required().only('changeMessagesEnabled')
    });
}
exports.projectChangeMessagesEnabledActionType = projectChangeMessagesEnabledActionType;
function projectChangeNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeName'),
        name: Joi.string().required()
    });
}
exports.projectChangeNameActionType = projectChangeNameActionType;
function projectSetExternalOAuthActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setExternalOAuth'),
        externalOAuth: externalOAuthType().optional()
    });
}
exports.projectSetExternalOAuthActionType = projectSetExternalOAuthActionType;
function projectSetShippingRateInputTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingRateInputType'),
        shippingRateInputType: shippingRateInputTypeType().optional()
    });
}
exports.projectSetShippingRateInputTypeActionType = projectSetShippingRateInputTypeActionType;
