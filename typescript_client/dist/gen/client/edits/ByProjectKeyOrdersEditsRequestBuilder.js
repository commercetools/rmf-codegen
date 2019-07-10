"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyOrdersEditsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyOrdersEditsKeyByKeyRequestBuilder");
const ByProjectKeyOrdersEditsByIDRequestBuilder_1 = require("./ByProjectKeyOrdersEditsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyOrdersEditsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyOrdersEditsKeyByKeyRequestBuilder_1.ByProjectKeyOrdersEditsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyOrdersEditsByIDRequestBuilder_1.ByProjectKeyOrdersEditsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/orders/edits',
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
            uriTemplate: '/{projectKey}/orders/edits',
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
exports.ByProjectKeyOrdersEditsRequestBuilder = ByProjectKeyOrdersEditsRequestBuilder;
