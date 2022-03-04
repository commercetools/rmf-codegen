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
import io.vrap.codegen.languages.python.toRelativePackageName
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapScalarType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.AnyType
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.types.UnionType

class PythonSchemaRenderer constructor(
    override val vrapTypeProvider: VrapTypeProvider,
    @AllAnyTypes var allAnyTypes: List<AnyType>
) : PyObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        var produced = allAnyTypes.filter { it is ObjectType || (it is StringType && it.pattern == null) }
            .groupBy {
                it.schemaModuleName()
            }
            .map { entry: Map.Entry<String, List<AnyType>> ->
                buildModule(entry.key, entry.value)
            }
            .toList()

        return produced
    }

    private fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {
        var sortedFields = types
            .filterIsInstance<ObjectType>()
            .filter { it.isDict() }
            .sortedByTopology(AnyType::getSuperTypes)

        var sortedSchemas = types
            .filter { it !is UnionType }
            .filter {
                when (it) {
                    is ObjectType -> !it.isDict()
                    else -> true
                }
            }
            .sortedByTopology(AnyType::getSuperTypes)

        val content = """
           |$pyGeneratedComment
           |import re
           |
           |import marshmallow
           |import marshmallow_enum
           |
           |from commercetools import helpers
           |${types.getSchemaImportsForModule(moduleName)}
           |
           |# Fields
           |${sortedFields.map { it.renderAnyType() }.joinToString(separator = "\n")}
           |
           |# Marshmallow Schemas
           |${sortedSchemas.map { it.renderAnyType() }.joinToString(separator = "\n")}
       """.trimMargin().keepIndentation()

        var filename = moduleName.split(".").joinToString(separator = "/")
        return TemplateFile(content, filename + ".py")
    }

    private fun AnyType.renderAnyType(): String {
        return when (this) {
            is ObjectType -> this.renderObjectType()
            is StringType -> ""
            else -> throw IllegalArgumentException("unhandled case ${this.javaClass}")
        }
    }

    private fun ObjectType.renderObjectType(): String {
        if (isDict()) {

            return """
                |class ${name}Field(marshmallow.fields.Dict):
                |
                |    def _deserialize(self, value, attr, data, **kwargs):
                |        result = super()._deserialize(value, attr, data)
                |        return models.$name(**result)
            """.trimMargin().keepIndentation()
        } else {
            return """
            |class ${name}Schema${renderExtendsExpr()}:
            |    <${renderPropertyFields()}>
            |
            |    class Meta:
            |        unknown = marshmallow.EXCLUDE
            |
            |${renderPostLoad().prependIndent("    ")}
            """.trimMargin().keepIndentation()
        }
    }

    private fun ObjectType.renderPostLoad(): String {
        val modelType = this.toVrapType()
        if (modelType is VrapObjectType) {

            val dProp = discriminatorProperty()
            var delStatement = ""
            if (dProp != null) {
                delStatement = "del data[\"${dProp.name.snakeCase()}\"]"
            }

            if (allProperties.any { it.isPatternProperty() }) {
                return """
                |@marshmallow.pre_load
                |def pre_load(self, data, **kwargs):
                |    field = typing.cast(helpers.RegexField, self.fields["_regex"])
                |    return field.pre_load(self, data)
                |
                |@marshmallow.post_load(pass_original=True)
                |def post_load(self, data, original_data, **kwargs):
                |    field = typing.cast(helpers.RegexField, self.fields["_regex"])
                |    data = field.post_load(data, original_data)
                |    return models.${modelType.simpleClassName}(**data)
                |
                |@marshmallow.post_dump(pass_original=True)
                |def post_dump(self, data, original_data, **kwargs):
                |    field = typing.cast(helpers.RegexField, self.fields["_regex"])
                |    return field.post_dump(data, original_data)
                """.trimMargin()
            } else {
                return """
                |@marshmallow.post_load
                |def post_load(self, data, **kwargs):
                |    $delStatement
                |    return models.${modelType.simpleClassName}(**data)
                """.trimMargin()
            }
        } else {
            return ""
        }
    }

    fun ObjectType.renderPropertyFields(): String {
        return this.PyClassProperties(false)
            .map {
                if (it.isPatternProperty()) {
                    "_regex = ${renderFieldDecl(it)}"
                } else {
                    "${it.name.snakeCase()} = ${renderFieldDecl(it)}"
                }
            }
            .joinToString("\n")
    }

    fun ObjectType.renderFieldDecl(prop: Property): String {
        val kwargs = prop.type.PyMarshmallowFieldKwargs(this)
        if (!prop.required) {
            kwargs.add("metadata={'omit_empty': True}")
        }
        kwargs.add("missing=None")
        if (prop.name != prop.name.snakeCase()) {
            kwargs.add("data_key=\"${prop.name}\"")
        }

        if (prop.isPatternProperty()) {
            return """
            |helpers.RegexField(
            |    unknown=marshmallow.EXCLUDE,
            |    pattern=re.compile("${prop.pattern}"),
            |    type=${prop.type.PyMarshmallowFieldClass()}(
            |       ${kwargs.joinToString(separator = ",\n      ")}
            |    )
            |)
            """.trimMargin()
        } else {
            return """
            |${prop.type.PyMarshmallowFieldClass()}(
            |   ${kwargs.joinToString(separator = ",\n")}
            |)
            """.trimMargin()
        }
    }

    fun ObjectType.renderExtendsExpr(): String {
        return type?.toSchemaVrapType()?.PySchemaNameReference()?.let { "($it)" } ?: "(helpers.BaseSchema)"
    }

    fun AnyType.PyMarshmallowFieldClass(): String {
        // TODO: when this is an uniontype we take the parent of the first item
        // and hope that the other types have the same base types.
        if (this is UnionType) {
            if (this.oneOf[0].type != null)
                return this.oneOf[0].type.PyMarshmallowFieldClass()
            return this.oneOf[0].PyMarshmallowFieldClass()
        }

        val vrapType = this.toSchemaVrapType()
        return when (vrapType) {
            is VrapObjectType -> {
                if (this is ObjectType && this.isDict()) {
                    return "${this.name}Field"
                }
                if (this.isDiscriminated()) {
                    return "helpers.Discriminator"
                }
                return "helpers.LazyNestedField"
            }
            is VrapArrayType -> {
                if (this !is ArrayType) {
                    throw Exception("invalid state")
                }
                val items = this.items
                if (items is ObjectType && !items.isDict() && (items.discriminator() == null || !items.discriminatorValue.isNullOrEmpty())) {
                    return "helpers.LazyNestedField"
                } else {
                    return "marshmallow.fields.List"
                }
            }
            else -> vrapType.PySchemaName()
        }
    }

    fun AnyType.PyMarshmallowFieldKwargs(parent: ObjectType): MutableList<String> {
        // TODO: when this is an uniontype we take the parent of the first item
        // and hope that the other types have the same base types.
        if (this is UnionType) {
            if (this.oneOf[0].type != null)
                return this.oneOf[0].type.PyMarshmallowFieldKwargs(parent)
            return this.oneOf[0].PyMarshmallowFieldKwargs(parent)
        }

        val schemaVrapType = this.toSchemaVrapType()
        val kwargs = mutableListOf("allow_none=True")

        return when (schemaVrapType) {
            is VrapScalarType -> kwargs
            is VrapObjectType -> {
                if (this !is ObjectType) {
                    throw Exception("Invalid state")
                }
                if (this.isDict()) {
                    val prop = this.PyClassProperties(true)[0]
                    val valuesKwargs = prop.type.PyMarshmallowFieldKwargs(this)
                    kwargs.add(
                        """
                        |values=${prop.type.PyMarshmallowFieldClass()}(
                        |    ${valuesKwargs.joinToString(separator = ",\n    ")}
                        |)
                    """
                    )
                    return kwargs
                } else if (this.isDiscriminated()) {
                    val subSchemas: List<String> = allAnyTypes.getTypeInheritance(this)
                        .filterIsInstance<ObjectType>()
                        .filter { !it.discriminatorValue.isNullOrEmpty() }
                        .map {
                            val vrapSubType = it.toSchemaVrapType()
                            """"${it.discriminatorValue}": helpers.absmod(__name__, "${vrapSubType.PySchemaName().toRelativePackageName(parent.schemaModuleName())}")"""
                        }
                    if (subSchemas.size == 0) {
                        throw Exception("Expected child schemas")
                    }
                    kwargs.add("discriminator_field=(\"${this.discriminator()}\", \"${this.discriminator().snakeCase()}\")")
                    kwargs.add("discriminator_schemas={${subSchemas.joinToString(separator = ",\n")}}")
                    return kwargs
                } else {
                    kwargs.add(0, "nested=helpers.absmod(__name__, \"${schemaVrapType.PySchemaName().toRelativePackageName(parent.schemaModuleName())}\")")
                    kwargs.add("unknown=marshmallow.EXCLUDE")
                    return kwargs
                }
            }
            is VrapEnumType -> {
                val modelVrapType = this.toVrapType()
                kwargs.add(0, "${modelVrapType.simplePyName()}")
                kwargs.add(1, "by_value=True")
                return kwargs
            }
            is VrapArrayType -> {
                if (this !is ArrayType) {
                    throw Exception("invalid state")
                }
                val itemType = this.items

                if (itemType is ObjectType && !itemType.isDict() && (itemType.discriminator() == null || !itemType.discriminatorValue.isNullOrEmpty())) {
                    kwargs.add(0, "nested=helpers.absmod(__name__, \"${schemaVrapType.PySchemaName().toRelativePackageName(parent.schemaModuleName())}\")")
                    kwargs.add("many=True")
                    kwargs.add("unknown=marshmallow.EXCLUDE")
                    return kwargs
                }

                val nestedKwargs = itemType.PyMarshmallowFieldKwargs(parent)
                val nestedKwargStr = nestedKwargs.joinToString(separator = ", ")

                kwargs.add(0, "${itemType.PyMarshmallowFieldClass()}($nestedKwargStr)")
                return kwargs
            }
            else -> mutableListOf<String>()
        }
    }
}
