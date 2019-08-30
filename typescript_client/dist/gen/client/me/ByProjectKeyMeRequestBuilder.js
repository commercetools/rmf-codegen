"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyMeEmailRequestBuilder_1 = require("./../email/ByProjectKeyMeEmailRequestBuilder");
const ByProjectKeyMePasswordRequestBuilder_1 = require("./../password/ByProjectKeyMePasswordRequestBuilder");
const ByProjectKeyMeSignupRequestBuilder_1 = require("./../signup/ByProjectKeyMeSignupRequestBuilder");
const ByProjectKeyMeLoginRequestBuilder_1 = require("./../login/ByProjectKeyMeLoginRequestBuilder");
const ByProjectKeyMeActiveCartRequestBuilder_1 = require("./../active-cart/ByProjectKeyMeActiveCartRequestBuilder");
const ByProjectKeyMeCartsRequestBuilder_1 = require("./../carts/ByProjectKeyMeCartsRequestBuilder");
const ByProjectKeyMeOrdersRequestBuilder_1 = require("./../orders/ByProjectKeyMeOrdersRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyMeRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    email() {
        return new ByProjectKeyMeEmailRequestBuilder_1.ByProjectKeyMeEmailRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    password() {
        return new ByProjectKeyMePasswordRequestBuilder_1.ByProjectKeyMePasswordRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    signup() {
        return new ByProjectKeyMeSignupRequestBuilder_1.ByProjectKeyMeSignupRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    login() {
        return new ByProjectKeyMeLoginRequestBuilder_1.ByProjectKeyMeLoginRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    activeCart() {
        return new ByProjectKeyMeActiveCartRequestBuilder_1.ByProjectKeyMeActiveCartRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    carts() {
        return new ByProjectKeyMeCartsRequestBuilder_1.ByProjectKeyMeCartsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    orders() {
        return new ByProjectKeyMeOrdersRequestBuilder_1.ByProjectKeyMeOrdersRequestBuilder({
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
            uriTemplate: '/{projectKey}/me',
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
            uriTemplate: '/{projectKey}/me',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
    delete(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'DELETE',
            uriTemplate: '/{projectKey}/me',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyMeRequestBuilder = ByProjectKeyMeRequestBuilder;
