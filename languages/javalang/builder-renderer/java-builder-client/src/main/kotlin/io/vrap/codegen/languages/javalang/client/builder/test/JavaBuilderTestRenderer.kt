package io.vrap.codegen.languages.javalang.client.builder.test

import io.vrap.codegen.languages.extensions.deprecated
import io.vrap.codegen.languages.extensions.isPatternProperty
import io.vrap.codegen.languages.java.base.extensions.*
import io.vrap.rmf.codegen.di.AllObjectTypes
import io.vrap.rmf.codegen.firstLowerCase

import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendering.FileProducer
import io.vrap.rmf.codegen.rendering.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendering.utils.*
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.*
import javax.lang.model.SourceVersion
import kotlin.random.Random

class JavaBuilderTestRenderer constructor(override val vrapTypeProvider: VrapTypeProvider, @AllObjectTypes private val allObjectTypes: List<ObjectType>): FileProducer, JavaEObjectTypeExtensions, JavaObjectTypeExtensions {

    override fun produceFiles(): List<TemplateFile> {
        return allObjectTypes.filter { !it.isAbstract() && !it.deprecated() }.map { render(it) }
    }

    fun render(type: ObjectType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType

        val properties = type.properties.filterNot { it.deprecated() || it.isPatternProperty() || it.name == type.discriminator() }
        val content = """
            |package ${vrapType.`package`};
            |
            |import com.tngtech.junit.dataprovider.DataProvider;
            |import com.tngtech.junit.dataprovider.DataProviderExtension;
            |import com.tngtech.junit.dataprovider.UseDataProvider;
            |import com.tngtech.junit.dataprovider.UseDataProviderExtension;
            |import org.assertj.core.api.Assertions;
            |import org.junit.jupiter.api.Test;
            |import org.junit.jupiter.api.TestTemplate;
            |import org.junit.jupiter.api.extension.ExtendWith;
            |
            |import java.time.LocalDate;
            |import java.time.LocalTime;
            |import java.time.ZonedDateTime;
            |import java.util.Collections;
            |
            |@ExtendWith(UseDataProviderExtension.class)
            |@ExtendWith(DataProviderExtension.class)
            |public class ${vrapType.simpleClassName}Test {
            |    ${if (properties.isNotEmpty()) """
            |    @TestTemplate
            |    @UseDataProvider("objectBuilder")
            |    public void buildUnchecked(${vrapType.simpleClassName}Builder builder) {
            |        ${vrapType.simpleClassName} ${vrapType.simpleClassName.firstLowerCase()} = builder.buildUnchecked();
            |        Assertions.assertThat(${vrapType.simpleClassName.firstLowerCase()}).isInstanceOf(${vrapType.simpleClassName}.class);
            |    }
            |
            |    @DataProvider
            |    public static Object[][] objectBuilder() {
            |        return new Object[][] {
            |            <<${properties.joinToString(",\n") { "new Object[] { ${builder(type, it)} }" }}>>
            |        };
            |    }
            |    
            |    <<${properties.joinToString("\n") { getterSetterTest(type, it) }}>>""" else ""}
            |}
        """.trimMargin().keepAngleIndent()

        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}Test".replace(".", "/") + ".java",
                content = content
        )
    }

    private fun builder(type: ObjectType, property: Property): String {
        var propertyName = property.name
        val checkedPropertyType = property.type
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType
        if(SourceVersion.isKeyword(propertyName)) {
            propertyName = "_$propertyName"
        }

        return "${vrapType.simpleClassName}.builder().${propertyName}(${propertyValue(propertyName, checkedPropertyType, Random(propertyName.hashCode()))})"
    }

    private fun getterSetterTest(type: ObjectType, property: Property): String {
        val vrapType = vrapTypeProvider.doSwitch(type).toJavaVType() as VrapObjectType
        var methodName = property.name.lowerCamelCase()
        if(SourceVersion.isKeyword(methodName)) {
            methodName = "_$methodName"
        }
        val propertyValue = propertyValue(property.name, property.type, Random(property.name.hashCode()))
        return """
            |@Test
            |public void ${methodName}() {
            |    ${vrapType.simpleClassName} value = ${vrapType.simpleClassName}.of();
            |    value.set${property.name.upperCamelCase()}($propertyValue);
            |    Assertions.assertThat(value.get${property.name.upperCamelCase()}()).isEqualTo($propertyValue);
            |}
        """.trimMargin()
    }

    private fun propertyValue(name: String, type: AnyType, r: Random) : Any {
        val vrapType = type.toVrapType();

        val checkedType = if (type.isInlineType) type.type else type
        return when (type) {
            is ArrayType -> "Collections.singletonList(${propertyValue(name, type.items, r)})"
            is BooleanType -> true
            is IntegerType -> when (type.format) {
                    NumberFormat.LONG,
                    NumberFormat.INT64 -> "${r.nextInt(1, 10)}L"
                    else -> r.nextInt(1, 10)
                }
            is NumberType -> when (type.format) {
                    NumberFormat.DOUBLE -> r.nextDouble()
                    NumberFormat.FLOAT -> r.nextFloat()
                    NumberFormat.LONG,
                    NumberFormat.INT64 -> "${r.nextInt(1, 10)}L"
                    else -> r.nextInt(1, 10)
                }
            is StringType -> when (vrapType) {
                is VrapEnumType -> "${vrapType.fullClassName()}.findEnum(\"${checkedType.enum.firstOrNull()?.value ?: name}\")"
                else -> "\"${name}\""
            }
            is ObjectType -> when(vrapType.fullClassName()) {
                "java.lang.Object" -> "\"${name}\""
                else -> "new ${vrapType.fullClassName()}Impl()"
            }
            is DateOnlyType -> "LocalDate.parse(\"2023-06-01\")"
            is TimeOnlyType -> "LocalTime.parse(\"12:00\")"
            is DateTimeType -> "ZonedDateTime.parse(\"2023-06-01T12:00Z\")"
            else -> "\"${name}\""
        }
    }
}
