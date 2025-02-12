package io.vrap.codegen.languages.postman.model

import io.vrap.codegen.languages.extensions.resource
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.apache.commons.text.StringEscapeUtils

class MethodRenderer {
    fun render(method: Method): String {

        val resourceName = method.resource().toResourceName().removePrefix("ByProjectKey")
        val toolName = method.displayName?.value?.split(" ") ?.joinToString("") { word ->
            word.replaceFirstChar { it.uppercase() }
        }?.replaceFirstChar { it.lowercaseChar() } ?: resourceName.replaceFirstChar { it.lowercaseChar() }
        val requestMethod = method.methodName.uppercase()
        val actionType = when (requestMethod) {
            "GET" -> "read"
            "HEAD" -> "read"
            "POST" -> "write"
            "PUT" -> "write"
            "DELETE" -> "delete"
            else -> "read"
        }


        val description = method.description?.description() ?: method.displayName?.value
        val url = PostmanUrl(method.resource(), method) { resource, name -> when (name) {
            "ID" -> resource.resourcePathName.singularize() + "-id"
            "key" -> resource.resourcePathName.singularize() + "-key"
            else -> StringCaseFormat.LOWER_HYPHEN_CASE.apply(name)
        }}
        val res =  """
            | {
            | "name": "${toolName}Tool",
            | "actionType": "${actionType}",
            | "tool": tool(
            | async (args: any) => {
            |   try {
            |   const method: string = "${requestMethod}";
            |   let body = {};
            |   const bodyStr = args.postRequestParams;
            |   if (method === "POST" && bodyStr) {
            |     try {
            |       JSON.parse(bodyStr);
            |     } catch {
            |       return "Invalid body value: "+ body
            |     }
            |     body = { body: bodyStr };
            |   }
            |   
            |   let url = [apiUrl, projectKey, "${url.path(2)}"].join("/");
            |   Object.entries(args.pathVariables || {}).forEach(([key, value]: any) => {
            |              url = url.replace(`{{\$\{key}}}`, value);
            |            })
            |   const headers = { "Content-Type": "application/json", "Authorization": "Bearer " + accessToken };
            |   const response = await fetch(url, {
            |     method,
            |     headers,
            |     ...body,
            |   });
            |   const jsonData = await response.text();
            |   return jsonData;
            |   } catch (error) {
            |   return `Fetch Error:` + (error as Error).message;
            |   }
            | }, {
            |  name: "${toolName}",
            |  description: "${description}",
            |  schema: z.object({
            |           "query": z.object({
            |           <<${url.querySchema()}>>
            |           }).optional(),
            |           "pathVariables": z.object({
            |           <<${url.pathVars(2)}>>
            |           }).optional(),
            |           <<${method.bodySchema()}>>
            |  })
            | })
            |}
        """.trimMargin()

        return res.trimMargin()
//        export const ${toolName}Tool = new DynamicTool({
//            name: "${toolName}",
//            description: "Fetches data from an API and validates the response.",
//            func: async () => {
//            try {
//                const response = await fetch("https://api.example.com/data");
//                const jsonData = await response.json();
//
//                const parsedData = ApiResponseSchema.safeParse(jsonData);
//                if (!parsedData.success) {
//                    return `Validation Error: ${parsedData.error.toString()}`;
//                }
//
//                return parsedData.data;
//            } catch (error) {
//                return `Fetch Error: ${error.message}`;
//            }
//        },
//        });

    }

    fun Method.getExample(): String? {
        val s = this.bodies?.
        getOrNull(0)?.
        type?.
        examples?.
        getOrNull(0)?.
        value
        return StringEscapeUtils.escapeJson(s?.toJson())
    }

    fun Method.rawBody(): String {
        return this.getExample()?.escapeAll()?:""
    }

    fun Method.bodySchema(): String {
        val example = this.getExample()?.escapeAll()?:""

        if (example.isEmpty()) {
            return ""
        }

        return  "\"postRequestParams\": z.string().describe(`String value for the request JSON payload. example value (all fields are optional): ${example}`)"
    }
}
