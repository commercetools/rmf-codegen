import { ApiRoot } from "../gen/client/ApiRoot";
import { createFromCtpMiddlwares } from "../necessary-middlewares/ctp_middlware";



  export function createCtpClient(ctpMiddlwares: any[]){
      return new ApiRoot({
          middlewares:[createFromCtpMiddlwares(ctpMiddlwares)]
      })
  }