import axios = require("axios");
import { Middleware, MiddlewareArg } from "../gen/base/common-types";

const client = axios.default;

export const httpMiddleware: Middleware = async (args: MiddlewareArg) => {
  try {
    const { request, error, next } = args;

    if (error != null) {
      return next(args);
    }

    var axiosConfig: axios.AxiosRequestConfig = {
      method: request.method,
      url: request.uri,
      data: request.body,
      headers: request.headers
    };

    const response = await client.request(axiosConfig);
    return args.next({
      ...args,
      response: {
        body:response.data,
        headers:response.headers,
        statusCode:response.status
      }
    });
  } catch (error) {
    if (error.response && error.response.data) {
      return args.next({
        ...args,
        error: error.response.data
      });
    }
    return args.next({
      ...args,
      error
    });
  }
};
