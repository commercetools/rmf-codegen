package io.vrap.codegen.languages.bruno.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.vrap.rmf.raml.model.types.Instance
import io.vrap.rmf.raml.model.types.ObjectInstance
import java.io.IOException

class InstanceSerializer @JvmOverloads constructor(t: Class<Instance>? = null) : StdSerializer<Instance>(t) {

    @Throws(IOException::class)
    override fun serialize(value: Instance, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeObject(value.value)
    }
}

class ObjectInstanceSerializer @JvmOverloads constructor(t: Class<ObjectInstance>? = null) : StdSerializer<ObjectInstance>(t) {

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
