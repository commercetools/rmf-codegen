import { Middleware, MiddlewareArg } from "./common-types";

export const  loggerMiddleware: Middleware = async (arg:MiddlewareArg) => {
    console.log(`called url ${arg.request.url}`)
    return arg.next(arg)
}