package io.vrap.codegen.languages.javalang.client.builder.model;

import com.google.inject.Inject
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.JavaEObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.JavaObjectTypeExtensions
import io.vrap.codegen.languages.java.base.extensions.enumValueName
import io.vrap.codegen.languages.java.base.extensions.toJavaVType
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType

class JavaStringTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, StringTypeRenderer {

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapEnumType

        val content = """
                |package ${vrapType.`package`};
                |
                |import com.fasterxml.jackson.annotation.JsonProperty;
                |import java.lang.String;
                |import java.util.Arrays;
                |import java.util.Optional;
                |import io.vrap.rmf.base.client.utils.Generated;
                |
                |${type.toComment().escapeAll()}
                |${JavaSubTemplates.generatedAnnotation}
                |public enum ${vrapType.simpleClassName} {
                |
                |  <${type.enumFields()}>
                |
                |  private final String jsonName;
                |
                |  private ${vrapType.simpleClassName}(final String jsonName) {
                |    this.jsonName = jsonName;
                |  }
                |
                |  public String getJsonName() {
                |     return jsonName;
                |  }
                |
                |  public static Optional\<${vrapType.simpleClassName}\> findEnumViaJsonName(String jsonName) {
                |    Optional\<${vrapType.simpleClassName}\> optional = Arrays.stream(values()).filter(t -\> t.getJsonName().equals(jsonName)).findFirst();
                |    if(!optional.isPresent()) {
                |      return Optional.of(UNKNOWN);
                |    }else {
                |      return optional;
                |    }
                |  }
                |}
                """.trimMargin().keepIndentation()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }

    fun StringType.enumFields() = enumValues()
            ?.map {
                """
                |${it.toComment()?.escapeAll()?:""}
                |@JsonProperty("${it.value}")
                |${it.value.enumValueName()}("${it.value}")
            """.trimMargin()
            }
            ?.plus("""
                |@JsonProperty("unknown")
                |UNKNOWN("unknown")
            """.trimMargin())
            ?.joinToString(separator = ",\n\n", postfix = ";")

    fun StringType.enumValues() =  enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.filter { it.value != null }
}
