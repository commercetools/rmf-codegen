package com.commercetools.category;

import com.commercetools.utils.CommercetoolsTestUtils;
import com.commercetools.client.ApiRoot;
import com.commercetools.models.Category.*;
import com.commercetools.models.Common.LocalizedString;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CategoryIntegrationTests {
    
    @Test
    public void create() {
        Category category = CategoryFixtures.createCategory();
        Assert.assertNotNull(category);
        Category deletedCategory = CategoryFixtures.deleteCategory(category.getId(), category.getVersion());
        Assert.assertEquals(category.getId(), deletedCategory.getId());
    }
    
    @Test
    public void getById() {
        CategoryFixtures.withCategory(category -> {
            Category queriedCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .categories()
                    .withId(category.getId())
                    .get()
                    .executeBlocking();
            Assert.assertEquals(category.getId(), queriedCategory.getId());
        });
    }
    
    @Test
    public void getByKey() {
        CategoryFixtures.withCategory(category -> {
            Category queriedCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .categories()
                    .withKey(category.getKey())
                    .get()
                    .executeBlocking();
            Assert.assertEquals(category.getId(), queriedCategory.getId());
            Assert.assertEquals(category.getKey(), queriedCategory.getKey());
        });
    }
    
    @Test
    public void deleteById() {
        Category category = CategoryFixtures.createCategory();
        Category deletedCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .categories()
                .withId(category.getId())
                .delete()
                .addVersion(category.getVersion())
                .executeBlocking();
        Assert.assertEquals(category.getId(), deletedCategory.getId());
    }
    
    @Test
    public void deleteByKey() {
        Category category = CategoryFixtures.createCategory();
        Category deletedCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .categories()
                .withKey(category.getKey())
                .delete()
                .addVersion(category.getVersion())
                .executeBlocking();
        Assert.assertEquals(category.getId(), deletedCategory.getId());
    }

    @Test
    public void queryCategories() {
        Category category = CategoryFixtures.createCategory();
        CategoryPagedQueryResponse response = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                .categories()
                .get()
                .addWhere("id=" + "\"" + category.getId() + "\"")
                .executeBlocking();
        Assert.assertEquals(response.getResults().size(), 1);
        Assert.assertEquals(response.getResults().get(0).getId(), category.getId());
        CategoryFixtures.deleteCategory(category.getId(), category.getVersion());
    }

    @Test
    public void updateCategory() {
        CategoryFixtures.withUpdateableCategory(category -> {
            List<CategoryUpdateAction> updateActions = new ArrayList<>();
            LocalizedString newName = LocalizedString.of();
            newName.setValue("key-Temp", "value-Temp");
            updateActions.add(CategoryChangeNameActionBuilder.of()
                    .name(newName)
                    .build());
            CategoryUpdate categoryUpdate = CategoryUpdateBuilder.of()
                    .version(category.getVersion())
                    .actions(updateActions)
                    .build();
            Category updatedCategory = ApiRoot.withProjectKeyValue(CommercetoolsTestUtils.getProjectKey())
                    .categories()
                    .withId(category.getId())
                    .post(categoryUpdate)
                    .executeBlocking();
            Assert.assertEquals(category.getId(), updatedCategory.getId());
            Assert.assertEquals(newName.values(), updatedCategory.getName().values());
            
            return updatedCategory;
        });
    }
}