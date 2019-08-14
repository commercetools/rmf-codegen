package com.commercetools.shipping_method;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ShippingMethod.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ShippingMethodIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        ShippingMethod shippingMethod = ShippingMethodFixtures.createShippingMethod();
        ShippingMethodFixtures.deleteShippingMethod(shippingMethod.getId(), shippingMethod.getVersion());
    }
    
    @Test
    public void getById() {
        ShippingMethodFixtures.withShippingMethod(shippingMethod -> {
            ShippingMethod queriedShippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .shippingMethods()
                    .withId(shippingMethod.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedShippingMethod);
            Assert.assertEquals(queriedShippingMethod.getId(), shippingMethod.getId());
        });
    }

    @Test
    public void getByKey() {
        ShippingMethodFixtures.withShippingMethod(shippingMethod -> {
            ShippingMethod queriedShippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .shippingMethods()
                    .withKey(shippingMethod.getKey())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedShippingMethod);
            Assert.assertEquals(queriedShippingMethod.getId(), shippingMethod.getId());
        });
    }
    
    @Test
    public void query() {
        ShippingMethodFixtures.withShippingMethod(shippingMethod -> {
            ShippingMethodPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .shippingMethods()
                    .get()
                    .addWhere("id=" + "\"" + shippingMethod.getId() + "\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), shippingMethod.getId());
        });
    }
    
    @Test
    public void updateById() {
        ShippingMethodFixtures.withUpdateableShippingMethod(shippingMethod -> {
            List<ShippingMethodUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ShippingMethodSetKeyActionBuilder.of().key(newKey).build());

            ShippingMethod updatedShippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .shippingMethods()
                    .withId(shippingMethod.getId())
                    .post(ShippingMethodUpdateBuilder.of().actions(updateActions).version(shippingMethod.getVersion()).build())
                    .executeBlocking();
            
            Assert.assertNotNull(updatedShippingMethod);
            Assert.assertEquals(updatedShippingMethod.getKey(), newKey);
            
            return updatedShippingMethod;
        });
    }

    @Test
    public void updateByKey() {
        ShippingMethodFixtures.withUpdateableShippingMethod(shippingMethod -> {
            List<ShippingMethodUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ShippingMethodSetKeyActionBuilder.of().key(newKey).build());

            ShippingMethod updatedShippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .shippingMethods()
                    .withKey(shippingMethod.getKey())
                    .post(ShippingMethodUpdateBuilder.of().actions(updateActions).version(shippingMethod.getVersion()).build())
                    .executeBlocking();

            Assert.assertNotNull(updatedShippingMethod);
            Assert.assertEquals(updatedShippingMethod.getKey(), newKey);

            return updatedShippingMethod;
        });
    }

    @Test
    public void deleteByKey(){
        ShippingMethod shippingMethod = ShippingMethodFixtures.createShippingMethod();
        ShippingMethod deletedShippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .shippingMethods()
                .withKey(shippingMethod.getKey())
                .delete()
                .addVersion(shippingMethod.getVersion())
                .executeBlocking();
        
        Assert.assertNotNull(deletedShippingMethod);
        Assert.assertEquals(shippingMethod.getId(), deletedShippingMethod.getId());
    }
}