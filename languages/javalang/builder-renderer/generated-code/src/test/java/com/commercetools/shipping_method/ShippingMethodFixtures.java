package com.commercetools.shipping_method;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ShippingMethod.ShippingMethod;
import com.commercetools.models.ShippingMethod.ShippingMethodDraft;
import com.commercetools.models.ShippingMethod.ShippingMethodDraftBuilder;
import com.commercetools.models.TaxCategory.TaxCategory;
import com.commercetools.models.TaxCategory.TaxCategoryReferenceBuilder;
import com.commercetools.tax_category.TaxCategoryFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;

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
                .taxCategory(TaxCategoryReferenceBuilder.of()
                    .id(taxCategory.getId())
                    .obj(taxCategory)
                    .build())
                .build();

        ShippingMethod shippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .shippingMethods()
                .post(shippingMethodDraft)
                .executeBlocking();

        Assert.assertNotNull(shippingMethod);
        Assert.assertEquals(shippingMethodDraft.getKey(), shippingMethod.getKey());
        
        return shippingMethod;
    }
    
    public static ShippingMethod deleteShippingMethod(final String id, final Long version) {
        ShippingMethod shippingMethod = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .shippingMethods()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assert.assertNotNull(shippingMethod);
        Assert.assertEquals(shippingMethod.getId(), id);
        
        return shippingMethod;
    }
    
}
