"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeySubscriptionsKeyByKeyRequestBuilder_1 = require("./ByProjectKeySubscriptionsKeyByKeyRequestBuilder");
const ByProjectKeySubscriptionsByIDRequestBuilder_1 = require("./ByProjectKeySubscriptionsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeySubscriptionsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeySubscriptionsKeyByKeyRequestBuilder_1.ByProjectKeySubscriptionsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeySubscriptionsByIDRequestBuilder_1.ByProjectKeySubscriptionsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/subscriptions',
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
            uriTemplate: '/{projectKey}/subscriptions',
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
exports.ByProjectKeySubscriptionsRequestBuilder = ByProjectKeySubscriptionsRequestBuilder;
