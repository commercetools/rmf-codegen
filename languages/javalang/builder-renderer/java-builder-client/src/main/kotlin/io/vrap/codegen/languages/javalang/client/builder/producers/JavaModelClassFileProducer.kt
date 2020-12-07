package io.vrap.codegen.languages.javalang.client.builder.producers

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.rendring.utils.keepSingleAngleIndent
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property


class JavaModelClassFileProducer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider, private val allObjectTypes: MutableList<ObjectType>) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content = """
                |package ${vrapType.`package`.toJavaPackage()};
                |
                |${type.imports()}
                |import io.vrap.rmf.base.client.utils.Generated;
                |import javax.validation.Valid;
                |import javax.validation.constraints.NotNull;
                |import java.util.*;
                |import java.time.*;
                |
                |import com.fasterxml.jackson.core.JsonProcessingException;
                |import com.fasterxml.jackson.databind.annotation.*;
                |import com.fasterxml.jackson.annotation.JsonInclude;
                |import com.fasterxml.jackson.annotation.JsonCreator;
                |import com.fasterxml.jackson.annotation.JsonProperty;
                |import org.apache.commons.lang3.builder.EqualsBuilder;
                |import org.apache.commons.lang3.builder.HashCodeBuilder;

                |
                |<${type.toComment().escapeAll()}>
                |<${JavaSubTemplates.generatedAnnotation}>
                |public final class ${vrapType.simpleClassName}Impl implements ${vrapType.simpleClassName} {
                |
                |    <${type.beanFields().escapeAll()}>
                |
                |    <${type.constructors().escapeAll()}>
                |
                |    <${type.getters().escapeAll()}>
                |
                |    <${type.setters().escapeAll()}>
                |
                |    <${type.equalsMethod().escapeAll()}>
                |
                |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Impl".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun Property.toJavaField(): String {

        val vrapType = this.type.toVrapType()
        return if (this.isPatternProperty()) {
            "private Map<String, ${vrapType.fullClassName()}> values;"
        } else {
            if(this.name.equals("interface")) {
                "private ${vrapType.fullClassName()} _interface;"
            } else {
                "private ${vrapType.fullClassName()} ${this.name.lowerCamelCase()};"
            }
        }
    }

    private fun ObjectType.equalsMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        return """
            |@Override
            |public boolean equals(Object o) {
            |    if (this == o) return true;
            |
            |    if (o == null || getClass() != o.getClass()) return false;
            |
            |    ${vrapType.simpleClassName}Impl that = (${vrapType.simpleClassName}Impl) o;
            |
            |    return new EqualsBuilder()
            |            <${this.allProperties.joinToString("\n") { it.equalsMethod() }}>
            |            .isEquals();
            |}
            |
            |@Override
            |public int hashCode() {
            |    return new HashCodeBuilder(17, 37)
            |        <${this.allProperties.joinToString("\n") { it.hashMethod() }}>
            |        .toHashCode();
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Property.equalsMethod(): String {
        if (this.isPatternProperty()) {
            return ".append(values, that.values)"
        } else if (this.name.equals("interface")) {
            return ".append(_interface, that._interface)"
        } else {
            return ".append(${this.name.lowerCamelCase()}, that.${this.name.lowerCamelCase()})"
        }
    }

    private fun Property.hashMethod(): String {
        if (this.isPatternProperty()) {
            return ".append(values)"
        } else if (this.name.equals("interface")) {
            return ".append(_interface)"
        } else {
            return ".append(${this.name.lowerCamelCase()})"
        }
    }

    private fun ObjectType.beanFields() = this.allProperties
            .map { it.toJavaField() }.joinToString(separator = "\n\n")

    private fun ObjectType.setters() = this.allProperties
            .filter { it.name != this.discriminator() }
            .map { it.setter() }
            .joinToString(separator = "\n\n")

    private fun ObjectType.getters() = this.allProperties
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    private fun Property.setter(): String {
        val vrapType = this.type.toVrapType()
        return if (this.isPatternProperty()) {
            """
            |public void setValue(String key, ${vrapType.fullClassName()} value) {
            |    if (values == null) {
            |        values = new HashMap<>();
            |    }
            |    values.put(key, value);
            |}
            """.trimMargin()
        } else if(this.name.equals("interface")) {
            """
                |public void setInterface(final ${vrapType.fullClassName()} _interface) {
                |    this._interface = _interface;
                |}
            """.trimMargin()
        } else if (vrapType is VrapArrayType) {
            """
            |public void set${this.name.upperCamelCase()}(final ${vrapType.itemType.fullClassName()} ...${this.name.lowerCamelCase()}){
            |   this.${this.name.lowerCamelCase()} = new ArrayList<>(Arrays.asList(${this.name.lowerCamelCase()}));
            |}
            |
            |public void set${this.name.upperCamelCase()}(final ${vrapType.fullClassName()} ${this.name.lowerCamelCase()}){
            |   this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
            |}
            """.trimMargin()
        }else {
            """
            |public void set${this.name.upperCamelCase()}(final ${vrapType.fullClassName()} ${this.name.lowerCamelCase()}){
            |    this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
            |}
            """.trimMargin()
        }
    }

    private fun Property.getter(): String {
        val vrapType = this.type.toVrapType()
        return if (this.isPatternProperty()) {
            """
            |${this.type.toComment()}
            |public Map<String,${vrapType.fullClassName()}> values() {
            |    return values;
            |}
            """.trimMargin()
        } else if(this.name.equals("interface")) {
            """
                |public ${vrapType.fullClassName()} getInterface() {
                |    return this._interface;
                |}
            """.trimMargin()
        } else {
            """
            |${this.type.toComment()}
            |public ${vrapType.fullClassName()} get${this.name.upperCamelCase()}(){
            |    return this.${this.name.lowerCamelCase()};
            |}
        """.trimMargin()
        }
    }

    private fun ObjectType.constructors(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        val constructorArguments = this.allProperties
                .filterNot { it.name == this.discriminator() && this.discriminatorValue != null}
                .map {
                    if(it.isPatternProperty()) {
                        "@JsonProperty(\"values\") final Map<String, ${it.type.toVrapType().fullClassName()}> values"
                    }else if(it.name.equals("interface")) {
                        "@JsonProperty(\"${it.name.lowerCamelCase()}\") final ${it.type.toVrapType().fullClassName()} _interface"
                    } else {
                        "@JsonProperty(\"${it.name}\") final ${it.type.toVrapType().fullClassName()} ${it.name.lowerCamelCase()}"
                    }
                }
                .joinToString(separator = ", ")

        val propertiesAssignment : String = this.allProperties
                .filterNot { it.name == this.discriminator() && this.discriminatorValue != null }
                .map {
                    if(it.isPatternProperty()){
                        "this.values = values;"
                    } else if (it.name.equals("interface")){
                        "this._interface = _interface;"
                    } else {
                        "this.${it.name.lowerCamelCase()} = ${it.name.lowerCamelCase()};"
                    }
                }
                .joinToString(separator = "\n")

        val discriminatorAssignment =
                if(this.discriminator() != null && this.discriminatorValue != null) {
                     val enumName : String = this.allProperties.filter { it.name == this.discriminator() }.get(0).type.toVrapType().simpleName()
                    if(enumName != "String"){
                        "this.${this.discriminator()} = $enumName.findEnum(\"${this.discriminatorValue}\");"
                    }else{
                        "this.${this.discriminator()} = \"${this.discriminatorValue}\";"
                    }
                } else {
                    ""
                }

        val emptyConstructor : String = """
            |public ${vrapType.simpleClassName}Impl() {
            |    <$discriminatorAssignment>
            |}
        """.trimMargin()

        return """
            |@JsonCreator
            |${vrapType.simpleClassName}Impl(${constructorArguments.escapeAll()}) {
            |    <$propertiesAssignment>
            |    <$discriminatorAssignment>
            |}
            |${if(constructorArguments.isEmpty()) "" else emptyConstructor }
        """.trimMargin().keepIndentation()
    }
}
