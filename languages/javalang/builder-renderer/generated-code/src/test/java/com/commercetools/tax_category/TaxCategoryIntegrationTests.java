package com.commercetools.tax_category;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.TaxCategory.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TaxCategoryIntegrationTests {
    
    @Test
    public void createAndDelete() {
        TaxCategoryDraft taxCategoryDraft = TaxCategoryDraftBuilder.of()
                .name(CommercetoolsTestUtils.randomString())
                .key(CommercetoolsTestUtils.randomKey())
                .build();

        TaxCategory taxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .taxCategories()
                .post(taxCategoryDraft)
                .executeBlocking();

        Assert.assertNotNull(taxCategory);
        Assert.assertEquals(taxCategoryDraft.getName(), taxCategory.getName());
        Assert.assertEquals(taxCategoryDraft.getKey(), taxCategory.getKey());
        
        TaxCategory deletedTaxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .taxCategories()
                .withId(taxCategory.getId())
                .delete()
                .addVersion(taxCategory.getVersion())
                .executeBlocking();

        Assert.assertNotNull(deletedTaxCategory);
        Assert.assertEquals(deletedTaxCategory.getId(), taxCategory.getId());
    }
    
    @Test
    public void getById() {
        TaxCategoryFixtures.withTaxCategory(taxCategory -> {
            TaxCategory queriedTaxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .taxCategories()
                    .withId(taxCategory.getId())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedTaxCategory);
            Assert.assertEquals(queriedTaxCategory.getId(), taxCategory.getId());
        });
    }

    @Test
    public void getByKey() {
        TaxCategoryFixtures.withTaxCategory(taxCategory -> {
            TaxCategory queriedTaxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .taxCategories()
                    .withKey(taxCategory.getKey())
                    .get()
                    .executeBlocking();

            Assert.assertNotNull(queriedTaxCategory);
            Assert.assertEquals(queriedTaxCategory.getKey(), taxCategory.getKey());
        });
    }

    @Test
    public void query(){
        TaxCategoryFixtures.withTaxCategory(taxCategory -> {
            TaxCategoryPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .taxCategories()
                    .get()
                    .addWhere("id=" + "\"" + taxCategory.getId() +"\"")
                    .executeBlocking();

            Assert.assertNotNull(response);
            Assert.assertEquals(response.getResults().get(0).getId(), taxCategory.getId());
        });
    }

    @Test
    public void updateById(){
        TaxCategoryFixtures.withUpdateableTaxCategory(taxCategory -> {

            List<TaxCategoryUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(TaxCategorySetKeyActionBuilder.of().key(newKey).build());
            
            TaxCategory updatedTaxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .taxCategories()
                    .withId(taxCategory.getId())
                    .post(TaxCategoryUpdateBuilder.of()
                        .actions(updateActions)
                        .version(taxCategory.getVersion())
                        .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedTaxCategory);
            Assert.assertEquals(updatedTaxCategory.getKey(), newKey);
            
            return updatedTaxCategory;
        });
    }

    @Test
    public void updateByIKey(){
        TaxCategoryFixtures.withUpdateableTaxCategory(taxCategory -> {

            List<TaxCategoryUpdateAction> updateActions = new ArrayList<>();
            String newKey = CommercetoolsTestUtils.randomKey();
            updateActions.add(TaxCategorySetKeyActionBuilder.of().key(newKey).build());

            TaxCategory updatedTaxCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .taxCategories()
                    .withKey(taxCategory.getKey())
                    .post(TaxCategoryUpdateBuilder.of()
                            .actions(updateActions)
                            .version(taxCategory.getVersion())
                            .build())
                    .executeBlocking();

            Assert.assertNotNull(updatedTaxCategory);
            Assert.assertEquals(updatedTaxCategory.getKey(), newKey);

            return updatedTaxCategory;
        });
    }
    
}
