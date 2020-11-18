package io.vrap.codegen.languages.csharp.model

import com.google.inject.Inject
import io.vrap.codegen.languages.csharp.extensions.*
import io.vrap.codegen.languages.extensions.EObjectExtensions
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.StringTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.StringInstance
import io.vrap.rmf.raml.model.types.StringType

class CsharpStringTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : CsharpObjectTypeExtensions, EObjectExtensions, StringTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var basePackagePrefix:String

    override fun render(type: StringType): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type) as VrapEnumType

        val content = """
                |using System.ComponentModel;
                |using System.Linq;
                |using commercetools.Base.CustomAttributes;
                |using commercetools.Base.Models;
                |namespace ${vrapType.csharpPackage()}
                |{
                |   public enum ${vrapType.simpleClassName}
                |   {
                |       <${type.enumFields()}>
                |   }
                |   public class ${vrapType.simpleClassName}Wrapper : I${vrapType.simpleClassName}
                |   {
                |       public string JsonName { get; internal set; }
                |       public ${vrapType.simpleClassName}? Value { get; internal set; }
                |   }
                |   [EnumInterfaceCreator(typeof(I${vrapType.simpleClassName}), "FindEnum")]
                |   public interface I${vrapType.simpleClassName} : IJsonName
                |   {
                |       <${type.enumStaticFields("${vrapType.simpleClassName}")}>
                |       
                |        ${vrapType.simpleClassName}? Value { get; }
                |        
                |        static I${vrapType.simpleClassName}[] Values()
                |        {
                |           return new[]
                |           {
                |               <${type.enumFieldsAsNames()}>
                |           };
                |        }
                |        static I${vrapType.simpleClassName} FindEnum(string value)
                |        {
                |           return Values().FirstOrDefault(origin =\> origin.JsonName == value) ?? new ${vrapType.simpleClassName}Wrapper() {JsonName = value};
                |        }
                |   }
                |}
                """.trimMargin().keepIndentation()

        val relativePath = vrapType.csharpClassRelativePath(false).replace(basePackagePrefix.replace(".", "/"), "").trimStart('/')

        return TemplateFile(
                relativePath = relativePath,
                content = content
        )
    }

    fun StringType.enumFields() = enumValues()
            ?.map {
                """
                 |[Description("${it.value}")]
                 |${it.value.enumValueName()}
            """.trimMargin()
            }
            ?.joinToString(separator = ",\n\n", postfix = "")

    fun StringType.enumStaticFields(enumName: String) = enumValues()
            ?.map {
                """
                 | public static I${enumName} ${it.value.enumValueName()} = new ${enumName}Wrapper
                 |  {Value = ${enumName}.${it.value.enumValueName()}, JsonName = "${it.value}"}; 
            """.trimMargin()
            }
            ?.joinToString(separator = "\n\n", postfix = "")

    fun StringType.enumFieldsAsNames() = enumValues()
            ?.map {
                """
                 | ${it.value.enumValueName()} 
            """.trimMargin()
            }
            ?.joinToString(separator = ",\n", postfix = "")


    fun StringType.enumValues() =  enum?.filter { it is StringInstance }
            ?.map { it as StringInstance }
            ?.filter { it.value != null }
}
