package com.commercetools;

import com.commercetools.models.Category.*;
import com.commercetools.models.Common.*;
import com.commercetools.models.Type.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class ModelSerializationTest {
    
    @Test
    public void serializeCategoryDraftToJson() {
        LocalizedString localizedString = new LocalizedStringImpl();
        localizedString.setValue("test-key", "test-value");
        String key = "test-key";
        String id = "test-id";
        String testString = "test-string";
        
        CategoryResourceIdentifier resourceIdentifier = new CategoryResourceIdentifierImpl();
        resourceIdentifier.setId(id);
        resourceIdentifier.setKey(key);

        FieldContainer fieldContainer = new FieldContainerImpl();
        fieldContainer.setValue(key, testString);

        AssetDimensions assetDimensions = new AssetDimensionsImpl();
        assetDimensions.setH(10);
        assetDimensions.setW(5);
        
        AssetSource assetSource = new AssetSourceImpl();
        assetSource.setContentType("application/json");
        assetSource.setDimensions(assetDimensions);
        
        AssetDraft assetDraft = AssetDraftBuilder.of()
                .description(localizedString)
                .key(key)
                .name(localizedString)
                .custom(CustomFieldsDraftBuilder.of()
                        .fields(fieldContainer)
                        .type("string type")
                        .build())
                .sources(Arrays.asList(assetSource))
                .tags(Arrays.asList("tag 1", "tag 2"))
                .build();
                
        CategoryDraft categoryDraft = CategoryDraftBuilder.of()
                .parent(resourceIdentifier)
                .assets(Arrays.asList(assetDraft))
                .custom(CustomFieldsDraftBuilder.of().type("string type").fields(fieldContainer).build())
                .description(localizedString)
                .externalId(id)
                .key(key)
                .metaDescription(localizedString)
                .metaKeywords(localizedString)
                .metaTitle(localizedString)
                .name(localizedString)
                .orderHint(testString)
                .slug(localizedString)
                .build();
        
        String categoryDraftJson = categoryDraft.toJson();
        
        try{
            final URL url = Thread.currentThread().getContextClassLoader().getResource("json_examples/category-draft-example.json");
            String categoryDraftExample = new String(Files.readAllBytes(Paths.get(url.getPath())));
            Assert.assertEquals(categoryDraftJson, categoryDraftExample);
        }catch (IOException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    public void serializeProductDraftToJson() {
        
    }
    
    @Test
    public void deserializeCategoryFromJson() {
        LocalizedString localizedString = new LocalizedStringImpl();
        localizedString.setValue("test-key", "test-value");
        String key = "test-key";
        String id = "test-id";
        String testString = "test-string";

        CategoryResourceIdentifier resourceIdentifier = new CategoryResourceIdentifierImpl();
        resourceIdentifier.setId(id);
        resourceIdentifier.setKey(key);

        FieldContainer fieldContainer = new FieldContainerImpl();
        fieldContainer.setValue(key, testString);

        AssetDimensions assetDimensions = new AssetDimensionsImpl();
        assetDimensions.setH(10);
        assetDimensions.setW(5);

        AssetSource assetSource = new AssetSourceImpl();
        assetSource.setContentType("application/json");
        assetSource.setDimensions(assetDimensions);

        CustomFields customFields = new CustomFieldsImpl();
        customFields.setFields(fieldContainer);
        TypeReference typeReference = TypeReference.of();
        typeReference.setKey(key);
        typeReference.setObj(Type.of());
        customFields.setType(typeReference);

        Asset asset = new AssetImpl();
        asset.setCustom(customFields);
        asset.setDescription(localizedString);
        asset.setId(id);
        asset.setKey(key);
        asset.setName(localizedString);
        asset.setCustom(customFields);
        asset.setSources(Arrays.asList(assetSource));
        asset.setTags(Arrays.asList("tag 1", "tag 2"));

        CategoryReference reference = new CategoryReferenceImpl();
        reference.setKey(key);
        reference.setId(id);
        reference.setObj(new CategoryImpl());

        Category category = new CategoryImpl();
        category.setKey(key);
        category.setId(id);
        category.setAncestors(Arrays.asList(reference));
        category.setAssets(Arrays.asList(asset));
        category.setCustom(customFields);
        category.setDescription(localizedString);
        category.setExternalId(id);
        category.setMetaDescription(localizedString);
        category.setMetaKeywords(localizedString);
        category.setMetaTitle(localizedString);
        category.setName(localizedString);
        category.setOrderHint(testString);
        category.setParent(reference);
        category.setSlug(localizedString);
        category.setCreatedAt(ZonedDateTime.of(2019, 12, 12, 12, 12, 12, 12, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1))));

        try{
            final URL url = Thread.currentThread().getContextClassLoader().getResource("json_examples/category-example.json");
            String categoryExampleJsonString = new String(Files.readAllBytes(Paths.get(url.getPath())));
            Category exampleCategory = Category.of(categoryExampleJsonString);
            Assert.assertEquals(category.toJson(), exampleCategory.toJson());
        }catch (IOException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    public void deserializeProductFromJson() throws Exception {
        
    }
}