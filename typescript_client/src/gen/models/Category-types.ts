/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { assetType } from './Common-types'
import { localizedStringType } from './Common-types'
import { customFieldsType } from './Type-types'
import { loggedResourceType } from './Common-types'
import { assetDraftType } from './Common-types'
import { customFieldsDraftType } from './Type-types'
import { referenceTypeIdType } from './Common-types'
import { referenceType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { typeResourceIdentifierType } from './Type-types'
import { assetSourceType } from './Common-types'
import { fieldContainerType } from './Type-types'

export function categoryType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             ancestors: Joi.array().items(categoryReferenceType()).required(),
             name: localizedStringType().required(),
             slug: localizedStringType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             orderHint: Joi.string().required(),
             assets: Joi.array().items(assetType()).optional(),
             parent: categoryReferenceType().optional(),
             createdBy: createdByType().optional(),
             custom: customFieldsType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             description: localizedStringType().optional(),
             metaDescription: localizedStringType().optional(),
             metaKeywords: localizedStringType().optional(),
             metaTitle: localizedStringType().optional(),
             externalId: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function categoryDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             slug: localizedStringType().required(),
             assets: Joi.array().items(assetDraftType()).optional(),
             parent: categoryResourceIdentifierType().optional(),
             custom: customFieldsDraftType().optional(),
             description: localizedStringType().optional(),
             metaDescription: localizedStringType().optional(),
             metaKeywords: localizedStringType().optional(),
             metaTitle: localizedStringType().optional(),
             externalId: Joi.string().optional(),
             key: Joi.string().optional(),
             orderHint: Joi.string().optional()
          })
}

export function categoryPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(categoryType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function categoryReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('category'),
             id: Joi.string().required(),
             obj: categoryType().optional()
          })
}

export function categoryResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('category'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function categoryUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(categoryUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function categoryUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([categoryAddAssetActionType(), categoryChangeAssetNameActionType(), categoryChangeAssetOrderActionType(), categoryChangeNameActionType(), categoryChangeOrderHintActionType(), categoryChangeParentActionType(), categoryChangeSlugActionType(), categoryRemoveAssetActionType(), categorySetAssetCustomFieldActionType(), categorySetAssetCustomTypeActionType(), categorySetAssetDescriptionActionType(), categorySetAssetKeyActionType(), categorySetAssetSourcesActionType(), categorySetAssetTagsActionType(), categorySetCustomFieldActionType(), categorySetCustomTypeActionType(), categorySetDescriptionActionType(), categorySetExternalIdActionType(), categorySetKeyActionType(), categorySetMetaDescriptionActionType(), categorySetMetaKeywordsActionType(), categorySetMetaTitleActionType()])
}

export function categoryAddAssetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             asset: assetDraftType().required(),
             action: Joi.string().required().only('addAsset'),
             position: Joi.number().optional()
          })
}

export function categoryChangeAssetNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeAssetName'),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categoryChangeAssetOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             assetOrder: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeAssetOrder')
          })
}

export function categoryChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName')
          })
}

export function categoryChangeOrderHintActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeOrderHint'),
             orderHint: Joi.string().required()
          })
}

export function categoryChangeParentActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             parent: categoryResourceIdentifierType().required(),
             action: Joi.string().required().only('changeParent')
          })
}

export function categoryChangeSlugActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             action: Joi.string().required().only('changeSlug')
          })
}

export function categoryRemoveAssetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeAsset'),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetCustomType'),
             type: typeResourceIdentifierType().optional(),
             fields: Joi.any().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetDescription'),
             description: localizedStringType().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetKey'),
             assetId: Joi.string().required(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetSourcesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             sources: Joi.array().items(assetSourceType()).required(),
             action: Joi.string().required().only('setAssetSources'),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetAssetTagsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetTags'),
             tags: Joi.array().items(Joi.string()).optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional()
          })
}

export function categorySetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional()
          })
}

export function categorySetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCustomType'),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional()
          })
}

export function categorySetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional()
          })
}

export function categorySetExternalIdActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setExternalId'),
             externalId: Joi.string().optional()
          })
}

export function categorySetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function categorySetMetaDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaDescription'),
             metaDescription: localizedStringType().optional()
          })
}

export function categorySetMetaKeywordsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaKeywords'),
             metaKeywords: localizedStringType().optional()
          })
}

export function categorySetMetaTitleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaTitle'),
             metaTitle: localizedStringType().optional()
          })
}