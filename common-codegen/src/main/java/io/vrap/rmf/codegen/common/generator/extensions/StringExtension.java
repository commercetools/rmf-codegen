package io.vrap.rmf.codegen.common.generator.extensions;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import io.vrap.rmf.codegen.common.processor.annotations.ExtensionMethod;
import io.vrap.rmf.codegen.common.processor.annotations.ModelExtension;
import io.vrap.rmf.raml.model.util.StringCaseFormat;
import org.apache.commons.lang3.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@ModelExtension(extend = String.class)
public class StringExtension {

    private static final Converter<String, String> classNameMapper = CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL);

    @ExtensionMethod
    public String getJavaIdentifier(final String input) {
        return classNameMapper.convert(input);
    }

    @ExtensionMethod
    public String getEnumValueName(final String input) {
        return StringCaseFormat.UPPER_UNDERSCORE_CASE.apply(input);
    }

    @ExtensionMethod
    public String getCapitalize(final String input) {
        return StringUtils.capitalize(input);
    }

    @ExtensionMethod
    public String getUpperCase(final String input) {
        return StringUtils.upperCase(input);
    }

}
