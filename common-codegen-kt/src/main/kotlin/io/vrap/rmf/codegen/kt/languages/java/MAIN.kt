package io.vrap.rmf.codegen.kt.languages.java

import com.google.inject.Inject
import io.vrap.rmf.codegen.kt.CodeGeneratorConfig
import io.vrap.rmf.codegen.kt.di.GeneratorComponent
import io.vrap.rmf.codegen.kt.di.GeneratorModule
import io.vrap.rmf.codegen.kt.doc.toHtml
import io.vrap.rmf.codegen.kt.io.DataSink
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.languages.java.extensions.getImports
import io.vrap.rmf.codegen.kt.languages.java.extensions.toIndentedComment
import io.vrap.rmf.codegen.kt.rendring.fixIndentation
import io.vrap.rmf.codegen.kt.types.TypeNameSwitch
import io.vrap.rmf.codegen.kt.types.VrapArrayType
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapType
import io.vrap.rmf.raml.model.types.DescriptionFacet
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import org.eclipse.emf.common.util.URI
import java.lang.IllegalStateException

fun main(args: Array<String>) {

    val generatorConfig = CodeGeneratorConfig(docTransformer = DescriptionFacet::toHtml,
            ramlFileLocation = URI.createFileURI("/Users/abeniasaad/IdeaProjects/rmf-codegen/common-codegen/src/test/resources/api-spec/api.raml")
    )

    val generatorModule = GeneratorModule(generatorConfig, JavaBaseTypes)
    val generatorComponent = GeneratorComponent(generatorModule)

    val javaCodeGenerator = generatorComponent.injector.getInstance(JavaCodeGenerator::class.java)
    val objectTypeRenderer = generatorComponent.injector.getInstance(ObjectTypeRenderer::class.java)

    javaCodeGenerator.allTypes
            .filter { it is ObjectType }
            .map { it as ObjectType }
            .map(objectTypeRenderer::render)
            .forEach(DataSink::save)

}


class ObjectTypeRenderer @Inject constructor(val typeNameSwitch: TypeNameSwitch) {


    fun Property.toJavaField() = "private ${typeNameSwitch.doSwitch(this.type).simpleName()} ${this.name};"

    fun VrapType.simpleName():String{
        return when(this){
            is VrapObjectType -> this.simpleClassName
            is VrapArrayType -> "List<${this.itemType.simpleName()}>"
            else -> throw IllegalStateException("$this has no simple class name.")
        }
    }



    fun render(objectType: ObjectType): TemplateFile {

        val vrapType = typeNameSwitch.doSwitch(objectType) as VrapObjectType

        val content = """
                |package ${vrapType.`package`};
                |
                |<${objectType.getImports(typeNameSwitch).map { "import $it;" }.joinToString(separator = "\n")}>
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
                |${objectType.toIndentedComment()}
                |public class ${vrapType.simpleClassName} {
                |
                |    <${objectType.properties.map { it.toJavaField()}.joinToString(separator = "\n")}>
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


}
