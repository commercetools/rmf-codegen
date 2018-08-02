package io.vrap.rmf.codegen.common.generator.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vrap.rmf.codegen.common.generator.core.STCodeGenerator;


public final class GeneratorComponent {

    private final Injector injector ;

    public GeneratorComponent(GeneratorModule generatorModule) {
        this.injector = Guice.createInjector(generatorModule);
    }

    public STCodeGenerator getStCodeGenerator(){
        return injector.getInstance(STCodeGenerator.class);
    }

}
