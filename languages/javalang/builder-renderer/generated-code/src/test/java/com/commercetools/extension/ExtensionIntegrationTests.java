package com.commercetools.extension;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Extension.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExtensionIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Extension extension = ExtensionFixtures.createExtension();
        Extension deletedExtension = ExtensionFixtures.deleteExtension(extension.getId(), extension.getVersion());

        Assertions.assertEquals(extension.getId(), deletedExtension.getId());
    }
    
    @Test
    public void getById() {
        ExtensionFixtures.withExtension(extension -> {
            Extension queriedExtension = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .extensions()
                    .withId(extension.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedExtension);
            Assertions.assertEquals(queriedExtension.getId(), extension.getId());
        });
    }

    @Test
    public void getByKey() {
        ExtensionFixtures.withExtension(extension -> {
            Extension queriedExtension = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .extensions()
                    .withKey(extension.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedExtension);
            Assertions.assertEquals(queriedExtension.getId(), extension.getId());
        });
    }
    
    @Test
    public void query() {
        ExtensionFixtures.withExtension(extension -> {
            ExtensionPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .extensions()
                    .get()
                    .addWhere("id=" + "\"" + extension.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), extension.getId());
        });
    }
    
    @Test
    public void updateById() {
        ExtensionFixtures.withUpdateableExtension(extension -> {
            List<ExtensionUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ExtensionSetKeyActionBuilder.of().key(newKey).build());
            Extension updatedExtension = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .extensions()
                    .withId(extension.getId())
                    .post(ExtensionUpdateBuilder.of()
                            .actions(updateActions)
                            .version(extension.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedExtension);
            Assertions.assertEquals(updatedExtension.getKey(), newKey);
            
            return updatedExtension;
        });
    }

    @Test
    public void updateByKey() {
        ExtensionFixtures.withUpdateableExtension(extension -> {
            List<ExtensionUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ExtensionSetKeyActionBuilder.of().key(newKey).build());
            Extension updatedExtension = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .extensions()
                    .withKey(extension.getKey())
                    .post(ExtensionUpdateBuilder.of()
                            .actions(updateActions)
                            .version(extension.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedExtension);
            Assertions.assertEquals(updatedExtension.getKey(), newKey);

            return updatedExtension;
        });
    }
    
    @Test
    public void deleteByKey() {
        Extension extension = ExtensionFixtures.createExtension();
        Extension deletedExtension = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .extensions()
                .withId(extension.getId())
                .delete()
                .addVersion(extension.getVersion())
                .executeBlocking();

        Assertions.assertNotNull(deletedExtension);
        Assertions.assertEquals(extension.getId(), deletedExtension.getId());
    }
    
}
