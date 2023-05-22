package io.vrap.codegen.languages.javalang.client.builder.predicates;

import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.lang.model.SourceVersion

class JavaQueryPredicateRenderer constructor(val basePackage: String, override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions,
    JavaEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val implements = emptyList<String>()
            .plus(
                when (val ex = type.getAnnotation("java-query-implements") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            )
            .filterNotNull()

        val content = """
            |package ${vrapType.`package`.predicatePackage()};
            |
            |import ${basePackage.toJavaPackage()}.predicates.query.*;
            |
            |import java.util.function.Function;
            |
            |${if (type.markDeprecated()) """
            |@Deprecated""" else ""}
            |public class ${vrapType.builderDslName()} ${if (implements.isNotEmpty()) { "implements ${implements.joinToString(separator = ", ")}" } else ""} {
            |    public ${vrapType.builderDslName()}() {
            |    }
            |
            |    public static ${vrapType.builderDslName()} of() {
            |        return new ${vrapType.builderDslName()}();
            |    }
            |
            |    <${type.allProperties.filterNot { it.isPatternProperty() }.joinToString("\n") { it.toBuilderDsl(type) }}>
            |    
            |    <${type.namedSubTypes().filterIsInstance<ObjectType>().joinToString("\n") { it.toBuilderDsl("as${it.subtypeName()}", vrapType) }}>
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}".replace(".", "/") + ".java",
            content = content
        )
    }

    private fun AnyType.subtypeName(): String {
        if (this is ObjectType && this.discriminatorValue != null) {
            return StringCaseFormat.UPPER_CAMEL_CASE.apply(this.discriminatorValue)
        }
        return this.name.firstUpperCase()
    }

    private fun ObjectType.toBuilderDsl(methodName: String, baseType: VrapObjectType): String {
        val vrapType = this.toVrapType() as VrapObjectType
        return """
                |public CombinationQueryPredicate<${baseType.builderDslName()}> $methodName(
                |    Function<${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}, CombinationQueryPredicate<${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}>> fn) {
                |    return new CombinationQueryPredicate<>(fn.apply(${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}.of()),
                |        ${baseType.builderDslName()}::of);
                |}
            """.trimMargin().escapeAll()

    }
    private fun String.predicatePackage() : String {
        return this.replace(".models.", ".predicates.query.")
    }

    private fun Property.toBuilderDsl(type: ObjectType) : String {
        val vrapType = type.toVrapType() as VrapObjectType
        val propType = this.type.toVrapType()
        return propType.toBuilderDsl(this.name, this.type, vrapType);
    }

    private fun VrapType.toBuilderDsl(propertyName: String, propertyType: AnyType, vrapType: VrapObjectType, isItemType: Boolean = false) : String {
        var methodName = propertyName
        if(SourceVersion.isKeyword(methodName)) {
            methodName = "_$methodName"
        }

        if (this.simpleName() == ZonedDateTime::class.simpleName || this.simpleName() == LocalDate::class.simpleName || this.simpleName() == Object::class.java.simpleName) {
            return """
                |public ${propertyType.comparisonPredicate()}<${vrapType.builderDslName()}> $methodName() {
                |    return new ${propertyType.comparisonPredicate()}<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${propertyName}")),
                |    p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin().escapeAll()
        }
        val builders = mutableListOf<String>()
        if (this is VrapObjectType && this.simpleName() == "GeoJson") {
            builders.add("""
                |public GeoJsonPredicateBuilder<${vrapType.builderDslName()}> $methodName() {
                |    return new GeoJsonPredicateBuilder<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${propertyName}")),
                |            p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin())
        }
        if (this is VrapObjectType && isItemType) {
            builders.add("""
                |public CollectionPredicateBuilder<${vrapType.builderDslName()}> $methodName() {
                |    return new CollectionPredicateBuilder<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${propertyName}")),
                |            p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin())
        }
        val comparisonPredicate = if (isItemType) {
            propertyType.collectionPredicate()
        } else {
            propertyType.comparisonPredicate()
        }
        return when (this) {
            is VrapArrayType -> this.itemType.toBuilderDsl(propertyName, propertyType, vrapType, true)
            is VrapObjectType -> """
                |public CombinationQueryPredicate<${vrapType.builderDslName()}> $methodName(
                |    Function<${this.`package`.predicatePackage()}.${this.builderDslName()}, CombinationQueryPredicate<${this.`package`.predicatePackage()}.${this.builderDslName()}>> fn) {
                |    return new CombinationQueryPredicate<>(ContainerQueryPredicate.of()
                |        .parent(ConstantQueryPredicate.of().constant("${propertyName}"))
                |        .inner(fn.apply(${this.`package`.predicatePackage()}.${this.builderDslName()}.of())),
                |        ${vrapType.builderDslName()}::of);
                |}
                |${builders.joinToString("\n")}
            """.trimMargin().escapeAll()
            else -> """
                |public $comparisonPredicate<${vrapType.builderDslName()}> $methodName() {
                |    return new $comparisonPredicate<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${propertyName}")),
                |    p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin().escapeAll()
        }
    }

    private fun AnyType.collectionPredicate() : String {
        if (this.isInlineType && this.type != null) {
            return this.type.collectionPredicate()
        }
        return when(this) {
            is BooleanType -> "BooleanComparisonPredicateBuilder"
            is DateTimeType -> "DateTimeCollectionPredicateBuilder"
            is TimeOnlyType -> "TimeCollectionPredicateBuilder"
            is DateOnlyType -> "DateCollectionPredicateBuilder"
            is NumberType -> when (this.format) {
                NumberFormat.DOUBLE -> "DoubleCollectionPredicateBuilder"
                NumberFormat.FLOAT -> "DoubleCollectionPredicateBuilder"
                else -> "LongCollectionPredicateBuilder"
            }
            is IntegerType -> "LongCollectionPredicateBuilder"
            is StringType -> "StringCollectionPredicateBuilder"
            else -> "StringCollectionPredicateBuilder"
        }
    }
    private fun AnyType.comparisonPredicate() : String {
        return when(this) {
            is BooleanType -> "BooleanComparisonPredicateBuilder"
            is DateTimeType -> "DateTimeComparisonPredicateBuilder"
            is TimeOnlyType -> "TimeComparisonPredicateBuilder"
            is DateOnlyType -> "DateComparisonPredicateBuilder"
            is NumberType -> when (this.format) {
                NumberFormat.DOUBLE -> "DoubleComparisonPredicateBuilder"
                NumberFormat.FLOAT -> "DoubleComparisonPredicateBuilder"
                else -> "LongComparisonPredicateBuilder"
            }
            is IntegerType -> "LongComparisonPredicateBuilder"
            is StringType -> "StringComparisonPredicateBuilder"
            else -> "StringComparisonPredicateBuilder"
        }
    }

    private fun AnyType.isAnyType(): Boolean {
        return this.isScalar().not() && this !is ObjectType && this !is ArrayType && this !is UnionType && this !is IntersectionType && this !is NilType
    }

    private fun AnyType.isScalar(): Boolean {
        return when(this) {
            is StringType -> true
            is IntegerType -> true
            is NumberType -> true
            is BooleanType -> true
            is DateTimeType -> true
            is DateOnlyType -> true
            is DateTimeOnlyType -> true
            is TimeOnlyType -> true
            is ArrayType -> this.items == null || this.items.isScalar()
            else -> false
        }
    }


    private fun VrapObjectType.builderDslName() : String {
        return this.simpleClassName + "QueryBuilderDsl"
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

}
