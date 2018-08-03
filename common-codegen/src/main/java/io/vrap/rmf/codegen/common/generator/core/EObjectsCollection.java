package io.vrap.rmf.codegen.common.generator.core;

import com.squareup.javapoet.ClassName;
import org.eclipse.emf.ecore.EObject;

import java.util.List;
import java.util.Objects;

public class EObjectsCollection {

    private final List<? extends EObject> eObjects;

    private final ClassName className;


    public EObjectsCollection(List<? extends EObject> eObjects, ClassName className) {

        Objects.requireNonNull(eObjects);
        Objects.requireNonNull(className);
        if(eObjects.isEmpty()){
            throw new IllegalArgumentException("The resource collection is supposed to be non empty");
        }

        this.eObjects = eObjects;
        this.className = className;
    }

    public List<? extends EObject> getEObjects() {
        return eObjects;
    }

    public ClassName getCollectionClassName() {
        return className;
    }

    public EObject getSample(){
        return eObjects.get(0);
    }
}
