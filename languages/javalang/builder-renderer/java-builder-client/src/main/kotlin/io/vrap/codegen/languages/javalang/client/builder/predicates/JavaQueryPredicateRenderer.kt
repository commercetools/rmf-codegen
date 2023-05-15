package io.vrap.codegen.languages.javalang.client.builder.predicates;

import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.lang.model.SourceVersion

class JavaQueryPredicateRenderer constructor(val basePackage: String, override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions,
    JavaEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content = """
            |package ${vrapType.`package`.predicatePackage()};
            |
            |import $basePackage.predicates.query.*;
            |
            |import java.util.function.Function;
            |
            |${if (type.markDeprecated()) """
            |@Deprecated""" else ""}
            |public class ${vrapType.builderDslName()} {
            |    public ${vrapType.builderDslName()}() {
            |    }
            |
            |    public static ${vrapType.builderDslName()} of() {
            |        return new ${vrapType.builderDslName()}();
            |    }
            |
            |    <${type.allProperties.filterNot { it.isPatternProperty() }.joinToString("\n") { it.toBuilderDsl(type) }}>
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}".replace(".", "/") + ".java",
            content = content
        )
    }

    private fun String.predicatePackage() : String {
        return this.replace(".models.", ".predicates.query.")
    }

    private fun Property.toBuilderDsl(type: ObjectType) : String {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType
        val propType = this.type.toVrapType()
        return propType.toBuilderDsl(this, vrapType);
    }

    private fun VrapType.toBuilderDsl(property: Property, vrapType: VrapObjectType, isItemType: Boolean = false) : String {
        var propertyName = property.name
        if(SourceVersion.isKeyword(propertyName)) {
            propertyName = "_$propertyName"
        }

        if (this.simpleName() == ZonedDateTime::class.simpleName || this.simpleName() == LocalDate::class.simpleName || this.simpleName() == Object::class.java.simpleName) {
            return """
                |public ${property.type.comparisonPredicate()}<${vrapType.builderDslName()}> $propertyName() {
                |    return new ${property.type.comparisonPredicate()}<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${property.name}")),
                |    p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin().escapeAll()
        }
        val builders = mutableListOf<String>()
        if (this is VrapObjectType && this.simpleName() == "GeoJson") {
            builders.add("""
                |public GeoJsonPredicateBuilder<${vrapType.builderDslName()}> $propertyName() {
                |    return new GeoJsonPredicateBuilder<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${property.name}")),
                |            p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin())
        }
        if (this is VrapObjectType && isItemType) {
            builders.add("""
                |public CollectionPredicateBuilder<${vrapType.builderDslName()}> $propertyName() {
                |    return new CollectionPredicateBuilder<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${property.name}")),
                |            p -> new CombinationQueryPredicate<>(p, ${vrapType.builderDslName()}::of));
                |}
            """.trimMargin())
        }
        val comparisonPredicate = if (isItemType) {
            property.type.collectionPredicate()
        } else {
            property.type.comparisonPredicate()
        }
        return when (this) {
            is VrapArrayType -> this.itemType.toBuilderDsl(property, vrapType, true)
            is VrapObjectType -> """
                |public CombinationQueryPredicate<${vrapType.builderDslName()}> $propertyName(
                |    Function<${this.`package`.predicatePackage()}.${this.builderDslName()}, CombinationQueryPredicate<${this.`package`.predicatePackage()}.${this.builderDslName()}>> fn) {
                |    return new CombinationQueryPredicate<>(ContainerQueryPredicate.of()
                |        .parent(ConstantQueryPredicate.of().constant("${property.name}"))
                |        .inner(fn.apply(${this.`package`.predicatePackage()}.${this.builderDslName()}.of())),
                |        ${vrapType.builderDslName()}::of);
                |}
                |${builders.joinToString("\n")}
            """.trimMargin().escapeAll()
            else -> """
                |public $comparisonPredicate<${vrapType.builderDslName()}> $propertyName() {
                |    return new $comparisonPredicate<>(BinaryQueryPredicate.of().left(new ConstantQueryPredicate("${property.name}")),
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

    private fun VrapObjectType.builderDslName() : String {
        return this.simpleClassName + "QueryBuilderDsl"
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

}
