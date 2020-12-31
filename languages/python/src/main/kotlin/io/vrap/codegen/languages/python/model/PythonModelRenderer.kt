/**
 *  Copyright 2021 Michael van Tellingen
 */
package io.vrap.codegen.languages.python.model

import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.getSuperTypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.sortedByTopology
import io.vrap.codegen.languages.python.pyGeneratedComment
import io.vrap.codegen.languages.python.snakeCase
import io.vrap.codegen.languages.python.toDocString
import io.vrap.codegen.languages.python.toLineComment
import io.vrap.codegen.languages.python.toRelativePackageName
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.IntersectionType
import io.vrap.rmf.raml.model.types.NilType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.UnionType
import io.vrap.rmf.raml.model.util.StringCaseFormat

class PythonModelRenderer constructor(
    override val vrapTypeProvider: VrapTypeProvider,
    @AllAnyTypes var allAnyTypes: List<AnyType>
) : PyObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.filter { it is ObjectType || (it is StringType && it.pattern == null) }
            .groupBy {
                it.moduleName()
            }
            .map { entry: Map.Entry<String, List<AnyType>> ->
                buildModule(entry.key, entry.value)
            }
            .toList()
    }

    private fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {
        var sortedTypes = types.sortedByTopology(AnyType::getSuperTypes)
        val content = """
           |$pyGeneratedComment
           |
           |${sortedTypes.getImportsForModule(moduleName)}
           |
           |${sortedTypes.getTypeImportsForModule(moduleName)}
           |
           |${getExportedVariables(types)}
           |
           |${sortedTypes.map { it.renderAnyType() }.joinToString(separator = "\n")}
       """.trimMargin().keepIndentation()

        var filename = moduleName.split(".").joinToString(separator = "/")
        return TemplateFile(content, filename + ".py")
    }

    private fun AnyType.renderAnyType(): String {
        return when (this) {
            is ObjectType -> this.renderObjectType()
            is StringType -> this.renderStringType()
            else -> throw IllegalArgumentException("unhandled case ${this.javaClass}")
        }
    }

    /**
     * Render the __all__ = [...] statement on the top of the file to indicate
     * all the objects we want to export if `from x import *` is used.
     */
    private fun getExportedVariables(types: List<AnyType>): String {
        return types
            .map { "'${it.toPythonVrapType().simplePyName()}'" }
            .sorted()
            .joinToString(prefix = "__all__ = [", postfix = "]\n", separator = ",\n")
    }

    private fun ObjectType.renderObjectType(): String {

        val isDict = allProperties.all { it.isPatternProperty() }
        if (isDict) {
            val valueType = allProperties[0].type.renderTypeExpr()

            return """
            |class $name(typing.Dict[str, $valueType]):
            |    pass
            """.trimMargin()
        } else {
            return """
                |class ${name}${renderExtendsExpr()}:
                |    <${toDocString().escapeAll()}>
                |    <${renderPropertyDecls()}>
                |
                |    <${renderInitFunction()}>
                |    <${renderSerializationMethods()}>
            """.trimMargin()
        }
    }

    /**
     * Renders the optional base class expression for the model class.
     */
    fun ObjectType.renderExtendsExpr(): String {
        return type?.toVrapType()?.simplePyName()?.let { "($it)" } ?: "(_BaseType)"
    }

    /**
     * Renders the attribute of this model as type annotations
     */
    fun ObjectType.renderPropertyDecls(): String {
        return PyClassProperties(false)
            .map {
                val comment: String = it.type.toLineComment().escapeAll()
                if (it.required) {
                    """
                    |<$comment>
                    |${it.name.snakeCase()}: ${it.type.renderTypeExpr()}
                    """.trimMargin()
                } else {
                    """
                    |<$comment>
                    |${it.name.snakeCase()}: typing.Optional[${it.type.renderTypeExpr()}]
                    """.trimMargin()
                }
            }
            .joinToString("\n")
    }

    /**
     * Create the __init__ method of the class
     */
    fun ObjectType.renderInitFunction(): String {
        var attributes = arrayOf("self")
        var kwargs = PyClassProperties(true).map {
            if (it.required) {
                "${it.name.snakeCase()}: ${it.type.renderTypeExpr()}"
            } else {
                "${it.name.snakeCase()}: typing.Optional[${it.type.renderTypeExpr()}] = None"
            }
        }

        if (kwargs.size > 0) {
            attributes += arrayOf("*")
            attributes += kwargs
        }

        var initArgs = mutableListOf<String>()
        val passProperties = PyClassProperties(true) - PyClassProperties(false)
        passProperties.forEach {
            initArgs.add("${it.name.snakeCase()}=${it.name.snakeCase()}")
        }

        val property = discriminatorProperty()
        if (property != null && discriminatorValue != null) {
            //
            val propVrapType = property.type.toVrapType()
            val dVal: String = when (propVrapType) {
                is VrapEnumType -> "${propVrapType.simpleClassName}.${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(discriminatorValue)}"
                else -> "\"${discriminatorValue}\""
            }
            initArgs.add("${property.name.snakeCase()}=$dVal")
        }

        val attrStr = attributes.joinToString(separator = ", ")
        return """
        |def __init__($attrStr):
        |    ${renderInitAssignments()}
        |    super().__init__(${initArgs.joinToString(separator = ", ")})
        """
    }

    fun ObjectType.renderSerializationMethods(): String {
        val schemaType = this.toSchemaVrapType()
        if (schemaType !is VrapObjectType) return ""

        val packageName = schemaType.`package`.toRelativePackageName(moduleName())
        var switchStatement = ""

        if (this.isDiscriminated()) {
            switchStatement = allAnyTypes.getTypeInheritance(this)
                .filterIsInstance<ObjectType>()
                .filter { !it.discriminatorValue.isNullOrEmpty() }
                .map {
                    val vrapSubType = it.toSchemaVrapType() as VrapObjectType
                    val module = vrapSubType
                        .`package`
                        .toRelativePackageName(moduleName())

                    """
                |if data["${discriminator()}"] == "${it.discriminatorValue}":
                |    from $module import ${vrapSubType.simplePyName()}
                |    return ${vrapSubType.simplePyName()}().load(data)
                """.trimMargin()
                }
                .joinToString("\n")

            return """
            |@classmethod
            |def deserialize(cls, data: typing.Dict[str, typing.Any]) -\> "$name":
            |${switchStatement.prependIndent("    ")}
            |
            |def serialize(self) -\> typing.Dict[str, typing.Any]:
            |   from $packageName import ${schemaType.simpleClassName}
            |   return ${schemaType.simpleClassName}().dump(self)
            """.trimMargin()
        }

        return """
        |@classmethod
        |def deserialize(cls, data: typing.Dict[str, typing.Any]) -\> "$name":
        |   from $packageName import ${schemaType.simpleClassName}
        |   return ${schemaType.simpleClassName}().load(data)
        |
        |def serialize(self) -\> typing.Dict[str, typing.Any]:
        |   from $packageName import ${schemaType.simpleClassName}
        |   return ${schemaType.simpleClassName}().dump(self)
        """.trimMargin()
    }

    fun ObjectType.renderInitAssignments(): String {
        return PyClassProperties(false)
            .map { "self.${it.name.snakeCase()} = ${it.name.snakeCase()}" }
            .joinToString("\n    ")
    }

    /**
     * Renders the Python type annotation this types type.
     */
    fun AnyType.renderTypeExpr(): String {
        return when (this) {
            is UnionType -> oneOf.map { it.renderTypeExpr() }.joinToString(prefix = "typing.Union[", separator = ", ", postfix = "]")
            is IntersectionType -> allOf.map { it.renderTypeExpr() }.joinToString(" & ")
            is NilType -> "None"
            else -> toVrapType().pyTypeName()
        }
    }

    private fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType() as VrapEnumType

        return """
        |class ${vrapType.simpleClassName}(enum.Enum):
        |   <${toDocString().escapeAll()}>
        |   <${this.renderEnumValues()}>
        |
        |
        """.trimMargin()
    }

    private fun StringType.renderEnumValues(): String = enumValues()
        .map { "${StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(it)} = '$it'" }
        .joinToString("\n")

    private fun StringType.enumValues() = enum?.filter { it is StringInstance }
        ?.map { (it as StringInstance).value }
        ?.filterNotNull() ?: listOf()
}
