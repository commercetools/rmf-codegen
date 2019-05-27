package io.vrap.codegen.languages.java.file.producers

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.java.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

class JavaModelDraftBuilderFileProducer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider, private val allObjectTypes: MutableList<ObjectType>) : JavaObjectTypeExtensions, EObjectExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.filter { it.name.endsWith("Draft") }.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType
        
        val content : String = """
            |package ${vrapType.`package`};
            |
            |${type.imports()}
            |import com.commercetools.importer.models.Builder;
            |import ${vrapType.`package`}.${vrapType.simpleClassName};
            |import javax.annotation.Nullable;
            |import java.util.List;
            |
            |public final class ${vrapType.simpleClassName}Builder implements ${type.implements()} {
            |   
            |   <${type.fields().escapeAll()}>
            |   
            |   <${type.assignments().escapeAll()}>
            |   
            |   <${type.getters().escapeAll()}>
            |   
            |   @Override
            |   public ${vrapType.simpleClassName} build() {
            |       <${type.buildMethodBody().escapeAll()}>
            |   }
            |   
            |   <${type.staticOfMethod().escapeAll()}>
            |   
            |   <${type.templateMethod()}>
            |   
            |}
        """.trimMargin().keepIndentation()
        
        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Builder".replace(".", "/") + ".java",
                content = content
        )
    }
    
    private fun ObjectType.implements() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        return "Builder<${vrapType.simpleClassName}>".escapeAll()
    }
    
    private fun ObjectType.fields() = this.allProperties
            .map { it.toField() }
            .joinToString(separator = "\n\n")
    
    private fun Property.toField() : String {
        return """
            |${if(!this.required) "@Nullable" else ""}
            |private ${this.type.toVrapType().simpleName()} ${this.name};
        """.escapeAll().trimMargin().keepIndentation()
    }
    
    private fun ObjectType.assignments() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        
        return this.allProperties.map { assignment(it, vrapType) }.joinToString(separator = "\n\n")
    }
    
    private fun assignment(property: Property, type: VrapObjectType) : String {
        
        return """
            |public ${type.simpleClassName}Builder ${property.name}(${if(!property.required) "@Nullable" else ""} final ${property.type.toVrapType().simpleName()} ${property.name}) {
            |   this.${property.name} = ${property.name};
            |   return this;
            |}
        """.escapeAll().trimMargin().keepIndentation()
    }
    
    private fun ObjectType.getters() : String {
        return this.allProperties.map { it.getter() }.joinToString(separator = "\n\n")
    }
    
    private fun Property.getter() : String {
       return """
            |${if(!this.required) "@Nullable" else ""}
            |public ${this.type.toVrapType().simpleName()} get${this.name.capitalize()}(){
            |   return this.${this.name};
            |}
        """.escapeAll().trimMargin().keepIndentation()
    }
    
    private fun ObjectType.buildMethodBody() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        val constructorArguments = this.allProperties
                .map { it.name }
                .joinToString(separator = ", ")
        return "return new ${vrapType.simpleClassName}Impl($constructorArguments);"
    }
    
    private fun ObjectType.staticOfMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        
        return """
            |
            |public static ${vrapType.simpleClassName}Builder of() {
            |   return new ${vrapType.simpleClassName}Builder();
            |}
            |
        """.trimMargin().keepIndentation()
    }
    
    private fun ObjectType.templateMethod(): String {
        val vrapType = vrapTypeProvider.doSwitch(this) as VrapObjectType
        val fieldsAssignment : String =  this.allProperties
                .map { "builder.${it.name} = template.get${it.name.capitalize()}();" }
                .joinToString(separator = "\n")
        
        return """
            |public static ${vrapType.simpleClassName}Builder of(final ${vrapType.simpleClassName} template) {
            |   ${vrapType.simpleClassName}Builder builder = new ${vrapType.simpleClassName}Builder();
            |   <$fieldsAssignment>
            |   return builder;
            |}
        """.escapeAll().trimMargin().keepIndentation()
    }
}