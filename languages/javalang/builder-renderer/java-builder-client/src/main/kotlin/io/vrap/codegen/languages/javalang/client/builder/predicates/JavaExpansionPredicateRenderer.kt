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
import io.vrap.rmf.codegen.rendering.utils.keepAngleIndent
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import javax.lang.model.SourceVersion

class JavaExpansionPredicateRenderer constructor(val basePackage: String, override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions,
    JavaEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val implements = listOf("ExpansionDsl")
            .plus(
                when (val ex = type.getAnnotation("java-expansion-implements") ) {
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
            |import com.commercetools.api.predicates.expansion.ExpansionDsl;
            |
            |import static com.commercetools.api.predicates.expansion.ExpansionUtil.appendOne;
            |
            |import java.util.Collections;
            |import java.util.List;
            |
            |${if (type.markDeprecated()) """
            |@Deprecated""" else ""}
            |public class ${vrapType.builderDslName()} ${if (implements.isNotEmpty()) { "implements ${implements.joinToString(separator = ", ")}" } else ""} {
            |
            |    private final List<String> path;
            |    
            |    private ${vrapType.builderDslName()}(final List<String> path) {
            |        this.path = path;
            |    }
            |
            |    public static ${vrapType.builderDslName()} of() {
            |        return new ${vrapType.builderDslName()}(Collections.emptyList());
            |    }
            |
            |    public static ${vrapType.builderDslName()} of(final List<String> path) {
            |        return new ${vrapType.builderDslName()}(path);
            |    }
            |    
            |    @Override
            |    public List<String> getPath() {
            |        return path;
            |    }
            |
            |    <<${type.allProperties.asSequence()
                    .filterNot { it.deprecated() }
                    .filterNot { it.type.isScalar() }
                    .filterNot { it.isPatternProperty() }
                    .filter { (it.type.toVrapType() is VrapObjectType && it.type.toVrapType().fullClassName() != "java.lang.Object") || (it.type.toVrapType() is VrapArrayType && (it.type.toVrapType() as VrapArrayType).itemType is VrapObjectType && (it.type.toVrapType() as VrapArrayType).itemType.fullClassName() != "java.lang.Object") }
                    .joinToString("\n") { it.toBuilderDsl(type) }}>>
            |}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(
            relativePath = "${vrapType.`package`.predicatePackage()}.${vrapType.builderDslName()}".replace(".", "/") + ".java",
            content = content
        )
    }

    private fun String.predicatePackage() : String {
        return this.replace(".models.", ".predicates.expansion.")
    }

    private fun Property.toBuilderDsl(type: ObjectType) : String {
        val vrapType = type.toVrapType()
        val propType = this.type.toVrapType()

        return propType.toBuilderDsl(this.name, this.type, vrapType);
    }

    private fun VrapType.toBuilderDsl(propertyName: String, propertyType: AnyType, vrapType: VrapType, isItemType: Boolean = false) : String {
        var methodName = propertyName
        if(SourceVersion.isKeyword(methodName)) {
            methodName = "_$methodName"
        }

        if (this is VrapArrayType) {
            return """
                |public ${this.builderDslName(true)} $methodName() {
                |    return ${this.builderDslName(true)}.of(appendOne(path, "$propertyName[*]"));
                |}
                |public ${this.builderDslName(true)} $methodName(long index) {
                |    return ${this.builderDslName(true)}.of(appendOne(path, "$propertyName[" + index + "]"));
                |}
            """.trimMargin()
        }
        return """
            |public ${(this as VrapObjectType).builderDslName(true)} $methodName() {
            |   return ${this.builderDslName(true)}.of(appendOne(path, "$propertyName"));
            |}
        """.trimMargin()
    }

    private fun VrapArrayType.builderDslName(fqcn: Boolean = false) : String {
        return "${if (fqcn) this.itemType.fullClassName().predicatePackage() else this.itemType.simpleName()}ExpansionBuilderDsl"
    }

    private fun VrapObjectType.builderDslName(fqcn: Boolean = false) : String {
        return "${if (fqcn) this.fullClassName().predicatePackage() else this.simpleName()}ExpansionBuilderDsl"
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun AnyType.isAnyType(): Boolean {
        return this.isScalar().not() && this !is ObjectType && this !is ArrayType && this !is UnionType && this !is IntersectionType && this !is NilType
    }

    private fun AnyType.isScalar(): Boolean {
        return when (this) {
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

}
