"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class CompactClient {
    constructor(middlewares) {
        if (!middlewares || middlewares.length == 0) {
            middlewares = [noOpMiddleware];
        }
        this.middleware = middlewares.reduce(reduceMiddleware);
    }
    async execute(request) {
        const req = {
            ...request,
            url: getURI(request)
        };
        const res = await this.middleware({
            request: req,
            error: null,
            next: noOpMiddleware,
            response: null
        });
        if (res.error) {
            throw res.error;
        }
        return res.response;
    }
}
exports.CompactClient = CompactClient;
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
