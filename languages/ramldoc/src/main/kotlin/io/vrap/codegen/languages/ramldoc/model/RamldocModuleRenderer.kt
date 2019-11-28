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
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
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

class RamldocModuleRenderer @Inject constructor(val api: Api, override val vrapTypeProvider: VrapTypeProvider) : EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return listOf(
                ramldoc(api)
        )
    }

    private fun ramldoc(api: Api): TemplateFile {

        val mapper = ObjectMapper();

        val module = EMFModule();
        module.configure(EMFModule.Feature.OPTION_SERIALIZE_TYPE, false)
        val handler = BaseURIHandler()
        val typeInfo = EcoreTypeInfo()
        val referenceInfo = EcoreReferenceInfo(handler)
        val referenceSerializer = EcoreReferenceSerializer(referenceInfo, typeInfo)
//        val filters = SimpleFilterProvider().addFilter("someProps", SomePropertyFilter())

        module.typeInfo = typeInfo
        module.referenceSerializer = ReferenceSerializer(referenceSerializer);

        module.addSerializer(RegExp::class.java, RegExpSerializer())
        module.addSerializer(UriTemplate::class.java, UriTemplateSerializer())
        module.addSerializer(ObjectInstance::class.java, ObjectInstanceSerializer())
        module.addSerializer<Instance>(ArrayInstance::class.java, InstanceSerializer())
        module.addSerializer<Instance>(IntegerInstance::class.java, InstanceSerializer())
        module.addSerializer<Instance>(BooleanInstance::class.java, InstanceSerializer())
        module.addSerializer<Instance>(StringInstance::class.java, InstanceSerializer())
        module.addSerializer<Instance>(NumberInstance::class.java, InstanceSerializer())
        module.addSerializer(Example::class.java, ExampleSerializer())


        module.addSerializer(NumberType::class.java, InlineTypeSerializer())

        mapper.registerModule(module)

//        var apiRaml = mapper.valueToTree<JsonNode>(api)
//
//        apiRaml.withArray("types").toMutableList().filter { jsonNode -> jsonNode.has("inlineTypes") }.map { jsonNode -> jsonNode.toMutableList().plus(jsonNode.get("inlineTypes").flatten()) }

        return TemplateFile(relativePath = "api.json",
                content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(api)
        )
    }
}

class ExampleSerializer : JsonSerializer<Example>() {
    override fun serialize(value: Example?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value?.value)
    }
}

class InlineTypeSerializer : JsonSerializer<AnyType>() {

    override fun serialize(value: AnyType?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        val eVal = value?.eClass()
        val eFeat = eVal?.eAllStructuralFeatures
        gen?.writeStartObject()
        for (con in eFeat!!) {
            if (con.name == "subTypes") {
                continue
            }
            if (con.name == "inlineTypes") {
                continue
            }
            when (val eValue = value.eGet(con)) {
                is List<*> -> {
                    if (eValue.size > 0)
                        gen?.writeObjectField(con.name, eValue)
                }
                null -> {}
                else ->
                    gen?.writeObjectField(con.name, eValue)
            }
        }
        gen?.writeEndObject()
//        when (value?.isInlineType) {
//            true -> {
//                gen?.writeString("inlineType")
//            }
//            else -> {
//                gen?.writeObject(value)
//            }
//        }
    }
}

class ReferenceSerializer (private val ecoreReferenceSerializer: JsonSerializer<EObject>) : JsonSerializer<EObject>() {

    override fun serialize(value: EObject?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        when (value) {
            is AnyType -> {
                gen?.writeString(value.name)
            }
            is AnyAnnotationType -> {
                gen?.writeString(value.name)
            }
            else ->
                ecoreReferenceSerializer.serialize(value, gen, serializers)
        }
    }
}

class UriTemplateSerializer : JsonSerializer<UriTemplate>() {
    override fun serialize(value: UriTemplate, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.template)
    }
}

// @ToDo: correct serialization of pattern properties
class RegExpSerializer : JsonSerializer<RegExp>() {
    @Throws(IOException::class)
    override fun serialize(value: RegExp, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString("/" + value.toString() + "/")
    }
}

class InstanceSerializer : JsonSerializer<Instance>() {

    @Throws(IOException::class)
    override fun serialize(value: Instance, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeObject(value.value)
    }
}

class ObjectInstanceSerializer : JsonSerializer<ObjectInstance>() {

    @Throws(IOException::class)
    override fun serialize(value: ObjectInstance, gen: JsonGenerator, provider: SerializerProvider) {
        val properties = value.value
        gen.writeStartObject()
        for (v in properties) {
            gen.writeObjectField(v.name, v.value)
        }
        gen.writeEndObject()
    }
}

class SomePropertyFilter : SimpleBeanPropertyFilter() {
    @Throws(Exception::class)
    override fun serializeAsField(pojo: Any, jgen: JsonGenerator, provider: SerializerProvider, writer: PropertyWriter) {
        if (include(writer)) {
            if (writer.getName().equals("subTypes")) {
                return
            }
            writer.serializeAsField(pojo, jgen, provider)
        } else if (!jgen.canOmitFields()) { // since 2.3
            writer.serializeAsOmittedField(pojo, jgen, provider)
        }
    }

    protected override fun include(writer: BeanPropertyWriter): Boolean {
        return true
    }

    protected override fun include(writer: PropertyWriter): Boolean {
        return true
    }
}
