package io.vrap.rmf.codegen.plugin


import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.client.SpringClientModule
import io.vrap.codegen.languages.javalang.client.builder.module.JavaCompleteModule
import io.vrap.codegen.languages.javalang.dsl.GroovyDslModule
import io.vrap.codegen.languages.javalang.model.JavaModelModule
import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.codegen.languages.typescript.server.TypescriptServerModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.ApiProvider
import io.vrap.rmf.codegen.di.GeneratorComponent
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.types.VrapObjectType
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths

open class RamlCodeGeneratorTask : DefaultTask() {


    @TaskAction
    internal fun generateClasses() {

        val targets = project.extensions.getByName("RamlGenerator") as NamedDomainObjectContainer<RamlGenerator>

        targets.forEach { generator ->
            run {
                val file = generator.uri
                if (file == null) {
                    logger.error("uri in $generator must be non null")
                    return
                }
                val apiProvider = ApiProvider(file.toPath())
                generator.targets?.all { target: Target -> generateFiles(apiProvider, target) }
            }
        }
    }

    fun generateFiles(apiProvider: ApiProvider, target: Target): Unit {


        val generatorConfig = CodeGeneratorConfig(
            basePackageName = target.base_package,
            modelPackage = target.models_package,
            clientPackage = target.client_package,
            outputFolder = target.path?.toPath() ?: Paths.get("gensrc/${target.name}"),
            customTypeMapping = target.customTypeMapping.
                    entries.associate { kotlin.Pair(it.key, VrapObjectType(it.value.substringBeforeLast("."), it.value.substringAfterLast("."))) }
        )

        val generatorComponent: GeneratorComponent = when (target.target) {

            TargetType.JAVA_MODEL -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, JavaModelModule)
            }
            TargetType.JAVA_MODEL_WITH_INTERFACES -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, JavaCompleteModule)
            }
            TargetType.JAVA_SPRING_CLIENT -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, SpringClientModule)
            }
            TargetType.GROOVY_DSL -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                GeneratorComponent(generatorModule, GroovyDslModule)
            }
            TargetType.TYPESCRIPT_MODEL -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                GeneratorComponent(generatorModule, TypescriptModelModule)
            }
            TargetType.TYPESCRIPT_CLIENT -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                GeneratorComponent(generatorModule, TypescriptClientModule)
            }
            TargetType.TYPESCRIPT_HAPI_SERVER -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                GeneratorComponent(generatorModule, TypescriptServerModule)
            }
            TargetType.JOI_VALIDATOR -> {
                val generatorModule = GeneratorModule(apiProvider, generatorConfig, JoiBaseTypes)
                GeneratorComponent(generatorModule, JoiModule)
            }
            else -> throw IllegalArgumentException("unsupported target value '${target.target}', allowed values is one of ${TargetType.values().toList()}")
        }
        logger.warn("generating files for target $target, ${generatorConfig.outputFolder}")
        generatorComponent.generateFiles()

    }

}
