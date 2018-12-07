package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.VrapConstants
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import io.vrap.rmf.raml.model.util.StringCaseFormat
import org.eclipse.emf.ecore.EObject
import java.util.*

class PhpObjectTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |<<${type.imports()}>>
            |
            |final class ${vrapType.simpleClassName}Model implements ${vrapType.simpleClassName}
            |{
            |    ${if (type.discriminator != null || type.discriminatorValue != null) {"const DISCRIMINATOR_VALUE = '${type.discriminatorValue ?: ""}';"} else ""}
            |    <<${type.constructor()}>>
            |
            |    <<${type.toBeanFields()}>>
            |
            |    <<${type.getters()}>>
            |}
        """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape()


        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Model.php",
                content = content
        )
    }


    fun ObjectType.constructor(): String {
        return if (this.discriminator != null)
            """
            |/**
            | * @param array $!data
            | */
            |public function __construct(array $!data = []) {
            |    parent::__construct($!data);
            |    $!this->set${this.discriminator.capitalize()}(static::DISCRIMINATOR_VALUE);
            |}
            """.trimMargin()
        else
            ""
    }

    fun ObjectType.imports() = this.getImports(this.allProperties).map { "use ${it.escapeAll()};" }.joinToString(separator = "\n")

    fun Property.toPhpField(): String {

        return """
            |/**
            | * @var ${this.type.toVrapType().simpleName()}
            | */
            |protected $${if (this.isPatternProperty()) "values" else this.name};
        """.trimMargin();
    }

    fun Property.toPhpConstant(): String {

        return """
            |const FIELD_${if (this.isPatternProperty()) "VALUES" else StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this.name)} = '${if (this.isPatternProperty()) "values" else this.name}';
        """.trimMargin();
    }

    fun ObjectType.toBeanConstant() = this.properties
            .filter { it.name != this.discriminator }
            .map { it.toPhpConstant() }.joinToString(separator = "\n")

    fun ObjectType.toBeanFields() = this.allProperties
            .filter { it.name != this.discriminator }
            .map { it.toPhpField() }.joinToString(separator = "\n\n")

    fun ObjectType.setters() = this.properties
            //Filter the discriminators because they don't make much sense the generated bean
            .filter { it.name != this.discriminator }
            .map { it.setter() }
            .joinToString(separator = "\n\n")


    fun ObjectType.getters() = this.allProperties
            //Filter the discriminators because they don't make much sense the generated bean
            .filter { it.name != this.discriminator }
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

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
                |/**
                | ${this.type.toPhpComment()}
                | */
                |public function values() {
                |    return $!values;
                |}
            """.trimMargin()
        } else {
            """
                |/**
                | ${this.type.toPhpComment()}
                | * @return ${this.type.toVrapType().simpleName()}
                | */
                |public function get${this.name.capitalize()}(){
                |   return $!this->${this.name};
                |}
        """.trimMargin()
        }
    }

    fun Property.validationAnnotations(): String {
        val validationAnnotations = ArrayList<String>()
        if (this.required != null && this.required!!) {
            validationAnnotations.add("@NotNull")
        }
        if (CascadeValidationCheck.doSwitch(this.type)) {
            validationAnnotations.add("@Valid")
        }
        return validationAnnotations.joinToString(separator = "\n")
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
