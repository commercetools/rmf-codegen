/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { referenceType } from './Common-types'

export function extensionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             triggers: Joi.array().items(extensionTriggerType()).required(),
             destination: extensionDestinationType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             timeoutInMs: Joi.number().optional(),
             key: Joi.string().optional()
          })
}

export function extensionAWSLambdaDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             accessKey: Joi.string().required(),
             accessSecret: Joi.string().required(),
             arn: Joi.string().required(),
             type: Joi.string().required().only('AWSLambda')
          })
}

const extensionActionTypeValues = [

   'Create',
   'Update'

]

export function extensionActionType(): Joi.AnySchema {
   return Joi.string().only(extensionActionTypeValues)
}

export function extensionAuthorizationHeaderAuthenticationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             headerValue: Joi.string().required(),
             type: Joi.string().required().only('AuthorizationHeader')
          })
}

export function extensionAzureFunctionsAuthenticationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             key: Joi.string().required(),
             type: Joi.string().required().only('AzureFunctions')
          })
}

export function extensionDestinationType(): Joi.AnySchema {
   return Joi.alternatives([extensionHttpDestinationType(), extensionAWSLambdaDestinationType()])
}

export function extensionDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             triggers: Joi.array().items(extensionTriggerType()).required(),
             destination: extensionDestinationType().required(),
             timeoutInMs: Joi.number().optional(),
             key: Joi.string().optional()
          })
}

export function extensionHttpDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('HTTP'),
             url: Joi.string().required(),
             authentication: extensionHttpDestinationAuthenticationType().optional()
          })
}

export function extensionHttpDestinationAuthenticationType(): Joi.AnySchema {
   return Joi.alternatives([extensionAuthorizationHeaderAuthenticationType(), extensionAzureFunctionsAuthenticationType()])
}

export function extensionInputType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: extensionActionType().required(),
             resource: referenceType().required()
          })
}

export function extensionPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(extensionType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

const extensionResourceTypeIdTypeValues = [

   'cart',
   'order',
   'payment',
   'customer'

]

export function extensionResourceTypeIdType(): Joi.AnySchema {
   return Joi.string().only(extensionResourceTypeIdTypeValues)
}

export function extensionTriggerType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(extensionActionType()).required(),
             resourceTypeId: extensionResourceTypeIdType().required()
          })
}

export function extensionUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(extensionUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function extensionUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([extensionChangeDestinationActionType(), extensionChangeTriggersActionType(), extensionSetKeyActionType(), extensionSetTimeoutInMsActionType()])
}

export function extensionChangeDestinationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             destination: extensionDestinationType().required(),
             action: Joi.string().required().only('changeDestination')
          })
}

export function extensionChangeTriggersActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             triggers: Joi.array().items(extensionTriggerType()).required(),
             action: Joi.string().required().only('changeTriggers')
          })
}

export function extensionSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function extensionSetTimeoutInMsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTimeoutInMs'),
             timeoutInMs: Joi.number().optional()
          })
}