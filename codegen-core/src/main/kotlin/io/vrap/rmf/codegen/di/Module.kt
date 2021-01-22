package io.vrap.rmf.codegen.di;

import io.vrap.rmf.codegen.rendring.CodeGenerator;

interface Module {
    fun configure(generatorModule: GeneratorModule): Set<CodeGenerator>
}
