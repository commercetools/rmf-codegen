package io.vrap.codegen.languages.csharp.predicates;

import io.vrap.codegen.languages.csharp.extensions.CsharpEObjectTypeExtensions
import io.vrap.codegen.languages.csharp.extensions.CsharpObjectTypeExtensions
import io.vrap.codegen.languages.csharp.extensions.toCsharpPackage
import io.vrap.codegen.languages.csharp.extensions.toCsharpVType
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat

class CsharpQueryPredicateRenderer constructor(val basePackage: String, override val vrapTypeProvider: VrapTypeProvider) : CsharpObjectTypeExtensions,
    CsharpEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toCsharpVType() as VrapObjectType

        val implements = emptyList<String>()
            .plus(
                when (val ex = type.getAnnotation("csharp-query-implements") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            )
            .filterNotNull()

        val content = """
            |using System;
            |using ${basePackage.toCsharpPackage()}.Predicates.Query;
            |using commercetools.Base.Models;
            |
            |// ReSharper disable CheckNamespace
            |namespace ${vrapType.`package`.predicatePackage()}
            |{
            |    ${if (type.markDeprecated()) """
            |    [Obsolete]""" else ""}
            |    public partial class ${vrapType.builderDslName()} ${if (implements.isNotEmpty()) { ": ${implements.joinToString(separator = ", ")}" } else ""}
            |    {
            |        public ${vrapType.builderDslName()}()
            |        {
            |        }
            |
            |        public static ${vrapType.builderDslName()} Of()
            |        {
            |            return new ${vrapType.builderDslName()}();
            |        }
            |
            |        <${type.allProperties.filterNot { it.deprecated() }.filterNot { it.isPatternProperty() }.joinToString("\n") { it.toBuilderDsl(type) }}>
            |
            |        <${type.namedSubTypes().filterIsInstance<ObjectType>().joinToString("\n") { it.toBuilderDsl("As${it.subtypeName()}", vrapType) }}>
            |    }
            |}
        """.trimMargin().keepIndentation()

        val relativePath = vrapType.queryCsharpClassRelativePath(false).predicatePackage().replace(basePackage.replace(".", "/"), "").trimStart('/')

        return TemplateFile(
            relativePath = relativePath,
            content = content
        )
    }

    fun VrapType.queryCsharpClassRelativePath(isInterface: Boolean = false): String {
        var packageName = "";
        var simpleClassName = ""

        if (this is VrapObjectType) {
            packageName = this.`package`
            simpleClassName = this.simpleClassName
        } else if (this is VrapEnumType) {
            packageName = this.`package`
            simpleClassName = this.simpleClassName
        }

        var namespaceDir = packageName.replace("Models", "").toNamespaceDir()
        var fileName = if (isInterface) "I${simpleClassName}" else simpleClassName

        return "${namespaceDir}.${fileName}QueryBuilderDsl".replace(".", "/") + ".cs"
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
                |    Func<${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}, CombinationQueryPredicate<${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}>> fn)
                |{
                |    return new CombinationQueryPredicate<${baseType.builderDslName()}>(fn.Invoke(${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}.Of()),
                |        ${baseType.builderDslName()}.Of);
                |}
            """.trimMargin().escapeAll()

    }
    private fun String.predicatePackage() : String {
        return this.replace(".Models.", ".Predicates.Query.")
    }

    private fun Property.toBuilderDsl(type: ObjectType) : String {
        val vrapType = type.toVrapType() as VrapObjectType
        val propType = this.type.toVrapType()
        return propType.toBuilderDsl(this.name, this.type, vrapType);
    }

    private fun VrapType.toBuilderDsl(propertyName: String, propertyType: AnyType, vrapType: VrapObjectType, isItemType: Boolean = false) : String {
        val methodName = propertyName.upperCamelCase()

        if (this.simpleName() == "DateTime" || this.simpleName() == "Date" || this.simpleName() == "TimeSpan" || this.simpleName() == Object::class.java.simpleName) {
            return """
                |public IComparisonPredicateBuilder<${vrapType.builderDslName()}, ${propertyType.comparisonValue()}> $methodName()
                |{
                |    return new ComparisonPredicateBuilder<${vrapType.builderDslName()}, ${propertyType.comparisonValue()}>(BinaryQueryPredicate.Of().Left(new ConstantQueryPredicate("${propertyName}")),
                |    p => new CombinationQueryPredicate<${vrapType.builderDslName()}>(p, ${vrapType.builderDslName()}.Of),
                |    PredicateFormatter.Format);
                |}
            """.trimMargin().escapeAll()
        }
        val builders = mutableListOf<String>()
        if (this is VrapObjectType && this.simpleName() == "IGeoJson") {
            builders.add("""
                |public GeoJsonPredicateBuilder<${vrapType.builderDslName()}> $methodName()
                |{
                |    return new GeoJsonPredicateBuilder<${vrapType.builderDslName()}>(BinaryQueryPredicate.Of().Left(new ConstantQueryPredicate("${propertyName}")),
                |            p => new CombinationQueryPredicate<${vrapType.builderDslName()}>(p, ${vrapType.builderDslName()}.Of));
                |}
            """.trimMargin())
        }
        if (this is VrapObjectType && isItemType) {
            builders.add("""
                |public ICollectionPredicateBuilder<${vrapType.builderDslName()}> $methodName()
                |{
                |    return new CollectionPredicateBuilder<${vrapType.builderDslName()}>(BinaryQueryPredicate.Of().Left(new ConstantQueryPredicate("${propertyName}")),
                |            p => new CombinationQueryPredicate<${vrapType.builderDslName()}>(p, ${vrapType.builderDslName()}.Of));
                |}
            """.trimMargin())
        }
        val comparisonPredicate = if (isItemType) {
            propertyType.collectionPredicate()
        } else {
            "ComparisonPredicateBuilder"
        }
        val comparisonValue = if (isItemType) {
            propertyType.comparisonValue()
        } else {
            propertyType.comparisonValue()
        }
        return when (this) {
            is VrapArrayType -> this.itemType.toBuilderDsl(propertyName, propertyType, vrapType, true)
            is VrapObjectType -> """
                |public CombinationQueryPredicate<${vrapType.builderDslName()}> $methodName(
                |    Func<${this.`package`.predicatePackage()}.${this.builderDslName()}, CombinationQueryPredicate<${this.`package`.predicatePackage()}.${this.builderDslName()}>> fn)
                |{
                |    return new CombinationQueryPredicate<${vrapType.builderDslName()}>(ContainerQueryPredicate.Of()
                |        .Parent(ConstantQueryPredicate.Of().Constant("${propertyName}"))
                |        .Inner(fn.Invoke(${this.`package`.predicatePackage()}.${this.builderDslName()}.Of())),
                |        ${vrapType.builderDslName()}.Of);
                |}
                |${builders.joinToString("\n")}
            """.trimMargin().escapeAll()
            else -> """
                |public I$comparisonPredicate<${vrapType.builderDslName()}, $comparisonValue> $methodName()
                |{
                |    return new $comparisonPredicate<${vrapType.builderDslName()}, $comparisonValue>(BinaryQueryPredicate.Of().Left(new ConstantQueryPredicate("${propertyName}")),
                |    p => new CombinationQueryPredicate<${vrapType.builderDslName()}>(p, ${vrapType.builderDslName()}.Of),
                |    PredicateFormatter.Format);
                |}
            """.trimMargin().escapeAll()
        }
    }

    private fun AnyType.collectionPredicate() : String {
        if (this.isInlineType && this.type != null) {
            return this.type.collectionPredicate()
        }
        return when(this) {
            is BooleanType -> "ComparisonPredicateBuilder"
            else -> "ComparableCollectionPredicateBuilder"
        }
    }

    private fun AnyType.comparisonValue() : String {
        return when(this) {
            is BooleanType -> "bool"
            is DateTimeType -> "DateTime"
            is TimeOnlyType -> "TimeSpan"
            is DateOnlyType -> "Date"
            is NumberType -> when (this.format) {
                NumberFormat.DOUBLE -> "decimal"
                NumberFormat.FLOAT -> "decimal"
                else -> "long"
            }
            is IntegerType -> "long"
            is StringType -> "string"
            else -> "string"
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
