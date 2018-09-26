package io.vrap.rmf.codegen.kt.languages.java;

import com.google.inject.Inject
import io.vrap.rmf.codegen.kt.core.ObjectTypeRenderer
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.languages.java.extensions.AnyTypeExtensions
import io.vrap.rmf.codegen.kt.languages.java.extensions.ObjectTypeExtensions
import io.vrap.rmf.codegen.kt.languages.java.extensions.simpleName
import io.vrap.rmf.codegen.kt.languages.java.extensions.toJavaComment
import io.vrap.rmf.codegen.kt.rendring.escapeAll
import io.vrap.rmf.codegen.kt.rendring.fixIndentation
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapTypeSwitch
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

class JavaObjectTypeRenderer @Inject constructor(override val vrapTypeSwitch:VrapTypeSwitch) : ObjectTypeExtensions, AnyTypeExtensions,ObjectTypeRenderer{

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeSwitch.doSwitch(type) as VrapObjectType

        val content = """
                |package ${vrapType.`package`};
                |
                |${type.imports()}
                |import com.fasterxml.jackson.annotation.*;
                |import javax.annotation.Generated;
                |import javax.validation.Valid;
                |import javax.validation.constraints.NotNull;
                |import java.util.*;
                |import org.apache.commons.lang3.builder.EqualsBuilder;
                |import org.apache.commons.lang3.builder.HashCodeBuilder;
                |import org.apache.commons.lang3.builder.ToStringBuilder;
                |import org.apache.commons.lang3.builder.ToStringStyle;
                |
                |${type.toJavaComment().escapeAll()}
                |public class ${vrapType.simpleClassName} {
                |
                |    <${type.toBeanFields().escapeAll()}>
                |
                |    <${type.setters().escapeAll()}>
                |
                |    <${type.getters().escapeAll()}>
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
        """.trimMargin()
                .fixIndentation()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }



    fun ObjectType.imports() = this.getImports().map { "import $it;" }.joinToString(separator = "\n")

    fun Property.toJavaField() = "private ${this.type.toVrapType().simpleName()} ${this.name};"

    fun ObjectType.toBeanFields() = this.properties.map { it.toJavaField() }.joinToString(separator = "\n\n")

    fun ObjectType.setters() = this.properties.map { """
        |public void set${it.name.capitalize()}(final ${it.type.toVrapType().simpleName()} ${it.name}){
        |   this.${it.name} = ${it.name};
        |}
    """.trimMargin() }.joinToString(separator = "\n\n")


    fun ObjectType.getters() = this.properties.map { """
        |${it.type.toJavaComment()}
        |public ${it.type.toVrapType().simpleName()} get${it.name.capitalize()}(){
        |   return this.${it.name};
        |}
    """.trimMargin() }.joinToString(separator = "\n\n")
}
