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

export function attributeBooleanTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('boolean')
          })
}

const attributeConstraintEnumTypeValues = [

   'None',
   'Unique',
   'CombinationUnique',
   'SameForAll'

]

export function attributeConstraintEnumType(): Joi.AnySchema {
   return Joi.string().only(attributeConstraintEnumTypeValues)
}

const attributeConstraintEnumDraftTypeValues = [

   'None'

]

export function attributeConstraintEnumDraftType(): Joi.AnySchema {
   return Joi.string().only(attributeConstraintEnumDraftTypeValues)
}

export function attributeDateTimeTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('datetime')
          })
}

export function attributeDateTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('date')
          })
}

export function attributeDefinitionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributeConstraint: attributeConstraintEnumType().required(),
             type: attributeTypeType().required(),
             label: localizedStringType().required(),
             inputHint: textInputHintType().required(),
             isRequired: Joi.boolean().required(),
             isSearchable: Joi.boolean().required(),
             name: Joi.string().required(),
             inputTip: localizedStringType().optional()
          })
}

export function attributeDefinitionDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: attributeTypeType().required(),
             label: localizedStringType().required(),
             isRequired: Joi.boolean().required(),
             name: Joi.string().required(),
             attributeConstraint: attributeConstraintEnumType().optional(),
             inputTip: localizedStringType().optional(),
             inputHint: textInputHintType().optional(),
             isSearchable: Joi.boolean().optional()
          })
}

export function attributeEnumTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(attributePlainEnumValueType()).required(),
             name: Joi.string().required().only('enum')
          })
}

export function attributeLocalizableTextTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('ltext')
          })
}

export function attributeLocalizedEnumTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(attributeLocalizedEnumValueType()).required(),
             name: Joi.string().required().only('lenum')
          })
}

export function attributeLocalizedEnumValueType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             key: Joi.string().required()
          })
}

export function attributeMoneyTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('money')
          })
}

export function attributeNestedTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeReference: productTypeReferenceType().required(),
             name: Joi.string().required().only('nested')
          })
}

export function attributeNumberTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('number')
          })
}

export function attributePlainEnumValueType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             key: Joi.string().required(),
             label: Joi.string().required()
          })
}

export function attributeReferenceTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             referenceTypeId: referenceTypeIdType().required(),
             name: Joi.string().required().only('reference')
          })
}

export function attributeSetTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             elementType: attributeTypeType().required(),
             name: Joi.string().required().only('set')
          })
}

export function attributeTextTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('text')
          })
}

export function attributeTimeTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required().only('time')
          })
}

export function attributeTypeType(): Joi.AnySchema {
   return Joi.alternatives([attributeBooleanTypeType(), attributeDateTimeTypeType(), attributeDateTypeType(), attributeEnumTypeType(), attributeLocalizableTextTypeType(), attributeLocalizedEnumTypeType(), attributeMoneyTypeType(), attributeNestedTypeType(), attributeNumberTypeType(), attributeReferenceTypeType(), attributeSetTypeType(), attributeTextTypeType(), attributeTimeTypeType()])
}

export function productTypeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             description: Joi.string().required(),
             id: Joi.string().required(),
             name: Joi.string().required(),
             attributes: Joi.array().items(attributeDefinitionType()).optional(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             key: Joi.string().optional()
          })
}

export function productTypeDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             description: Joi.string().required(),
             name: Joi.string().required(),
             attributes: Joi.array().items(attributeDefinitionDraftType()).optional(),
             key: Joi.string().optional()
          })
}

export function productTypePagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(productTypeType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function productTypeReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('product-type'),
             id: Joi.string().required(),
             obj: productTypeType().optional()
          })
}

