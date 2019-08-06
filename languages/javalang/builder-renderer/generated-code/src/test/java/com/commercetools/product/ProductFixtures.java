package com.commercetools.product;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Common.ReferenceTypeId;
import com.commercetools.models.Product.Product;
import com.commercetools.models.Product.ProductDraft;
import com.commercetools.models.Product.ProductDraftBuilder;
import com.commercetools.models.ProductType.ProductType;
import com.commercetools.models.ProductType.ProductTypeReferenceBuilder;
import com.commercetools.product.type.ProductTypeFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ProductFixtures {
    
    public static void withProduct(final Consumer<Product> consumer) {
        ProductTypeFixtures.withProductType(productType -> {
            Product product = createProduct(productType);
            consumer.accept(product);
            deleteProductById(product.getId(), product.getVersion());
        });
    }
    
    public static void withUpdateableProduct(final UnaryOperator<Product> operator) {
        ProductTypeFixtures.withProductType(productType -> {
            Product product = createProduct(productType);
            product = operator.apply(product);
            deleteProductById(product.getId(), product.getVersion());
        });
    }
    
    public static Product createProduct(final ProductType productType) {
        ProductDraft productDraft = ProductDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .slug(CommercetoolsTestUtils.randomLocalizedString())
                .productType(ProductTypeReferenceBuilder.of()
                        .typeId(ReferenceTypeId.PRODUCT_TYPE)
                        .id(productType.getId())
                        .obj(productType)
                        .build())
                .build();
        
        Product product = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .products()
                .post(productDraft)
                .executeBlocking();
        Assert.assertNotNull(product);
        Assert.assertEquals(product.getKey(), productDraft.getKey());
        
        return product;
    }
    
    public static Product deleteProductById(final String id, final Long version) {
        Product product = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .products()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();
        Assert.assertNotNull(product);
        Assert.assertEquals(product.getId(), id);
        return product;
    }
    
    public static Product deleteProductByKey(final String key, final Long version) {
        Product product = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .products()
                .withKey(key)
                .delete()
                .addVersion(version)
                .executeBlocking();
        Assert.assertNotNull(product);
        Assert.assertEquals(product.getKey(), key);
        return product;
    }
    
}
