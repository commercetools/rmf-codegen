package io.vrap.codegen.languages.javalang.client.builder.producers

import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.di.AllObjectTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation


class JavaModelClassFileProducer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllObjectTypes private val allObjectTypes: List<ObjectType>) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.filter{!it.deprecated()}.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val implements = Lists.newArrayList(vrapType.simpleClassName, "ModelBase")
                .plus(
                        when (val ex = type.getAnnotation("java-impl-implements") ) {
                            is Annotation -> {
                                (ex.value as StringInstance).value.escapeAll()
                            }
                            else -> null
                        }
                )
                .filterNotNull()

        val content = """
                |package ${vrapType.`package`.toJavaPackage()};
                |
                |${type.imports()}
                |import io.vrap.rmf.base.client.utils.Generated;
                |import io.vrap.rmf.base.client.ModelBase;
                |import jakarta.validation.Valid;
                |import jakarta.validation.constraints.NotNull;
                |import java.util.*;
                |import java.time.*;
                |
                |import com.fasterxml.jackson.core.JsonProcessingException;
                |import com.fasterxml.jackson.databind.annotation.*;
                |import com.fasterxml.jackson.annotation.JsonCreator;
                |import com.fasterxml.jackson.annotation.JsonIgnore;
                |import com.fasterxml.jackson.annotation.JsonInclude;
                |import com.fasterxml.jackson.annotation.JsonProperty;
                |import org.apache.commons.lang3.builder.EqualsBuilder;
                |import org.apache.commons.lang3.builder.HashCodeBuilder;
                |import org.apache.commons.lang3.builder.ToStringBuilder;
                |import org.apache.commons.lang3.builder.ToStringStyle;
                |
                |/**
                |${type.toComment(" * ${vrapType.simpleClassName}").escapeAll()}
                | */
                |<${JavaSubTemplates.generatedAnnotation}>${if (type.markDeprecated()) """
                |@Deprecated""" else ""}
                |public class ${vrapType.simpleClassName}Impl implements ${implements.joinToString(separator = ", ")} {
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
                |    <${type.getAnnotation("java-impl-mixin")?.value?.value?.let { (it as String).escapeAll()} ?: ""}>
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
            """
                |${this.deprecationAnnotation()}
                |private Map<String, ${vrapType.fullClassName()}> values;""".trimMargin()
        } else {
            if(this.name.equals("interface")) {
                """
                |${this.deprecationAnnotation()}
                |private ${vrapType.fullClassName()} _interface;""".trimMargin()
            } else {
                """
                |${this.deprecationAnnotation()}
                |private ${vrapType.fullClassName()} ${this.name.lowerCamelCase()};""".trimMargin()
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
            |            <${this.allProperties.filterNot { it.deprecated() }.joinToString("\n") { it.equalsMethod() }}>
            |            <${this.allProperties.filterNot { it.deprecated() }.joinToString("\n") { it.equalsMethod() }}>
            |            .isEquals();
            |}
            |
            |@Override
            |public int hashCode() {
            |    return new HashCodeBuilder(17, 37)
            |        <${this.allProperties.filterNot { it.deprecated() }.joinToString("\n") { it.hashMethod() }}>
            |        .toHashCode();
            |}
            |
            |@Override
            |public String toString() {
            |    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            |        <${this.allProperties.filterNot { it.deprecated() }.joinToString("\n") { it.toStringMethod() }}>
            |        .build();
            |}
        """.trimMargin().keepIndentation()
    }

    private fun Property.equalsMethod(): String {
        return if (this.isPatternProperty()) {
            ".append(values, that.values)"
        } else if (this.name.equals("interface")) {
            ".append(_interface, that._interface)"
        } else {
            ".append(${this.name.lowerCamelCase()}, that.${this.name.lowerCamelCase()})"
        }
    }

    private fun Property.hashMethod(): String {
        return if (this.isPatternProperty()) {
            ".append(values)"
        } else if (this.name.equals("interface")) {
            ".append(_interface)"
        } else {
            ".append(${this.name.lowerCamelCase()})"
        }
    }

    private fun Property.toStringMethod(): String {
        return if (this.isPatternProperty()) {
            ".append(\"values\", values)"
        } else if (this.name.equals("interface")) {
            ".append(\"interface\", _interface)"
        } else {
            ".append(\"${this.name.lowerCamelCase()}\", ${this.name.lowerCamelCase()})"
        }
    }

    private fun ObjectType.beanFields() = this.allProperties
            .filterNot { it.deprecated() }
            .map { it.toJavaField() }.joinToString(separator = "\n\n")

    private fun ObjectType.setters() = this.allProperties
            .filterNot { it.deprecated() }
            .filter { it.name != this.discriminator() }
            .map { it.setter() }
            .joinToString(separator = "\n\n")

    private fun ObjectType.getters() = this.allProperties
            .filterNot { it.deprecated() }
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    private fun Property.setter(): String {
        val vrapType = this.type.toVrapType()
        return if (this.isPatternProperty()) {
            """
            |${this.deprecationAnnotation()}
            |public void setValue(String key, ${vrapType.fullClassName()} value) {
            |    if (values == null) {
            |        values = new HashMap<>();
            |    }
            |    values.put(key, value);
            |}
            """.trimMargin()
        } else if(this.name.equals("interface")) {
            """
                |${this.deprecationAnnotation()}
                |public void setInterface(final ${vrapType.fullClassName()} _interface) {
                |    this._interface = _interface;
                |}
            """.trimMargin()
        } else if (vrapType is VrapArrayType) {
            """
            |${this.deprecationAnnotation()}
            |public void set${this.name.upperCamelCase()}(final ${vrapType.itemType.fullClassName()} ...${this.name.lowerCamelCase()}){
            |   this.${this.name.lowerCamelCase()} = new ArrayList<>(Arrays.asList(${this.name.lowerCamelCase()}));
            |}
            |
            |${this.deprecationAnnotation()}
            |public void set${this.name.upperCamelCase()}(final ${vrapType.fullClassName()} ${this.name.lowerCamelCase()}){
            |   this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
            |}
            """.trimMargin()
        } else if (this.type is UnionType) {
            (this.type as UnionType).oneOf
                .map { anyType -> """
                    |${this.deprecationAnnotation()}
                    |@JsonIgnore
                    |public void set${this.name.upperCamelCase()}(final ${anyType.toVrapType().simpleName()} ${this.name.lowerCamelCase()}) {
                    |    this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
                    }""".trimMargin() }
                .plus("""
                    |${this.deprecationAnnotation()}
                    |public void set${this.name.upperCamelCase()}(final ${vrapType.fullClassName()} ${this.name.lowerCamelCase()}){
                    |    this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
                    |}
                    """.trimMargin())
                .joinToString("\n");
        } else {
            """
            |${this.deprecationAnnotation()}
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
            |/**
            |${this.type.toComment(" *")}
            | */
            |${this.deprecationAnnotation()}
            |public Map<String,${vrapType.fullClassName()}> values() {
            |    return values;
            |}
            """.trimMargin()
        } else if(this.name.equals("interface")) {
            """
                |/**
                |${this.type.toComment(" *")}
                | */
                |${this.deprecationAnnotation()}
                |public ${vrapType.fullClassName()} getInterface() {
                |    return this._interface;
                |}
            """.trimMargin()
        } else {
            """
            |/**
            |${this.type.toComment(" *")}
            | */
            |${this.deprecationAnnotation()}
            |public ${vrapType.fullClassName()} get${this.name.upperCamelCase()}(){
            |    return this.${this.name.lowerCamelCase()};
            |}
        """.trimMargin()
        }
    }

    private fun ObjectType.constructors(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        val constructorArguments = this.allProperties
                .filterNot { it.deprecated() }
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
                .filterNot { it.deprecated() }
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
                        "this.${this.discriminator()} =  ${this.discriminatorValue.enumValueName()};"
                    }
                } else {
                    ""
                }

        val emptyConstructor : String = """
            |/**
            | * create empty instance
            | */
            |public ${vrapType.simpleClassName}Impl() {
            |    <$discriminatorAssignment>
            |}
        """.trimMargin()

        return """
            |/**
            | * create instance with all properties
            | */
            |@JsonCreator
            |${if(constructorArguments.isEmpty()) "public " else ""}${vrapType.simpleClassName}Impl(${constructorArguments.escapeAll()}) {
            |    <$propertiesAssignment>
            |    <$discriminatorAssignment>
            |}
            |${if(constructorArguments.isEmpty()) "" else emptyConstructor }
        """.trimMargin().keepIndentation()
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private fun ObjectType.deprecated() : Boolean {
        val anno = this.getAnnotation("deprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }
}
