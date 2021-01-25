package io.vrap.codegen.languages.csharp.modules

import io.vrap.codegen.languages.csharp.requests.CsharpHttpRequestRenderer
import io.vrap.codegen.languages.csharp.requests.CsharpRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.di.GeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendring.*


object CsharpClientBuilderModule: Module {

    override fun configure(generatorModule: GeneratorModule) = setOf<CodeGenerator>(
            MethodGenerator(
                    setOf(
                            CsharpHttpRequestRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName())
                    ),
                    generatorModule.allResourceMethods()
            ),
            ResourceGenerator(
                    setOf(
                            CsharpRequestBuilderResourceRenderer(generatorModule.vrapTypeProvider(), generatorModule.providePackageName())
                    ),
                    generatorModule.allResources()
            )
    )
}
