package com.commercetools.rmf.validators;

import io.vrap.rmf.raml.persistence.RamlResourceSet;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import java.io.IOException;
import java.net.URL;


/**
 * Resource related test fixtures.
 */
public interface ValidatorFixtures {

    default URI uriFromClasspath(final String name) {
        final URL url = ValidatorFixtures.class.getResource(name);
        return URI.createURI(url.toString());
    }

}
