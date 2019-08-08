package com.commercetools.discount_code;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.DiscountCode.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DiscountCodeIntegrationTests {
    
    @Test
    public void createAndDelete() {
        DiscountCode discountCode = DiscountCodeFixtures.createDiscountCode();
        DiscountCodeFixtures.deleteDiscountCode(discountCode.getId(), discountCode.getVersion());
    }
    
    @Test
    public void getById() {
        DiscountCodeFixtures.withDiscountCode(discountCode -> {
            DiscountCode queriedDiscountCode = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .discountCodes()
                    .withId(discountCode.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedDiscountCode);
            Assert.assertEquals(queriedDiscountCode.getId(), discountCode.getId());
        });
    }
    
    @Test
    public void query() {
        DiscountCodeFixtures.withDiscountCode(discountCode -> {
            DiscountCodePagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .discountCodes()
                    .get()
                    .addWhere("id=" + "\"" + discountCode.getId() + "\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), discountCode.getId());
        });
    }
    
    @Test
    public void updateById() {
        DiscountCodeFixtures.withUpdateableDiscountCode(discountCode -> {
            List<DiscountCodeUpdateAction> updateActions = new ArrayList<>();
            updateActions.add(DiscountCodeSetMaxApplicationsActionBuilder.of().maxApplications(10L).build());
            DiscountCode updatedDiscountCode = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .discountCodes()
                    .withId(discountCode.getId())
                    .post(DiscountCodeUpdateBuilder.of()
                        .actions(updateActions)
                        .version(discountCode.getVersion())
                        .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedDiscountCode);
            Assert.assertEquals(updatedDiscountCode.getMaxApplications(), Long.valueOf(10));
            
            return updatedDiscountCode;
        });
    }
    
}