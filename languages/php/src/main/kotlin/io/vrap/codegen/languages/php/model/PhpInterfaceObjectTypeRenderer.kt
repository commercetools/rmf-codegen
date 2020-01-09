package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendring.utils.keepCurlyIndent
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.AnyAnnotationType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpInterfaceObjectTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix:String

    @Inject
    @SharedPackageName
    lateinit var sharedPackageName: String

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val mapAnnotation = type.getAnnotation("asMap")


        val content = when (mapAnnotation) {
            is Annotation -> mapContent(type, mapAnnotation.type)
            else -> content(type)
        }


        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(basePackagePrefix.toNamespaceName(), "").replace("\\", "/") + ".php",
                content = content
        )
    }

    fun mapContent(type: ObjectType, anno: AnyAnnotationType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\Collection;
            |
            |interface ${vrapType.simpleClassName} extends Collection
            |{
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }

    fun content(type: ObjectType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\DateTimeImmutableCollection;
            |<<${type.imports()}>>
            |
            |interface ${vrapType.simpleClassName} ${type.type?.toVrapType()?.simpleName()?.let { "extends $it" } ?: "extends JsonObject"}
            |{
            |    ${if (type.discriminator != null) {"const DISCRIMINATOR_FIELD = '${type.discriminator}';"} else ""}
            |    <<${type.toBeanConstant()}>>
            |
            |    <<${type.getters()}>>
            |
            |    <<${type.setters()}>>
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }

    fun ObjectType.imports() = this.getImports().map { "use ${it.escapeAll()};" }.joinToString(separator = "\n")

    fun Property.toPhpConstant(): String {

        return """
            |const FIELD_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this.patternName())} = '${this.name}';
        """.trimMargin();
    }

    fun ObjectType.toBeanConstant(): String {
        val superTypeAllProperties = when(this.type) {
            is ObjectType -> (this.type as ObjectType).allProperties
            else -> emptyList<Property>()
        };
        return this.properties
                .asSequence()
                .filter { it -> superTypeAllProperties.none { property -> it.name == property.name } }
                .map { it.toPhpConstant() }.joinToString(separator = "\n")
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

    fun ObjectType.setters(): String {
        val discriminator = this.discriminatorProperty()
        return this.properties
                .filter { property -> property != discriminator }
                .filter { !it.isPatternProperty() }
                .map { it.setter() }
                .joinToString(separator = "\n\n")
    }

    fun ObjectType.getters() = this.properties
            .filter { !it.isPatternProperty() }
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    fun Property.setter(): String {
        return """
            |public function set${this.name.capitalize()}(?${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" } $${this.name}): void;
        """.trimMargin()
    }

    fun Property.getter(): String {
        return """
            |/**${if (this.type.description?.value?.isNotBlank() == true) """
            | {{${this.type.toPhpComment()}}}
            | *""" else ""}
            | * @return null|${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" }
            | */
            |public function get${this.name.capitalize()}();
        """.trimMargin().keepCurlyIndent()
    }

    private fun Property.patternName(): String {
        return if (this.isPatternProperty())
            "pattern" + (this.eContainer() as ObjectType).properties.indexOf(this)
        else
            this.name
    }
}
