"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyProductsByIDImagesRequestBuilder_1 = require("./../images/ByProjectKeyProductsByIDImagesRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyProductsByIDRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    images() {
        return new ByProjectKeyProductsByIDImagesRequestBuilder_1.ByProjectKeyProductsByIDImagesRequestBuilder({
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
            uriTemplate: '/{projectKey}/products/{ID}',
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
            uriTemplate: '/{projectKey}/products/{ID}',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/json',
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
    delete(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'DELETE',
            uriTemplate: '/{projectKey}/products/{ID}',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyProductsByIDRequestBuilder = ByProjectKeyProductsByIDRequestBuilder;
