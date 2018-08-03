package io.vrap.rmf.codegen.common.generator.core;

import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapper;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ComposedSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JavaSTFileSwitch extends ComposedSwitch<STGroupFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaSTFileSwitch.class);

    private Map<String, STGroupFile> cache = new HashMap<>();

    RmfModelAdaptor rmfModelAdaptor;

    public JavaSTFileSwitch(List<ExtensionMapper> extensionMappers) {
        rmfModelAdaptor = new RmfModelAdaptor(Object.class, extensionMappers);
        addSwitch(new JavaSTFileTypesSwitch());
    }

    public STGroupFile getSTFileFor(Object object) {
        STGroupFile result;
        if (object instanceof EObject) {
            result = doSwitch((EObject) object);
        } else if (object instanceof EObjectsCollection) {
            if (((EObjectsCollection) object).getSample() instanceof Resource) {
                result = getSTFileFor("strTemplate/resourceCollection.stg");
            } else {
                throw new IllegalArgumentException("unhandled input " + object + " of type " + object.getClass().getCanonicalName());
            }

        } else {
            throw new IllegalArgumentException("unhandled input " + object + " of type " + object.getClass().getCanonicalName());
        }
        if (result == null) {
            throw new RuntimeException("no file associated with type " + object);
        }
        return result;
    }

    public STGroupFile getSTFileFor(String str) {
        if (cache.get(str) == null) {
            STGroupFile groupFile = new STGroupFile(str);
            groupFile.registerModelAdaptor(rmfModelAdaptor.getHandledClass(), rmfModelAdaptor);
            groupFile.load();
            cache.put(str, groupFile);
        }
        return cache.get(str);
    }


    @Override
    public STGroupFile doSwitch(EObject eObject) {
        final STGroupFile result = super.doSwitch(eObject);
        if (result == null) {
            throw new RuntimeException("no file associated with type " + eObject);
        }
        return result;
    }

    private class JavaSTFileTypesSwitch extends TypesSwitch<STGroupFile> {

        @Override
        public STGroupFile caseStringType(StringType object) {
            return getSTFileFor("strTemplate/stringType.stg");
        }

        @Override
        public STGroupFile caseObjectType(ObjectType object) {
            return getSTFileFor("strTemplate/objectType.stg");
        }

    }


}
