package com.commercetools.store;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Store.Store;
import com.commercetools.models.Store.StoreDraft;
import com.commercetools.models.Store.StoreDraftBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class StoreFixtures {
    
    
    public static void withStore(final Consumer<Store> consumer) {
        Store store = createStore();
        consumer.accept(store);
        deleteStore(store.getId(), store.getVersion());
    }
    
    public static void withUpdateableStore(final UnaryOperator<Store> operator) {
        Store store = createStore();
        store = operator.apply(store);
        deleteStore(store.getId(), store.getVersion());
    }
    
    public static Store createStore() {
        StoreDraft storeDraft = StoreDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .build();
        
        Store store = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .stores()
                .post(storeDraft)
                .executeBlocking();

        Assertions.assertNotNull(store);
        Assertions.assertEquals(storeDraft.getKey(), store.getKey());
        
        return store;
    }
    
    public static Store deleteStore(final String id, final Long version) {
        Store store = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .stores()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();
        
        Assertions.assertNotNull(store);
        Assertions.assertEquals(store.getId(), id);
        
        return store;
    }
    
}
