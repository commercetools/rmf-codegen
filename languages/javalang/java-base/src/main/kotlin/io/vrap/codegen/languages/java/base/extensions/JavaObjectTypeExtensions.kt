package io.vrap.codegen.languages.java.base.extensions

import io.vrap.codegen.languages.extensions.ExtensionsBase
import io.vrap.codegen.languages.extensions.discriminatorProperty
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.BooleanInstance
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.UnionType

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
        val anno = this.getAnnotation("markDeprecated")
        if (anno != null && (anno.value as BooleanInstance).value == true) {
            return """
                |@Deprecated""".trimMargin()
        }
        return "";
    }
}

fun getImportsForType(vrapType: VrapType): String? {
    return when (val javaVTypes = vrapType.toJavaVType()) {
        is VrapObjectType -> "${javaVTypes.`package`}.${javaVTypes.simpleClassName}"
        is VrapArrayType -> getImportsForType(javaVTypes.itemType)
        is VrapEnumType -> "${javaVTypes.`package`}.${javaVTypes.simpleClassName}"
        else -> null

    }
}
