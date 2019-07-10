"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    get(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'GET',
            uriTemplate: '/{projectKey}/in-store/key={storeKey}/me/active-cart',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder = ByProjectKeyInStoreKeyByStoreKeyMeActiveCartRequestBuilder;
