package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.UnionTypeRenderer
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.UnionType

class PhpUnionCollectionRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, UnionTypeRenderer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    override fun render(type: UnionType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName()};
            |
            |use ${sharedPackageName.toNamespaceName()}\Base\MapperSequence;
            |use ${sharedPackageName.toNamespaceName()}\Exception\InvalidArgumentException;
            |use stdClass;
            |
            |/**
            | * @extends MapperSequence<${ vrapType.simpleClassName }>
            | * @method ${vrapType.simpleClassName} current()
            | * @method ${vrapType.simpleClassName} at($!offset)
            | */
            |class ${vrapType.simpleClassName}Collection extends MapperSequence
            |{
            |    /**
            |     * @psalm-assert ${vrapType.simpleClassName} $!value
            |     * @psalm-param ${vrapType.simpleClassName}|stdClass $!value
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
            |     * @psalm-return callable(int):?${vrapType.simpleClassName}
            |     */
            |    protected function mapper()
            |    {
            |        return function (int $!index): ?${vrapType.simpleClassName} {
            |            $!data = $!this->get($!index);
            |            if ($!data instanceof stdClass) {
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
