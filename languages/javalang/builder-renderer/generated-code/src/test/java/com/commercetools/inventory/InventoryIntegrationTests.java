package com.commercetools.inventory;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Inventory.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class InventoryIntegrationTests {
    
    @Test
    public void createAndDelete() {
        InventoryEntryDraft inventoryEntryDraft = InventoryEntryDraftBuilder.of()
                .sku(CommercetoolsTestUtils.randomString())
                .quantityOnStock(10L)
                .build();

        InventoryEntry inventoryEntry  = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .inventory()
                .post(inventoryEntryDraft)
                .executeBlocking();

        Assertions.assertNotNull(inventoryEntry);
        Assertions.assertEquals(inventoryEntry.getSku(), inventoryEntryDraft.getSku());
        Assertions.assertEquals(inventoryEntry.getQuantityOnStock(), inventoryEntryDraft.getQuantityOnStock());

        InventoryEntry deletedInventoryEntry = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .inventory()
                .withId(inventoryEntry.getId())
                .delete()
                .executeBlocking();

        Assertions.assertNotNull(deletedInventoryEntry);
        Assertions.assertEquals(inventoryEntry.getId(), deletedInventoryEntry.getId());
    }
    
    @Test
    public void getById() {
        InventoryEntryFixtures.withInventoryEntry(inventoryEntry -> {
            InventoryEntry queriedInventoryEntry = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .withId(inventoryEntry.getId())
                    .get()
                    .executeBlocking();
            Assertions.assertNotNull(queriedInventoryEntry);
            Assertions.assertEquals(inventoryEntry.getId(), queriedInventoryEntry.getId());
        });
    }

    @Test
    public void query() {
        InventoryEntryFixtures.withInventoryEntry(inventoryEntry -> {
            InventoryPagedQueryResponse inventoryPagedQueryResponse = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .get()
                    .addWhere("id=" + "\"" + inventoryEntry.getId() +"\"")
                    .executeBlocking();
            Assertions.assertNotNull(inventoryPagedQueryResponse);
            Assertions.assertEquals(inventoryPagedQueryResponse.getResults().get(0).getId(), inventoryEntry.getId());
        });
    }

    @Test
    public void update() {
        InventoryEntryFixtures.withUpdatableInventoryEntry(inventoryEntry -> {
            List<InventoryEntryUpdateAction> updateActions = new ArrayList<>();
            updateActions.add(InventoryEntrySetRestockableInDaysActionBuilder.of()
                    .restockableInDays(10L).build());

            InventoryEntry updatedInventoryEntry = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .withId(inventoryEntry.getId())
                    .post(InventoryEntryUpdateBuilder.of()
                        .actions(updateActions)
                        .version(inventoryEntry.getVersion())
                        .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedInventoryEntry);
            Assertions.assertEquals(updatedInventoryEntry.getRestockableInDays(), Long.valueOf(10));
            
            return updatedInventoryEntry;
        });
    }
}
