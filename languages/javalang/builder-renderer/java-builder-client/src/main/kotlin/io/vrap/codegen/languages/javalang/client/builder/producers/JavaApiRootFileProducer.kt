package io.vrap.codegen.languages.javalang.client.builder.producers

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.toJavaPackage
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.ResourceContainer

class JavaApiRootFileProducer @Inject constructor(@ClientPackageName val clientPackage: String, val api: Api) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(generateApiRoot(api))
    }
    
    private fun generateApiRoot(api: Api) : TemplateFile {
        
        val content =  """
            |package ${clientPackage.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.ApiHttpClient;
            |import io.vrap.rmf.base.client.middlewares.Middleware;
            |
            |import java.util.List;
            |import java.util.Arrays;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |<${JavaSubTemplates.generatedAnnotation}>
            |public class ApiRoot {
            |   
            |   private final ApiHttpClient apiHttpClient;
            |      
            |   private ApiRoot(final Middleware... middlewares) {
            |      this.apiHttpClient = new ApiHttpClient(Arrays.asList(middlewares));
            |   }
            |      
            |   public static ApiRoot fromMiddlewares(final Middleware... middlewares) {
            |       return new ApiRoot(middlewares);
            |   }
            |           
            |   <${api.subResources()}>
            |   
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "$clientPackage.ApiRoot".replace(".", "/") + ".java",
                content = content
        )
    }
    
    private fun ResourceContainer.subResources() : String {
        
        return this.resources.map {
            val args = if (it.relativeUri.variables.isNullOrEmpty()){
                ""
            }else {
                it.relativeUri.variables.map { "String $it" }.joinToString(separator = " ,")
            }
            
            val constructorArgs = if (it.relativeUri.variables.isNullOrEmpty()){
                ""
            }else {
                it.relativeUri.variables.joinToString(separator = " ,")
            }
            """
            |public ${it.toResourceName()}RequestBuilder ${it.getMethodName()}($args) {
            |   return new ${it.toResourceName()}RequestBuilder(this.apiHttpClient, $constructorArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }
}