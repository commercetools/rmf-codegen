import { createApiBuilderFromCtpClient } from "./ctp-client/ctp-client";
import { ApiRoot } from "./gen/client/ApiRoot";

const ctpClient = null;

const apiRoot: ApiRoot = createApiBuilderFromCtpClient(ctpClient);

apiRoot.withProjectKeyValue({
  projectKey:'some_key'
}).apiClients()
.get()
.execute()
.then(res => res.body.count)