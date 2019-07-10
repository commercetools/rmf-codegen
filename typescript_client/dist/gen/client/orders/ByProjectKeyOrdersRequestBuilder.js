"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyOrdersImportRequestBuilder_1 = require("./../import/ByProjectKeyOrdersImportRequestBuilder");
const ByProjectKeyOrdersOrderNumberByOrderNumberRequestBuilder_1 = require("./ByProjectKeyOrdersOrderNumberByOrderNumberRequestBuilder");
const ByProjectKeyOrdersEditsRequestBuilder_1 = require("./../edits/ByProjectKeyOrdersEditsRequestBuilder");
const ByProjectKeyOrdersByIDRequestBuilder_1 = require("./ByProjectKeyOrdersByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyOrdersRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    import() {
        return new ByProjectKeyOrdersImportRequestBuilder_1.ByProjectKeyOrdersImportRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withOrderNumber(childPathArgs) {
        return new ByProjectKeyOrdersOrderNumberByOrderNumberRequestBuilder_1.ByProjectKeyOrdersOrderNumberByOrderNumberRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    edits() {
        return new ByProjectKeyOrdersEditsRequestBuilder_1.ByProjectKeyOrdersEditsRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyOrdersByIDRequestBuilder_1.ByProjectKeyOrdersByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/orders',
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
            uriTemplate: '/{projectKey}/orders',
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
exports.ByProjectKeyOrdersRequestBuilder = ByProjectKeyOrdersRequestBuilder;
