/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { typedMoneyType } from './Common-types'
import { customFieldsType } from './Type-types'
import { customerReferenceType } from './Customer-types'
import { loggedResourceType } from './Common-types'
import { moneyType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { customerResourceIdentifierType } from './Customer-types'
import { localizedStringType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { stateReferenceType } from './State-types'
import { stateResourceIdentifierType } from './State-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function paymentType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             interfaceInteractions: Joi.array().items(customFieldsType()).required(),
             transactions: Joi.array().items(transactionType()).required(),
             paymentMethodInfo: paymentMethodInfoType().required(),
             paymentStatus: paymentStatusType().required(),
             amountPlanned: typedMoneyType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customer: customerReferenceType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             amountAuthorized: typedMoneyType().optional(),
             amountPaid: typedMoneyType().optional(),
             amountRefunded: typedMoneyType().optional(),
             anonymousId: Joi.string().optional(),
             authorizedUntil: Joi.string().optional(),
             externalId: Joi.string().optional(),
             interfaceId: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function paymentDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             amountPlanned: moneyType().required(),
             interfaceInteractions: Joi.array().items(customFieldsDraftType()).optional(),
             transactions: Joi.array().items(transactionDraftType()).optional(),
             custom: customFieldsDraftType().optional(),
             customer: customerResourceIdentifierType().optional(),
             amountAuthorized: moneyType().optional(),
             amountPaid: moneyType().optional(),
             amountRefunded: moneyType().optional(),
             paymentMethodInfo: paymentMethodInfoType().optional(),
             paymentStatus: paymentStatusDraftType().optional(),
             anonymousId: Joi.string().optional(),
             authorizedUntil: Joi.string().optional(),
             externalId: Joi.string().optional(),
             interfaceId: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function paymentMethodInfoType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().optional(),
             method: Joi.string().optional(),
             paymentInterface: Joi.string().optional()
          })
}

export function paymentPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(paymentType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function paymentReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('payment'),
             id: Joi.string().required(),
             obj: paymentType().optional()
          })
}

export function paymentResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('payment'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function paymentStatusType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateReferenceType().optional(),
             interfaceCode: Joi.string().optional(),
             interfaceText: Joi.string().optional()
          })
}

export function paymentStatusDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateResourceIdentifierType().optional(),
             interfaceCode: Joi.string().optional(),
             interfaceText: Joi.string().optional()
          })
}

export function paymentUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(paymentUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function paymentUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([paymentAddInterfaceInteractionActionType(), paymentAddTransactionActionType(), paymentChangeAmountPlannedActionType(), paymentChangeTransactionInteractionIdActionType(), paymentChangeTransactionStateActionType(), paymentChangeTransactionTimestampActionType(), paymentSetAmountPaidActionType(), paymentSetAmountRefundedActionType(), paymentSetAnonymousIdActionType(), paymentSetAuthorizationActionType(), paymentSetCustomFieldActionType(), paymentSetCustomTypeActionType(), paymentSetCustomerActionType(), paymentSetExternalIdActionType(), paymentSetInterfaceIdActionType(), paymentSetKeyActionType(), paymentSetMethodInfoInterfaceActionType(), paymentSetMethodInfoMethodActionType(), paymentSetMethodInfoNameActionType(), paymentSetStatusInterfaceCodeActionType(), paymentSetStatusInterfaceTextActionType(), paymentTransitionStateActionType()])
}

export function transactionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: transactionTypeType().required(),
             amount: typedMoneyType().required(),
             id: Joi.string().required(),
             state: transactionStateType().optional(),
             timestamp: Joi.date().iso().optional(),
             interactionId: Joi.string().optional()
          })
}

export function transactionDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             amount: moneyType().required(),
             type: transactionTypeType().required(),
             state: transactionStateType().optional(),
             timestamp: Joi.date().iso().optional(),
             interactionId: Joi.string().optional()
          })
}

const transactionStateTypeValues = [

   'Initial',
   'Pending',
   'Success',
   'Failure'

]

export function transactionStateType(): Joi.AnySchema {
   return Joi.string().only(transactionStateTypeValues)
}

const transactionTypeTypeValues = [

   'Authorization',
   'CancelAuthorization',
   'Charge',
   'Refund',
   'Chargeback'

]

export function transactionTypeType(): Joi.AnySchema {
   return Joi.string().only(transactionTypeTypeValues)
}

export function paymentAddInterfaceInteractionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: typeResourceIdentifierType().required(),
             action: Joi.string().required().only('addInterfaceInteraction'),
             fields: fieldContainerType().optional()
          })
}

export function paymentAddTransactionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             transaction: transactionDraftType().required(),
             action: Joi.string().required().only('addTransaction')
          })
}

export function paymentChangeAmountPlannedActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             amount: moneyType().required(),
             action: Joi.string().required().only('changeAmountPlanned')
          })
}

export function paymentChangeTransactionInteractionIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeTransactionInteractionId'),
             interactionId: Joi.string().required(),
             transactionId: Joi.string().required()
          })
}

export function paymentChangeTransactionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: transactionStateType().required(),
             action: Joi.string().required().only('changeTransactionState'),
             transactionId: Joi.string().required()
          })
}

export function paymentChangeTransactionTimestampActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             timestamp: Joi.date().iso().required(),
             action: Joi.string().required().only('changeTransactionTimestamp'),
             transactionId: Joi.string().required()
          })
}

export function paymentSetAmountPaidActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAmountPaid'),
             amount: moneyType().optional()
          })
}

export function paymentSetAmountRefundedActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAmountRefunded'),
             amount: moneyType().optional()
          })
}

export function paymentSetAnonymousIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAnonymousId'),
             anonymousId: Joi.string().optional()
          })
}

export function paymentSetAuthorizationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAuthorization'),
             amount: moneyType().optional(),
             until: Joi.date().iso().optional()
          })
}

export function paymentSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function paymentSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function paymentSetCustomerActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomer'),
             customer: customerResourceIdentifierType().optional()
          })
}

export function paymentSetExternalIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setExternalId'),
             externalId: Joi.string().optional()
          })
}

export function paymentSetInterfaceIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setInterfaceId'),
             interfaceId: Joi.string().required()
          })
}

export function paymentSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function paymentSetMethodInfoInterfaceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMethodInfoInterface'),
             interface: Joi.string().required()
          })
}

export function paymentSetMethodInfoMethodActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMethodInfoMethod'),
             method: Joi.string().optional()
          })
}

export function paymentSetMethodInfoNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMethodInfoName'),
             name: localizedStringType().optional()
          })
}

export function paymentSetStatusInterfaceCodeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setStatusInterfaceCode'),
             interfaceCode: Joi.string().optional()
          })
}

export function paymentSetStatusInterfaceTextActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setStatusInterfaceText'),
             interfaceText: Joi.string().required()
          })
}

export function paymentTransitionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             state: stateResourceIdentifierType().required(),
             action: Joi.string().required().only('transitionState'),
             force: Joi.boolean().optional()
          })
}