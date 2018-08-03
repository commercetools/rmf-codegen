package io.vrap.rmf.codegen.common.generator.extensions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.vrap.functional.utils.TypeSwitch;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.AnyType;
import org.eclipse.emf.ecore.EObject;

import javax.inject.Inject;
import java.lang.reflect.Type;

@ModelExtension(extend = EObject.class)
public class EObjectExtension {

    private TypeNameSwitch typeNameSwitch;

    @Inject
    public void setGeneratorConfig(final GeneratorConfig generatorConfig) {
        this.typeNameSwitch = TypeNameSwitch.of(generatorConfig);
    }

    @ExtensionMethod
    public TypeName getTypeName(final EObject anyType) {
        return typeNameSwitch.doSwitch(anyType);
    }


}
