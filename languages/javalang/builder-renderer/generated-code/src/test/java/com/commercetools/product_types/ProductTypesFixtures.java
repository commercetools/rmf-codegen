package com.commercetools.product_types;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ProductType.ProductType;
import com.commercetools.models.ProductType.ProductTypeDraft;
import com.commercetools.models.ProductType.ProductTypeDraftBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ProductTypesFixtures {
    
    public static void withProductType(final Consumer<ProductType> consumer) {
        ProductType productType = createProductType();
        consumer.accept(productType);
        deleteProductType(productType.getId(), productType.getVersion());
    }
    
    public static void withUpdateableProductType(final UnaryOperator<ProductType> operator) {
        ProductType productType = createProductType();
        productType = operator.apply(productType);
        deleteProductType(productType.getId(), productType.getVersion());
    }
    
    public static ProductType createProductType() {
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
        
        return productType;
    }
    
    public static ProductType deleteProductType(final String id, final Long version) {
        ProductType productType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productTypes()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(productType);
        Assertions.assertEquals(productType.getId(), id);
        
        return productType;
    }
    
}
