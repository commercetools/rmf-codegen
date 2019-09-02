package com.commercetools.subscription;

import com.commercetools.client.ApiRoot;
import com.commercetools.models.Subscription.*;
import com.commercetools.utils.CommercetoolsTestUtils;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SubscriptionFixtures {
    
    public static void withSubscription(final Consumer<Subscription> consumer) {
        Subscription subscription = createSubscription();
        consumer.accept(subscription);
        deleteSubscription(subscription.getId(), subscription.getVersion());
    }
    
    public static void withUpdateableSubscription(final UnaryOperator<Subscription> operator) {
        Subscription subscription = createSubscription();
        subscription = operator.apply(subscription);
        deleteSubscription(subscription.getId(), subscription.getVersion());
    }
    
    public static Subscription createSubscription() {
        SubscriptionDraft subscriptionDraft = SubscriptionDraftBuilder.of()
                .key(CommercetoolsTestUtils.randomKey())
                //TODO connection string
                .destination(AzureServiceBusDestinationBuilder.of().connectionString("").build())
                .messages(Arrays.asList(MessageSubscriptionBuilder.of()
                        .resourceTypeId("review")
                        .build()))
                .build();
        
        Subscription subscription = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .subscriptions()
                .post(subscriptionDraft)
                .executeBlocking();

        Assertions.assertNotNull(subscription);
        
        return subscription;
    }
    
    public static Subscription deleteSubscription(final String id, final Long version) {
        Subscription subscription = ApiRoot.withProjectKey(CommercetoolsTestUtils.getProjectKey())
                .subscriptions()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking();

        Assertions.assertNotNull(subscription);
        
        return subscription;
    }
    
}
