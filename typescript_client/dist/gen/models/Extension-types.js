"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
function extensionType() {
    return Joi.object().unknown().keys({
        triggers: Joi.array().items(extensionTriggerType()).required(),
        destination: extensionDestinationType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        timeoutInMs: Joi.number().optional(),
        key: Joi.string().optional()
    });
}
exports.extensionType = extensionType;
function extensionAWSLambdaDestinationType() {
    return Joi.object().unknown().keys({
        accessKey: Joi.string().required(),
        accessSecret: Joi.string().required(),
        arn: Joi.string().required(),
        type: Joi.string().required().only('AWSLambda')
    });
}
exports.extensionAWSLambdaDestinationType = extensionAWSLambdaDestinationType;
const extensionActionTypeValues = [
    'Create',
    'Update'
];
function extensionActionType() {
    return Joi.string().only(extensionActionTypeValues);
}
exports.extensionActionType = extensionActionType;
function extensionAuthorizationHeaderAuthenticationType() {
    return Joi.object().unknown().keys({
        headerValue: Joi.string().required(),
        type: Joi.string().required().only('AuthorizationHeader')
    });
}
exports.extensionAuthorizationHeaderAuthenticationType = extensionAuthorizationHeaderAuthenticationType;
function extensionAzureFunctionsAuthenticationType() {
    return Joi.object().unknown().keys({
        key: Joi.string().required(),
        type: Joi.string().required().only('AzureFunctions')
    });
}
exports.extensionAzureFunctionsAuthenticationType = extensionAzureFunctionsAuthenticationType;
function extensionDestinationType() {
    return Joi.alternatives([extensionHttpDestinationType(), extensionAWSLambdaDestinationType()]);
}
exports.extensionDestinationType = extensionDestinationType;
function extensionDraftType() {
    return Joi.object().unknown().keys({
        triggers: Joi.array().items(extensionTriggerType()).required(),
        destination: extensionDestinationType().required(),
        timeoutInMs: Joi.number().optional(),
        key: Joi.string().optional()
    });
}
exports.extensionDraftType = extensionDraftType;
function extensionHttpDestinationType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('HTTP'),
        url: Joi.string().required(),
        authentication: extensionHttpDestinationAuthenticationType().optional()
    });
}
exports.extensionHttpDestinationType = extensionHttpDestinationType;
function extensionHttpDestinationAuthenticationType() {
    return Joi.alternatives([extensionAuthorizationHeaderAuthenticationType(), extensionAzureFunctionsAuthenticationType()]);
}
exports.extensionHttpDestinationAuthenticationType = extensionHttpDestinationAuthenticationType;
function extensionInputType() {
    return Joi.object().unknown().keys({
        action: extensionActionType().required(),
        resource: Common_types_3.referenceType().required()
    });
}
exports.extensionInputType = extensionInputType;
function extensionPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(extensionType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.extensionPagedQueryResponseType = extensionPagedQueryResponseType;
const extensionResourceTypeIdTypeValues = [
    'cart',
    'order',
    'payment',
    'customer'
];
function extensionResourceTypeIdType() {
    return Joi.string().only(extensionResourceTypeIdTypeValues);
}
exports.extensionResourceTypeIdType = extensionResourceTypeIdType;
function extensionTriggerType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(extensionActionType()).required(),
        resourceTypeId: extensionResourceTypeIdType().required()
    });
}
exports.extensionTriggerType = extensionTriggerType;
function extensionUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(extensionUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.extensionUpdateType = extensionUpdateType;
function extensionUpdateActionType() {
    return Joi.alternatives([extensionChangeDestinationActionType(), extensionChangeTriggersActionType(), extensionSetKeyActionType(), extensionSetTimeoutInMsActionType()]);
}
exports.extensionUpdateActionType = extensionUpdateActionType;
function extensionChangeDestinationActionType() {
    return Joi.object().unknown().keys({
        destination: extensionDestinationType().required(),
        action: Joi.string().required().only('changeDestination')
    });
}
exports.extensionChangeDestinationActionType = extensionChangeDestinationActionType;
function extensionChangeTriggersActionType() {
    return Joi.object().unknown().keys({
        triggers: Joi.array().items(extensionTriggerType()).required(),
        action: Joi.string().required().only('changeTriggers')
    });
}
exports.extensionChangeTriggersActionType = extensionChangeTriggersActionType;
function extensionSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.extensionSetKeyActionType = extensionSetKeyActionType;
function extensionSetTimeoutInMsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTimeoutInMs'),
        timeoutInMs: Joi.number().optional()
    });
}
exports.extensionSetTimeoutInMsActionType = extensionSetTimeoutInMsActionType;
