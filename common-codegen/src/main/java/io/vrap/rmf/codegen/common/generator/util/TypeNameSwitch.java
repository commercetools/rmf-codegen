package io.vrap.rmf.codegen.common.generator.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.raml.model.elements.NamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ComposedSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeNameSwitch extends ComposedSwitch<TypeName> {

    private final PackageSwitch packageSwitch;
    private final Map<String, String> customTypeMapping;

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeNameSwitch.class);

    private TypeNameSwitch(final String basePackageName, final Map<String, String> customTypeMapping) {
        this.packageSwitch = new PackageSwitch(basePackageName);
        this.customTypeMapping = customTypeMapping;
        addSwitch(new TypeNameTypeSwitch(packageSwitch));
        addSwitch(new TypeNameResourcesSwitch(packageSwitch));
    }

    @Override
    public TypeName doSwitch(EObject eObject) {
        if ((eObject instanceof NamedElement) && (getCustomTypeMapping().get(((NamedElement) eObject).getName()) != null)) {
            return ClassName.bestGuess(getCustomTypeMapping().get(((NamedElement) eObject).getName()));
        }

        final TypeName result = super.doSwitch(eObject);

        if (result == null) {
            LOGGER.warn("No typeName was associated with {}", eObject);
            return TypeName.get(Object.class);
        }

        return result;
    }

    public static TypeNameSwitch of(final String basePackageName, @Nullable final Map<String, String> customTypeMapping) {
        return new TypeNameSwitch(basePackageName, Optional.ofNullable(customTypeMapping).orElseGet(HashMap::new));
    }

    public static TypeNameSwitch of(final GeneratorConfig generatorConfig) {
        return new TypeNameSwitch(generatorConfig.getPackagePrefix(), generatorConfig.getCustomTypeMapping());
    }

    public Map<String, String> getCustomTypeMapping() {
        return customTypeMapping;
    }


}