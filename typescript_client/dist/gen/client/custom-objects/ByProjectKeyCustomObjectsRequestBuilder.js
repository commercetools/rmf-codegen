"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder_1 = require("./ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder");
const ByProjectKeyCustomObjectsByIDRequestBuilder_1 = require("./ByProjectKeyCustomObjectsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCustomObjectsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withContainerAndKey(childPathArgs) {
        return new ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder_1.ByProjectKeyCustomObjectsByContainerByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyCustomObjectsByIDRequestBuilder_1.ByProjectKeyCustomObjectsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/custom-objects',
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
            uriTemplate: '/{projectKey}/custom-objects',
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
exports.ByProjectKeyCustomObjectsRequestBuilder = ByProjectKeyCustomObjectsRequestBuilder;
