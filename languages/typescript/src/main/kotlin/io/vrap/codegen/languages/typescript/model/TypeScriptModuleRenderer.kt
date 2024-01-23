package io.vrap.codegen.languages.typescript.model

import io.vrap.codegen.languages.extensions.*
import io.vrap.codegen.languages.typescript.deprecated
import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.codegen.languages.typescript.toTsComment
import io.vrap.codegen.languages.typescript.toTsCommentList
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*

class TypeScriptModuleRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllAnyTypes val allAnyTypes: List<AnyType>) : TsObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.filter { it is ObjectType || (it is StringType && it.pattern == null) }
                .groupBy {
                    it.moduleName()
                }
            .map { entry: Map.Entry<String, List<AnyType>> ->
                buildModule(entry.key, entry.value) }
            .toList()
    }

    private fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {
        var sortedTypes = types.filter { it !is UnionType }.filterNot{it.deprecated()}.sortedByTopology(AnyType::getSuperTypes)
        val content = """
           |$tsGeneratedComment
           |
           |${sortedTypes.getImportsForModule(moduleName)}
           |
           |${sortedTypes.map { it.renderAnyType() }.joinToString(separator = "\n")}
       """.trimMargin().keepIndentation()

        return TemplateFile(content, moduleName + ".ts")

    }

    private fun AnyType.renderAnyType(): String {
        return when (this) {
            is ObjectType -> this.renderObjectType()
            is StringType -> this.renderStringType()
            else -> throw IllegalArgumentException("unhandled case ${this.javaClass}")
        }
    }

    private fun ObjectType.renderObjectType(): String {
        return if (discriminator() != null) {
            if (discriminatorValue === null) {
                """
                |<${toTsComment().escapeAll()}>
                |export type ${name} =
                |  <${subTypes.plus(subTypes.flatMap { it.subTypes }).distinctBy { it.name }.filter { !it.isInlineType }.filterNot { it.deprecated() }.map { it.renderTypeExpr() }.sorted().joinToString(" |\n")}>
                |;
                """.trimMargin()
            } else {
                """
                |<${toTsComment().escapeAll()}>
                |export interface ${name} {
                |  readonly ${discriminator()}: "${discriminatorValue}";
                |  <${renderPatternSpec()}>
                |  <${renderPropertyDecls(true)}>
                |}
                """.trimMargin()
            }
        } else {
            if (this.namedSubTypes().isNotEmpty()) {
                """
                    |<${this.toTsComment().escapeAll()}>
                    |export interface ${name} ${renderExtendsExpr()} {
                    |  <${renderPatternSpec()}>
                    |  <${renderPropertyDecls(false)}>
                    |}
                    |export type _${name} = ${name} | ${this.namedSubTypes().joinToString(" | ") { it.subTypeName() }}
                """.trimMargin()
            } else {
                """
                    |<${this.toTsComment().escapeAll()}>
                    |export interface ${name} ${renderExtendsExpr()} {
                    |  <${renderPatternSpec()}>
                    |  <${renderPropertyDecls(false)}>
                    |}
                """.trimMargin()
            }
        }
    }

    private fun AnyType.subTypeName(): String {
        val type = if (this.isInlineType()) this.type else this;
        if ((type is ObjectType) && type.discriminator() == null && type.namedSubTypes().isNotEmpty()) {
            return "_${this.name}"
        }
        return this.name
    }

    /**
     * Renders the optional typescript extends expression of this types type.
     *
     * @return the rendered extends expression
     */
    fun ObjectType.renderExtendsExpr(): String {
        return type?.toVrapType()?.simpleTSName()?.let { "extends $it " } ?: ""
    }

    /**
     * Renders the properties of this object type as typescript property declarations.
     * Excludes pattern proprties and discriminator properties.
     *
     * @param all if true renders all inherited properties, if false renders only direct properties
     * @return the rendered property type declarations
     */
    fun ObjectType.renderPropertyDecls(all: Boolean): String {
        val renderProperties = if (all) allProperties else properties
        return renderProperties
            .filter { !it.isPatternProperty() && it.name != discriminator() }
            .filterNot { it.deprecated() }
            .map {
                val comments = it.type.toTsCommentList().plus(it.markDeprecated())
                val comment = if (comments.isEmpty()) "" else comments.joinToString("\n*\t", "/**\n*\t", "\n*/").escapeAll()
                val optional = if (it.required) "" else "?"
                """
                    |<${comment}>
                    |readonly ${it.name}${optional}: ${it.type.renderTypeExpr(useSubTypes = true)}
                    """.trimMargin()
            }
            .joinToString(";\n")
    }

    fun Property.markDeprecated(): String {
        val anno = this.getAnnotation("markDeprecated", true)
        if (anno != null && (anno.value as BooleanInstance).value == true) {
            return "@deprecated";
        }
        return "";
    }

    /**
     * Renders the typescript type expression for this types type.
     *
     * @param the typescript expression
     */
    fun AnyType.renderTypeExpr(useSubTypes: Boolean = false): String {
        return when (this) {
            is UnionType -> oneOf.map { it.renderTypeExpr() }.joinToString(" | ")
            is IntersectionType -> allOf.map { it.renderTypeExpr() }.joinToString(" & ")
            is NilType -> "null"
            is ObjectType -> {
                val type = (if (this.type != null && this.isInlineType) this.type else this) as ObjectType
                if (useSubTypes && type.discriminator == null && type.namedSubTypes().isNotEmpty()) {
                    "_${toVrapType().simpleTSName()}"
                } else {
                    toVrapType().simpleTSName()
                }
            }
            else -> toVrapType().simpleTSName()
        }
    }

    private fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType()

        return when (vrapType) {
            is VrapEnumType ->
                """
                |<${this.toTsComment().escapeAll()}>
                |export type ${vrapType.simpleClassName} =
                |   <${this.renderEnumValues()}>;
                """.trimMargin()
            is VrapScalarType -> """
                |export type ${this.name} = ${vrapType.scalarType}
            """.trimMargin()
            else -> ""
        }
    }

    private fun StringType.renderEnumValues(): String = enumValues()
        .map { "'${it}'" }
        .sorted()
        .plus("string")
        .joinToString(" |\n")


    private fun StringType.enumValues() = enum?.filter { it is StringInstance }
        ?.map { (it as StringInstance).value }
        ?.sorted()
        ?.filterNotNull() ?: listOf()

    /**
     * If the handled is a map type then the map args should be specified
     */
    private fun ObjectType.renderPatternSpec(): String? {
        return allProperties
            .filter { it.isPatternProperty() }
            .firstOrNull()
            .let {
                if (it != null) "[key:string]: ${it.type.renderTypeExpr()}" else ""
            }
    }
}
