package io.vrap.codegen.languages.python.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getSuperTypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.sortedByTopology
import io.vrap.codegen.languages.python.tsGeneratedComment
import io.vrap.codegen.languages.python.toTsComment
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*

class PythonModuleRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : PyObjectTypeExtensions, FileProducer {

    @Inject
    @AllAnyTypes
    lateinit var allAnyTypes: MutableList<AnyType>

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
        var sortedTypes = types.filter { it !is UnionType }.sortedByTopology(AnyType::getSuperTypes)
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
                |class ${name}:
                |  <${subTypes.filter { !it.isInlineType }.map { it.renderTypeExpr() }.joinToString(" |\n")}>
                """.trimMargin()
            } else {
                """
                |<${toTsComment().escapeAll()}>
                |class ${name}:
                |  readonly ${discriminator()}: "${discriminatorValue}";
                |  <${renderPatternSpec()}>
                |  <${renderPropertyDecls(true)}>
                """.trimMargin()
            }
        } else {
            """
                |<${this.toTsComment().escapeAll()}>
                |class ${name} ${renderExtendsExpr()}:
                |  <${renderPatternSpec()}>
                |  <${renderPropertyDecls(false)}>
            """.trimMargin()
        }
    }

    /**
     * Renders the optional typescript extends expression of this types type.
     *
     * @return the rendered extends expression
     */
    fun ObjectType.renderExtendsExpr(): String {
        return type?.toVrapType()?.simpleTSName()?.let { "($it)" } ?: ""
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
            .map {
                val comment: String = it.type.toTsComment().escapeAll()
                val optional = if (it.required) "" else "?"
                """
                    |<${comment}>
                    |${it.name}${optional}: ${it.type.renderTypeExpr()}
                    """.trimMargin()
            }
            .joinToString("\n")
    }

    /**
     * Renders the typescript type expression for this types type.
     *
     * @param the typescript expression
     */
    fun AnyType.renderTypeExpr(): String {
        return when (this) {
            is UnionType -> oneOf.map { it.renderTypeExpr() }.joinToString(" | ")
            is IntersectionType -> allOf.map { it.renderTypeExpr() }.joinToString(" & ")
            is NilType -> "None"
            else -> toVrapType().simpleTSName()
        }
    }

    private fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType() as VrapEnumType

        return """
        |<${this.toTsComment().escapeAll()}>
        |class ${vrapType.simpleClassName}:
        |   <${this.renderEnumValues()}>
        """.trimMargin()
    }

    private fun StringType.renderEnumValues(): String = enumValues()
        .map { "'${it}'" }
        .joinToString(" |\n")


    private fun StringType.enumValues() = enum?.filter { it is StringInstance }
        ?.map { (it as StringInstance).value }
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
