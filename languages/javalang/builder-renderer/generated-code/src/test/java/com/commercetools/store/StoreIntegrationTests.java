package com.commercetools.store;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Store.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StoreIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Store store = StoreFixtures.createStore();
        Store deletedStore = StoreFixtures.deleteStore(store.getId(), store.getVersion());
        
        Assertions.assertEquals(store.getId(), deletedStore.getId());
    }
    
    @Test
    public void getById() {
        StoreFixtures.withStore(store -> {
            Store queriedStore = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .stores()
                    .withId(store.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedStore);
            Assertions.assertEquals(queriedStore.getId(), store.getId());
        });
    }

    @Test
    public void getByKey() {
        StoreFixtures.withStore(store -> {
            Store queriedStore = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .stores()
                    .withKey(store.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedStore);
            Assertions.assertEquals(queriedStore.getId(), store.getId());
        });
    }
    
    @Test
    public void query() {
        StoreFixtures.withStore(store -> {
            StorePagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .stores()
                    .get()
                    .addWhere("id=" + "\"" + store.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), store.getId());
        });
    }
    
    @Test
    public void updateById() {
        StoreFixtures.withUpdateableStore(store -> {
            List<StoreUpdateAction> updateActions = new ArrayList<>();
            LocalizedString newName = CommercetoolsTestUtils.randomLocalizedString();
            updateActions.add(StoreSetNameActionBuilder.of().name(newName).build());
            
            Store updatedStore = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .stores()
                    .withId(store.getId())
                    .post(StoreUpdateBuilder.of()
                            .actions(updateActions)
                            .version(store.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedStore);
            
            return updatedStore;
        });
    }

    @Test
    public void updateByKey() {
        StoreFixtures.withUpdateableStore(store -> {
            List<StoreUpdateAction> updateActions = new ArrayList<>();
            LocalizedString newName = CommercetoolsTestUtils.randomLocalizedString();
            updateActions.add(StoreSetNameActionBuilder.of().name(newName).build());

            Store updatedStore = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .stores()
                    .withKey(store.getKey())
                    .post(StoreUpdateBuilder.of()
                            .actions(updateActions)
                            .version(store.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedStore);

            return updatedStore;
        });
    }
    
    @Test
    public void deleteByKey() {
        Store store = StoreFixtures.createStore();
        Store deletedStore = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .stores()
                .withKey(store.getKey())
                .delete()
                .addVersion(store.getVersion())
                .executeBlocking();

        Assertions.assertNotNull(deletedStore);
        Assertions.assertEquals(store.getId(), deletedStore.getId());
    }
    
}
