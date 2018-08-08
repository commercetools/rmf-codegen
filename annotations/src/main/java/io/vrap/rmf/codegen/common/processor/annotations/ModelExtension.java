package io.vrap.rmf.codegen.common.processor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to extend a type for the purpose of string template code generation,
 * if you need an additional method just add a class annotated with @{@link ModelExtension} and write the extra extension method
 * these methods can be accessed later via string template modelAdaptors
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelExtension {

    Class extend();

}
