# Popsicle Transport HTTP

[![NPM version][npm-image]][npm-url]
[![NPM downloads][downloads-image]][downloads-url]
[![Build status][travis-image]][travis-url]
[![Test coverage][coveralls-image]][coveralls-url]

> Popsicle transport for sending requests over HTTP1 and HTTP2.

## Installation

```
npm install popsicle-transport-http --save
```

## Usage

```js
import { transport } from "popsicle-transport-http";

const req = new Request("/");
const res = await transport()(req, done);
```

### Transport Options

The `transport` function sends the Servie `Request` to a remote server.

- `keepAlive?: number` Duration to keep connection alive for re-use (default: `5000`)
- `negotiateHttpVersion?: NegotiateHttpVersion` Configure HTTP version negotiation (default: `HTTP2_FOR_HTTPS`)
- `servername?: string` Override remote server name for TLS
- `rejectUnauthorized?: boolean` Rejects unauthorized TLS connections
- `ca?: string | Buffer | Array<string | Buffer>` Set TLS CA
- `cert?: string | Buffer` Set TLS certificate
- `key?: string | Buffer` Set TLS key
- `secureContext?: SecureContext` Set TLS secure context
- `secureProtocol?: string` Set TLS secure protocol

## TypeScript

This project is written using [TypeScript](https://github.com/Microsoft/TypeScript) and publishes the definitions directly to NPM.

## License

MIT

[npm-image]: https://img.shields.io/npm/v/popsicle-transport-http.svg?style=flat
[npm-url]: https://npmjs.org/package/popsicle-transport-http
[downloads-image]: https://img.shields.io/npm/dm/popsicle-transport-http.svg?style=flat
[downloads-url]: https://npmjs.org/package/popsicle-transport-http
[travis-image]: https://img.shields.io/travis/serviejs/popsicle-transport-http.svg?style=flat
[travis-url]: https://travis-ci.org/serviejs/popsicle-transport-http
[coveralls-image]: https://img.shields.io/coveralls/serviejs/popsicle-transport-http.svg?style=flat
[coveralls-url]: https://coveralls.io/r/serviejs/popsicle-transport-http?branch=master
