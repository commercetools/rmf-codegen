import { Middleware, MiddlewareArg } from "./common-types";
import axios = require("axios");
import fs = require("fs");

const client = axios.default;

export const httpMiddleware: Middleware = async (args: MiddlewareArg) => {
  try {
    const { request, error, next } = args;

    if (error != null) {
      return next(args);
    }

    var axiosConfig: axios.AxiosRequestConfig;
    if (request.dataType == "BINARY") {
      const data = fs.readFileSync(request.payload.filePath);
      axiosConfig = {
        method: request.method,
        url: request.url,
        data,
        headers: request.headers
      };
    } else {
      axiosConfig = {
        method: request.method,
        url: request.url,
        data: request.payload,
        headers: request.headers
      };
    }

    const response = await client.request(axiosConfig);
    return args.next({
      ...args,
      response: response.data
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
