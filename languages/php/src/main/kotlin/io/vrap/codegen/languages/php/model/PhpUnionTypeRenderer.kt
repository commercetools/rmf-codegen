package io.vrap.codegen.languages.php.model

import com.google.inject.Inject
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.UnionTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.UnionType

class PhpUnionTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, UnionTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix:String

    @Inject
    @SharedPackageName
    lateinit var sharedPackageName: String

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
