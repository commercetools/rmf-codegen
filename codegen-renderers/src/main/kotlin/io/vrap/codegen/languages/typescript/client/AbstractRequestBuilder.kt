package io.vrap.codegen.languages.typescript.client

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.java.extensions.returnType
import io.vrap.codegen.languages.php.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.php.extensions.resource
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.codegen.languages.typescript.client.files_producers.middleware
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.tsMediaType
import io.vrap.codegen.languages.typescript.tsRemoveRegexp
import io.vrap.codegen.languages.typescript.tsRequestModuleName
import io.vrap.codegen.languages.typescript.tsRequestName
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.resources.ResourceContainer
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractRequestBuilder constructor(
        val api: Api,
        override val vrapTypeProvider: VrapTypeProvider
) : EObjectTypeExtensions {

    protected fun ResourceContainer.subResources(): String {
        return this.resources
                .map {

                    val args = if (it.relativeUri.variables.isNullOrEmpty()) "" else """|
                        |   childPathArgs: {
                        |       <${it.relativeUri.variables.map { "$it: string" }.joinToString(separator = "\n")}>
                        |   }
                        |
                    """.trimMargin()

                    """|
                    |${it.getMethodName()}($args): ${it.toRequestBuilderName()} {
                    |   return new ${it.toRequestBuilderName()}(
                    |         {
                    |            pathArgs: {
                    |               <${if(hasPathArgs()) "...this.args.pathArgs," else ""}>
                    |               <${if (it.relativeUri.variables.isNotEmpty()) "...childPathArgs" else ""}>
                    |            },
                    |            middlewares: this.args.middlewares
                    |         }
                    |   )
                    |}
                    |
                 """.trimMargin()
                }.joinToString(separator = "")
    }

    protected fun Resource.toRequestBuilderName(): String = "${this.toResourceName()}RequestBuilder"


    abstract fun hasPathArgs(): Boolean
}



