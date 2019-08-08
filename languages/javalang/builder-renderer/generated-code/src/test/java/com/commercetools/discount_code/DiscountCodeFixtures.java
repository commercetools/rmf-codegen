package com.commercetools.discount_code;

import com.commercetools.cart_discount.CartDiscountFixtures;
import com.commercetools.client.ApiRoot;
import com.commercetools.models.CartDiscount.CartDiscount;
import com.commercetools.models.CartDiscount.CartDiscountReference;
import com.commercetools.models.CartDiscount.CartDiscountReferenceBuilder;
import com.commercetools.models.DiscountCode.DiscountCode;
import com.commercetools.models.DiscountCode.DiscountCodeDraft;
import com.commercetools.models.DiscountCode.DiscountCodeDraftBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class DiscountCodeFixtures {
    
    public static void withDiscountCode(final Consumer<DiscountCode> consumer) {
        DiscountCode discountCode = createDiscountCode();
        consumer.accept(discountCode);
        deleteDiscountCode(discountCode.getId(), discountCode.getVersion());
    }
    
    public static void withUpdateableDiscountCode(final UnaryOperator<DiscountCode> operator) {
        DiscountCode discountCode = createDiscountCode();
        discountCode = operator.apply(discountCode);
        deleteDiscountCode(discountCode.getId(), discountCode.getVersion());
    }
    
    public static DiscountCode createDiscountCode() {
        CartDiscount cartDiscount = CartDiscountFixtures.createCartDiscount();
        CartDiscountReference cartDiscountReference = CartDiscountReferenceBuilder.of()
                .id(cartDiscount.getId())
                .obj(cartDiscount)
                .build();
        
        DiscountCodeDraft discountCodeDraft = DiscountCodeDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .code(CommercetoolsTestUtils.randomString())
                .cartDiscounts(Arrays.asList(cartDiscountReference))
                .build();

        DiscountCode discountCode = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .discountCodes()
                .post(discountCodeDraft)
                .executeBlocking();

        Assert.assertNotNull(discountCode);
        Assert.assertEquals(discountCodeDraft.getCode(), discountCode.getCode());
        
        return discountCode;
    }
    
    public static DiscountCode deleteDiscountCode(final String id, final Long version) {
        DiscountCode discountCode = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .discountCodes()
                .withId(id)
                .delete()
                .addExpand("cartDiscounts[*]")
                .addVersion(version)
                .executeBlocking();

        discountCode.getCartDiscounts().forEach(cartDiscountReference -> {
            CartDiscountFixtures.deleteCartDiscount(cartDiscountReference.getId(), cartDiscountReference.getObj().getVersion()); 
        });
        
        Assert.assertNotNull(discountCode);
        Assert.assertEquals(discountCode.getId(), id);
        
        return discountCode;
    }
}
