package io.vrap.rmf.codegen.cli.diff;

import org.eclipse.emf.common.util.URI;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * Resource related test fixtures.
 */
public interface ValidatorFixtures {

    default URI uriFromClasspath(final String name) {
        final URL url = ValidatorFixtures.class.getResource(name);
        return URI.createURI(url.toString());
    }

    default File fileFromClasspath(final String name) {
        final URL url = ValidatorFixtures.class.getResource(name);
        try {
            assert url != null;
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
