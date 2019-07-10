"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyCustomersEmailConfirmRequestBuilder_1 = require("./../confirm/ByProjectKeyCustomersEmailConfirmRequestBuilder");
class ByProjectKeyCustomersEmailRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    confirm() {
        return new ByProjectKeyCustomersEmailConfirmRequestBuilder_1.ByProjectKeyCustomersEmailConfirmRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
}
exports.ByProjectKeyCustomersEmailRequestBuilder = ByProjectKeyCustomersEmailRequestBuilder;
