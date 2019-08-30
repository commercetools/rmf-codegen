/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'
import { attributeLocalizedEnumValueType } from './ProductType-types'
import { attributePlainEnumValueType } from './ProductType-types'
import { referenceType } from './Common-types'
import { moneyType } from './Common-types'
import { localizedStringType } from './Common-types'
import { createdByType } from './Common-types'
import { lastModifiedByType } from './Common-types'
import { stateReferenceType } from './State-types'
import { reviewRatingStatisticsType } from './Review-types'
import { productTypeReferenceType } from './ProductType-types'
import { taxCategoryReferenceType } from './TaxCategory-types'
import { loggedResourceType } from './Common-types'
import { categoryReferenceType } from './Category-types'
import { taxCategoryResourceIdentifierType } from './TaxCategory-types'
import { stateResourceIdentifierType } from './State-types'
import { categoryResourceIdentifierType } from './Category-types'
import { productTypeResourceIdentifierType } from './ProductType-types'
import { baseResourceType } from './Common-types'
import { referenceTypeIdType } from './Common-types'
import { resourceIdentifierType } from './Common-types'
import { scopedPriceType } from './Common-types'
import { imageType } from './Common-types'
import { assetType } from './Common-types'
import { priceType } from './Common-types'
import { assetDraftType } from './Common-types'
import { priceDraftType } from './Common-types'
import { productPublishScopeType } from './Cart-types'
import { typeResourceIdentifierType } from './Type-types'
import { assetSourceType } from './Common-types'
import { discountedPriceType } from './Common-types'
import { fieldContainerType } from './Type-types'

export function attributeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             value: Joi.any().required(),
             name: Joi.string().required()
          })
}

export function attributeValueType(){
   return Joi.alternatives([Joi.string(),Joi.number(),Joi.date(),Joi.date(),Joi.string(),Joi.boolean(),attributeLocalizedEnumValueType(),attributePlainEnumValueType(),referenceType(),moneyType(),localizedStringType(),attributeType()])
}

export function categoryOrderHintsType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('/^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/'), Joi.string())
}

export function customTokenizerType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             inputs: Joi.array().items(Joi.string()).required(),
             type: Joi.string().required().only('custom')
          })
}

export function facetResultType(): Joi.AnySchema {
   return Joi.alternatives([filteredFacetResultType(), rangeFacetResultType(), termFacetResultType()])
}

export function facetResultRangeType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             count: Joi.number().required(),
             from: Joi.number().required(),
             max: Joi.number().required(),
             mean: Joi.number().required(),
             min: Joi.number().required(),
             to: Joi.number().required(),
             total: Joi.number().required(),
             fromStr: Joi.string().required(),
             toStr: Joi.string().required(),
             productCount: Joi.number().optional()
          })
}

export function facetResultTermType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             term: Joi.any().required(),
             count: Joi.number().required(),
             productCount: Joi.number().optional()
          })
}

export function facetResultsType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('/^[a-z].*$/'), Joi.any())
}

const facetTypesTypeValues = [

   'terms',
   'range',
   'filter'

]

export function facetTypesType(): Joi.AnySchema {
   return Joi.string().only(facetTypesTypeValues)
}

export function filteredFacetResultType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: facetTypesType().required().only('filter'),
             count: Joi.number().required(),
             productCount: Joi.number().optional()
          })
}

export function productType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             masterData: productCatalogDataType().required(),
             productType: productTypeReferenceType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             createdBy: createdByType().optional(),
             lastModifiedBy: lastModifiedByType().optional(),
             reviewRatingStatistics: reviewRatingStatisticsType().optional(),
             state: stateReferenceType().optional(),
             taxCategory: taxCategoryReferenceType().optional(),
             key: Joi.string().optional()
          })
}

export function productCatalogDataType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             current: productDataType().required(),
             staged: productDataType().required(),
             hasStagedChanges: Joi.boolean().required(),
             published: Joi.boolean().required()
          })
}

export function productDataType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             categories: Joi.array().items(categoryReferenceType()).required(),
             variants: Joi.array().items(productVariantType()).required(),
             name: localizedStringType().required(),
             slug: localizedStringType().required(),
             masterVariant: productVariantType().required(),
             searchKeywords: searchKeywordsType().required(),
             categoryOrderHints: categoryOrderHintsType().optional(),
             description: localizedStringType().optional(),
             metaDescription: localizedStringType().optional(),
             metaKeywords: localizedStringType().optional(),
             metaTitle: localizedStringType().optional()
          })
}

