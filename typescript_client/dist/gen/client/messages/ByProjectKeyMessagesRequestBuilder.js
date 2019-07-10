"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyMessagesByIDRequestBuilder_1 = require("./ByProjectKeyMessagesByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyMessagesRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withId(childPathArgs) {
        return new ByProjectKeyMessagesByIDRequestBuilder_1.ByProjectKeyMessagesByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/messages',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyMessagesRequestBuilder = ByProjectKeyMessagesRequestBuilder;
