package io.vrap.codegen.languages.javalang.client.builder.producers

import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.java.base.JavaBaseTypes
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.di.AllObjectTypes
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.FileProducer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.LanguageBaseTypes
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import javax.lang.model.SourceVersion


class JavaModelDraftBuilderFileProducer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllObjectTypes private val allObjectTypes: List<ObjectType>) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, FileProducer {

    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.filter { !it.isAbstract() }.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val content : String = """
            |package ${vrapType.`package`};
            |
            |${type.imports()}
            |import ${vrapType.`package`}.${vrapType.simpleClassName};
            |import javax.annotation.Nullable;
            |import java.util.*;
            |import java.util.function.Function;
            |import java.time.ZonedDateTime;
            |import io.vrap.rmf.base.client.Builder;
            |import io.vrap.rmf.base.client.utils.Generated;
            |
            |<${JavaSubTemplates.generatedAnnotation}>
            |public final class ${vrapType.simpleClassName}Builder implements Builder\<${vrapType.simpleClassName}\> {
            |
            |    <${type.fields().escapeAll()}>
            |
            |    <${type.assignments().escapeAll()}>
            |
            |    <${type.getters().escapeAll()}>
            |
            |    public ${vrapType.simpleClassName} build() {
            |        <${type.requiredChecks().escapeAll()}>
            |        <${type.buildMethodBody().escapeAll()}>
            |    }
            |    
            |    /**
            |     * builds ${vrapType.simpleClassName} without checking for non null required values
            |     */
            |    public ${vrapType.simpleClassName} buildUnchecked() {
            |        <${type.buildMethodBody().escapeAll()}>
            |    }
            |
            |    <${type.staticOfMethod().escapeAll()}>
            |
            |    <${type.templateMethod()}>
            |
            |}
        """.trimMargin().keepIndentation()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Builder".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun ObjectType.fields() = this.allProperties
            .filter { it.getAnnotation("deprecated") == null }
            .filter { it.name != this.discriminator() }
            .map { it.toField() }
            .joinToString(separator = "\n\n")

    private fun Property.toField() : String {
        val vrapType = this.type.toVrapType()
        return if(this.isPatternProperty()){
            """
                |${this.deprecationAnnotation()}
                |${if(!this.required) "@Nullable" else ""}
                |private Map<String, ${vrapType.fullClassName()}> values = new HashMap<>();
            """.escapeAll().trimMargin().keepIndentation()
        } else if(this.name.equals("interface")) {
            """
                |${this.deprecationAnnotation()}
                |${if (!this.required) "@Nullable" else ""}
                |private ${vrapType.fullClassName()} _interface;
            """.trimMargin()
        }else{
            """
            |${this.deprecationAnnotation()}
            |${if(!this.required) "@Nullable" else ""}
            |private ${vrapType.fullClassName()} ${this.name};
            """.escapeAll().trimMargin().keepIndentation()
        }
    }

    private fun ObjectType.assignments() : String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType

