"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyMeActiveCartRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    get(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'GET',
            uriTemplate: '/{projectKey}/me/active-cart',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyMeActiveCartRequestBuilder = ByProjectKeyMeActiveCartRequestBuilder;
