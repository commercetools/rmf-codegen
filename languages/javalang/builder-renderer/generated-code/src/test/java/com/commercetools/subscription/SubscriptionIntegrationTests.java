package com.commercetools.subscription;

import com.commercetools.models.Subscription.Subscription;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubscriptionIntegrationTests {
    
    @Ignore
    @Test
    public void createAndDeleteById() {
        Subscription subscription = SubscriptionFixtures.createSubscription();
        Subscription deletedSubscription = SubscriptionFixtures.deleteSubscription(subscription.getId(), subscription.getVersion());

        Assertions.assertEquals(subscription.getId(), deletedSubscription.getId());
    }
    
    
}
