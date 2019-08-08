package com.commercetools.product_type;

import com.commercetools.utils.CommercetoolsTestUtils;
import com.commercetools.client.ApiRoot;
import com.commercetools.models.ProductType.ProductType;
import com.commercetools.models.ProductType.ProductTypeDraft;
import com.commercetools.models.ProductType.ProductTypeDraftBuilder;
import org.junit.Assert;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ProductTypeFixtures {
    
    public static void withProductType(final Consumer<ProductType> consumer){
        ProductType productType = createProductType();
        consumer.accept(productType);
        deleteProductType(productType.getId(), productType.getVersion());
    }
    
    public static void withUpdateableProductType(final UnaryOperator<ProductType> operator){
        ProductType productType = createProductType();
        productType = operator.apply(productType);
        deleteProductType(productType.getId(), productType.getVersion());
    }
    
    public static ProductType createProductType() {
        ProductTypeDraft productTypeDraft = ProductTypeDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .name(CommercetoolsTestUtils.randomString())
                .description(CommercetoolsTestUtils.randomString())
                .build();

        ProductType productType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productTypes()
                .post(productTypeDraft)
                .executeBlocking();

        Assert.assertNotNull(productType);
        Assert.assertEquals(productType.getName(), productTypeDraft.getName());
        Assert.assertEquals(productType.getDescription(), productTypeDraft.getDescription());
        
        return productType;
    }
    
    public static ProductType deleteProductType(final String id, final Long version) {
        ProductType deletedProductType = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .productTypes()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();
        
        Assert.assertNotNull(deletedProductType);
        Assert.assertEquals(deletedProductType.getId(), id);
        return deletedProductType;
    }
    
}