export function productDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             slug: localizedStringType().required(),
             productType: productTypeResourceIdentifierType().required(),
             categories: Joi.array().items(categoryResourceIdentifierType()).optional(),
             variants: Joi.array().items(productVariantDraftType()).optional(),
             categoryOrderHints: categoryOrderHintsType().optional(),
             description: localizedStringType().optional(),
             metaDescription: localizedStringType().optional(),
             metaKeywords: localizedStringType().optional(),
             metaTitle: localizedStringType().optional(),
             masterVariant: productVariantDraftType().optional(),
             searchKeywords: searchKeywordsType().optional(),
             state: stateResourceIdentifierType().optional(),
             taxCategory: taxCategoryResourceIdentifierType().optional(),
             publish: Joi.boolean().optional(),
             key: Joi.string().optional()
          })
}

export function productPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(productType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function productProjectionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             categories: Joi.array().items(categoryReferenceType()).required(),
             variants: Joi.array().items(productVariantType()).required(),
             name: localizedStringType().required(),
             slug: localizedStringType().required(),
             productType: productTypeReferenceType().required(),
             masterVariant: productVariantType().required(),
             createdAt: Joi.date().iso().required(),
             lastModifiedAt: Joi.date().iso().required(),
             version: Joi.number().required(),
             id: Joi.string().required(),
             categoryOrderHints: categoryOrderHintsType().optional(),
             description: localizedStringType().optional(),
             metaDescription: localizedStringType().optional(),
             metaKeywords: localizedStringType().optional(),
             metaTitle: localizedStringType().optional(),
             reviewRatingStatistics: reviewRatingStatisticsType().optional(),
             searchKeywords: searchKeywordsType().optional(),
             state: stateReferenceType().optional(),
             taxCategory: taxCategoryReferenceType().optional(),
             hasStagedChanges: Joi.boolean().optional(),
             published: Joi.boolean().optional(),
             key: Joi.string().optional()
          })
}

export function productProjectionPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(productProjectionType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function productProjectionPagedSearchResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(productProjectionType()).required(),
             facets: facetResultsType().required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}

export function productReferenceType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().required().only('product'),
             id: Joi.string().required(),
             obj: productType().optional()
          })
}

export function productResourceIdentifierType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             typeId: referenceTypeIdType().optional().only('product'),
             id: Joi.string().optional(),
             key: Joi.string().optional()
          })
}

export function productUpdateType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             actions: Joi.array().items(productUpdateActionType()).required(),
             version: Joi.number().required()
          })
}

export function productUpdateActionType(): Joi.AnySchema {
   return Joi.alternatives([productAddAssetActionType(), productAddExternalImageActionType(), productAddPriceActionType(), productAddToCategoryActionType(), productAddVariantActionType(), productChangeAssetNameActionType(), productChangeAssetOrderActionType(), productChangeMasterVariantActionType(), productChangeNameActionType(), productChangePriceActionType(), productChangeSlugActionType(), productLegacySetSkuActionType(), productMoveImageToPositionActionType(), productPublishActionType(), productRemoveAssetActionType(), productRemoveFromCategoryActionType(), productRemoveImageActionType(), productRemovePriceActionType(), productRemoveVariantActionType(), productRevertStagedChangesActionType(), productRevertStagedVariantChangesActionType(), productSetAssetCustomFieldActionType(), productSetAssetCustomTypeActionType(), productSetAssetDescriptionActionType(), productSetAssetKeyActionType(), productSetAssetSourcesActionType(), productSetAssetTagsActionType(), productSetAttributeActionType(), productSetAttributeInAllVariantsActionType(), productSetCategoryOrderHintActionType(), productSetDescriptionActionType(), productSetDiscountedPriceActionType(), productSetImageLabelActionType(), productSetKeyActionType(), productSetMetaDescriptionActionType(), productSetMetaKeywordsActionType(), productSetMetaTitleActionType(), productSetPricesActionType(), productSetProductPriceCustomFieldActionType(), productSetProductPriceCustomTypeActionType(), productSetProductVariantKeyActionType(), productSetSearchKeywordsActionType(), productSetSkuActionType(), productSetTaxCategoryActionType(), productTransitionStateActionType(), productUnpublishActionType()])
}

