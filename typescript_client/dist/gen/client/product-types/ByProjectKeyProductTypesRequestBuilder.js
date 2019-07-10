"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyProductTypesKeyByKeyRequestBuilder_1 = require("./ByProjectKeyProductTypesKeyByKeyRequestBuilder");
const ByProjectKeyProductTypesByIDRequestBuilder_1 = require("./ByProjectKeyProductTypesByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyProductTypesRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyProductTypesKeyByKeyRequestBuilder_1.ByProjectKeyProductTypesKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyProductTypesByIDRequestBuilder_1.ByProjectKeyProductTypesByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/product-types',
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
            uriTemplate: '/{projectKey}/product-types',
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
exports.ByProjectKeyProductTypesRequestBuilder = ByProjectKeyProductTypesRequestBuilder;
