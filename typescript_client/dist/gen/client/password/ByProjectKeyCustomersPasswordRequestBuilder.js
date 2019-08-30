"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCustomersPasswordResetRequestBuilder_1 = require("./../reset/ByProjectKeyCustomersPasswordResetRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCustomersPasswordRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    reset() {
        return new ByProjectKeyCustomersPasswordResetRequestBuilder_1.ByProjectKeyCustomersPasswordResetRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    post(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'POST',
            uriTemplate: '/{projectKey}/customers/password',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyCustomersPasswordRequestBuilder = ByProjectKeyCustomersPasswordRequestBuilder;
