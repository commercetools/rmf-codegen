package com.commercetools.custom_object;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.CustomObject.CustomObject;
import com.commercetools.models.CustomObject.CustomObjectDraft;
import com.commercetools.models.CustomObject.CustomObjectDraftBuilder;
import com.commercetools.models.CustomObject.CustomObjectPagedQueryResponse;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomObjectIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        CustomObject customObject = CustomObjectFixtures.createCustomObject();
        CustomObject deletedCustomObject = CustomObjectFixtures.deleteCustomObject(customObject.getId(), customObject.getVersion());

        Assertions.assertEquals(customObject.getId(), deletedCustomObject.getId());
    }
    
    @Test
    public void getById() {
        CustomObjectFixtures.withCustomObject(customObject -> {
            CustomObject queriedCustomObject = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customObjects()
                    .withId(customObject.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedCustomObject);
            Assertions.assertEquals(customObject.getId(), queriedCustomObject.getId());
        });
    }
    
    @Test
    public void update() {
        CustomObjectFixtures.withUpdateableCustomObject(customObject -> {
            String newValue = CommercetoolsTestUtils.randomString();
            CustomObjectDraft customObjectDraft = CustomObjectDraftBuilder.of()
                    .key(customObject.getKey())
                    .container(customObject.getContainer())
                    .value(newValue)
                    .build();

            CustomObject updatedCustomObject = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customObjects()
                    .post(customObjectDraft)
                    .executeBlocking();

            Assertions.assertNotNull(updatedCustomObject);
            Assertions.assertEquals(updatedCustomObject.getValue(), newValue);
            
            return updatedCustomObject;
        });
    }
    
    @Test
    public void query() {
        CustomObjectFixtures.withCustomObject(customObject -> {
            CustomObjectPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customObjects()
                    .get()
                    .addWhere("id=" + "\"" + customObject.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), customObject.getId());
        });
    }
}
