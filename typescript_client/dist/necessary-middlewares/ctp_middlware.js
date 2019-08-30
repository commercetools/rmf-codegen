"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function middlewareFromCtpClient(client) {
    return async (middlewareArg) => handleRequest(client, middlewareArg);
}
exports.middlewareFromCtpClient = middlewareFromCtpClient;
async function handleRequest(client, middlewareArg) {
    const { request } = middlewareArg;
    const modifiedRequest = {
        ...request,
        uri: removeBaseUrl(request.uri)
    };
    try {
        const result = await client.execute(modifiedRequest);
        const { uri, error, ...response } = result;
        return middlewareArg.next({
            ...middlewareArg,
            response,
            error
        });
    }
    catch (error) {
        return middlewareArg.next({
            ...middlewareArg,
            error
        });
    }
}
function removeBaseUrl(url) {
    var baseUrlPattern = /^https?:\/\/[a-z\:0-9.]+/;
    var result = "";
    var match = baseUrlPattern.exec(url);
    if (match != null) {
        result = match[0];
    }
    if (result.length > 0) {
        url = url.replace(result, "");
    }
    return url;
}
