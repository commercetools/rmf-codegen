package io.vrap.codegen.languages.javalang.plantuml

import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.isSuccessfull
import io.vrap.codegen.languages.java.base.extensions.JavaEObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.firstBody
import io.vrap.codegen.languages.java.base.extensions.simpleName
import io.vrap.rmf.codegen.di.AllObjectTypes
import io.vrap.rmf.codegen.di.AllResources
import io.vrap.rmf.codegen.di.EnumStringTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.util.StringCaseFormat
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.util.EcoreUtil
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset


class PlantUmlDiagramProducer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllObjectTypes val allObjectTypes: List<ObjectType>, @EnumStringTypes val allStringTypes: List<StringType>, @AllResources val api: Api) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> =
        listOf<TemplateFile>()
            .plus(allObjectTypes.flatMap { render(it)})
            .plus(allStringTypes.flatMap { render(it)})
            .plus(renderIndex())

    private fun renderIndex(): List<TemplateFile> {
        val puml = """
                |@startmindmap
                |
                |* Api
                |${api.resources.sortedBy { it.relativeUri.template }.joinToString("\n") { render(it, 2) }}
                |
                |@endmindmap
                """.trimMargin()
        val reader = SourceStringReader(puml)
        val os = ByteArrayOutputStream()
        reader.outputImage(os, FileFormatOption(FileFormat.SVG))
        os.close()

        val svg = String(os.toByteArray(), Charset.forName("UTF-8"))

        return listOf(
            TemplateFile(
                relativePath = "_index.puml",
                content = puml.replace("!pragma layout elk", "")
            ),
            TemplateFile(
                relativePath = "svg/_index.svg",
                content = svg
            ),
        )
    }

    private fun render(resource: Resource, level: Int): String {
        return """
                |${"".padEnd(level,'*')} ${resource.relativeUri.template}
                |${resource.methods.joinToString("\n") { render(it, level + 1) }}
                |${resource.resources.sortedBy { it.relativeUri.template }.joinToString("\n") { render(it, level + 1) }}
        """.trimMargin()
    }

    private fun render(method: Method, level: Int): String {
        val requestType = method.firstBody()?.type
        return """
                |${"".padEnd(level,'*')}: ${method.methodName.uppercase()}${if (requestType != null) """
                |  request: ${requestType.plantUmlAttribute() }""" else "" }
                |  responses:
                |    ${method.responses.filter { it.isSuccessfull() }.mapNotNull { it.firstBody()?.type }.distinctBy { it.name }.joinToString("\n  ") { it.plantUmlAttribute() }}
                |;
        """.trimMargin()
    }

    private fun render(type: ObjectType): List<TemplateFile> {
        val references = EcoreUtil.UsageCrossReferencer.find(type, type.eContainer())
            .map { it.eObject }
            .filter { it.eContainer() is Property }
            .map { it.eContainer() as Property }
            .sortedBy { it.type?.name }
        val inlinedSubTypes = type.subTypes.plus(type.subTypes.flatMap { it.subTypes }).distinctBy { it.name }.sortedBy { it.name }
        val subTypes = inlinedSubTypes
            .filterIsInstance<ObjectType>()
            .filter { it.discriminatorValue != null }
            .distinctBy { it.name }
        val childTypes = inlinedSubTypes
            .filterIsInstance<ObjectType>()
            .filter { it.discriminatorProperty() == null }
            .filterNot { it.name == type.name }
            .distinctBy { it.name }
        val puml = """
                |@startuml
                |!pragma layout elk
                |hide empty fields
                |hide empty methods
                |legend
                ||= |= line |
                ||<back:black>   </back>| inheritance |
                ||<back:green>   </back>| property reference |
                ||<back:blue>   </back>| discriminated class |
                |endlegend
                |${ type.plantUmlClassDef() }
                |${if (type.type is ObjectType) (type.type as ObjectType).plantUmlClassDef(false) else ""}
                |${childTypes.joinToString("\n") { it.plantUmlClassDef() }}
                |${type.plantUmlSubTypes(subTypes)}
                |${type.plantUmlPropertyTypes(references)}
                |${type.plantUmlSubTypeConnections(subTypes)}
                |${type.plantUmlPropertyTypeConnections(references)}
                |@enduml
            """.trimMargin()
        val reader = SourceStringReader(puml)
        val os = ByteArrayOutputStream()
        reader.outputImage(os, FileFormatOption(FileFormat.SVG))
        os.close()

        val svg = String(os.toByteArray(), Charset.forName("UTF-8"))
        return listOf(
            TemplateFile(
                relativePath = type.toVrapType().simpleName() + ".puml",
                content = puml.replace("!pragma layout elk", "")
            ),
            TemplateFile(
                relativePath = "svg/" + type.toVrapType().simpleName() + ".svg",
                content = svg
            ),
        )
    }

    private fun render(type: StringType): List<TemplateFile> {
        val puml = """
                |@startuml
                |!pragma layout elk
                |hide methods
                |${ type.plantUmlEnumDef() }
                |@enduml
            """.trimMargin()
        val reader = SourceStringReader(puml)
        val os = ByteArrayOutputStream()
        reader.outputImage(os, FileFormatOption(FileFormat.SVG))
        os.close()

        val svg = String(os.toByteArray(), Charset.forName("UTF-8"))
        return listOf(
            TemplateFile(
                relativePath = type.toVrapType().simpleName() + ".puml",
                content = puml.replace("!pragma layout elk", "")
            ),
            TemplateFile(
                relativePath = "svg/" + type.toVrapType().simpleName() + ".svg",
                content = svg
            ),
        )
    }

    fun ObjectType.modelCompositionRelations(): String {

        val vrapType = this.toVrapType()
        return this.properties
                .map {
                    if(it.type is ObjectType){
                        "${vrapType.simpleName()} ${PlantUmlRelations.COMPOSITION} ${it.type.toVrapType().simpleName()}"
                    }else if( (it.type is ArrayType) && ((it.type as ArrayType).items is ObjectType) ){
                        "${vrapType.simpleName()} ${PlantUmlRelations.COMPOSITION} ${(it.type as ArrayType).items.toVrapType().simpleName()}"
                    }else{
                        ""
                    }
                }
                .filter { it.isNotBlank() }
                .joinToString(separator = "\n\n")
    }

    fun ObjectType.modelInheritenceRelation(): String {

        return this.subTypes
                .filter { it.name != this.name }
                .map {
            """|
               |${this.toVrapType().simpleName()} ${PlantUmlRelations.INHERITS} ${it.toVrapType().simpleName()}
            """.trimMargin()
        }.joinToString(separator = "\n\n")

    }


    private fun ObjectType.plantUmlClassDef(withExtends: Boolean = true): String {
        val vrapType = this.toVrapType() as VrapObjectType

        return """
            |interface ${vrapType.simpleClassName} [[${vrapType.simpleName()}.svg]] ${if (withExtends && this.type != null) "extends ${this.type.toVrapType().simpleName()}" else ""} {
            |    <${this.plantUmlProperties().escapeAll()}>
            |}
        """.trimMargin().keepIndentation()
    }

    private fun AnyType.plantUmlClassDefShort(withExtends: Boolean = false): String {
        val vrapType = this.toVrapType()
        return when (vrapType) {
            is VrapObjectType -> "interface ${this.toVrapType().simpleName()} [[${this.toVrapType().simpleName()}.svg]] ${if (withExtends && this.type != null) "extends ${this.type.toVrapType().simpleName()}" else ""}"
            is VrapArrayType -> (this as ArrayType).items.plantUmlClassDefShort(withExtends)
            is VrapEnumType -> "enum ${this.toVrapType().simpleName()} [[${this.toVrapType().simpleName()}.svg]]"
            else -> "class ${this.toVrapType().simpleName()}"
        }
    }

    private fun AnyType.plantUmlClassDefShortPacked(): String {
        val vrapType = this.toVrapType()
        return when (vrapType) {
            is VrapObjectType -> """
                |package ${vrapType.`package`} {
                |    interface ${this.toVrapType().simpleName()} [[${this.toVrapType().simpleName()}.svg]] ${if (this.type != null) "extends ${this.type.toVrapType().simpleName()}" else ""}
                |}
                """.trimMargin()
            is VrapArrayType -> (this as ArrayType).items.plantUmlClassDefShortPacked()
            is VrapEnumType -> """
                |package ${vrapType.`package`} {
                |    enum ${this.toVrapType().simpleName()} [[${this.toVrapType().simpleName()}.svg]]
                |}
                """.trimMargin()
            else -> "class ${this.toVrapType().simpleName()}"
        }
    }

    private fun AnyType.plantUmlPropertyTypes(references: List<Property>): String {
        return references
            .map { (it.eContainer() as ObjectType).plantUmlClassDef(false) }
            .distinct()
            .joinToString(separator = "\n")
    }

    private fun AnyType.plantUmlPropertyTypeConnections(references: List<Property>): String {
        return references
            .joinToString(separator = "\n") { "${this.toVrapType().simpleName()} --> ${(it.eContainer() as ObjectType).linkName()} #green;text:green : \"${it.name}\"" }
    }

    private fun ObjectType.plantUmlSubTypes(subTypes: List<ObjectType>): String {
        return subTypes
            .map { it.plantUmlClassDef(false) }
            .joinToString(separator = "\n")
    }

    private fun ObjectType.plantUmlSubTypeConnections(subTypes: List<ObjectType>): String {
        return subTypes
            .joinToString(separator = "\n") { "${this.toVrapType().simpleName()} --> ${it.linkName()} #blue;text:blue : \"${it.discriminatorProperty()?.name} : ${it.discriminatorValue}\"" }
    }

    private fun ObjectType.plantUmlProperties (): String {
        return this.allProperties
            .filterNot { it.isPatternProperty() }.joinToString(separator = "\n") { it.plantUmlAttribute() }
    }

    private fun AnyType.linkName(): String {
        return when (this) {
            is ArrayType -> this.items.linkName()
            else -> this.toVrapType().simpleName()
        }
    }
    fun Property.plantUmlAttribute(): String {
        val type = this.type
        return "${this.name}: ${type.plantUmlAttribute()}"
    }

    fun AnyType.plantUmlAttribute(): String {
        val vrapType = this.toVrapType()
        return when(vrapType) {
            is VrapObjectType -> "[[${this.linkName()}.svg ${vrapType.simpleName()}]]"
            is VrapArrayType -> "[[${this.linkName()}.svg ${vrapType.simpleName()}]]"
            is VrapEnumType -> "[[${this.linkName()}.svg ${vrapType.simpleName()}]]"
            else -> vrapType.simpleName()
        }
    }


    fun StringType.plantUmlEnumDef(): String {
        val vrapType = this.toVrapType() as VrapEnumType
        val references = EcoreUtil.UsageCrossReferencer.find(this, this.eContainer())
            .map { it.eObject }
            .filter { it.eContainer() is Property }
            .map { it.eContainer() as Property }
        return """
            |enum ${vrapType.simpleClassName} {
            |    <${this.enumFields()}>
            |}
            |${this.plantUmlPropertyTypes(references).escapeAll()}
            |${this.plantUmlPropertyTypeConnections(references).escapeAll()}
        """.trimMargin().keepIndentation()
    }

    fun StringType.enumFields(): String = this.enumJsonNames()
            .map {
                """
                |${it.enumValueName()}
            """.trimMargin()
            }
            .joinToString(separator = "\n", postfix = "")


    fun StringType.enumJsonNames() = this.enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.map { it.value }
            ?.filterNotNull() ?: listOf()

    fun String.enumValueName(): String {
        return StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this)
    }

    fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

}
