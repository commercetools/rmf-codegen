"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const ProductType_types_1 = require("./ProductType-types");
const ProductType_types_2 = require("./ProductType-types");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const Common_types_4 = require("./Common-types");
const Common_types_5 = require("./Common-types");
const State_types_1 = require("./State-types");
const Review_types_1 = require("./Review-types");
const ProductType_types_3 = require("./ProductType-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const Category_types_1 = require("./Category-types");
const TaxCategory_types_2 = require("./TaxCategory-types");
const State_types_2 = require("./State-types");
const Category_types_2 = require("./Category-types");
const ProductType_types_4 = require("./ProductType-types");
const Common_types_6 = require("./Common-types");
const Common_types_7 = require("./Common-types");
const Common_types_8 = require("./Common-types");
const Common_types_9 = require("./Common-types");
const Common_types_10 = require("./Common-types");
const Common_types_11 = require("./Common-types");
const Common_types_12 = require("./Common-types");
const Cart_types_1 = require("./Cart-types");
const Type_types_1 = require("./Type-types");
const Common_types_13 = require("./Common-types");
const Common_types_14 = require("./Common-types");
const Type_types_2 = require("./Type-types");
function attributeType() {
    return Joi.object().unknown().keys({
        value: Joi.any().required(),
        name: Joi.string().required()
    });
}
exports.attributeType = attributeType;
function attributeValueType() {
    return Joi.alternatives([Joi.string(), Joi.number(), Joi.date(), Joi.date(), Joi.string(), Joi.boolean(), ProductType_types_1.attributeLocalizedEnumValueType(), ProductType_types_2.attributePlainEnumValueType(), Common_types_1.referenceType(), Common_types_2.moneyType(), Common_types_3.localizedStringType(), attributeType()]);
}
exports.attributeValueType = attributeValueType;
function categoryOrderHintsType() {
    return Joi.object().pattern(new RegExp('/^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/'), Joi.string());
}
exports.categoryOrderHintsType = categoryOrderHintsType;
function customTokenizerType() {
    return Joi.object().unknown().keys({
        inputs: Joi.array().items(Joi.string()).required(),
        type: Joi.string().required().only('custom')
    });
}
exports.customTokenizerType = customTokenizerType;
function facetResultType() {
    return Joi.alternatives([filteredFacetResultType(), rangeFacetResultType(), termFacetResultType()]);
}
exports.facetResultType = facetResultType;
function facetResultRangeType() {
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
    });
}
exports.facetResultRangeType = facetResultRangeType;
function facetResultTermType() {
    return Joi.object().unknown().keys({
        term: Joi.any().required(),
        count: Joi.number().required(),
        productCount: Joi.number().optional()
    });
}
exports.facetResultTermType = facetResultTermType;
function facetResultsType() {
    return Joi.object().pattern(new RegExp('/^[a-z].*$/'), Joi.any());
}
exports.facetResultsType = facetResultsType;
const facetTypesTypeValues = [
    'terms',
    'range',
    'filter'
];
function facetTypesType() {
    return Joi.string().only(facetTypesTypeValues);
}
exports.facetTypesType = facetTypesType;
function filteredFacetResultType() {
    return Joi.object().unknown().keys({
        type: facetTypesType().required().only('filter'),
        count: Joi.number().required(),
        productCount: Joi.number().optional()
    });
}
exports.filteredFacetResultType = filteredFacetResultType;
function productType() {
    return Joi.object().unknown().keys({
        masterData: productCatalogDataType().required(),
        productType: ProductType_types_3.productTypeReferenceType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        createdBy: Common_types_4.createdByType().optional(),
        lastModifiedBy: Common_types_5.lastModifiedByType().optional(),
        reviewRatingStatistics: Review_types_1.reviewRatingStatisticsType().optional(),
        state: State_types_1.stateReferenceType().optional(),
        taxCategory: TaxCategory_types_1.taxCategoryReferenceType().optional(),
        key: Joi.string().optional()
    });
}
exports.productType = productType;
function productCatalogDataType() {
    return Joi.object().unknown().keys({
        current: productDataType().required(),
        staged: productDataType().required(),
        hasStagedChanges: Joi.boolean().required(),
        published: Joi.boolean().required()
    });
}
exports.productCatalogDataType = productCatalogDataType;
function productDataType() {
    return Joi.object().unknown().keys({
        categories: Joi.array().items(Category_types_1.categoryReferenceType()).required(),
        variants: Joi.array().items(productVariantType()).required(),
        name: Common_types_3.localizedStringType().required(),
        slug: Common_types_3.localizedStringType().required(),
        masterVariant: productVariantType().required(),
        searchKeywords: searchKeywordsType().required(),
        categoryOrderHints: categoryOrderHintsType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        metaDescription: Common_types_3.localizedStringType().optional(),
        metaKeywords: Common_types_3.localizedStringType().optional(),
        metaTitle: Common_types_3.localizedStringType().optional()
    });
}
exports.productDataType = productDataType;
function productDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        slug: Common_types_3.localizedStringType().required(),
        productType: ProductType_types_4.productTypeResourceIdentifierType().required(),
        categories: Joi.array().items(Category_types_2.categoryResourceIdentifierType()).optional(),
        variants: Joi.array().items(productVariantDraftType()).optional(),
        categoryOrderHints: categoryOrderHintsType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        metaDescription: Common_types_3.localizedStringType().optional(),
        metaKeywords: Common_types_3.localizedStringType().optional(),
        metaTitle: Common_types_3.localizedStringType().optional(),
        masterVariant: productVariantDraftType().optional(),
        searchKeywords: searchKeywordsType().optional(),
        state: State_types_2.stateResourceIdentifierType().optional(),
        taxCategory: TaxCategory_types_2.taxCategoryResourceIdentifierType().optional(),
        publish: Joi.boolean().optional(),
        key: Joi.string().optional()
    });
}
exports.productDraftType = productDraftType;
function productPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(productType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.productPagedQueryResponseType = productPagedQueryResponseType;
function productProjectionType() {
    return Joi.object().unknown().keys({
        categories: Joi.array().items(Category_types_1.categoryReferenceType()).required(),
        variants: Joi.array().items(productVariantType()).required(),
        name: Common_types_3.localizedStringType().required(),
        slug: Common_types_3.localizedStringType().required(),
        productType: ProductType_types_3.productTypeReferenceType().required(),
        masterVariant: productVariantType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        categoryOrderHints: categoryOrderHintsType().optional(),
        description: Common_types_3.localizedStringType().optional(),
        metaDescription: Common_types_3.localizedStringType().optional(),
        metaKeywords: Common_types_3.localizedStringType().optional(),
        metaTitle: Common_types_3.localizedStringType().optional(),
        reviewRatingStatistics: Review_types_1.reviewRatingStatisticsType().optional(),
        searchKeywords: searchKeywordsType().optional(),
        state: State_types_1.stateReferenceType().optional(),
        taxCategory: TaxCategory_types_1.taxCategoryReferenceType().optional(),
        hasStagedChanges: Joi.boolean().optional(),
        published: Joi.boolean().optional(),
        key: Joi.string().optional()
    });
}
exports.productProjectionType = productProjectionType;
function productProjectionPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(productProjectionType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.productProjectionPagedQueryResponseType = productProjectionPagedQueryResponseType;
function productProjectionPagedSearchResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(productProjectionType()).required(),
        facets: facetResultsType().required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.productProjectionPagedSearchResponseType = productProjectionPagedSearchResponseType;
function productReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().required().only('product'),
        id: Joi.string().required(),
        obj: productType().optional()
    });
}
exports.productReferenceType = productReferenceType;
function productResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_6.referenceTypeIdType().optional().only('product'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.productResourceIdentifierType = productResourceIdentifierType;
function productUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(productUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.productUpdateType = productUpdateType;
function productUpdateActionType() {
    return Joi.alternatives([productAddAssetActionType(), productAddExternalImageActionType(), productAddPriceActionType(), productAddToCategoryActionType(), productAddVariantActionType(), productChangeAssetNameActionType(), productChangeAssetOrderActionType(), productChangeMasterVariantActionType(), productChangeNameActionType(), productChangePriceActionType(), productChangeSlugActionType(), productLegacySetSkuActionType(), productMoveImageToPositionActionType(), productPublishActionType(), productRemoveAssetActionType(), productRemoveFromCategoryActionType(), productRemoveImageActionType(), productRemovePriceActionType(), productRemoveVariantActionType(), productRevertStagedChangesActionType(), productRevertStagedVariantChangesActionType(), productSetAssetCustomFieldActionType(), productSetAssetCustomTypeActionType(), productSetAssetDescriptionActionType(), productSetAssetKeyActionType(), productSetAssetSourcesActionType(), productSetAssetTagsActionType(), productSetAttributeActionType(), productSetAttributeInAllVariantsActionType(), productSetCategoryOrderHintActionType(), productSetDescriptionActionType(), productSetDiscountedPriceActionType(), productSetImageLabelActionType(), productSetKeyActionType(), productSetMetaDescriptionActionType(), productSetMetaKeywordsActionType(), productSetMetaTitleActionType(), productSetPricesActionType(), productSetProductPriceCustomFieldActionType(), productSetProductPriceCustomTypeActionType(), productSetProductVariantKeyActionType(), productSetSearchKeywordsActionType(), productSetSkuActionType(), productSetTaxCategoryActionType(), productTransitionStateActionType(), productUnpublishActionType()]);
}
exports.productUpdateActionType = productUpdateActionType;
function productVariantType() {
    return Joi.object().unknown().keys({
        id: Joi.number().required(),
        assets: Joi.array().items(Common_types_9.assetType()).optional(),
        attributes: Joi.array().items(attributeType()).optional(),
        images: Joi.array().items(Common_types_8.imageType()).optional(),
        prices: Joi.array().items(Common_types_10.priceType()).optional(),
        price: Common_types_10.priceType().optional(),
        availability: productVariantAvailabilityType().optional(),
        scopedPrice: Common_types_7.scopedPriceType().optional(),
        isMatchingVariant: Joi.boolean().optional(),
        scopedPriceDiscounted: Joi.boolean().optional(),
        key: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productVariantType = productVariantType;
function productVariantAvailabilityType() {
    return Joi.object().unknown().keys({
        channels: productVariantChannelAvailabilityMapType().optional(),
        isOnStock: Joi.boolean().optional(),
        availableQuantity: Joi.number().optional(),
        restockableInDays: Joi.number().optional()
    });
}
exports.productVariantAvailabilityType = productVariantAvailabilityType;
function productVariantChannelAvailabilityType() {
    return Joi.object().unknown().keys({
        isOnStock: Joi.boolean().optional(),
        availableQuantity: Joi.number().optional(),
        restockableInDays: Joi.number().optional()
    });
}
exports.productVariantChannelAvailabilityType = productVariantChannelAvailabilityType;
function productVariantChannelAvailabilityMapType() {
    return Joi.object().pattern(new RegExp('//'), productVariantChannelAvailabilityType());
}
exports.productVariantChannelAvailabilityMapType = productVariantChannelAvailabilityMapType;
function productVariantDraftType() {
    return Joi.object().unknown().keys({
        assets: Joi.array().items(Common_types_11.assetDraftType()).optional(),
        attributes: Joi.array().items(attributeType()).optional(),
        images: Joi.array().items(Common_types_8.imageType()).optional(),
        prices: Joi.array().items(Common_types_12.priceDraftType()).optional(),
        key: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productVariantDraftType = productVariantDraftType;
function rangeFacetResultType() {
    return Joi.object().unknown().keys({
        ranges: Joi.array().items(facetResultRangeType()).required(),
        type: facetTypesType().required().only('range')
    });
}
exports.rangeFacetResultType = rangeFacetResultType;
function searchKeywordType() {
    return Joi.object().unknown().keys({
        text: Joi.string().required(),
        suggestTokenizer: Joi.any().optional()
    });
}
exports.searchKeywordType = searchKeywordType;
function searchKeywordsType() {
    return Joi.object().pattern(new RegExp('/^[a-z]{2}(-[A-Z]{2})?$/'), searchKeywordType());
}
exports.searchKeywordsType = searchKeywordsType;
function suggestTokenizerType() {
    return Joi.alternatives([whitespaceTokenizerType(), customTokenizerType()]);
}
exports.suggestTokenizerType = suggestTokenizerType;
function suggestionType() {
    return Joi.object().unknown().keys({
        text: Joi.string().required()
    });
}
exports.suggestionType = suggestionType;
function suggestionResultType() {
    return Joi.object().pattern(new RegExp('/searchKeywords.[a-z]{2}(-[A-Z]{2})?/'), suggestionType());
}
exports.suggestionResultType = suggestionResultType;
function termFacetResultType() {
    return Joi.object().unknown().keys({
        terms: Joi.array().items(facetResultTermType()).required(),
        type: facetTypesType().required().only('terms'),
        dataType: termFacetResultTypeType().required(),
        missing: Joi.number().required(),
        other: Joi.number().required(),
        total: Joi.number().required()
    });
}
exports.termFacetResultType = termFacetResultType;
const termFacetResultTypeTypeValues = [
    'text',
    'date',
    'time',
    'datetime',
    'boolean',
    'number'
];
function termFacetResultTypeType() {
    return Joi.string().only(termFacetResultTypeTypeValues);
}
exports.termFacetResultTypeType = termFacetResultTypeType;
function whitespaceTokenizerType() {
    return Joi.object().unknown().keys({
        type: Joi.string().required().only('whitespace')
    });
}
exports.whitespaceTokenizerType = whitespaceTokenizerType;
function productAddAssetActionType() {
    return Joi.object().unknown().keys({
        asset: Common_types_11.assetDraftType().required(),
        action: Joi.string().required().only('addAsset'),
        staged: Joi.boolean().optional(),
        position: Joi.number().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productAddAssetActionType = productAddAssetActionType;
function productAddExternalImageActionType() {
    return Joi.object().unknown().keys({
        image: Common_types_8.imageType().required(),
        action: Joi.string().required().only('addExternalImage'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productAddExternalImageActionType = productAddExternalImageActionType;
function productAddPriceActionType() {
    return Joi.object().unknown().keys({
        price: Common_types_12.priceDraftType().required(),
        action: Joi.string().required().only('addPrice'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productAddPriceActionType = productAddPriceActionType;
function productAddToCategoryActionType() {
    return Joi.object().unknown().keys({
        category: Category_types_2.categoryResourceIdentifierType().required(),
        action: Joi.string().required().only('addToCategory'),
        staged: Joi.boolean().optional(),
        orderHint: Joi.string().optional()
    });
}
exports.productAddToCategoryActionType = productAddToCategoryActionType;
function productAddVariantActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addVariant'),
        assets: Joi.array().items(Common_types_9.assetType()).optional(),
        attributes: Joi.array().items(attributeType()).optional(),
        images: Joi.array().items(Common_types_8.imageType()).optional(),
        prices: Joi.array().items(Common_types_12.priceDraftType()).optional(),
        staged: Joi.boolean().optional(),
        key: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productAddVariantActionType = productAddVariantActionType;
function productChangeAssetNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('changeAssetName'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productChangeAssetNameActionType = productChangeAssetNameActionType;
function productChangeAssetOrderActionType() {
    return Joi.object().unknown().keys({
        assetOrder: Joi.array().items(Joi.string()).required(),
        action: Joi.string().required().only('changeAssetOrder'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productChangeAssetOrderActionType = productChangeAssetOrderActionType;
function productChangeMasterVariantActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('changeMasterVariant'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productChangeMasterVariantActionType = productChangeMasterVariantActionType;
function productChangeNameActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('changeName'),
        staged: Joi.boolean().optional()
    });
}
exports.productChangeNameActionType = productChangeNameActionType;
function productChangePriceActionType() {
    return Joi.object().unknown().keys({
        price: Common_types_12.priceDraftType().required(),
        action: Joi.string().required().only('changePrice'),
        priceId: Joi.string().required(),
        staged: Joi.boolean().optional()
    });
}
exports.productChangePriceActionType = productChangePriceActionType;
function productChangeSlugActionType() {
    return Joi.object().unknown().keys({
        slug: Common_types_3.localizedStringType().required(),
        action: Joi.string().required().only('changeSlug'),
        staged: Joi.boolean().optional()
    });
}
exports.productChangeSlugActionType = productChangeSlugActionType;
function productLegacySetSkuActionType() {
    return Joi.object().unknown().keys({
        variantId: Joi.number().required(),
        action: Joi.string().required().only('legacySetSku'),
        sku: Joi.string().optional()
    });
}
exports.productLegacySetSkuActionType = productLegacySetSkuActionType;
function productMoveImageToPositionActionType() {
    return Joi.object().unknown().keys({
        position: Joi.number().required(),
        action: Joi.string().required().only('moveImageToPosition'),
        imageUrl: Joi.string().required(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productMoveImageToPositionActionType = productMoveImageToPositionActionType;
function productPublishActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('publish'),
        scope: Cart_types_1.productPublishScopeType().optional()
    });
}
exports.productPublishActionType = productPublishActionType;
function productRemoveAssetActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeAsset'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productRemoveAssetActionType = productRemoveAssetActionType;
function productRemoveFromCategoryActionType() {
    return Joi.object().unknown().keys({
        category: Category_types_2.categoryResourceIdentifierType().required(),
        action: Joi.string().required().only('removeFromCategory'),
        staged: Joi.boolean().optional()
    });
}
exports.productRemoveFromCategoryActionType = productRemoveFromCategoryActionType;
function productRemoveImageActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeImage'),
        imageUrl: Joi.string().required(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productRemoveImageActionType = productRemoveImageActionType;
function productRemovePriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removePrice'),
        priceId: Joi.string().required(),
        staged: Joi.boolean().optional()
    });
}
exports.productRemovePriceActionType = productRemovePriceActionType;
function productRemoveVariantActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeVariant'),
        staged: Joi.boolean().optional(),
        id: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productRemoveVariantActionType = productRemoveVariantActionType;
function productRevertStagedChangesActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('revertStagedChanges')
    });
}
exports.productRevertStagedChangesActionType = productRevertStagedChangesActionType;
function productRevertStagedVariantChangesActionType() {
    return Joi.object().unknown().keys({
        variantId: Joi.number().required(),
        action: Joi.string().required().only('revertStagedVariantChanges')
    });
}
exports.productRevertStagedVariantChangesActionType = productRevertStagedVariantChangesActionType;
function productSetAssetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetCustomFieldActionType = productSetAssetCustomFieldActionType;
function productSetAssetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetCustomType'),
        type: Type_types_1.typeResourceIdentifierType().optional(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        fields: Joi.any().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetCustomTypeActionType = productSetAssetCustomTypeActionType;
function productSetAssetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetDescription'),
        description: Common_types_3.localizedStringType().optional(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetDescriptionActionType = productSetAssetDescriptionActionType;
function productSetAssetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetKey'),
        assetId: Joi.string().required(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetKeyActionType = productSetAssetKeyActionType;
function productSetAssetSourcesActionType() {
    return Joi.object().unknown().keys({
        sources: Joi.array().items(Common_types_13.assetSourceType()).required(),
        action: Joi.string().required().only('setAssetSources'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetSourcesActionType = productSetAssetSourcesActionType;
function productSetAssetTagsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAssetTags'),
        tags: Joi.array().items(Joi.string()).optional(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        assetId: Joi.string().optional(),
        assetKey: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAssetTagsActionType = productSetAssetTagsActionType;
function productSetAttributeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAttribute'),
        name: Joi.string().required(),
        value: Joi.any().optional(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetAttributeActionType = productSetAttributeActionType;
function productSetAttributeInAllVariantsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAttributeInAllVariants'),
        name: Joi.string().required(),
        value: Joi.any().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetAttributeInAllVariantsActionType = productSetAttributeInAllVariantsActionType;
function productSetCategoryOrderHintActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCategoryOrderHint'),
        categoryId: Joi.string().required(),
        staged: Joi.boolean().optional(),
        orderHint: Joi.string().optional()
    });
}
exports.productSetCategoryOrderHintActionType = productSetCategoryOrderHintActionType;
function productSetDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDescription'),
        description: Common_types_3.localizedStringType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetDescriptionActionType = productSetDescriptionActionType;
function productSetDiscountedPriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDiscountedPrice'),
        priceId: Joi.string().required(),
        discounted: Common_types_14.discountedPriceType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetDiscountedPriceActionType = productSetDiscountedPriceActionType;
function productSetImageLabelActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setImageLabel'),
        imageUrl: Joi.string().required(),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        label: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetImageLabelActionType = productSetImageLabelActionType;
function productSetKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setKey'),
        key: Joi.string().optional()
    });
}
exports.productSetKeyActionType = productSetKeyActionType;
function productSetMetaDescriptionActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaDescription'),
        metaDescription: Common_types_3.localizedStringType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetMetaDescriptionActionType = productSetMetaDescriptionActionType;
function productSetMetaKeywordsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaKeywords'),
        metaKeywords: Common_types_3.localizedStringType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetMetaKeywordsActionType = productSetMetaKeywordsActionType;
function productSetMetaTitleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setMetaTitle'),
        metaTitle: Common_types_3.localizedStringType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetMetaTitleActionType = productSetMetaTitleActionType;
function productSetPricesActionType() {
    return Joi.object().unknown().keys({
        prices: Joi.array().items(Common_types_12.priceDraftType()).required(),
        action: Joi.string().required().only('setPrices'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetPricesActionType = productSetPricesActionType;
function productSetProductPriceCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setProductPriceCustomField'),
        name: Joi.string().required(),
        priceId: Joi.string().required(),
        value: Joi.any().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetProductPriceCustomFieldActionType = productSetProductPriceCustomFieldActionType;
function productSetProductPriceCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setProductPriceCustomType'),
        priceId: Joi.string().required(),
        fields: Type_types_2.fieldContainerType().optional(),
        type: Type_types_1.typeResourceIdentifierType().optional(),
        staged: Joi.boolean().optional()
    });
}
exports.productSetProductPriceCustomTypeActionType = productSetProductPriceCustomTypeActionType;
function productSetProductVariantKeyActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setProductVariantKey'),
        staged: Joi.boolean().optional(),
        variantId: Joi.number().optional(),
        key: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetProductVariantKeyActionType = productSetProductVariantKeyActionType;
function productSetSearchKeywordsActionType() {
    return Joi.object().unknown().keys({
        searchKeywords: searchKeywordsType().required(),
        action: Joi.string().required().only('setSearchKeywords'),
        staged: Joi.boolean().optional()
    });
}
exports.productSetSearchKeywordsActionType = productSetSearchKeywordsActionType;
function productSetSkuActionType() {
    return Joi.object().unknown().keys({
        variantId: Joi.number().required(),
        action: Joi.string().required().only('setSku'),
        staged: Joi.boolean().optional(),
        sku: Joi.string().optional()
    });
}
exports.productSetSkuActionType = productSetSkuActionType;
function productSetTaxCategoryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setTaxCategory'),
        taxCategory: TaxCategory_types_2.taxCategoryResourceIdentifierType().optional()
    });
}
exports.productSetTaxCategoryActionType = productSetTaxCategoryActionType;
function productTransitionStateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('transitionState'),
        state: State_types_2.stateResourceIdentifierType().optional(),
        force: Joi.boolean().optional()
    });
}
exports.productTransitionStateActionType = productTransitionStateActionType;
function productUnpublishActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('unpublish')
    });
}
exports.productUnpublishActionType = productUnpublishActionType;
