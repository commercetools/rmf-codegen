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

class JavaObjectTypeClassRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeRenderer() {

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val parentTypeImport = if(type.type != null && type.type is ObjectType) {
            val parentType = vrapTypeProvider.doSwitch(type.type) as VrapObjectType
            "${parentType.`package`}.${parentType.simpleClassName}"
        }else{
            null
        }

        val content = """
                |package ${vrapType.`package`};
                |
                |${type.imports()}
                |${parentTypeImport?.let { "import ${it}Impl;"} ?: ""}
                |import com.fasterxml.jackson.annotation.*;
                |import javax.annotation.Generated;
                |import javax.validation.Valid;
                |import javax.validation.constraints.NotNull;
                |import java.util.*;
                |import org.apache.commons.lang3.builder.EqualsBuilder;
                |import org.apache.commons.lang3.builder.HashCodeBuilder;
                |import org.apache.commons.lang3.builder.ToStringBuilder;
                |import org.apache.commons.lang3.builder.ToStringStyle;
                |import java.util.List;
                |
                |<${type.toComment().escapeAll()}>
                |<${JavaSubTemplates.generatedAnnotation}>
                |public class ${vrapType.simpleClassName}Impl ${type.type?.toVrapType()?.simpleName()?.let { "extends ${it}Impl" } ?: ""} implements ${vrapType.simpleClassName} {
                |
                |    <${type.toBeanFields().escapeAll()}>
                |
                |    <${type.getters().escapeAll()}>
                |
                |    <${type.setters().escapeAll()}>
                |
                |
                |    @Override
                |    public String toString() {
                |        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
                |    }
                |
                |    @Override
                |    public boolean equals(Object o) {
                |        return EqualsBuilder.reflectionEquals(this, o);
                |    }
                |
                |    @Override
                |    public int hashCode() {
                |        return HashCodeBuilder.reflectionHashCode(this);
                |    }
                |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Impl".replace(".", "/") + ".java",
            content = content
        )
    }

    fun Property.toJavaField(): String {

        return if (this.isPatternProperty()) {
            "private Map<String,${this.type.toVrapType().simpleName()}> values;"
        } else {
            "private ${this.type.toVrapType().simpleName()} ${if (this.isPatternProperty()) "values" else this.name};"
        }

    }

    fun ObjectType.toBeanFields() = this.properties
        .filter { it.name != this.discriminator }
        .map { it.toJavaField() }.joinToString(separator = "\n\n")

    fun ObjectType.setters() = this.properties
        //Filter the discriminators because they don't make much sense the generated bean
        .filter { it.name != this.discriminator }
        .map { it.setter() }
        .joinToString(separator = "\n\n")


    fun ObjectType.getters() = this.properties
        //Filter the discriminators because they don't make much sense the generated bean
        .filter { it.name != this.discriminator }
        .map { it.getter() }
        .joinToString(separator = "\n\n")


    fun Property.setter(): String {
        return if (this.isPatternProperty()) {

            """
            |@JsonAnySetter
            |public void setValue(String key, ${this.type.toVrapType().simpleName()} value) {
            |    if (values == null) {
            |        values = new HashMap<>();
            |    }
            |    values.put(key, value);
            |}
            """.trimMargin()
        } else {
            """
            |public void set${this.name.capitalize()}(final ${this.type.toVrapType().simpleName()} ${this.name}){
            |   this.${this.name} = ${this.name};
            |}
            """.trimMargin()
        }
    }

    fun Property.getter(): String {
        return if (this.isPatternProperty()) {

            """
            |${this.validationAnnotations()}
            |${this.type.toComment()}
            |@JsonAnyGetter
            |public Map<String, ${this.type.toVrapType().simpleName()}> values() {
            |    return values;
            |}
            """.trimMargin()
        } else {
            """
            |${this.type.toComment()}
            |${this.validationAnnotations()}
            |@JsonProperty("${this.name}")
            |public ${this.type.toVrapType().simpleName()} get${this.name.capitalize()}(){
            |   return this.${this.name};
            |}
        """.trimMargin()
        }
    }
}
