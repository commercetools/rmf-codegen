"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ApiRoot_1 = require("../gen/client/ApiRoot");
const ctp_middlware_1 = require("../necessary-middlewares/ctp_middlware");
function createApiBuilderFromCtpClient(ctpClient) {
    return new ApiRoot_1.ApiRoot({
        middlewares: [ctp_middlware_1.middlewareFromCtpClient(ctpClient)]
    });
}
exports.createApiBuilderFromCtpClient = createApiBuilderFromCtpClient;
