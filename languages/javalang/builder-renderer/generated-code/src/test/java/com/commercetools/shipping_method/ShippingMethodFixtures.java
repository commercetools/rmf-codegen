package com.commercetools.shipping_method;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ShippingMethod.ShippingMethod;
import com.commercetools.models.ShippingMethod.ShippingMethodDraft;
import com.commercetools.models.ShippingMethod.ShippingMethodDraftBuilder;
import com.commercetools.models.TaxCategory.TaxCategory;
import com.commercetools.models.TaxCategory.TaxCategoryResourceIdentifierBuilder;
import com.commercetools.tax_category.TaxCategoryFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ShippingMethodFixtures {
    
    public static void withShippingMethod(final Consumer<ShippingMethod> consumer) {
        ShippingMethod shippingMethod = createShippingMethod();
        consumer.accept(shippingMethod);
        deleteShippingMethod(shippingMethod.getId(), shippingMethod.getVersion());
    }
   
    public static void withUpdateableShippingMethod(final UnaryOperator<ShippingMethod> operator) {
        ShippingMethod shippingMethod = createShippingMethod();
        shippingMethod = operator.apply(shippingMethod);
        deleteShippingMethod(shippingMethod.getId(), shippingMethod.getVersion());
    }
    
    public static ShippingMethod createShippingMethod() {
        TaxCategory taxCategory = TaxCategoryFixtures.createTaxCategory();
        ShippingMethodDraft shippingMethodDraft = ShippingMethodDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomString())
                .key(CommercetoolsTestUtils.randomKey())
                .taxCategory(TaxCategoryResourceIdentifierBuilder.of()
                    .id(taxCategory.getId())
                    .build())
                .build();

        ShippingMethod shippingMethod = CommercetoolsTestUtils.getApiRoot().withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .shippingMethods()
                .post(shippingMethodDraft)
                .executeBlocking();

        Assertions.assertNotNull(shippingMethod);
        Assertions.assertEquals(shippingMethodDraft.getKey(), shippingMethod.getKey());
        
        return shippingMethod;
    }
    
    public static ShippingMethod deleteShippingMethod(final String id, final Long version) {
        ShippingMethod shippingMethod = CommercetoolsTestUtils.getApiRoot().withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .shippingMethods()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(shippingMethod);
        Assertions.assertEquals(shippingMethod.getId(), id);
        
        return shippingMethod;
    }
    
}
