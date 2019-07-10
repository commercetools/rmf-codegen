"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyProductProjectionsSearchRequestBuilder_1 = require("./../search/ByProjectKeyProductProjectionsSearchRequestBuilder");
const ByProjectKeyProductProjectionsSuggestRequestBuilder_1 = require("./../suggest/ByProjectKeyProductProjectionsSuggestRequestBuilder");
const ByProjectKeyProductProjectionsKeyByKeyRequestBuilder_1 = require("./ByProjectKeyProductProjectionsKeyByKeyRequestBuilder");
const ByProjectKeyProductProjectionsByIDRequestBuilder_1 = require("./ByProjectKeyProductProjectionsByIDRequestBuilder");
const requests_utils_1 = require("./../../base/requests-utils");
class ByProjectKeyProductProjectionsRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    search() {
        return new ByProjectKeyProductProjectionsSearchRequestBuilder_1.ByProjectKeyProductProjectionsSearchRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    suggest() {
        return new ByProjectKeyProductProjectionsSuggestRequestBuilder_1.ByProjectKeyProductProjectionsSuggestRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
    withKey(childPathArgs) {
        return new ByProjectKeyProductProjectionsKeyByKeyRequestBuilder_1.ByProjectKeyProductProjectionsKeyByKeyRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
    withId(childPathArgs) {
        return new ByProjectKeyProductProjectionsByIDRequestBuilder_1.ByProjectKeyProductProjectionsByIDRequestBuilder({
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
            uriTemplate: '/{projectKey}/product-projections',
            pathVariables: this.args.pathArgs,
            headers: {
                ...(methodArgs || {}).headers
            },
            queryParams: (methodArgs || {}).queryArgs,
        }, this.args.middlewares);
    }
}
exports.ByProjectKeyProductProjectionsRequestBuilder = ByProjectKeyProductProjectionsRequestBuilder;
