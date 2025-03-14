import { createAuthMiddlewareForClientCredentialsFlow } from "@commercetools/sdk-middleware-auth"
import { createHttpMiddleware } from "@commercetools/sdk-middleware-http"
import { createClient } from "@commercetools/ts-client"

import fetch from "node-fetch"
import { createApiBuilderFromCtpClient } from "./ctp/ctp-client"
import { ApiRoot } from "./generated/client/api-root"

const projectKey = "achraf-61"

const authMiddleware = createAuthMiddlewareForClientCredentialsFlow({
  host: "https://auth.sphere.io",
  projectKey,
  credentials: {
    clientId: "G2pnjGEW1R5vu3c9l0sGJ_Rj",
    clientSecret: "kDQ4pZpkMvOY3XlH-HkFxljEKFs6IcN5",

  },
  scopes: [`manage_project:${projectKey}`],
  httpClient: fetch
})

const httpMiddleware = createHttpMiddleware({
  host: "https://api.sphere.io",
  httpClient: fetch
})

const ctpClient = createClient({
  middlewares: [authMiddleware, httpMiddleware]
})

const apiRoot: ApiRoot = createApiBuilderFromCtpClient(ctpClient)

apiRoot
  .withProjectKey({
    projectKey
  })
  .products()
  .withId({
    ID:'b14d56a4-053c-4dc0-90b0-92797798e571'
  })
  .get()
  .execute()
  .then(res => res.body.masterData.current)
  .then(console.log)
  .catch(console.log)
