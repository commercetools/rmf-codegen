package com.commercetools.request_errors;

import com.commercetools.category.CategoryFixtures;
import com.commercetools.client.ApiRoot;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryGetNonExisting {

    @Test
    public void getByNonExistingId() {
        CategoryFixtures.withCategory(category -> {
            Assertions.assertThrows(RuntimeException.class, () -> {
                CommercetoolsTestUtils.getApiRoot().withProjectKey(CommercetoolsTestUtils.getProjectKey())
                        .categories()
                        .withId("non-existing")
                        .get()
                        .executeBlocking();
            });
        });
    }
    
}
