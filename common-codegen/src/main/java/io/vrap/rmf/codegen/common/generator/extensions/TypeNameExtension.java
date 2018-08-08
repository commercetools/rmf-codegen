package io.vrap.rmf.codegen.common.generator.extensions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import org.eclipse.emf.ecore.EObject;

import javax.inject.Inject;

@ModelExtension(extend = TypeName.class)
public class TypeNameExtension {


    @ExtensionMethod
    public String getSimpleClassName(final TypeName typeName) {
        if (typeName instanceof ClassName) {
            return ((ClassName) typeName).simpleName();
        }
        return typeName.toString();
    }

    @ExtensionMethod
    public String getFullClassName(final TypeName typeName) {
        return typeName.toString();
    }
}
