package io.vrap.rmf.codegen.common.generator.core;

import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.List;

/**
 * Adapts RMF objects for usage in our string templates.
 */
public class RmfModelAdaptor extends ObjectModelAdaptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RmfModelAdaptor.class);

    private final static Object NULL = new Object();

    private final List<ExtensionMapper> mappers;

    public RmfModelAdaptor(final List<ExtensionMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public synchronized Object getProperty(final Interpreter interp,
                                           final ST self, final Object extendedObject,
                                           final Object property, final String propertyName) throws STNoSuchPropertyException {
        final Object result = Flowable.fromIterable(mappers)
                .flatMapMaybe(extensionMapper -> extensionMapper.apply(extendedObject, propertyName))
                .blockingFirst(NULL);

        if (result != NULL) {
            return result;
        }
        try{
            return super.getProperty(interp, self, extendedObject, property, propertyName);
        }catch (STNoSuchPropertyException exception){
            LOGGER.warn("property '{}' not found in {} or any of its extensions",propertyName, extendedObject.getClass());
            throw exception;
        }
    }
}
