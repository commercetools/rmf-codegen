package io.vrap.rmf.codegen.kt

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.codegen.kt.types.VrapType
import io.vrap.rmf.raml.model.types.DescriptionFacet
import org.eclipse.emf.common.util.URI
import java.nio.file.Path
import java.nio.file.Paths

data class CodeGeneratorConfig (
        val packagePrefix: String? = null,
        val outputFolder: Path = Paths.get("build/gensrc"),
        val customTypeMapping: Map<String, VrapType> = mapOf(),
        val ramlFileLocation: URI,
        val docTransformer: (DescriptionFacet) -> String? = DescriptionFacet::toHtml
)
