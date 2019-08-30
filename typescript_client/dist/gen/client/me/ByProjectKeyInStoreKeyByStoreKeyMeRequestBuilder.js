"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyInStoreKeyByStoreKeyMeCartsRequestBuilder_1 = require("./../carts/ByProjectKeyInStoreKeyByStoreKeyMeCartsRequestBuilder");
const ByProjectKeyInStoreKeyByStoreKeyMeOrdersRequestBuilder_1 = require("./../orders/ByProjectKeyInStoreKeyByStoreKeyMeOrdersRequestBuilder");
const ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder_1 = require("./../active-cart/ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder");
class ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    carts() {
        return new ByProjectKeyInStoreKeyByStoreKeyMeCartsRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyMeCartsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    orders() {
        return new ByProjectKeyInStoreKeyByStoreKeyMeOrdersRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyMeOrdersRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    activeCart() {
        return new ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
}
exports.ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder = ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder;
