package io.vrap.rmf.codegen.common.generator.extensions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import org.eclipse.emf.ecore.EObject;

import javax.inject.Inject;

@ModelExtension(extend = ClassName.class)
public class ClassNameExtension extends TypeNameExtension{


    @ExtensionMethod
    public String  getPackageName(final ClassName className) {
        return className.packageName();
    }
    
}
