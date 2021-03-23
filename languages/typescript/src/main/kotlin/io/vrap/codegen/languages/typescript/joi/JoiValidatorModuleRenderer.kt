package io.vrap.codegen.languages.typescript.joi
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.typescript.model.TsObjectTypeExtensions
import io.vrap.codegen.languages.typescript.model.simpleTSName
import io.vrap.codegen.languages.typescript.toJoiPackageName
import io.vrap.codegen.languages.typescript.tsGeneratedComment
import io.vrap.rmf.codegen.di.AllAnyTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*

class JoiValidatorModuleRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllAnyTypes val allAnyTypes: List<AnyType>) : JoiObjectTypeExtensions, TsObjectTypeExtensions, FileProducer {

    private var joiAlternativesTypes: List<String> = ArrayList()
    private var recursionTypes: List<String> = ArrayList()

    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.groupBy { it.moduleName() }
                .filter { it.key.isNotBlank() }
                .map { entry -> buildModule(entry.key.toJoiPackageName(), entry.value) }
                .toList()
    }

    private fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {
        recursionTypes = types.filterIsInstance<ObjectType>().flatMap { it.renderJoiObjectSchema().second }
        joiAlternativesTypes = types.filterIsInstance<ObjectType>().flatMap { it.renderJoiObjectSchema().third }
        val joiSchema = types.filterNot { it is StringType && it.pattern != null }.joinToString(separator = "\n\n") { it.renderJoiSchema() }

        val content = """
           |$tsGeneratedComment
           |
           |import * as Joi from 'joi'
           |${getImportsForJoiModule(moduleName, types)}
           |
           |$joiSchema
           |
       """.trimMargin().keepIndentation()

        return TemplateFile(content, moduleName.replace(".", "/") + ".ts")

    }

    private fun AnyType.renderJoiSchema(): String {
        return when (this) {
            is ObjectType -> this.renderJoiObjectSchema().first
            is StringType -> this.renderJoiStringSchema()
            is UnionType -> this.renderJoiUnionSchema()
            else -> throw IllegalArgumentException("unhandled case ${this.javaClass}")
        }
    }

    private fun UnionType.renderJoiUnionSchema():String {
        val arrayArg:String = this.getOneOf().map { "${it.toVrapType().simpleJoiName()}()"}
                .joinToString(prefix = "[",separator = ",",postfix = "]")
        val schemaDeclaration = """
            |Joi.alternatives($arrayArg)
            |
        """
        return toVrapType().renderSchemaExportFunction(schemaDeclaration).trimMargin()
    }

    private fun VrapType.renderSchemaExportFunction(schemaDeclaration: String): String {
        val attachedId = if(recursionTypes.contains(simpleJoiName())) ".id('${simpleJoiName()}')" else ""
        return """
            |export const ${simpleJoiName()} = () =\> ${schemaDeclaration}${attachedId}
        """.trimMargin()
    }

    private fun ObjectType.renderJoiObjectSchema(): Triple<String, List<String>, List<String>> {
        val recursionTypes = ArrayList<String>()
        val joiAlternativesTypes = ArrayList<String>()
        val schemaDeclaration : String
        if (this.hasSubtypes()) {
            val parentType = this.toVrapType().simpleJoiName()
            val sharedSchema: MutableList<String> = ArrayList()
            val allSubsCases = this.subTypes
                    .filterIsInstance(ObjectType::class.java)
                    .filter { !it.discriminatorValue.isNullOrEmpty() }
                    .map {
                        val typeName = it.toVrapType().simpleJoiName()
                        val existMainType = it.allProperties
                                .filter { !it.isPatternProperty() }
                                .sortedWith(PropertiesComparator)
                                .filter {
                                    it.type.toVrapType().simpleJoiName() == parentType
                                }

                        if(existMainType.isNotEmpty()){
                            sharedSchema.add("$typeName")
                            recursionTypes.add("$typeName")

                            "Joi.link('#$typeName')"
                        } else "$typeName()"

                    }.joinToString(separator = ", ")

            val id = if(sharedSchema.isNotEmpty()) """.id('$parentType').${sharedSchema.joinToString(separator = ".") { "shared($it())" }}""" else ""
            joiAlternativesTypes.add(this.toVrapType().simpleJoiName())

            schemaDeclaration = """Joi.alternatives().try($allSubsCases)$id"""

        } else {
            schemaDeclaration =  """<${renderPropertySchemas()}>"""
        }
        return Triple(toVrapType().renderSchemaExportFunction(schemaDeclaration).trimIndent(), recursionTypes, joiAlternativesTypes)
    }

    private fun ObjectType.renderPropertySchemas(): String {
        val discriminatorProperty = this.allProperties
                .filter { !it.isPatternProperty() }
                .sortedWith(PropertiesComparator)
                .contains(this.discriminatorProperty())

        val nonPatternProperties = this.allProperties
                .filter { !it.isPatternProperty() }
                .sortedWith(PropertiesComparator).joinToString(separator = ",\n") { it.renderPropertyTypeSchema(this, discriminatorProperty) }
        val patternProperties = this.allProperties
                .filter { it.isPatternProperty() }.joinToString(separator = "\n") { ".pattern(${it.asRegExp()}, ${it.type.toVrapType().renderTypeRef()})" }
        val additionalProperties = this.additionalProperties?:true
        val unknown = if (!additionalProperties) ".unknown(false)" else ""
        return if (patternProperties.isNullOrEmpty())
            """ |Joi.object()${unknown}.keys({
                |   <$nonPatternProperties>
                |})
            """.trimMargin()
        else {
            if (nonPatternProperties.isNotBlank())
                """
                |Joi.object().keys({
                |   <$nonPatternProperties>
                |})
                |$patternProperties
            """.trimMargin()
            else
                "Joi.object()<$patternProperties>"
        }
    }

    private fun Property.renderPropertyTypeSchema(current : ObjectType, discriminatorProperty: Boolean): String {
        val vrapType: VrapType = this.type.toVrapType()
        val discriminatorConstraint = if (this == current.discriminatorProperty()) ".valid('${current.discriminatorValue}')" else ""
        val requiredConstraint = if (this.required) ".required()" else ".optional()"
        val maxConstraint = if (this.type is ArrayType && (this.type as ArrayType).maxItems != null) ".max(${(this.type as ArrayType).maxItems})" else ""
        val joiLink = joiAlternativesTypes.contains(vrapType.simpleJoiName()) && discriminatorProperty
        val joiSchema = if(joiLink)"Joi.link('#${vrapType.simpleJoiName()}')" else vrapType.renderTypeRef()

        return "${name}: ${joiSchema}${maxConstraint}${discriminatorConstraint}${requiredConstraint}"
    }

    private fun VrapType.renderTypeRef(): String {
        return when (this) {
            is VrapArrayType -> {
                "Joi.array().items(${this.itemType.renderTypeRef()})"
            }
            is VrapScalarType -> {
                val type: String = if (this.simpleTSName() == "date") "date().iso()" else "${this.simpleTSName()}()"
                return "Joi.$type"
            }
            else -> "${this.simpleJoiName()}()"
        }
    }

    private fun StringType.renderJoiStringSchema(): String {
        val vrapType = this.toVrapType() as VrapEnumType

        return """
        |const ${vrapType.simpleJoiName()}Values = [
        |   <${this.enumFields()}>
        |]
        |
        |export const ${vrapType.simpleJoiName()} = () =\> {
        |   return Joi.string().valid(...${vrapType.simpleJoiName()}Values)
        |}
        """.trimMargin()
    }

    private fun StringType.enumFields(): String = this.enumJsonNames()
        .map { "'$it'" }
        .joinToString(separator = ",\n")


    private fun StringType.enumJsonNames() = this.enum?.filter { it is StringInstance }
        ?.map { it as StringInstance }
        ?.map { it.value }
        ?.filterNotNull() ?: listOf()

    private fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

    private fun Property.asRegExp() = if (this.name.equals("//")) "/.*/" else this.name

    /**
     * in typescript optional properties should come after required ones
     */
    object PropertiesComparator : Comparator<Property> {
        override fun compare(o1: Property, o2: Property): Int {
            return if (o1.required && !o2.required) {
                -1
            } else if (!o1.required && o2.required) {
                +1
            } else if ((o1.type?.name ?: "").compareTo(o2.type?.name ?: "") != 0) {
                (o1.type?.name ?: "").compareTo(o2.type?.name ?: "")
            } else {
                o1.name.compareTo(o2.name)
            }
        }
    }
}
