"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.loggerMiddleware = async (arg) => {
    console.log(arg);
    return arg.next(arg);
};
