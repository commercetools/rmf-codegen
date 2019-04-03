package io.vrap.codegen.languages.java.commands

import io.vrap.codegen.languages.java.extensions.EObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.ObjectTypeExtensions
import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.MethodRenderer
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.resources.HttpMethod
import io.vrap.rmf.raml.model.resources.Method
import org.eclipse.emf.ecore.EObject
import javax.inject.Inject

class JavaCommandsRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : MethodRenderer, ObjectTypeExtensions, EObjectTypeExtensions {

    override fun render(type: Method): TemplateFile {
        val vrapType = vrapTypeProvider.doSwitch(type as EObject) as VrapObjectType

        val test = type.resource().fullUri.template

        val content = """
                |package ${vrapType.`package`};
                |
                |import com.commercetools.importer.commands.SphereRequest;
                |import io.sphere.sdk.http.HttpResponse;
                |import io.sphere.sdk.client.HttpRequestIntent;
                |
                |final class ${type.toRequestName()} implements SphereRequest {
                |
                |   @Override
                |   public Object deserialize(HttpResponse httpResponse) {
                |      return null;
                |   }
                |
                |   @Override
                |   public HttpRequestIntent httpRequestIntent() {
                |      return null;
                |   }
                |
                |   @Override
                |   public boolean canDeserialize(HttpResponse httpResponse) {
                |      return false;
                |   }
                |}
                |
                """.trimMargin().keepIndentation()

        return TemplateFile(
            relativePath = "${vrapType.`package`}.${type.toRequestName()}".replace(".", "/") + ".java",
            content = content
        )
    }

    fun Method.httpRequestIntentBody() : String? {
        val path = "?";
        if(this.method == HttpMethod.GET){
            return """
                HttpRequestIntent httpRequestIntent = HttpRequestIntent.of(HttpMethod.GET, path);
            """.trimMargin()
        }else{
            return null
        }
    }

}
