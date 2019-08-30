"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyRequestBuilder_1 = require("./ByProjectKeyRequestBuilder");
class ApiRoot {
    constructor(args) {
        this.args = args;
    }
    withProjectKeyValue(childPathArgs) {
        return new ByProjectKeyRequestBuilder_1.ByProjectKeyRequestBuilder({
            pathArgs: {
                ...childPathArgs
            },
            middlewares: this.args.middlewares
        });
    }
}
exports.ApiRoot = ApiRoot;
