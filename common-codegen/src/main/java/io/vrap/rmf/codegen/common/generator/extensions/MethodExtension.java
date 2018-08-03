package io.vrap.rmf.codegen.common.generator.extensions;

import io.vrap.rmf.codegen.common.generator.util.RmfNilTypeImpl;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.resources.Method;
import io.vrap.rmf.raml.model.responses.Body;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.TypedElement;

@ModelExtension(extend = Method.class)
public class MethodExtension {


    @ExtensionMethod
    public AnyType getReturnType(final Method method) {
        return method.getResponses().stream()
                .filter(r -> r.getStatusCode().equals("200"))
                .filter(response -> (response.getBodies() != null) && (!response.getBodies().isEmpty()))
                .findFirst()
                .map(method1 -> method1.getBodies().get(0))
                .map(TypedElement::getType)
                .orElse(new RmfNilTypeImpl());
    }

    @ExtensionMethod
    public Boolean hasBody(final Method method) {
        return !method.getBodies().isEmpty();
    }

    @ExtensionMethod
    public Body getFirstBody(final Method method) {
        return method.getBodies().get(0);
    }
}