export function productVariantType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             id: Joi.number().required(),
             assets: Joi.array().items(assetType()).optional(),
             attributes: Joi.array().items(attributeType()).optional(),
             images: Joi.array().items(imageType()).optional(),
             prices: Joi.array().items(priceType()).optional(),
             price: priceType().optional(),
             availability: productVariantAvailabilityType().optional(),
             scopedPrice: scopedPriceType().optional(),
             isMatchingVariant: Joi.boolean().optional(),
             scopedPriceDiscounted: Joi.boolean().optional(),
             key: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productVariantAvailabilityType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             channels: productVariantChannelAvailabilityMapType().optional(),
             isOnStock: Joi.boolean().optional(),
             availableQuantity: Joi.number().optional(),
             restockableInDays: Joi.number().optional()
          })
}

export function productVariantChannelAvailabilityType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             isOnStock: Joi.boolean().optional(),
             availableQuantity: Joi.number().optional(),
             restockableInDays: Joi.number().optional()
          })
}

export function productVariantChannelAvailabilityMapType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('//'), productVariantChannelAvailabilityType())
}

export function productVariantDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             assets: Joi.array().items(assetDraftType()).optional(),
             attributes: Joi.array().items(attributeType()).optional(),
             images: Joi.array().items(imageType()).optional(),
             prices: Joi.array().items(priceDraftType()).optional(),
             key: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function rangeFacetResultType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             ranges: Joi.array().items(facetResultRangeType()).required(),
             type: facetTypesType().required().only('range')
          })
}

export function searchKeywordType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             text: Joi.string().required(),
             suggestTokenizer: Joi.any().optional()
          })
}

export function searchKeywordsType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('/^[a-z]{2}(-[A-Z]{2})?$/'), searchKeywordType())
}

export function suggestTokenizerType(): Joi.AnySchema {
   return Joi.alternatives([whitespaceTokenizerType(), customTokenizerType()])
}

export function suggestionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             text: Joi.string().required()
          })
}

export function suggestionResultType(): Joi.AnySchema {
   return Joi.object().pattern(new RegExp('/searchKeywords.[a-z]{2}(-[A-Z]{2})?/'), suggestionType())
}

export function termFacetResultType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             terms: Joi.array().items(facetResultTermType()).required(),
             type: facetTypesType().required().only('terms'),
             dataType: termFacetResultTypeType().required(),
             missing: Joi.number().required(),
             other: Joi.number().required(),
             total: Joi.number().required()
          })
}

const termFacetResultTypeTypeValues = [

   'text',
   'date',
   'time',
   'datetime',
   'boolean',
   'number'

]

export function termFacetResultTypeType(): Joi.AnySchema {
   return Joi.string().only(termFacetResultTypeTypeValues)
}

export function whitespaceTokenizerType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             type: Joi.string().required().only('whitespace')
          })
}

export function productAddAssetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             asset: assetDraftType().required(),
             action: Joi.string().required().only('addAsset'),
             staged: Joi.boolean().optional(),
             position: Joi.number().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productAddExternalImageActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             image: imageType().required(),
             action: Joi.string().required().only('addExternalImage'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productAddPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: priceDraftType().required(),
             action: Joi.string().required().only('addPrice'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productAddToCategoryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             category: categoryResourceIdentifierType().required(),
             action: Joi.string().required().only('addToCategory'),
             staged: Joi.boolean().optional(),
             orderHint: Joi.string().optional()
          })
}

export function productAddVariantActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('addVariant'),
             assets: Joi.array().items(assetType()).optional(),
             attributes: Joi.array().items(attributeType()).optional(),
             images: Joi.array().items(imageType()).optional(),
             prices: Joi.array().items(priceDraftType()).optional(),
             staged: Joi.boolean().optional(),
             key: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productChangeAssetNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeAssetName'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productChangeAssetOrderActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             assetOrder: Joi.array().items(Joi.string()).required(),
             action: Joi.string().required().only('changeAssetOrder'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productChangeMasterVariantActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('changeMasterVariant'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productChangeNameActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: localizedStringType().required(),
             action: Joi.string().required().only('changeName'),
             staged: Joi.boolean().optional()
          })
}

export function productChangePriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             price: priceDraftType().required(),
             action: Joi.string().required().only('changePrice'),
             priceId: Joi.string().required(),
             staged: Joi.boolean().optional()
          })
}

export function productChangeSlugActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             slug: localizedStringType().required(),
             action: Joi.string().required().only('changeSlug'),
             staged: Joi.boolean().optional()
          })
}

