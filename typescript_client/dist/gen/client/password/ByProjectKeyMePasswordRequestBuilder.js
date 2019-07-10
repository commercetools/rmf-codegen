"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyMePasswordResetRequestBuilder_1 = require("./../reset/ByProjectKeyMePasswordResetRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyMePasswordRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    reset() {
        return new ByProjectKeyMePasswordResetRequestBuilder_1.ByProjectKeyMePasswordResetRequestBuilder({
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
            uriTemplate: '/{projectKey}/me/password',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyMePasswordRequestBuilder = ByProjectKeyMePasswordRequestBuilder;
