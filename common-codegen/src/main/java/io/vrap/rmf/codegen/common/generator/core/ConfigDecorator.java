package io.vrap.rmf.codegen.common.generator.core;

import io.vrap.rmf.codegen.common.generator.doc.JavaDocProcessor;
import io.vrap.rmf.codegen.common.generator.util.TypeNameSwitch;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class ConfigDecorator implements GeneratorConfig{

    private final GeneratorConfig delegate;

    private final TypeNameSwitch typeNameSwitch;

    public ConfigDecorator(final GeneratorConfig delegate) {
        Objects.requireNonNull(delegate);
        this.delegate = delegate;
        this.typeNameSwitch = TypeNameSwitch.of(getPackagePrefix(), getCustomTypeMapping());
    }

    @Override
    public String getPackagePrefix() {
        return delegate.getPackagePrefix();
    }

    @Override
    public Path getOutputFolder() {
        return delegate.getOutputFolder();
    }

    @Override
    public Path getRamlFileLocation() {
        return delegate.getRamlFileLocation();
    }

    @Override
    public JavaDocProcessor getJavaDocProcessor() {
        return delegate.getJavaDocProcessor();
    }

    @Override
    public Map<String, String> getCustomTypeMapping() {
        return delegate.getCustomTypeMapping();
    }

    public TypeNameSwitch getTypeNameSwitch() {
        return typeNameSwitch;
    }

}
