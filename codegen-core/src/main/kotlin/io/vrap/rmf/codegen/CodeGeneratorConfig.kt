package io.vrap.rmf.codegen

import io.vrap.rmf.codegen.doc.toHtml
import io.vrap.rmf.codegen.types.VrapType
import io.vrap.rmf.raml.model.types.DescriptionFacet
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Contains the customization config for the generator
 * @param basePackageName use the base package for the generated code every model would be under '${basePackageName}.model' and the client under '{basePackageName}.client'
 * @param modelPackage if used it would override the package specified from basePackageName, for the model classes
 * @param clientPackage if used it would override the package specified from clientPackage, for the client classes
 */
data class CodeGeneratorConfig (
        val sharedPackage: String? = null,
        val basePackageName: String? = null,
        val modelPackage: String? = null,
        val clientPackage: String? = null,
        val writeGitHash: Boolean = false,
        val outputFolder: Path = Paths.get("build/gensrc"),
        val customTypeMapping: Map<String, VrapType> = mapOf()
)
