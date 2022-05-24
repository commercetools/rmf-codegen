package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.TraitRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.Trait
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.QueryParameter
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject

class PhpTraitRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, TraitRenderer {
    protected val basePackagePrefix = clientConstants.basePackagePrefix

    protected val sharedPackageName = clientConstants.sharedPackageName

    protected val clientPackageName = clientConstants.clientPackage

    private val resourcePackage = "Resource"
    override fun render(type: Trait): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType


        val content= """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()}\\$resourcePackage;
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Client\\ApiRequestInterface;
            |
            |/**
            | * @template T of ApiRequestInterface
            | * @template-extends ApiRequestInterface<T>
            | */
            |interface ${type.toTraitName()} extends ApiRequestInterface
            |{
            |    <<${type.queryParamsWithers()}>>
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()

        val relativeTypeNamespace = vrapType.`package`.toNamespaceName().replace(basePackagePrefix.toNamespaceName() + "\\", "").replace("\\", "/") + "/$resourcePackage"
        val relativePath = "src/" + relativeTypeNamespace + "/" + type.toTraitName() + ".php"

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    private fun Trait.queryParamsWithers() : String = this.queryParameters
            .filter { it.getAnnotation(PLACEHOLDER_PARAM_ANNOTATION, true) == null }
            .map { """
                |/**
                | * @return ApiRequestInterface
                | * @psalm-return T
                | */
                |public function with${it.fieldName().firstUpperCase()}(${it.witherType()} $${it.fieldName()});
            """.trimMargin().escapeAll() }
            .joinToString(separator = "\n\n")

    private fun QueryParameter.witherType() : String {
        val type = this.type;
        return when (type) {
            is ArrayType -> type.items.toVrapType().simpleName()
            else -> type.toVrapType().simpleName()
        }
    }

    private fun QueryParameter.fieldName(): String {
        return StringCaseFormat.LOWER_CAMEL_CASE.apply(this.name.replace(".", "-"))
    }
}
