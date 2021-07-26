package io.vrap.codegen.languages.php.model

import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.php.ClientConstants
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendring.utils.keepCurlyIndent
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PhpObjectTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, clientConstants: ClientConstants) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    private val basePackagePrefix = clientConstants.basePackagePrefix

    private val sharedPackageName = clientConstants.sharedPackageName

    override fun render(type: ObjectType): TemplateFile {

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

    private fun mapContent(type: ObjectType): String {
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

    fun content(type: ObjectType): String {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        val traitAnno = type.getAnnotation("php-use-trait")
        val traits = if (traitAnno != null) (traitAnno.value.value as String).split(",").map(String::trim) else emptyList()

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
            |<<${type.imports()}>>
            |
            |/**
            | * @internal
            | */
            |final class ${vrapType.simpleClassName}Model extends JsonObjectModel implements ${vrapType.simpleClassName}
            |{
            |    <<${traits.joinToString("\n") { "use ${it};" }}>>
            |
            |    ${if (type.discriminator != null || type.discriminatorValue != null) {"public const DISCRIMINATOR_VALUE = '${type.discriminatorValue ?: ""}';"} else ""}
            |    <<${type.toBeanFields()}>>
            |
            |    <<${type.discriminatorClasses()}>>
            |
            |    <<${type.constructor()}>>
            |
            |    <<${type.getters()}>>
            |
            |    <<${type.unionGetters()}>>
            |
            |    <<${type.setters()}>>
            |
            |    <<${type.patternGetter()}>>
            |
            |    <<${type.serializer()}>>
            |    
            |    <<${type.discriminatorResolver()}>>
            |}
        """.trimMargin().keepAngleIndent().forcedLiteralEscape()
    }


    fun ObjectType.constructor(): String {
        val discriminator = this.discriminatorProperty()
        return """
            |/**
            | * @psalm-suppress MissingParamType
            | */
            |public function __construct(
            |    <<${this.allProperties.filter { it.getAnnotation("deprecated") == null }.filter { property -> property != discriminator }.filter { !it.isPatternProperty() }.joinToString(",\n") { it.toParam() }}>>
            |) {
            |    <<${this.allProperties.filter { it.getAnnotation("deprecated") == null }.filter { property -> property != discriminator }.filter { !it.isPatternProperty() }.joinToString("\n") { "$!this->${it.name} = $${it.name};" }}>>
            |    ${if (discriminator != null) "$!this->${discriminator.name} = static::DISCRIMINATOR_VALUE;" else ""}
            |}
        """.trimMargin()
    }

    private fun ObjectType.imports() = this.getImports(this.allProperties).map { "use ${it.escapeAll()};" }
            .plus(this.getPropertyImports(this.allProperties).map { "use ${it.escapeAll()};" })
            .plus(this.getObjectImports(this.allProperties).map { "use ${it.escapeAll()}Model;" })
            .distinct()
            .sorted()
            .joinToString(separator = "\n")

    private fun Property.toParam(): String {
        val t = when(this.type.toVrapType().simpleName()) {
            "stdClass" -> "?JsonObject"
            "mixed" -> ""
            else -> "?${this.type.toVrapType().simpleName()}"
        }
        return "$t $${this.name} = null"
    }

    private fun Property.toPhpField(): String {
        val typeName = when(this.type) {
            is UnionType -> "mixed"
            else -> this.type.typeName()
        }
        return """
            |/**
            | * @var ?$typeName
            | */
            |protected $${this.name};
        """.trimMargin()
    }

    private fun AnyType.typeNames(): String = when (this) {
        is UnionType -> this.oneOf.map { it.typeName() }.plus("JsonObject").distinct().sorted().joinToString(separator = "|")
        else -> this.typeName()
    }
    private fun AnyType.typeName(): String = if (this.toVrapType().simpleName() != "stdClass") this.toVrapType().simpleName() else "mixed"

    private fun Property.toPhpConstantName(): String {
        return "FIELD_${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this.patternName())}"
    }

    private fun Property.toPhpConstant(): String {

        return """
            |public const ${this.toPhpConstantName()} = '${this.name}';
        """.trimMargin()
    }

    private fun Property.patternName(): String {
        return if (this.isPatternProperty())
            "pattern" + (this.eContainer() as ObjectType).properties.indexOf(this)
        else
            this.name
    }

    private fun ObjectType.toBeanFields() = this.allProperties
            .filter { !it.isPatternProperty() }.joinToString(separator = "\n\n") { it.toPhpField() }

    private fun ObjectType.setters(): String {
        val discriminator = this.discriminatorProperty()
        return this.allProperties
                .filter { property -> property != discriminator }
                .filter { it.getAnnotation("deprecated") == null }
                .filter { !it.isPatternProperty() }.joinToString(separator = "\n\n") { it.setter() }
    }

    private fun ObjectType.getters() = this.allProperties
            .filter { it.getAnnotation("deprecated") == null }
            .filter { !it.isPatternProperty() }.joinToString(separator = "\n\n") { it.getter() }

    private fun ObjectType.unionGetters() = this.allProperties
            .filter { it.type is UnionType && !it.isPatternProperty() }.flatMap { (it.type as UnionType).oneOf.distinctBy { t -> it.unionFunctionName(t) } .map { t -> it.unionGetter(t) } }.joinToString(separator = "\n\n")

    private fun ObjectType.serializer(): String {
        val dtProperties = this.allProperties
                .filter { it.getAnnotation("deprecated") == null }
                .filter { it.type is DateTimeOnlyType || it.type is DateTimeType || it.type is DateOnlyType || it.type is TimeOnlyType }

        if (dtProperties.isNotEmpty()) {
            return """
                |public function jsonSerialize()
                |{
                |    $!data = $!this->toArray();
                |    <<${dtProperties.joinToString(separator = "\n\n") { it.serializer() }}>>
                |    return (object) $!data;
                |}
            """.trimMargin()
        } else {
            return ""
        }
    }

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
            |public function set${this.name.capitalize()}($t $${this.name}): void
            |{
            |    $!this->${this.name} = $${this.name};
            |}
        """.trimMargin()
    }

    private fun Property.getter(): String {
        return when (this.type) {
            is UnionType ->
                """
                    |/**${if (this.type.description?.value?.isNotBlank() == true) """
                    | {{${this.type.toPhpComment()}}}
                    | *""" else ""}
                    | * @return ?mixed
                    | */
                    |public function get${this.name.capitalize()}()
                    |{
                    |    if (is_null($!this->${this.name})) {
                    |        /** @psalm-var ?mixed $!data */
                    |        $!data = $!this->raw(self::${this.toPhpConstantName()});
                    |        if (is_null($!data)) {
                    |            return null;
                    |        }
                    |        $!this->${this.name} = $!data;
                    |    }
                    |
                    |    return $!this->${this.name};
                    |}
               """.trimMargin().keepCurlyIndent()
            else ->
                """
                    |/**${if (this.type.description?.value?.isNotBlank() == true) """
                    | {{${this.type.toPhpComment()}}}
                    | *""" else ""}
                    | * @return null|${this.type.typeNames()}
                    | */
                    |public function get${this.name.capitalize()}()
                    |{
                    |    if (is_null($!this->${this.name})) {
                    |        <<${this.mapper()}>>
                    |    }
                    |
                    |    return $!this->${this.name};
                    |}
               """.trimMargin().keepCurlyIndent()
        }
    }

    private fun Property.unionFunctionName(type: AnyType): String {
        val typeName = when (type.toVrapType()) {
            is VrapDateTimeType -> type.name
            else -> if (type.toVrapType().simpleName() != "stdClass") type.toVrapType().simpleName() else "JsonObject"
        }
        return "get${this.name.capitalize()}As${StringCaseFormat.UPPER_CAMEL_CASE.apply(typeName)}"
    }

    private fun Property.unionGetter(type: AnyType): String {
        val docTypeName = type.typeName()
        return """
                |/**${if (this.type.description?.value?.isNotBlank() == true) """
                | {{${this.type.toPhpComment()}}}
                | *""" else ""}
                | * @return null|$docTypeName
                | */
                |public function ${this.unionFunctionName(type)}()
                |{
                |    <<${this.mapper(type, "return")}>>
                |}
        """.trimMargin().keepCurlyIndent()
    }

    private fun Property.s(type: AnyType): String {
        val typeName = type.typeName()
        val t = type.toVrapType()
        return when (t) {
            is VrapObjectType -> """!$!this->${this.name} instanceof $typeName"""
            is VrapArrayType -> """!$!this->${this.name} instanceof ${typeName}"""
            is VrapEnumType -> """!is_string($!this->${this.name})"""
            is VrapDateTimeType -> """!$!this->${this.name} instanceof DateTimeImmutable"""
            else ->  when (t) {
                PhpBaseTypes.integerType -> """!is_int($!this->${this.name})"""
                PhpBaseTypes.stringType -> """!is_string($!this->${this.name})"""
                PhpBaseTypes.doubleType -> """!is_float($!this->${this.name})"""
                PhpBaseTypes.booleanType -> """!is_bool($!this->${this.name})"""
                else -> """!is_scalar($!this->${this.name})"""
            }
        }
    }


    private fun ObjectType.patternGetter(): String {
        if (this.allProperties.none { it.isPatternProperty() }) {
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
            |    <<${this.properties.filter { it.isPatternProperty() }.joinToString("\n") { it.patternGet() }}>>
            |
            |    return $!data;
            |}
        """.trimMargin().forcedLiteralEscape()
    }

    private fun Property.patternGet(): String {
        val defineObject = this.eContainer().toVrapType()
        return """
            |if (preg_match(${defineObject.simpleName()}::${this.toPhpConstantName()}, $!key) === 1) {
            |    <<${this.patternMapper()}>>
            |}
        """.trimMargin().forcedLiteralEscape()
    }

    private fun Property.serializer(): String {
        val type = this.type
        val defineObject = this.eContainer().toVrapType()
        val indexName = "${defineObject.simpleName()}::${this.toPhpConstantName()}"
        return when(type) {
            is DateTimeType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |    $!data[$indexName] = $!data[$indexName]->setTimeZone(new \DateTimeZone('UTC'))->format('c');
                |}
            """.trimMargin()
            is DateOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |    $!data[$indexName] = $!data[$indexName]->format('Y-m-d');
                |}
            """.trimMargin()
            is TimeOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |    $!data[$indexName] = $!data[$indexName]->format('H:i:s.u');
                |}
            """.trimMargin()
            is DateTimeOnlyType -> """
                |if (isset($!data[$indexName]) && $!data[$indexName] instanceof \DateTimeImmutable) {
                |    $!data[$indexName] = $!data[$indexName]->setTimeZone(new \DateTimeZone('UTC'))->format('c');
                |}
            """.trimMargin()
            else -> return ""
        }.escapeAll()
    }


    private fun Property.patternMapper():String {
        val type = this.type
        return when(type) {
            is ObjectType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var stdClass $!data */
                        |return JsonObjectModel::of($!data);
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var stdClass|array<string, mixed> $!data */
                        |${if (type.discriminator.isNullOrBlank()) "" else "$!className = ${type.toVrapType().simpleName()}Model::resolveDiscriminatorClass($!data);"}
                        |return ${if (type.discriminator.isNullOrBlank()) "${type.toVrapType().simpleName()}Model" else "$!className"}::of($!data);
                    """.trimMargin()
                }
            is ArrayType ->
                if (type.toVrapType().isScalar()) {
                    """
                        |/** @psalm-var list<stdClass> $!data */
                        |return $!data;
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var list<stdClass> $!data */
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
                    PhpBaseTypes.dateOnlyType -> """
                        |/** @psalm-var string $!data */
                        |$!data = DateTimeImmutable::createFromFormat(MapperFactory::DATE_FORMAT, $!data);
                        |if ($!data === false) {
                        |    return null;
                        |}
                        |return $!data;
                    """.trimMargin()
                    PhpBaseTypes.timeOnlyType -> """
                        |/** @psalm-var string $!data */
                        |$!data = DateTimeImmutable::createFromFormat(MapperFactory::TIME_FORMAT, $!data);
                        |if ($!data === false) {
                        |    return null;
                        |}
                        |return $!data;
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

    private fun Property.mapper() = this.mapper(this.type)

    private fun Property.mapper(type: AnyType, assignment: String = "$!this->${this.name} = "):String {
        val vrapType = type.toVrapType()
        return when(type) {
            is ObjectType ->
                if (vrapType.isScalar()) {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(self::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$assignment JsonObjectModel::of($!data);
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(self::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |${if (type.discriminator.isNullOrBlank()) "" else "$!className = ${type.toVrapType().simpleName()}Model::resolveDiscriminatorClass($!data);"}
                        |$assignment ${if (type.discriminator.isNullOrBlank()) "${type.toVrapType().simpleName()}Model" else "$!className"}::of($!data);
                    """.trimMargin()
                }
            is ArrayType ->
                when (type.items.toVrapType()) {
                    PhpBaseTypes.timeOnlyType,
                    PhpBaseTypes.dateOnlyType,
                    PhpBaseTypes.dateTimeType -> """
                                |/** @psalm-var ?list<string> $!data */
                                |$!data = $!this->raw(self::${this.toPhpConstantName()});
                                |if (is_null($!data)) {
                                |    return null;
                                |}
                                |$assignment new DateTimeImmutableCollection($!data);
                            """.trimMargin()
                    else ->
                        if (vrapType.isScalar()) {
                            """
                                |/** @psalm-var ?list<mixed> $!data */
                                |$!data = $!this->raw(self::${this.toPhpConstantName()});
                                |if (is_null($!data)) {
                                |    return null;
                                |}
                                |$assignment $!data;
                            """.trimMargin()
                        } else {
                            """
                                |/** @psalm-var ?list<stdClass> $!data */
                                |$!data = $!this->raw(self::${this.toPhpConstantName()});
                                |if (is_null($!data)) {
                                |    return null;
                                |}
                                |$assignment ${if (type.items?.isScalar() != true) "${type.toVrapType().simpleName()}::fromArray($!data)" else "$!data"};
                            """.trimMargin()
                        }
                }
            is UnionType ->
                if (vrapType.isScalar()) {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(self::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$assignment JsonObjectModel::of($!data);
                    """.trimMargin()
                } else {
                    """
                        |/** @psalm-var stdClass|array<string, mixed>|null $!data */
                        |$!data = $!this->raw(self::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$assignment ${type.toVrapType().simpleName()}Model::of($!data);
                    """.trimMargin()
                }
            else ->
                when (vrapType) {
                    is VrapEnumType -> """
                       |/** @psalm-var ?string $!data */
                       |$!data = $!this->raw(self::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$assignment (string) $!data;
                    """.trimMargin()
                    is VrapDateTimeType ->
                        when (vrapType) {
                            PhpBaseTypes.timeOnlyType -> """
                               |/** @psalm-var ?string $!data */
                               |$!data = $!this->raw(self::${this.toPhpConstantName()});
                               |if (is_null($!data)) {
                               |    return null;
                               |}
                               |$!data = DateTimeImmutable::createFromFormat(MapperFactory::TIME_FORMAT, $!data);
                               |if (false === $!data) {
                               |    return null;
                               |}
                               |$assignment $!data;
                            """.trimMargin()
                            PhpBaseTypes.dateOnlyType -> """
                               |/** @psalm-var ?string $!data */
                               |$!data = $!this->raw(self::${this.toPhpConstantName()});
                               |if (is_null($!data)) {
                               |    return null;
                               |}
                               |$!data = DateTimeImmutable::createFromFormat(MapperFactory::DATE_FORMAT, $!data);
                               |if (false === $!data) {
                               |    return null;
                               |}
                               |$assignment $!data;
                            """.trimMargin()
                            else -> """
                               |/** @psalm-var ?string $!data */
                               |$!data = $!this->raw(self::${this.toPhpConstantName()});
                               |if (is_null($!data)) {
                               |    return null;
                               |}
                               |$!data = DateTimeImmutable::createFromFormat(MapperFactory::DATETIME_FORMAT, $!data);
                               |if (false === $!data) {
                               |    return null;
                               |}
                               |$assignment $!data;
                            """.trimMargin()
                        }
                    PhpBaseTypes.integerType ->  """
                       |/** @psalm-var ?int $!data */
                       |$!data = $!this->raw(self::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$assignment (int) $!data;
                    """.trimMargin()
                    PhpBaseTypes.stringType ->  """
                       |/** @psalm-var ?string $!data */
                       |$!data = $!this->raw(self::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$assignment (string) $!data;
                    """.trimMargin()
                    PhpBaseTypes.doubleType ->  """
                       |/** @psalm-var ?float $!data */
                       |$!data = $!this->raw(self::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$assignment (float) $!data;
                    """.trimMargin()
                    PhpBaseTypes.booleanType ->  """
                       |/** @psalm-var ?bool $!data */
                       |$!data = $!this->raw(self::${this.toPhpConstantName()});
                       |if (is_null($!data)) {
                       |    return null;
                       |}
                       |$assignment (bool) $!data;
                    """.trimMargin()

                    else -> """
                        |/** @psalm-var mixed $!data */
                        |$!data = $!this->raw(self::${this.toPhpConstantName()});
                        |if (is_null($!data)) {
                        |    return null;
                        |}
                        |$assignment $!data;
                    """.trimMargin()
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
            |   <<${this.namedSubTypes().filterIsInstance<ObjectType>().map { "'${it.discriminatorValue}' => ${it.toVrapType().simpleName()}Model::class," }.sorted().joinToString(separator = "\n")}>>
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
            |       /** @psalm-var string $!discriminatorValue */
            |       $!discriminatorValue = $!value->$!fieldName;
            |       if (isset(self::$!discriminatorClasses[$!discriminatorValue])) {
            |            return self::$!discriminatorClasses[$!discriminatorValue];
            |       }
            |   }
            |   if (is_array($!value) && isset($!value[$!fieldName])) {
            |       /** @psalm-var string $!discriminatorValue */
            |       $!discriminatorValue = $!value[$!fieldName];
            |       if (isset(self::$!discriminatorClasses[$!discriminatorValue])) {
            |            return self::$!discriminatorClasses[$!discriminatorValue];
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

