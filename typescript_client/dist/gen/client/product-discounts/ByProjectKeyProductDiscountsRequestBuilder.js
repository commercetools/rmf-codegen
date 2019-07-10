"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyProductDiscountsMatchingRequestBuilder_1 = require("./../matching/ByProjectKeyProductDiscountsMatchingRequestBuilder");
const ByProjectKeyProductDiscountsByIDRequestBuilder_1 = require("./ByProjectKeyProductDiscountsByIDRequestBuilder");
const ByProjectKeyProductDiscountsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyProductDiscountsKeyByKeyRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyProductDiscountsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    matching() {
        return new ByProjectKeyProductDiscountsMatchingRequestBuilder_1.ByProjectKeyProductDiscountsMatchingRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyProductDiscountsByIDRequestBuilder_1.ByProjectKeyProductDiscountsByIDRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withKey(childPathArgs) {
        return new ByProjectKeyProductDiscountsKeyByKeyRequestBuilder_1.ByProjectKeyProductDiscountsKeyByKeyRequestBuilder({
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
            uriTemplate: '/{projectKey}/product-discounts',
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
            uriTemplate: '/{projectKey}/product-discounts',
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
exports.ByProjectKeyProductDiscountsRequestBuilder = ByProjectKeyProductDiscountsRequestBuilder;
