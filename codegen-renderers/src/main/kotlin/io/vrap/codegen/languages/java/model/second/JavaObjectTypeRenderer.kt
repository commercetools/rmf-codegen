package io.vrap.codegen.languages.java.model.second

import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.ObjectTypeExtensions
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.raml.model.types.ObjectType

abstract class JavaObjectTypeRenderer : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    fun ObjectType.imports() = this.getImports().map { "import $it;" }.joinToString(separator = "\n")
}
