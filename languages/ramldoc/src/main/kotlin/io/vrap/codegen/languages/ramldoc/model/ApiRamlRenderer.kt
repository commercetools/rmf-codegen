package io.vrap.codegen.languages.ramldoc.model

import com.damnhandy.uri.template.UriTemplate
import com.damnhandy.uri.template.jackson.datatype.UriTemplateSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.PropertyFilter
import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.di.ModelPackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.modules.Api
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.values.RegExp
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EObjectContainmentEList
import org.emfjson.jackson.annotations.EcoreReferenceInfo
import org.emfjson.jackson.annotations.EcoreTypeInfo
import org.emfjson.jackson.databind.ser.EMFSerializers
import org.emfjson.jackson.databind.ser.EcoreReferenceSerializer
import org.emfjson.jackson.handlers.BaseURIHandler
import org.emfjson.jackson.module.EMFModule
import java.io.IOException

class ApiRamlRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {
    @Inject
    @ModelPackageName
    lateinit var modelPackageName: String

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                apiRaml(api)
        )
    }

    private fun apiRaml(api: Api): TemplateFile {

        val content = """
            |#%RAML 1.0
            |---
            |title: ${api.title}
            |types:
            |  <<${api.types.joinToString("\n") { "${it.name}: !include ${ramlFileName(it)}" }}>>
        """.trimMargin().keepIndentation("<<", ">>")

        return TemplateFile(relativePath = "api.raml",
                content = content
        )
    }

    private fun ramlFileName(type: AnyType): String {
        when (val vrapType = type.toVrapType()) {
            is VrapObjectType ->
                return "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml"
            is VrapEnumType ->
                return "types/" + vrapType.`package`.replace(modelPackageName, "").trim('/') + "/" + vrapType.simpleClassName + ".raml"
            else -> return ""
        }
    }
}
