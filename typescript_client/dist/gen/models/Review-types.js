"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const State_types_1 = require("./State-types");
const Customer_types_1 = require("./Customer-types");
const Type_types_2 = require("./Type-types");
const State_types_2 = require("./State-types");
const Customer_types_2 = require("./Customer-types");
const Common_types_3 = require("./Common-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function reviewType() {
    return Joi.object().unknown().keys({
        includedInStatistics: Joi.boolean().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        target: Joi.any().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customer: Customer_types_1.customerReferenceType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        state: State_types_1.stateReferenceType().optional(),
        rating: Joi.number().optional(),
        authorName: Joi.string().optional(),
        key: Joi.string().optional(),
        locale: Joi.string().optional(),
        text: Joi.string().optional(),
        title: Joi.string().optional(),
        uniquenessValue: Joi.string().optional()
    });
}
exports.reviewType = reviewType;
function reviewDraftType() {
    return Joi.object().unknown().keys({
        target: Joi.any().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customer: Customer_types_2.customerResourceIdentifierType().optional(),
        state: State_types_2.stateResourceIdentifierType().optional(),
        rating: Joi.number().optional(),
        authorName: Joi.string().optional(),
        key: Joi.string().optional(),
        locale: Joi.string().optional(),
        text: Joi.string().optional(),
        title: Joi.string().optional(),
        uniquenessValue: Joi.string().optional()
    });
}
exports.reviewDraftType = reviewDraftType;
function reviewPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(reviewType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.reviewPagedQueryResponseType = reviewPagedQueryResponseType;
function reviewRatingStatisticsType() {
    return Joi.object().unknown().keys({
        count: Joi.number().required(),
        averageRating: Joi.number().required(),
        highestRating: Joi.number().required(),
        lowestRating: Joi.number().required(),
        ratingsDistribution: Joi.any().required()
    });
}
exports.reviewRatingStatisticsType = reviewRatingStatisticsType;
function reviewReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().required().only('review'),
        id: Joi.string().required(),
        obj: reviewType().optional()
    });
}
exports.reviewReferenceType = reviewReferenceType;
function reviewResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_3.referenceTypeIdType().optional().only('review'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.reviewResourceIdentifierType = reviewResourceIdentifierType;
function reviewUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(reviewUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.reviewUpdateType = reviewUpdateType;
function reviewUpdateActionType() {
    return Joi.alternatives([reviewSetAuthorNameActionType(), reviewSetCustomFieldActionType(), reviewSetCustomTypeActionType(), reviewSetCustomerActionType(), reviewSetKeyActionType(), reviewSetLocaleActionType(), reviewSetRatingActionType(), reviewSetTargetActionType(), reviewSetTextActionType(), reviewSetTitleActionType(), reviewTransitionStateActionType()]);
}
exports.reviewUpdateActionType = reviewUpdateActionType;
function reviewSetAuthorNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAuthorName'),
        authorName: Joi.string().optional()
    });
}
exports.reviewSetAuthorNameActionType = reviewSetAuthorNameActionType;
function reviewSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.reviewSetCustomFieldActionType = reviewSetCustomFieldActionType;
function reviewSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.reviewSetCustomTypeActionType = reviewSetCustomTypeActionType;
function reviewSetCustomerActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomer'),
        customer: Customer_types_2.customerResourceIdentifierType().optional()
    });
}
exports.reviewSetCustomerActionType = reviewSetCustomerActionType;
function reviewSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.reviewSetKeyActionType = reviewSetKeyActionType;
function reviewSetLocaleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLocale'),
        locale: Joi.string().optional()
    });
}
exports.reviewSetLocaleActionType = reviewSetLocaleActionType;
function reviewSetRatingActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setRating'),
        rating: Joi.number().optional()
    });
}
exports.reviewSetRatingActionType = reviewSetRatingActionType;
function reviewSetTargetActionType() {
    return Joi.object().unknown().keys({
        target: Joi.any().required(),
        action: Joi.string().required().only('setTarget')
    });
}
exports.reviewSetTargetActionType = reviewSetTargetActionType;
function reviewSetTextActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setText'),
        text: Joi.string().optional()
    });
}
exports.reviewSetTextActionType = reviewSetTextActionType;
function reviewSetTitleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTitle'),
        title: Joi.string().optional()
    });
}
exports.reviewSetTitleActionType = reviewSetTitleActionType;
function reviewTransitionStateActionType() {
    return Joi.object().unknown().keys({
        state: State_types_2.stateResourceIdentifierType().required(),
        action: Joi.string().required().only('transitionState'),
        force: Joi.boolean().optional()
    });
}
exports.reviewTransitionStateActionType = reviewTransitionStateActionType;
