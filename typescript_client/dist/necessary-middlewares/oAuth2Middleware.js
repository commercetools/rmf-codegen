"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ClientOAuth2 = require("client-oauth2");
function createOAuth2Middleware(apiCredentials) {
    return new TokenProvider(apiCredentials).middleware();
}
exports.createOAuth2Middleware = createOAuth2Middleware;
class TokenProvider {
    constructor(apiCredentials) {
        this.mutex = new Mutex();
        this.clientOauth2 = new ClientOAuth2({
            ...apiCredentials
        });
    }
    async getOrCreateToken() {
        if (this.token) {
            if (this.token.expired()) {
                this.token = await this.clientOauth2.credentials.getToken();
            }
        }
        else {
            this.token = await this.clientOauth2.credentials.getToken();
        }
        return this.token;
    }
    middleware() {
        return async (arg) => {
            try {
                const token = await this.mutex.dispatch(() => this.getOrCreateToken());
                const { request, ...rest } = arg;
                const headers = {
                    Authorization: `Bearer ${token.accessToken}`,
                    ...request.headers,
                };
                const newRequest = {
                    ...request,
                    headers
                };
                return arg.next({
                    ...rest,
                    request: newRequest
                });
            }
            catch (error) {
                console.log({
                    error,
                    ...arg
                });
                console.log(error);
                return arg.next({
                    error,
                    ...arg
                });
            }
        };
    }
}
class Mutex {
    constructor() {
        this.mutex = Promise.resolve();
    }
    lock() {
        let begin = unlock => { };
        this.mutex = this.mutex.then(() => {
            return new Promise(begin);
        });
        return new Promise(res => {
            begin = res;
        });
    }
    async dispatch(fn) {
        const unlock = await this.lock();
        try {
            return await Promise.resolve(fn());
        }
        finally {
            unlock();
        }
    }
}
exports.Mutex = Mutex;
