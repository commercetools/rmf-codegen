package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.AbstractRequestBuilder
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.forcedLiteralEscape
import io.vrap.codegen.languages.php.extensions.toNamespaceDir
import io.vrap.codegen.languages.php.extensions.toNamespaceName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.BooleanInstance

class ApiRootFileProducer constructor(api: Api, vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider, clientConstants) {

    override fun produceFiles(): List<TemplateFile> = listOf(
        produceApiRoot(api)
    )

    private fun produceApiRoot(type: Api): TemplateFile {
        val rootResource = type.resources.filterNot { it.deprecated() }.firstOrNull { resource -> resource.resourcePath == "/" }
        return TemplateFile(relativePath = "src/${clientPackageName.replace(basePackagePrefix, "").toNamespaceDir()}/${rootResource()}.php",
                content = """
                    |<?php
                    |${PhpSubTemplates.generatorInfo}
                    |
                    |namespace ${clientPackageName.toNamespaceName().escapeAll()};
                    |
                    |<<${type.imports()}>>
                    |use GuzzleHttp\\ClientInterface;
                    |use ${sharedPackageName.toNamespaceName()}\\Client\\ApiResource;
                    |
                    |/**
                    | * ${if (type.markDeprecated()) """
                    | * @deprecated""" else ""}
                    | */
                    |class ${rootResource()} extends ApiResource
                    |{
                    |    /**
                    |     * @psalm-param array<string, string> $!args
                    |     */
                    |    public function __construct(ClientInterface $!client = null, array $!args = [])
                    |    {
                    |        parent::__construct('', $!args, $!client);
                    |    }
                    |
                    |    <<${if (rootResource != null && type.resources.size == 1) rootResource.subResources() else type.subResources()}>>
                    |    <<${rootResource?.methods() ?: ""}>>
                    |}
                """.trimMargin().keepAngleIndent().forcedLiteralEscape())
    }

    private fun Api.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }
}
