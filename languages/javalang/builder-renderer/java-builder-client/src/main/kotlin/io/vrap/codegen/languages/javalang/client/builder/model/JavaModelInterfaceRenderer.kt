package io.vrap.codegen.languages.javalang.client.builder.model

import com.google.common.collect.Lists
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.firstLowerCase
import io.vrap.rmf.codegen.firstUpperCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject

class JavaModelInterfaceRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val extends = Lists.newArrayList(type.type?.toVrapType()?.simpleName())
                .plus(
                    when (val ex = type.getAnnotation("java-extends") ) {
                        is Annotation -> {
                            (ex.value as StringInstance).value.escapeAll()
                        }
                        else -> null
                    }
                )
                .plus(
                    if (type.name.endsWith("Draft") && !type.hasSubtypes()) {
                        "io.vrap.rmf.base.client.Draft<${vrapType.simpleClassName}>".escapeAll()
                    } else {
                        null
                    }
                )
            .filterNotNull()

        val content= """
            |package ${vrapType.`package`};
            |
            |${type.imports()}
            |${type.subclassImport()}
            |
            |import com.fasterxml.jackson.annotation.*;
            |import com.fasterxml.jackson.databind.annotation.*;
            |import io.vrap.rmf.base.client.utils.Generated;
            |import io.vrap.rmf.base.client.Accessor;
            |import javax.validation.Valid;
            |import javax.validation.constraints.NotNull;
            |import java.util.*;
            |import java.time.*;
            |import java.util.function.Function;
            |import java.io.IOException;
            |
            |/**
            |${type.toComment(" * ${vrapType.simpleClassName}").escapeAll()}
            | *
            | * \<hr\>
            | <${type.builderComment().escapeAll()}> 
            | */
            |<${type.subTypesAnnotations()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |<${type.jsonDeserialize()}>${if (type.markDeprecated()) """
            |@Deprecated""" else ""}
            |public interface ${vrapType.simpleClassName} ${if (extends.isNotEmpty()) { "extends ${extends.joinToString(separator = ", ")}" } else ""} {
            |
            |    <${type.discriminatorValueConst()}>
            |
            |    <${type.getters().escapeAll()}>
            |
            |    <${type.setters().escapeAll()}>
            |
            |    <${type.staticOfMethod()}>
            |
            |    <${type.templateMethodBody()}>
            |
            |    <${type.staticBuilderMethod()}>
            |
            |    <${type.subTypeBuilders().escapeAll()}>
            |
            |    default \<T\> T with${vrapType.simpleClassName}(Function\<${vrapType.simpleClassName}, T\> helper) {
            |        return helper.apply(this);
            |    }
            |    <${type.getAnnotation("java-mixin")?.value?.value?.let { (it as String).escapeAll()} ?: ""}>
            |    
            |    <${type.typeReference()}>
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
            content = content
        )
    }

    private fun ObjectType.typeReference(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        return """
            |public static com.fasterxml.jackson.core.type.TypeReference<${vrapType.simpleClassName}> typeReference() {
            |    return new com.fasterxml.jackson.core.type.TypeReference<${vrapType.simpleClassName}>() {
            |        @Override
            |        public String toString() {
            |            return "TypeReference<${vrapType.simpleClassName}>";
            |        }
            |    };
            |}
        """.trimMargin().escapeAll()
    }

    private fun ObjectType.subclassImport() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()){
            ""
        }else {
            "import ${vrapType.`package`.toJavaPackage()}.${vrapType.simpleClassName}Impl;"
        }
    }

    private fun ObjectType.discriminatorValueConst(): String {
        if (this.discriminatorValue == null) return "";

        return "String ${this.discriminatorValue.enumValueName()} = \"${this.discriminatorValue}\";";
    }

    private fun ObjectType.subTypesAnnotations(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        return if (this.hasSubtypes())
            """
            |@JsonSubTypes({
            |   <${
                this.subTypes.plus(this.subTypes.flatMap { it.subTypes }).distinctBy { it.name }
                    .asSequence()
                    .filterIsInstance<ObjectType>()
                    .filter { it.getAnnotation("deprecated") == null }
                    .filter { it.discriminatorValue != null }
                    .sortedBy { anyType -> anyType.name }
                    .map {
                        val vrapSubType = vrapTypeProvider.doSwitch(it) as VrapObjectType
                        "@JsonSubTypes.Type(value = ${vrapSubType.`package`.toJavaPackage()}.${vrapSubType.simpleClassName}Impl.class, name = ${vrapSubType.simpleClassName}.${it.discriminatorValue.enumValueName()})" 
                    }
                    .joinToString(separator = ",\n")}>
            |})
            |@JsonTypeInfo(
            |    use = JsonTypeInfo.Id.NAME,
            |    include = JsonTypeInfo.As.EXISTING_PROPERTY,
            |    property = "${this.discriminator}",
            |    defaultImpl = ${vrapType.simpleClassName}Impl.class,
            |    visible = true
            |)
            |@JsonDeserialize(as = ${vrapType.simpleClassName}Impl.class)
            """.trimMargin()
        else
            ""
    }

    private fun ObjectType.staticOfMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()) {
            ""
        }else {
            """
                |public static ${vrapType.simpleClassName} of(){
                |    return new ${vrapType.simpleClassName}Impl();
                |}
                |
             """.trimMargin()
        }
    }

    private fun ObjectType.staticBuilderMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()) {
            ""
        }else {
            """
                |public static ${vrapType.simpleClassName}Builder builder() {
                |    return ${vrapType.simpleClassName}Builder.of();
                |}
                |
                |public static ${vrapType.simpleClassName}Builder builder(final ${vrapType.simpleClassName} template) {
                |    return ${vrapType.simpleClassName}Builder.of(template);
                |}
             """.trimMargin()
        }
    }

    private fun ObjectType.subTypeBuilders() : String {
        if (this.hasSubtypes()) {
            return this.subTypes.plus(this.subTypes.flatMap { it.subTypes }).distinctBy { it.name }
                .asSequence()
                .filterIsInstance<ObjectType>()
                .filter { it.getAnnotation("deprecated") == null }
                .filter { it.discriminatorValue != null }
                .sortedBy { anyType -> anyType.name }
                .map {
                    val vrapSubType = vrapTypeProvider.doSwitch(it) as VrapObjectType
                    """
                    |public static ${vrapSubType.`package`.toJavaPackage()}.${vrapSubType.simpleClassName}Builder ${it.discriminatorValue.lowerCamelCase()}Builder() {
                    |   return ${vrapSubType.`package`.toJavaPackage()}.${vrapSubType.simpleClassName}Builder.of();
                    |}
                    """.trimMargin()
                }
                .joinToString(separator = "\n")
        }
        return ""
    }


    private fun ObjectType.getters() = this.properties
            .filter { it.getAnnotation("deprecated") == null }
            .map { it.getter() }
            .joinToString(separator = "\n")

    private fun Property.getter(): String {
         return if(this.isPatternProperty()){
            """
            |/**
            |${this.type.toComment(" *")}
            | * @return map of the pattern property values
            | */
            |${this.validationAnnotations()}
            |@JsonAnyGetter
            |public Map<String, ${this.type.toVrapType().simpleName()}> values();
            """.trimMargin()
        }else {
            """
            |/**
            |${this.type.toComment(" *")}
            | * @return ${this.name}
            | */
            |${this.validationAnnotations()}
            |@JsonProperty("${this.name}")
            |public ${this.type.toVrapType().simpleName()} get${this.name.upperCamelCase()}();
        """.trimMargin()
        }
    }

    private fun ObjectType.setters() = this.properties
            .filter { it.getAnnotation("deprecated") == null }
            .filter { it.name != this.discriminator() }
            .map { it.setter() }
            .joinToString(separator = "\n\n")

    private fun Property.setter(): String {
        val vrapType = this.type.toVrapType()
        return if (this.isPatternProperty()) {
            """
            |${this.deprecationAnnotation()}
            |@JsonAnySetter
            |public void setValue(String key, ${vrapType.simpleName()} value);
            """.trimMargin()
        } else if (this.name.equals("interface")) {
            """
            |${this.deprecationAnnotation()}
            |public void setInterface(final ${vrapType.simpleName()} _interface);
            |""".trimMargin()
        } else if (vrapType is VrapArrayType) {
            """
            |${this.deprecationAnnotation()}
            |@JsonIgnore
            |public void set${this.name.upperCamelCase()}(final ${vrapType.itemType.simpleName()} ...${this.name.lowerCamelCase()});
            |public void set${this.name.upperCamelCase()}(final ${vrapType.simpleName()} ${this.name.lowerCamelCase()});
            """.trimMargin()
        } else if (this.type is UnionType) {
            (this.type as UnionType).oneOf
                .map { anyType -> """
                    |${this.deprecationAnnotation()}
                    |public void set${this.name.upperCamelCase()}(final ${anyType.toVrapType().simpleName()} ${this.name.lowerCamelCase()});""".trimMargin() }
                .plus("""
                    |${this.deprecationAnnotation()}
                    |public void set${this.name.upperCamelCase()}(final ${vrapType.simpleName()} ${this.name.lowerCamelCase()});""".trimMargin())
                .joinToString("\n");
        } else {
            """
            |${this.deprecationAnnotation()}
            |public void set${this.name.upperCamelCase()}(final ${vrapType.simpleName()} ${this.name.lowerCamelCase()});
            |""".trimMargin()
        }
    }

    private fun Property.validationAnnotations(): String {
        val validationAnnotations = ArrayList<String>()
        if (this.required != null && this.required!!) {
            validationAnnotations.add("@NotNull")
        }
        if (CascadeValidationCheck.doSwitch(this.type)) {
            validationAnnotations.add("@Valid")
        }
        return validationAnnotations.plus(deprecationAnnotation().trim()).filterNot { it.isEmpty()}.joinToString(separator = "\n")
    }

    private fun ObjectType.jsonDeserialize() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()){
            ""
        }else{
            "@JsonDeserialize(as = ${vrapType.simpleClassName}Impl.class)"
        }
    }

    private fun ObjectType.templateMethodBody(): String {
        return if(this.isAbstract()) {
            ""
        }else {
            val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
            val fieldsAssignment : String = this.allProperties
                    .filter { it.getAnnotation("deprecated") == null }
                    .filter {it.name != this.discriminator()}
                    .map {
                        if(!it.isPatternProperty()){
                            "instance.set${it.name.upperCamelCase()}(template.get${it.name.upperCamelCase()}());"
                        }else{
                            "template.values().forEach((s, o) -> instance.setValue(s, o));".escapeAll()
                            ""
                        }
                    }
                    .joinToString(separator = "\n")

            """
            |public static ${vrapType.simpleClassName} of(final ${vrapType.simpleClassName} template) {
            |    ${vrapType.simpleClassName}Impl instance = new ${vrapType.simpleClassName}Impl();
            |    <$fieldsAssignment>
            |    return instance;
            |}
        """.trimMargin()
        }
    }

    private fun ObjectType.markDeprecated() : Boolean {
        val anno = this.getAnnotation("markDeprecated")
        return (anno != null && (anno.value as BooleanInstance).value)
    }

    private object CascadeValidationCheck : TypesSwitch<Boolean>() {
        override fun defaultCase(`object`: EObject?): Boolean? {
            return false
        }

        override fun caseObjectType(objectType: ObjectType?): Boolean? {
            return true
        }

        override fun caseArrayType(arrayType: ArrayType): Boolean? {
            return if (arrayType.items != null) {
                doSwitch(arrayType.items)
            } else {
                false
            }
        }
    }
}
