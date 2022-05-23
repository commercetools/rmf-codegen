package io.vrap.codegen.languages.csharp.modules

import io.vrap.codegen.languages.csharp.requests.CsharpHttpRequestRenderer
import io.vrap.codegen.languages.csharp.requests.CsharpRequestBuilderResourceRenderer
import io.vrap.rmf.codegen.di.RamlGeneratorModule
import io.vrap.rmf.codegen.di.Module
import io.vrap.rmf.codegen.rendering.*


object CsharpClientBuilderModule: Module {

    override fun configure(generatorModule: RamlGeneratorModule) = setOf<CodeGenerator>(
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
