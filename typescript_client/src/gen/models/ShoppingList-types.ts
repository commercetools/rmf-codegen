/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { customFieldsType } from './Type-types'
import { localizedStringType } from './Common-types'
import { customerReferenceType } from './Customer-types'
import { loggedResourceType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { customerResourceIdentifierType } from './Customer-types'
import { productVariantType } from './Product-types'
import { productTypeReferenceType } from './ProductType-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { fieldContainerType } from './Type-types'
import { typeResourceIdentifierType } from './Type-types'

export function shoppingListType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             lineItems: Joi.array().items(shoppingListLineItemType()).optional(),
             textLineItems: Joi.array().items(textLineItemType()).optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             customer: customerReferenceType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             slug: localizedStringType().optional(),
             deleteDaysAfterLastModification: Joi.number().optional(),
             anonymousId: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function shoppingListDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             lineItems: Joi.array().items(shoppingListLineItemDraftType()).optional(),
             textLineItems: Joi.array().items(textLineItemDraftType()).optional(),
             custom: customFieldsDraftType().optional(),
             customer: customerResourceIdentifierType().optional(),
             description: localizedStringType().optional(),
             slug: localizedStringType().optional(),
             deleteDaysAfterLastModification: Joi.number().optional(),
             anonymousId: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function shoppingListLineItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             productType: productTypeReferenceType().required(),
             addedAt: Joi.date().iso().required(),
             quantity: Joi.number().required(),
             id: Joi.string().required(),
             productId: Joi.string().required(),
             custom: customFieldsType().optional(),
             productSlug: localizedStringType().optional(),
             variant: productVariantType().optional(),
             deactivatedAt: Joi.date().iso().optional(),
             variantId: Joi.number().optional()
          })
}

export function shoppingListLineItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             custom: customFieldsDraftType().optional(),
             addedAt: Joi.date().iso().optional(),
             quantity: Joi.number().optional(),
             variantId: Joi.number().optional(),
             productId: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function shoppingListPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(shoppingListType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function shoppingListReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('shopping-list'),
             id: Joi.string().required(),
             obj: shoppingListType().optional()
          })
}

export function shoppingListResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('shopping-list'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function shoppingListUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(shoppingListUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function shoppingListUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([shoppingListAddLineItemActionType(), shoppingListAddTextLineItemActionType(), shoppingListChangeLineItemQuantityActionType(), shoppingListChangeLineItemsOrderActionType(), shoppingListChangeNameActionType(), shoppingListChangeTextLineItemNameActionType(), shoppingListChangeTextLineItemQuantityActionType(), shoppingListChangeTextLineItemsOrderActionType(), shoppingListRemoveLineItemActionType(), shoppingListRemoveTextLineItemActionType(), shoppingListSetAnonymousIdActionType(), shoppingListSetCustomFieldActionType(), shoppingListSetCustomTypeActionType(), shoppingListSetCustomerActionType(), shoppingListSetDeleteDaysAfterLastModificationActionType(), shoppingListSetDescriptionActionType(), shoppingListSetKeyActionType(), shoppingListSetLineItemCustomFieldActionType(), shoppingListSetLineItemCustomTypeActionType(), shoppingListSetSlugActionType(), shoppingListSetTextLineItemCustomFieldActionType(), shoppingListSetTextLineItemCustomTypeActionType(), shoppingListSetTextLineItemDescriptionActionType()])
}

export function textLineItemType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             addedAt: Joi.date().iso().required(),
             quantity: Joi.number().required(),
             id: Joi.string().required(),
             custom: customFieldsType().optional(),
             description: localizedStringType().optional()
          })
}

export function textLineItemDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             addedAt: Joi.date().iso().optional(),
             quantity: Joi.number().optional()
          })
}

export function shoppingListAddLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addLineItem'),
             custom: customFieldsDraftType().optional(),
             addedAt: Joi.date().iso().optional(),
             quantity: Joi.number().optional(),
             variantId: Joi.number().optional(),
             productId: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function shoppingListAddTextLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('addTextLineItem'),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             addedAt: Joi.date().iso().optional(),
             quantity: Joi.number().optional()
          })
}

export function shoppingListChangeLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeLineItemQuantity'),
             lineItemId: Joi.string().required()
          })
}

export function shoppingListChangeLineItemsOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             lineItemOrder: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeLineItemsOrder')
          })
}

export function shoppingListChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function shoppingListChangeTextLineItemNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeTextLineItemName'),
             textLineItemId: Joi.string().required()
          })
}

export function shoppingListChangeTextLineItemQuantityActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             quantity: Joi.number().required(),
             action: Joi.string().required().only('changeTextLineItemQuantity'),
             textLineItemId: Joi.string().required()
          })
}

export function shoppingListChangeTextLineItemsOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             textLineItemOrder: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeTextLineItemsOrder')
          })
}

export function shoppingListRemoveLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeLineItem'),
             lineItemId: Joi.string().required(),
             quantity: Joi.number().optional()
          })
}

export function shoppingListRemoveTextLineItemActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeTextLineItem'),
             textLineItemId: Joi.string().required(),
             quantity: Joi.number().optional()
          })
}

export function shoppingListSetAnonymousIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAnonymousId'),
             anonymousId: Joi.string().optional()
          })
}

export function shoppingListSetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function shoppingListSetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function shoppingListSetCustomerActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomer'),
             customer: customerResourceIdentifierType().optional()
          })
}

export function shoppingListSetDeleteDaysAfterLastModificationActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDeleteDaysAfterLastModification'),
             deleteDaysAfterLastModification: Joi.number().optional()
          })
}

export function shoppingListSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}

export function shoppingListSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function shoppingListSetLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomField'),
             lineItemId: Joi.string().required(),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function shoppingListSetLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setLineItemCustomType'),
             lineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function shoppingListSetSlugActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setSlug'),
             slug: localizedStringType().optional()
          })
}

export function shoppingListSetTextLineItemCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTextLineItemCustomField'),
             name: Joi.string().required(),
             textLineItemId: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function shoppingListSetTextLineItemCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTextLineItemCustomType'),
             textLineItemId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function shoppingListSetTextLineItemDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTextLineItemDescription'),
             textLineItemId: Joi.string().required(),
             description: localizedStringType().optional()
          })
}