        return this.allProperties
                .filter { it.getAnnotation("deprecated") == null }
                .filter { it.name != this.discriminator() }
                .map { assignment(it, vrapType) }.joinToString(separator = "\n\n")
    }

    private fun assignment(property: Property, type: VrapObjectType) : String {
        val propertyType = property.type;
        val propType = propertyType.toVrapType()
        return if(property.isPatternProperty()) {
            """
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder values(${if (!property.required) "@Nullable" else ""} final Map<String, ${propType.fullClassName()}> values){
                |    this.values = values;
                |    return this;
                |}
                |
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder addValue(final String key, final ${propType.fullClassName()} value) {
                |    if (this.values == null) {
                |        values = new HashMap<>();
                |    }
                |    values.put(key, value);
                |    return this;
                |}

            """.escapeAll().trimMargin().keepIndentation()
        } else if (propType is VrapArrayType) {
            var propertyName = property.name
            val propItemType = (property.type as ArrayType).items
            if(SourceVersion.isKeyword(propertyName)) {
                propertyName = "_$propertyName"
            }
            """
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder $propertyName(${if (!property.required) "@Nullable" else ""} final ${propType.itemType.fullClassName()} ...$propertyName) {
                |    this.$propertyName = new ArrayList<>(Arrays.asList($propertyName));
                |    return this;
                |}
                |
                |${if (propItemType is ObjectType && !propItemType.isAbstract() && propType.simpleName() != JavaBaseTypes.objectType.simpleName()) """
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder with${property.name.capitalize()}(Function<${propType.itemType.fullClassName()}Builder, ${propType.itemType.fullClassName()}Builder> builder) {
                |    this.$propertyName = new ArrayList<>();
                |    this.$propertyName.add(builder.apply(${propType.itemType.fullClassName()}Builder.of()).build());
                |    return this;
                |}
                |
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder plus${property.name.capitalize()}(Function<${propType.itemType.fullClassName()}Builder, ${propType.itemType.fullClassName()}Builder> builder) {
                |    if (this.$propertyName == null) {
                |        this.$propertyName = new ArrayList<>();
                |    }
                |    this.$propertyName.add(builder.apply(${propType.itemType.fullClassName()}Builder.of()).build());
                |    return this;
                |}
                |
                """ else ""}
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder $propertyName(${if (!property.required) "@Nullable" else ""} final ${propType.fullClassName()} $propertyName) {
                |    this.$propertyName = $propertyName;
                |    return this;
                |}
            """.trimMargin()
        } else {
            var propertyName = property.name
            if(SourceVersion.isKeyword(propertyName)) {
                propertyName = "_$propertyName"
            }
            """
                |${if (propertyType is ObjectType && !propertyType.isAbstract() && propType.simpleName() != JavaBaseTypes.objectType.simpleName()) """
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder $propertyName(Function<${propType.fullClassName()}Builder, ${propType.fullClassName()}Builder> builder) {
                |    this.$propertyName = builder.apply(${propType.fullClassName()}Builder.of()).build();
                |    return this;
                |}
                |
                """ else ""}
                |${property.deprecationAnnotation()}
                |public ${type.simpleClassName}Builder $propertyName(${if (!property.required) "@Nullable" else ""} final ${propType.fullClassName()} $propertyName) {
                |    this.$propertyName = $propertyName;
                |    return this;
                |}
            """.trimMargin()
        }
    }

    private fun ObjectType.getters() : String {
        return this.allProperties
                .filter { it.getAnnotation("deprecated") == null }
                .filter { it.name != this.discriminator() }
                .map { it.getter() }
                .joinToString(separator = "\n\n")
    }

    private fun Property.getter() : String {

        val vrapType = this.type.toVrapType()
        return if(this.isPatternProperty()){
            """
                |${this.deprecationAnnotation()}
                |${if(!this.required) "@Nullable" else ""}
                |public Map<String, ${vrapType.fullClassName()}> getValues(){
                |    return this.values;
                |}
            """.escapeAll().trimMargin().keepIndentation()
        } else if(this.name.equals("interface")) {
            """
                |${this.deprecationAnnotation()}
                |${if (!this.required) "@Nullable" else ""}
                |public ${vrapType.fullClassName()} getInterface(){
                |    return this._interface;
                |}
            """.escapeAll().trimMargin().keepIndentation()
        }else{
            """
                |${this.deprecationAnnotation()}
                |${if(!this.required) "@Nullable" else ""}
                |public ${vrapType.fullClassName()} get${this.name.capitalize()}(){
                |    return this.${this.name};
                |}
            """.escapeAll().trimMargin().keepIndentation()
        }
    }

    private fun ObjectType.requiredChecks() : String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        return this.allProperties
            .filter { it.getAnnotation("deprecated") == null }
            .filter { it.name != this.discriminator() }
            .filter { it.required }
            .map {
                if(it.isPatternProperty()) {
                    "Objects.requireNonNull(values, ${vrapType.simpleClassName}.class + \": values are missing\");"
                } else if(it.name.equals("interface")) {
                    "Objects.requireNonNull(_interface, ${vrapType.simpleClassName}.class + \": interface is missing\");"
                } else {
                    "Objects.requireNonNull(${it.name}, ${vrapType.simpleClassName}.class + \": ${it.name} is missing\");"
                }
            }
            .joinToString(separator = "\n")
    }

    private fun ObjectType.buildMethodBody() : String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        val constructorArguments = this.allProperties
                .filter { it.getAnnotation("deprecated") == null }
                .filter { it.name != this.discriminator() }
                .map {
                    if(it.isPatternProperty()) {
                        "values"
                    } else if(it.name.equals("interface")) {
                        "_interface"
                    } else {
                        it.name
                    }
                }
                .joinToString(separator = ", ")
        return "return new ${vrapType.simpleClassName}Impl($constructorArguments);"
    }

    private fun ObjectType.staticOfMethod() : String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType

        return """
            |public static ${vrapType.simpleClassName}Builder of() {
            |    return new ${vrapType.simpleClassName}Builder();
            |}
        """.trimMargin().keepIndentation()
    }

    private fun ObjectType.templateMethod(): String {
        val vrapType = vrapTypeProvider.doSwitch(this).toJavaVType() as VrapObjectType
        val fieldsAssignment : String = this.allProperties
                .filter { it.getAnnotation("deprecated") == null }
                .filter {it.name != this.discriminator()}
                .map {
                    if(it.isPatternProperty()){
                        "builder.values = template.values();"
                    } else if(it.name.equals("interface")) {
                        "builder._interface = template.getInterface();"
                    } else{
                        "builder.${it.name} = template.get${it.name.upperCamelCase()}();"
                    }
                }
                .joinToString(separator = "\n")

        return """
            |public static ${vrapType.simpleClassName}Builder of(final ${vrapType.simpleClassName} template) {
            |    ${vrapType.simpleClassName}Builder builder = new ${vrapType.simpleClassName}Builder();
            |    <$fieldsAssignment>
            |    return builder;
            |}
        """.escapeAll().trimMargin().keepIndentation()
    }
}
