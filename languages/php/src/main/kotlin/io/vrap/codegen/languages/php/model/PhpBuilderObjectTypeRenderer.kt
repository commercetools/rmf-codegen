package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.di.SharedPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpBuilderObjectTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var packagePrefix:String

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
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Builder.php",
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
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\MapperMap;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\Builder;
            |
            |/**
            | * @extends Builder<${vrapType.simpleClassName}>
            | */
            |final class ${vrapType.simpleClassName}Builder extends MapperMap implements Builder
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
            |    
            |    /**
            |     * @return ${vrapType.simpleClassName}
            |     */
            |    public function build() {
            |        return new ${vrapType.simpleClassName}Model($!this->toArray());
            |    }
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
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\Builder;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObjectModel;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\MapperFactory;
            |use stdClass;
            |<<${type.imports()}>>
            |
            |/**
            | * @implements Builder<${vrapType.simpleClassName}>
            | */
            |final class ${vrapType.simpleClassName}Builder implements Builder
            |{
            |    <<${type.constructor()}>>
            |
            |    <<${type.toBeanFields()}>>
            |
            |    <<${type.getters()}>>
            |    <<${type.withers()}>>
            |    <<${type.withBuilders()}>>
            |    
            |    <<${type.build()}>>
            |    
            |    public static function of(): ${vrapType.simpleClassName}Builder
            |    {
            |        return new self();
            |    }
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }


    fun ObjectType.constructor(): String {
        return """
                |public function __construct() {
                |}
            """.trimMargin()
    }

    fun ObjectType.build(): String {
        val vrapType = this.toVrapType() as VrapObjectType

        return """
                |public function build(): ${vrapType.simpleClassName} {
                |    return new ${vrapType.simpleClassName}Model(
                |        <<${this.allProperties.filter { !it.isPatternProperty() }.joinToString(",\n") { it.buildProperty() }}>>
                |    );
                |}
            """.trimMargin()
    }

    fun Property.buildProperty(): String {
        if (this.type.isScalar() || this.type is ArrayType || this.type.toVrapType().simpleName() == "stdClass") {
            return "$!this->${this.name}"
        }
        return "($!this->${this.name} instanceof ${this.type.toVrapType().simpleBuilderName()} ? $!this->${this.name}->build() : $!this->${this.name})"
    }

    fun ObjectType.imports() = this.getImports(this.allProperties).map { "use ${it.escapeAll()};" }
            .plus(this.getImports(this.allProperties.filter { !it.type.isScalar() && !(it.type is ArrayType) && !(it.type.toVrapType().simpleName() == "stdClass") }).map { "use ${it.escapeAll()}Builder;" })
            .distinct()
            .sorted()
            .joinToString(separator = "\n")

    fun Property.toPhpField(): String {
        if (this.type.isScalar() || this.type is ArrayType || this.type.toVrapType().simpleName() == "stdClass") {
            return """
                |/**
                | * @var ?${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" }
                | */
                |protected $${this.name};
            """.trimMargin();
        }
        return """
            |/**
            | * @var ?${this.type.toVrapType().simpleBuilderName()}|${this.type.toVrapType().simpleName()}
            | */
            |protected $${this.name};
        """.trimMargin();
    }

    fun Property.toPhpConstantName(): String {
        return "FIELD_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this.patternName())}"
    }

    fun Property.toPhpConstant(): String {
        return """
            |const ${this.toPhpConstantName()} = '${this.name}';
        """.trimMargin();
    }

    private fun Property.patternName(): String {
        return if (this.isPatternProperty())
            "pattern" + (this.eContainer() as ObjectType).properties.indexOf(this)
        else
            this.name
    }

    fun ObjectType.toBeanConstant() = this.properties
            .map { it.toPhpConstant() }.joinToString(separator = "\n")

    fun ObjectType.toBeanFields() = this.allProperties
            .filter { !it.isPatternProperty() }
            .map { it.toPhpField() }.joinToString(separator = "\n\n")

    fun ObjectType.withers() = this.allProperties
            .filter { !it.isPatternProperty() }
            .map { it.wither() }
            .joinToString(separator = "\n\n")

    fun ObjectType.withBuilders() = this.allProperties
            .filter { !it.isPatternProperty() }
            .filter { !it.type.isScalar() && !(it.type is ArrayType) && !(it.type.toVrapType().simpleName() == "stdClass")}
            .map { it.withBuilder() }
            .joinToString(separator = "\n\n")

    fun ObjectType.getters() = this.allProperties
            .filter { !it.isPatternProperty() }
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    fun Property.wither(): String {
        return """
            |/**
            | * @return $!this
            | */
            |final public function with${this.name.capitalize()}(?${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" } $${this.name})
            |{
            |    $!this->${this.name} = $${this.name};
            |    
            |    return $!this;
            |}
        """.trimMargin()
    }

    fun Property.withBuilder(): String {
        return """
            |/**
            | * @return $!this
            | */
            |final public function with${this.name.capitalize()}Builder(?${this.type.toVrapType().simpleBuilderName()} $${this.name})
            |{
            |    $!this->${this.name} = $${this.name};
            |    
            |    return $!this;
            |}
        """.trimMargin()
    }

    fun Property.getter(): String {
        return """
                |/**
                | ${this.type.toPhpComment()}
                | * @return ${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" }|null
                | */
                |final public function get${this.name.capitalize()}()
                |{
                |   return ${this.buildProperty()};
                |}
        """.trimMargin()
    }
}

