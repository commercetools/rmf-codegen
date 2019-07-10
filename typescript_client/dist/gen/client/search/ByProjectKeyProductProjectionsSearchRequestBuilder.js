"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyProductProjectionsSearchRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    post(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'POST',
            uriTemplate: '/{projectKey}/product-projections/search',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
        }, this.args.middlewares);
    }
    get(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'GET',
            uriTemplate: '/{projectKey}/product-projections/search',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyProductProjectionsSearchRequestBuilder = ByProjectKeyProductProjectionsSearchRequestBuilder;
