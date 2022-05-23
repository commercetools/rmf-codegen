package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType

class PhpCollectionRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val parentType = type.type
        val sequenceType = if (parentType != null && parentType.name != "object") {
            "${(parentType.toVrapType() as VrapObjectType).simpleClassName}Collection" } else "MapperSequence"
        val importSequence = if (parentType != null && parentType.name != "object") {
            "${(parentType.toVrapType() as VrapObjectType).fullClassName()}Collection" } else "${sharedPackageName.toNamespaceName()}\\Base\\MapperSequence"
        val template = when (type.namedSubTypes().isNotEmpty()) {
            true -> """@template T of ${vrapType.simpleClassName}
                | * @extends $sequenceType<T>
                | * @psalm-method T current()
                | * @psalm-method T end()
                | * @psalm-method T at($!offset)"""
            else -> "@extends $sequenceType<${vrapType.simpleClassName}>"
        }

        val genType = when (type.namedSubTypes().isNotEmpty()) {
            true -> "T"
            else -> vrapType.simpleClassName
        }

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName()};
            |
            |use $importSequence;
            |use ${sharedPackageName.toNamespaceName()}\Exception\InvalidArgumentException;
            |use stdClass;
            |
            |/**
            | * $template
            | * @method ${vrapType.simpleClassName} current()
            | * @method ${vrapType.simpleClassName} end()
            | * @method ${vrapType.simpleClassName} at($!offset)
            | */
            |class ${vrapType.simpleClassName}Collection extends $sequenceType
            |{
            |    /**
            |     * @psalm-assert $genType $!value
            |     * @psalm-param $genType|stdClass $!value
            |     * @throws InvalidArgumentException
            |     *
            |     * @return ${vrapType.simpleClassName}Collection
            |     */
            |    public function add($!value)
            |    {
            |        if (!$!value instanceof ${vrapType.simpleClassName}) {
            |            throw new InvalidArgumentException();
            |        }
            |        $!this->store($!value);
            |
            |        return $!this;
            |    }
            |
            |    /**
            |     * @psalm-return callable(int):?$genType
            |     */
            |    protected function mapper()
            |    {
            |        return function (?int $!index): ?${vrapType.simpleClassName} {
            |            $!data = $!this->get($!index);
            |            if ($!data instanceof stdClass) {
            |                /** @var $genType $!data */
            |                $!data = ${vrapType.simpleName()}Model::of($!data);
            |                $!this->set($!data, $!index);
            |            }
            |
            |            return $!data;
            |        };
            |    }
            |}
        """.trimMargin().forcedLiteralEscape()

        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(basePackagePrefix.toNamespaceName(), "").replace("\\", "/") + "Collection.php",
                content = content
        )
    }
}
