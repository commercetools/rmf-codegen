package io.vrap.codegen.languages.php.model

import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendering.utils.keepCurlyIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpInterfaceObjectTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType


        val content = when (type.getAnnotation("asMap")) {
            is Annotation -> mapContent(type)
            else -> content(type)
        }


        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(basePackagePrefix.toNamespaceName(), "").replace("\\", "/") + ".php",
                content = content
        )
    }

    private fun mapContent(type: ObjectType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\CMap;
            |
            |interface ${vrapType.simpleClassName} extends CMap
            |{
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }

    fun content(type: ObjectType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val extends = Lists.newArrayList(type.type?.toVrapType()?.simpleName() ?: "JsonObject")
                .plus(
                        when (val ex = type.getAnnotation("php-use-trait") ) {
                            is Annotation -> {
                                (ex.value as StringInstance).value + "Interface".escapeAll()
                            }
                            else -> null
                        }
                ).filterNotNull()

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\DateTimeImmutableCollection;
            |<<${type.imports()}>>
            |
            |interface ${vrapType.simpleClassName} extends ${extends.joinToString(separator = ", ")}
            |{
            |    ${if (type.discriminator != null) {"public const DISCRIMINATOR_FIELD = '${type.discriminator}';"} else ""}
            |    <<${type.toBeanConstant()}>>
            |
            |    <<${type.getters()}>>
            |
            |    <<${type.setters()}>>
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }

    private fun ObjectType.imports() = this.getImports().map { "use ${it.escapeAll()};" }
            .plus(this.getPropertyImports(this.properties).map { "use ${it.escapeAll()};" })
            .distinct()
            .sorted()
            .joinToString(separator = "\n")

    private fun Property.toPhpConstant(): String {

        return """
            |public const FIELD_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this.patternName())} = '${this.name}';
        """.trimMargin()
    }

    private fun ObjectType.toBeanConstant(): String {
        val superTypeAllProperties = when(this.type) {
            is ObjectType -> (this.type as ObjectType).allProperties
            else -> emptyList<Property>()
        }
        return this.properties
                .filter { it.getAnnotation("deprecated") == null }
                .filter { superTypeAllProperties.none { property -> it.name == property.name } }
                .joinToString(separator = "\n") { it.toPhpConstant() }
    }

    private fun ObjectType.patternGetter(): String {
        if (this.properties.none { it.isPatternProperty() }) {
            return ""
        }
        return """
            |/**
            | * @return mixed
            | */
            |public function by(string $!key);
        """.trimMargin()
    }

    private fun ObjectType.setters(): String {
        val discriminator = this.discriminatorProperty()
        return this.properties
                .filter { property -> property != discriminator }
                .filter { it.getAnnotation("deprecated") == null }
                .filter { !it.isPatternProperty() }.joinToString(separator = "\n\n") { it.setter() }
    }

    private fun ObjectType.getters() = this.properties
            .filter { it.getAnnotation("deprecated") == null }
            .filter { !it.isPatternProperty() }.joinToString(separator = "\n\n") { it.getter() }

    private fun Property.setter(): String {
        val t = when(this.type.toVrapType().simpleName()) {
            "stdClass" -> "?JsonObject"
            "mixed" -> ""
            else -> "?${this.type.toVrapType().simpleName()}"
        }
        val d = when(this.type.toVrapType().simpleName()) {
            "stdClass" -> "?JsonObject"
            "mixed" -> "mixed"
            else -> "?${this.type.toVrapType().simpleName()}"
        }
        return """
            |/**
            | * @param $d $${this.name}
            | */
            |public function set${this.name.firstUpperCase()}($t $${this.name}): void;
        """.trimMargin()
    }

    private fun Property.getter(): String {
        val typeName = when(this.type) {
            is UnionType -> "mixed"
            else -> this.type.typeName()
        }
        return """
            |/**${if (this.type.description?.value?.isNotBlank() == true) """
            | {{${this.type.toPhpComment()}}}
            | *""" else ""}
            |${this.deprecationAnnotation()}
            | * @return null|$typeName
            | */
            |public function get${this.name.firstUpperCase()}();
        """.trimMargin().keepCurlyIndent()
    }

    private fun Property.patternName(): String {
        return if (this.isPatternProperty())
            "pattern" + (this.eContainer() as ObjectType).properties.indexOf(this)
        else
            this.name
    }

    private fun AnyType.typeNames(): String = when (this) {
        is UnionType -> this.oneOf.map { it.typeName() }.plus("JsonObject").distinct().sorted().joinToString(separator = "|")
        else -> this.typeName()
    }
    private fun AnyType.typeName(): String = if (this.toVrapType().simpleName() != "stdClass") this.toVrapType().simpleName() else "mixed"

    fun Property.deprecationAnnotation(): String {
        val anno = this.getAnnotation("markDeprecated", true)
        if (anno != null && (anno.value as BooleanInstance).value == true) {
            return """
                | * @deprecated""".trimMargin()
        }
        return "";
    }
}
