package com.commercetools.product_discount;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ProductDiscount.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductDiscountIntegrationTests {
    
    @Test
    public void createAndDelete() {
        ProductDiscountDraft productDiscountDraft =  ProductDiscountDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .key(CommercetoolsTestUtils.randomKey())
                .sortOrder("0.4")
                .predicate("product.key=\"random-key\"")
                .value(ProductDiscountValueExternalBuilder.of().type("external").build())
                .isActive(false)
                .build();

        ProductDiscount productDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productDiscounts()
                .post(productDiscountDraft)
                .executeBlocking();

        Assert.assertNotNull(productDiscount);
        Assert.assertEquals(productDiscount.getKey(), productDiscountDraft.getKey());

        ProductDiscount deletedProductDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productDiscounts()
                .withId(productDiscount.getId())
                .delete()
                .addVersion(productDiscount.getVersion())
                .executeBlocking();

        Assert.assertNotNull(deletedProductDiscount);
        Assert.assertEquals(productDiscount.getId(), deletedProductDiscount.getId());
    }
    
    @Test
    public void getById() {
        ProductDiscountFixtures.withProductDiscount(productDiscount -> {
            ProductDiscount queriedProductDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productDiscounts()
                    .withId(productDiscount.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedProductDiscount);
            Assert.assertEquals(queriedProductDiscount.getId(), productDiscount.getId());
        });
    }

    @Test
    public void getByKey() {
        ProductDiscountFixtures.withProductDiscount(productDiscount -> {
            ProductDiscount queriedProductDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productDiscounts()
                    .withKey(productDiscount.getKey())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedProductDiscount);
            Assert.assertEquals(queriedProductDiscount.getKey(), productDiscount.getKey());
        });
    }
    
    @Test
    public void query() {
        ProductDiscountFixtures.withProductDiscount(productDiscount -> {
            ProductDiscountPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productDiscounts()
                    .get()
                    .addWhere("id=" + "\"" + productDiscount.getId() + "\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), productDiscount.getId());
        });
    }

    @Test
    public void updateById() {
        ProductDiscountFixtures.withUpdateableProductDiscount(productDiscount -> {
            List<ProductDiscountUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ProductDiscountSetKeyActionBuilder.of().key(newKey).build());
            ProductDiscount updatedProductDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productDiscounts()
                    .withId(productDiscount.getId())
                    .post(ProductDiscountUpdateBuilder.of()
                            .actions(updateActions)
                            .version(productDiscount.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedProductDiscount);
            Assert.assertEquals(updatedProductDiscount.getKey(), newKey);
            return updatedProductDiscount; 
        });
    }

    @Test
    public void updateByKey() {
        ProductDiscountFixtures.withUpdateableProductDiscount(productDiscount -> {
            List<ProductDiscountUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(ProductDiscountSetKeyActionBuilder.of().key(newKey).build());
            ProductDiscount updatedProductDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .productDiscounts()
                    .withKey(productDiscount.getKey())
                    .post(ProductDiscountUpdateBuilder.of()
                            .actions(updateActions)
                            .version(productDiscount.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedProductDiscount);
            Assert.assertEquals(updatedProductDiscount.getKey(), newKey);
            return updatedProductDiscount;
        });
    }
    
}
