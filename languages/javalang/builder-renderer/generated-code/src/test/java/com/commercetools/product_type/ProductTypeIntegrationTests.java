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
    public void createAndDelete() {
        ProductTypeDraft productTypeDraft = ProductTypeDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomString())
                .description(CommercetoolsTestUtils.randomString())
                .build();

        ProductType productType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productTypes()
                .post(productTypeDraft)
                .executeBlocking();

        Assertions.assertNotNull(productType);
        Assertions.assertEquals(productType.getName(), productTypeDraft.getName());
        Assertions.assertEquals(productType.getDescription(), productTypeDraft.getDescription());
        
        ProductType deletedProductType = ProductTypeFixtures.deleteProductType(productType.getId(), productType.getVersion());
        Assertions.assertNotNull(deletedProductType);
        Assertions.assertEquals(deletedProductType.getId(), productType.getId());
    }
    
    @Test
    public void getById() {
        ProductTypeFixtures.withProductType(productType -> {
            ProductType queriedProductType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
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
            ProductType queriedProductType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
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
            ProductTypePagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
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
        List<ProductTypeUpdateAction> updateActions = new ArrayList<>();
        String newName = CommercetoolsTestUtils.randomString();
        updateActions.add(ProductTypeChangeNameActionBuilder.of().name(newName).build());
        
        ProductTypeFixtures.withUpdateableProductType(productType -> {
            ProductType updatedProductType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withId(productType.getId())
                    .post(ProductTypeUpdateBuilder.of()
                            .version(productType.getVersion())
                            .actions(updateActions).build())
                    .executeBlocking();
            Assertions.assertNotNull(updatedProductType);
            Assertions.assertEquals(updatedProductType.getName(), newName);
            return updatedProductType;
        });
    }

    @Test
    public void updateByKey() {
        List<ProductTypeUpdateAction> updateActions = new ArrayList<>();
        String newName = CommercetoolsTestUtils.randomString();
        updateActions.add(ProductTypeChangeNameActionBuilder.of().name(newName).build());

        ProductTypeFixtures.withUpdateableProductType(productType -> {
            ProductType updatedProductType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productTypes()
                    .withKey(productType.getKey())
                    .post(ProductTypeUpdateBuilder.of()
                            .version(productType.getVersion())
                            .actions(updateActions).build())
                    .executeBlocking();
            Assertions.assertNotNull(updatedProductType);
            Assertions.assertEquals(updatedProductType.getName(), newName);
            return updatedProductType;
        });
    }
    
}
