"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ByProjectKeyMeEmailConfirmRequestBuilder_1 = require("./../confirm/ByProjectKeyMeEmailConfirmRequestBuilder");
class ByProjectKeyMeEmailRequestBuilder {
    constructor(args) {
        this.args = args;
    }
    confirm() {
        return new ByProjectKeyMeEmailConfirmRequestBuilder_1.ByProjectKeyMeEmailConfirmRequestBuilder({
            pathArgs: {
                ...this.args.pathArgs,
            },
            middlewares: this.args.middlewares
        });
    }
}
exports.ByProjectKeyMeEmailRequestBuilder = ByProjectKeyMeEmailRequestBuilder;
