"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyStoresKeyByKeyRequestBuilder_1 = require("./ByProjectKeyStoresKeyByKeyRequestBuilder");
const ByProjectKeyStoresByIDRequestBuilder_1 = require("./ByProjectKeyStoresByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyStoresRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyStoresKeyByKeyRequestBuilder_1.ByProjectKeyStoresKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyStoresByIDRequestBuilder_1.ByProjectKeyStoresByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/stores',
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
            uriTemplate: '/{projectKey}/stores',
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
exports.ByProjectKeyStoresRequestBuilder = ByProjectKeyStoresRequestBuilder;
