package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import java.util.*

class PhpCollectionRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName()};
            |
            |use ${packagePrefix.toNamespaceName()}\Base\MapCollection;
            |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
            |
            |/**
            | * @method ${vrapType.simpleClassName} current()
            | * @method ${vrapType.simpleClassName} at($!offset)
            | */
            |final class ${vrapType.simpleClassName}Collection extends MapCollection
            |{
            |    public function add(${vrapType.simpleClassName} $!value): ${vrapType.simpleClassName}Collection
            |    {
            |        parent::add($!value);
            |
            |        return $!this;
            |    }
            |
            |    protected function mapper()
            |    {
            |        return function($!index) {
            |            $!data = $!this->get($!index);
            |            if (!is_null($!data) && !$!data instanceof ${vrapType.simpleClassName}) {
            |                $!data = new ${vrapType.simpleName()}Model($!data);
            |                $!this->set($!data, $!index);
            |            }
            |            return $!data;
            |        };
            |    }
            |}
        """.trimMargin().forcedLiteralEscape()

        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Collection.php",
                content = content
        )
    }
}
