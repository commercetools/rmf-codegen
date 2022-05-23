package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.UnionTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.UnionType

class PhpUnionTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, UnionTypeRenderer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    override fun render(type: UnionType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = when (type.getAnnotation("asMap")) {
            is Annotation -> mapContent(type)
            else -> content(type)
        }

        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(basePackagePrefix.toNamespaceName(), "").replace("\\", "/") + "Model.php",
                content = content
        )
    }

    private fun mapContent(type: UnionType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\MapperMap;
            |
            |/**
            | * @internal
            | */
            |final class ${vrapType.simpleClassName}Model extends MapperMap implements ${vrapType.simpleClassName}
            |{
            |    /**
            |     * @psalm-return callable(string):?mixed
            |     */
            |    protected function mapper()
            |    {
            |        return
            |            /**
            |             * @psalm-return ?mixed
            |             */
            |            function(string $!key) {
            |               return $!this->get($!key);
            |            };
            |    }
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }

    fun content(type: UnionType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\DateTimeImmutableCollection;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObjectModel;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\MapperFactory;
            |use stdClass;
            |
            |/**
            | * @internal
            | */
            |final class ${vrapType.simpleClassName}Model extends JsonObjectModel implements ${vrapType.simpleClassName}
            |{
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }
}
