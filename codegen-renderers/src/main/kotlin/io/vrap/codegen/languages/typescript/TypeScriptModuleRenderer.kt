package io.vrap.codegen.languages.typescript

import com.google.inject.Inject
import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.codegen.languages.java.extensions.toComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat

class TypeScriptModuleRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : TsObjectTypeExtensions, EObjectTypeExtensions, FileProducer {

    @Inject
    lateinit var allAnyTypes: MutableList<AnyType>


    override fun produceFiles(): List<TemplateFile> {
        return allAnyTypes.groupBy { (it.toVrapType() as VrapObjectType).`package` }
                .map { entry: Map.Entry<String, List<AnyType>> -> buildModule(entry.key, entry.value) }
                .toList()
    }


    fun buildModule(moduleName: String, types: List<AnyType>): TemplateFile {

        val content = """
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
                    .properties
                    .filter { !it.isPatternProperty() }
                    .sortedWith(PropertiesComparator)
                    .map {
                        if (it.name != this.discriminator())
                            it.name
                        else if ((it.type as StringType).enum.isNotEmpty())
                            "${it.type.toVrapType().simpleTSName()}.${this.discriminatorValueOrDefault().enumValueName()}"
                        else
                            "'${this.discriminatorValueOrDefault()}'"
                    }
                    .joinToString(separator = ",")
            })\n"
        else ""
    }

    fun ObjectType.objectFields(): String {
        return this.allProperties
                .filter { !it.isPatternProperty() }
                .filter {
                    it.name != this.discriminator() || this.properties.map { it.name }.contains(it.name)

                }
                .sortedWith(PropertiesComparator)
                .map {
                    val comment: String = it.type.toComment()
                    val commentNotEmpty: Boolean = comment.isNotBlank()
                    """
                    |${if (commentNotEmpty) {
                        comment + "\n"
                    } else ""}readonly ${it.name}${if (!it.required) "?" else ""} : ${it.type.toVrapType().simpleTSName()}""".trimMargin()
                }
                .joinToString(separator = ", \n")
    }

    fun StringType.renderStringType(): String {
        val vrapType = this.toVrapType() as VrapObjectType

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
}