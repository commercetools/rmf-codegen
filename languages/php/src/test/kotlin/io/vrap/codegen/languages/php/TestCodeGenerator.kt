package io.vrap.codegen.languages.php

import io.vrap.codegen.languages.php.base.PhpBaseModule
import io.vrap.codegen.languages.php.model.PhpModelModule
import io.vrap.codegen.languages.php.test.PhpTestModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths

class TestCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val importerUserProvidedPath = System.getenv("IMPORTER_RAML_FILE")
        private val userProvidedOutputPath = System.getenv("OUTPUT_FOLDER")
        private val apiPath : Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        private val importerPath : Path = Paths.get(if (importerUserProvidedPath == null) "../../api-spec/api.raml" else importerUserProvidedPath)
        private val outputFolder : Path = Paths.get(if (userProvidedOutputPath == null) "build/gensrc" else userProvidedOutputPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val importProvider: ApiProvider = ApiProvider(importerPath)
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

        runPHPUnitTests(outputFolder.parent)

        val generatorConfigTests = CodeGeneratorConfig(
                basePackageName = "commercetools/api",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-api-tests")
        )

        val generatorModuleTests = GeneratorModule(apiProvider, generatorConfigTests, PhpBaseTypes)
        val generatorComponentTests = GeneratorComponent(generatorModuleTests, PhpTestModule())
        generatorComponentTests.generateFiles()
    }

    @Test
    fun generateImportSdk() {
        val generatorConfig = CodeGeneratorConfig(
                basePackageName = "commercetools/import",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-import")
        )

        val generatorModule = GeneratorModule(importProvider, generatorConfig, PhpBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PhpModelModule())
        generatorComponent.generateFiles()

        runPHPUnitTests(outputFolder.parent)

        val generatorConfigTests = CodeGeneratorConfig(
                basePackageName = "commercetools/import",
                sharedPackage = "commercetools",
                outputFolder = Paths.get("${outputFolder}/commercetools-import-tests")
        )

        val generatorModuleTests = GeneratorModule(importProvider, generatorConfigTests, PhpBaseTypes)
        val generatorComponentTests = GeneratorComponent(generatorModuleTests, PhpTestModule())
        generatorComponentTests.generateFiles()
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

    private fun runPHPUnitTests(workingDir: Path) {
        val process = ProcessBuilder("sh", "-c", "vendor/bin/phpunit --testsuite=unit")
                .directory(workingDir.toFile())
                .start()

        val output: String = IOUtils.toString(process.inputStream, StandardCharsets.UTF_8)
        Assert.assertEquals("Unit tests didn't ran successful!\n$output",0, process.waitFor())
    }
}
