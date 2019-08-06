package com.commercetools.product;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Product.*;
import com.commercetools.product.type.ProductTypeFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        ProductTypeFixtures.withProductType(productType -> {
            Product product = ProductFixtures.createProduct(productType);
            Assert.assertNotNull(product);

            Product deletedProduct = ProductFixtures.deleteProductById(product.getId(), product.getVersion());
            Assert.assertNotNull(deletedProduct);
        });
    }
    
    @Test
    public void getById() {
        ProductFixtures.withProduct(product -> {
            Product queriedProduct = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .products()
                    .withId(product.getId())
                    .get()
                    .executeBlocking();
            Assert.assertNotNull(queriedProduct);
            Assert.assertEquals(product.getId(), queriedProduct.getId());
        });
    }
    
    @Test
    public void getByKey() {
        ProductFixtures.withProduct(product -> {
            Product queriedProduct = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .products()
                    .withKey(product.getKey())
                    .get()
                    .executeBlocking();
            Assert.assertNotNull(queriedProduct);
            Assert.assertEquals(product.getKey(), queriedProduct.getKey());
        });
    }
    
    @Test
    public void updateById(){
        ProductFixtures.withUpdateableProduct(product -> {
            List<ProductUpdateAction> updateActions = new ArrayList<>();
            LocalizedString newName = CommercetoolsTestUtils.randomLocalizedString();
            updateActions.add(ProductChangeNameActionBuilder.of().name(newName).build());
            
            Product updatedProduct = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .products()
                    .withId(product.getId())
                    .post(ProductUpdateBuilder.of()
                            .actions(updateActions)
                            .version(product.getVersion())
                            .build())
                    .executeBlocking();
            
            Assert.assertNotNull(updatedProduct);
            
            return updatedProduct;
        });
    }

    @Test
    public void updateByKey(){
        ProductFixtures.withUpdateableProduct(product -> {
            List<ProductUpdateAction> updateActions = new ArrayList<>();
            LocalizedString newName = CommercetoolsTestUtils.randomLocalizedString();
            updateActions.add(ProductChangeNameActionBuilder.of().name(newName).build());

            Product updatedProduct = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .products()
                    .withKey(product.getKey())
                    .post(ProductUpdateBuilder.of()
                            .actions(updateActions)
                            .version(product.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedProduct);

            return updatedProduct;
        });
    }
    
    @Test
    public void query() {
        ProductFixtures.withProduct(product -> {
            ProductPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .products()
                    .get()
                    .addWhere("id=" + "\"" + product.getId() + "\"")
                    .executeBlocking();
            Assert.assertEquals(response.getResults().size(), 1);
            Assert.assertEquals(response.getResults().get(0).getId(), product.getId());
        });
    }
}