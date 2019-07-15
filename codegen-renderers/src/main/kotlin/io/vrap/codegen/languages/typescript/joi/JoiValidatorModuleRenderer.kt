package io.vrap.codegen.languages.typescript.joi

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.php.extensions.simpleName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.*

class JoiValidatorModuleRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JoiObjectTypeExtensions, EObjectExtensions, FileProducer {

    @Inject
    lateinit var allAnyTypes: MutableList<AnyType>


    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.groupBy { s(it) }
            .map { entry: Map.Entry<String, List<AnyType>> -> buildModule("${entry.key}-types", entry.value) }
            .toList()
    }

    private fun s(it: AnyType): String {
        val t = it.toVrapType()
        return when (t) {
            is VrapObjectType -> t.`package`
            is VrapEnumType -> t.`package`
            else -> ""
        }
    }


    fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {
        val content = """
           |/* tslint:disable */
           |//Generated file, please do not change
           |
           |import * as Joi from 'joi'
           |${getImportsForJoiModule(moduleName, types)}
           |
           |const schema = {
           |  <${types.map { it.renderSchemaPlaceholder() }.joinToString(separator = ",\n")}>
           |}
           |
           |${types.map { it.renderAnyType() }.joinToString(separator = "\n\n")}
       """.trimMargin().keepIndentation()
        return TemplateFile(content, moduleName.replace(".", "/") + ".ts")

    }

    fun AnyType.renderSchemaPlaceholder() : String {
        return """<${toVrapType().simpleJoiName()}: null> """
    }

    fun AnyType.renderSchemaInitializer() : String {
        return """<${toVrapType().simpleJoiName()}: null> """
    }

    fun AnyType.renderAnyType(): String {
        return when (this) {
            is ObjectType -> this.renderObjectType()
            is StringType -> this.renderStringType()
            else -> throw IllegalArgumentException("unhandled case ${this.javaClass}")

        }
    }

    fun ObjectType.renderObjectType(): String {
        val vrapType = this.toVrapType() as VrapObjectType
        if (this.hasSubtypes()) {

            val allSubsCases = this.subTypes
                .filterIsInstance(ObjectType::class.java)
                .filter { !it.discriminatorValue.isNullOrEmpty() }
                .map {
                    "schema.${it.toVrapType().simpleJoiName()}"
                }.joinToString(separator = ", ")

            return """
            |schema.${vrapType.simpleJoiName()} = Joi.lazy(() =\>
            |   Joi.alternatives([$allSubsCases]))
            |
            |export function ${vrapType.simpleJoiName()}(): Joi.AnySchema {
            |   return schema.${vrapType.simpleJoiName()}
            |}
            """.trimMargin()
        } else
            return """
            |schema.${vrapType.simpleJoiName()} = <${renderObjectProperties()}>
            |
            |export function ${vrapType.simpleJoiName()}(): Joi.AnySchema {
            |   return schema.${vrapType.simpleJoiName()}
            |}
            """.trimMargin()
    }


    fun ObjectType.renderObjectProperties(): String {
        val nonPatternProperties = this.allProperties
                .filter { !it.isPatternProperty() }
                .sortedWith(PropertiesComparator)
                .map {it.renderType() }
                .joinToString(separator = ",\n")
        val patternProperties = this.allProperties
                .filter { it.isPatternProperty() }
                .map { ".pattern(new RegExp('${it.name}'), ${it.type.toVrapType().simpleJoiName()})" }
                .joinToString(separator = "\n")

        val additionalProperties = this.additionalProperties?:true
        val unknown = if (additionalProperties) ".unknown()" else ""
        return if (patternProperties.isNullOrEmpty())
            """ |Joi.lazy(() =\> Joi.object()${unknown}.keys({
                |   <$nonPatternProperties>
                |}))
            """.trimMargin()
        else {
            if (nonPatternProperties.isNotBlank())
                """
                |Joi.lazy(() =\> Joi.object().keys({
                |   <$nonPatternProperties>
                |}))
                |$patternProperties
            """.trimMargin()
            else
                "Joi.object()<$patternProperties>"
        }
    }

    fun Property.renderType(): String {
        val owner: ObjectType = eContainer() as ObjectType
        val vrapType: VrapType = this.type.toVrapType()
        val constraint = if (this.required) "required()" else "optional()" +
                if (name == owner.discriminator()) ".only('${owner.discriminatorValue}')" else ""
        return "${name}: ${vrapType.joiSchemaRef()}.${constraint}"
    }

    fun VrapType.joiSchemaRef(): String {
        return when (this) {
            is VrapArrayType -> "Joi.array().items(${this.itemType.joiSchemaRef()})"
            is VrapScalarType -> {
                val type: String = if (this.simpleName() == "date") "date().iso()" else "${this.simpleName()}()"
                return "Joi.$type"
            }
            else -> "${this.simpleJoiName()}()"
        }
    }

    fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType() as VrapEnumType

        return """
        |const ${vrapType.simpleJoiName()}Values = [
        |
        |   <${this.enumFields()}>
        |
        |]
        |
        |export function ${vrapType.simpleJoiName()}(): Joi.AnySchema {
        |   return Joi.string().only(${vrapType.simpleJoiName()}Values)
        |}
        """.trimMargin()
    }

    fun StringType.enumFields(): String = this.enumJsonNames()
        .map { "'$it'" }
        .joinToString(separator = ",\n")


    fun StringType.enumJsonNames() = this.enum?.filter { it is StringInstance }
        ?.map { it as StringInstance }
        ?.map { it.value }
        ?.filterNotNull() ?: listOf()

    fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

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