export function productTypeResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('product-type'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function productTypeUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(productTypeUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function productTypeUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([productTypeAddAttributeDefinitionActionType(), productTypeAddLocalizedEnumValueActionType(), productTypeAddPlainEnumValueActionType(), productTypeChangeAttributeConstraintActionType(), productTypeChangeAttributeNameActionType(), productTypeChangeAttributeOrderActionType(), productTypeChangeAttributeOrderByNameActionType(), productTypeChangeDescriptionActionType(), productTypeChangeEnumKeyActionType(), productTypeChangeInputHintActionType(), productTypeChangeIsSearchableActionType(), productTypeChangeLabelActionType(), productTypeChangeLocalizedEnumValueLabelActionType(), productTypeChangeLocalizedEnumValueOrderActionType(), productTypeChangeNameActionType(), productTypeChangePlainEnumValueLabelActionType(), productTypeChangePlainEnumValueOrderActionType(), productTypeRemoveAttributeDefinitionActionType(), productTypeRemoveEnumValuesActionType(), productTypeSetInputTipActionType(), productTypeSetKeyActionType()])
}

const textInputHintTypeValues = [

   'SingleLine',
   'MultiLine'

]

export function textInputHintType(): Joi.AnySchema {
   return Joi.string().only(textInputHintTypeValues)
}

export function productTypeAddAttributeDefinitionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attribute: attributeDefinitionDraftType().required(),
             action: Joi.string().required().only('addAttributeDefinition')
          })
}

export function productTypeAddLocalizedEnumValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: attributeLocalizedEnumValueType().required(),
             action: Joi.string().required().only('addLocalizedEnumValue'),
             attributeName: Joi.string().required()
          })
}

export function productTypeAddPlainEnumValueActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: attributePlainEnumValueType().required(),
             action: Joi.string().required().only('addPlainEnumValue'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeAttributeConstraintActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             newValue: attributeConstraintEnumDraftType().required(),
             action: Joi.string().required().only('changeAttributeConstraint'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeAttributeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeAttributeName'),
             attributeName: Joi.string().required(),
             newAttributeName: Joi.string().required()
          })
}

export function productTypeChangeAttributeOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributes: Joi.array().items(attributeDefinitionType()).required(),
             action: Joi.string().required().only('changeAttributeOrder')
          })
}

export function productTypeChangeAttributeOrderByNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             attributeNames: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeAttributeOrderByName')
          })
}

export function productTypeChangeDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeDescription'),
             description: Joi.string().required()
          })
}

export function productTypeChangeEnumKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeEnumKey'),
             attributeName: Joi.string().required(),
             key: Joi.string().required(),
             newKey: Joi.string().required()
          })
}

export function productTypeChangeInputHintActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             newValue: textInputHintType().required(),
             action: Joi.string().required().only('changeInputHint'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeIsSearchableActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isSearchable: Joi.boolean().required(),
             action: Joi.string().required().only('changeIsSearchable'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             label: localizedStringType().required(),
             action: Joi.string().required().only('changeLabel'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeLocalizedEnumValueLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             newValue: attributeLocalizedEnumValueType().required(),
             action: Joi.string().required().only('changeLocalizedEnumValueLabel'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeLocalizedEnumValueOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(attributeLocalizedEnumValueType()).required(),
             action: Joi.string().required().only('changeLocalizedEnumValueOrder'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeName'),
             name: Joi.string().required()
          })
}

export function productTypeChangePlainEnumValueLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             newValue: attributePlainEnumValueType().required(),
             action: Joi.string().required().only('changePlainEnumValueLabel'),
             attributeName: Joi.string().required()
          })
}

export function productTypeChangePlainEnumValueOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             values: Joi.array().items(attributePlainEnumValueType()).required(),
             action: Joi.string().required().only('changePlainEnumValueOrder'),
             attributeName: Joi.string().required()
          })
}

export function productTypeRemoveAttributeDefinitionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeAttributeDefinition'),
             name: Joi.string().required()
          })
}

export function productTypeRemoveEnumValuesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             keys: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('removeEnumValues'),
             attributeName: Joi.string().required()
          })
}

export function productTypeSetInputTipActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setInputTip'),
             attributeName: Joi.string().required(),
             inputTip: localizedStringType().optional()
          })
}

export function productTypeSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}