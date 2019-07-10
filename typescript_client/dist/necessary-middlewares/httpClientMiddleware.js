"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const axios = require("axios");
const client = axios.default;
exports.httpMiddleware = async (args) => {
    try {
        const { request, error, next } = args;
        if (error != null) {
            return next(args);
        }
        const axiosConfig = {
            method: request.method,
            url: request.uri,
            data: request.body,
            headers: request.headers
        };
        const response = await client.request(axiosConfig);
        return args.next({
            ...args,
            response: {
                body: response.data,
                headers: response.headers,
                statusCode: response.status
            }
        });
    }
    catch (error) {
        if (error.response && error.response.data) {
            return args.next({
                ...args,
                error: error.response.data
            });
        }
        return args.next({
            ...args,
            error
        });
    }
};
