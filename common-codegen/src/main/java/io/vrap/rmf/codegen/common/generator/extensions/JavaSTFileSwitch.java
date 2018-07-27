package io.vrap.rmf.codegen.common.generator.extensions;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapperFactory;
import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;


public class JavaSTFileSwitch extends TypesSwitch<STGroupFile> {

    final Logger LOGGER = LoggerFactory.getLogger(JavaSTFileSwitch.class);

    Map<String,STGroupFile> cache = new HashMap<>();

    private Injector injector;

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public STGroupFile doSwitch(EObject eObject) {
        final STGroupFile result = super.doSwitch(eObject);
        if(result ==null){
            throw new RuntimeException("no file associated with type "+ eObject);
        }
        return result;
    }

    @Override
    public STGroupFile caseStringType(StringType object) {
        return getSTFileFor("strTemplate/stringType.stg");
    }

    @Override
    public STGroupFile caseObjectType(ObjectType object) {
        return getSTFileFor("strTemplate/objectType.stg");
    }

    private STGroupFile getSTFileFor(String str){
        if(cache.get(str)==null){
            STGroupFile groupFile = new STGroupFile(str);
            addModelAdaptors(groupFile, injector);
            groupFile.load();
            cache.put(str,groupFile );
        }
        return cache.get(str);
    }

    private static void addModelAdaptors(final STGroup stGroupDir, Injector injector) {
        Flowable.fromIterable(ServiceLoader.load(ExtensionMapperFactory.class))
                .map(ExtensionMapperFactory::create)
                .doOnNext(extensionMapper -> injector.injectMembers(extensionMapper.getExtension()))
                .toList()
                .map(extensionMappers -> new RmfModelAdaptor(Object.class, extensionMappers))
                .subscribe(rmfodelAdaptor -> stGroupDir.registerModelAdaptor(rmfodelAdaptor.getHandledClass(), rmfodelAdaptor), Throwable::printStackTrace);
    }
}
