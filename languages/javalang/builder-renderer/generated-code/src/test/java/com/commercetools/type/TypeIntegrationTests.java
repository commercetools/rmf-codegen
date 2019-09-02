package com.commercetools.type;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Type.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TypeIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Type type = TypeFixtures.createType();
        Type deletedType = TypeFixtures.deleteType(type.getId(), type.getVersion());

        Assertions.assertEquals(type.getId(), deletedType.getId());
    }
    
    @Test
    public void getById() {
        TypeFixtures.withType(type -> {
            Type queriedType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .types()
                    .withId(type.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedType);
            Assertions.assertEquals(type.getId(), queriedType.getId());
        });
    }

    @Test
    public void getByKey() {
        TypeFixtures.withType(type -> {
            Type queriedType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .types()
                    .withKey(type.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedType);
            Assertions.assertEquals(type.getId(), queriedType.getId());
        });
    }
    
    @Test
    public void query() {
        TypeFixtures.withType(type -> {
            TypePagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .types()
                    .get()
                    .addWhere("id=" + "\"" + type.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), type.getId());
        });
    }
    
    @Test
    public void updateById() {
        TypeFixtures.withUpdateableType(type -> {
            List<TypeUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(TypeChangeKeyActionBuilder.of().key(newKey).build());
            
            Type updatedType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .types()
                    .withId(type.getId())
                    .post(TypeUpdateBuilder.of()
                            .actions(updateActions)
                            .version(type.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedType);
            Assertions.assertEquals(updatedType.getKey(), newKey);
            
            return updatedType;
        });
    }

    @Test
    public void updateByKey() {
        TypeFixtures.withUpdateableType(type -> {
            List<TypeUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(TypeChangeKeyActionBuilder.of().key(newKey).build());

            Type updatedType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .types()
                    .withKey(type.getKey())
                    .post(TypeUpdateBuilder.of()
                            .actions(updateActions)
                            .version(type.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedType);
            Assertions.assertEquals(updatedType.getKey(), newKey);

            return updatedType;
        });
    }

    @Test
    public void deleteByKey() {
        Type type = TypeFixtures.createType();
        Type deletedType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .types()
                .withKey(type.getKey())
                .delete()
                .addVersion(type.getVersion())
                .executeBlocking();

        Assertions.assertNotNull(deletedType);
        Assertions.assertEquals(type.getId(), deletedType.getId());
    }

}
