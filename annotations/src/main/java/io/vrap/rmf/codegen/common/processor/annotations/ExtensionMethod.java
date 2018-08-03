package io.vrap.rmf.codegen.common.processor.annotations;

import java.lang.annotation.*;

/**
 * If a class is marked as {@link ModelExtension} an extension method has to be marked via this method
 * it should accept either no argument or an argument of the type extended type.
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtensionMethod {

}
