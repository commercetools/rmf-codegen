"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function middlewareFromCtpClient(client) {
    return async (midlllewareArg) => handleRequest(client, midlllewareArg);
}
exports.middlewareFromCtpClient = middlewareFromCtpClient;
async function handleRequest(client, middlwareArg) {
    const { request } = middlwareArg;
    const modifiedRequest = {
        ...request,
        uri: removeBaseUrl(request.uri)
    };
    try {
        const result = await client.execute(modifiedRequest);
        const { uri, error, ...response } = result;
        return middlwareArg.next({
            ...middlwareArg,
            response,
            error
        });
    }
    catch (error) {
        return middlwareArg.next({
            ...middlwareArg,
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
