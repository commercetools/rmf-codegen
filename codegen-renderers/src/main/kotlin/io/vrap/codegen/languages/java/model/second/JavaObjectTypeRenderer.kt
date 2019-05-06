package io.vrap.codegen.languages.java.model.second

import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.java.extensions.JavaObjectTypeExtensions
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.raml.model.types.ObjectType

const val ANNOTATION_ABSTRACT = "abstract"

abstract class JavaObjectTypeRenderer : JavaObjectTypeExtensions, EObjectExtensions, ObjectTypeRenderer {
    
    fun ObjectType.imports() = this.getImports().map { "import $it;" }.joinToString(separator = "\n")
    
    fun ObjectType.isAbstract() : Boolean = this.annotations.find { it.type.name == ANNOTATION_ABSTRACT } != null
}
