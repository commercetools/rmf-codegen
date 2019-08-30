/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { referenceType } from './Common-types'
import { userProvidedIdentifiersType } from './Message-types'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { loggedResourceType } from './Common-types'

export function azureEventGridDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             accessKey: Joi.string().required(),
             type: Joi.string().required().only('EventGrid'),
             uri: Joi.string().required()
          })
}

export function azureServiceBusDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             connectionString: Joi.string().required(),
             type: Joi.string().required().only('AzureServiceBus')
          })
}

export function changeSubscriptionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resourceTypeId: Joi.string().required()
          })
}

export function deliveryCloudEventsFormatType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             cloudEventsVersion: Joi.string().required(),
             type: Joi.string().required().only('CloudEvents')
          })
}

export function deliveryFormatType(): Joi.AnySchema {
   return Joi.alternatives([deliveryPlatformFormatType(), deliveryCloudEventsFormatType()])
}

export function deliveryPlatformFormatType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('Platform')
          })
}

export function destinationType(): Joi.AnySchema {
   return Joi.alternatives([googleCloudPubSubDestinationType(), ironMqDestinationType(), snsDestinationType(), sqsDestinationType(), azureEventGridDestinationType(), azureServiceBusDestinationType()])
}

export function googleCloudPubSubDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             projectId: Joi.string().required(),
             topic: Joi.string().required(),
             type: Joi.string().required().only('GoogleCloudPubSub')
          })
}

export function ironMqDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('IronMQ'),
             uri: Joi.string().required()
          })
}

export function messageDeliveryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payloadNotIncluded: payloadNotIncludedType().required(),
             resource: referenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             resourceVersion: Joi.number().required(),
             sequenceNumber: Joi.number().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             notificationType: Joi.string().required().only('Message'),
             projectKey: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function messageSubscriptionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resourceTypeId: Joi.string().required(),
             types: Joi.array().items(Joi.string()).optional()
          })
}

export function payloadNotIncludedType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             payloadType: Joi.string().required(),
             reason: Joi.string().required()
          })
}

export function resourceCreatedDeliveryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             modifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             notificationType: Joi.string().required().only('ResourceCreated'),
             projectKey: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function resourceDeletedDeliveryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             modifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             notificationType: Joi.string().required().only('ResourceDeleted'),
             projectKey: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function resourceUpdatedDeliveryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resource: referenceType().required(),
             modifiedAt: Joi.date().iso().required(),
             oldVersion: Joi.number().required(),
             version: Joi.number().required(),
             notificationType: Joi.string().required().only('ResourceUpdated'),
             projectKey: Joi.string().required(),
             resourceUserProvidedIdentifiers: userProvidedIdentifiersType().optional()
          })
}

export function snsDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             accessKey: Joi.string().required(),
             accessSecret: Joi.string().required(),
             topicArn: Joi.string().required(),
             type: Joi.string().required().only('SNS')
          })
}

export function sqsDestinationType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             accessKey: Joi.string().required(),
             accessSecret: Joi.string().required(),
             queueUrl: Joi.string().required(),
             region: Joi.string().required(),
             type: Joi.string().required().only('SQS')
          })
}

export function subscriptionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             changes: Joi.array().items(changeSubscriptionType()).required(),
             messages: Joi.array().items(messageSubscriptionType()).required(),
             format: deliveryFormatType().required(),
             destination: destinationType().required(),
             status: subscriptionHealthStatusType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             key: Joi.string().optional()
          })
}

export function subscriptionDeliveryType(): Joi.AnySchema {
   return Joi.alternatives([messageDeliveryType(), resourceCreatedDeliveryType(), resourceDeletedDeliveryType(), resourceUpdatedDeliveryType()])
}

export function subscriptionDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             destination: destinationType().required(),
             changes: Joi.array().items(changeSubscriptionType()).optional(),
             messages: Joi.array().items(messageSubscriptionType()).optional(),
             format: deliveryFormatType().optional(),
             key: Joi.string().optional()
          })
}

const subscriptionHealthStatusTypeValues = [

   'Healthy',
   'ConfigurationError',
   'ConfigurationErrorDeliveryStopped',
   'TemporaryError'

]

export function subscriptionHealthStatusType(): Joi.AnySchema {
   return Joi.string().only(subscriptionHealthStatusTypeValues)
}

export function subscriptionPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(subscriptionType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function subscriptionUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(subscriptionUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function subscriptionUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([subscriptionChangeDestinationActionType(), subscriptionSetChangesActionType(), subscriptionSetKeyActionType(), subscriptionSetMessagesActionType()])
}

export function subscriptionChangeDestinationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             destination: destinationType().required(),
             action: Joi.string().required().only('changeDestination')
          })
}

export function subscriptionSetChangesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setChanges'),
             changes: Joi.array().items(changeSubscriptionType()).optional()
          })
}

export function subscriptionSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function subscriptionSetMessagesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMessages'),
             messages: Joi.array().items(messageSubscriptionType()).optional()
          })
}