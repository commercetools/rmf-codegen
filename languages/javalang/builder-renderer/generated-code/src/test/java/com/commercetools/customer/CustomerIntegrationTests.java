package com.commercetools.customer;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Customer.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CustomerIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        Customer customer = CustomerFixtures.createCustomer();
        Customer deletedCustomer = CustomerFixtures.deleteCustomer(customer.getId(), customer.getVersion());
        Assertions.assertEquals(customer.getId(), deletedCustomer.getId());
    }
    
    @Test
    public void getById() {
        CustomerFixtures.withCustomer(customer -> {
            Customer queriedCustomer = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customers()
                    .withId(customer.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedCustomer);
            Assertions.assertEquals(queriedCustomer.getId(), customer.getId());
        });
    }

    @Test
    public void getByKey() {
        CustomerFixtures.withCustomer(customer -> {
            Customer queriedCustomer = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customers()
                    .withKey(customer.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedCustomer);
            Assertions.assertEquals(queriedCustomer.getId(), customer.getId());
        });
    }
    
    @Test
    public void query() {
        CustomerFixtures.withCustomer(customer -> {
            CustomerPagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customers()
                    .get()
                    .addWhere("id=" +  "\"" + customer.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), customer.getId());
        });
    }
    
    @Test
    public void updateById() {
        CustomerFixtures.withUpdateableCustomer(customer -> {
            List<CustomerUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(CustomerSetKeyActionBuilder.of().key(newKey).build());
            
            Customer updatedCustomer = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customers()
                    .withId(customer.getId())
                    .post(CustomerUpdateBuilder.of().actions(updateActions).version(customer.getVersion()).build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedCustomer);
            Assertions.assertEquals(updatedCustomer.getKey(), newKey);
            
            return updatedCustomer;
        });
    }

    @Test
    public void updateByKey() {
        CustomerFixtures.withUpdateableCustomer(customer -> {
            List<CustomerUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(CustomerSetKeyActionBuilder.of().key(newKey).build());

            Customer updatedCustomer = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .customers()
                    .withKey(customer.getKey())
                    .post(CustomerUpdateBuilder.of().actions(updateActions).version(customer.getVersion()).build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedCustomer);
            Assertions.assertEquals(updatedCustomer.getKey(), newKey);

            return updatedCustomer;
        });
    }
    
    @Test
    public void deleteByKey() {
        Customer customer = CustomerFixtures.createCustomer();
        Customer deletedCustomer = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .customers()
                .withKey(customer.getKey())
                .delete()
                .addVersion(customer.getVersion())
                .executeBlocking();

        Assertions.assertNotNull(deletedCustomer);
        Assertions.assertEquals(customer.getId(), deletedCustomer.getId());
    }
}
