package com.commercetools.cart_discount;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.CartDiscount.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CartDiscountIntegrationTests {
    
    @Test
    public void createAndDelete() {
        CartDiscountValue cartDiscountValue = CartDiscountValueRelativeBuilder.of()
                .permyriad(10L)
                .build();

        CartDiscountDraft cartDiscountDraft = CartDiscountDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .key(CommercetoolsTestUtils.randomKey())
                .value(cartDiscountValue)
                .cartPredicate("country=\"DE\"")
                .target(CartDiscountShippingCostTargetBuilder.of().type("shipping").build())
                .sortOrder("0.42")
                .build();

        CartDiscount cartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .cartDiscounts()
                .post(cartDiscountDraft)
                .executeBlocking();

        Assert.assertNotNull(cartDiscount);
        Assert.assertEquals(cartDiscountDraft.getKey(), cartDiscount.getKey());
        
        CartDiscount deletedCartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .cartDiscounts()
                .withId(cartDiscount.getId())
                .delete()
                .addVersion(cartDiscount.getVersion())
                .executeBlocking();

        Assert.assertNotNull(deletedCartDiscount);
    }
    
    @Test
    public void getById() {
        CartDiscountFixtures.withCartDiscount(cartDiscount -> {
            CartDiscount queriedCartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .cartDiscounts()
                    .withId(cartDiscount.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedCartDiscount);
            Assert.assertEquals(queriedCartDiscount.getId(), cartDiscount.getId());
        });
    }

    @Test
    public void getByKey() {
        CartDiscountFixtures.withCartDiscount(cartDiscount -> {
            CartDiscount queriedCartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .cartDiscounts()
                    .withKey(cartDiscount.getKey())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedCartDiscount);
            Assert.assertEquals(queriedCartDiscount.getId(), cartDiscount.getId());
        });
    }

    @Test
    public void query() {
        CartDiscountFixtures.withCartDiscount(cartDiscount -> {
            CartDiscountPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .cartDiscounts()
                    .get()
                    .addWhere("id=" + "\"" + cartDiscount.getId() + "\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), cartDiscount.getId());
        });
    }

    @Test
    public void updateById() {
        CartDiscountFixtures.withUpdateableCartDiscount(cartDiscount -> {
            List<CartDiscountUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(CartDiscountSetKeyActionBuilder.of().key(newKey).build());
            CartDiscount updatedCartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .cartDiscounts()
                    .withId(cartDiscount.getId())
                    .post(CartDiscountUpdateBuilder.of()
                            .actions(updateActions)
                            .version(cartDiscount.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedCartDiscount);
            Assert.assertEquals(updatedCartDiscount.getKey(), newKey);
            
            return updatedCartDiscount;
        });
    }

    @Test
    public void updateByKey() {
        CartDiscountFixtures.withUpdateableCartDiscount(cartDiscount -> {
            List<CartDiscountUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(CartDiscountSetKeyActionBuilder.of().key(newKey).build());
            CartDiscount updatedCartDiscount = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .cartDiscounts()
                    .withKey(cartDiscount.getKey())
                    .post(CartDiscountUpdateBuilder.of()
                            .actions(updateActions)
                            .version(cartDiscount.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedCartDiscount);
            Assert.assertEquals(updatedCartDiscount.getKey(), newKey);

            return updatedCartDiscount;
        });
    }
}
