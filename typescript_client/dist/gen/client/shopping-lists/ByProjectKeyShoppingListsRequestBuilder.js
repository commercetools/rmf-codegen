"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyShoppingListsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyShoppingListsKeyByKeyRequestBuilder");
const ByProjectKeyShoppingListsByIDRequestBuilder_1 = require("./ByProjectKeyShoppingListsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyShoppingListsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyShoppingListsKeyByKeyRequestBuilder_1.ByProjectKeyShoppingListsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyShoppingListsByIDRequestBuilder_1.ByProjectKeyShoppingListsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/shopping-lists',
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
            uriTemplate: '/{projectKey}/shopping-lists',
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
exports.ByProjectKeyShoppingListsRequestBuilder = ByProjectKeyShoppingListsRequestBuilder;
