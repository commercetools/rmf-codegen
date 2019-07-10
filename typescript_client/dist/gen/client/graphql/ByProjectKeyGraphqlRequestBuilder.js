"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyGraphqlRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    post(methodArgs) {
        return new requests_utils_1.ApiRequest({
            baseURL: 'https://api.sphere.io',
            method: 'POST',
            uriTemplate: '/{projectKey}/graphql',
            pathVariables: this.args.pathArgs,
            headers: {
                'Content-Type': 'application/graphql',
                ...(methodArgs || {}).headers
            },
            body: (methodArgs || {}).body,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyGraphqlRequestBuilder = ByProjectKeyGraphqlRequestBuilder;
