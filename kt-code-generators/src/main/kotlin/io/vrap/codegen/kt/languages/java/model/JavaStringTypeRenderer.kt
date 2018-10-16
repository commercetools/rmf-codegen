package io.vrap.codegen.kt.languages.java.model

import com.google.inject.Inject
import io.vrap.codegen.kt.languages.java.JavaSubTemplates
import io.vrap.codegen.kt.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.kt.languages.java.extensions.ObjectTypeExtensions
import io.vrap.codegen.kt.languages.java.extensions.toJavaComment
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.kt.rendring.utils.escapeAll
import io.vrap.rmf.codegen.kt.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType
import io.vrap.rmf.raml.model.util.StringCaseFormat

class JavaStringTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, StringTypeRenderer {

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
                |package ${vrapType.`package`};
                |
                |import com.fasterxml.jackson.annotation.JsonProperty;
                |import java.lang.String;
                |import java.util.Arrays;
                |import java.util.Optional;
                |import javax.annotation.Generated;
                |
                |${type.toJavaComment().escapeAll()}
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
                |    return Arrays.stream(values()).filter(t -\> t.getJsonName().equals(jsonName)).findFirst();
                |  }
                |}
                """.trimMargin().keepIndentation()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }

    fun StringType.enumFields(): String = this.enumJsonNames()
            .map {
                """
                |@JsonProperty("$it")
                |${it.enumValueName()}("$it")
            """.trimMargin()
            }
            .joinToString(separator = ",\n\n", postfix = ";")


    fun StringType.enumJsonNames() = this.enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.map { it.value }
            ?.filterNotNull() ?: listOf()

    fun String.enumValueName(): String {
        return StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(this)
    }

}