package io.vrap.rmf.codegen.common;

import io.vrap.rmf.raml.model.resources.Method;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.values.StringTemplate;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class GeneratorConfig {
    private final Path genFolder;
    private final String packagePrefix;
    private final StringTemplate resourcePathNameMapping;
    private final MethodNameMapping methodNameMapping = MethodNameMapping.of();

    public GeneratorConfig(final Path genFolder, final String packagePrefix, final StringTemplate resourcePathNameMapping) {
        this.genFolder = genFolder;
        this.packagePrefix = packagePrefix;
        this.resourcePathNameMapping = resourcePathNameMapping;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public Path getGenFolder() {
        return genFolder;
    }

    public String getMappedMethodName(final Method method) {
        return methodNameMapping.getMappedName(method);
    }

    public String getMappedResourceName(final Resource resource) {
        final String resourcePathName = resource.getResourcePathName();
        return getMappedResourceName(resourcePathName);
    }

    public String getMappedResourceName(final String resourcePathName) {
        final Map<String, String> parameters = Collections.singletonMap("resourcePathName", resourcePathName);
        return resourcePathNameMapping.render(parameters);
    }
}
