package io.vrap.codegen.languages.javalang


import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.java.base.PlantUmlBaseTypes
import io.vrap.codegen.languages.java.base.fromDefaultJavaType
import io.vrap.codegen.languages.java.base.fromJavaType
import io.vrap.codegen.languages.javalang.dsl.GroovyDslModule
import io.vrap.codegen.languages.javalang.model.JavaModelModule
import io.vrap.codegen.languages.javalang.plantuml.PlantUmlModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.Boolean
import java.lang.Double
import java.lang.Long
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime


class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: RamlApiProvider = RamlApiProvider(apiPath)
    }


    @Test
    fun generateJavaModels() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com/commercetools/importer")
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, JavaModelModule)
        generatorComponent.generateFiles()
    }


    @Test
    fun generateGroovyDsl() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "com/commercetools/importer")
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, GroovyDslModule)
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePlantUmlDiagram() {
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
        val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, PlantUmlBaseTypes)
        val generatorComponent = RamlGeneratorComponent(generatorModule, PlantUmlModule)
        generatorComponent.generateFiles()
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
