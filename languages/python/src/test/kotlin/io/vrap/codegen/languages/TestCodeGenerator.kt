package io.vrap.codegen.languages

import io.vrap.codegen.languages.python.client.PythonClientModule
import io.vrap.codegen.languages.python.model.PythonBaseTypes
import io.vrap.codegen.languages.python.model.PythonModelModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths

class TestPythonCodeGenerator {

    companion object {
        private val userProvidedPath = System.getenv("TEST_RAML_FILE")
        private val apiPath: Path = Paths.get(if (userProvidedPath == null) "../../api-spec/api.raml" else userProvidedPath)
        val apiProvider: ApiProvider = ApiProvider(apiPath)
        val generatorConfig = CodeGeneratorConfig(basePackageName = "")
    }

    @Test
    fun generatPyModels() {
        val generatorModule = GeneratorModule(apiProvider, generatorConfig, PythonBaseTypes)
        val generatorComponent = GeneratorComponent(generatorModule, PythonModelModule, PythonClientModule)
        generatorComponent.generateFiles()
    }

    private fun cleanGenTestFolder() {
        cleanFolder("build/gensrc")
    }

    private fun cleanFolder(path: String) {
        Paths.get(path).toFile().deleteRecursively()
    }
}
