package io.vrap.codegen.languages.typescript.client.files_producers

import com.google.inject.Inject
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.raml.model.modules.Api

class TsClientFileProducer @Inject constructor(val api:Api): FileProducer{


    override fun produceFiles(): List<TemplateFile> {
        return listOf(produceRequestTemplate())
    }

    fun produceRequestTemplate(): TemplateFile{
        val content = """
import { Middleware, MiddlewareArg, CommonRequest } from "./common-types";

export class ApiRequest<I, O, T extends CommonRequest<I>> {
  constructor(
    private readonly commonRequest: T,
    private readonly middlewares: Middleware[]
  ) {}

  async execute(): Promise<O> {
    const middleware: Middleware = this.middlewares.reduce(reduceMiddleware);

    const req = {
      ...this.commonRequest,
      url: getURI(this.commonRequest)
    };

    const res = await middleware({
      request: req,
      error: null,
      next: async arg => arg,
      response: null
    });

    if (res.error) {
      throw res.error;
    }
    return res.response;
  }
}

export function reduceMiddleware(op1: Middleware, op2: Middleware): Middleware {
  return async (arg: MiddlewareArg) => {
    const { next, ...rest } = arg;
    const intermediateOp: Middleware = (tmpArg: MiddlewareArg) => {
      const { next, ...rest } = tmpArg;
      return op2({ ...rest, next: arg.next });
    };

    return op1({
      ...rest,
      next: intermediateOp
    });
  };
}

function getURI(commonRequest: CommonRequest<any>): string {
  const pathMap = commonRequest.pathVariables;
  const queryMap = commonRequest.queryParams;
  var uri: String = commonRequest.uriTemplate;
  var queryParams = [];
  for (const param in pathMap) {
    uri = uri.replace(`{${'$'}{param}}`, `${'$'}{pathMap[param]}`);
  }
  for (const query in queryMap) {
    queryParams = [
      ...queryParams,
      `${'$'}{query}=${'$'}{encodeURIComponent(`${'$'}{queryMap[query]}`)}`
    ];
  }
  const resQuery = queryParams.join("&");
  if (resQuery == "") {
    return `${'$'}{commonRequest.baseURL}${'$'}{uri}`;
  }
  return `${'$'}{commonRequest.baseURL}${'$'}{uri}?${'$'}{resQuery}`;
}

"""


        return TemplateFile(content = content,relativePath = "base/requests-utils.ts")

    }
}