package io.vrap.codegen.languages.php.model

import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.forcedLiteralEscape
import io.vrap.codegen.languages.php.extensions.toNamespaceName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api

class ApiRootFileProducer @Inject constructor(api: Api, vrapTypeProvider: VrapTypeProvider) : FileProducer, AbstractRequestBuilder(api, vrapTypeProvider) {

    override fun produceFiles(): List<TemplateFile> = listOf(
        produceApiRoot(api)
    )

    fun produceApiRoot(type: Api): TemplateFile {
        return TemplateFile(relativePath = "src/Client/ApiRoot.php",
                content = """
                        |<?php
                        |${PhpSubTemplates.generatorInfo}
                        |
                        |namespace ${packagePrefix.toNamespaceName().escapeAll()}\\Client;
                        |
                        |<<${type.imports()}>>
                        |use GuzzleHttp\\Client;
                        |
                        |class ApiRoot extends ApiResource
                        |{
                        |   /**
                        |    * @psalm-param array<string, scalar> $!args
                        |    */
                        |   public function __construct(Client $!client = null, array $!args = [])
                        |   {
                        |       parent::__construct('', $!args, $!client);
                        |   }
                        |
                        |   <<${type.subResources()}>>
                        |}
                    """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape())
    }
}
