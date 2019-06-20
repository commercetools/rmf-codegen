import { Middleware } from "./gen/base/common-types";
import { createOAuth2Middleware } from "./necessary-middlewares/oAuth2Middleware";
import { ApiRoot } from "./gen/ct/client/ApiRoot";
import { httpMiddleware } from "./necessary-middlewares/httpClientMiddleware";
import { loggerMiddleware } from "./necessary-middlewares/loggerMiddleware";
import fs = require("fs");
import { version } from "punycode";
const apiRoot = new ApiRoot({
  middlewares: [
    createOAuth2Middleware({
      clientId: "",
      clientSecret: "",
      scopes: [],
      authorizationUri: "https://auth.sphere.io",
      accessTokenUri: "https://auth.sphere.io/oauth/token"
    }),
    httpMiddleware,
    loggerMiddleware
  ]
});

apiRoot
  .withProjectKeyValue({
    projectKey: "achraf-61"
  })
  .products()
  .withId({
    ID: "5e4906fc-331c-4611-bc7e-ff85b0a92586"
  })
  .images()
  .post({
    headers:{
      "Content-Type":"image/jpeg"
    },
    payload: {
      filePath:'/Users/abeniasaad/IdeaProjects/rmf-codegen/typescript_client/src/necessary-middlewares/VswGeFK.jpg'
    }
  })
  .execute()
  .then(res =>
    res.masterData.staged.masterVariant.images
  )
  .then(console.log)
  .catch(console.log);
