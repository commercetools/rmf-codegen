"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ctp_client_1 = require("./ctp-client/ctp-client");
const ctpClient = null;
const apiRoot = ctp_client_1.createApiBuilderFromCtpClient(ctpClient);
apiRoot.withProjectKeyValue({
    projectKey: 'some_key'
}).apiClients()
    .get()
    .execute()
    .then(res => res.body.count);
