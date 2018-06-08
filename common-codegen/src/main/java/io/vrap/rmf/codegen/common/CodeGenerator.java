package io.vrap.rmf.codegen.common;

import org.eclipse.emf.ecore.EObject;

import java.io.IOException;

public interface CodeGenerator<T extends EObject> {
    GenerationResult generate(T from) throws IOException;
}
