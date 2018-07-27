package io.vrap.rmf.codegen.common.generator.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.reactivex.Flowable;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;
import io.vrap.rmf.codegen.common.generator.core.GeneratorConfig;
import io.vrap.rmf.codegen.common.generator.extensions.RmfModelAdaptor;
import io.vrap.rmf.codegen.common.generator.extensions.STCodeGenerator;
import io.vrap.rmf.codegen.common.processor.extension.ExtensionMapperFactory;
import io.vrap.rmf.raml.model.types.AnyType;

import java.util.List;
import java.util.ServiceLoader;


public final class GeneratorComponent {

    private final Injector injector ;

    public GeneratorComponent(GeneratorModule generatorModule) {
        this.injector = Guice.createInjector(generatorModule);
    }

    public void injectObject(Object instance){
        this.injector.injectMembers(instance);
    }

    public MasterCodeGenerator getMasterCodeGenerator(){
        return injector.getInstance(MasterCodeGenerator.class);
    }

    public List<AnyType> getRamleTypes(){
        return injector.getInstance(Key.get(new TypeLiteral<List<AnyType>>(){}));
    }

    public Injector getInjector() {
        return injector;
    }

    public STCodeGenerator getStCodeGenerator(){
        return injector.getInstance(STCodeGenerator.class);
    }
}
