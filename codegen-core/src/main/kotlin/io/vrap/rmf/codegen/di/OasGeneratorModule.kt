package io.vrap.rmf.codegen.di

import io.vrap.rmf.codegen.io.DataSink
import io.vrap.rmf.codegen.io.FileDataSink
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import org.slf4j.LoggerFactory

class OasGeneratorModule constructor(
    override val apiProvider: OasProvider,
    override val generatorConfig: io.vrap.rmf.codegen.CodeGeneratorConfig,
    override val languageBaseTypes: LanguageBaseTypes,
    override val defaultPackage: String = "io/vrap/rmf",
    override val dataSink: DataSink =  FileDataSink(generatorConfig.outputFolder)
): GeneratorModule {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(OasGeneratorModule::class.java)
    }
}
