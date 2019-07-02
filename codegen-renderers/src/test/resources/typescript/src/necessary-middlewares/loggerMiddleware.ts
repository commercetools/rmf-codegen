import { Middleware, MiddlewareArg } from "../gen/base/common-types";


export const loggerMiddleware: Middleware = async (arg: MiddlewareArg) => {

  console.log(arg)

  return arg.next(arg);
};
