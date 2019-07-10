"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCustomersPasswordTokenByPasswordTokenRequestBuilder_1 = require("./ByProjectKeyCustomersPasswordTokenByPasswordTokenRequestBuilder");
const ByProjectKeyCustomersEmailTokenByEmailTokenRequestBuilder_1 = require("./ByProjectKeyCustomersEmailTokenByEmailTokenRequestBuilder");
const ByProjectKeyCustomersEmailTokenRequestBuilder_1 = require("./../email-token/ByProjectKeyCustomersEmailTokenRequestBuilder");
const ByProjectKeyCustomersEmailRequestBuilder_1 = require("./../email/ByProjectKeyCustomersEmailRequestBuilder");
const ByProjectKeyCustomersPasswordRequestBuilder_1 = require("./../password/ByProjectKeyCustomersPasswordRequestBuilder");
const ByProjectKeyCustomersPasswordTokenRequestBuilder_1 = require("./../password-token/ByProjectKeyCustomersPasswordTokenRequestBuilder");
const ByProjectKeyCustomersByIDRequestBuilder_1 = require("./ByProjectKeyCustomersByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCustomersRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withPasswordToken(childPathArgs) {
        return new ByProjectKeyCustomersPasswordTokenByPasswordTokenRequestBuilder_1.ByProjectKeyCustomersPasswordTokenByPasswordTokenRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withEmailToken(childPathArgs) {
        return new ByProjectKeyCustomersEmailTokenByEmailTokenRequestBuilder_1.ByProjectKeyCustomersEmailTokenByEmailTokenRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    emailToken() {
        return new ByProjectKeyCustomersEmailTokenRequestBuilder_1.ByProjectKeyCustomersEmailTokenRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    email() {
        return new ByProjectKeyCustomersEmailRequestBuilder_1.ByProjectKeyCustomersEmailRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    password() {
        return new ByProjectKeyCustomersPasswordRequestBuilder_1.ByProjectKeyCustomersPasswordRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    passwordToken() {
        return new ByProjectKeyCustomersPasswordTokenRequestBuilder_1.ByProjectKeyCustomersPasswordTokenRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyCustomersByIDRequestBuilder_1.ByProjectKeyCustomersByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/customers',
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
            uriTemplate: '/{projectKey}/customers',
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
exports.ByProjectKeyCustomersRequestBuilder = ByProjectKeyCustomersRequestBuilder;
