package io.vrap.codegen.languages.java.model.second

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.JavaSubTemplates
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

class JavaModelClassRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeRenderer() {

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        
        if(type.isAbstract()){
            return TemplateFile(
                    relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Impl".replace(".", "/") + ".java",
                    content = ""
            )
        }
        
        val content = """
                |package ${vrapType.`package`};
                |
                |${type.imports()}
                |import com.fasterxml.jackson.annotation.JsonCreator;
                |import javax.annotation.Generated;
                |import javax.validation.Valid;
                |import javax.validation.constraints.NotNull;
                |import java.util.*;
                |import org.apache.commons.lang3.builder.EqualsBuilder;
                |import org.apache.commons.lang3.builder.HashCodeBuilder;
                |import org.apache.commons.lang3.builder.ToStringBuilder;
                |import org.apache.commons.lang3.builder.ToStringStyle;
                |import java.util.List;
                |import java.util.Map;
                |
                |<${type.toComment().escapeAll()}>
                |<${JavaSubTemplates.generatedAnnotation}>
                |public class ${vrapType.simpleClassName}Impl ${type.extendsStatement()} implements ${vrapType.simpleClassName} {
                |
                |   <${type.finalBeanFields().escapeAll()}>
                |   
                |   <${type.beanFields().escapeAll()}>
                |
                |   <${type.constructor().escapeAll()}>
                |   
                |   <${type.getters().escapeAll()}>
                |
                |   <${type.setters().escapeAll()}>
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
    
    private fun Property.toJavaField(): String {

        return if (this.isPatternProperty()) {
            "private Map<String,${this.type.toVrapType().simpleName()}> values;"
        } else {
            "private ${this.type.toVrapType().simpleName()} ${if (this.isPatternProperty()) "values" else this.name};"
        }

    }
    
    private fun Property.toFinalJavaField() : String {
        return if (this.isPatternProperty()) {
            "private final Map<String,${this.type.toVrapType().simpleName()}> values;"
        } else {
            "private final ${this.type.toVrapType().simpleName()} ${if (this.isPatternProperty()) "values" else this.name};"
        }
    }

    private fun ObjectType.extendsStatement() : String {
        if(this.type is ObjectType){
            return type.type?.toVrapType()?.simpleName()?.let { "extends ${it}Impl" } ?: ""
        }
        return ""
    }
    
    private fun ObjectType.beanFields() = this.allProperties
            .filter { it.name != this.discriminator() }
            .map { it.toJavaField() }.joinToString(separator = "\n\n")

    private fun ObjectType.finalBeanFields() = this.allProperties
            .filter { it.name == this.discriminator() }
            .map { it.toFinalJavaField() }.joinToString(separator = "\n\n")
    
    private fun ObjectType.setters() = this.allProperties
        //Filter the discriminators because they don't make much sense the generated bean
        .filter { it.name != this.discriminator() }
        .map { it.setter() }
        .joinToString(separator = "\n\n")
    
    private fun ObjectType.getters() = this.allProperties
        //Filter the discriminators because they don't make much sense the generated bean
        .map { it.getter() }
        .joinToString(separator = "\n\n")
    
    private fun Property.setter(): String {
        return if (this.isPatternProperty()) {
            """
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

    private fun Property.getter(): String {
        return if (this.isPatternProperty()) {
            """
            |${this.type.toComment()}
            |public Map<String, ${this.type.toVrapType().simpleName()}> values() {
            |    return values;
            |}
            """.trimMargin()
        } else {
            """
            |${this.type.toComment()}
            |public ${this.type.toVrapType().simpleName()} get${this.name.capitalize()}(){
            |   return this.${this.name};
            |}
        """.trimMargin()
        }
    }

    private fun ObjectType.constructor(): String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        val constructorArguments = this.properties
                .filter { it.name != this.discriminator() }
                .map { if(it.isPatternProperty()) "final Map<String, ${it.type.toVrapType().simpleName()}> values" else "final ${it.type.toVrapType().simpleName()} ${it.name}" }
                .joinToString(separator = ", ")
        
        val discriminatorAssignment : String = 
                if(this.discriminator() != null) {
                    "this.${this.discriminator()} = \"${this.discriminatorValue}\";"
                } else {
                    ""
                }
        
        val propertiesAssignment : String = this.properties
                .map { if(it.isPatternProperty()) "this.values = values;" else "this.${it.name} = ${it.name};" }
                .joinToString(separator = "\n")
        
        return """
            |
            |@JsonCreator
            |${vrapType.simpleClassName}Impl(${constructorArguments.escapeAll()}){
            |   <$discriminatorAssignment>
            |   <$propertiesAssignment>
            |}
            |
        """.trimMargin().keepIndentation()
    }
}