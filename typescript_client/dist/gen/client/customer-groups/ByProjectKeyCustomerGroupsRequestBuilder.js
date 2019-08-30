"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCustomerGroupsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyCustomerGroupsKeyByKeyRequestBuilder");
const ByProjectKeyCustomerGroupsByIDRequestBuilder_1 = require("./ByProjectKeyCustomerGroupsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyCustomerGroupsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    withKey(childPathArgs) {
        return new ByProjectKeyCustomerGroupsKeyByKeyRequestBuilder_1.ByProjectKeyCustomerGroupsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyCustomerGroupsByIDRequestBuilder_1.ByProjectKeyCustomerGroupsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/customer-groups',
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
            uriTemplate: '/{projectKey}/customer-groups',
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
exports.ByProjectKeyCustomerGroupsRequestBuilder = ByProjectKeyCustomerGroupsRequestBuilder;
