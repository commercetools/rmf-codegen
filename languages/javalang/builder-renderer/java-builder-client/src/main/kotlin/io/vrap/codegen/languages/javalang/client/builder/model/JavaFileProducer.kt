package io.vrap.codegen.languages.javalang.client.builder.model

import com.google.inject.Inject
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.raml.model.modules.Api

class JavaFileProducer @Inject constructor() : FileProducer {

    @Inject
    @BasePackageName
    lateinit var packagePrefix:String

    @Inject
    lateinit var api:Api

    override fun produceFiles(): List<TemplateFile> = listOf(
            buildGradle()
   
    )

    private fun buildGradle(): TemplateFile {
        return TemplateFile(relativePath = "build.gradle",
                content = """
                        |dependencies {
                        |   compile group: 'junit', name: 'junit', version: '4.12'
                        |   compile 'com.fasterxml.jackson.core:jackson-databind:2.9.8'
                        |   compile 'javax.annotation:javax.annotation-api:1.2'
                        |   compile 'javax.validation:validation-api:2.0.1.Final'
                        |}
                    """.trimMargin()
        )
    }

  
}
