package io.vrap.rmf.codegen.common.generator.extensions;

import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.resources.Method;
import io.vrap.rmf.raml.model.responses.Body;
import io.vrap.rmf.raml.model.responses.Response;
import io.vrap.rmf.raml.model.types.AnyType;
import io.vrap.rmf.raml.model.types.TypedElement;
import io.vrap.rmf.raml.model.types.impl.TypesFactoryImpl;

@ModelExtension(extend = Method.class)
public class MethodExtension {


    @ExtensionMethod
    public AnyType getReturnType(final Method method) {
        return method.getResponses().stream()
                .filter(this::isSuccessfulResponse)
                .filter(response -> (response.getBodies() != null) && (!response.getBodies().isEmpty()))
                .findFirst()
                .map(method1 -> method1.getBodies().get(0))
                .map(TypedElement::getType)
                .orElse(TypesFactoryImpl.eINSTANCE.createNilType());
    }

    @ExtensionMethod
    public Boolean hasBody(final Method method) {
        return !method.getBodies().isEmpty();
    }

    @ExtensionMethod
    public Body getFirstBody(final Method method) {
        return method.getBodies().get(0);
    }

    private boolean isSuccessfulResponse(final Response response) {
        try {
            final int statusCode = Integer.parseInt(response.getStatusCode());
            return statusCode >= 200 && statusCode < 300;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
