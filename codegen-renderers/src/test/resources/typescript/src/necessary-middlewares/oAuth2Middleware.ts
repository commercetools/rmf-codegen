import ClientOAuth2 = require("client-oauth2");
import { Middleware, MiddlewareArg } from "./common-types";

export function createOAuth2Middleware(apiCredentials: {
  clientId: string;
  clientSecret: string;
  scopes: string[];
  authorizationUri: string;
  accessTokenUri: string;
}): Middleware {
  return new TokenProvider(apiCredentials).middleware();
}

class TokenProvider {
  private token: ClientOAuth2.Token;
  private clientOauth2: ClientOAuth2;
  private mutex = new Mutex();

  constructor(apiCredentials: ClientOAuth2.Options) {
    this.clientOauth2 = new ClientOAuth2({
      ...apiCredentials
    });
  }

  async getOrCreateToken(): Promise<ClientOAuth2.Token> {
    if (this.token) {
      if (this.token.expired()) {
        this.token = await this.clientOauth2.credentials.getToken();
      }
    } else {
      this.token = await this.clientOauth2.credentials.getToken();
    }

    return this.token;
  }

  middleware(): Middleware {
    return async (arg: MiddlewareArg) => {
      try {
        const token = await this.mutex.dispatch(()=>this.getOrCreateToken())
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
}

export class Mutex {
  private mutex = Promise.resolve();

  lock(): PromiseLike<() => void> {
    let begin: (unlock: () => void) => void = unlock => {};

    this.mutex = this.mutex.then(() => {
      return new Promise(begin);
    });

    return new Promise(res => {
      begin = res;
    });
  }

  async dispatch<T>(fn: (() => T) | (() => PromiseLike<T>)): Promise<T> {
    const unlock = await this.lock();
    try {
      return await Promise.resolve(fn());
    } finally {
      unlock();
    }
  }
}
