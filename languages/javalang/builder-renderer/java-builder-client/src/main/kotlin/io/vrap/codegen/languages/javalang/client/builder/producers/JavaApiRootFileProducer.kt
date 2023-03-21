package io.vrap.codegen.languages.javalang.client.builder.producers

import io.vrap.codegen.languages.extensions.getMethodName
import io.vrap.codegen.languages.extensions.toResourceName
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.toJavaPackage
import io.vrap.rmf.codegen.di.ClientPackageName
import io.vrap.rmf.codegen.di.RamlApi
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.ResourceContainer
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.StringInstance

class JavaApiRootFileProducer constructor(@ClientPackageName val clientPackage: String, @RamlApi val api: Api) : FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(generateApiRoot(api))
    }

    private fun generateApiRoot(api: Api) : TemplateFile {
        val implements = arrayListOf("Closeable")
            .plus(
                when (val ex = api.getAnnotation("java-implements") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            )
            .filterNotNull()

        val content =  """
            |package ${clientPackage.toJavaPackage()};
            |
            |import io.vrap.rmf.base.client.ApiHttpClient;
            |import io.vrap.rmf.base.client.SerializerOnlyApiHttpClient;
            |
            |import java.io.Closeable;
            |import java.io.IOException;
            |import java.util.List;
            |import java.util.Arrays;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |/**
            | * Entrypoint for building requests against the API
            | */
            |<${JavaSubTemplates.generatedAnnotation}>
            |public class ApiRoot ${if (implements.isNotEmpty()) "implements ${implements.joinToString(", ")}" else ""} {
            |
            |    private final ApiHttpClient apiHttpClient;
            |
            |    private ApiRoot(final ApiHttpClient apiHttpClient) {
            |       this.apiHttpClient = apiHttpClient;
            |    }
            |
            |    public static ApiRoot of() {
            |        return new ApiRoot(SerializerOnlyApiHttpClient.of());
            |    }
            |
            |    public static ApiRoot fromClient(final ApiHttpClient apiHttpClient) {
            |        return new ApiRoot(apiHttpClient);
            |    }
            |
            |    <${api.subResources()}>
            |
            |    @Override
            |    public void close() {
            |        if(apiHttpClient == null) {
            |            return;
            |        }
            |        try {
            |            apiHttpClient.close();
            |        } catch (final Throwable ignored) { }
            |    }
            |    
            |    <${api.getAnnotation("java-mixin")?.value?.value?.let { (it as String).escapeAll()} ?: ""}>
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
            |    return new ${it.toResourceName()}RequestBuilder(this.apiHttpClient, $constructorArgs);
            |}
        """.trimMargin()
        }.joinToString(separator = "\n")
    }
}
