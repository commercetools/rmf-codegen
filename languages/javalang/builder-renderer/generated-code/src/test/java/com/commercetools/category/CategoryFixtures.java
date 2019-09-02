package com.commercetools.category;

import com.commercetools.utils.CommercetoolsTestUtils;
import com.commercetools.client.ApiRoot;
import com.commercetools.models.Category.Category;
import com.commercetools.models.Category.CategoryDraft;
import com.commercetools.models.Category.CategoryDraftBuilder;
import com.commercetools.models.Common.AssetDraftBuilder;
import com.commercetools.models.Common.AssetSource;
import com.commercetools.models.Common.AssetSourceBuilder;
import com.commercetools.models.Common.LocalizedString;
import com.commercetools.models.Type.FieldContainer;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CategoryFixtures {
    
    public static void withCategory(final Consumer<Category> consumer) {
        Category category = createCategory();
        consumer.accept(category);
        deleteCategory(category.getId(), category.getVersion());
    }
    
    public static void withUpdateableCategory(final UnaryOperator<Category> operator){
        Category category = createCategory();
        category = operator.apply(category);
        deleteCategory(category.getId(), category.getVersion());
    }
    
    public static Category createCategory() {
        String key = CommercetoolsTestUtils.randomKey();
        String value = "value-" + CommercetoolsTestUtils.randomString();
        LocalizedString localizedString = LocalizedString.of();
        localizedString.setValue(key, value);
        FieldContainer fieldContainer = FieldContainer.of();
        fieldContainer.setValue(key, value);
        AssetSource assetSource = AssetSourceBuilder.of().uri("www.commercetools.com").build();
        CategoryDraft categoryDraft = CategoryDraftBuilder.of()
                .name(localizedString)
                .slug(localizedString)
                .assets(Arrays.asList(AssetDraftBuilder.of()
                        .key(CommercetoolsTestUtils.getProjectKey())
                        .name(localizedString)
                        .sources(Arrays.asList(assetSource))
                        .build()))
                .description(localizedString)
                .externalId(CommercetoolsTestUtils.randomId())
                .key(CommercetoolsTestUtils.randomKey())
                .metaDescription(localizedString)
                .metaKeywords(localizedString)
                .metaTitle(localizedString)
                .orderHint(CommercetoolsTestUtils.randomString())
                .build();

        return ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .categories()
                .post(categoryDraft)
                .executeBlocking();
    }
    
    public static Category deleteCategory(final String id, final Long version){
        return ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .categories()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();
    }
}
