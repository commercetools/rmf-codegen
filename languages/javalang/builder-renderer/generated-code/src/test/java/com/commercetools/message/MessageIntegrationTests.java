package com.commercetools.message;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Message.Message;
import com.commercetools.models.Message.MessagePagedQueryResponse;
import com.commercetools.models.Product.Product;
import com.commercetools.product.ProductFixtures;
import com.commercetools.product_type.ProductTypeFixtures;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageIntegrationTests {
    
    @Test
    public void query(){
        Product product = ProductFixtures.createProduct(ProductTypeFixtures.createProductType());
        ProductFixtures.deleteProductById(product.getId(), product.getVersion());
        
        MessagePagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .messages()
                .get()
                .executeBlocking();

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getResults().isEmpty());
    }    
    
    @Test
    public void getById() {
        Product product = ProductFixtures.createProduct(ProductTypeFixtures.createProductType());
        ProductFixtures.deleteProductById(product.getId(), product.getVersion());

        MessagePagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .messages()
                .get()
                .executeBlocking();
        
        String messageId = response.getResults().get(0).getId();
        Message message = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .messages()
                .withId(messageId)
                .get()
                .executeBlocking();

        Assertions.assertNotNull(message);
        Assertions.assertEquals(message.getId(), messageId);
    }
}
