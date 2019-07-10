"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCategoriesKeyByKeyRequestBuilder_1 = require("./ByProjectKeyCategoriesKeyByKeyRequestBuilder");
const ByProjectKeyCategoriesByIDRequestBuilder_1 = require("./ByProjectKeyCategoriesByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCategoriesRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyCategoriesKeyByKeyRequestBuilder_1.ByProjectKeyCategoriesKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyCategoriesByIDRequestBuilder_1.ByProjectKeyCategoriesByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/categories',
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
            uriTemplate: '/{projectKey}/categories',
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
exports.ByProjectKeyCategoriesRequestBuilder = ByProjectKeyCategoriesRequestBuilder;
