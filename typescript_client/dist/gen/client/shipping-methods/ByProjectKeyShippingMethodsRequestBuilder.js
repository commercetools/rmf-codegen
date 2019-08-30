"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyShippingMethodsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyShippingMethodsKeyByKeyRequestBuilder");
const ByProjectKeyShippingMethodsByIDRequestBuilder_1 = require("./ByProjectKeyShippingMethodsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyShippingMethodsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyShippingMethodsKeyByKeyRequestBuilder_1.ByProjectKeyShippingMethodsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyShippingMethodsByIDRequestBuilder_1.ByProjectKeyShippingMethodsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/shipping-methods',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
    post(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'POST',
            uriTemplate: '/{projectKey}/shipping-methods',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyShippingMethodsRequestBuilder = ByProjectKeyShippingMethodsRequestBuilder;
