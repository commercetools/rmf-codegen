package io.vrap.codegen.languages.javalang.client.builder.model;

import io.vrap.codegen.languages.extensions.toComment
import io.vrap.codegen.languages.java.base.JavaSubTemplates
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.StringTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.escapeAll
import io.vrap.rmf.codegen.rendering.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.Annotation
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType

class JavaStringTypeRenderer constructor(override val vrapTypeProvider: VrapTypeProvider) : JavaObjectTypeExtensions, JavaEObjectTypeExtensions, StringTypeRenderer {

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapEnumType

        val extends = arrayListOf("JsonEnum")
            .plus(
                when (val ex = type.getAnnotation("java-extends") ) {
                    is Annotation -> {
                        (ex.value as StringInstance).value.escapeAll()
                    }
                    else -> null
                }
            )
            .filterNotNull()

        val content = """
                |package ${vrapType.`package`};
                |
                |import com.fasterxml.jackson.annotation.JsonCreator;
                |import com.fasterxml.jackson.annotation.JsonValue;
                |import java.lang.String;
                |import java.util.Arrays;
                |import java.util.Optional;
                |import io.vrap.rmf.base.client.JsonEnum;
                |import io.vrap.rmf.base.client.utils.Generated;
                |
                |/**
                |${type.toComment(" * ${vrapType.simpleClassName}").escapeAll()}
                | */
                |${JavaSubTemplates.generatedAnnotation}
                |public interface ${vrapType.simpleClassName} ${if (extends.isNotEmpty()) { "extends ${extends.joinToString(separator = ", ")}" } else ""} {
                |
                |    <${type.enumConsts()}>
                |    
                |    /**
                |     * possible values of ${vrapType.simpleClassName}
                |     */
                |    enum ${vrapType.simpleClassName}Enum implements ${vrapType.simpleClassName} {
                |        <${type.enumFields()}>
                |        private final String jsonName;
                |
                |        private ${vrapType.simpleClassName}Enum(final String jsonName) {
                |            this.jsonName = jsonName;
                |        }
                |
                |        public String getJsonName() {
                |            return jsonName;
                |        }
                |
                |        public String toString() {
                |            return jsonName;
                |        }
                |    }
                |
                |    /**
                |     * the JSON value
                |     * @return json value
                |     */
                |    @JsonValue
                |    String getJsonName();
                |
                |    /**
                |     * the enum value
                |     * @return name
                |     */
                |    String name();
                |
                |    /**
                |     * convert value to string
                |     * @return string representation
                |     */
                |    String toString();
                |
                |    /**
                |     * factory method for a enum value of ${vrapType.simpleClassName}
                |     * if no enum has been found an anonymous instance will be created
                |     * @return enum instance
                |     */
                |    @JsonCreator
                |    public static ${vrapType.simpleClassName} findEnum(String value) {
                |        return findEnumViaJsonName(value).orElse(new ${vrapType.simpleClassName}() {
                |            @Override
                |            public String getJsonName() {
                |                return value;
                |            }
                |
                |            @Override
                |            public String name() {
                |                return value.toUpperCase();
                |            }
                |            
                |            public String toString() {
                |                return value;
                |            }
                |        });
                |    }
                |
                |    /**
                |     * method to find enum using the JSON value
                |     * @return optional of enum instance
                |     */
                |    public static Optional\<${vrapType.simpleClassName}\> findEnumViaJsonName(String jsonName) {
                |        return Arrays.stream(values()).filter(t -\> t.getJsonName().equals(jsonName)).findFirst();
                |    }
                |    
                |    /**
                |     * possible enum values
                |     * @return array of possible enum values
                |     */
                |    public static ${vrapType.simpleClassName}[] values() {
                |        return ${vrapType.simpleClassName}Enum.values();
                |    }
                |    
                |    <${type.getAnnotation("java-mixin")?.value?.value?.let { (it as String).escapeAll()} ?: ""}>
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
                |/**
                | * ${it.value}
                | */
                |${it.value.enumValueName()}("${it.value}")
            """.trimMargin()
            }
            ?.joinToString(separator = ",\n\n", postfix = ";")

    fun StringType.enumConsts() = enumValues()
            ?.map {
                """
                |${it.toComment()?.escapeAll()?:""}
                |${this.toVrapType().simpleName()} ${it.value.enumValueName()} = ${this.toVrapType().simpleName()}Enum.${it.value.enumValueName()}
            """.trimMargin()
            }
            ?.joinToString(separator = ";\n", postfix = ";")

    fun StringType.enumValues() =  enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.filter { it.value != null }
}
