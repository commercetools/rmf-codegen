"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyTaxCategoriesByIDRequestBuilder_1 = require("./ByProjectKeyTaxCategoriesByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyTaxCategoriesRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withId(childPathArgs) {
        return new ByProjectKeyTaxCategoriesByIDRequestBuilder_1.ByProjectKeyTaxCategoriesByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/tax-categories',
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
            uriTemplate: '/{projectKey}/tax-categories',
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
exports.ByProjectKeyTaxCategoriesRequestBuilder = ByProjectKeyTaxCategoriesRequestBuilder;
