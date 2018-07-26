package io.vrap.rmf.codegen.common.generator.extensions;

import io.vrap.rmf.raml.model.types.ObjectType;
import io.vrap.rmf.raml.model.types.StringType;
import io.vrap.rmf.raml.model.types.util.TypesSwitch;
import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.Map;


public class JavaSTFileSwitch extends TypesSwitch<STGroupFile> {

    final Logger LOGGER = LoggerFactory.getLogger(JavaSTFileSwitch.class);

    Map<String,STGroupFile> cach = new HashMap<>();

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
        if(cach.get(str)==null){
            STGroupFile groupFile = new STGroupFile(str);
            groupFile.load();
            cach.put(str,groupFile );
        }
        return cach.get(str);
    }
}
