package io.vrap.codegen.languages.java.file.producers

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.ResourceContainer

class JavaApiRootFileProducer @Inject constructor(@Named(VrapConstants.CLIENT_PACKAGE_NAME) val clientPackage: String, val api: Api, val vrapTypeProvider: VrapTypeProvider) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(generateApiRoot(api))
    }
    
    private fun generateApiRoot(api: Api) : TemplateFile {
        
        val content =  """
            |package $clientPackage;
            |
            |public class ApiRoot {
            |   
            |   <${api.subResources()}>
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
            |public static ${it.toResourceName()}RequestBuilder ${it.getMethodName()}($args) {
            |   return new ${it.toResourceName()}RequestBuilder($constructorArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }
}