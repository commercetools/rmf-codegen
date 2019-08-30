/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { shippingRateTierTypeType } from './ShippingMethod-types'
import { customFieldLocalizedEnumValueType } from './Type-types'
import { messageConfigurationType } from './Message-types'
import { messageConfigurationDraftType } from './Message-types'

export function cartClassificationTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: shippingRateTierTypeType().required().only('CartClassification'),
             values: Joi.array().items(customFieldLocalizedEnumValueType()).required()
          })
}

export function cartScoreTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: shippingRateTierTypeType().required().only('CartScore')
          })
}

export function cartValueTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: shippingRateTierTypeType().required().only('CartValue')
          })
}

export function externalOAuthType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             authorizationHeader: Joi.string().required(),
             url: Joi.string().required()
          })
}

export function projectType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             countries: Joi.array().items(Joi.string()).required(),
             currencies: Joi.array().items(Joi.string()).required(),
             languages: Joi.array().items(Joi.string()).required(),
             messages: messageConfigurationType().required(),
             createdAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             key: Joi.string().required(),
             name: Joi.string().required(),
             externalOAuth: externalOAuthType().optional(),
             shippingRateInputType: shippingRateInputTypeType().optional(),
             trialUntil: Joi.string().optional()
          })
}

export function projectUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(projectUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function projectUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([projectChangeCountriesActionType(), projectChangeCurrenciesActionType(), projectChangeLanguagesActionType(), projectChangeMessagesConfigurationActionType(), projectChangeMessagesEnabledActionType(), projectChangeNameActionType(), projectSetExternalOAuthActionType(), projectSetShippingRateInputTypeActionType()])
}

export function shippingRateInputTypeType(): Joi.AnySchema {
   return Joi.alternatives([cartClassificationTypeType(), cartScoreTypeType(), cartValueTypeType()])
}

export function projectChangeCountriesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             countries: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeCountries')
          })
}

export function projectChangeCurrenciesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             currencies: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeCurrencies')
          })
}

export function projectChangeLanguagesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             languages: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeLanguages')
          })
}

export function projectChangeMessagesConfigurationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             messagesConfiguration: messageConfigurationDraftType().required(),
             action: Joi.string().required().only('changeMessagesConfiguration')
          })
}

export function projectChangeMessagesEnabledActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             messagesEnabled: Joi.boolean().required(),
             action: Joi.string().required().only('changeMessagesEnabled')
          })
}

export function projectChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function projectSetExternalOAuthActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setExternalOAuth'),
             externalOAuth: externalOAuthType().optional()
          })
}

export function projectSetShippingRateInputTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setShippingRateInputType'),
             shippingRateInputType: shippingRateInputTypeType().optional()
          })
}