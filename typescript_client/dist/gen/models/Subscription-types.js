"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Message_types_1 = require("./Message-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
function azureEventGridDestinationType() {
    return Joi.object().unknown().keys({
        accessKey: Joi.string().required(),
        type: Joi.string().required().only('EventGrid'),
        uri: Joi.string().required()
    });
}
exports.azureEventGridDestinationType = azureEventGridDestinationType;
function azureServiceBusDestinationType() {
    return Joi.object().unknown().keys({
        connectionString: Joi.string().required(),
        type: Joi.string().required().only('AzureServiceBus')
    });
}
exports.azureServiceBusDestinationType = azureServiceBusDestinationType;
function changeSubscriptionType() {
    return Joi.object().unknown().keys({
        resourceTypeId: Joi.string().required()
    });
}
exports.changeSubscriptionType = changeSubscriptionType;
function deliveryCloudEventsFormatType() {
    return Joi.object().unknown().keys({
        cloudEventsVersion: Joi.string().required(),
        type: Joi.string().required().only('CloudEvents')
    });
}
exports.deliveryCloudEventsFormatType = deliveryCloudEventsFormatType;
function deliveryFormatType() {
    return Joi.alternatives([deliveryPlatformFormatType(), deliveryCloudEventsFormatType()]);
}
exports.deliveryFormatType = deliveryFormatType;
function deliveryPlatformFormatType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('Platform')
    });
}
exports.deliveryPlatformFormatType = deliveryPlatformFormatType;
function destinationType() {
    return Joi.alternatives([googleCloudPubSubDestinationType(), ironMqDestinationType(), snsDestinationType(), sqsDestinationType(), azureEventGridDestinationType(), azureServiceBusDestinationType()]);
}
exports.destinationType = destinationType;
function googleCloudPubSubDestinationType() {
    return Joi.object().unknown().keys({
        projectId: Joi.string().required(),
        topic: Joi.string().required(),
        type: Joi.string().required().only('GoogleCloudPubSub')
    });
}
exports.googleCloudPubSubDestinationType = googleCloudPubSubDestinationType;
function ironMqDestinationType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('IronMQ'),
        uri: Joi.string().required()
    });
}
exports.ironMqDestinationType = ironMqDestinationType;
function messageDeliveryType() {
    return Joi.object().unknown().keys({
        payloadNotIncluded: payloadNotIncludedType().required(),
        resource: Common_types_1.referenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        resourceVersion: Joi.number().required(),
        sequenceNumber: Joi.number().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        notificationType: Joi.string().required().only('Message'),
        projectKey: Joi.string().required(),
        resourceUserProvidedIdentifiers: Message_types_1.userProvidedIdentifiersType().optional()
    });
}
exports.messageDeliveryType = messageDeliveryType;
function messageSubscriptionType() {
    return Joi.object().unknown().keys({
        resourceTypeId: Joi.string().required(),
        types: Joi.array().items(Joi.string()).optional()
    });
}
exports.messageSubscriptionType = messageSubscriptionType;
function payloadNotIncludedType() {
    return Joi.object().unknown().keys({
        payloadType: Joi.string().required(),
        reason: Joi.string().required()
    });
}
exports.payloadNotIncludedType = payloadNotIncludedType;
function resourceCreatedDeliveryType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        modifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        notificationType: Joi.string().required().only('ResourceCreated'),
        projectKey: Joi.string().required(),
        resourceUserProvidedIdentifiers: Message_types_1.userProvidedIdentifiersType().optional()
    });
}
exports.resourceCreatedDeliveryType = resourceCreatedDeliveryType;
function resourceDeletedDeliveryType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        modifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        notificationType: Joi.string().required().only('ResourceDeleted'),
        projectKey: Joi.string().required(),
        resourceUserProvidedIdentifiers: Message_types_1.userProvidedIdentifiersType().optional()
    });
}
exports.resourceDeletedDeliveryType = resourceDeletedDeliveryType;
function resourceUpdatedDeliveryType() {
    return Joi.object().unknown().keys({
        resource: Common_types_1.referenceType().required(),
        modifiedAt: Joi.date().iso().required(),
        oldVersion: Joi.number().required(),
        version: Joi.number().required(),
        notificationType: Joi.string().required().only('ResourceUpdated'),
        projectKey: Joi.string().required(),
        resourceUserProvidedIdentifiers: Message_types_1.userProvidedIdentifiersType().optional()
    });
}
exports.resourceUpdatedDeliveryType = resourceUpdatedDeliveryType;
function snsDestinationType() {
    return Joi.object().unknown().keys({
        accessKey: Joi.string().required(),
        accessSecret: Joi.string().required(),
        topicArn: Joi.string().required(),
        type: Joi.string().required().only('SNS')
    });
}
exports.snsDestinationType = snsDestinationType;
function sqsDestinationType() {
    return Joi.object().unknown().keys({
        accessKey: Joi.string().required(),
        accessSecret: Joi.string().required(),
        queueUrl: Joi.string().required(),
        region: Joi.string().required(),
        type: Joi.string().required().only('SQS')
    });
}
exports.sqsDestinationType = sqsDestinationType;
function subscriptionType() {
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
        createdBy: Common_types_2.createdByType().optional(),
        lastModifiedBy: Common_types_3.lastModifiedByType().optional(),
        key: Joi.string().optional()
    });
}
exports.subscriptionType = subscriptionType;
function subscriptionDeliveryType() {
    return Joi.alternatives([messageDeliveryType(), resourceCreatedDeliveryType(), resourceDeletedDeliveryType(), resourceUpdatedDeliveryType()]);
}
exports.subscriptionDeliveryType = subscriptionDeliveryType;
function subscriptionDraftType() {
    return Joi.object().unknown().keys({
        destination: destinationType().required(),
        changes: Joi.array().items(changeSubscriptionType()).optional(),
        messages: Joi.array().items(messageSubscriptionType()).optional(),
        format: deliveryFormatType().optional(),
        key: Joi.string().optional()
    });
}
exports.subscriptionDraftType = subscriptionDraftType;
const subscriptionHealthStatusTypeValues = [
    'Healthy',
    'ConfigurationError',
    'ConfigurationErrorDeliveryStopped',
    'TemporaryError'
];
function subscriptionHealthStatusType() {
    return Joi.string().only(subscriptionHealthStatusTypeValues);
}
exports.subscriptionHealthStatusType = subscriptionHealthStatusType;
function subscriptionPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(subscriptionType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.subscriptionPagedQueryResponseType = subscriptionPagedQueryResponseType;
function subscriptionUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(subscriptionUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.subscriptionUpdateType = subscriptionUpdateType;
function subscriptionUpdateActionType() {
    return Joi.alternatives([subscriptionChangeDestinationActionType(), subscriptionSetChangesActionType(), subscriptionSetKeyActionType(), subscriptionSetMessagesActionType()]);
}
exports.subscriptionUpdateActionType = subscriptionUpdateActionType;
function subscriptionChangeDestinationActionType() {
    return Joi.object().unknown().keys({
        destination: destinationType().required(),
        action: Joi.string().required().only('changeDestination')
    });
}
exports.subscriptionChangeDestinationActionType = subscriptionChangeDestinationActionType;
function subscriptionSetChangesActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setChanges'),
        changes: Joi.array().items(changeSubscriptionType()).optional()
    });
}
exports.subscriptionSetChangesActionType = subscriptionSetChangesActionType;
function subscriptionSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.subscriptionSetKeyActionType = subscriptionSetKeyActionType;
function subscriptionSetMessagesActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMessages'),
        messages: Joi.array().items(messageSubscriptionType()).optional()
    });
}
exports.subscriptionSetMessagesActionType = subscriptionSetMessagesActionType;
