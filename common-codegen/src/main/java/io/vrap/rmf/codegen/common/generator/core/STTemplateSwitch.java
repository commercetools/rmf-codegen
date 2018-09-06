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


public class STTemplateSwitch extends ComposedSwitch<STGroupFile> {
    private static final Logger LOGGER = LoggerFactory.getLogger(STTemplateSwitch.class);

    private final Map<String, STGroupFile> cache = new HashMap<>();

    private final RmfModelAdaptor rmfModelAdaptor;
    private final String genLanguage;

    public STTemplateSwitch(final String genLanguage, final List<ExtensionMapper> extensionMappers) {
        this.genLanguage = genLanguage;
        rmfModelAdaptor = new RmfModelAdaptor(extensionMappers);
        addSwitch(new JavaSTFileTypesSwitch());
    }

    public STGroupFile getSTFileFor(final Object object) {
        STGroupFile result;
        if (object instanceof EObject) {
            result = doSwitch((EObject) object);
        } else if (object instanceof EObjectsCollection) {
            if (((EObjectsCollection) object).getSample() instanceof Resource) {
                result = getSTFileFor("resourceCollection.stg");
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

    public STGroupFile getSTFileFor(final String templateName) {
        final String templateLocation = String.format("templates/%s/%s", genLanguage, templateName);
        if (cache.get(templateLocation) == null) {
            STGroupFile groupFile = new STGroupFile(templateLocation);
            groupFile.registerModelAdaptor(Object.class, rmfModelAdaptor);
            groupFile.load();
            cache.put(templateLocation, groupFile);
        }
        return cache.get(templateLocation);
    }


    @Override
    public STGroupFile doSwitch(final EObject eObject) {
        final STGroupFile result = super.doSwitch(eObject);
        if (result == null) {
            throw new RuntimeException("no file associated with type " + eObject);
        }
        return result;
    }

    private class JavaSTFileTypesSwitch extends TypesSwitch<STGroupFile> {

        @Override
        public STGroupFile caseStringType(final StringType stringType) {
            return getSTFileFor("stringType.stg");
        }

        @Override
        public STGroupFile caseObjectType(final ObjectType objectType) {
            return getSTFileFor("objectType.stg");
        }

    }


}
