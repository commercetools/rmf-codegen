package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapType
import java.nio.file.Path

interface GeneratorModule {
    val apiProvider: ApiProvider
    val generatorConfig: io.vrap.rmf.codegen.CodeGeneratorConfig
    val languageBaseTypes: LanguageBaseTypes
    val defaultPackage: String

    @DefaultPackage
    fun defaultPackage(): String = defaultPackage

    @OutputFolder
    fun outpuFolder(): Path = generatorConfig.outputFolder

    @GenDataSink
    fun dataSink(): DataSink = FileDataSink(outpuFolder())

    @CustomTypeMapping
    fun customTypeMapping(): Map<String, VrapType> = generatorConfig.customTypeMapping

    @ApiGitHash
    fun provideGitHash(): String = if (generatorConfig.writeGitHash) apiProvider.gitHash else ""
}
