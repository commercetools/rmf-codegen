package io.vrap.codegen.languages.java.model.second

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.java.JavaSubTemplates
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.codegen.languages.java.extensions.toComment
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject

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
            |import com.fasterxml.jackson.core.type.TypeReference;
            |import javax.annotation.Generated;
            |import javax.validation.constraints.NotNull;
            |import javax.validation.Valid;
            |import javax.validation.constraints.NotNull;
            |import java.util.List;
            |import java.util.Map;
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
            |   public static ${vrapType.simpleClassName}Impl of(${type.requiredProperties().escapeAll()}){
            |       <${type.staticOfMethodBody()}>
            |   }
            |   
            |   <${type.typeReference().escapeAll()}>
            |   
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
            content = content
        )
    }

    fun ObjectType.subTypesAnnotations(): String {
        return if (this.hasSubtypes())
            """
            |@JsonSubTypes({
            |   <${this.subTypes.map { "@JsonSubTypes.Type(value = ${it.toVrapType().simpleName()}.class, name = \"${(it as ObjectType).discriminatorValue}\")" }.joinToString(separator = ",\n")}>
            |})
            |@JsonTypeInfo(
            |   use = JsonTypeInfo.Id.NAME,
            |   include = JsonTypeInfo.As.PROPERTY,
            |   property = "${this.discriminator}"
            |)
            """.trimMargin()
        else
            ""
    }

    fun ObjectType.requiredProperties() : String = allRequiredProperties(this)
            .map { "final ${it.type.toVrapType().simpleName()} ${it.name}" }
            .joinToString(separator = ", ")

    fun ObjectType.staticOfMethodBody() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        val setters : String = allRequiredProperties(this)
            .map { "${vrapType.simpleClassName.decapitalize()}Impl.set${it.name.capitalize()}(${it.name});" }
            .joinToString(separator = "\n")
        return """
            |final ${vrapType.simpleClassName}Impl ${vrapType.simpleClassName.decapitalize()}Impl = new ${vrapType.simpleClassName}Impl();
            |<$setters>
            |return ${vrapType.simpleClassName.decapitalize()}Impl;
        """.trimMargin().keepIndentation()
    }

    /**
     * This method returns a list of required properties for the specified {@param rootObjectType} and for it's parent types.
     */
    private fun allRequiredProperties(rootObjectType : ObjectType) : List<Property> {
        var currentType: ObjectType? = rootObjectType
        val properties : MutableList<Property> = mutableListOf()
        while (currentType != null) {
            val currentTypeDiscriminator : String = currentType.discriminator ?: ""
            properties.addAll(currentType.properties.filter { it.name != currentTypeDiscriminator && it.required && !it.isPatternProperty()})
            currentType = if(currentType.type is ObjectType) {
                currentType.type as ObjectType
            }else{
                null
            }
        }
        return properties
    }

    fun ObjectType.getters() = this.properties
        .filter { it.name != this.discriminator }
        .map { it.geter() }
        .joinToString(separator = "\n")

    fun Property.geter(): String {
        return if(this.isPatternProperty()){
            """
            |${this.validationAnnotations()}
            |${this.type.toComment()}
            |@JsonAnyGetter
            |public Map<String, ${this.type.toVrapType().simpleName()}> values();
            """.trimMargin()
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

    fun Property.validationAnnotations(): String {
        val validationAnnotations = ArrayList<String>()
        if (this.required != null && this.required!!) {
            validationAnnotations.add("@NotNull")
        }
        if (JavaObjectTypeInterfaceRenderer.CascadeValidationCheck.doSwitch(this.type)) {
            validationAnnotations.add("@Valid")
        }
        return validationAnnotations.joinToString(separator = "\n")
    }
    
    fun ObjectType.typeReference() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return """
            |static TypeReference<${vrapType.simpleClassName}> typeReference() {
            |   return new TypeReference<${vrapType.simpleClassName}>(){
            |      @Override
            |      public String toString() {
            |         return "TypeReference<${vrapType.simpleClassName}>";
            |      }
            |   };
            |}
            |
        """.trimMargin()
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
                JavaObjectTypeInterfaceRenderer.CascadeValidationCheck.doSwitch(arrayType.items)
            } else {
                false
            }
        }
    }
}
