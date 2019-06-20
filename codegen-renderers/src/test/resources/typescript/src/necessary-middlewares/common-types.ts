
type HttpMethod =
  | "GET"
  | "HEAD"
  | "POST"
  | "PUT"
  | "DELETE"
  | "CONNECT"
  | "OPTIONS"
  | "TRACE";

export interface CommonRequest<T> {
  baseURL: string;
  url?: string,
  headers?: { [key: string]: string };
  method: HttpMethod;
  uriTemplate: string;
  pathVariables?: { [key: string]: string | number | boolean };
  queryParams?: { [key: string]: string | number | boolean };
  payload?: T;
  dataType?: 'TEXT'|'BINARY';
}

export type MiddlewareArg = {
  request: CommonRequest<any>;
  response: any;
  error: Error;
  next: Middleware;
};

export type Middleware = (arg: MiddlewareArg) => Promise<MiddlewareArg>;

