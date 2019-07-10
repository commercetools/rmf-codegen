"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCartsReplicateRequestBuilder_1 = require("./../replicate/ByProjectKeyCartsReplicateRequestBuilder");
const ByProjectKeyCartsByIDRequestBuilder_1 = require("./ByProjectKeyCartsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCartsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    replicate() {
        return new ByProjectKeyCartsReplicateRequestBuilder_1.ByProjectKeyCartsReplicateRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyCartsByIDRequestBuilder_1.ByProjectKeyCartsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/carts',
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
            uriTemplate: '/{projectKey}/carts',
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
exports.ByProjectKeyCartsRequestBuilder = ByProjectKeyCartsRequestBuilder;