export function productLegacySetSkuActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variantId: Joi.number().required(),
             action: Joi.string().required().only('legacySetSku'),
             sku: Joi.string().optional()
          })
}

export function productMoveImageToPositionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             position: Joi.number().required(),
             action: Joi.string().required().only('moveImageToPosition'),
             imageUrl: Joi.string().required(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productPublishActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('publish'),
             scope: productPublishScopeType().optional()
          })
}

export function productRemoveAssetActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeAsset'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productRemoveFromCategoryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             category: categoryResourceIdentifierType().required(),
             action: Joi.string().required().only('removeFromCategory'),
             staged: Joi.boolean().optional()
          })
}

export function productRemoveImageActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeImage'),
             imageUrl: Joi.string().required(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productRemovePriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removePrice'),
             priceId: Joi.string().required(),
             staged: Joi.boolean().optional()
          })
}

export function productRemoveVariantActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('removeVariant'),
             staged: Joi.boolean().optional(),
             id: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productRevertStagedChangesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('revertStagedChanges')
          })
}

export function productRevertStagedVariantChangesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variantId: Joi.number().required(),
             action: Joi.string().required().only('revertStagedVariantChanges')
          })
}

export function productSetAssetCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetCustomField'),
             name: Joi.string().required(),
             value: Joi.any().optional(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAssetCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetCustomType'),
             type: typeResourceIdentifierType().optional(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             fields: Joi.any().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAssetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetDescription'),
             description: localizedStringType().optional(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAssetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetKey'),
             assetId: Joi.string().required(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAssetSourcesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             sources: Joi.array().items(assetSourceType()).required(),
             action: Joi.string().required().only('setAssetSources'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAssetTagsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAssetTags'),
             tags: Joi.array().items(Joi.string()).optional(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             assetId: Joi.string().optional(),
             assetKey: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAttributeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAttribute'),
             name: Joi.string().required(),
             value: Joi.any().optional(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetAttributeInAllVariantsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setAttributeInAllVariants'),
             name: Joi.string().required(),
             value: Joi.any().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetCategoryOrderHintActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setCategoryOrderHint'),
             categoryId: Joi.string().required(),
             staged: Joi.boolean().optional(),
             orderHint: Joi.string().optional()
          })
}

export function productSetDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDescription'),
             description: localizedStringType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetDiscountedPriceActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setDiscountedPrice'),
             priceId: Joi.string().required(),
             discounted: discountedPriceType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetImageLabelActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setImageLabel'),
             imageUrl: Joi.string().required(),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             label: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setKey'),
             key: Joi.string().optional()
          })
}

export function productSetMetaDescriptionActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaDescription'),
             metaDescription: localizedStringType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetMetaKeywordsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaKeywords'),
             metaKeywords: localizedStringType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetMetaTitleActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setMetaTitle'),
             metaTitle: localizedStringType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetPricesActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             prices: Joi.array().items(priceDraftType()).required(),
             action: Joi.string().required().only('setPrices'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetProductPriceCustomFieldActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setProductPriceCustomField'),
             name: Joi.string().required(),
             priceId: Joi.string().required(),
             value: Joi.any().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetProductPriceCustomTypeActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setProductPriceCustomType'),
             priceId: Joi.string().required(),
             fields: fieldContainerType().optional(),
             type: typeResourceIdentifierType().optional(),
             staged: Joi.boolean().optional()
          })
}

export function productSetProductVariantKeyActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setProductVariantKey'),
             staged: Joi.boolean().optional(),
             variantId: Joi.number().optional(),
             key: Joi.string().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetSearchKeywordsActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             searchKeywords: searchKeywordsType().required(),
             action: Joi.string().required().only('setSearchKeywords'),
             staged: Joi.boolean().optional()
          })
}

export function productSetSkuActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             variantId: Joi.number().required(),
             action: Joi.string().required().only('setSku'),
             staged: Joi.boolean().optional(),
             sku: Joi.string().optional()
          })
}

export function productSetTaxCategoryActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('setTaxCategory'),
             taxCategory: taxCategoryResourceIdentifierType().optional()
          })
}

export function productTransitionStateActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('transitionState'),
             state: stateResourceIdentifierType().optional(),
             force: Joi.boolean().optional()
          })
}

export function productUnpublishActionType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             action: Joi.string().required().only('unpublish')
          })
}