package io.vrap.rmf.codegen.common.generator.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import io.vrap.rmf.raml.model.resources.Resource;
import io.vrap.rmf.raml.model.resources.util.ResourcesSwitch;

public class TypeNameResourcesSwitch extends ResourcesSwitch<TypeName> {

    private final Converter<String, String> classNameMapper = CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL);
    private final PackageSwitch packageSwitch;

    public TypeNameResourcesSwitch(PackageSwitch packageSwitch) {
        this.packageSwitch = packageSwitch;
    }

    @Override
    public TypeName caseResource(Resource resource) {
        return ClassName.get(packageSwitch.doSwitch(resource), classNameMapper.convert(resource.getResourcePathName()) + "Requests");
    }

}
