package io.vrap.codegen.languages.java.base.extensions

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.*

interface JavaObjectTypeExtensions : ExtensionsBase {

    fun ObjectType.getImports(): List<String> = this.properties
        .map { it.type }
        .plus(this.properties.map { it.type }.filterIsInstance<UnionType>().flatMap { it.oneOf })
        //If the subtipes are in the same package they should be imported
        .plus(this.namedSubTypes())
        .plus(this.type)
        .plus( discriminatorProperty()?.type )
        .filterNotNull()
        .map { vrapTypeProvider.doSwitch(it) }
        .map { getImportsForType(it) }
        .filterNotNull()
        .sortedBy { it }
        .distinct()
        .toList()

    fun ObjectType.imports() = this.getImports().map { "import $it;" }.joinToString(separator = "\n")

    fun ObjectType.isAbstract() : Boolean = this.discriminator() != null && this.discriminator().isNotEmpty() && (this.discriminatorValue == null || this.discriminatorValue.isEmpty()) || this.hasAbstractAnnotation()

    fun ObjectType.hasAbstractAnnotation() : Boolean {
        return (this.getAnnotation("abstract")?.value as BooleanInstance?)?.value ?: false
    }

    fun Property.deprecationAnnotation(): String {
        val anno = this.getAnnotation("markDeprecated", true)
        if (anno != null && (anno.value as BooleanInstance).value == true) {
            return """
                |@Deprecated""".trimMargin()
        }
        return "";
    }

    public fun ObjectType.builderComment(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        if (this.hasSubtypes()) {
            val firstSubType = this.subTypes.plus(this.subTypes.flatMap { it.subTypes }).distinctBy { it.name }
                .asSequence()
                .filterIsInstance<ObjectType>()
                .filter { it.discriminatorValue != null }
                .sortedBy { anyType -> anyType.name }
                .first()
            return """
            | Example to create a subtype instance using the builder pattern
            | \<div class=code-example\>
            | \<pre\>\<code class='java'\>
            |   ${vrapType.simpleClassName} ${vrapType.simpleClassName.firstLowerCase()} = ${vrapType.simpleClassName}.${firstSubType.discriminatorValue.lowerCamelCase()}Builder()
            |           <${firstSubType.allProperties.filter { property -> property.required }.filter { it.getAnnotation("deprecated") == null }.filter { it.name != this.discriminator() }.joinToString("\n") { it.builderComment()}}>
            |           .build()
            | \</code\>\</pre\>
            | \</div\>
        """.trimMargin().keepIndentation()
        }

        return """
            | Example to create an instance using the builder pattern
            | \<div class=code-example\>
            | \<pre\>\<code class='java'\>
            |   ${vrapType.simpleClassName} ${vrapType.simpleClassName.firstLowerCase()} = ${vrapType.simpleClassName}.builder()
            |           <${this.allProperties.filter { property -> property.required }.filter { it.getAnnotation("deprecated") == null }.filter { it.name != this.discriminator() }.map { it.builderComment() }.filterNot { it.isBlank() }.joinToString("\n", transform = { ".$it"})}>
            |           .build()
            | \</code\>\</pre\>
            | \</div\>
        """.trimMargin().keepIndentation()
    }

    private fun Property.builderComment(): String {
        return """
            |${this.builderExample()}
        """.trimMargin()
    }

    private fun Property.builderExample(): String {
        val vrapType = vrapTypeProvider.doSwitch(this.type)
        val propType = this.type
        return when(propType) {
            is NumberType -> "${this.name}(0.3)"
            is IntegerType -> "${this.name}(1)"
            is StringType -> when (vrapType) {
                is VrapEnumType -> "${this.name}(${vrapType.simpleClassName}.${propType.enumValues()?.first()?.value?.enumValueName()})"
                else -> "${this.name}(\"{${this.name}}\")"
            }
            is BooleanType -> "${this.name}(true)"
            is TimeOnlyType -> "${this.name}(LocalTime.parse(\"12:00:00.301\"))"
            is DateOnlyType -> "${this.name}(LocalDate.parse(\"2022-01-01\"))"
            is DateTimeType -> "${this.name}(ZonedDateTime.parse(\"2022-01-01T12:00:00.301Z\"))"
            is ArrayType -> "plus${this.name.firstUpperCase()}(${this.name.firstLowerCase()}Builder -> ${this.name.firstLowerCase()}Builder)"
            is ObjectType -> "${this.name}(${this.name.firstLowerCase()}Builder -> ${this.name.firstLowerCase()}Builder)"
            else -> ""
        }.escapeAll()
    }

    private fun StringType.enumValues() = (if (this.isInlineType) this.type.enum else this.enum)?.filter { it is StringInstance }
        ?.map { it as StringInstance }
        ?.filter { it.value != null }
}

fun getImportsForType(vrapType: VrapType): String? {
    return when (val javaVTypes = vrapType.toJavaVType()) {
        is VrapObjectType -> "${javaVTypes.`package`}.${javaVTypes.simpleClassName}"
        is VrapArrayType -> getImportsForType(javaVTypes.itemType)
        is VrapEnumType -> "${javaVTypes.`package`}.${javaVTypes.simpleClassName}"
        else -> null

    }
}
