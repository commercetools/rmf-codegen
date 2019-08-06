package com.commercetools.inventory;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Inventory.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InventoryIntegrationTests {
    
    @Test
    public void createAndDelete() {
        InventoryEntryDraft inventoryEntryDraft = InventoryEntryDraftBuilder.of()
                .sku(CommercetoolsTestUtils.randomString())
                .quantityOnStock(10L)
                .build();

        InventoryEntry inventoryEntry  = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .inventory()
                .post(inventoryEntryDraft)
                .executeBlocking();

        Assert.assertNotNull(inventoryEntry);
        Assert.assertEquals(inventoryEntry.getSku(), inventoryEntryDraft.getSku());
        Assert.assertEquals(inventoryEntry.getQuantityOnStock(), inventoryEntryDraft.getQuantityOnStock());

        InventoryEntry deletedInventoryEntry = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .inventory()
                .withId(inventoryEntry.getId())
                .delete()
                .executeBlocking();
        
        Assert.assertNotNull(deletedInventoryEntry);
        Assert.assertEquals(inventoryEntry.getId(), deletedInventoryEntry.getId());
    }
    
    @Test
    public void getById() {
        InventoryEntryFixtures.withInventoryEntry(inventoryEntry -> {
            InventoryEntry queriedInventoryEntry = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .withId(inventoryEntry.getId())
                    .get()
                    .executeBlocking();
            Assert.assertNotNull(queriedInventoryEntry);
            Assert.assertEquals(inventoryEntry.getId(), queriedInventoryEntry.getId());
        });
    }

    @Test
    public void query() {
        InventoryEntryFixtures.withInventoryEntry(inventoryEntry -> {
            InventoryPagedQueryResponse inventoryPagedQueryResponse = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .get()
                    .addWhere("id=" + "\"" + inventoryEntry.getId() +"\"")
                    .executeBlocking();
            Assert.assertNotNull(inventoryPagedQueryResponse);
            Assert.assertEquals(inventoryPagedQueryResponse.getResults().get(0).getId(), inventoryEntry.getId());
        });
    }

    @Test
    public void update() {
        InventoryEntryFixtures.withUpdatableInventoryEntry(inventoryEntry -> {
            List<InventoryUpdateAction> updateActions = new ArrayList<>();
            updateActions.add(InventorySetRestockableInDaysActionBuilder.of()
                    .restockableInDays(10L).build());

            InventoryEntry updatedInventoryEntry = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .inventory()
                    .withId(inventoryEntry.getId())
                    .post(InventoryUpdateBuilder.of()
                        .actions(updateActions)
                        .version(inventoryEntry.getVersion())
                        .build())
                    .executeBlocking();
            
            Assert.assertNotNull(updatedInventoryEntry);
            Assert.assertEquals(updatedInventoryEntry.getRestockableInDays(), Long.valueOf(10));
            
            return updatedInventoryEntry;
        });
    }
}
