package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.php.PhpBaseTypes
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
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.util.*

class PhpObjectTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

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
                relativePath = "src/" + vrapType.fullClassName().replace(basePackagePrefix.toNamespaceName(), "").replace("\\", "/") + "Model.php",
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
            |
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

    fun content(type: ObjectType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        return """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObject;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\JsonObjectModel;
            |use ${sharedPackageName.toNamespaceName().escapeAll()}\\Base\\MapperFactory;
            |use stdClass;
            |
            |<<${type.imports()}>>
            |
            |final class ${vrapType.simpleClassName}Model extends JsonObjectModel implements ${vrapType.simpleClassName}
            |{
            |    ${if (type.discriminator != null || type.discriminatorValue != null) {"const DISCRIMINATOR_VALUE = '${type.discriminatorValue ?: ""}';"} else ""}
            |    <<${type.constructor()}>>
            |
            |    <<${type.toBeanFields()}>>
            |
            |    <<${type.getters()}>>
            |    <<${type.setters()}>>
            |    <<${type.patternGetter()}>>
            |    <<${type.serializer()}>>
            |    
            |    <<${type.discriminatorClasses()}>>
            |    <<${type.discriminatorResolver()}>>
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }


    fun ObjectType.constructor(): String {
        return """
                |public function __construct(
                |    <<${this.allProperties.filter { !it.isPatternProperty() }.joinToString(",\n") { it.toParam() }}>>
                |) {
                |    <<${this.allProperties.filter { !it.isPatternProperty() }.joinToString("\n") { "$!this->${it.name} = $${it.name};" }}>>
                |    ${if (this.discriminator.isNullOrEmpty().not()) "$!this->${this.discriminator} = static::DISCRIMINATOR_VALUE;" else ""}
                |}
            """.trimMargin()
    }

    fun ObjectType.imports() = this.getImports(this.allProperties).map { "use ${it.escapeAll()};" }
            .plus(this.getPropertyImports(this.allProperties).map { "use ${it.escapeAll()};" })
            .plus(this.getObjectImports(this.allProperties).map { "use ${it.escapeAll()}Model;" })
            .distinct()
            .sorted()
            .joinToString(separator = "\n")

    fun Property.toParam(): String {
        return "${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" } $${this.name} = null"
    }

    fun Property.toPhpField(): String {

        return """
            |/**
            | * @var ?${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" }
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

    fun ObjectType.setters() = this.allProperties
            .filter { !it.isPatternProperty() }
            .map { it.setter() }
            .joinToString(separator = "\n\n")


    fun ObjectType.getters() = this.allProperties
            .filter { !it.isPatternProperty() }
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    fun ObjectType.serializer(): String {
        val dtProperties = this.allProperties
            .filter { it.type is DateTimeOnlyType || it.type is DateTimeType || it.type is DateOnlyType || it.type is TimeOnlyType }

        if (dtProperties.isNotEmpty()) {
            return """
                |public function jsonSerialize() {
                |    $!data = $!this->toArray();
                |    <<${dtProperties.map { it.serializer() }.joinToString(separator = "\n\n")}>>
                |    return (object)$!data;
                |}
            """.trimMargin()
        } else {
            return ""
        }
    }

    fun Property.setter(): String {
        return """
            |final public function set${this.name.capitalize()}(?${if (this.type.toVrapType().simpleName() != "stdClass") this.type.toVrapType().simpleName() else "JsonObject" } $${this.name}): void
            |{
            |    $!this->${this.name} = $${this.name};
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
                |   if (is_null($!this->${this.name})) {
                |       <<${this.mapper()}>>
                |   }
                |   return $!this->${this.name};
                |}
        """.trimMargin()
    }

    private fun ObjectType.patternGetter(): String {
        if (this.allProperties.filter { it.isPatternProperty() }.isEmpty()) {
            return ""
        }
        return """
            |/**
            | * @return mixed
            | */
            |public function by(string $!key)
            |{
            |    $!data = $!this->raw($!key);
            |    if (is_null($!data)) {
            |        return null;
            |    }
            |    <<${this.properties.filter { it.isPatternProperty() }.map { it.patternGet() }.joinToString("\n")}>>
            |    return $!data;
            |}
        """.trimMargin().forcedLiteralEscape()
    }

    private fun Property.patternGet(): String {
        val defineObject = this.eContainer().toVrapType();
        return """
            |if (preg_match(${defineObject.simpleName()}::${this.toPhpConstantName()}, $!key) === 1) {
            |    <<${this.patternMapper()}>>
            |}
        """.trimMargin().forcedLiteralEscape()
    }

    fun Property.validationAnnotations(): String {
        val validationAnnotations = ArrayList<String>()
        if (this.required != null && this.required!!) {
            validationAnnotations.add("@NotNull")
        }
        if (CascadeValidationCheck.doSwitch(this.type)) {
            validationAnnotations.add("@Valid")
        }
        return validationAnnotations.joinToString(separator = "\n")
    }

    fun Property.serializer(): String {
        val type = this.type
        val defineObject = this.eContainer().toVrapType();
        val indexName = "${defineObject.simpleName()}::${this.toPhpConstantName()}"
        return when(type) {
            is DateTimeType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |   $!data[$indexName] = $!data[$indexName]->setTimeZone(new \DateTimeZone('UTC'))->format('c');
                |}
            """.trimMargin()
            is DateOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |   $!data[$indexName] = $!data[$indexName]->format('Y-m-d');
                |}
            """.trimMargin()
            is TimeOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |   $!data[$indexName] = $!data[$indexName]->format('H:i:s.u');
                |}
            """.trimMargin()
            is DateTimeOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |   $!data[$indexName] = $!data[$indexName]->setTimeZone(new \DateTimeZone('UTC'))->format('c');
                |}
            """.trimMargin()
            else -> return ""
        }.escapeAll()
    }


    fun Property.patternMapper():String {
        val type = this.type
        return when(type) {
            is ObjectType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var stdClass $!data */
                        |return new JsonObjectModel($!data);
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var stdClass|array<string, mixed> $!data */
                        |${if (type.discriminator.isNullOrBlank()) "" else "$!className = ${type.toVrapType().simpleName()}Model::resolveDiscriminatorClass($!data);"}
                        |return ${if (type.discriminator.isNullOrBlank()) "${this.type.toVrapType().simpleName()}Model" else "$!className"}::of($!data);
                    """.trimMargin()
                }
            is ArrayType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var array<int, stdClass> $!data */
                        |return $!data;
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var array<int, stdClass> $!data */
                        |return ${if (type.items?.isScalar() != true) "new ${this.type.toVrapType().simpleName()}($!data)" else "$!data"};
                    """.trimMargin()
                }
            else ->
                when (type.toVrapType()) {
                    is VrapEnumType -> """
                        |/** @psalm-var scalar $!data */
                        |return (string)$!data;
                    """.trimMargin()
                    PhpBaseTypes.integerType ->  """
                        |/** @psalm-var scalar $!data */
                        |return (int)$!data;
                    """.trimMargin()
                    PhpBaseTypes.stringType ->  """
                        |/** @psalm-var scalar $!data */
                        |return (string)$!data;
                    """.trimMargin()
                    PhpBaseTypes.doubleType ->  """
                        |/** @psalm-var scalar $!data */
                        |return (float)$!data;
                    """.trimMargin()
                    PhpBaseTypes.booleanType ->  """
                        |/** @psalm-var scalar $!data */
                        |return (bool)$!data;
                    """.trimMargin()
                    PhpBaseTypes.dateTimeType -> """
                        |/** @psalm-var string $!data */
                        |$!data = DateTimeImmutable::createFromFormat(MapperFactory::DATETIME_FORMAT, $!data);
                        |if ($!data === false) {
                        |    return null;
                        |}
                        |return $!data;
                    """.trimMargin()
                    else -> """
                        |/** @psalm-var stdClass $!data */
                        |return JsonObjectModel::of($!data);
                    """.trimMargin()
                }
        }
    }

    fun Property.mapper():String {
        val type = this.type
        val defineObject = this.eContainer().toVrapType();
        return when(type) {
            is ObjectType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$!this->${this.name} = JsonObjectModel::of($!data);
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |${if (type.discriminator.isNullOrBlank()) "" else "$!className = ${type.toVrapType().simpleName()}Model::resolveDiscriminatorClass($!data);"}
                        |$!this->${this.name} = ${if (type.discriminator.isNullOrBlank()) "${this.type.toVrapType().simpleName()}Model" else "$!className"}::of($!data);
                    """.trimMargin()
                }
            is ArrayType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var ?array<int, mixed> $!data */
                        |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$!this->${this.name} = $!data;
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var ?array<int, stdClass> $!data */
                        |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$!this->${this.name} = ${if (type.items?.isScalar() != true) "${this.type.toVrapType().simpleName()}::fromArray($!data)" else "$!data"};
                    """.trimMargin()
                }
            else ->
                when (type.toVrapType()) {
                    is VrapEnumType -> """
                       |/** @psalm-var ?string $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!this->${this.name} = (string)$!data;
                    """.trimMargin()
                    PhpBaseTypes.integerType ->  """
                       |/** @psalm-var ?int $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!this->${this.name} = (int)$!data;
                    """.trimMargin()
                    PhpBaseTypes.stringType ->  """
                       |/** @psalm-var ?string $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!this->${this.name} = (string)$!data;
                    """.trimMargin()
                    PhpBaseTypes.doubleType ->  """
                       |/** @psalm-var ?float $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!this->${this.name} = (float)$!data;
                    """.trimMargin()
                    PhpBaseTypes.booleanType ->  """
                       |/** @psalm-var ?bool $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!this->${this.name} = (bool)$!data;
                    """.trimMargin()
                    PhpBaseTypes.dateTimeType -> """
                       |/** @psalm-var ?string $!data */
                       |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$!data = DateTimeImmutable::createFromFormat(MapperFactory::DATETIME_FORMAT, $!data);
                       |if ($!data === false) {
                       |    return null;
                       |}
                       |$!this->${this.name} = $!data;
                    """.trimMargin()
                    else -> """
                        |/** @psalm-var ?stdClass $!data */
                        |$!data = $!this->raw(${defineObject.simpleName()}::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$!this->${this.name} = JsonObjectModel::of($!data);
                    """.trimMargin()
                }
        }
    }

    private object CascadeValidationCheck : TypesSwitch<Boolean>() {
        override fun defaultCase(`object`: EObject?): Boolean? {
            return false
        }

        override fun caseObjectType(objectType: ObjectType?): Boolean? {
            return true
        }

        override fun caseArrayType(arrayType: ArrayType): Boolean? {
            return if (arrayType.items != null) {
                doSwitch(arrayType.items)
            } else {
                false
            }
        }
    }


    private fun ObjectType.discriminatorClasses(): String {
        if (this.discriminator.isNullOrBlank()) {
            return ""
        }
        return """
            |/**
            | * @psalm-var array<string, class-string<${this.toVrapType().simpleName()}> >
            | * ${if (this.namedSubTypes().filterIsInstance<ObjectType>().count() > 50) "@psalm-suppress InvalidPropertyAssignmentValue" else ""}
            | */
            |private static $!discriminatorClasses = [
            |   <<${this.namedSubTypes().filterIsInstance<ObjectType>().map { "'${it.discriminatorValue}' => ${it.toVrapType().simpleName()}Model::class," }.joinToString(separator = "\n")}>>
            |];
        """.trimMargin()
    }

    private fun ObjectType.discriminatorResolver(): String {
        if (this.discriminator.isNullOrBlank()) {
            return ""
        }
        return """
            |/**
            | * @psalm-param stdClass|array<string, mixed> $!value
            | * @psalm-return class-string<${this.toVrapType().simpleName()}>
            | */
            |public static function resolveDiscriminatorClass($!value): string
            |{
            |   $!fieldName = ${this.toVrapType().simpleName()}::DISCRIMINATOR_FIELD;
            |   if (is_object($!value) && isset($!value->$!fieldName)) {
            |       /** @var string $!discriminatorValue */
            |       $!discriminatorValue = $!value->$!fieldName;
            |       if (isset(static::$!discriminatorClasses[$!discriminatorValue])) {
            |            return static::$!discriminatorClasses[$!discriminatorValue];
            |       }
            |   }
            |   if (is_array($!value) && isset($!value[$!fieldName])) {
            |       /** @var string $!discriminatorValue */
            |       $!discriminatorValue = $!value[$!fieldName];
            |       if (isset(static::$!discriminatorClasses[$!discriminatorValue])) {
            |            return static::$!discriminatorClasses[$!discriminatorValue];
            |       }
            |   }
            |   
            |   /** @psalm-var class-string<${this.toVrapType().simpleName()}> */
            |   $!type = ${this.toVrapType().simpleName()}Model::class;
            |   return $!type;
            |}
        """.trimMargin()
    }
}

