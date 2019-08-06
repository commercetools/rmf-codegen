package com.commercetools.product.projection;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Product.ProductProjection;
import com.commercetools.models.Product.ProductProjectionPagedQueryResponse;
import com.commercetools.product.ProductFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

public class ProductProjectionIntegrationTests {
    
    @Test
    public void getById() {
        ProductFixtures.withProduct(product -> {
            ProductProjection productProjection = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productProjections()
                    .withId(product.getId())
                    .get()
                    .addStaged(true)
                    .executeBlocking();

            Assert.assertEquals(productProjection.getId(), product.getId());
        });
    }

    @Test
    public void getByKey() {
        ProductFixtures.withProduct(product -> {
            ProductProjection productProjection = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productProjections()
                    .withKey(product.getKey())
                    .get()
                    .addStaged(true)
                    .executeBlocking();

            Assert.assertEquals(productProjection.getKey(), product.getKey());
        });
    }
    
    @Test
    public void query() {
        ProductFixtures.withProduct(product -> {
            ProductProjectionPagedQueryResponse productProjectionPagedQueryResponse = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productProjections()
                    .get()
                    .addStaged(true)
                    .addWhere("id=" + "\"" + product.getId() +  "\"")
                    .executeBlocking();
            
            Assert.assertEquals(productProjectionPagedQueryResponse.getResults().get(0).getId(), product.getId());
        });
    }
}