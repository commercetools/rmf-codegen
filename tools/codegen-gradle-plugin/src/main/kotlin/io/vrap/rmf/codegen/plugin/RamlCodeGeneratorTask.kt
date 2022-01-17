package io.vrap.rmf.codegen.plugin


import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.javalang.client.builder.module.JavaClientBuilderModule
import io.vrap.codegen.languages.javalang.client.builder.module.JavaCompleteModule
import io.vrap.codegen.languages.javalang.client.builder.module.JavaInterfaceModelModule
import io.vrap.codegen.languages.javalang.dsl.GroovyDslModule
import io.vrap.codegen.languages.javalang.model.JavaModelModule
import io.vrap.codegen.languages.typescript.client.TypescriptClientModule
import io.vrap.codegen.languages.typescript.joi.JoiBaseTypes
import io.vrap.codegen.languages.typescript.joi.JoiModule
import io.vrap.codegen.languages.typescript.model.TypeScriptBaseTypes
import io.vrap.codegen.languages.typescript.model.TypescriptModelModule
import io.vrap.codegen.languages.typescript.server.TypescriptServerModule
import io.vrap.rmf.codegen.CodeGeneratorConfig
import io.vrap.rmf.codegen.di.RamlApiProvider
import io.vrap.rmf.codegen.di.RamlGeneratorComponent
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.types.VrapObjectType
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths

open class RamlCodeGeneratorTask : DefaultTask() {


    @Suppress("UNCHECKED_CAST")
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
                val apiProvider = RamlApiProvider(file.toPath())
                generator.targets?.all { target: Target -> generateFiles(apiProvider, target) }
            }
        }
    }

    fun generateFiles(apiProvider: RamlApiProvider, target: Target): Unit {


        val generatorConfig = CodeGeneratorConfig(
            sharedPackage = target.shared_package,
            basePackageName = target.base_package,
            modelPackage = target.models_package,
            clientPackage = target.client_package,
            outputFolder = target.path?.toPath() ?: Paths.get("gensrc/${target.name}"),
            customTypeMapping = target.customTypeMapping.
                    entries.associate { kotlin.Pair(it.key, VrapObjectType(it.value.substringBeforeLast("."), it.value.substringAfterLast("."))) },
            writeGitHash = target.writeGitHash
        )

        val generatorComponent: RamlGeneratorComponent = when (target.target) {

            TargetType.JAVA_MODEL -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                RamlGeneratorComponent(generatorModule, JavaModelModule)
            }
            TargetType.JAVA_MODEL_WITH_INTERFACES -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                RamlGeneratorComponent(generatorModule, JavaInterfaceModelModule)
            }
            TargetType.JAVA_API_BUILDER -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                RamlGeneratorComponent(generatorModule, JavaClientBuilderModule)
            }
            TargetType.GROOVY_DSL -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                RamlGeneratorComponent(generatorModule, GroovyDslModule)
            }
            TargetType.JAVA_COMPLETE -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JavaBaseTypes)
                RamlGeneratorComponent(generatorModule, JavaCompleteModule)
            }
            TargetType.TYPESCRIPT_MODEL -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                RamlGeneratorComponent(generatorModule, TypescriptModelModule)
            }
            TargetType.TYPESCRIPT_CLIENT -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                RamlGeneratorComponent(generatorModule, TypescriptClientModule)
            }
            TargetType.TYPESCRIPT_HAPI_SERVER -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                RamlGeneratorComponent(generatorModule, TypescriptServerModule)
            }
            TargetType.JOI_VALIDATOR -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, JoiBaseTypes)
                RamlGeneratorComponent(generatorModule, JoiModule)
            }
            TargetType.TS_CLIENT_COMPLETE -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                RamlGeneratorComponent(generatorModule, TypescriptModelModule, TypescriptClientModule)
            }
            TargetType.TS_SERVER_COMPLETE -> {
                val generatorModule = RamlGeneratorModule(apiProvider, generatorConfig, TypeScriptBaseTypes)
                RamlGeneratorComponent(generatorModule, TypescriptModelModule, JoiModule, TypescriptServerModule)
            }

            else -> throw IllegalArgumentException("unsupported target value '${target.target}', allowed values is one of ${TargetType.values().toList()}")
        }
        logger.warn("generating files for target $target, ${generatorConfig.outputFolder}")
        generatorComponent.generateFiles()

    }

}
