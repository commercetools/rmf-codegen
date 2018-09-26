package io.vrap.codegen.kt.languagages.languages.java.groovy.dsl;

import com.google.inject.Inject
import io.vrap.codegen.kt.languagages.languages.java.extensions.AnyTypeExtensions
import io.vrap.codegen.kt.languagages.languages.java.extensions.ObjectTypeExtensions
import io.vrap.rmf.codegen.kt.core.ObjectTypeRenderer
import io.vrap.rmf.codegen.kt.io.TemplateFile
import io.vrap.rmf.codegen.kt.types.VrapObjectType
import io.vrap.rmf.codegen.kt.types.VrapTypeSwitch
import io.vrap.rmf.raml.model.types.ObjectType

class GroovyDslGenerator @Inject constructor(override val vrapTypeSwitch: VrapTypeSwitch) : ObjectTypeExtensions, AnyTypeExtensions, ObjectTypeRenderer {

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeSwitch.doSwitch(type) as VrapObjectType

        val content = """
            |package ${vrapType.`package`};
            |
            |import ${vrapType.`package`}.${vrapType.simpleClassName};
            |import groovy.lang.Closure;
            |import groovy.lang.DelegatesTo;
            |
            |/**
            | * Provides a Groovy DSL to build instances of this type.
            | */
            |public interface ${vrapType.simpleClassName}Dsl {
            |  /**
            |   * Create a new instance of this type.
            |   *
            |   * @param closure the closure to initialize the fields of the new instance
            |   * @return new instance intialized via the given closure
            |   */
            |  default ${vrapType.simpleClassName} ${type.name.decapitalize()}(@DelegatesTo(${vrapType.simpleClassName}.class) final Closure<${vrapType.simpleClassName}> closure) {
            |    final ${vrapType.simpleClassName} ${type.name.decapitalize()} = new ${vrapType.simpleClassName}();
            |    closure.setDelegate(${type.name.decapitalize()});
            |    closure.call();
            |    return ${type.name.decapitalize()};
            |  }
            |}
        """.trimMargin()


        return TemplateFile(
                relativePath = "${vrapType.`package`}.${vrapType.simpleClassName}".replace(".", "/") + ".java",
                content = content
        )
    }
}
