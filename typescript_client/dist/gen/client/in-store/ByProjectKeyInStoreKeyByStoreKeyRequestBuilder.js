"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyInStoreKeyByStoreKeyCartsRequestBuilder_1 = require("./../carts/ByProjectKeyInStoreKeyByStoreKeyCartsRequestBuilder");
const ByProjectKeyInStoreKeyByStoreKeyOrdersRequestBuilder_1 = require("./../orders/ByProjectKeyInStoreKeyByStoreKeyOrdersRequestBuilder");
const ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder_1 = require("./../me/ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder");
class ByProjectKeyInStoreKeyByStoreKeyRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    carts() {
        return new ByProjectKeyInStoreKeyByStoreKeyCartsRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyCartsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    orders() {
        return new ByProjectKeyInStoreKeyByStoreKeyOrdersRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyOrdersRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    me() {
        return new ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder_1.ByProjectKeyInStoreKeyByStoreKeyMeRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
}
exports.ByProjectKeyInStoreKeyByStoreKeyRequestBuilder = ByProjectKeyInStoreKeyByStoreKeyRequestBuilder;
