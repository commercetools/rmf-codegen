package com.commercetools.zone;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Zone.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ZoneIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Zone zone = ZoneFixtures.createZone();
        Zone deletedZone = ZoneFixtures.deleteZone(zone.getId(), zone.getVersion());

        Assertions.assertNotNull(deletedZone);
        Assertions.assertEquals(zone.getId(), deletedZone.getId());
    }
    
    @Test
    public void getById() {
        ZoneFixtures.withZone(zone -> {
            Zone queriedZone = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withId(zone.getId())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedZone);
            Assertions.assertEquals(zone.getId(), queriedZone.getId());
        });
    }

    @Test
    public void getByKey() {
        ZoneFixtures.withZone(zone -> {
            Zone queriedZone = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withKey(zone.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedZone);
            Assertions.assertEquals(zone.getId(), queriedZone.getId());
        });
    }
    
    @Test
    public void query() {
        ZoneFixtures.withZone(zone -> {
            ZonePagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .get()
                    .addWhere("id=" + "\"" + zone.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), zone.getId());
        });
    }
    
    @Test
    public void updateById() {
        ZoneFixtures.withUpdateableZone(zone -> {
            List<ZoneUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ZoneSetKeyActionBuilder.of().key(newKey).build());
            
            Zone updatedZone = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withId(zone.getId())
                    .post(ZoneUpdateBuilder.of()
                            .actions(updateActions)
                            .version(zone.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedZone);
            Assertions.assertEquals(updatedZone.getKey(), newKey);
            
            return updatedZone;
        });
    }

    @Test
    public void updateByKey() {
        ZoneFixtures.withUpdateableZone(zone -> {
            List<ZoneUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ZoneSetKeyActionBuilder.of().key(newKey).build());

            Zone updatedZone = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withKey(zone.getKey())
                    .post(ZoneUpdateBuilder.of()
                            .actions(updateActions)
                            .version(zone.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedZone);
            Assertions.assertEquals(updatedZone.getKey(), newKey);

            return updatedZone;
        });
    }
    
    @Test
    public void deleteByKey() {
        Zone zone = ZoneFixtures.createZone();
        Zone deletedZone = ZoneFixtures.deleteZone(zone.getId(), zone.getVersion());

        Assertions.assertNotNull(deletedZone);
        Assertions.assertEquals(zone.getId(), deletedZone.getId());
    }
    
}
