"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyOrdersEditsByIDApplyRequestBuilder_1 = require("./../apply/ByProjectKeyOrdersEditsByIDApplyRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyOrdersEditsByIDRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    apply() {
        return new ByProjectKeyOrdersEditsByIDApplyRequestBuilder_1.ByProjectKeyOrdersEditsByIDApplyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    get(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'GET',
            uriTemplate: '/{projectKey}/orders/edits/{ID}',
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
            uriTemplate: '/{projectKey}/orders/edits/{ID}',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
    delete(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'DELETE',
            uriTemplate: '/{projectKey}/orders/edits/{ID}',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyOrdersEditsByIDRequestBuilder = ByProjectKeyOrdersEditsByIDRequestBuilder;
