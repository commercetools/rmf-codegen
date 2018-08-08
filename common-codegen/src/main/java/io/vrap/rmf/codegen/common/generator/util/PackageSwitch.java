package io.vrap.rmf.codegen.common.generator.util;

import io.vrap.rmf.raml.model.modules.Library;
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch;
import io.vrap.rmf.raml.model.types.Annotation;
import io.vrap.rmf.raml.model.types.AnnotationsFacet;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.StringInstance;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ComposedSwitch;

import java.util.Optional;

public class PackageSwitch extends ComposedSwitch<String> {

    private final String basePackage;

    public PackageSwitch(final String basePackage) {
        this.basePackage = basePackage;
        addSwitch(new TypePackageSwitch());
        addSwitch(new ResourcePackageSwitch());
    }

    @Override
    public String defaultCase(EObject eObject) {
        return basePackage;
    }

    private class TypePackageSwitch extends TypesSwitch<String> {
        @Override
        public String defaultCase(EObject object) {
            return basePackage + ".models";
        }

        @Override
        public String caseAnyType(AnyType type) {
            final String modelsPackage = basePackage + ".models";
            while (type.getType() != null) {
                if ((type.eContainer() instanceof Library) && (((Library) type.eContainer()).getAnnotation("package") != null)) {
                    break;
                }
                type = type.getType();
            }
            EObject eContainer = type.eContainer();
            while (eContainer != null) {
                if (eContainer instanceof AnnotationsFacet) {
                    final String resultPackage = Optional.of(eContainer)
                            .map(AnnotationsFacet.class::cast)
                            .map(library -> library.getAnnotation("package"))
                            .map(Annotation::getValue)
                            .map(StringInstance.class::cast)
                            .map(StringInstance::getValue)
                            .map(s -> modelsPackage + "." + s)
                            .orElse(modelsPackage);
                    return resultPackage;
                }
                eContainer = eContainer.eContainer();
            }
            return modelsPackage;
        }

    }

    private class ResourcePackageSwitch extends ResourcesSwitch<String> {
        @Override
        public String defaultCase(EObject object) {
            return basePackage + ".client";
        }
    }



}
