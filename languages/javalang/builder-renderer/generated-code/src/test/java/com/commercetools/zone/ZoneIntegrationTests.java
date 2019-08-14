package com.commercetools.zone;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Zone.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ZoneIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Zone zone = ZoneFixtures.createZone();
        Zone deletedZone = ZoneFixtures.deleteZone(zone.getId(), zone.getVersion());

        Assert.assertNotNull(deletedZone);
        Assert.assertEquals(zone.getId(), deletedZone.getId());
    }
    
    @Test
    public void getById() {
        ZoneFixtures.withZone(zone -> {
            Zone queriedZone = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withId(zone.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedZone);
            Assert.assertEquals(zone.getId(), queriedZone.getId());
        });
    }

    @Test
    public void getByKey() {
        ZoneFixtures.withZone(zone -> {
            Zone queriedZone = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withKey(zone.getKey())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedZone);
            Assert.assertEquals(zone.getId(), queriedZone.getId());
        });
    }
    
    @Test
    public void query() {
        ZoneFixtures.withZone(zone -> {
            ZonePagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .get()
                    .addWhere("id=" + "\"" + zone.getId() + "\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), zone.getId());
        });
    }
    
    @Test
    public void updateById() {
        ZoneFixtures.withUpdateableZone(zone -> {
            List<ZoneUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ZoneSetKeyActionBuilder.of().key(newKey).build());
            
            Zone updatedZone = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withId(zone.getId())
                    .post(ZoneUpdateBuilder.of()
                            .actions(updateActions)
                            .version(zone.getVersion())
                            .build())
                    .executeBlocking();
            
            Assert.assertNotNull(updatedZone);
            Assert.assertEquals(updatedZone.getKey(), newKey);
            
            return updatedZone;
        });
    }

    @Test
    public void updateByKey() {
        ZoneFixtures.withUpdateableZone(zone -> {
            List<ZoneUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ZoneSetKeyActionBuilder.of().key(newKey).build());

            Zone updatedZone = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .zones()
                    .withKey(zone.getKey())
                    .post(ZoneUpdateBuilder.of()
                            .actions(updateActions)
                            .version(zone.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedZone);
            Assert.assertEquals(updatedZone.getKey(), newKey);

            return updatedZone;
        });
    }
    
    @Test
    public void deleteByKey() {
        Zone zone = ZoneFixtures.createZone();
        Zone deletedZone = ZoneFixtures.deleteZone(zone.getId(), zone.getVersion());

        Assert.assertNotNull(deletedZone);
        Assert.assertEquals(zone.getId(), deletedZone.getId());
    }
    
}
