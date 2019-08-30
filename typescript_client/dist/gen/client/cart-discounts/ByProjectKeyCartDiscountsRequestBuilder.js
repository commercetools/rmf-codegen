"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCartDiscountsByIDRequestBuilder_1 = require("./ByProjectKeyCartDiscountsByIDRequestBuilder");
const ByProjectKeyCartDiscountsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyCartDiscountsKeyByKeyRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCartDiscountsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withId(childPathArgs) {
        return new ByProjectKeyCartDiscountsByIDRequestBuilder_1.ByProjectKeyCartDiscountsByIDRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withKey(childPathArgs) {
        return new ByProjectKeyCartDiscountsKeyByKeyRequestBuilder_1.ByProjectKeyCartDiscountsKeyByKeyRequestBuilder({
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
            uriTemplate: '/{projectKey}/cart-discounts',
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
            uriTemplate: '/{projectKey}/cart-discounts',
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
exports.ByProjectKeyCartDiscountsRequestBuilder = ByProjectKeyCartDiscountsRequestBuilder;
