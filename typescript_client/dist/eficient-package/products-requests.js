"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function getAllProducts(methodArgs) {
    return {
        baseURL: "https://api.sphere.io",
        method: "GET",
        uriTemplate: "/{projectKey}/products",
        pathVariables: (methodArgs || {}).pathVariables,
        headers: {
            ...(methodArgs || {}).headers
        },
        queryParams: (methodArgs || {}).queryArgs
    };
}
exports.getAllProducts = getAllProducts;
