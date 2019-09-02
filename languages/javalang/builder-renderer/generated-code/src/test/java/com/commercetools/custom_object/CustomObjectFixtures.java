package com.commercetools.custom_object;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.CustomObject.CustomObject;
import com.commercetools.models.CustomObject.CustomObjectDraft;
import com.commercetools.models.CustomObject.CustomObjectDraftBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CustomObjectFixtures {
    
    public static void withCustomObject(final Consumer<CustomObject> consumer) {
        CustomObject customObject = createCustomObject();
        consumer.accept(customObject);
        deleteCustomObject(customObject.getId(), customObject.getVersion());
    }
    
    public static void withUpdateableCustomObject(final UnaryOperator<CustomObject> operator) {
        CustomObject customObject = createCustomObject();
        customObject = operator.apply(customObject);
        deleteCustomObject(customObject.getId(), customObject.getVersion());
    }
    
    public static CustomObject createCustomObject() {
        CustomObjectDraft customObjectDraft = CustomObjectDraftBuilder.of()
                .container("a")
                .key(CommercetoolsTestUtils.randomKey())
                .value(CommercetoolsTestUtils.randomString())
                .build();
        
        CustomObject customObject = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .customObjects()
                .post(customObjectDraft)
                .executeBlocking();

        Assertions.assertNotNull(customObject);
        
        return customObject;
    }
    
    public static CustomObject deleteCustomObject(final String id, final Long version) {
        CustomObject customObject = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .customObjects()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(customObject);
        return customObject;
    }
}
