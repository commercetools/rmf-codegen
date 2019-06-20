import ClientOAuth2 = require("client-oauth2");
import { Middleware, MiddlewareArg } from "./common-types";

export function createOAuth2Middleware(apiCredentials: {
  clientId: string;
  clientSecret: string;
  scopes: string[];
  authorizationUri: string;
  accessTokenUri: string;
}): Middleware {
  const clientOauth2 = new ClientOAuth2({
    ...apiCredentials
  });
  var token:ClientOAuth2.Token;
  return async (arg: MiddlewareArg) => {
    try {
      if(token){
          if(token.expired()){
            token = await token.refresh()
          }
      }
      else{
        token = await clientOauth2.credentials.getToken();
      }
      
      const { request, ...rest } = arg;
      const headers = {
        ...request.headers,
        Authorization: `Bearer ${token.accessToken}`
      };
      const newRequest = {
        ...request,
        headers
      };

      return arg.next({
        ...rest,
        request: newRequest
      });
    } catch (error) {
      return arg.next({
        error,
        ...arg
      });
    }
  };
}
