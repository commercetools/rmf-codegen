package io.vrap.codegen.languages.java.file.producers

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.JavaSubTemplates
import io.vrap.codegen.languages.java.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.lowerCamelCase
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.codegen.languages.java.extensions.upperCamelCase
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property


class JavaModelClassFileProducer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider, private val allObjectTypes: MutableList<ObjectType>) : JavaObjectTypeExtensions, EObjectExtensions, FileProducer {
    
    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.filter { !it.isAbstract() }.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        
        val content = """
                |package ${vrapType.`package`};
                |
                |${type.imports()}
                |import com.fasterxml.jackson.annotation.JsonCreator;
                |import javax.annotation.Generated;
                |import javax.validation.Valid;
                |import javax.validation.constraints.NotNull;
                |import java.util.*;
                |import java.time.*;
                |import java.util.List;
                |import java.util.Map;
                |
                |<${type.toComment().escapeAll()}>
                |<${JavaSubTemplates.generatedAnnotation}>
                |public class ${vrapType.simpleClassName}Impl implements ${vrapType.simpleClassName} {
                |
                |   <${type.finalBeanFields().escapeAll()}>
                |   
                |   <${type.beanFields().escapeAll()}>
                |
                |   <${type.constructors().escapeAll()}>
                |   
                |   <${type.getters().escapeAll()}>
                |
                |   <${type.setters().escapeAll()}>
                |
                |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Impl".replace(".", "/") + ".java",
                content = content
        )
    }
    
    private fun Property.packageName() : String {
        return if(this.type is ObjectType) {
            "${(this.type.toVrapType() as VrapObjectType).`package`}."
        } else {
            ""
        }
    }
    
    private fun Property.toJavaField(): String {

        return if (this.isPatternProperty()) {
            "private Map<String, ${this.packageName()}${this.type.toVrapType().simpleName()}> values;"
        } else {
            "private ${this.packageName()}${this.type.toVrapType().simpleName()} ${if (this.isPatternProperty()) "values" else this.name.lowerCamelCase()};"
        }

    }

    private fun Property.toFinalJavaField() : String {
        return if (this.isPatternProperty()) {
            "private final Map<String, ${this.type.toVrapType().simpleName()}> values;"
        } else {
            "private final ${this.type.toVrapType().simpleName()} ${if (this.isPatternProperty()) "values" else this.name.lowerCamelCase()};"
        }
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
            |public void setValue(String key, ${this.packageName()}${this.type.toVrapType().simpleName()} value) {
            |    if (values == null) {
            |        values = new HashMap<>();
            |    }
            |    values.put(key, value);
            |}
            """.trimMargin()
        } else {
            """
            |public void set${this.name.upperCamelCase()}(final ${this.packageName()}${this.type.toVrapType().simpleName()} ${this.name.lowerCamelCase()}){
            |   this.${this.name.lowerCamelCase()} = ${this.name.lowerCamelCase()};
            |}
            """.trimMargin()
        }
    }

    private fun Property.getter(): String {
        return if (this.isPatternProperty()) {
            """
            |${this.type.toComment()}
            |public Map<String, ${this.packageName()}${this.type.toVrapType().simpleName()}> values() {
            |    return values;
            |}
            """.trimMargin()
        } else {
            """
            |${this.type.toComment()}
            |public ${this.packageName()}${this.type.toVrapType().simpleName()} get${this.name.upperCamelCase()}(){
            |   return this.${this.name.lowerCamelCase()};
            |}
        """.trimMargin()
        }
    }
    
    private fun ObjectType.constructors(): String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        val constructorArguments = this.allProperties
                .filter { it.name != this.discriminator() }
                .map { if(it.isPatternProperty()) "final Map<String, ${it.packageName()}${it.type.toVrapType().simpleName()}> values" else "final ${it.packageName()}${it.type.toVrapType().simpleName()} ${it.name.lowerCamelCase()}" }
                .joinToString(separator = ", ")

        val discriminatorAssignment : String =
                if(this.discriminator() != null) {
                    //if the type of a discriminator is an enum, and discriminatorValue is a String, we have to call EnumName.valueOf(discriminatorValue)
                    val enumName : String = this.allProperties.filter { it.name == this.discriminator() }.get(0).type.toVrapType().simpleName()
                    if(enumName != "String"){
                        "this.${this.discriminator()} = $enumName.valueOf(\"${this.discriminatorValue}\");"
                    }else{
                        "this.${this.discriminator()} = \"${this.discriminatorValue}\";"
                    }
                } else {
                    ""
                }

        val propertiesAssignment : String = this.allProperties
                .filter { it.name != this.discriminator() }
                .map { if(it.isPatternProperty()) "this.values = values;" else "this.${it.name.lowerCamelCase()} = ${it.name.lowerCamelCase()};" }
                .joinToString(separator = "\n")

        val emptyConstructor : String = """
            |
            |public ${vrapType.simpleClassName}Impl() {
            |   <$discriminatorAssignment>
            |}
        """.trimMargin()
        
        return """
            |
            |@JsonCreator
            |${vrapType.simpleClassName}Impl(${constructorArguments.escapeAll()}) {
            |   <$discriminatorAssignment>
            |   <$propertiesAssignment>
            |}
            |${if(constructorArguments.isEmpty()) "" else emptyConstructor}
        """.trimMargin().keepIndentation()
    }

}