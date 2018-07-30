package io.vrap.rmf.codegen.common.processor.extension;

import io.reactivex.Maybe;

public interface ExtensionMapper {

    Class getHandledType();

    Maybe apply(final Object self, final String propertyName);

    Object getExtension();

}
