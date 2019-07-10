"use strict";
// import { ApiRoot } from "./gen/client/ApiRoot";
// import { loggerMiddleware } from "./necessary-middlewares/loggerMiddleware";
// import { createOAuth2Middleware } from "./necessary-middlewares/oAuth2Middleware";
// import { httpMiddleware } from "./necessary-middlewares/httpClientMiddleware";
// const oAuthMiddlware = createOAuth2Middleware({
//   clientId: "kOgSGYIMCS4Wai4Ko-lEwp4a",
//   clientSecret: "DsopewhYM-VUpHBgbcXLS8Na8NdjvPUT",
//   scopes: ["manage_project:achraf-61"],
//   accessTokenUri: "https://auth.sphere.io/oauth/token",
//   authorizationUri: "https://auth.sphere.io/"
// });
// const apiRoot = new ApiRoot({
//   middlewares: [oAuthMiddlware, httpMiddleware]
// });
// apiRoot
//   .withProjectKeyValue({
//     projectKey:'Hello'
//   })
//   .get()
//   .execute()
//   .then(console.log)
//   .catch(console.error);
