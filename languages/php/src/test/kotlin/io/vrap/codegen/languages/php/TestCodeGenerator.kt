package io.vrap.codegen.languages.php

import io.vrap.codegen.languages.php.base.PhpBaseModule
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths


class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc" else userProvidedOutputPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
    }

    @Test
    fun generateApiSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/api",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-api")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generateImportSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/import",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-import")
        )

        val generatorModule = GeneratorModule(ApiProvider(Paths.get("../../api-spec/api.raml")), generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()
    }

    @Test
    fun generatePHPBase() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-base")
        )

        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpBaseModule())
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder(outputFolder.toString())
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
