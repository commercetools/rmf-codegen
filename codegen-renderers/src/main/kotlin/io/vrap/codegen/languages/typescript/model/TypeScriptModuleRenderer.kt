package io.vrap.codegen.languages.typescript.model

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.getSuperTypes
import io.vrap.codegen.languages.extensions.sortedByTopology
import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.codegen.languages.java.extensions.toComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import java.util.*

class TypeScriptModuleRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : TsObjectTypeExtensions, EObjectTypeExtensions, FileProducer {

    @Inject
    lateinit var allAnyTypes: MutableList<AnyType>


    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.groupBy { s(it) }
                .map { entry: Map.Entry<String, List<AnyType>> -> buildModule(entry.key, entry.value) }
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
        var types = types.filter { it !is UnionType }.sortedByTopology(AnyType::getSuperTypes, AnyType::getSubTypes)
        val content = """
           |/* tslint:disable */
           |//Generated file, please do not change
           |
           |${getImportsForModule(moduleName, types)}
           |
           |${types.map { it.renderAnyType() }.joinToString(separator = "\n")}
       """.trimMargin().keepIndentation()

        return TemplateFile(content, moduleName.replace(".", "/") + ".ts")

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

        return """
        |${this.toComment().escapeAll()}
        |export class ${vrapType.simpleClassName} ${this.type?.toVrapType()?.simpleName()?.let { "extends $it " }
                ?: ""}{
        |
        |   <${this.patternSpec()}>
        |   <${this.constructorBlock()}>
        |}
        """.trimMargin()
    }

    fun ObjectType.constructorBlock():String{
        return if(this.allProperties.filter { !it.isPatternProperty() }.isNotEmpty())
            """
            |constructor(
            |       <${this.objectFields().escapeAll()}>
            |){<${this.superConstructor()}>}
            |""".trimMargin()
        else ""
    }

    fun ObjectType.superConstructor(): String {
        return if (this.type != null)
            "\nsuper(${(
                    this.type as ObjectType)
                    .allProperties
                    .filter { !it.isPatternProperty() }
                    .sortedWith(PropertiesComparator)
                    .map { it.TSName() }
                    .joinToString(separator = ",")
            })\n"
        else ""
    }

    fun ObjectType.objectFields(): String {
        return this.allProperties
                .filter { !it.isPatternProperty() }
                .sortedWith(PropertiesComparator)
                .map {
                    var defaultValue = ""
                    if (discriminatorValue != null && it.required && it.name == discriminator()) {
                        defaultValue = " = " +
                            if (!it.type.isInlineType && (it.type as StringType).enum.isNotEmpty())
                                "${it.type.toVrapType().simpleTSName()}.${this.discriminatorValue.enumValueName()}"
                            else
                                "'${this.discriminatorValue}'"
                    }

                    val comment: String = it.type.toComment()
                    val commentNotEmpty: Boolean = comment.isNotBlank()
                    """
                    |${if (commentNotEmpty) {
                        comment + "\n"
                    } else ""}readonly ${it.TSName()}${if (!it.required) "?" else ""} : ${it.type.TSType()}${defaultValue}""".trimMargin()
                }
                .joinToString(separator = ", \n")
    }

    val reservedTSNames = Arrays.asList("function", "interface")

    fun Property.TSName() : String {
        return if (reservedTSNames.contains(name)) "_${name}"  else name
    }

    fun AnyType.TSType() : String {
        return when(this) {
            is UnionType -> this.oneOf.map { it.toVrapType().simpleTSName() }.joinToString("|")
            else -> this.toVrapType().simpleTSName()
        }
    }

    fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType() as VrapEnumType

        return """
        |${this.toComment().escapeAll()}
        |export enum ${vrapType.simpleClassName} {
        |
        |   <${this.enumFields()}>
        |
        |}
        """.trimMargin()
    }

    fun StringType.enumFields(): String = this.enumJsonNames()
            .map { "${it.enumValueName()} = '$it'" }
            .joinToString(separator = ",\n")


    fun StringType.enumJsonNames() = this.enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.map { it.value }
            ?.filterNotNull() ?: listOf()

    fun String.enumValueName(): String {
        return StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this)
    }

    fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

    /**
     * If the handled is a map type then the map args should be specified
     */
    fun ObjectType.patternSpec(): String? {
        return this.allProperties
                .filter { it.isPatternProperty() }
                .firstOrNull()
                .let {
                    if (it != null) "[key:string]: ${it.type.toVrapType().simpleTSName()}" else ""
                }
    }

    /**
     * in typescript optional properties should come after required ones
     */
    object PropertiesComparator : Comparator<Property> {
        override fun compare(o1: Property, o2: Property): Int {
            return if (o1.required && !o2.required) {
                -1
            } else if (!o1.required && o2.required) {
                +1
            } else if((o1.type?.name?:"").compareTo(o2.type?.name?:"") != 0){
                (o1.type?.name?:"").compareTo(o2.type?.name?:"")
            }
            else {
                o1.name.compareTo(o2.name)
            }
        }
    }

    object AnyTypeComparator : Comparator<AnyType> {

        override fun compare(o1: AnyType?, o2: AnyType?): Int {
            if (o1?.type === null || o1?.subTypes.contains(o2)) {
                return -1
            }
            if (o2?.type === null  || o2?.subTypes.contains(o1)) {
                return 1
            }
            return 0
        }
    }
}
