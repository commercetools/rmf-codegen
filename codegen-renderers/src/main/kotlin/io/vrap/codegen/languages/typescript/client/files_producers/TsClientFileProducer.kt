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

    const res = await middleware({
      request: this.commonRequest,
      error: null,
      next: async arg => arg,
      response: null
    });
    if(res.error){
      throw res.error
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


"""


        return TemplateFile(content = content,relativePath = "base/requests-utils.ts")

    }
}