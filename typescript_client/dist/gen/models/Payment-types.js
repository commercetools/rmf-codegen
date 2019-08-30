"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Type_types_1 = require("./Type-types");
const Customer_types_1 = require("./Customer-types");
const Common_types_4 = require("./Common-types");
const Type_types_2 = require("./Type-types");
const Customer_types_2 = require("./Customer-types");
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const State_types_1 = require("./State-types");
const State_types_2 = require("./State-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
function paymentType() {
    return Joi.object().unknown().keys({
        interfaceInteractions: Joi.array().items(Type_types_1.customFieldsType()).required(),
        transactions: Joi.array().items(transactionType()).required(),
        paymentMethodInfo: paymentMethodInfoType().required(),
        paymentStatus: paymentStatusType().required(),
        amountPlanned: Common_types_3.typedMoneyType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customer: Customer_types_1.customerReferenceType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        amountAuthorized: Common_types_3.typedMoneyType().optional(),
        amountPaid: Common_types_3.typedMoneyType().optional(),
        amountRefunded: Common_types_3.typedMoneyType().optional(),
        anonymousId: Joi.string().optional(),
        authorizedUntil: Joi.string().optional(),
        externalId: Joi.string().optional(),
        interfaceId: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.paymentType = paymentType;
function paymentDraftType() {
    return Joi.object().unknown().keys({
        amountPlanned: Common_types_4.moneyType().required(),
        interfaceInteractions: Joi.array().items(Type_types_2.customFieldsDraftType()).optional(),
        transactions: Joi.array().items(transactionDraftType()).optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customer: Customer_types_2.customerResourceIdentifierType().optional(),
        amountAuthorized: Common_types_4.moneyType().optional(),
        amountPaid: Common_types_4.moneyType().optional(),
        amountRefunded: Common_types_4.moneyType().optional(),
        paymentMethodInfo: paymentMethodInfoType().optional(),
        paymentStatus: paymentStatusDraftType().optional(),
        anonymousId: Joi.string().optional(),
        authorizedUntil: Joi.string().optional(),
        externalId: Joi.string().optional(),
        interfaceId: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.paymentDraftType = paymentDraftType;
function paymentMethodInfoType() {
    return Joi.object().unknown().keys({
        name: Common_types_5.localizedStringType().optional(),
        method: Joi.string().optional(),
        paymentInterface: Joi.string().optional()
    });
}
exports.paymentMethodInfoType = paymentMethodInfoType;
function paymentPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(paymentType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.paymentPagedQueryResponseType = paymentPagedQueryResponseType;
function paymentReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().required().only('payment'),
        id: Joi.string().required(),
        obj: paymentType().optional()
    });
}
exports.paymentReferenceType = paymentReferenceType;
function paymentResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().optional().only('payment'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.paymentResourceIdentifierType = paymentResourceIdentifierType;
function paymentStatusType() {
    return Joi.object().unknown().keys({
        state: State_types_1.stateReferenceType().optional(),
        interfaceCode: Joi.string().optional(),
        interfaceText: Joi.string().optional()
    });
}
exports.paymentStatusType = paymentStatusType;
function paymentStatusDraftType() {
    return Joi.object().unknown().keys({
        state: State_types_2.stateResourceIdentifierType().optional(),
        interfaceCode: Joi.string().optional(),
        interfaceText: Joi.string().optional()
    });
}
exports.paymentStatusDraftType = paymentStatusDraftType;
function paymentUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(paymentUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.paymentUpdateType = paymentUpdateType;
function paymentUpdateActionType() {
    return Joi.alternatives([paymentAddInterfaceInteractionActionType(), paymentAddTransactionActionType(), paymentChangeAmountPlannedActionType(), paymentChangeTransactionInteractionIdActionType(), paymentChangeTransactionStateActionType(), paymentChangeTransactionTimestampActionType(), paymentSetAmountPaidActionType(), paymentSetAmountRefundedActionType(), paymentSetAnonymousIdActionType(), paymentSetAuthorizationActionType(), paymentSetCustomFieldActionType(), paymentSetCustomTypeActionType(), paymentSetCustomerActionType(), paymentSetExternalIdActionType(), paymentSetInterfaceIdActionType(), paymentSetKeyActionType(), paymentSetMethodInfoInterfaceActionType(), paymentSetMethodInfoMethodActionType(), paymentSetMethodInfoNameActionType(), paymentSetStatusInterfaceCodeActionType(), paymentSetStatusInterfaceTextActionType(), paymentTransitionStateActionType()]);
}
exports.paymentUpdateActionType = paymentUpdateActionType;
function transactionType() {
    return Joi.object().unknown().keys({
        type: transactionTypeType().required(),
        amount: Common_types_3.typedMoneyType().required(),
        id: Joi.string().required(),
        state: transactionStateType().optional(),
        timestamp: Joi.date().iso().optional(),
        interactionId: Joi.string().optional()
    });
}
exports.transactionType = transactionType;
function transactionDraftType() {
    return Joi.object().unknown().keys({
        amount: Common_types_4.moneyType().required(),
        type: transactionTypeType().required(),
        state: transactionStateType().optional(),
        timestamp: Joi.date().iso().optional(),
        interactionId: Joi.string().optional()
    });
}
exports.transactionDraftType = transactionDraftType;
const transactionStateTypeValues = [
    'Initial',
    'Pending',
    'Success',
    'Failure'
];
function transactionStateType() {
    return Joi.string().only(transactionStateTypeValues);
}
exports.transactionStateType = transactionStateType;
const transactionTypeTypeValues = [
    'Authorization',
    'CancelAuthorization',
    'Charge',
    'Refund',
    'Chargeback'
];
function transactionTypeType() {
    return Joi.string().only(transactionTypeTypeValues);
}
exports.transactionTypeType = transactionTypeType;
function paymentAddInterfaceInteractionActionType() {
    return Joi.object().unknown().keys({
        type: Type_types_4.typeResourceIdentifierType().required(),
        action: Joi.string().required().only('addInterfaceInteraction'),
        fields: Type_types_3.fieldContainerType().optional()
    });
}
exports.paymentAddInterfaceInteractionActionType = paymentAddInterfaceInteractionActionType;
function paymentAddTransactionActionType() {
    return Joi.object().unknown().keys({
        transaction: transactionDraftType().required(),
        action: Joi.string().required().only('addTransaction')
    });
}
exports.paymentAddTransactionActionType = paymentAddTransactionActionType;
function paymentChangeAmountPlannedActionType() {
    return Joi.object().unknown().keys({
        amount: Common_types_4.moneyType().required(),
        action: Joi.string().required().only('changeAmountPlanned')
    });
}
exports.paymentChangeAmountPlannedActionType = paymentChangeAmountPlannedActionType;
function paymentChangeTransactionInteractionIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeTransactionInteractionId'),
        interactionId: Joi.string().required(),
        transactionId: Joi.string().required()
    });
}
exports.paymentChangeTransactionInteractionIdActionType = paymentChangeTransactionInteractionIdActionType;
function paymentChangeTransactionStateActionType() {
    return Joi.object().unknown().keys({
        state: transactionStateType().required(),
        action: Joi.string().required().only('changeTransactionState'),
        transactionId: Joi.string().required()
    });
}
exports.paymentChangeTransactionStateActionType = paymentChangeTransactionStateActionType;
function paymentChangeTransactionTimestampActionType() {
    return Joi.object().unknown().keys({
        timestamp: Joi.date().iso().required(),
        action: Joi.string().required().only('changeTransactionTimestamp'),
        transactionId: Joi.string().required()
    });
}
exports.paymentChangeTransactionTimestampActionType = paymentChangeTransactionTimestampActionType;
function paymentSetAmountPaidActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAmountPaid'),
        amount: Common_types_4.moneyType().optional()
    });
}
exports.paymentSetAmountPaidActionType = paymentSetAmountPaidActionType;
function paymentSetAmountRefundedActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAmountRefunded'),
        amount: Common_types_4.moneyType().optional()
    });
}
exports.paymentSetAmountRefundedActionType = paymentSetAmountRefundedActionType;
function paymentSetAnonymousIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAnonymousId'),
        anonymousId: Joi.string().optional()
    });
}
exports.paymentSetAnonymousIdActionType = paymentSetAnonymousIdActionType;
function paymentSetAuthorizationActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAuthorization'),
        amount: Common_types_4.moneyType().optional(),
        until: Joi.date().iso().optional()
    });
}
exports.paymentSetAuthorizationActionType = paymentSetAuthorizationActionType;
function paymentSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.paymentSetCustomFieldActionType = paymentSetCustomFieldActionType;
function paymentSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.paymentSetCustomTypeActionType = paymentSetCustomTypeActionType;
function paymentSetCustomerActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomer'),
        customer: Customer_types_2.customerResourceIdentifierType().optional()
    });
}
exports.paymentSetCustomerActionType = paymentSetCustomerActionType;
function paymentSetExternalIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setExternalId'),
        externalId: Joi.string().optional()
    });
}
exports.paymentSetExternalIdActionType = paymentSetExternalIdActionType;
function paymentSetInterfaceIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setInterfaceId'),
        interfaceId: Joi.string().required()
    });
}
exports.paymentSetInterfaceIdActionType = paymentSetInterfaceIdActionType;
function paymentSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.paymentSetKeyActionType = paymentSetKeyActionType;
function paymentSetMethodInfoInterfaceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMethodInfoInterface'),
        interface: Joi.string().required()
    });
}
exports.paymentSetMethodInfoInterfaceActionType = paymentSetMethodInfoInterfaceActionType;
function paymentSetMethodInfoMethodActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMethodInfoMethod'),
        method: Joi.string().optional()
    });
}
exports.paymentSetMethodInfoMethodActionType = paymentSetMethodInfoMethodActionType;
function paymentSetMethodInfoNameActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMethodInfoName'),
        name: Common_types_5.localizedStringType().optional()
    });
}
exports.paymentSetMethodInfoNameActionType = paymentSetMethodInfoNameActionType;
function paymentSetStatusInterfaceCodeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setStatusInterfaceCode'),
        interfaceCode: Joi.string().optional()
    });
}
exports.paymentSetStatusInterfaceCodeActionType = paymentSetStatusInterfaceCodeActionType;
function paymentSetStatusInterfaceTextActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setStatusInterfaceText'),
        interfaceText: Joi.string().required()
    });
}
exports.paymentSetStatusInterfaceTextActionType = paymentSetStatusInterfaceTextActionType;
function paymentTransitionStateActionType() {
    return Joi.object().unknown().keys({
        state: State_types_2.stateResourceIdentifierType().required(),
        action: Joi.string().required().only('transitionState'),
        force: Joi.boolean().optional()
    });
}
exports.paymentTransitionStateActionType = paymentTransitionStateActionType;
