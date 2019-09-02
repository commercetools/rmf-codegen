package com.commercetools.shopping_list;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.ShoppingList.ShoppingList;
import com.commercetools.models.ShoppingList.ShoppingListDraft;
import com.commercetools.models.ShoppingList.ShoppingListDraftBuilder;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ShoppingListFixtures { 
    
    public static void withShoppingList(final Consumer<ShoppingList> consumer) {
        ShoppingList shoppingList = createShoppingList();
        consumer.accept(shoppingList);
        deleteShoppingList(shoppingList.getId(), shoppingList.getVersion());
    }
    
    public static void withUpdateableShoppingList(final UnaryOperator<ShoppingList> operator) {
        ShoppingList shoppingList = createShoppingList();
        shoppingList = operator.apply(shoppingList);
        deleteShoppingList(shoppingList.getId(), shoppingList.getVersion());
    }
    
    public static ShoppingList createShoppingList() {
        ShoppingListDraft shoppingListDraft = ShoppingListDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                .name(CommercetoolsTestUtils.randomLocalizedString())
                .build();

        ShoppingList shoppingList = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .shoppingLists()
                .post(shoppingListDraft)
                .executeBlocking();

        Assertions.assertNotNull(shoppingList);
        Assertions.assertEquals(shoppingListDraft.getKey(), shoppingList.getKey());
        
        return shoppingList;
    }
    
    public static ShoppingList deleteShoppingList(final String id, final Long version) {
        ShoppingList shoppingList = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .shoppingLists()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(shoppingList);
        Assertions.assertEquals(shoppingList.getId(), id);
        
        return shoppingList;
    }
}