package io.vrap.rmf.codegen.common.generator.injection;

import dagger.Component;
import io.vrap.rmf.codegen.common.generator.MasterCodeGenerator;

import javax.inject.Singleton;

@Singleton
@Component(modules = GeneratorModule.class)
public interface GeneratorComponent {

    MasterCodeGenerator getMasterCodeGenerator();

}
