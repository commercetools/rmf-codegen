package io.vrap.rmf.codegen.kt

import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.codegen.kt.types.VrapType
import io.vrap.rmf.raml.model.types.DescriptionFacet
import org.eclipse.emf.common.util.URI
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Contains the customization config for the generator
 * @param packagePrefix use the base package for the generated code every model would be under '${packagePrefix}.model' and the client under '{packagePrefix}.client'
 * @param modelPackage if used it would override the package specified from packagePrefix, for the model classes
 * @param clientPackage if used it would override the package specified from clientPackage, for the client classes
 */
data class CodeGeneratorConfig (
        val packagePrefix: String? = null,
        val modelPackage: String? = null,
        val clientPackage: String? = null,
        val outputFolder: Path = Paths.get("build/gensrc"),
        val customTypeMapping: Map<String, VrapType> = mapOf(),
        val ramlFileLocation: URI,
        val docTransformer: (DescriptionFacet) -> String? = DescriptionFacet::toHtml
)
