package io.vrap.codegen.languages.java.model.second

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.java.JavaSubTemplates
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.codegen.languages.java.extensions.toComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

class JavaObjectTypeInterfaceRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeRenderer() {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content= """
            |package ${vrapType.`package`};
            |
            |${type.imports()}
            |import ${vrapType.`package`}.${vrapType.simpleClassName}Impl;
            |
            |import com.fasterxml.jackson.annotation.*;
            |import com.fasterxml.jackson.databind.annotation.*;
            |import javax.annotation.Generated;
            |import javax.validation.constraints.NotNull;
            |import javax.validation.Valid;
            |import javax.validation.constraints.NotNull;
            |import java.util.List;
            |
            |<${type.toComment().escapeAll()}>
            |<${type.subTypesAnnotations()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |@JsonDeserialize(as = ${vrapType.simpleClassName}Impl.class)
            |public interface ${vrapType.simpleClassName} ${type.type?.toVrapType()?.simpleName()?.let { "extends $it" } ?: ""} {
            |
            |   <${type.getters().escapeAll()}>
            |
            |   <${type.setters().escapeAll()}>
            |
            |}
            |
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
            content = content
        )
    }

    fun ObjectType.getters() = this.properties
        .filter { it.name != this.discriminator }
        .map { it.geter() }
        .joinToString(separator = "\n\n")

    fun Property.geter(): String {
        return if(this.isPatternProperty()){
            ""
        }else {
            """
            |${this.type.toComment()}
            |${this.validationAnnotations()}
            |@JsonProperty("${this.name}")
            |public ${this.type.toVrapType().simpleName()} get${this.name.capitalize()}();
        """.trimMargin()
        }
    }

    fun ObjectType.setters() = this.properties
        .filter { it.name != this.discriminator }
        .map { it.setter() }
        .joinToString(separator = "\n\n")

    fun Property.setter(): String {
        return if (this.isPatternProperty()) {
            """
            |@JsonAnySetter
            |public void setValue(String key, ${this.type.toVrapType().simpleName()} value);
            """.trimMargin()
        } else {
            "public void set${this.name.capitalize()}(final ${this.type.toVrapType().simpleName()} ${this.name});"
        }
    }
}
