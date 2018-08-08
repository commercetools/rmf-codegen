package io.vrap.rmf.codegen.common.generator.extensions;

import com.google.inject.Inject;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.types.*;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ModelExtension(extend = Property.class)
public class PropertyExtension {
    private CascadeValidationSwitch cascadeValidationSwitch = new CascadeValidationSwitch();
    private DescriptionFacetExtension descriptionFacetExtension;

    @Inject
    public void setDescriptionFacetExtension(final DescriptionFacetExtension descriptionFacetExtension) {
        this.descriptionFacetExtension = descriptionFacetExtension;
    }

    @ExtensionMethod
    public Boolean  isPatternProperty(Property property){
        return property.getName().startsWith("/");
    }

    @ExtensionMethod
    public Boolean hasDescription(final Property property) {
        return property.getType().isInlineType() && descriptionFacetExtension.hasDescription(property.getType());
    }

    @ExtensionMethod
    public String getDescription(final Property property) {
        return descriptionFacetExtension.getDescription(property.getType());
    }

    @ExtensionMethod
    public List<String> getValidationAnnotations(final Property property) {
        final List<String> validationAnnotations = new ArrayList<>();
        if (property.getRequired() != null && property.getRequired()) {
            validationAnnotations.add("NotNull");
        }
        if (cascadeValidationSwitch.doSwitch(property.getType())) {
            validationAnnotations.add("Valid");
        }
        return validationAnnotations;
    }

    private static class CascadeValidationSwitch extends TypesSwitch<Boolean> {
        @Override
        public Boolean defaultCase(final EObject object) {
            return false;
        }

        @Override
        public Boolean caseObjectType(final ObjectType objectType) {
            return true;
        }

        @Override
        public Boolean caseArrayType(final ArrayType arrayType) {
            if (arrayType.getItems() != null) {
                return doSwitch(arrayType.getItems());
            } else {
                return false;
            }
        }
    }
}
