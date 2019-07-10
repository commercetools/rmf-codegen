"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCategoriesRequestBuilder_1 = require("./categories/ByProjectKeyCategoriesRequestBuilder");
const ByProjectKeyCartsRequestBuilder_1 = require("./carts/ByProjectKeyCartsRequestBuilder");
const ByProjectKeyCartDiscountsRequestBuilder_1 = require("./cart-discounts/ByProjectKeyCartDiscountsRequestBuilder");
const ByProjectKeyChannelsRequestBuilder_1 = require("./channels/ByProjectKeyChannelsRequestBuilder");
const ByProjectKeyCustomersRequestBuilder_1 = require("./customers/ByProjectKeyCustomersRequestBuilder");
const ByProjectKeyCustomerGroupsRequestBuilder_1 = require("./customer-groups/ByProjectKeyCustomerGroupsRequestBuilder");
const ByProjectKeyCustomObjectsRequestBuilder_1 = require("./custom-objects/ByProjectKeyCustomObjectsRequestBuilder");
const ByProjectKeyDiscountCodesRequestBuilder_1 = require("./discount-codes/ByProjectKeyDiscountCodesRequestBuilder");
const ByProjectKeyGraphqlRequestBuilder_1 = require("./graphql/ByProjectKeyGraphqlRequestBuilder");
const ByProjectKeyInventoryRequestBuilder_1 = require("./inventory/ByProjectKeyInventoryRequestBuilder");
const ByProjectKeyLoginRequestBuilder_1 = require("./login/ByProjectKeyLoginRequestBuilder");
const ByProjectKeyMessagesRequestBuilder_1 = require("./messages/ByProjectKeyMessagesRequestBuilder");
const ByProjectKeyOrdersRequestBuilder_1 = require("./orders/ByProjectKeyOrdersRequestBuilder");
const ByProjectKeyPaymentsRequestBuilder_1 = require("./payments/ByProjectKeyPaymentsRequestBuilder");
const ByProjectKeyProductsRequestBuilder_1 = require("./products/ByProjectKeyProductsRequestBuilder");
const ByProjectKeyProductDiscountsRequestBuilder_1 = require("./product-discounts/ByProjectKeyProductDiscountsRequestBuilder");
const ByProjectKeyProductProjectionsRequestBuilder_1 = require("./product-projections/ByProjectKeyProductProjectionsRequestBuilder");
const ByProjectKeyProductTypesRequestBuilder_1 = require("./product-types/ByProjectKeyProductTypesRequestBuilder");
const ByProjectKeyReviewsRequestBuilder_1 = require("./reviews/ByProjectKeyReviewsRequestBuilder");
const ByProjectKeyShippingMethodsRequestBuilder_1 = require("./shipping-methods/ByProjectKeyShippingMethodsRequestBuilder");
const ByProjectKeyShoppingListsRequestBuilder_1 = require("./shopping-lists/ByProjectKeyShoppingListsRequestBuilder");
const ByProjectKeyStatesRequestBuilder_1 = require("./states/ByProjectKeyStatesRequestBuilder");
const ByProjectKeySubscriptionsRequestBuilder_1 = require("./subscriptions/ByProjectKeySubscriptionsRequestBuilder");
const ByProjectKeyTaxCategoriesRequestBuilder_1 = require("./tax-categories/ByProjectKeyTaxCategoriesRequestBuilder");
const ByProjectKeyTypesRequestBuilder_1 = require("./types/ByProjectKeyTypesRequestBuilder");
const ByProjectKeyZonesRequestBuilder_1 = require("./zones/ByProjectKeyZonesRequestBuilder");
const ByProjectKeyMeRequestBuilder_1 = require("./me/ByProjectKeyMeRequestBuilder");
const ByProjectKeyExtensionsRequestBuilder_1 = require("./extensions/ByProjectKeyExtensionsRequestBuilder");
const ByProjectKeyApiClientsRequestBuilder_1 = require("./api-clients/ByProjectKeyApiClientsRequestBuilder");
const ByProjectKeyStoresRequestBuilder_1 = require("./stores/ByProjectKeyStoresRequestBuilder");
const ByProjectKeyInStoreKeyByStoreKeyRequestBuilder_1 = require("./in-store/ByProjectKeyInStoreKeyByStoreKeyRequestBuilder");
const requests_utils_1 = require("./../base/requests-utils");
class ByProjectKeyRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    categories() {
        return new ByProjectKeyCategoriesRequestBuilder_1.ByProjectKeyCategoriesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    carts() {
        return new ByProjectKeyCartsRequestBuilder_1.ByProjectKeyCartsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    cartDiscounts() {
        return new ByProjectKeyCartDiscountsRequestBuilder_1.ByProjectKeyCartDiscountsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    channels() {
        return new ByProjectKeyChannelsRequestBuilder_1.ByProjectKeyChannelsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    customers() {
        return new ByProjectKeyCustomersRequestBuilder_1.ByProjectKeyCustomersRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    customerGroups() {
        return new ByProjectKeyCustomerGroupsRequestBuilder_1.ByProjectKeyCustomerGroupsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    customObjects() {
        return new ByProjectKeyCustomObjectsRequestBuilder_1.ByProjectKeyCustomObjectsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    discountCodes() {
        return new ByProjectKeyDiscountCodesRequestBuilder_1.ByProjectKeyDiscountCodesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    graphql() {
        return new ByProjectKeyGraphqlRequestBuilder_1.ByProjectKeyGraphqlRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    inventory() {
        return new ByProjectKeyInventoryRequestBuilder_1.ByProjectKeyInventoryRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    login() {
        return new ByProjectKeyLoginRequestBuilder_1.ByProjectKeyLoginRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    messages() {
        return new ByProjectKeyMessagesRequestBuilder_1.ByProjectKeyMessagesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    orders() {
        return new ByProjectKeyOrdersRequestBuilder_1.ByProjectKeyOrdersRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    payments() {
        return new ByProjectKeyPaymentsRequestBuilder_1.ByProjectKeyPaymentsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    products() {
        return new ByProjectKeyProductsRequestBuilder_1.ByProjectKeyProductsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    productDiscounts() {
        return new ByProjectKeyProductDiscountsRequestBuilder_1.ByProjectKeyProductDiscountsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    productProjections() {
        return new ByProjectKeyProductProjectionsRequestBuilder_1.ByProjectKeyProductProjectionsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    productTypes() {
        return new ByProjectKeyProductTypesRequestBuilder_1.ByProjectKeyProductTypesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    reviews() {
        return new ByProjectKeyReviewsRequestBuilder_1.ByProjectKeyReviewsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    shippingMethods() {
        return new ByProjectKeyShippingMethodsRequestBuilder_1.ByProjectKeyShippingMethodsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    shoppingLists() {
        return new ByProjectKeyShoppingListsRequestBuilder_1.ByProjectKeyShoppingListsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    states() {
        return new ByProjectKeyStatesRequestBuilder_1.ByProjectKeyStatesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    subscriptions() {
        return new ByProjectKeySubscriptionsRequestBuilder_1.ByProjectKeySubscriptionsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    taxCategories() {
        return new ByProjectKeyTaxCategoriesRequestBuilder_1.ByProjectKeyTaxCategoriesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    types() {
        return new ByProjectKeyTypesRequestBuilder_1.ByProjectKeyTypesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    zones() {
        return new ByProjectKeyZonesRequestBuilder_1.ByProjectKeyZonesRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    me() {
        return new ByProjectKeyMeRequestBuilder_1.ByProjectKeyMeRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    extensions() {
        return new ByProjectKeyExtensionsRequestBuilder_1.ByProjectKeyExtensionsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    apiClients() {
        return new ByProjectKeyApiClientsRequestBuilder_1.ByProjectKeyApiClientsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    stores() {
        return new ByProjectKeyStoresRequestBuilder_1.ByProjectKeyStoresRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    inStoreKeyWithStoreKeyValue(childPathArgs) {
        return new ByProjectKeyInStoreKeyByStoreKeyRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    get(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'GET',
            uriTemplate: '/{projectKey}',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
        }, this.args.middlewares);
    }
    post(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'POST',
            uriTemplate: '/{projectKey}',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyRequestBuilder = ByProjectKeyRequestBuilder;
