package io.vrap.codegen.languages.java.model.second

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.JavaSubTemplates
import io.vrap.codegen.languages.java.extensions.simpleName
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

class JavaModelInterfaceRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeRenderer() {

    override fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content= """
            |package ${vrapType.`package`};
            |
            |${type.imports()}
            |${type.subclassImport()}
            |
            |import com.fasterxml.jackson.annotation.*;
            |import com.fasterxml.jackson.databind.annotation.*;
            |import javax.annotation.Generated;
            |import javax.validation.Valid;
            |import javax.validation.constraints.NotNull;
            |import javax.annotation.Nullable;
            |import java.util.List;
            |import java.util.Map;
            |import java.time.*;
            |
            |<${type.toComment().escapeAll()}>
            |<${type.subTypesAnnotations()}>
            |<${JavaSubTemplates.generatedAnnotation}>
            |<${type.jsonDeserialize()}>
            |public interface ${vrapType.simpleClassName} ${type.type?.toVrapType()?.simpleName()?.let { "extends $it" } ?: ""} {
            |
            |   <${type.getters().escapeAll()}>
            |
            |   <${type.setters().escapeAll()}>
            |
            |   <${type.staticOfMethod()}>
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

    private fun ObjectType.subclassImport() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()){
            ""
        }else {
            "import ${vrapType.`package`}.${vrapType.simpleClassName}Impl;"    
        }
    }
    
    private fun ObjectType.subTypesAnnotations(): String {
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

    private fun ObjectType.staticOfMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType

        val constructorArguments : String = this.allProperties
                .filter { it.name != this.discriminator() }
                .map { if(it.isPatternProperty()) "final Map<String, ${it.type.toVrapType().simpleName()}> values"
                else if(!it.required) "@Nullable final ${it.type.toVrapType().simpleName()} ${it.name}"
                else "final ${it.type.toVrapType().simpleName()} ${it.name}"}
                .joinToString(separator = ", ")

        val constructorValues : String = this.allProperties
                .filter { it.name != this.discriminator() }
                .map { if(it.isPatternProperty()) "values" else it.name }
                .joinToString(separator = ", ")

        return if(this.isAbstract()) {
            ""
        }else {
            """
                |public static ${vrapType.simpleClassName}Impl of(${constructorArguments.escapeAll()}){
                |   return new ${vrapType.simpleClassName}Impl($constructorValues);
                |}
                |
             """.trimMargin()
        }
    }
    
    /**
     * This method returns a list of required properties for the specified {@param rootObjectType} and for it's parent types.
     */
    private fun allRequiredProperties(rootObjectType : ObjectType) : List<Property> {
        var currentType: ObjectType? = rootObjectType
        val properties : MutableList<Property> = mutableListOf()
        while (currentType != null) {
            val currentTypeDiscriminator : String = currentType.discriminator() ?: ""
            properties.addAll(currentType.properties.filter { it.name != currentTypeDiscriminator && it.required && !it.isPatternProperty()})
            currentType = if(currentType.type is ObjectType) {
                currentType.type as ObjectType
            }else{
                null
            }
        }
        return properties
    }
    
    private fun ObjectType.getters() = this.properties
        .map { it.geter() }
        .joinToString(separator = "\n")

    private fun Property.geter(): String {
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

    private fun ObjectType.setters() = this.properties
        .filter { it.name != this.discriminator() }
        .map { it.setter() }
        .joinToString(separator = "\n\n")

    private fun Property.setter(): String {
        return if (this.isPatternProperty()) {
            """
            |@JsonAnySetter
            |public void setValue(String key, ${this.type.toVrapType().simpleName()} value);
            """.trimMargin()
        } else {
            "public void set${this.name.capitalize()}(final ${this.type.toVrapType().simpleName()} ${this.name});"
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
        return validationAnnotations.joinToString(separator = "\n")
    }
    
    private fun ObjectType.typeReference() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return """
            |static com.fasterxml.jackson.core.type.TypeReference<${vrapType.simpleClassName}> typeReference() {
            |   return new com.fasterxml.jackson.core.type.TypeReference<${vrapType.simpleClassName}>(){
            |      @Override
            |      public String toString() {
            |         return "TypeReference<${vrapType.simpleClassName}>";
            |      }
            |   };
            |}
            |
        """.trimMargin()
    }
    
    private fun ObjectType.jsonDeserialize() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return if(this.isAbstract()){
            ""
        }else{
            "@JsonDeserialize(as = ${vrapType.simpleClassName}Impl.class)"    
        }
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
