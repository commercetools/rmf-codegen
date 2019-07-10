"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class ApiRequest {
    constructor(commonRequest, middlewares) {
        this.commonRequest = commonRequest;
        if (!middlewares || middlewares.length == 0) {
            middlewares = [noOpMiddleware];
        }
        this.middleware = middlewares.reduce(reduceMiddleware);
    }
    async execute() {
        const { body, headers, method } = this.commonRequest;
        const req = {
            headers,
            method,
            body,
            uri: getURI(this.commonRequest)
        };
        const res = await this.middleware({
            request: req,
            next: noOpMiddleware,
        });
        if (res.error) {
            throw res.error;
        }
        if (res.response) {
            return res.response;
        }
        return {
            body: {}
        };
    }
}
exports.ApiRequest = ApiRequest;
function reduceMiddleware(op1, op2) {
    return async (arg) => {
        const { next, ...rest } = arg;
        const intermediateOp = (tmpArg) => {
            const { next, ...rest } = tmpArg;
            return op2({ ...rest, next: arg.next });
        };
        return op1({
            ...rest,
            next: intermediateOp
        });
    };
}
function getURI(commonRequest) {
    const pathMap = commonRequest.pathVariables;
    const queryMap = commonRequest.queryParams;
    var uri = commonRequest.uriTemplate;
    var queryParams = [];
    for (const param in pathMap) {
        uri = uri.replace(`{${param}}`, `${pathMap[param]}`);
    }
    for (const query in queryMap) {
        queryParams = [
            ...queryParams,
            `${query}=${encodeURIComponent(`${queryMap[query]}`)}`
        ];
    }
    const resQuery = queryParams.join("&");
    if (resQuery == "") {
        return `${commonRequest.baseURL}${uri}`;
    }
    return `${commonRequest.baseURL}${uri}?${resQuery}`;
}
const noOpMiddleware = async (x) => x;
