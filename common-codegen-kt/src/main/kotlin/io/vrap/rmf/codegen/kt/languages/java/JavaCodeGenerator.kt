package io.vrap.rmf.codegen.kt.languages.java

import com.google.inject.name.Named
import io.vrap.rmf.codegen.common.generator.core.ResourceCollection
import io.vrap.rmf.raml.model.resources.Resource
import io.vrap.rmf.raml.model.types.AnyType
import javax.inject.Inject

class JavaCodeGenerator @Inject constructor(val allTypes : MutableList<AnyType>)