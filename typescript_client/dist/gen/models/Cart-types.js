"use strict";
/* tslint:disable */
//Generated file, please do not change
Object.defineProperty(exports, "__esModule", { value: true });
const Joi = require("joi");
const Common_types_1 = require("./Common-types");
const Common_types_2 = require("./Common-types");
const Common_types_3 = require("./Common-types");
const CartDiscount_types_1 = require("./CartDiscount-types");
const Common_types_4 = require("./Common-types");
const CustomerGroup_types_1 = require("./CustomerGroup-types");
const Type_types_1 = require("./Type-types");
const Store_types_1 = require("./Store-types");
const Order_types_1 = require("./Order-types");
const CustomerGroup_types_2 = require("./CustomerGroup-types");
const Type_types_2 = require("./Type-types");
const ShippingMethod_types_1 = require("./ShippingMethod-types");
const Store_types_2 = require("./Store-types");
const Common_types_5 = require("./Common-types");
const Common_types_6 = require("./Common-types");
const TaxCategory_types_1 = require("./TaxCategory-types");
const Order_types_2 = require("./Order-types");
const TaxCategory_types_2 = require("./TaxCategory-types");
const Common_types_7 = require("./Common-types");
const TaxCategory_types_3 = require("./TaxCategory-types");
const DiscountCode_types_1 = require("./DiscountCode-types");
const TaxCategory_types_4 = require("./TaxCategory-types");
const Common_types_8 = require("./Common-types");
const Product_types_1 = require("./Product-types");
const Channel_types_1 = require("./Channel-types");
const ProductType_types_1 = require("./ProductType-types");
const Channel_types_2 = require("./Channel-types");
const ShippingMethod_types_2 = require("./ShippingMethod-types");
const ShippingMethod_types_3 = require("./ShippingMethod-types");
const Order_types_3 = require("./Order-types");
const Payment_types_1 = require("./Payment-types");
const ShoppingList_types_1 = require("./ShoppingList-types");
const Type_types_3 = require("./Type-types");
const Type_types_4 = require("./Type-types");
const ShippingMethod_types_4 = require("./ShippingMethod-types");
function cartType() {
    return Joi.object().unknown().keys({
        customLineItems: Joi.array().items(customLineItemType()).required(),
        lineItems: Joi.array().items(lineItemType()).required(),
        refusedGifts: Joi.array().items(CartDiscount_types_1.cartDiscountReferenceType()).required(),
        origin: cartOriginType().required(),
        cartState: cartStateType().required(),
        taxRoundingMode: roundingModeType().required(),
        taxCalculationMode: taxCalculationModeType().required(),
        taxMode: taxModeType().required(),
        totalPrice: Common_types_3.typedMoneyType().required(),
        createdAt: Joi.date().iso().required(),
        lastModifiedAt: Joi.date().iso().required(),
        version: Joi.number().required(),
        id: Joi.string().required(),
        discountCodes: Joi.array().items(discountCodeInfoType()).optional(),
        itemShippingAddresses: Joi.array().items(Common_types_4.addressType()).optional(),
        billingAddress: Common_types_4.addressType().optional(),
        shippingAddress: Common_types_4.addressType().optional(),
        country: Joi.string().optional(),
        createdBy: Common_types_1.createdByType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        customerGroup: CustomerGroup_types_1.customerGroupReferenceType().optional(),
        inventoryMode: inventoryModeType().optional(),
        lastModifiedBy: Common_types_2.lastModifiedByType().optional(),
        paymentInfo: Order_types_1.paymentInfoType().optional(),
        shippingInfo: shippingInfoType().optional(),
        shippingRateInput: shippingRateInputType().optional(),
        store: Store_types_1.storeKeyReferenceType().optional(),
        taxedPrice: taxedPriceType().optional(),
        deleteDaysAfterLastModification: Joi.number().optional(),
        anonymousId: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        customerId: Joi.string().optional(),
        locale: Joi.string().optional()
    });
}
exports.cartType = cartType;
function cartDraftType() {
    return Joi.object().unknown().keys({
        currency: Joi.string().required(),
        customLineItems: Joi.array().items(customLineItemDraftType()).optional(),
        itemShippingAddresses: Joi.array().items(Common_types_4.addressType()).optional(),
        lineItems: Joi.array().items(lineItemDraftType()).optional(),
        billingAddress: Common_types_4.addressType().optional(),
        shippingAddress: Common_types_4.addressType().optional(),
        origin: cartOriginType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional(),
        externalTaxRateForShippingMethod: externalTaxRateDraftType().optional(),
        inventoryMode: inventoryModeType().optional(),
        taxRoundingMode: roundingModeType().optional(),
        shippingMethod: ShippingMethod_types_1.shippingMethodResourceIdentifierType().optional(),
        shippingRateInput: shippingRateInputDraftType().optional(),
        store: Store_types_2.storeResourceIdentifierType().optional(),
        taxCalculationMode: taxCalculationModeType().optional(),
        taxMode: taxModeType().optional(),
        deleteDaysAfterLastModification: Joi.number().optional(),
        anonymousId: Joi.string().optional(),
        country: Joi.string().optional(),
        customerEmail: Joi.string().optional(),
        customerId: Joi.string().optional(),
        locale: Joi.string().optional()
    });
}
exports.cartDraftType = cartDraftType;
const cartOriginTypeValues = [
    'Customer',
    'Merchant'
];
function cartOriginType() {
    return Joi.string().only(cartOriginTypeValues);
}
exports.cartOriginType = cartOriginType;
function cartPagedQueryResponseType() {
    return Joi.object().unknown().keys({
        results: Joi.array().items(cartType()).required(),
        count: Joi.number().required(),
        offset: Joi.number().required(),
        total: Joi.number().optional()
    });
}
exports.cartPagedQueryResponseType = cartPagedQueryResponseType;
function cartReferenceType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().required().only('cart'),
        id: Joi.string().required(),
        obj: cartType().optional()
    });
}
exports.cartReferenceType = cartReferenceType;
function cartResourceIdentifierType() {
    return Joi.object().unknown().keys({
        typeId: Common_types_5.referenceTypeIdType().optional().only('cart'),
        id: Joi.string().optional(),
        key: Joi.string().optional()
    });
}
exports.cartResourceIdentifierType = cartResourceIdentifierType;
const cartStateTypeValues = [
    'Active',
    'Merged',
    'Ordered'
];
function cartStateType() {
    return Joi.string().only(cartStateTypeValues);
}
exports.cartStateType = cartStateType;
function cartUpdateType() {
    return Joi.object().unknown().keys({
        actions: Joi.array().items(cartUpdateActionType()).required(),
        version: Joi.number().required()
    });
}
exports.cartUpdateType = cartUpdateType;
function cartUpdateActionType() {
    return Joi.alternatives([cartAddCustomLineItemActionType(), cartAddDiscountCodeActionType(), cartAddItemShippingAddressActionType(), cartAddLineItemActionType(), cartAddPaymentActionType(), cartAddShoppingListActionType(), cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType(), cartApplyDeltaToLineItemShippingDetailsTargetsActionType(), cartChangeCustomLineItemMoneyActionType(), cartChangeCustomLineItemQuantityActionType(), cartChangeLineItemQuantityActionType(), cartChangeTaxCalculationModeActionType(), cartChangeTaxModeActionType(), cartChangeTaxRoundingModeActionType(), cartRecalculateActionType(), cartRemoveCustomLineItemActionType(), cartRemoveDiscountCodeActionType(), cartRemoveItemShippingAddressActionType(), cartRemoveLineItemActionType(), cartRemovePaymentActionType(), cartSetAnonymousIdActionType(), cartSetBillingAddressActionType(), cartSetCartTotalTaxActionType(), cartSetCountryActionType(), cartSetCustomFieldActionType(), cartSetCustomLineItemCustomFieldActionType(), cartSetCustomLineItemCustomTypeActionType(), cartSetCustomLineItemShippingDetailsActionType(), cartSetCustomLineItemTaxAmountActionType(), cartSetCustomLineItemTaxRateActionType(), cartSetCustomShippingMethodActionType(), cartSetCustomTypeActionType(), cartSetCustomerEmailActionType(), cartSetCustomerGroupActionType(), cartSetCustomerIdActionType(), cartSetDeleteDaysAfterLastModificationActionType(), cartSetLineItemCustomFieldActionType(), cartSetLineItemCustomTypeActionType(), cartSetLineItemPriceActionType(), cartSetLineItemShippingDetailsActionType(), cartSetLineItemTaxAmountActionType(), cartSetLineItemTaxRateActionType(), cartSetLineItemTotalPriceActionType(), cartSetLocaleActionType(), cartSetShippingAddressActionType(), cartSetShippingMethodActionType(), cartSetShippingMethodTaxAmountActionType(), cartSetShippingMethodTaxRateActionType(), cartSetShippingRateInputActionType(), cartUpdateItemShippingAddressActionType()]);
}
exports.cartUpdateActionType = cartUpdateActionType;
function classificationShippingRateInputType() {
    return Joi.object().unknown().keys({
        label: Common_types_6.localizedStringType().required(),
        key: Joi.string().required(),
        type: Joi.string().required().only('Classification')
    });
}
exports.classificationShippingRateInputType = classificationShippingRateInputType;
function classificationShippingRateInputDraftType() {
    return Joi.object().unknown().keys({
        key: Joi.string().required(),
        type: Joi.string().required().only('Classification')
    });
}
exports.classificationShippingRateInputDraftType = classificationShippingRateInputDraftType;
function customLineItemType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
        state: Joi.array().items(Order_types_2.itemStateType()).required(),
        name: Common_types_6.localizedStringType().required(),
        money: Common_types_3.typedMoneyType().required(),
        totalPrice: Common_types_3.typedMoneyType().required(),
        quantity: Joi.number().required(),
        id: Joi.string().required(),
        slug: Joi.string().required(),
        custom: Type_types_1.customFieldsType().optional(),
        shippingDetails: itemShippingDetailsType().optional(),
        taxCategory: TaxCategory_types_2.taxCategoryReferenceType().optional(),
        taxRate: TaxCategory_types_1.taxRateType().optional(),
        taxedPrice: taxedItemPriceType().optional()
    });
}
exports.customLineItemType = customLineItemType;
function customLineItemDraftType() {
    return Joi.object().unknown().keys({
        name: Common_types_6.localizedStringType().required(),
        money: Common_types_7.moneyType().required(),
        quantity: Joi.number().required(),
        slug: Joi.string().required(),
        custom: Type_types_1.customFieldsType().optional(),
        externalTaxRate: externalTaxRateDraftType().optional(),
        shippingDetails: itemShippingDetailsDraftType().optional(),
        taxCategory: TaxCategory_types_3.taxCategoryResourceIdentifierType().optional()
    });
}
exports.customLineItemDraftType = customLineItemDraftType;
function discountCodeInfoType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        state: discountCodeStateType().required()
    });
}
exports.discountCodeInfoType = discountCodeInfoType;
const discountCodeStateTypeValues = [
    'NotActive',
    'DoesNotMatchCart',
    'MatchesCart',
    'MaxApplicationReached'
];
function discountCodeStateType() {
    return Joi.string().only(discountCodeStateTypeValues);
}
exports.discountCodeStateType = discountCodeStateType;
function discountedLineItemPortionType() {
    return Joi.object().unknown().keys({
        discount: CartDiscount_types_1.cartDiscountReferenceType().required(),
        discountedAmount: Common_types_7.moneyType().required()
    });
}
exports.discountedLineItemPortionType = discountedLineItemPortionType;
function discountedLineItemPriceType() {
    return Joi.object().unknown().keys({
        includedDiscounts: Joi.array().items(discountedLineItemPortionType()).required(),
        value: Common_types_3.typedMoneyType().required()
    });
}
exports.discountedLineItemPriceType = discountedLineItemPriceType;
function discountedLineItemPriceForQuantityType() {
    return Joi.object().unknown().keys({
        discountedPrice: discountedLineItemPriceType().required(),
        quantity: Joi.number().required()
    });
}
exports.discountedLineItemPriceForQuantityType = discountedLineItemPriceForQuantityType;
function externalLineItemTotalPriceType() {
    return Joi.object().unknown().keys({
        price: Common_types_7.moneyType().required(),
        totalPrice: Common_types_7.moneyType().required()
    });
}
exports.externalLineItemTotalPriceType = externalLineItemTotalPriceType;
function externalTaxAmountDraftType() {
    return Joi.object().unknown().keys({
        taxRate: externalTaxRateDraftType().required(),
        totalGross: Common_types_7.moneyType().required()
    });
}
exports.externalTaxAmountDraftType = externalTaxAmountDraftType;
function externalTaxRateDraftType() {
    return Joi.object().unknown().keys({
        country: Joi.string().required(),
        name: Joi.string().required(),
        subRates: Joi.array().items(TaxCategory_types_4.subRateType()).optional(),
        includedInPrice: Joi.boolean().optional(),
        amount: Joi.number().optional(),
        state: Joi.string().optional()
    });
}
exports.externalTaxRateDraftType = externalTaxRateDraftType;
const inventoryModeTypeValues = [
    'TrackOnly',
    'ReserveOnOrder',
    'None'
];
function inventoryModeType() {
    return Joi.string().only(inventoryModeTypeValues);
}
exports.inventoryModeType = inventoryModeType;
function itemShippingDetailsType() {
    return Joi.object().unknown().keys({
        targets: Joi.array().items(itemShippingTargetType()).required(),
        valid: Joi.boolean().required()
    });
}
exports.itemShippingDetailsType = itemShippingDetailsType;
function itemShippingDetailsDraftType() {
    return Joi.object().unknown().keys({
        targets: Joi.array().items(itemShippingTargetType()).required()
    });
}
exports.itemShippingDetailsDraftType = itemShippingDetailsDraftType;
function itemShippingTargetType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        addressKey: Joi.string().required()
    });
}
exports.itemShippingTargetType = itemShippingTargetType;
function lineItemType() {
    return Joi.object().unknown().keys({
        discountedPricePerQuantity: Joi.array().items(discountedLineItemPriceForQuantityType()).required(),
        state: Joi.array().items(Order_types_2.itemStateType()).required(),
        lineItemMode: lineItemModeType().required(),
        priceMode: lineItemPriceModeType().required(),
        name: Common_types_6.localizedStringType().required(),
        totalPrice: Common_types_7.moneyType().required(),
        price: Common_types_8.priceType().required(),
        productType: ProductType_types_1.productTypeReferenceType().required(),
        variant: Product_types_1.productVariantType().required(),
        quantity: Joi.number().required(),
        id: Joi.string().required(),
        productId: Joi.string().required(),
        distributionChannel: Channel_types_1.channelReferenceType().optional(),
        supplyChannel: Channel_types_1.channelReferenceType().optional(),
        custom: Type_types_1.customFieldsType().optional(),
        shippingDetails: itemShippingDetailsType().optional(),
        productSlug: Common_types_6.localizedStringType().optional(),
        taxRate: TaxCategory_types_1.taxRateType().optional(),
        taxedPrice: taxedItemPriceType().optional()
    });
}
exports.lineItemType = lineItemType;
function lineItemDraftType() {
    return Joi.object().unknown().keys({
        distributionChannel: Channel_types_2.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_2.channelResourceIdentifierType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        externalTotalPrice: externalLineItemTotalPriceType().optional(),
        externalTaxRate: externalTaxRateDraftType().optional(),
        shippingDetails: itemShippingDetailsDraftType().optional(),
        externalPrice: Common_types_7.moneyType().optional(),
        quantity: Joi.number().optional(),
        variantId: Joi.number().optional(),
        productId: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.lineItemDraftType = lineItemDraftType;
const lineItemModeTypeValues = [
    'Standard',
    'GiftLineItem'
];
function lineItemModeType() {
    return Joi.string().only(lineItemModeTypeValues);
}
exports.lineItemModeType = lineItemModeType;
const lineItemPriceModeTypeValues = [
    'Platform',
    'ExternalTotal',
    'ExternalPrice'
];
function lineItemPriceModeType() {
    return Joi.string().only(lineItemPriceModeTypeValues);
}
exports.lineItemPriceModeType = lineItemPriceModeType;
function replicaCartDraftType() {
    return Joi.object().unknown().keys({
        reference: Joi.any().required()
    });
}
exports.replicaCartDraftType = replicaCartDraftType;
const roundingModeTypeValues = [
    'HalfEven',
    'HalfUp',
    'HalfDown'
];
function roundingModeType() {
    return Joi.string().only(roundingModeTypeValues);
}
exports.roundingModeType = roundingModeType;
function scoreShippingRateInputType() {
    return Joi.object().unknown().keys({
        score: Joi.number().required(),
        type: Joi.string().required().only('Score')
    });
}
exports.scoreShippingRateInputType = scoreShippingRateInputType;
function scoreShippingRateInputDraftType() {
    return Joi.object().unknown().keys({
        score: Joi.number().required(),
        type: Joi.string().required().only('Score')
    });
}
exports.scoreShippingRateInputDraftType = scoreShippingRateInputDraftType;
function shippingInfoType() {
    return Joi.object().unknown().keys({
        shippingMethodState: shippingMethodStateType().required(),
        shippingRate: ShippingMethod_types_2.shippingRateType().required(),
        price: Common_types_3.typedMoneyType().required(),
        shippingMethodName: Joi.string().required(),
        deliveries: Joi.array().items(Order_types_3.deliveryType()).optional(),
        discountedPrice: discountedLineItemPriceType().optional(),
        shippingMethod: ShippingMethod_types_3.shippingMethodReferenceType().optional(),
        taxCategory: TaxCategory_types_2.taxCategoryReferenceType().optional(),
        taxRate: TaxCategory_types_1.taxRateType().optional(),
        taxedPrice: taxedItemPriceType().optional()
    });
}
exports.shippingInfoType = shippingInfoType;
const shippingMethodStateTypeValues = [
    'DoesNotMatchCart',
    'MatchesCart'
];
function shippingMethodStateType() {
    return Joi.string().only(shippingMethodStateTypeValues);
}
exports.shippingMethodStateType = shippingMethodStateType;
function shippingRateInputType() {
    return Joi.alternatives([classificationShippingRateInputType(), scoreShippingRateInputType()]);
}
exports.shippingRateInputType = shippingRateInputType;
function shippingRateInputDraftType() {
    return Joi.alternatives([classificationShippingRateInputDraftType(), scoreShippingRateInputDraftType()]);
}
exports.shippingRateInputDraftType = shippingRateInputDraftType;
const taxCalculationModeTypeValues = [
    'LineItemLevel',
    'UnitPriceLevel'
];
function taxCalculationModeType() {
    return Joi.string().only(taxCalculationModeTypeValues);
}
exports.taxCalculationModeType = taxCalculationModeType;
const taxModeTypeValues = [
    'Platform',
    'External',
    'ExternalAmount',
    'Disabled'
];
function taxModeType() {
    return Joi.string().only(taxModeTypeValues);
}
exports.taxModeType = taxModeType;
function taxPortionType() {
    return Joi.object().unknown().keys({
        amount: Common_types_7.moneyType().required(),
        rate: Joi.number().required(),
        name: Joi.string().optional()
    });
}
exports.taxPortionType = taxPortionType;
function taxedItemPriceType() {
    return Joi.object().unknown().keys({
        totalGross: Common_types_3.typedMoneyType().required(),
        totalNet: Common_types_3.typedMoneyType().required()
    });
}
exports.taxedItemPriceType = taxedItemPriceType;
function taxedPriceType() {
    return Joi.object().unknown().keys({
        taxPortions: Joi.array().items(taxPortionType()).required(),
        totalGross: Common_types_7.moneyType().required(),
        totalNet: Common_types_7.moneyType().required()
    });
}
exports.taxedPriceType = taxedPriceType;
function cartAddCustomLineItemActionType() {
    return Joi.object().unknown().keys({
        name: Common_types_6.localizedStringType().required(),
        money: Common_types_7.moneyType().required(),
        quantity: Joi.number().required(),
        action: Joi.string().required().only('addCustomLineItem'),
        slug: Joi.string().required(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        externalTaxRate: externalTaxRateDraftType().optional(),
        taxCategory: TaxCategory_types_3.taxCategoryResourceIdentifierType().optional()
    });
}
exports.cartAddCustomLineItemActionType = cartAddCustomLineItemActionType;
function cartAddDiscountCodeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addDiscountCode'),
        code: Joi.string().required()
    });
}
exports.cartAddDiscountCodeActionType = cartAddDiscountCodeActionType;
function cartAddItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_4.addressType().required(),
        action: Joi.string().required().only('addItemShippingAddress')
    });
}
exports.cartAddItemShippingAddressActionType = cartAddItemShippingAddressActionType;
function cartAddLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('addLineItem'),
        distributionChannel: Channel_types_2.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_2.channelResourceIdentifierType().optional(),
        custom: Type_types_2.customFieldsDraftType().optional(),
        externalTotalPrice: externalLineItemTotalPriceType().optional(),
        externalTaxRate: externalTaxRateDraftType().optional(),
        shippingDetails: itemShippingDetailsDraftType().optional(),
        externalPrice: Common_types_7.moneyType().optional(),
        quantity: Joi.number().optional(),
        variantId: Joi.number().optional(),
        productId: Joi.string().optional(),
        sku: Joi.string().optional()
    });
}
exports.cartAddLineItemActionType = cartAddLineItemActionType;
function cartAddPaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('addPayment')
    });
}
exports.cartAddPaymentActionType = cartAddPaymentActionType;
function cartAddShoppingListActionType() {
    return Joi.object().unknown().keys({
        shoppingList: ShoppingList_types_1.shoppingListResourceIdentifierType().required(),
        action: Joi.string().required().only('addShoppingList'),
        distributionChannel: Channel_types_2.channelResourceIdentifierType().optional(),
        supplyChannel: Channel_types_2.channelResourceIdentifierType().optional()
    });
}
exports.cartAddShoppingListActionType = cartAddShoppingListActionType;
function cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType() {
    return Joi.object().unknown().keys({
        targetsDelta: Joi.array().items(itemShippingTargetType()).required(),
        action: Joi.string().required().only('applyDeltaToCustomLineItemShippingDetailsTargets'),
        customLineItemId: Joi.string().required()
    });
}
exports.cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType = cartApplyDeltaToCustomLineItemShippingDetailsTargetsActionType;
function cartApplyDeltaToLineItemShippingDetailsTargetsActionType() {
    return Joi.object().unknown().keys({
        targetsDelta: Joi.array().items(itemShippingTargetType()).required(),
        action: Joi.string().required().only('applyDeltaToLineItemShippingDetailsTargets'),
        lineItemId: Joi.string().required()
    });
}
exports.cartApplyDeltaToLineItemShippingDetailsTargetsActionType = cartApplyDeltaToLineItemShippingDetailsTargetsActionType;
function cartChangeCustomLineItemMoneyActionType() {
    return Joi.object().unknown().keys({
        money: Common_types_7.moneyType().required(),
        action: Joi.string().required().only('changeCustomLineItemMoney'),
        customLineItemId: Joi.string().required()
    });
}
exports.cartChangeCustomLineItemMoneyActionType = cartChangeCustomLineItemMoneyActionType;
function cartChangeCustomLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeCustomLineItemQuantity'),
        customLineItemId: Joi.string().required()
    });
}
exports.cartChangeCustomLineItemQuantityActionType = cartChangeCustomLineItemQuantityActionType;
function cartChangeLineItemQuantityActionType() {
    return Joi.object().unknown().keys({
        quantity: Joi.number().required(),
        action: Joi.string().required().only('changeLineItemQuantity'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: externalLineItemTotalPriceType().optional(),
        externalPrice: Common_types_7.moneyType().optional()
    });
}
exports.cartChangeLineItemQuantityActionType = cartChangeLineItemQuantityActionType;
function cartChangeTaxCalculationModeActionType() {
    return Joi.object().unknown().keys({
        taxCalculationMode: taxCalculationModeType().required(),
        action: Joi.string().required().only('changeTaxCalculationMode')
    });
}
exports.cartChangeTaxCalculationModeActionType = cartChangeTaxCalculationModeActionType;
function cartChangeTaxModeActionType() {
    return Joi.object().unknown().keys({
        taxMode: taxModeType().required(),
        action: Joi.string().required().only('changeTaxMode')
    });
}
exports.cartChangeTaxModeActionType = cartChangeTaxModeActionType;
function cartChangeTaxRoundingModeActionType() {
    return Joi.object().unknown().keys({
        taxRoundingMode: roundingModeType().required(),
        action: Joi.string().required().only('changeTaxRoundingMode')
    });
}
exports.cartChangeTaxRoundingModeActionType = cartChangeTaxRoundingModeActionType;
function cartRecalculateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('recalculate'),
        updateProductData: Joi.boolean().optional()
    });
}
exports.cartRecalculateActionType = cartRecalculateActionType;
function cartRemoveCustomLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeCustomLineItem'),
        customLineItemId: Joi.string().required()
    });
}
exports.cartRemoveCustomLineItemActionType = cartRemoveCustomLineItemActionType;
function cartRemoveDiscountCodeActionType() {
    return Joi.object().unknown().keys({
        discountCode: DiscountCode_types_1.discountCodeReferenceType().required(),
        action: Joi.string().required().only('removeDiscountCode')
    });
}
exports.cartRemoveDiscountCodeActionType = cartRemoveDiscountCodeActionType;
function cartRemoveItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeItemShippingAddress'),
        addressKey: Joi.string().required()
    });
}
exports.cartRemoveItemShippingAddressActionType = cartRemoveItemShippingAddressActionType;
function cartRemoveLineItemActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('removeLineItem'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: externalLineItemTotalPriceType().optional(),
        shippingDetailsToRemove: itemShippingDetailsDraftType().optional(),
        externalPrice: Common_types_7.moneyType().optional(),
        quantity: Joi.number().optional()
    });
}
exports.cartRemoveLineItemActionType = cartRemoveLineItemActionType;
function cartRemovePaymentActionType() {
    return Joi.object().unknown().keys({
        payment: Payment_types_1.paymentResourceIdentifierType().required(),
        action: Joi.string().required().only('removePayment')
    });
}
exports.cartRemovePaymentActionType = cartRemovePaymentActionType;
function cartSetAnonymousIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setAnonymousId'),
        anonymousId: Joi.string().optional()
    });
}
exports.cartSetAnonymousIdActionType = cartSetAnonymousIdActionType;
function cartSetBillingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setBillingAddress'),
        address: Common_types_4.addressType().optional()
    });
}
exports.cartSetBillingAddressActionType = cartSetBillingAddressActionType;
function cartSetCartTotalTaxActionType() {
    return Joi.object().unknown().keys({
        externalTotalGross: Common_types_7.moneyType().required(),
        action: Joi.string().required().only('setCartTotalTax'),
        externalTaxPortions: Joi.array().items(taxPortionType()).optional()
    });
}
exports.cartSetCartTotalTaxActionType = cartSetCartTotalTaxActionType;
function cartSetCountryActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCountry'),
        country: Joi.string().optional()
    });
}
exports.cartSetCountryActionType = cartSetCountryActionType;
function cartSetCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomField'),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.cartSetCustomFieldActionType = cartSetCustomFieldActionType;
function cartSetCustomLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomField'),
        customLineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.cartSetCustomLineItemCustomFieldActionType = cartSetCustomLineItemCustomFieldActionType;
function cartSetCustomLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemCustomType'),
        customLineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.cartSetCustomLineItemCustomTypeActionType = cartSetCustomLineItemCustomTypeActionType;
function cartSetCustomLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemShippingDetails'),
        customLineItemId: Joi.string().required(),
        shippingDetails: itemShippingDetailsDraftType().optional()
    });
}
exports.cartSetCustomLineItemShippingDetailsActionType = cartSetCustomLineItemShippingDetailsActionType;
function cartSetCustomLineItemTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemTaxAmount'),
        customLineItemId: Joi.string().required(),
        externalTaxAmount: externalTaxAmountDraftType().optional()
    });
}
exports.cartSetCustomLineItemTaxAmountActionType = cartSetCustomLineItemTaxAmountActionType;
function cartSetCustomLineItemTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomLineItemTaxRate'),
        customLineItemId: Joi.string().required(),
        externalTaxRate: externalTaxRateDraftType().optional()
    });
}
exports.cartSetCustomLineItemTaxRateActionType = cartSetCustomLineItemTaxRateActionType;
function cartSetCustomShippingMethodActionType() {
    return Joi.object().unknown().keys({
        shippingRate: ShippingMethod_types_4.shippingRateDraftType().required(),
        action: Joi.string().required().only('setCustomShippingMethod'),
        shippingMethodName: Joi.string().required(),
        externalTaxRate: externalTaxRateDraftType().optional(),
        taxCategory: TaxCategory_types_3.taxCategoryResourceIdentifierType().optional()
    });
}
exports.cartSetCustomShippingMethodActionType = cartSetCustomShippingMethodActionType;
function cartSetCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomType'),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.cartSetCustomTypeActionType = cartSetCustomTypeActionType;
function cartSetCustomerEmailActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerEmail'),
        email: Joi.string().required()
    });
}
exports.cartSetCustomerEmailActionType = cartSetCustomerEmailActionType;
function cartSetCustomerGroupActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerGroup'),
        customerGroup: CustomerGroup_types_2.customerGroupResourceIdentifierType().optional()
    });
}
exports.cartSetCustomerGroupActionType = cartSetCustomerGroupActionType;
function cartSetCustomerIdActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setCustomerId'),
        customerId: Joi.string().optional()
    });
}
exports.cartSetCustomerIdActionType = cartSetCustomerIdActionType;
function cartSetDeleteDaysAfterLastModificationActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setDeleteDaysAfterLastModification'),
        deleteDaysAfterLastModification: Joi.number().optional()
    });
}
exports.cartSetDeleteDaysAfterLastModificationActionType = cartSetDeleteDaysAfterLastModificationActionType;
function cartSetLineItemCustomFieldActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomField'),
        lineItemId: Joi.string().required(),
        name: Joi.string().required(),
        value: Joi.any().optional()
    });
}
exports.cartSetLineItemCustomFieldActionType = cartSetLineItemCustomFieldActionType;
function cartSetLineItemCustomTypeActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemCustomType'),
        lineItemId: Joi.string().required(),
        fields: Type_types_3.fieldContainerType().optional(),
        type: Type_types_4.typeResourceIdentifierType().optional()
    });
}
exports.cartSetLineItemCustomTypeActionType = cartSetLineItemCustomTypeActionType;
function cartSetLineItemPriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemPrice'),
        lineItemId: Joi.string().required(),
        externalPrice: Common_types_7.moneyType().optional()
    });
}
exports.cartSetLineItemPriceActionType = cartSetLineItemPriceActionType;
function cartSetLineItemShippingDetailsActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemShippingDetails'),
        lineItemId: Joi.string().required(),
        shippingDetails: itemShippingDetailsDraftType().optional()
    });
}
exports.cartSetLineItemShippingDetailsActionType = cartSetLineItemShippingDetailsActionType;
function cartSetLineItemTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTaxAmount'),
        lineItemId: Joi.string().required(),
        externalTaxAmount: externalTaxAmountDraftType().optional()
    });
}
exports.cartSetLineItemTaxAmountActionType = cartSetLineItemTaxAmountActionType;
function cartSetLineItemTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTaxRate'),
        lineItemId: Joi.string().required(),
        externalTaxRate: externalTaxRateDraftType().optional()
    });
}
exports.cartSetLineItemTaxRateActionType = cartSetLineItemTaxRateActionType;
function cartSetLineItemTotalPriceActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLineItemTotalPrice'),
        lineItemId: Joi.string().required(),
        externalTotalPrice: externalLineItemTotalPriceType().optional()
    });
}
exports.cartSetLineItemTotalPriceActionType = cartSetLineItemTotalPriceActionType;
function cartSetLocaleActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setLocale'),
        locale: Joi.string().optional()
    });
}
exports.cartSetLocaleActionType = cartSetLocaleActionType;
function cartSetShippingAddressActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingAddress'),
        address: Common_types_4.addressType().optional()
    });
}
exports.cartSetShippingAddressActionType = cartSetShippingAddressActionType;
function cartSetShippingMethodActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethod'),
        externalTaxRate: externalTaxRateDraftType().optional(),
        shippingMethod: ShippingMethod_types_1.shippingMethodResourceIdentifierType().optional()
    });
}
exports.cartSetShippingMethodActionType = cartSetShippingMethodActionType;
function cartSetShippingMethodTaxAmountActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethodTaxAmount'),
        externalTaxAmount: externalTaxAmountDraftType().optional()
    });
}
exports.cartSetShippingMethodTaxAmountActionType = cartSetShippingMethodTaxAmountActionType;
function cartSetShippingMethodTaxRateActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingMethodTaxRate'),
        externalTaxRate: externalTaxRateDraftType().optional()
    });
}
exports.cartSetShippingMethodTaxRateActionType = cartSetShippingMethodTaxRateActionType;
function cartSetShippingRateInputActionType() {
    return Joi.object().unknown().keys({
        action: Joi.string().required().only('setShippingRateInput'),
        shippingRateInput: shippingRateInputDraftType().optional()
    });
}
exports.cartSetShippingRateInputActionType = cartSetShippingRateInputActionType;
function cartUpdateItemShippingAddressActionType() {
    return Joi.object().unknown().keys({
        address: Common_types_4.addressType().required(),
        action: Joi.string().required().only('updateItemShippingAddress')
    });
}
exports.cartUpdateItemShippingAddressActionType = cartUpdateItemShippingAddressActionType;
const productPublishScopeTypeValues = [
    'All',
    'Prices'
];
function productPublishScopeType() {
    return Joi.string().only(productPublishScopeTypeValues);
}
exports.productPublishScopeType = productPublishScopeType;
