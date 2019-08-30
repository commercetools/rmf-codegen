/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { localizedStringType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { loggedResourceType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'

export function customFieldBooleanTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('Boolean')
          })
}

export function customFieldDateTimeTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('DateTime')
          })
}

export function customFieldDateTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('Date')
          })
}

export function customFieldEnumTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(customFieldEnumValueType()).required(),
             name: Joi.string().required().only('Enum')
          })
}

export function customFieldEnumValueType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             key: Joi.string().required(),
             label: Joi.string().required()
          })
}

export function customFieldLocalizedEnumTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(customFieldLocalizedEnumValueType()).required(),
             name: Joi.string().required().only('LocalizedEnum')
          })
}

export function customFieldLocalizedEnumValueType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             key: Joi.string().required()
          })
}

export function customFieldLocalizedStringTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('LocalizedString')
          })
}

export function customFieldMoneyTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('Money')
          })
}

export function customFieldNumberTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('Number')
          })
}

export function customFieldReferenceTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             referenceTypeId: referenceTypeIdType().required(),
             name: Joi.string().required().only('Reference')
          })
}

export function customFieldSetTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             elementType: fieldTypeType().required(),
             name: Joi.string().required().only('Set')
          })
}

export function customFieldStringTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('String')
          })
}

export function customFieldTimeTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('Time')
          })
}

export function customFieldsType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fields: fieldContainerType().required(),
             type: typeReferenceType().required()
          })
}

export function customFieldsDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: typeResourceIdentifierType().required(),
             fields: fieldContainerType().optional()
          })
}

export function fieldContainerType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('//'), Joi.any())
}

export function fieldDefinitionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             required: Joi.boolean().required(),
             type: Joi.any().required(),
             name: Joi.string().required(),
             inputHint: typeTextInputHintType().optional()
          })
}

export function fieldTypeType(): Joi.AnySchema {
   return Joi.alternatives([customFieldBooleanTypeType(), customFieldDateTimeTypeType(), customFieldDateTypeType(), customFieldEnumTypeType(), customFieldLocalizedEnumTypeType(), customFieldLocalizedStringTypeType(), customFieldMoneyTypeType(), customFieldNumberTypeType(), customFieldReferenceTypeType(), customFieldSetTypeType(), customFieldStringTypeType(), customFieldTimeTypeType()])
}

const resourceTypeIdTypeValues = [

   'asset',
   'category',
   'channel',
   'customer',
   'order',
   'order-edit',
   'inventory-entry',
   'line-item',
   'custom-line-item',
   'product-price',
   'payment',
   'payment-interface-interaction',
   'review',
   'shopping-list',
   'shopping-list-text-line-item',
   'discount-code',
   'cart-discount',
   'customer-group'

]

export function resourceTypeIdType(): Joi.AnySchema {
   return Joi.string().only(resourceTypeIdTypeValues)
}

export function typeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fieldDefinitions: Joi.array().items(fieldDefinitionType()).required(),
             resourceTypeIds: Joi.array().items(resourceTypeIdType()).required(),
             name: localizedStringType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             key: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional()
          })
}

export function typeDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             resourceTypeIds: Joi.array().items(resourceTypeIdType()).required(),
             name: localizedStringType().required(),
             key: Joi.string().required(),
             fieldDefinitions: Joi.array().items(fieldDefinitionType()).optional(),
             description: localizedStringType().optional()
          })
}

export function typePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(typeType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function typeReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('type'),
             id: Joi.string().required(),
             obj: typeType().optional()
          })
}

export function typeResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('type'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

const typeTextInputHintTypeValues = [

   'SingleLine',
   'MultiLine'

]

export function typeTextInputHintType(): Joi.AnySchema {
   return Joi.string().only(typeTextInputHintTypeValues)
}

export function typeUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(typeUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function typeUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([typeAddEnumValueActionType(), typeAddFieldDefinitionActionType(), typeAddLocalizedEnumValueActionType(), typeChangeEnumValueLabelActionType(), typeChangeEnumValueOrderActionType(), typeChangeFieldDefinitionLabelActionType(), typeChangeFieldDefinitionOrderActionType(), typeChangeInputHintActionType(), typeChangeKeyActionType(), typeChangeLabelActionType(), typeChangeLocalizedEnumValueLabelActionType(), typeChangeLocalizedEnumValueOrderActionType(), typeChangeNameActionType(), typeRemoveFieldDefinitionActionType(), typeSetDescriptionActionType()])
}

export function typeAddEnumValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: customFieldEnumValueType().required(),
             action: Joi.string().required().only('addEnumValue'),
             fieldName: Joi.string().required()
          })
}

export function typeAddFieldDefinitionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fieldDefinition: fieldDefinitionType().required(),
             action: Joi.string().required().only('addFieldDefinition')
          })
}

export function typeAddLocalizedEnumValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: customFieldLocalizedEnumValueType().required(),
             action: Joi.string().required().only('addLocalizedEnumValue'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeEnumValueLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: customFieldEnumValueType().required(),
             action: Joi.string().required().only('changeEnumValueLabel'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeEnumValueOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             keys: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeEnumValueOrder'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeFieldDefinitionLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             action: Joi.string().required().only('changeFieldDefinitionLabel'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeFieldDefinitionOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             fieldNames: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeFieldDefinitionOrder')
          })
}

export function typeChangeInputHintActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             inputHint: typeTextInputHintType().required(),
             action: Joi.string().required().only('changeInputHint'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeKey'),
             key: Joi.string().required()
          })
}

export function typeChangeLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             action: Joi.string().required().only('changeLabel'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeLocalizedEnumValueLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: customFieldLocalizedEnumValueType().required(),
             action: Joi.string().required().only('changeLocalizedEnumValueLabel'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeLocalizedEnumValueOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             keys: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeLocalizedEnumValueOrder'),
             fieldName: Joi.string().required()
          })
}

export function typeChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function typeRemoveFieldDefinitionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeFieldDefinition'),
             fieldName: Joi.string().required()
          })
}

export function typeSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}