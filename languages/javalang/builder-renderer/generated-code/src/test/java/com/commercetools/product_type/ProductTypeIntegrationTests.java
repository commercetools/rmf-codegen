package com.commercetools.product_type;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ProductType.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeIntegrationTests {
    
    @Test
    public void createAndDeleteById() {
        ProductType productType = ProductTypeFixtures.createProductType();
        ProductType deleteProductType = ProductTypeFixtures.deleteProductType(productType.getId(), productType.getVersion());

        Assertions.assertEquals(productType.getId(), deleteProductType.getId());
    }
    
    @Test
    public void getById() {
        ProductTypeFixtures.withProductType(productType -> {
            ProductType queriedProductType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withId(productType.getId())
                    .get()
                    .executeBlocking();
            
            Assertions.assertNotNull(queriedProductType);
            Assertions.assertEquals(queriedProductType.getId(), productType.getId());
        });
    }

    @Test
    public void getByKey() {
        ProductTypeFixtures.withProductType(productType -> {
            ProductType queriedProductType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withKey(productType.getKey())
                    .get()
                    .executeBlocking();

            Assertions.assertNotNull(queriedProductType);
            Assertions.assertEquals(queriedProductType.getId(), productType.getId());
        });
    }
    
    @Test
    public void query() {
        ProductTypeFixtures.withProductType(productType -> {
            ProductTypePagedQueryResponse response = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .get()
                    .addWhere("id=" + "\"" + productType.getId() + "\"")
                    .executeBlocking();

            Assertions.assertNotNull(response);
            Assertions.assertEquals(response.getResults().get(0).getId(), productType.getId());
        });
    }
    
    @Test
    public void updateById() {
        ProductTypeFixtures.withUpdateableProductType(productType -> {
            List<ProductTypeUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ProductTypeSetKeyActionBuilder.of().key(newKey).build());
            ProductType updatedProductType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withId(productType.getId())
                    .post(ProductTypeUpdateBuilder.of()
                            .actions(updateActions)
                            .version(productType.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedProductType);
            Assertions.assertEquals(updatedProductType.getKey(), newKey);
            
            return updatedProductType;
        });
    }

    @Test
    public void updateByKey() {
        ProductTypeFixtures.withUpdateableProductType(productType -> {
            List<ProductTypeUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ProductTypeSetKeyActionBuilder.of().key(newKey).build());
            ProductType updatedProductType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withKey(productType.getKey())
                    .post(ProductTypeUpdateBuilder.of()
                            .actions(updateActions)
                            .version(productType.getVersion())
                            .build())
                    .executeBlocking();

            Assertions.assertNotNull(updatedProductType);
            Assertions.assertEquals(updatedProductType.getKey(), newKey);

            return updatedProductType;
        });
    }
    
    @Test
    public void deleteByKey() {
        ProductType productType = ProductTypeFixtures.createProductType();
        ProductType deletedProductType = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .productTypes()
                .withKey(productType.getKey())
                .delete()
                .addVersion(productType.getVersion())
                .executeBlocking();
        
        Assertions.assertNotNull(deletedProductType);
    }
    
}
