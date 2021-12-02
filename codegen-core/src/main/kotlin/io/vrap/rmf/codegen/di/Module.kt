package io.vrap.rmf.codegen.di;

import io.vrap.rmf.codegen.rendring.CodeGenerator;
import java.text.MessageFormat

interface Module {
    fun configure(generatorModule: GeneratorModule) = when (generatorModule) {
        is RamlGeneratorModule -> configure(generatorModule)
        is OasGeneratorModule -> configure(generatorModule)
        else -> throw IllegalArgumentException(MessageFormat.format("Module type not supported {0}", generatorModule::class))
    }

    fun configure(generatorModule: OasGeneratorModule) = setOf<CodeGenerator>(
    )
    fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
    )
}
