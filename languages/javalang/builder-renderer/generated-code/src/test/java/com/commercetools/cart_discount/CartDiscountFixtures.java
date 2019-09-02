package com.commercetools.cart_discount;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.CartDiscount.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CartDiscountFixtures {
    
    public static void withCartDiscount(final Consumer<CartDiscount> consumer) {
        CartDiscount cartDiscount = createCartDiscount();
        consumer.accept(cartDiscount);
        deleteCartDiscount(cartDiscount.getId(), cartDiscount.getVersion());
    }
    
    public static void withUpdateableCartDiscount(final UnaryOperator<CartDiscount> operator) {
        CartDiscount cartDiscount = createCartDiscount();
        cartDiscount = operator.apply(cartDiscount);
        deleteCartDiscount(cartDiscount.getId(), cartDiscount.getVersion());
    }
    
    public static CartDiscount createCartDiscount() {
        CartDiscountValue cartDiscountValue = CartDiscountValueRelativeBuilder.of()
                .permyriad(10L)
                .build();

        CartDiscountDraft cartDiscountDraft = CartDiscountDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .key(CommercetoolsTestUtils.randomKey())
                .value(cartDiscountValue)
                .cartPredicate("country=\"DE\"")
                .target(CartDiscountShippingCostTargetBuilder.of().build())
                .sortOrder("0.41")
                .requiresDiscountCode(true)
                .build();

        CartDiscount cartDiscount = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .cartDiscounts()
                .post(cartDiscountDraft)
                .executeBlocking();

        Assertions.assertNotNull(cartDiscount);
        Assertions.assertEquals(cartDiscountDraft.getKey(), cartDiscount.getKey());
        
        return cartDiscount;
    }
    
    public static CartDiscount deleteCartDiscount(final String id, final Long version) {
        CartDiscount deletedCartDiscount = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .cartDiscounts()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(deletedCartDiscount);
        
        return deletedCartDiscount;
    }
    
}
